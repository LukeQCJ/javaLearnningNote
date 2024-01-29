# HBase 计数（count）和截断（truncate）
## count（计数）
您可以使用count命令来计数表的行数。其语法如下：
```text
count '<table name>'
```

删除第一行后，emp表将具有两行。如下所示进行验证。
```text
hbase(main):023:0> count 'emp'
2 row(s) in 0.090 seconds
⇒ 2
```

## truncate（截断）
此命令禁用删除并重新创建表。truncate的语法如下：
```text
hbase> truncate 'table name'
```

下面给出的是truncate命令的示例。在这里，我们已经截断了emp表。
```text
hbase(main):011:0> truncate 'emp'
Truncating 'one' table (it may take a while):
- Disabling table...
- Truncating table...
  0 row(s) in 1.5950 seconds
```

截断表后，使用scan命令进行验证。您将获得一个零行的表。
```text
hbase(main):017:0> scan 'emp'
ROW                  COLUMN + CELL
0 row(s) in 0.3110 seconds
```
