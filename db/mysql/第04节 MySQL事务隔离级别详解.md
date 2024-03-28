# MySQL事务隔离级别详解


> 本文由 [SnailClimb](https://github.com/Snailclimb) 和 [guang19](https://github.com/guang19) 共同完成。

关于事务基本概览的介绍，请看这篇文章的介绍：[MySQL 常见知识点&面试题总结]。

## 事务隔离级别总结

SQL 标准定义了四个隔离级别：

- **READ-UNCOMMITTED(读取未提交)** ：
    最低的隔离级别，允许【读取尚未提交的数据变更】，可能会导致脏读、幻读或不可重复读。
- **READ-COMMITTED(读取已提交)** ：
    允许【读取并发事务已经提交的数据】，可以阻止脏读，但是幻读或不可重复读仍有可能发生。
- **REPEATABLE-READ(可重复读)** ：
    【对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改】，可以阻止脏读和不可重复读，但幻读仍有可能发生。
- **SERIALIZABLE(可串行化)** ：
    最高的隔离级别，完全服从ACID的隔离级别。【所有的事务依次逐个执行，事务之间就完全不可能产生干扰】，
    也就是说，该级别可以防止脏读、不可重复读以及幻读。



| 隔离级别             | 脏读  | 不可重复读 | 幻读  |
|------------------|-----|-------|-----|
| READ-UNCOMMITTED | √   | √     | √   |
| READ-COMMITTED   | ×   | √     | √   |
| REPEATABLE-READ  | ×   | ×     | √   |
| SERIALIZABLE     | ×   | ×     | ×   |

MySQL InnoDB存储引擎的默认支持的隔离级别是 **REPEATABLE-READ（可重读）**。
我们可以通过`SELECT @@tx_isolation;`命令来查看，MySQL 8.0该命令改为`SELECT @@transaction_isolation;`

```sql
MySQL> SELECT @@tx_isolation;
+-----------------+
| @@tx_isolation  |
+-----------------+
| REPEATABLE-READ |
+-----------------+
```

从上面对SQL标准定义了四个隔离级别的介绍可以看出，标准的SQL隔离级别定义里，REPEATABLE-READ(可重复读)是不可以防止幻读的。

但是！InnoDB实现的REPEATABLE-READ隔离级别其实是可以解决幻读问题发生的，主要有下面两种情况：
- **快照读**：由MVCC机制来保证不出现幻读。
- **当前读**：使用Next-Key Lock进行加锁来保证不出现幻读，Next-Key Lock是行锁（Record Lock）和间隙锁（Gap Lock）的结合，
    行锁只能锁住已经存在的行，为了避免插入新行，需要依赖间隙锁。

因为隔离级别越低，事务请求的锁越少，所以大部分数据库系统(比如Oracle)的隔离级别都是 **READ-COMMITTED** ，
但是你要知道的是InnoDB存储引擎默认使用 **REPEATABLE-READ** 并不会有任何性能损失。

InnoDB存储引擎在分布式事务的情况下一般会用到SERIALIZABLE隔离级别。

《MySQL 技术内幕：InnoDB 存储引擎(第 2 版)》7.7 章这样写到：

> InnoDB存储引擎提供了对XA事务的支持，并通过XA事务来支持分布式事务的实现。
> 分布式事务指的是允许多个独立的事务资源（transactional resources）参与到一个全局的事务中。
> 事务资源通常是关系型数据库系统，但也可以是其他类型的资源。
> 全局事务要求在其中的所有参与的事务要么都提交，要么都回滚，这对于事务原有的ACID要求又有了提高。
> 另外，在使用分布式事务时，InnoDB存储引擎的事务隔离级别必须设置为SERIALIZABLE。

## 实际情况演示

在下面我会使用2个命令行MySQL ，模拟多线程（多事务）对同一份数据的脏读问题。

MySQL命令行的默认配置中事务都是自动提交的，即执行SQL语句后就会马上执行COMMIT操作。
如果要显式地开启一个事务需要使用命令：`START TRANSACTION`。

我们可以通过下面的命令来设置隔离级别。

```sql
SET [SESSION|GLOBAL] TRANSACTION ISOLATION LEVEL [READ UNCOMMITTED|READ COMMITTED|REPEATABLE READ|SERIALIZABLE]
```

我们再来看一下我们在下面实际操作中使用到的一些并发控制语句:

- `START TRANSACTION` |`BEGIN`：显式地开启一个事务。
- `COMMIT`：提交事务，使得对数据库做的所有修改成为永久性。
- `ROLLBACK`：回滚会结束用户的事务，并撤销正在进行的所有未提交的修改。

### 脏读(读未提交)

![脏读](img/04/dirtyRead01.png)

### 避免脏读(读已提交)

![避免脏读](img/04/unrepeatableRead01.png)

### 不可重复读

还是刚才上面的读已提交的图，虽然避免了读未提交，但是却出现了，一个事务还没有结束，就发生了 不可重复读问题。

### 可重复读

![可重复读](img/04/repeatableRead01.png)

### 幻读

#### 演示幻读出现的情况

![幻读](img/04/phantomRead01.png)

SQL脚本1在第一次查询工资为500的记录时只有一条，SQL脚本2插入了一条工资为500的记录，提交之后；
SQL脚本1在同一个事务中再次使用当前读查询发现出现了两条工资为500的记录这种就是幻读。

#### 解决幻读的方法

解决幻读的方式有很多，但是它们的核心思想就是一个事务在操作某张表数据的时候，另外一个事务不允许新增或者删除这张表中的数据了。
解决幻读的方式主要有以下几种：

1. 将事务隔离级别调整为`SERIALIZABLE`。
2. 在可重复读的事务级别下，给事务操作的这张表添加表锁。
3. 在可重复读的事务级别下，给事务操作的这张表添加`Next-key Lock（Record Lock+Gap Lock）`。

### 参考

- 《MySQL 技术内幕：InnoDB 存储引擎》
- <https://dev.MySQL.com/doc/refman/5.7/en/>
- [Mysql 锁：灵魂七拷问](https://tech.youzan.com/seven-questions-about-the-lock-of-MySQL/)
- [Innodb 中的事务隔离级别和锁的关系](https://tech.meituan.com/2014/08/20/innodb-lock.html)

