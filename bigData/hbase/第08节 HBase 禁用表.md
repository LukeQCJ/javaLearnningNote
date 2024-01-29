# HBase 禁用表

## 使用HBase Shell禁用表
要删除表或更改其设置，您需要首先使用disable命令禁用该表。您可以使用enable命令重新启用它。

以下是禁用表的语法：
```text
disable 'emp'
```

例下面给出的示例显示了如何禁用表。
```text
hbase(main):025:0> disable 'emp'
0 row(s) in 1.2760 seconds
```

禁用该表之后，您仍然可以通过list和exist命令来感知其存在。您无法扫描。它会给您以下错误。
```text
hbase(main):028:0> scan 'emp'
ROW         COLUMN + CELL
ERROR: emp is disabled.
is_disabled
```

此命令用于查找是否禁用了表。其语法如下。
```text
hbase> is_disabled 'table name'
```

下面的示例验证是否禁用了名为emp的表。如果禁用，它将返回true，否则将返回false。
```text
hbase(main):031:0> is_disabled 'emp'
true
0 row(s) in 0.0440 seconds
disable_all
```

此命令用于禁用所有与给定正则表达式匹配的表。下面给出了disable_all命令的语法。
```text
hbase> disable_all 'emp.+'
```

假设HBase中有5个表，分别是raja，rajani，rajendra，rajesh和raju。以下代码将禁用所有以raj开头的表。
```text
hbase(main):002:07> disable_all 'raj.*'
raja
rajani
rajendra
rajesh
raju
Disable the above 5 tables (y/n)?
y
5 tables successfully disabled
```

## 使用Java API禁用表
要验证是否禁用了表，请使用isTableDisabled()方法；要禁用表，请使用disableTable()方法。
这些方法属于HBaseAdmin类。请按照下面给出的步骤禁用表。

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
使用isTableDisabled()方法验证是否禁用了该表，如下所示。
```text
Boolean isDisabled =  admin.isTableDisabled(TableName.valueOf("emp"));
```

### 第3步
如果未禁用该表，请如下所示将其禁用。
```text
if(!isDisabled) {
    admin.disableTable(TableName.valueOf("emp"));
    System.out.println("Table disabled");
}
```

下面给出的是验证表是否已禁用的完整程序；如果没有，如何禁用它。
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
         Boolean isDisabled =  admin.isTableDisabled(TableName.valueOf("emp"));
         System.out.println(isDisabled);
         if(!isDisabled) {
            admin.disableTable(TableName.valueOf("emp"));
            System.out.println("Table disabled");
         }
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac DisableTable.java
$java DisableTable
```

以下应该是输出：
```text
false
Table disabled
```
