# HBase 客户端 API
本章介绍了用于HBase的Java客户端API，该API用于对HBase表执行CRUD操作。
HBase用Java编写，并具有Java本机API。因此，它提供了对数据处理语言（DML）的编程访问。

## HBase Configuration 类
将HBase配置文件添加到配置中。此类属于org.apache.hadoop.hbase包。

### 方法与说明
| 方法	                                                   | 说明                             |
|-------------------------------------------------------|--------------------------------|
| static org.apache.hadoop.conf.Configuration create()	 | 此方法使用HBase资源创建一个Configuration。 |

## HTable类
HTable是代表HBase表的HBase内部类。它是表的实现，用于与单个HBase表进行通信。该类属于org.apache.hadoop.hbase.client类。

### 构造函数
| 方法	                                                                              | 说明                          |
|----------------------------------------------------------------------------------|-----------------------------|
| HTable()                                                                         |                             |
| HTable(TableName tableName, ClusterConnection connection, ExecutorService pool)	 | 使用此构造函数，您可以创建一个对象来访问HBase表。 |

### 方法与说明
| 方法	                                                      | 说明                        |
|----------------------------------------------------------|---------------------------|
| void close()	                                            | 释放HTable的所有资源。            |
| void delete(Delete delete)	                              | 删除指定的单元格/行。               |
| boolean exists(Get get)	                                 | 使用此方法，可以测试表中是否存在由Get指定的列。 |
| Result get(Get get)	                                     | 从给定的行中检索某些单元格。            |
| org.apache.hadoop.conf.Configuration getConfiguration()	 | 返回此实例使用的Configuration对象。  |
| TableName getName()	                                     | 返回此表的表名实例。                |
| HTableDescriptor getTableDescriptor()	                   | 返回此表的表描述符。                |
| byte[] getTableName()	                                   | 返回此表的名称。                  |
| void put(Put put)	                                       | 使用此方法，您可以将数据插入表中。         |

## Put 类
此类用于对单个行执行Put操作。它属于org.apache.hadoop.hbase.client软件包。

### 构造函数
| 方法	                                                          | 说明                                |
|--------------------------------------------------------------|-----------------------------------|
| Put(byte[] row)	                                             | 使用此构造函数，可以为指定的行创建Put操作。           |
| Put(byte[] rowArray, int rowOffset, int rowLength)	          | 使用此构造函数，您可以复制传入的行键以保持本地状态。        |
| Put(byte[] rowArray, int rowOffset, int rowLength, long ts)	 | 使用此构造函数，您可以复制传入的行键以保持本地状态。        |
| Put(byte[] row, long ts)	                                    | 使用此构造函数，我们可以使用给定的时间戳为指定的行创建Put操作。 |

### 方法与说明
| 方法	                                                                     | 说明                           |
|-------------------------------------------------------------------------|------------------------------|
| Put add(byte[] family, byte[] qualifier, byte[] value)	                 | 将指定的列和值添加到此Put操作。            |
| Put add(byte[] family, byte[] qualifier, long ts, byte[] value)         | 将指定列和值以及指定时间戳作为其版本添加到此Put操作。 |
| Put add(byte[] family, ByteBuffer qualifier, long ts, ByteBuffer value) | 将指定列和值以及指定时间戳作为其版本添加到此Put操作。 |
| Put add(byte[] family, ByteBuffer qualifier, long ts, ByteBuffer value) | 将指定列和值以及指定时间戳作为其版本添加到此Put操作。 |

## Get 类
此类用于在单行上执行Get操作。此类属于org.apache.hadoop.hbase.client软件包。

### 构造函数
| 方法	              | 说明                      |
|------------------|-------------------------|
| Get(byte[] row)	 | 使用此构造函数，可以为指定的行创建Get操作。 |
| Get(Get get)     |                         |

### 方法与说明
| 方法	                                             | 说明               |
|-------------------------------------------------|------------------|
| Get addColumn(byte[] family, byte[] qualifier)	 | 使用指定的限定词从特定族检索列。 |
| Get addFamily(byte[] family)	                   | 检索指定族的所有列。       |

## Delete 类
此类用于在单行上执行Delete操作。
要删除整行，请使用要删除的行实例化Delete对象。此类属于org.apache.hadoop.hbase.client软件包。

### 构造函数
| 方法	                                                             | 说明                     |
|-----------------------------------------------------------------|------------------------|
| Delete(byte[] row)	                                             | 为指定的行创建一个Delete操作。     |
| Delete(byte[] rowArray, int rowOffset, int rowLength)	          | 为指定的行和时间戳创建一个Delete操作。 |
| Delete(byte[] rowArray, int rowOffset, int rowLength, long ts)	 | 为指定的行和时间戳创建一个Delete操作。 |
| Delete(byte[] row, long timestamp)	                             | 为指定的行和时间戳创建一个Delete操作。 |

### 方法与说明
| 方法	                                                                 | 说明                        |
|---------------------------------------------------------------------|---------------------------|
| Delete addColumn(byte[] family, byte[] qualifier)	                  | 删除指定列的最新版本。               |
| Delete addColumns(byte[] family, byte[] qualifier, long timestamp)	 | 删除时间戳小于或等于指定时间戳的指定列的所有版本。 |
| Delete addFamily(byte[] family)	                                    | 删除指定族的所有列的所有版本。           |
| Delete addFamily(byte[] family, long timestamp)                     | 删除时间戳小于或等于指定时间戳的指定族的所有列。  |

## Result 类
此类用于获取Get或Scan查询的单行结果。

### 构造函数
| 方法	       | 说明                                                              |
|-----------|-----------------------------------------------------------------|
| Result()	 | 使用此构造函数，您可以创建一个没有KeyValue有效负载的空Result； 如果调用raw Cells（），则返回null。 |

### 方法与说明
| 方法	                                               | 说明                     |
|---------------------------------------------------|------------------------|
| byte[] getValue(byte[] family, byte[] qualifier)	 | 此方法用于获取指定列的最新版本。       |
| byte[] getRow()	                                  | 此方法用于检索与创建此结果的行相对应的行键。 |