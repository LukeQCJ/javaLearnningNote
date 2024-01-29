# HBase 更新数据
## 使用HBase Shell更新数据
您可以使用put命令更新现有的单元格值。为此，只需遵循相同的语法并提及您的新值，如下所示。
```text
put 'table name','row','Column family:column name','new value'
```

新给定的值将替换现有值，从而更新行。

假设在HBase中有一个名为emp的表，其中包含以下数据。
```text
hbase(main):003:0> scan 'emp'
ROW              COLUMN + CELL
row1 column = personal:name, timestamp = 1418051555, value = raju
row1 column = personal:city, timestamp = 1418275907, value = Hyderabad
row1 column = professional:designation, timestamp = 14180555,value = manager
row1 column = professional:salary, timestamp = 1418035791555,value = 50000
1 row(s) in 0.0100 seconds
```

以下命令会将名为raju的员工的city值更新为Delhi。
```text
hbase(main):002:0> put 'emp','row1','personal:city','Delhi'
0 row(s) in 0.0400 seconds
```

更新后的表格如下所示，您可以在其中观察raju市已更改为Delhi。
```text
hbase(main):003:0> scan 'emp'
ROW          COLUMN + CELL
row1 column = personal:name, timestamp = 1418035791555, value = raju
row1 column = personal:city, timestamp = 1418274645907, value = Delhi
row1 column = professional:designation, timestamp = 141857555,value = manager
row1 column = professional:salary, timestamp = 1418039555, value = 50000
1 row(s) in 0.0100 seconds
```

## 使用Java API更新数据
您可以使用Put类的add()方法将数据更新Hbase。您可以使用HTable类的put()方法保存它。
这些类属于org.apache.hadoop.hbase.client软件包。
下面给出了在HBase表中更新数据的步骤。

### 步骤1：连接
该配置类增加了HBase的配置文件，它的对象。您可以使用HbaseConfiguration类的create()方法创建配置对象，如下所示。
```text
// Instantiating Configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
```

### 步骤2：实例化Table类
您有一个名为HTable的类，它是HBase中Table的实现。此类用于与单个HBase表进行通信。
在实例化此类时，它接受配置对象和表名称作为参数。您可以实例化HTable类，如下所示。
```text
TableName tb = TableName.valueOf("emp");
// Instantiating HTable class
Table table = connection.getTable(tb);
```

### 步骤3：实例化Put类
要将数据更新到HBase表中，请使用add()方法及其变体。此方法属于Put，因此实例化put类。
此类要求您要以字符串格式将数据更新到的行名。您可以如下所示实例化Put类。
```text
Put p = new Put(Bytes.toBytes("row1"));
```

### 步骤4：更新数据
Put类的addColumn()方法用于更新数据。它需要3个字节的数组，分别代表列族，列限定符（列名）和要更新的值。
如下所示，使用addColumn()方法将数据更新HBase表。
```text
p.addColumn(Bytes.toBytes("personal data"), Bytes.toBytes("city"), Bytes.toBytes("Delhi"));
```

### 步骤5：将数据保存在表中
更新所需的行后，通过将put实例添加到HTable类的put()方法中来保存更改，如下所示。
```text
Table.put(p);
```

### 步骤6：关闭HTable实例
在HBase表中更新数据后，使用close()方法关闭HTable实例，如下所示。
```text
Table.close();
```

下面给出了在HBase Table中更新数据的完整程序。
```text
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory; 
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
public class UpdateData {
   public static void main(String[] args) throws IOException {
      // Instantiating Configuration class
      Configuration config = HBaseConfiguration.create();
      Connection connection = ConnectionFactory.createConnection(config);
      TableName tb = TableName.valueOf("emp");
      // Instantiating HTable class
      Table table = connection.getTable(tb);
      // Instantiating Put class
      // accepts a row name.
      Put p = new Put(Bytes.toBytes("row1"));
      // update values using add() method 
      p.addColumn(Bytes.toBytes("personal data"), Bytes.toBytes("city"), Bytes.toBytes("Delih"));
      // Saving the put Instance to the HTable.
      table.put(p);
      System.out.println("data updated");
      // closing HTable
      table.close();
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac UpdateData.java
$java UpdateData
```

以下应该是输出：
```text
data updated
```
