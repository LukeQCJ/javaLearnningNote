# HBase 关闭

## exit
您可以通过键入exit命令退出shell程序。
```text
hbase(main):021:0> exit
```

## 停止HBase
要停止HBase，请浏览至HBase主文件夹，然后键入以下命令。
```text
./bin/stop-hbase.sh
```


## 使用Java API停止HBase
您可以使用Admin类的shutdown()方法关闭HBase 。请按照以下步骤关闭HBase：

第1步 Admin类。
```text
// Instantiating configuration class
Configuration config = HBaseConfiguration.create();
Connection connection = ConnectionFactory.createConnection(config);
// Instantiating Admin class
Admin admin = null;
admin = connection.getAdmin();
```

第2步 使用Admin类的shutdown()方法关闭HBase 。
```text
admin.shutdown();
```


下面给出的是停止HBase的程序。
```text
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.conf.Configuration;
public class ShutDownHbase{
   public static void main(String args[])throws IOException {
      // Instantiating configuration class
      Configuration config = HBaseConfiguration.create();
      Connection connection = ConnectionFactory.createConnection(config);
      // Instantiating Admin class
      Admin admin = null;
      admin = connection.getAdmin();
      // Shutting down HBase
      System.out.println("Shutting down hbase");
      admin.shutdown();
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac ShutDownHbase.java
$java ShutDownHbase
```

以下应该是输出：
```text
Shutting down hbase
```
