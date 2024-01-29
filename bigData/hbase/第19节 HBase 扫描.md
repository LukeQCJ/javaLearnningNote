# HBase 扫描（scan）
## 使用HBase Shell扫描
scan命令用于在HTable查看数据。使用scan命令，可以获得表数据。其语法如下：
```text
scan '<table name>'
```

以下示例显示如何使用scan命令从表中读取数据。在这里，我们正在阅读emp表。
```text
hbase(main):010:0> scan 'emp'
ROW                           COLUMN + CELL
1 column = personal data:city, timestamp = 1417521848375, value = hyderabad
1 column = personal data:name, timestamp = 1417521785385, value = ramu
1 column = professional data:designation, timestamp = 1417585277,value = manager
1 column = professional data:salary, timestamp = 1417521903862, value = 50000
1 row(s) in 0.0370 seconds
```

## 使用Java API扫描
使用Java API扫描整个表数据的完整程序如下。
```text
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
public class ScanTable{
   public static void main(String args[]) throws IOException{
      // Instantiating Configuration class
      Configuration config = HBaseConfiguration.create();
      // Instantiating HTable class
      HTable table = new HTable(config, "emp");
      // Instantiating the Scan class
      Scan scan = new Scan();
      // Scanning the required columns
      scan.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("name"));
      scan.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("city"));
      // Getting the scan result
      ResultScanner scanner = table.getScanner(scan);
      // Reading values from scan result
      for (Result result = scanner.next(); result != null; result = scanner.next())
      System.out.println("Found row : " + result);
      //closing the scanner
      scanner.close();
   }
}
```

编译并执行上述程序，如下所示。
```text
$javac ScanTable.java
$java ScanTable
```

以下应该是输出：
```text
Found row : keyvalues={2/personal data:city/1609724365298/Put/vlen=7/seqid=0, 2/personal data:name/1609724347682/Put/vlen=4/seqid=0}
Found row : keyvalues={3/personal data:city/1609724457831/Put/vlen=5/seqid=0, 3/personal data:name/1609724433117/Put/vlen=6/seqid=0}
Found row : keyvalues={row1/personal data:city/1609730663707/Put/vlen=5/seqid=0}
```
