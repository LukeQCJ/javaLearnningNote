## 一、Hadoop常用命令合集

### 1.1、常见基础命令
#### 1.1.1启动Hadoop
(1)进入HADOOP_HOME目录

(2)执行sh bin/start-all.sh

#### 1.1.2关闭Hadoop
(1)进入HADOOP_HOME目录

(2)执行sh bin/stop-all.sh

#### 1.1.3查看指定目录下内容
hadoop fs -ls [file_path]

eg:hadoop fs -ls /user/cidp/test.dat

#### 1.1.4打开某个存在的文件
hadoop dfs -cat [file_path]

eg: hadoop dfs -cat / user/cidp/test.dat

#### 1.1.5将本地文件储存至Hadoop
hadoop fs -put [本地目录] [hadoop文件目录]

eg: hadoop fs -put /home/data/test.txt /user/data

#### 1.1.6将本地文件夹储存至Hadoop
hadoop fs -put [本地目录] [hadoop文件夹目录]

eg: hadoop fs -put /home/data/test /user/data  (test是文件夹名)

#### 1.1.7将hadoop上某个文件down至本地已有目录下
hadoop fs -get [hadoop文件目录] [本地目录]

eg: hadoop fs -get /user/data/test.txt /home/data

#### 1.1.8删除hadoop上指定文件
hadoop fs -rm [hadoop文件路径]

eg: hadoop fs -rm /user/data/test.txt

#### 1.1.9删除hadoop上指定文件夹（包含子目录等）
hadoop fs -rm [hadoop文件夹路径]

eg: hadoop fs -rm /user/data/test

#### 1.1.10在hadoop指定目录内创建新目录
hadoop fs -mkdir [hadoop目标目录]

eg: hadoop fs -mkdir /user/data/test

#### 1.1.11在hadoop指定目录下创建一个空文件
hadoop fs -touchz [文件名]

eg: hadoop fs - touchz /user/data/test.txt

#### 1.1.12将hadoop上某个文件重命名
hadoop fs -mv [文件名] [新文件名]

eg: hadoop fs – mv /user/data/test.txt /user/data/test_new.txt

#### 1.1.13将hadoop上指定目录下所有内容保存一个文件并下载到本地
hadoop dfs -getmerge [文件]

eg: hadoop dfs -getmerge /user/data/test

#### 1.1.14将正在运行的hadoop作业kill
hadoop job -kill [job_id]

eg: hadoop job -kill 277896

### 1.2、详细命令
#### 1.2.1启动hadoop所有进程
start-all.sh等价于start-dfs.sh+start-yarn.sh

说明：一般不推荐使用start-all.sh(开源框架中内部命令启动很多问题)

#### 1.2.2单进程启动
sbin/start-dfs.sh

sbin/hadoop-daemons.sh –config .. –hostname .. start namenode…

sbin/hadoop-daemons.sh –config .. –hostname .. start datanode…

sbin/hadoop-daemons.sh –config .. –hostname .. start sescondarynamenode…

sbin/hadoop-daemons.sh –config .. –hostname .. start zkfc…//



start-yarn.sh

libexec/yarn-config.sh

sbin/yarn-daemon.sh –config $YARN_CONF_DIR start resourcemanager

sbin/yarn-daemons.sh –config $YARN_CONF_DIR start nodemanager

### 1.3、常用命令
#### 1.3.1查看指定目录下内容
hdfs dfs -ls [file_path]

hdfs dfs -ls -R / --显示目录结构

eg:hdfs dfs -ls /user/cidp/test.dat

#### 1.3.2打开某个已存在文件
hdfs dfs -cat [file_path]

eg: hdfs dfs -cat / user/cidp/test.dat

#### 1.3.3将本地文件存储至hadoop
hdfs dfs -put [本地目录] [hadoop文件目录]

eg: hdfs dfs -put /home/data/test.txt /user/data

#### 1.3.4将本地文件夹存储至hadoop
hdfs dfs -put [本地目录] [hadoop文件夹目录]

eg: hdfs dfs -put /home/data/test /user/data  (test是文件夹名)

#### 1.3.5将hadoop上某个文件down至本地已有目录下
hdfs dfs -get [hadoop文件目录] [本地目录]

eg: hdfs dfs -get /user/data/test.txt /home/data

#### 1.3.6删除hadoop上指定文件
hdfs dfs -rm [hadoop文件路径]

eg: hdfs dfs -rm /user/data/test.txt

#### 1.3.7删除hadoop上指定文件夹（包含子目录等）
hdfs dfs -rm [hadoop文件夹路径]

eg: hdfs dfs -rm /user/data/test

#### 1.3.8在hadoop指定目录内创建新目录
hdfs dfs -mkdir [hadoop目标目录]

eg: hdfs dfs -mkdir -p /user/data/test

#### 1.3.9 hadoop指定目录下创建一个空文件
hdfs dfs -touchz [文件名]

eg: hdfs dfs - touchz /user/data/test.txt

#### 1.3.10将hadoop上某个文件重命名
hdfs dfs -mv [文件名] [新文件名]

eg: hdfs dfs – mv /user/data/test.txt /user/data/test_new.txt

#### 1.3.11将hadoop上指定目录下所有内容保存成一个文件并下载到本地
hdfs dfs -getmerge [文件]

eg: hdfs dfs -getmerge /user/data/test

#### 1.3.12将正在运行的hadoop作业kill
hadoop job -kill [job_id]

eg: hadoop job -kill 277896

#### 1.3.13查看帮助
hdfs dfs -help

#### 1.3.14查看最后1kb内容
hdfs dfs -tail [文件名]

eg: hdfs dfs -tail /usr/data/test.txt

#### 1.3.15从本地复制文件到hadoop上(同-put)
hdfs dfs -copyFromLocal [文件名] [Hadoop文件目录]

eg: hdfs dfs - copyFromLocal test.txt /usr/data/test.txt

#### 1.3.16从hadoop复制文件到本地(同-get)
hdfs dfs -copyToLocal [Hadoop文件目录] [文件名]

eg: hdfs dfs - copyToLocal /usr/data/test.txt test.txt

### 1.4、安全模式
#### 1.4.1退出安全模式
NameNode在启动时会进入安全模式。安全模式是NameNode的一种状态，在这个阶段，文件系统不允许有任何修改。

系统显示Name node in safe mode，说明系统处于安全模式，这时只需要等待十几秒即可，也可通过下面命令退出安全模式：

/usr/local/hadoop$bin/hadoop dfsadmin -safemode leave

#### 1.4.2进入安全模式
在必要情况下，可以通过命令把HDFS置于安全模式：

/usr/local/hadoop$bin/hadoop dfsadmin -safemode enter

### 1.5、节点添加
添加一个新的DataNode节点，先在新节点安装好Hadoop，要和NameNode使用相同配置（可以直接从NameNode复制），修改HADOOPHOME/conf/master文件，加入NameNode主机名。在NameNode节点修改HADOOPHOME/conf/master文件，加入NameNode主机名。在NameNode节点修改HADOOP_HOME/conf/slaves文件，加入新节点名，再建立新节点无密码的SSH连接，运行启动命令为：

/usr/local/hadoop$bin/start-all.sh。

### 1.6、负载均衡
HDFS的数据在各个节点DataNode中分布可能很不均匀，尤其在DataNode节点出现故障或新增DataNode节点时。新增数据块时NameNode对DataNode的节点选择策略也有可能导致数据块分布不均匀。用户可使用命令重新平衡DataNode上的数据分布：

/usr/loacalhadoop$bin/start-balancer.sh。

### 1.7、补充
#### 1.7.1对hdfs操作命令格式是hdfs dfs
(1)-ls表示对hdfs下一级目录的查看

(2)-lsr表示对hdfs目录的递归查看

(3)-text查看文件内容

(4)-rmr表示递归删除文件

(5)-chown [-R] [文件目录]，修改目录所属群组

(6)-chomd [-R] [文件目录]，修改目录拥有者

(7)-count [-q] [文件目录]，查看目录下子目录数、文件数、文件大小、文件名/目录名

(8)-du [文件目录]，显示目录中每个文件或目录的大小

(9)-dus [文件目录]，显示目录总大小

(10)-expunge 清空回收站

(11)-test [-ezd] [文件目录]，-e查看文件或目录是否存在，存在返回0否则为1；-z文件是否为空，长度为0返回0否则返回1；-d是否为目录，是返回0否则返回1。

## 二、区别
### 2.1hadoop fs、hadoop dfs和hdfs dfs命令区别
(1)hadoop fs:通用文件系统命令，针对任何系统，比如本地文件、HDFS文件、HFTP文件、S3文件系统等；

(2)hadoop dfs:特定针对HDFS的文件系统的相关操作，目前不推荐使用；

(3)hdfs dfs:与Hadoop dfs类似，针对HDFS系统的操作，代替hadoop dfs