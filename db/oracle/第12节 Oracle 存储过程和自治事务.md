## 一、事务和存储过程

在存储过程中如何使用事务？当需要在存储过程中同时执行多条添加、修改、删除SQL语句时，为了保证数据完整性，我们需要使用事务。
使用方式和在PL-SQL中非常相似，但也有一些区别。　　

```text
--带事务的存储过程
CREATE OR REPLACE PROCEDURE Account_zhuanzhang(fromuser NUMBER,touser NUMBER,money NUMBER) 
IS
BEGIN
    UPDATE account SET balance = balance - money WHERE id = fromuser;
    UPDATE account SET balance = balance + money WHERE id = touser;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
    dbms_output.put_line('转账失败');
    ROLLBACK;
END Account_zhuanzhang;
```


调用事务
```text
SELECT * FROM ACCOUNT;
DECLARE
    fromuser NUMBER := 1;
    touser NUMBER := 2;
    money NUMBER := &m;
BEGIN
    account_zhuanzhang(fromuser,touser,money);
END;
```

## 二、自主事务(自治事务)处理：

自主事务：是由另一个事务启动的独立事务处理。

自主事务处理可以暂停主事务处理，也就是处理自己存储过程内部的事务，当自主事务处理完之后会恢复主事务处理。

在某个事务中独立开辟一个事务，内部事务操作不会影响同一会话的外部事务未提交的内容，同时内部事务也不受外部事务影响。
例如，在A事务中开启一个b事务，A不管最后成功提交，还是失败会滚，b事务都能独立于A自己提交。

```text
PRAGMA AUTONOMOUS_TRANSACTION; --定义为自主事务，不受其他事务提交，回滚的影响
```

例：
```text
--自主事务：带参数添加部门信息，最后使用了回滚
CREATE OR REPLACE PROCEDURE PRO_DEPT_ADD(DEPTNO NUMBER,DNAME VARCHAR2,LOC VARCHAR2) 
AS
    PRAGMA AUTONOMOUS_TRANSACTION;  --定义为自主事务，不受其他事务提交，回滚影响
BEGIN
    INSERT INTO DEPT (DEPTNO, DNAME, LOC) VALUES (DEPTNO, DNAME, LOC);
    ROLLBACK;  --自主事务回滚操作，不影响主事务。
END;
```

```text
--主事务，添加部门信息，并调用带参数的自主事务，自己本身提交
CREATE OR REPLACE PROCEDURE PRO_DEPT_ADD2 
AS
BEGIN
    INSERT INTO DEPT (DEPTNO, DNAME, LOC) VALUES (60, 'test1', 'test2');
    PRO_DEPT_ADD(70, 'test', 'test');     
    -- 如果调用的事务回滚，如果不是自主事务当前存储过程中插入数据也要一起回滚。
    -- 但是添加了自主事务后，自主事务提交内容不会影响到当前存储过程
COMMIT;
END;
```

调用主事务：
```text
BEGIN
pro_dept_add2();  --调用完毕后，为60的部门插入成功，但是为70的部门信息回滚了。只有一条插入成功！
END;
```

## 总结自主事务：

1、自主事务处理结果的变化不依赖于主事务处理的状态或最终配置。

2、自主事务处理提交或回滚时，不影响主事务处理的结果。

3、自主事务提交一旦提交，该自主事务处理结果的变化对于其他事务处理就是课件的。
   这意味着，用于可以访问已更新的信息，无需等待主事务处理提交。

4、自主事务处理可以启动其它自主事务处理。


# Oracle 的自治事务

背景：最近在项目中遇到一个查询偶尔会卡三四秒，但是没有任何报错，查询的数据量很小，也不是每次都卡，比较奇怪。
最终发现是自治事务搞的鬼。

## 一、自治事务的含义

自治事务： AUTONOMOUS TRANSACTION

在某个事物中独立开辟一个事务，内部事务操作不会影响同一会话的外部事务未提交的内容，同时内部事务也不受外部事务影响。

例如在A事务中开启一个b事务，A不管最后成功提交，还是失败会滚，b事务都能独立于A自己提交。


## 二、自治事务的用法

1、存储过程的开头用PRAGMA AUTONOMOUS_TRANSACTION来声明自治事务。

2、自治事务结尾必须提交或回滚，否则报错：
```text
ORA-06519: active autonomous transaction detected and rolled back
```

3、 自治事务一般用于记录日志。不管业务（普通事务）成功提交或者失败回滚。日志（自治事务）都需要提交以记录必要的参数。


## 三、自治事务使用不当造成的死锁

最近在项目中遇到一个查询偶尔会卡三四秒，但是没有任何报错，查询的数据量很小，也不是每次都卡，比较奇怪。最终发现是自治事务搞的鬼。


外层事务=一个查询接口+一个自治事务B

一个查询接口A：先从临时表删除满足条件m的记录N1。然后在后续的内部自治事务中insert 一条记录N2，并提交。

一个自治事务B：从临时表删除满足条件m的记录N1，并提交。

最后外层大事务rollback。


1、当临时表没有对应删除的数据的时候，执行是正常的。

2、当临时表恰好有需要删除的数据时，那外层事务就把N1锁掉了，自治事务B再删除N1就会造成死锁。
   三秒后死锁报错，结束。外层事物继续执行。


## 四、自治事务使用建议

自治事务和普通事务最好业务分离，不要操作同一张表。最佳应用场景还是用于记录日志。