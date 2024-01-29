# HBase 管理员 API
HBase是用Java编写的，因此它提供了Java API与HBase通信。Java API是与HBase通信的最快方法。
下面给出的是所引用的java Admin API，涵盖了用于管理表的任务。

## 类 HBaseAdmin
HBaseAdmin 是代表Admin的类。
此类属于org.apache.hadoop.hbase.client软件包。使用此类，您可以**执行管理员的任务**。
您可以使用Connection.getAdmin() 方法获取Admin实例。

| 方法	                                                          | 说明                      |
|--------------------------------------------------------------|-------------------------|
| void createTable(HTableDescriptor desc)	                     | 创建一个新表。                 |
| void createTable(HTableDescriptor desc, byte[][] splitKeys)	 | 用指定的分割键定义的初始空区域集创建一个新表。 |
| void deleteColumn(byte[] tableName, String columnName)	      | 从表中删除列。                 |
| void deleteColumn(String tableName, String columnName)	      | 从表中删除列。                 |
| void deleteTable(String tableName)	                          | 删除表格。                   |

## 类 Descriptor
此类包含有关**HBase表的详细信息**，例如：

* 所有列族的描述符，
* 如果该表是目录表，
* 如果表是只读的，
* 记忆库的最大大小，
* 当应该发生区域分裂时，
* 与之相关的协处理器，等等。

**构造函数**

| 方法	                               | 说明                        |
|-----------------------------------|---------------------------|
| HTableDescriptor(TableName name)	 | 构造一个表描述符，指定一个TableName对象。 |

**方法与说明**

| 方法	                                                   | 说明           |
|-------------------------------------------------------|--------------|
| HTableDescriptor addFamily(HColumnDescriptor family)	 | 将列族添加到给定的描述符 |