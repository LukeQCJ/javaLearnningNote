# 二、搭建Zookeeper服务器

```text
前提：安装了JDK
下载zookeeper安装包apache-zookeeper-3.7.1-bin.tar.gz，解压
zoo_sample.cfg 复制一份改名为zoo.cfg
```

## 1、zoo.cfg配置文件说明
```text
# zookeeper时间配置中的基本单位（毫秒）
tickTime=2000
# 允许follower初始化连接到leader的最大时长，它表示tickTime的时间倍数，即：initLimit*tickTime
initLimit=10
# 允许follower与leader数据同步最大时长，它表示tickTime的时间倍速，即：syncLimit*tickTime
syncLimit=5
# zookeeper数据存储目录及日志保存目录（如果没有指明dataLogDir，则日志也保存在这个文件中）
dataDir=/java/myzookeeper/data
# 对客户端提供的端口号
clientPort=2181
# 单个客户端与zookeeper最大并发连接数
maxClientCnxns=60
# 保存的数据快照数量，之外的将会被清除
autopurge.snapRetainCount=3
# 自动触发清除任务时间间隔，单位为小时，默认为0，表示不自动清除
autopurge.purgeInterval=1
```

## 2、Zookeeper服务器的操作命令

到Zookeeper目录：
```text
cd /java/myzookeeper
```

启动zk服务器：
```text
./bin/zkServer.sh start ./conf/zoo.cfg
```

查看zk服务器的状态：
```text
./bin/zkServer.sh status ./conf/zoo.cfg
```

停止服务器：
```text
./bin/zkServer.sh stop ./conf/zoo.cfg
```

