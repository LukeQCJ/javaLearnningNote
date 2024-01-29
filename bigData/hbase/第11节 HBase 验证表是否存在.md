# HBase 验证表是否存在（exist）

## 使用HBase Shell验证表是否存在
您可以使用exist命令验证表的存在。以下示例显示如何使用此命令。
```text
hbase(main):024:0> exists 'emp'
Table emp does exist
0 row(s) in 0.0750 seconds
==================================================================
hbase(main):015:0> exists 'student'
Table student does not exist
0 row(s) in 0.0480 seconds
```

## 使用Java API验证表是否存在
您可以使用HBaseAdmin类的tableExists()方法来验证HBase中是否存在表。请按照以下步骤验证HBase中是否存在表。

### 第1步 获取Admin类。
```text
// Instantiating configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
// Instantiating Admin class
Admin admin = null;
admin = connection.getAdmin();
```

### 第2步 使用tableExists()方法验证表是否存在。

下面给出了使用Java API测试HBase中表是否存在的Java程序。
```text
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.conf.Configuration;
@SuppressWarnings("deprecation")
public class TableExists{
   public static void main(String args[])throws IOException{
     try {
        // Instantiating configuration class
        Configuration config = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(config);
        // Instantiating Admin class
        Admin admin = null;
        admin = connection.getAdmin();
        boolean bool =  admin.tableExists(TableName.valueOf("emp"));
        System.out.println(bool);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac TableExists.java
$java TableExists
```

以下应该是输出：
```text
true
```
