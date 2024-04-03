# 八、Zookeeper集群实战

## 1、Zookeeper集群角色
zookeeper集群中的节点有三种角色：
- Leader：处理集群的所有事务请求，集群中只有一个Leader
  - Zookeeper集群工作的核心
  - 事务请求（写操作）的唯一调度和处理者，保证集群事务处理的顺序性；
  - 集群内部各个服务器的调度者。
  - 对于create，setData，delete等有写操作的请求，则需要统一转发给leader处理，leader需要决定编号、执行操作，这个过程称为一个事务。

- Follower：只能处理读请求，参与Leader选举
  - 处理客户端非事务（读操作）请求，转发事务请求给Leader；
  - 参与集群Leader选举投票。
  - 此外，针对访问量比较大的zookeeper集群，还可新增观察者角色。

- Observer：只能处理读请求，提升集群读的性能，但不能参与Leader选举
  - 观察者角色，观察Zookeeper集群的最新状态变化并将这些状态同步过来，其对于非事务请求可以进行独立处理，
    对于事务请求，则会转发给Leader服务器进行处理。
  - 不会参与任何形式的投票只提供非事务服务，通常用于在不影响集群事务处理能力的前提下提升集群的非事务处理能力。

![zkCluster01.png](img/08/zkCluster01.png)

## 2、集群搭建
搭建4个节点，其中一个节点为Observer。

### 1.创建4个节点的myid并设值
在usr/local/zookeeper中创建一下四个文件
```text
/usr/local/zookeeper/data/zk1# echo 1 > myid
/usr/local/zookeeper/data/zk2# echo 2 > myid
/usr/local/zookeeper/data/zk3# echo 3 > myid
/usr/local/zookeeper/data/zk4# echo 4 > myid
```

### 2.编写4个zoo.cfg
```text
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just
# example sakes. 修改对应的zk1 zk2 zk3 zk4
dataDir=/usr/local/zookeeper/zkdata/zk1

# the port at which the clients will connect
clientPort=2181

# 2001为集群通信端口，3001为集群选举端口，observer（观察者身份）
server.1=127.0.0.1:2001:3001
server.2=127.0.0.1:2002:3002
server.3=127.0.0.1:2003:3003
server.4=127.0.0.1:2004:3004:observer
```

## 3.启动服务节点以及观察节点状态
启动

![zkClusterStart01.png](img/08/zkClusterStart01.png)

观察角色

![zkClusterStart02.png](img/08/zkClusterStart02.png)

## 4.连接Zookeeper集群
```text
# 在ZooKeeper集群中，客户端不需要显式地连接观察者节点。因为观察者节点只是提供额外的备份服务，并且不参与写操作的投票，
# 所以客户端只需要连接到主节点和从节点即可。
./bin/zkCli.sh -server 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
```
