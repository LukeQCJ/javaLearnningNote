# HBase 表描述和更改
## 描述
describe返回表的描述。其语法如下：
```text
hbase> describe 'table name'
```

下面给出的是emp表上describe命令的输出。
```text
hbase(main):006:0> describe 'emp'
Table emp is ENABLED  emp
COLUMN FAMILIES DESCRIPTION
{NAME => 'personal data', BLOOMFILTER => 'ROW', IN_MEMORY => 'false', VERSIONS => '1', KEEP_DELETED_CELLS => 'FA
LSE', DATA_BLOCK_ENCODING => 'NONE', COMPRESSION => 'NONE', TTL => 'FOREVER', MIN_VERSIONS => '0', BLOCKCACHE =>
'true', BLOCKSIZE => '65536', REPLICATION_SCOPE => '0'}
{NAME => 'professional data', BLOOMFILTER => 'ROW', IN_MEMORY => 'false', VERSIONS => '1', KEEP_DELETED_CELLS =>
'FALSE', DATA_BLOCK_ENCODING => 'NONE', COMPRESSION => 'NONE', TTL => 'FOREVER', MIN_VERSIONS => '0', BLOCKCACH
E => 'true', BLOCKSIZE => '65536', REPLICATION_SCOPE => '0'}
2 row(s)
Quota is disabled
Took 7.8766 seconds
```

## 更改
alter是用于**更改现有表**的命令。使用此命令，您可以更改列系列的最大单元数，设置和删除表范围运算符，以及从表中删除列系列。

### 更改列族的最大单元数
下面给出的是更改列族的最大单元数的语法。
```text
hbase> alter 't1', NAME => 'f1', VERSIONS => 5
```

在以下示例中，最大单元数设置为5。
```text
hbase(main):003:0> alter 'emp', NAME => 'personal data', VERSIONS => 5
Updating all regions with the new schema...
0/1 regions updated.
1/1 regions updated.
Done.
0 row(s) in 2.3050 seconds
```

### 表范围运算符
使用alter可以设置和删除表范围运算符，例如MAX_FILESIZE，READONLY，MEMSTORE_FLUSHSIZE，DEFERRED_LOG_FLUSH等。

### 设置只读
下面给出的是使表只读的语法。
```text
hbase> alter 't1', READONLY(option)
```

在以下示例中，我们使emp表为只读。
```text
hbase(main):006:0> alter 'emp', READONLY
Updating all regions with the new schema...
0/1 regions updated.
1/1 regions updated.
Done.
0 row(s) in 2.2140 seconds
```

### 删除表范围运算符
我们还可以删除表范围运算符。下面给出的是从emp表中删除MAX_FILESIZE的语法。
```text
hbase> alter 't1', METHOD => 'table_att_unset', NAME => 'MAX_FILESIZE'
```

### 删除列族
使用alter，还可以删除列族。下面给出的是使用alter删除列族的语法。
```text
hbase> alter 'table name ', 'delete' => 'column family'
```

以下是从emp表中删除列族的示例。
假设在HBase中有一个名为employee的表。它包含以下数据：
```text
hbase(main):006:0> scan 'employee'
ROW                   COLUMN+CELL
row1 column = personal:city, timestamp = 1418193767, value = hyderabad
row1 column = personal:name, timestamp = 1418193806767, value = raju
row1 column = professional:designation, timestamp = 1418193767, value = manager
row1 column = professional:salary, timestamp = 1418193806767, value = 50000
1 row(s) in 0.0160 seconds
```

现在，让我们使用alter命令删除名为professional的列族。
```text
hbase(main):007:0> alter 'employee','delete'⇒'professional'
Updating all regions with the new schema...
0/1 regions updated.
1/1 regions updated.
Done.
0 row(s) in 2.2380 seconds
```

现在，更改后验证表中的数据。观察列系列“专业”已不再存在，因为我们已将其删除。
```text
hbase(main):003:0> scan 'employee'
ROW             COLUMN + CELL
row1 column = personal:city, timestamp = 14181936767, value = hyderabad
row1 column = personal:name, timestamp = 1418193806767, value = raju
1 row(s) in 0.0830 seconds
```

## 使用Java API添加列族
您可以使用HBAseAdmin类的方法addColumn()把列族添加到表。请按照下面给出的步骤将列族添加到表中。

### 第1步
获得Admin类。
```text
// Instantiating configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
// Instantiating Admin class
Admin admin = null;
admin = connection.getAdmin();
```

### 第2步
上述addColumn()方法需要2个参数，一个是表名，一个是HColumnDescriptor类。
因此，需要实例化HColumnDescriptor类。
HColumnDescriptor的构造函数又需要添加列族名称。
在这里，我们将一个名为contactDetails的列族添加到现有的emp表中。
```text
// Instantiating columnDescriptor object
HColumnDescriptor columnDescriptor = new HColumnDescriptor("contactDetails");
```

第3步
使用addColumn方法添加列系列。将表名和HColumnDescriptor类对象作为参数传递给此方法。
```text
// Adding column family
admin.addColumn(TableName.valueOf("emp"), columnDescriptor);
```

下面给出的是将列族添加到现有表的完整程序。
```text
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.conf.Configuration;
@SuppressWarnings("deprecation")
public class AddColoumn{
  public static void main(String[] args) throws IOException {
      try {
         // Instantiating configuration class
         Configuration config = HBaseConfiguration.create();
         Connection connection = ConnectionFactory.createConnection(config);
         // Instantiating Admin class
         Admin admin = null;
         admin = connection.getAdmin();
         // Instantiating columnDescriptor class
         HColumnDescriptor columnDescriptor = new HColumnDescriptor("contactDetails");
         // Adding column family
         admin.addColumn(TableName.valueOf("emp"), columnDescriptor);
         System.out.println("coloumn added");
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac AddColumn.java
$java AddColumn
```

如果一切顺利，它将产生以下输出：
```text
column added
```

## 使用Java API删除列族
您可以使用该方法从表中删除列族deleteColumn（）的HBAseAdmin类。请按照下面给出的步骤将列族添加到表中。

### 第1步 Admin类。
```text
// Instantiating configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
// Instantiating Admin class
Admin admin = null;
admin = connection.getAdmin();
```

### 第2步 - 使用deleteColumn（）方法删除列族。将表名和列族名作为参数传递给此方法。
```text
// Deleting column family
String col = "contactDetails";
// delete column family
admin.deleteColumn(TableName.valueOf("emp"), col.getBytes());
```

下面给出的是从现有表中删除列族的完整程序。
```text
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.conf.Configuration;
@SuppressWarnings("deprecation")
public class DeleteColoumn{
   public static void main(String args[]) throws MasterNotRunningException, IOException{
      try {
         // Instantiating configuration class
         Configuration config = HBaseConfiguration.create();
         Connection connection = ConnectionFactory.createConnection(config);
         // Instantiating Admin class
         Admin admin = null;
         admin = connection.getAdmin();
         String col = "contactDetails";
         // delete column family
         admin.deleteColumn(TableName.valueOf("emp"), col.getBytes());
         System.out.println("coloumn deleted");
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac DeleteColumn.java
$java DeleteColumn
```

以下应该是输出：
```text
column deleted
```

## 使用Java API删除列族
您可以使用HBAseAdmin类的deleteColumn()方法从表中删除列族的。请按照下面给出的步骤将列族添加到表中。

### 第1步 获取Admin类。
```text
// Instantiating configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
// Instantiating Admin class
Admin admin = null;
admin = connection.getAdmin();
```

### 第2步 使用deleteColumn()方法删除列族。将表名和列族名作为参数传递给此方法。
```text
// Deleting column family
String col = "contactDetails";
// delete column family
admin.deleteColumn(TableName.valueOf("emp"), col.getBytes());
```

下面给出的是从现有表中删除列族的完整程序。
```text
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.conf.Configuration;
@SuppressWarnings("deprecation")
public class DeleteColumn{
   public static void main(String args[]) throws MasterNotRunningException, IOException{
      try {
         // Instantiating configuration class
         Configuration config = HBaseConfiguration.create();
         Connection connection = ConnectionFactory.createConnection(config);
         // Instantiating Admin class
         Admin admin = null;
         admin = connection.getAdmin();
         String col = "contactDetails";
         // delete column family
         admin.deleteColumn(TableName.valueOf("emp"), col.getBytes());
         System.out.println("column deleted");
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac DeleteColumn.java
$java DeleteColumn
```

以下应该是输出：
```text
column deleted
```
