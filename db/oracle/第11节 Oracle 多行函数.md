## 什么是多行函数（分组函数）？
* 分组函数作用于**一组数据**，并对一组数据**返回一个值**。
* 也叫，组函数，分组函数。
* 组函数会忽略空值：NVL函数使分组函数无法忽略空值。

## 常用的多行函数
* AVG
* COUNT
* MAX
* MIN
* SUM

## 多行函数示例

### 统计记录数 count()
示例：查询出所有员工的记录数
```text
select count(*) from emp;
```
注意：不建议使用count(*)，可以使用一个具体的列以免影响性能。
```text
select count(ename) from emp;
```

### 最小值查询 min()
示例：查询出来员工最低工资
```text
select min(sal) from emp;
```

### 最大值查询 max()
示例：查询出员工的最高工资
```text
select max(sal) from emp;
```

### 平均值查询 avg()
示例：查询出员工的平均工资
```text
select avg(sal) from emp;
```

### 求和函数 sum()
示例：查询出20号部分的员工的工资总和
```text
select sum(sal) from emp where emp.deptno=20;
```

## 分组数据
使用GROUP BY子句将表中的数据分成若干组。


示例：查询每个部分的人数
```text
select deptno, count(ename) from emp group by deptno;
```

示例：查询每个部分的人数
```text
select deptno, count(ename) from emp group by deptno;
```

示例：查询出每个部分的平均工资
```text
select deptno, avg(sal) from emp group by deptno;
```

示例：查询出来部门编号和部门下的人数
```text
select deptno, count(ename) from emp;
```
出错，报ORA-00937的错误：不是单组分组函数。

注意：
* 如果使用分组函数，SQL只可以把GROUP BY分组条件字段和分组函数查询出阿里，不能有其它字段。
* 如果使用分组函数，不使用GROUP BY 只可以查询出来分组函数的值

## 过滤分组数据
示例：查询出部分平均工资大于2000的部门
```text
select deptno, avg(sal) from emp
group by deptno 
having avg(sal)>2000;
```

## WHERE和HAVING的区别
最大区别在于：where后面不能有组函数
```text
select deptno, avg(sal) from emp
where avg(sal)>2000;
group by deptno 
```
报错：ORA-00934：group function is not allowed here.

## 多个列的分组
示例：按照部门不同的职位，统计工资的总和

分析：先按照第一个列分组，如果相同，再按第二个列分组，以此类推

```text
select deptno, job, sum(sal) from emp 
group by deptno,job;
```
