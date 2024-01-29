# HBase 删除数据
## 删除表格中的特定单元格
使用delete命令，可以删除表中的特定单元格。delete命令的语法如下：
```text
delete '<table name>', '<row>', '<column name>', '<time stamp>'
```

这是删除特定单元格的示例。这里我们要删除city。
```text
hbase(main):006:0> delete 'emp', '1', 'personal data:city'
0 row(s) in 0.0060 seconds
```

### 删除表中的所有单元格
使用deleteall命令，您可以删除一行中的所有单元格。下面给出的是deleteall命令的语法。
```text
deleteall '<table name>', '<row>'
```

这是deleteall命令的示例，其中我们删除了emp表的1的所有单元格。
```text
hbase(main):007:0> deleteall 'emp','1'
0 row(s) in 0.0240 seconds
```

使用scan命令验证表。下面给出了删除表后的表快照。
```text
hbase:013:0> scan 'emp'
ROW               COLUMN+CELL
2                 column=personal data:city, timestamp=2021-01-04T09:39:25.298, value=chennai
2                column=personal data:designation, timestamp=2021-01-04T09:39:56.897, value=sr.engineer
2                column=personal data:name, timestamp=2021-01-04T09:39:07.682, value=ravi
2                column=personal data:salary, timestamp=2021-01-04T09:40:13.935, value=30000
3                column=personal data:city, timestamp=2021-01-04T09:40:57.831, value=delhi
3                column=personal data:designation, timestamp=2021-01-04T09:41:27.262, value=jr.engineer
3                column=personal data:name, timestamp=2021-01-04T09:40:33.117, value=rajesh
3                 column=personal data:salary, timestamp=2021-01-04T09:41:46.733, value=25000
row1            column=personal data:city, timestamp=2021-01-04T11:24:23.707, value=Delih
row1             column=personal data:designation, timestamp=2021-01-04T10:48:33.531, value=manager
row1             column=personal data:name, timestamp=2021-01-04T10:48:33.531, value=raju
row1           column=personal data:salary, timestamp=2021-01-04T10:48:33.531, value=50000
3 row(s)
```

## 使用Java API删除数据
您可以使用Table类的delete()方法从HBase表中删除数据。请按照下面给出的步骤从表中删除数据。

### 步骤1：连接数据库
配置类将HBase配置文件添加到其对象。您可以使用HbaseConfiguration类的create()方法创建配置对象,并连接数据库，如下所示。
```text
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
```

### 步骤2：实例化Table类
您有一个名为Table的类，它是HBase中Table的实现。此类用于与单个HBase表进行通信。在实例化此类时，它接受配置对象和表名作为参数。您可以实例化Table类，如下所示。
```text
TableName tb = TableName.valueOf("emp");
// Instantiating Table class
Table table = connection.getTable(tb);
```

### 步骤3：实例化Delete类
通过以字节数组格式传递要删除的行的rowid来实例化Delete类。您还可以将时间戳记和行锁传递给此构造函数。
```text
Delete delete = new Delete(toBytes("row1"));
```

### 步骤4：选择要删除的数据
您可以使用Delete类的方法删除数据。此类具有各种删除方法。使用这些方法选择要删除的列或列族。
看下面的示例，这些示例显示Delete类方法的用法。
```text
// delete cell
delete.addColumn(Bytes.toBytes("personal data"), Bytes.toBytes("name"));
// delete column family
delete.addFamily(Bytes.toBytes("professional data"));
```

### 步骤5：删除数据
通过将delete实例传递给HTable类的delete()方法来删除所选数据，如下所示。
```text
table.delete(delete);
```

### 步骤6：关闭HTableInstance
删除数据后，关闭Table实例。
```text
table.close();
```

下面给出的是从HBase表删除数据的完整程序。
```text
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete; 
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes; 
public class DeleteData {
   public static void main(String[] args) throws IOException {
      // Instantiating Configuration class
      Configuration config = HBaseConfiguration.create();
      Connection connection = ConnectionFactory.createConnection(config);
      TableName tb = TableName.valueOf("emp");
      // Instantiating Table class
      Table table = connection.getTable(tb);
      // Instantiating delete class
      // accepts a row name.
      Delete delete = new Delete(Bytes.toBytes("row1"));
      // delete cell 
      delete.addColumn(Bytes.toBytes("personal data"), Bytes.toBytes("name"));
      // delete column family
      delete.addFamily(Bytes.toBytes("professional data"));
      // deleting the data
      table.delete(delete);
      // closing the HTable object
      table.close();
      System.out.println("data deleted.....");
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac DeleteData.java
$java DeleteData
```

以下应该是输出：
```text
data deleted
```
