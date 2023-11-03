## 一、前言信息
[官网命令说明查看](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/FileSystemShell.html)

![commandPage01.png](img/04/commandPage01.png)

说明：

| 命令	         | 适用范围                         |
|-------------|------------------------------|
| hadoop fs	  | 使用范围最广，建议使用，可操作任何对象          |
| hadoop dfs	 | 只可操作HDFS文件系统(已弃用)            |
| hdfs fs	    | 只可操作HDFS文件系统，包括与Local FS间的操作 |
| hdfs dfs	   | 只可操作HDFS文件系统，常用              |


## 二、常用命令

### 1、创建目录
格式：
```text
hadoop fs –mkdir [-p] <path>
```
示例：
```text
hadoop fs -mkdir -p /button/file
```

### 2、查看目录下的内容
格式：
```text
hadoop fs –ls [-h] [-R] [<path>]
-h  人性化显示文件大小
-R 递归查看指定目录及子目录
```
示例：
```text
hadoop fs -ls -h /
```

### 3、上传文件
格式：
```text
hadoop fs –put [-f] [-p] <localsrc> <dst>
-f   覆盖目标文件(若文件已存在)
-p   保留访问和修改时间、所有权和权限
localsrc   本地文件系统
dst   目标文件系统
```
示例：
```text
hadoop fs -put ./jdk.zip /button/file
```

### 4、上传并删除源文件
格式：
```text
hadoop fs –moveFromLocal <localsrc> <dst>
```
示例：
```text
hadoop fs -moveFromLocal ./myMove.txt /button/file
```
这个和put唯一不同的地方就在于该命令会上传完后会删除源文件


### 5、查看文件内容
读取文件全部内容显示在标准输出控制台(大文件慎用)。

格式：
```text
hadoop fs –cat <src>
```
示例：
```text
hadoop fs -cat /button/file/myMove.txt
```

### 6、查看文件开头内容
查看文件前1KB的内容。

格式：
```text
hadoop fs –head <src>
```
示例：
```text
hadoop fs -head /button/file/myMove.txt
```

### 7、查看文件末尾内容
查看文件末尾1KB的内容。

格式：
```text
hadoop fs –tail [-f] <src>
-f   动态显示文件中追加的内容
```
示例：
```text
hadoop fs -tail -f /button/file/myMove.txt
```

### 8、下载文件
下载文件到本地指定目录。

格式：
```text
hadoop fs –get [-f] [-p] <src> <localdst>
-f   覆盖目标文件(目标文件存在)
-p   保留访问和修改时间、所有权和权限
```
示例：
```text
hadoop fs -get /button/file/myMove.txt ./
```

### 9、合并下载文件
将HDFS上一个目录中所有的文件合并到一起输出到一个本地文件上。

格式：
```text
hadoop fs –getmerge [-nl] [-skip-empty-file] <src> <localdst>
-nl   每个文件末尾添加换行符
-skip-empty-file   跳过空白文件
```
示例：
```text
hadoop fs -mkdir -p /button/merge
hadoop fs -put a.txt b.txt c.txt /button/merge
hadoop fs -getmerge -nl -skip-empty-file /button/merge/* ./merge.txt
```

### 10、拷贝文件
将原始路径的文件拷贝到新的路径下。

格式：
```text
hadoop fs –cp [-f] <src> <dst>
-f   覆盖目标文件(文件存在的情况下)
```
示例：
```text
hadoop fs -cp /button/file/myMove.txt /button/merge
```

### 11、追加数据到文件中
将本地文件的内容追加到hdfs指定的文件中。

格式：
```text
hadoop fs –appendToFile <localsrc> <dst>
localsrc  本地文件，如果为”-”，则输入从标准输入中读取
dst 目标文件不存在则创建
```
示例：
```text
hadoop fs -appendToFile c.txt /button/file/a.txt
```
从标准输入读取，ctrl+c结束输入
```text
hadoop fs -appendToFile - /button/file/a.txt
asdfadsf
asfadsffa
^C
```

### 12、查看磁盘空间
格式：
```text
hadoop fs –df [-h] [<path>]
```
示例：
```text
hadoop fs -df -h /button
```

### 13、查看文件使用的空间

格式：
```text
hadoop fs –du [-s] [-h] <path>
-s   显示指定路径文件长度的汇总摘要而不是单个文件的摘要
-h   人性化显示文件大小
```
示例：
```text
hadoop fs -du -h /button
```

### 14、移动文件
移动文件到指定目录下(也可重命名文件)。

格式：
```text
hadoop fs –mv <src> <dst>
```
示例：
```text
hadoop fs -mv /button/file/a.txt /button/file/a_new.txt
```

### 15、修改文件副本个数
修改指定文件的副本个数。

格式：
```text
hadoop fs –setrep [-R] [-w] <rep> <path>
-R 表示递归修改文件夹及其子目录所有
-w 客户端是否等待副本修改完毕
```
示例：
```text
hadoop fs -setrep -R -w 2 /button/merge/a.txt
```

### 16、查看校验码信息
格式：
```text
hadoop fs –checksum <path>
```
示例：
```text
hadoop fs -checksum /button/merge/a.txt
```

### 17、显示路径下的目录、文件和字节数

格式：
```text
hadoop fs -count [-q] [-h] [-v] <paths>
-q   控制显示的列
-v   显示标题行
```
示例：
```text
hadoop fs -count -h -q -v /button
```

### 18、从本地拷贝文件

格式：
```text
hadoop fs -copyFromLocal <localsrc> <dst>
```
示例：
```text
hadoop fs copyFromLocal ./my.txt /button
```
注：该命令类似于put，不同的是拷贝的源地址必须是本地文件系统


### 19、拷贝文件到本地
注：该命令类似于get，不同的是拷贝目标地址必须为本地文件系统。

格式：
```text
hadoop fs –copyToLocal <path> <localdst>
```
示例：
```text
hadoop fs -copyToLocal /button/c.txt /opt
```

### 20、查找目录文件
格式：
```text
hadoop fs –find <path> <expression>

expression说明：
-name pattern   查找的文件名
-iname pattern  忽略大小写查找文件名
-print    打印(默认值)
-print0   打印在一行
```
示例：
```text
hadoop fs -find /button -iname a.txt
```

### 21、删除文件
删除指定参数的文件，如果启用了垃圾箱，文件系统会将删除的文件移动到垃圾箱目录，默认情况下禁用垃圾箱功能，
用户可以通过为参数fs.trash.interval（在core-site.xml中）设置大于零的值来启用垃圾箱。

格式：
```text
hadoop fs –rm [-f] [-R|-r][-skipTrash] <path>
-f   如果文件不存在，-f选项将不会显示诊断消息或修改退出状态以反映错误
-R   选项递归地删除目录及其下的任何内容
-skipTrash  选项将绕过垃圾箱（如果启用），并立即删除指定的文件
```
示例：
```text
hadoop fs -rm -f -R -skipTrash /button/c.txt
```

相关更多命令以及更详细的用法可以通过官网学习。
