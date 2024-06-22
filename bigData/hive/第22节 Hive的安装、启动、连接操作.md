# hive的安装
## 1. 修改 hadoop的 core-site.xml中, 添加以下内容:
修改hadoop 配置文件 etc/hadoop/core-site.xml,加入如下配置项:
```text
   <property>
      <name>hadoop.proxyuser.root.hosts</name>
      <value>*</value>
   </property>
   <property>
      <name>hadoop.proxyuser.root.groups</name>
      <value>*</value>
   </property>
```

注意: 如果发现没有配置, 请将其添加到core-site.xml中, 添加后, 记得发给其他节点

## 2. 上传hive的安装包到node1的节点, 并解压
```text
   cd /export/software/
   rz 上传即可
```

说明: 如果提示 -bash: rz: 未找到命令 请执行以下命令安装即可:  yum -y install lrzsz

执行解压:
```text
tar -zxf apache-hive-3.1.2-bin.tar.gz -C /export/server/
cd /export/server
mv apache-hive-3.1.2-bin/  hive-3.1.2
```

## 3. 修改 hive的环境配置文件: hive-env.sh
```text
   cd /export/server/hive-3.1.2/conf
   cp hive-env.sh.template  hive-env.sh
   vim hive-env.sh
   输入 i
```

修改一下内容:
```text
# 配置hadoop的家目录
HADOOP_HOME=/export/server/hadoop-3.3.0/
# 配置hive的配置文件的路径
export HIVE_CONF_DIR=/export/server/hive-3.1.2/conf/
# 配置hive的lib目录
export HIVE_AUX_JARS_PATH=/export/server/hive-3.1.2/lib/
```

配置后保存退出即可:
```text
esc
:wq
```

## 4. 添加一个hive的核心配置文件: hive-site.xml
```text
   cd /export/server/hive-3.1.2/conf
   vim hive-site.xml
   输入i
```
添加以下内容:
```text
<configuration>
    <!-- 存储元数据mysql相关配置 -->
    <property>
        <name>javax.jdo.option.ConnectionURL</name>
        <value>jdbc:mysql://node1:3306/hive3?createDatabaseIfNotExist=true&amp;useSSL=false</value>
    </property>

    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>com.mysql.jdbc.Driver</value>
    </property>

    <property>
        <name>javax.jdo.option.ConnectionUserName</name>
        <value>root</value>
    </property>

    <property>
        <name>javax.jdo.option.ConnectionPassword</name>
        <value>123456</value>
    </property>

    <!-- H2S运行绑定host -->
    <property>
        <name>hive.server2.thrift.bind.host</name>
        <value>node1</value>
    </property>

    <!-- 远程模式部署metastore metastore地址 -->
    <property>
        <name>hive.metastore.uris</name>
        <value>thrift://node1:9083</value>
    </property>

    <!-- 关闭元数据存储授权  -->
    <property>
        <name>hive.metastore.event.db.notification.api.auth</name>
        <value>false</value>
    </property>
    
    <!-- 默认数据仓库原始位置  -->
    <property>
        <name>hive.metastore.warehouse.dir</name>
        <!-- 默认值就是这个/user/hive/warehouse -->
        <value>/user/hive/warehouse</value>
        <description>location of default database for the warehouse</description>
    </property>
</configuration>
```


添加后, 保存退出即可:
```text
esc
:wq
```

## 5. 上传mysql的驱动包到hive的lib目录下
```text
   cd /export/server/hive-3.1.2/lib

    rz 上传即可
```

上传后, 校验是否已经上传到lib目录下

## 6. 解决Hive与Hadoop之间guava版本差异
```text
   cd /export/server/hive-3.1.2/
   rm -rf lib/guava-19.0.jar
   cp /export/server/hadoop-3.3.0/share/hadoop/common/lib/guava-27.0-jre.jar ./lib/
```

如果此步没有执行, 可能会报出错误

## 7. 初始化元数据
```text
cd /export/server/hive-3.1.2/

bin/schematool -initSchema -dbType mysql -verbose
```

执行完成后, 可以看到在mysql的hive3的数据库中, 会产生74张元数据表

可能出现的错误:
- root @ node1 (passwd no)  类似于这样的一个错误
- 出现的原因: 由于mysql的密码不对导致的
- 在mysql的 mysql数据库中有一个user表, 打开user表后, 找到password, 将 Host为%的password的免密内容拷贝到其他行中, 保证大家都一样即可
- 拷贝后, 然后就可以在Linux中重启mysql

重启命令:
```text
service  mysqld restart
或者
service mysql restart
或者
systemctl restart mysqld.service
或者
systemctl restart mysql.service
```

## 8. 创建HDFS的hive相关的目录
```text
   hadoop fs -mkdir /tmp
   hadoop fs -mkdir -p /user/hive/warehouse
   hadoop fs -chmod g+w /tmp
   hadoop fs -chmod g+w /user/hive/warehouse
```

# hive的启动
## 1. 先启动hadoop集群
```text
启动命令:
   node1执行  start-all.sh

启动后, 要确保hadoop是启动良好的
首先通过jps分别查看每一个节点:
在node1节点:
namenode
datanode
resourcemanager
nodemanager
在node2节点:
SecondaryNameNode
datanode
nodemanager
在node3节点:
datanode
nodemanager
```

接着通过浏览器, 访问 node1:9870 查看 安全模式是否退出以及是否有3个datanode

最后,通过浏览器,访问 node1:8088 查看是否有三个激活节点

## 2. 启动 hive的服务: metastore
先启动metastore服务项:
```text
前台启动:
   cd /export/server/hive-3.1.2/bin
   ./hive --service metastore

   注意: 前台启动后, 会一直占用前台界面, 无法进行操作
   好处: 一般先通过前台启动, 观察metastore服务是否启动良好
   前台退出: ctrl + c
   
后台启动:
   当前台启动没有任何问题的时候, 可以将其退出, 然后通过后台启动, 挂载后台服务即可
   cd /export/server/hive-3.1.2/bin
   nohup ./hive --service metastore &
   
   启动后, 通过 jps查看, 是否出现一个runjar 如果出现 说明没有问题(建议搁一分钟左右, 进行二次校验)
   注意: 如果失败了, 通过前台启动, 观察启动日志, 看一下是什么问题, 尝试解决
   
后台如何退出:
通过 jps 查看进程id 然后采用 kill -9
```

## 3. 启动hive的服务: hiveserver2服务
接着启动hiveserver2服务项:
```text
前台启动:
   cd /export/server/hive-3.1.2/bin
   ./hive --service hiveserver2
   注意: 前台启动后, 会一直占用前台界面, 无法进行操作
   好处: 一般先通过前台启动, 观察hiveserver2服务是否启动良好
   前台退出: ctrl + c

后台启动:
   当前台启动没有任何问题的时候, 可以将其退出, 然后通过后台启动, 挂载后台服务即可
   cd /export/server/hive-3.1.2/bin
   nohup ./hive --service hiveserver2 &

   启动后, 通过 jps查看, 是否出现一个runjar 如果出现 说明没有问题(建议搁一分钟左右, 进行二次校验)
   注意: 如果失败了, 通过前台启动, 观察启动日志, 看一下是什么问题, 尝试解决

后台如何退出:
   通过 jps 查看进程id 然后采用 kill -9
```

# 如何连接
第一种连接方式: 通过 hive原生客户端 (此种不需要掌握, 只是看看)
```text
cd /export/server/hive-3.1.2/bin
./hive
```

第二种连接方式: 基于beeline的连接方式
```text
cd /export/server/hive-3.1.2/bin
./beeline   --进入beeline客户端
连接hive:
!connect jdbc:hive2://node1:10000
接着输入用户名: root
最后输入密码: 无所谓(一般写的都是虚拟机的登录密码)
```
