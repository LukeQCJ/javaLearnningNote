# HBase 删除表

## 使用HBase Shell删除表
使用drop命令，可以删除表。删除表之前，必须禁用它。
```text
hbase(main):018:0> disable 'emp'
0 row(s) in 1.4580 seconds
hbase(main):019:0> drop 'emp'
0 row(s) in 0.3060 seconds
```

使用exist命令验证表是否存在。
```text
hbase(main):020:07gt; exists 'emp'
Table emp does not exist
0 row(s) in 0.0730 seconds
```

drop_all此命令用于删除与命令中给定的正则表达式匹配的表。其语法如下：
```text
hbase> drop_all 't.*'
```

假设有名为raja，rajani，rajendra，rajesh和raju的表。
```text
hbase(main):017:0> list
TABLE
raja
rajani
rajendra
rajesh
raju
9 row(s) in 0.0270 seconds
```

所有这些表都以字母raj开头。首先，让我们使用disable_all命令禁用所有这些表，如下所示。
```text
hbase(main):002:0> disable_all 'raj.*'
raja
rajani
rajendra
rajesh
raju
Disable the above 5 tables (y/n)?
y
5 tables successfully disabled
```

现在，您可以使用drop_all命令删除所有它们，如下所示。
```text
hbase(main):018:0> drop_all 'raj.*'
raja
rajani
rajendra
rajesh
raju
Drop the above 5 tables (y/n)?
y
5 tables successfully dropped
```

## 使用Java API删除表
您可以使用HBaseAdmin类中的deleteTable()方法删除表。请按照下面给出的步骤使用Java API删除表。

第1步 Admin类。
```text
// Instantiating configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
// Instantiating Admin class
Admin admin = null;
admin = connection.getAdmin();
```

第2步 使用Admin类的disableTable()方法禁用该表。
```text
admin.disableTable(tableName.valueOf("emp"));
```

第3步 现在，使用Admin类的deleteTable()方法删除表。
```text
admin.deleteTable(tableName.valueOf("emp"));
```

下面给出的是删除HBase中的表的完整Java程序。
```text
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.conf.Configuration;
public class DeleteTable {
   public static void main(String[] args) throws IOException {
     try {
         // Instantiating configuration class
         Configuration config = HBaseConfiguration.create();
         Connection connection = ConnectionFactory.createConnection(config);
         // Instantiating Admin class
         Admin admin = null;
         admin = connection.getAdmin();
         TableName tableName = TableName.valueOf("emp");
         boolean bool =  admin.isTableDisabled(tableName);
         if(!bool) {
            admin.disableTable(tableName);
            System.out.println("table disabled");
         }
         admin.deleteTable(tableName);
         System.out.println("table deleted");
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac DeleteTable.java
$java DeleteTable
```

以下应该是输出：
```text
Table deleted
```
