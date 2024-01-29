# HBase 启用表

## 使用HBase Shell 启用表
要删除表或更改其设置，您需要首先使用enable命令启用该表。您可以使用enable命令重新启用它。

以下是 启用表的语法：
```text
enable 'emp'
```

下面给出的示例显示了如何启用表。
```text
hbase(main):025:0> enable 'emp'
0 row(s) in 1.2760 seconds
```

启用表格后，对其进行扫描。如果可以看到架构，则表已成功启用。
```text
hbase(main):006:0> scan 'emp'
ROW                        COLUMN + CELL
1 column = personal data:city, timestamp = 1417516501, value = hyderabad
1 column = personal data:name, timestamp = 1417525058, value = ramu
1 column = professional data:designation, timestamp = 1417532601, value = manager
1 column = professional data:salary, timestamp = 1417524244109, value = 50000
2 column = personal data:city, timestamp = 1417524574905, value = chennai
2 column = personal data:name, timestamp = 1417524556125, value = ravi
2 column = professional data:designation, timestamp = 14175292204, value = sr:engg
2 column = professional data:salary, timestamp = 1417524604221, value = 30000
3 column = personal data:city, timestamp = 1417524681780, value = delhi
3 column = personal data:name, timestamp = 1417524672067, value = rajesh
3 column = professional data:designation, timestamp = 14175246987, value = jr:engg
3 column = professional data:salary, timestamp = 1417524702514, value = 25000
3 row(s) in 0.0400 seconds
```

## is_enabled
此命令用于查找是否启用了表。其语法如下。
```text
hbase> is_enabled 'table name'
```

下面的示例验证是否启用了名为emp的表。如果启用，它将返回`true`，否则将返回`false`。
```text
hbase(main):031:0> is_enabled 'emp'
true
0 row(s) in 0.0440 seconds
```

## 使用Java API 启用表
要验证是否启用了表，请使用isTableEnabled()方法；要 启用表，请使用enableTable()方法。
这些方法属于Admin类。请按照下面给出的步骤 启用表。

### 第1步
获得Admin类，如下所示。
```text
// Instantiating configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
// Instantiating Admin class
Admin admin = null;
admin = connection.getAdmin();
```

### 第2步
使用isTableEnabled()方法验证是否启用了该表，如下所示。
```text
Boolean isEnabled =  admin.isTableEnabled(TableName.valueOf("emp"));
```

### 第3步
如果未启用该表，请如下所示将其启用。
```text
if(!isEnabled) {
   admin.enableTable(TableName.valueOf("emp"));
   System.out.println("Table enabled");
}
```

下面给出的是验证表是否已启用的完整程序；如果没有，如何启用它。
```text
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.conf.Configuration;
public class CreateTable {
   public static void main(String[] args) throws IOException {
      try {
         // Instantiating configuration class
         Configuration config = HBaseConfiguration.create();
         Connection connection = ConnectionFactory.createConnection(config);
         // Instantiating Admin class
         Admin admin = null;
         admin = connection.getAdmin();
         Boolean isEnabled =  admin.isTableEnabled(TableName.valueOf("emp"));
         System.out.println(isEnabled);
         if(!isEnabled) {
            admin.enableTable(TableName.valueOf("emp"));
            System.out.println("Table enabled");
         }
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac EnableTable.java
$java EnableTable
```

以下应该是输出：
```text
false
Table enabled
```
