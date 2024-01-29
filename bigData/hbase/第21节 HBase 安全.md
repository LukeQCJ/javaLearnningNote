# HBase 安全
我们可以向HBase中的用户**授予**和**撤消**权限。
出于安全性目的，共有三个命令：grant，revoke和user_permission。

## grant
grant命令授予特定的权限，例如读取，写入，执行和管理上的表给某个用户。

grant命令的语法如下：
```text
hbase> grant <user> <permissions> [<table> [<column family> [<column; qualifier>]]
```

我们可以从RWXCA集中为用户授予零个或多个特权，其中

* R-表示读取特权。
* W-表示写特权。
* X-代表执行特权。
* C-表示创建特权。
* A-表示管理员特权。

下面给出的示例将所有特权授予名为hadoopdoc的用户。
```text
hbase(main):018:0> grant 'hadoopdoc', 'RWXCA'
```

## revoke
revoke 命令用于撤销表的用户的访问权限。其语法如下：
```text
hbase> revoke <user>
```

以下代码撤消了名为hadoopdoc的用户的所有权限。
```text
hbase(main):006:0> revoke 'hadoopdoc'
```

## user_permission
此命令用于列出特定表的所有权限。

user_permission的语法如下：
```text
hbase>user_permission 'tablename'
```

以下代码列出了’emp’表的所有用户权限。
```text
hbase(main):013:0> user_permission 'emp'
```
