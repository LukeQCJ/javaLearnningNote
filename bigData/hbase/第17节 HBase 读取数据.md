# HBase 读取数据
## 使用HBase Shell读取数据
HTable类的get命令和get()方法用于从HBase中的表读取数据。使用get命令，您一次可以获取一行数据。其语法如下：
```text
get '<table name>','row1'
```

以下示例显示如何使用get命令。让我们扫描emp表的第一行。
```text
hbase(main):012:0> get 'emp', '1'
COLUMN  CELL
personal data:city                                timestamp=2021-01-04T09:35:39.345, value=hyderahad
personal data:designation                         timestamp=2021-01-04T09:36:18.466, value=manager
personal data:name                               timestamp=2021-01-04T09:38:44.128, value=raju
personal data:salary                             timestamp=2021-01-04T09:36:42.145, value=50000
1 row(s)
Took 0.4045 seconds
```

### 读取指定的列
下面给出的是使用get方法读取特定列的语法。
```text
hbase> get 'table name', 'rowid', {COLUMN ⇒ 'column family:column name'}
```

下面给出了读取HBase表中特定列的示例。
```text
hbase:006:0> get 'emp','row1', {COLUMN => 'personal data:name'}
COLUMN           CELL
personal data:name                               timestamp=2021-01-04T10:48:33.531, value=raju
1 row(s)
Took 0.0822 seconds
```

## 使用Java API读取数据
要从HBase表读取数据，请使用HTable类的get()方法。此方法需要Get类的实例。请按照下面给出的步骤从HBase表中检索数据。

### 步骤1：连接
```text
// Instantiating Configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
```

### 步骤2：Table类
```text
// Instantiating Table class
Table table = connection.getTable(tb);
```

### 步骤3：实例化Get类
您可以使用Table类的get()方法从HBase表中检索数据。此方法从给定的行中提取一个单元格。
它需要一个Get类对象作为参数。如下所示创建它。
```text
Get get = new Get(toBytes("row1"));
```

### 步骤4：读取数据
检索数据时，您可以按ID获得单个行，或按一组行ID获得一组行，或者扫描整个表或行的子集。

您可以使用Get类中的add方法变体检索HBase表数据。
要从特定列族获取特定列，请使用以下方法。
```text
get.addFamily(personal)
```

要从特定列族获取所有列，请使用以下方法。
```text
get.addColumn(personal, name)
```

### 步骤5：获取结果
通过将Get类实例传递给HTable类的get方法来获取结果。此方法返回Result类对象，该对象保存请求的结果。
下面给出了get()方法的用法。
```text
Result result = table.get(g);
```

### 步骤6：从结果实例中读取值
结果类提供的getValue()方法来从它的实例中读取的值。如下所示使用它从Result实例读取值。
```text
byte [] value = result.getValue(Bytes.toBytes("personal data"),Bytes.toBytes("name"));
byte [] value1 = result.getValue(Bytes.toBytes("personal data"),Bytes.toBytes("city"));
```

下面给出的是从HBase表读取值的完整程序。
```text
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Result;
public class RetrieveData {
   public static void main(String[] args) throws IOException {
      // Instantiating Configuration class
      Configuration config = HBaseConfiguration.create();
      Connection connection = ConnectionFactory.createConnection(config);
      TableName tb = TableName.valueOf("emp");
      // Instantiating Table class
      Table table = connection.getTable(tb);
      // Instantiating Get class
      // accepts a row name.
      Get g = new Get(Bytes.toBytes("row1"));
      // Reading the data
      Result result = table.get(g);
      // Reading values from Result class object
      byte[] value = result.getValue(Bytes.toBytes("personal data"), Bytes.toBytes("name"));
      byte[] value1 = result.getValue(Bytes.toBytes("personal data"), Bytes.toBytes("city"));
      // Printing the values
      String name = Bytes.toString(value);
      String city = Bytes.toString(value1);
      System.out.println("name: " + name + " city: " + city);
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac RetrieveData.java
$java RetrieveData
```

以下应该是输出：
```text
name: Raju city: Delhi
```
