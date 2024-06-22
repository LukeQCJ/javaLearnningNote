## 什么是临时表？
临时表是一种会话级别的数据库对象，它只存在于创建它的数据库连接活动期间。
与常规的持久表不同，临时表在连接关闭或服务器重启后自动消失。
在MySQL中，临时表通常用于存储中间计算结果或临时数据集。

另外，MySQL 中临时表不是全局的，是会话（session）级别的，它们只对创建它们的数据库连接（或会话）可见。
这意味着每个连接可以拥有自己的私有临时表，并且同名的临时表可以在不同的连接中独立存在而不会互相干扰。

当会话结束或连接关闭时，临时表会自动被删除，其他会话无法访问或者看到该临时表。
因此，即使多个用户同时执行相同的代码来创建同名的临时表，他们也不会相互影响，每个用户都将在其自己的会话中与自己的临时表交互。

这种设计允许在并发环境下安全地使用临时表，每个用户的操作都被隔离在自己的会话中，确保了数据操作的独立性和安全性。

## 创建临时表
在MySQL中，创建临时表非常类似于创建常规表，差异在于需要在CREATE TABLE语句中添加TEMPORARY关键字。

以下是基本的创建语法：
```text
CREATE TEMPORARY TABLE IF NOT EXISTS temp_table_name (
column1 datatype,
column2 datatype,
column3 datatype,
...
);
```

我们还可以通过SELECT语句将查询结果直接导入到临时表中，同时指定存储引擎（默认是InnoDB）：
```text
CREATE TEMPORARY TABLE temp_table_name
ENGINE = InnoDB AS
SELECT column1, column2
FROM existing_table_name
WHERE condition;
```

## 为临时表创建和删除索引
虽然临时表主要用于临时存储，但有时在其中创建索引可以显著提高查询性能。以下是创建和删除索引的命令示例：

创建索引：
```text
CREATE INDEX index_name ON temp_table_name (column_name);
```

删除索引：
```text
DROP INDEX index_name ON temp_table_name;
```

## 删除临时表
### 手动删除临时表
```text
DROP TEMPORARY TABLE IF EXISTS temp_table_name;
```
这个命令会安全地删除名为temp_table_name的临时表，即使有同名的持久表存在，它也不会被影响。

### 自动删除临时表
临时表会在以下情况下自动删除：
```text
1）会话结束：
当创建临时表的客户端会话（数据库连接）正常关闭时，临时表会自动被删除。
无论是因为客户端断开连接、执行了QUIT命令、还是连接由于其他原因被终止，这个会话所关联的所有临时表都将消失。

2）服务器重启：如果MySQL服务器实例被重启，那么所有现存的客户端会话将被终结，与它们相关的临时表也将随之丢失。
```

临时表设计为只存在于它们被创建的特定会话期间，一旦会话结束，这些表就不再需要，因此会被自动清理。
这意味着用户通常不需要手动删除临时表，除非他们希望在会话仍然活跃的时候就释放掉那些不再需要的资源。

## 临时表的作用和优势
### 1、查询优化
临时表能够改善复杂查询的性能和可读性。
例如，当需要执行多个依赖于前一个查询结果的SQL查询时，可以使用临时表来存储每个查询步骤的结果。
这样做减少了重复计算和提高了代码整洁度。

### 2、数据整合
在需要从多个数据源汇总数据时，临时表可以起到桥梁的作用。它们使得数据格式统一化、易于处理。

### 3、处理用户会话相关数据
在Web应用中，例如在线购物网站，临时表可用于存储用户会话期间的状态信息，比如购物车内容。

### 4、批量数据处理
在需要对大量数据进行更新或清理时，临时表可以作为缓冲层，减少直接对生产环境的影响。

例如：公司需要生成一个报告，其中包含来自销售和财务两个部门数据库的数据。
这两个数据库有不同的结构，使用临时表可以先将数据整合起来。

```text
-- 从销售数据库创建一个临时表
CREATE TEMPORARY TABLE temp_sales_data AS
SELECT product_id, SUM(quantity) AS total_quantity
FROM sales_database.sales
GROUP BY product_id;

-- 从财务数据库插入数据到临时表
INSERT INTO temp_sales_data (product_id, total_quantity)
SELECT product_code, SUM(sold_units) AS total_quantity
FROM finance_database.financial_records
GROUP BY product_code;

-- 使用整合后的数据生成报告
SELECT product_id, total_quantity
FROM temp_sales_data;
```

---

# MySQL临时表Temporary在存储过程中的使用

在MySQL中，存储过程是一组预编译的SQL语句，可以接受参数、执行特定任务并返回结果。
临时表（Temporary Table）是存储过程中常用的一种数据结构，用于在存储过程执行期间暂时存储数据。

本篇文章将详细介绍MySQL存储过程中临时表的使用。

## 一、临时表的创建
在存储过程中创建临时表可以使用CREATE TEMPORARY TABLE语句。
临时表只在当前会话有效，当会话结束或连接断开时，临时表将自动删除。

示例：
```text
CREATE TEMPORARY TABLE temp_table (
id INT,
name VARCHAR(255)
);
```

## 二、临时表的使用

### 1）插入数据
与普通表一样，我们可以使用INSERT INTO语句向临时表中插入数据。

示例：
```text
INSERT INTO temp_table (id, name) VALUES (1, 'John');
```

### 2）查询数据
使用SELECT语句可以从临时表中查询数据。

示例：
```text
SELECT * FROM temp_table;
```

### 3）更新数据
使用UPDATE语句可以更新临时表中的数据。

示例：
```text
UPDATE temp_table SET name = 'Jane' WHERE id = 1;
```

### 4）删除数据
使用DELETE FROM语句可以从临时表中删除数据。

示例：
```text
DELETE FROM temp_table WHERE id = 1;
```

## 三、注意事项
- 临时表在每个会话中都是唯一的，不能在不同会话中共享。
- 临时表在关闭连接时会自动删除，无需手动删除。
- 临时表中的数据只在当前会话可见，其他会话无法访问。
- 临时表在存储过程中可以反复使用，但只能在存储过程执行期间存在。存储过程结束后，临时表将被自动删除。
- 临时表可以提高查询性能，因为在存储过程中多次查询相同的数据时，可以先将数据存储在临时表中，
  然后直接从临时表中查询，避免重复查询操作。

