# MySQL InnoDB存储引擎对MVCC的实现

## 多版本并发控制 (Multi-Version Concurrency Control)

MVCC是一种并发控制机制，用于在多个并发事务同时读写数据库时保持数据的一致性和隔离性。
它是通过在每个数据行上维护多个版本的数据来实现的。
当一个事务要对数据库中的数据进行修改时，MVCC会为该事务创建一个数据快照，而不是直接修改实际的数据行。

### 读操作（SELECT）：

当一个事务执行读操作时，它会使用快照读取。
快照读取是基于事务开始时数据库中的状态创建的，因此，事务不会读取其他事务尚未提交的修改。

具体工作情况如下：
- 对于读取操作，事务会查找符合条件的数据行，并选择符合其事务开始时间的数据版本进行读取。
- 如果某个数据行有多个版本，事务会选择不晚于其开始时间的最新版本，确保事务只读取在它开始之前已经存在的数据。
- 事务读取的是快照数据，因此其他并发事务对数据行的修改不会影响当前事务的读取操作。

### 写操作（INSERT、UPDATE、DELETE）：

当一个事务执行写操作时，它会生成一个新的数据版本，并将修改后的数据写入数据库。

具体工作情况如下：
- 对于写操作，事务会为要修改的数据行创建一个新的版本，并将修改后的数据写入新版本。
- 新版本的数据会带有当前事务的版本号，以便其他事务能够正确读取相应版本的数据。
- 原始版本的数据仍然存在，供其他事务使用快照读取，这保证了其他事务不受当前事务的写操作影响。

### 事务提交和回滚：

- 当一个事务提交时，它所做的修改将成为数据库的最新版本，并且对其他事务可见。
- 当一个事务回滚时，它所做的修改将被撤销，对其他事务不可见。

### 版本的回收：

为了防止数据库中的版本无限增长，MVCC会定期进行版本的回收。回收机制会删除已经不再需要的旧版本数据，从而释放空间。

MVCC 通过创建数据的多个版本和使用快照读取来实现并发控制。
读操作使用旧版本数据的快照，写操作创建新版本，并确保原始版本仍然可用。
这样，不同的事务可以在一定程度上并发执行，而不会相互干扰，从而提高了数据库的并发性能和数据一致性。

## 一致性非锁定读和锁定读

### 一致性非锁定读

对于[**一致性非锁定读（Consistent NonLocking Reads）**](https://dev.mysql.com/doc/refman/5.7/en/innodb-consistent-read.html)的实现，
通常做法是加一个版本号或者时间戳字段，在更新数据的同时版本号 + 1 或者更新时间戳。
查询时，将当前可见的版本号与对应记录的版本号进行比对，如果记录的版本小于可见版本，则表示该记录可见。

在`InnoDB`存储引擎中，[多版本控制 (multi versioning)](https://dev.mysql.com/doc/refman/5.7/en/innodb-multi-versioning.html)就是对非锁定读的实现。
如果读取的行正在执行`DELETE`或`UPDATE`操作，这时读取操作不会去等待行上锁的释放。
相反地，`InnoDB`存储引擎会去读取行的一个快照数据，对于这种读取历史数据的方式，我们叫它快照读(snapshot read)。

在`Repeatable Read`和`Read Committed`两个隔离级别下，
如果是执行普通的`select`语句（不包括`select ... lock in share mode`,`select ... for update`）
则会使用`一致性非锁定读（MVCC）`。并且在`Repeatable Read`下`MVCC`实现了可重复读和防止部分幻读。

### 锁定读

如果执行的是下列语句，就是 [**锁定读（Locking Reads）**](https://dev.mysql.com/doc/refman/5.7/en/innodb-locking-reads.html)

- `select ... lock in share mode`
- `select ... for update`
- `insert`、`update`、`delete` 操作

在锁定读下，读取的是数据的最新版本，这种读也被称为`当前读（current read）`。

锁定读会对读取到的记录加锁：
- `select ... lock in share mode`：对记录加`S`锁，其它事务也可以加`S`锁，如果加`x`锁则会被阻塞；
- `select ... for update`、`insert`、`update`、`delete`：对记录加`X`锁，且其它事务不能加任何锁；

在一致性非锁定读下，即使读取的记录已被其它事务加上`X`锁，这时记录也是可以被读取的，即读取的快照数据。
上面说了，在`Repeatable Read`下`MVCC`防止了部分幻读，这边的“部分”是指在`一致性非锁定读`情况下，
只能读取到第一次查询之前所插入的数据（根据Read View判断数据可见性，Read View在第一次查询时生成）。
但是！如果是`当前读`，每次读取的都是最新数据，这时如果两次查询中间有其它事务插入数据，就会产生幻读。
所以，**`InnoDB`在实现`Repeatable Read`时，如果执行的是当前读，则会对读取的记录使用`Next-key Lock`，来防止其它事务在间隙间插入数据**。

## InnoDB 对 MVCC 的实现

`MVCC`的实现依赖于：**隐藏字段、Read View、undo log**。

在内部实现中，`InnoDB`通过数据行的`DB_TRX_ID`和`Read View`来判断数据的可见性，
如不可见，则通过数据行的`DB_ROLL_PTR`找到`undo log`中的历史版本。
每个事务读到的数据版本可能是不一样的，在同一个事务中，用户只能看到该事务创建`Read View`之前已经提交的修改和该事务本身做的修改。

### 隐藏字段

在内部，`InnoDB`存储引擎为每行数据添加了三个 [隐藏字段](https://dev.mysql.com/doc/refman/5.7/en/innodb-multi-versioning.html)：
- `DB_TRX_ID（6字节）`：表示最后一次插入或更新该行的事务id。
    此外，`delete`操作在内部被视为更新，只不过会在记录头`Record header`中的`deleted_flag`字段将其标记为已删除。
- `DB_ROLL_PTR（7字节）`：回滚指针，指向该行的`undo log`。如果该行未被更新，则为空。
- `DB_ROW_ID（6字节）`：如果没有设置主键且该表没有唯一非空索引时，`InnoDB`会使用该id来生成聚簇索引

### ReadView

```c
class ReadView {
  /* ... */
private:
  trx_id_t m_low_limit_id;      /* 大于等于这个 ID 的事务均不可见 */

  trx_id_t m_up_limit_id;       /* 小于这个 ID 的事务均可见 */

  trx_id_t m_creator_trx_id;    /* 创建该 Read View 的事务ID */

  trx_id_t m_low_limit_no;      /* 事务 Number, 小于该 Number 的 Undo Logs 均可以被 Purge */

  ids_t m_ids;                  /* 创建 Read View 时的活跃事务列表 */

  m_closed;                     /* 标记 Read View 是否 close */
}
```

[`Read View`](https://github.com/facebook/mysql-8.0/blob/8.0/storage/innobase/include/read0types.h#L298) 
主要是用来做可见性判断，里面保存了“当前对本事务不可见的其他活跃事务”。

主要有以下字段：
- `m_low_limit_id`：目前出现过的最大的事务ID+1，即下一个将被分配的事务ID。大于等于这个ID的数据版本均不可见。
- `m_up_limit_id`：活跃事务列表`m_ids`中最小的事务ID，如果`m_ids`为空，则`m_up_limit_id`为`m_low_limit_id`。
    小于这个ID的数据版本均可见。
- `m_ids`：`Read View` 创建时其他未提交的活跃事务ID列表。
    创建`Read View`时，将当前未提交事务ID记录下来，后续即使它们修改了记录行的值，对于当前事务也是不可见的。
    `m_ids`不包括当前事务自己和已提交的事务（正在内存中）。
- `m_creator_trx_id`：创建该`Read View`的事务ID。

**事务可见性示意图**（[图源](https://leviathan.vip/2019/03/20/InnoDB%E7%9A%84%E4%BA%8B%E5%8A%A1%E5%88%86%E6%9E%90-MVCC/#MVCC-1)）：

![事务的可见性](img/06/trans_visible.png)

### undo-log

`undo log`主要有两个作用：
- 当事务回滚时用于将数据恢复到修改前的样子。
- 另一个作用是`MVCC`，当读取记录时，若该记录被其他事务占用或当前版本对该事务不可见，
- 则可以通过`undo log`读取之前的版本数据，以此实现非锁定读。

**在`InnoDB`存储引擎中`undo log`分为两种：`insert undo log`和`update undo log`：**

1. **`insert undo log`**：
    指在`insert`操作中产生的`undo log`。
    因为`insert`操作的记录只对事务本身可见，对其他事务不可见，故该`undo log`可以在事务提交后直接删除，不需要进行`purge`操作。

    **`insert`时的数据初始状态：**

    ![insert数据状态](img/06/insertDataState01.png)

2. **`update undo log`**：
    `update`或`delete`操作中产生的`undo log`。
    该`undo log`可能需要提供`MVCC`机制，因此不能在事务提交时就进行删除。
    提交时放入`undo log`链表，等待`purge线程`进行最后的删除。

    **数据第一次被修改时：**

    ![update数据状态](img/06/updateDataState01.png)

    **数据第二次被修改时：**

    ![update数据状态02](img/06/updateDataState02.png)

不同事务或者相同事务的对同一记录行的修改，会使该记录行的`undo log`成为一条链表，链首就是最新的记录，链尾就是最早的旧记录。

### 数据可见性算法

在`InnoDB`存储引擎中，创建一个新事务后，执行每个`select`语句前，都会创建一个快照（Read View），
**快照中保存了当前数据库系统中正处于活跃（没有commit）的事务的ID号**。
其实简单的说，保存的是系统中当前不应该被本事务看到的其他事务ID列表（即m_ids）。
当用户在这个事务中要读取某个记录行的时候，`InnoDB`会将该记录行的`DB_TRX_ID`与`Read View`中的一些变量及当前事务ID进行比较，
判断是否满足可见性条件。

[具体的比较算法](https://github.com/facebook/mysql-8.0/blob/8.0/storage/innobase/include/read0types.h#L161)如下([图源](https://leviathan.vip/2019/03/20/InnoDB%E7%9A%84%E4%BA%8B%E5%8A%A1%E5%88%86%E6%9E%90-MVCC/#MVCC-1))：

![事务Id可见性对比](img/06/transactionIdVisibleCompare01.png)

1. 如果记录DB_TRX_ID < m_up_limit_id，那么表明最新修改该行的事务（DB_TRX_ID）在当前事务创建快照之前就提交了，
    所以该记录行的值对当前事务是可见的。
2. 如果DB_TRX_ID >= m_low_limit_id，那么表明最新修改该行的事务（DB_TRX_ID）在当前事务创建快照之后才修改该行，
    所以该记录行的值对当前事务不可见，跳到步骤5。
3. m_ids为空，则表明在当前事务创建快照之前，修改该行的事务就已经提交了，所以该记录行的值对当前事务是可见的。
4. 如果m_up_limit_id <= DB_TRX_ID < m_low_limit_id，
    表明最新修改该行的事务（DB_TRX_ID）在当前事务创建快照的时候可能处于“活动状态”或者“已提交状态”，
    所以就要对活跃事务列表m_ids进行查找（源码中是用的二分查找，因为是有序的）。
    - 如果在活跃事务列表m_ids中能找到DB_TRX_ID，表明：
       - ① 在当前事务创建快照前，该记录行的值被事务ID为DB_TRX_ID的事务修改了，但没有提交；
       - ② 在当前事务创建快照后，该记录行的值被事务ID为DB_TRX_ID的事务修改了。
       
      这些情况下，这个记录行的值对当前事务都是不可见的。跳到步骤5。

    - 在活跃事务列表中找不到，则表明“id为trx_id的事务”在修改“该记录行的值”后，在“当前事务”创建快照前就已经提交了，
       所以记录行对当前事务可见。
5. 在该记录行的DB_ROLL_PTR指针所指向的`undo log`取出快照记录，用快照记录的DB_TRX_ID跳到步骤1重新开始判断，
    直到找到满足的快照版本或返回空。

## RC 和 RR 隔离级别下 MVCC 的差异

在事务隔离级别`RC`和`RR`（InnoDB存储引擎的默认事务隔离级别）下，`InnoDB`存储引擎使用`MVCC`（非锁定一致性读），
但它们生成`Read View`的时机却不同。

- 在 RC 隔离级别下的 **`每次select`** 查询前都生成一个`Read View`(m_ids列表)
- 在 RR 隔离级别下只在事务开始后 **`第一次select`** 数据前生成一个`Read View`(m_ids列表)

## MVCC 解决不可重复读问题

虽然 RC 和 RR 都通过 `MVCC` 来读取快照数据，但由于 **生成 Read View 时机不同**，从而在 RR 级别下实现可重复读。

举个例子：

![mvcc示例](img/06/mvccDemo01.png)

### 在 RC 下 ReadView 生成情况

**1. 假设时间线来到 T4 ，那么此时数据行 id = 1 的版本链为：**

![readView示例RC01](img/06/readViewRCDemo01.png)

由于RC级别下每次查询都会生成`Read View`，并且事务101、102并未提交，
此时`103`事务生成的`Read View`中活跃的事务**`m_ids`为：[101,102]** ，
`m_low_limit_id`为：104，`m_up_limit_id`为：101，`m_creator_trx_id` 为：103。

- 此时最新记录的`DB_TRX_ID`为101，m_up_limit_id <= 101 < m_low_limit_id，
    所以要在`m_ids`列表中查找，发现`DB_TRX_ID`存在列表中，那么这个记录不可见。
- 根据`DB_ROLL_PTR`找到`undo log`中的上一版本记录，上一条记录的`DB_TRX_ID`还是101，不可见。
- 继续找上一条`DB_TRX_ID`为1，满足1 < m_up_limit_id，可见，所以事务103查询到数据为`name = 菜花`。

**2. 时间线来到 T6 ，数据的版本链为：**

![readView示例RC02](img/06/readViewRCDemo02.png)

因为在RC级别下，重新生成`Read View`，这时事务101已经提交，102并未提交，所以此时`Read View`中活跃的事务 **`m_ids`：[102]** ，
`m_low_limit_id`为：104，`m_up_limit_id`为：102，`m_creator_trx_id`为：103。

- 此时最新记录的`DB_TRX_ID`为102，m_up_limit_id <= 102 < m_low_limit_id，所以要在`m_ids`列表中查找，
  发现`DB_TRX_ID` 存在列表中，那么这个记录不可见。
- 根据`DB_ROLL_PTR`找到`undo log`中的上一版本记录，上一条记录的`DB_TRX_ID` 为101，
  满足 101 < m_up_limit_id，记录可见，所以在`T6`时间点查询到数据为`name = 李四`，与时间T4查询到的结果不一致，不可重复读！

**3. 时间线来到 T9 ，数据的版本链为：**

![readView示例RC03](img/06/readViewRCDemo03.png)

重新生成`Read View`， 这时事务101和102都已经提交，所以 **m_ids** 为空，
则 m_up_limit_id = m_low_limit_id = 104，最新版本事务ID为102，
满足 102 < m_low_limit_id，可见，查询结果为 `name = 赵六`。

> **总结：** **在 RC 隔离级别下，事务在每次查询开始时都会生成并设置新的Read View，所以导致不可重复读**

### 在 RR 下 ReadView 生成情况

在可重复读级别下，只会在事务开始后第一次读取数据时生成一个Read View（m_ids列表）。

**1. 在 T4 情况下的版本链为：**

![readView示例RR01](img/06/readViewRRDemo01.png)

在当前执行`select`语句时生成一个`Read View`，此时 **`m_ids`：[101,102]** ，
`m_low_limit_id`为：104，`m_up_limit_id`为：101，`m_creator_trx_id` 为：103。

此时和 RC 级别下一样：
- 最新记录的`DB_TRX_ID`为101，m_up_limit_id <= 101 < m_low_limit_id，
    所以要在`m_ids`列表中查找，发现 `DB_TRX_ID` 存在列表中，那么这个记录不可见。
- 根据`DB_ROLL_PTR`找到`undo log`中的上一版本记录，上一条记录的`DB_TRX_ID`还是101，不可见。
- 继续找上一条`DB_TRX_ID`为1，满足 1 < m_up_limit_id，可见，所以事务103查询到数据为`name = 菜花`。

**2. 时间点 T6 情况下：**

![readView示例RR02](img/06/readViewRRDemo02.png)

在 RR 级别下只会生成一次`Read View`，所以此时依然沿用 **`m_ids`：[101,102]** ，
`m_low_limit_id`为：104，`m_up_limit_id`为：101，`m_creator_trx_id` 为：103。

- 最新记录的`DB_TRX_ID`为102，m_up_limit_id <= 102 < m_low_limit_id，
    所以要在`m_ids`列表中查找，发现`DB_TRX_ID`存在列表中，那么这个记录不可见。
- 根据`DB_ROLL_PTR`找到`undo log`中的上一版本记录，上一条记录的`DB_TRX_ID`为101，不可见。
- 继续根据`DB_ROLL_PTR`找到`undo log`中的上一版本记录，上一条记录的`DB_TRX_ID`还是101，不可见。
- 继续找上一条`DB_TRX_ID`为1，满足1 < m_up_limit_id，可见，所以事务103查询到数据为`name = 菜花`。

**3. 时间点 T9 情况下：**

![readView示例RR03](img/06/readViewRRDemo03.png)

此时情况跟 T6 完全一样，由于已经生成了 `Read View`，此时依然沿用 **`m_ids`：[101,102]** ，所以查询结果依然是 `name = 菜花`。

## MVCC + Next-key-Lock 防止幻读

`InnoDB`存储引擎在 RR 级别下通过 `MVCC`和 `Next-key Lock` 来解决幻读问题：

**1、执行普通`select`，此时会以`MVCC`快照读的方式读取数据**

在快照读的情况下，RR隔离级别只会在事务开启后的第一次查询生成`Read View`，并使用至事务提交。
所以，在生成`Read View`之后其它事务所做的更新、插入记录版本对当前事务并不可见，实现了可重复读和防止快照读下的“幻读”。

**2、执行 select...for update/lock in share mode、insert、update、delete 等当前读(锁定读)**

在当前读下，读取的都是最新的数据，如果其它事务有插入新的记录，并且刚好在当前事务查询范围内，就会产生幻读！
`InnoDB`使用 [Next-key Lock](https://dev.mysql.com/doc/refman/5.7/en/innodb-locking.html#innodb-next-key-locks) 来防止这种情况。
当执行当前读时，会锁定读取到的记录的同时，锁定它们的间隙，防止其它事务在查询范围内插入数据。
只要我不让你插入，就不会发生幻读。

## 参考

- **《MySQL 技术内幕 InnoDB 存储引擎第 2 版》**
- [Innodb 中的事务隔离级别和锁的关系](https://tech.meituan.com/2014/08/20/innodb-lock.html)
- [MySQL 事务与 MVCC 如何实现的隔离级别](https://blog.csdn.net/qq_35190492/article/details/109044141)
- [InnoDB 事务分析-MVCC](https://leviathan.vip/2019/03/20/InnoDB%E7%9A%84%E4%BA%8B%E5%8A%A1%E5%88%86%E6%9E%90-MVCC/)

