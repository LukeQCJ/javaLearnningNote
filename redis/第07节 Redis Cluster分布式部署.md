铺垫了那么久，总算到了 Cluster 的学习啦，开心撒花吧！！

久闻大名，但其实之前咱也从来没自己配过，借着这回学习的机会，深入地了解了一下 Redis 中这个自带的分布式方案。
之前的文章中就说过，Redis 的核心命令处理是单线程的，并且官方推荐的多线程处理方案就是使用分布式的方式，多来几个实例，
达到同时多个实例可以共同处理请求的目的。另外，由于 Redis 本身的核心业务是内存缓存应用，
单台服务器的内存上限以及 RDB 持久化都会对内存提出不小的挑战，而如果使用分布式，则可以很好地解决这个问题。

## 配置启动 Cluster
估计大家也想到了，还是通过配置文件来进行测试，如果是正式的线上环境，当然还是分散到不同的主机上更好，
但是我们自己练习的话，直接使用配置文件然后启动不同的端口主机就可以了。

首先需要修改主配置文件中的一个参数配置。
```text
cluster-enabled yes
```
将 cluster-enabled设置成 yes 之后，就开启了 Cluster 的功能。然后我们逐一对配置文件进行配置。
```text
// redis_80.conf
include /usr/local/etc/redis.conf
# master-slave
port 6380
pidfile "/var/run/redis_6380.pid"
dbfilename "dump6380.rdb"
#slaveof 127.0.0.1 6379

# cluster
cluster-config-file node-6380.conf
cluster-node-timeout 5000
```

基本还是沿用之前做主从复制时的配置，需要添加的是两个新的配置。
```text
cluster-config-file 指定 Cluster 启动后生成的配置文件
cluster-node-timeout 节点超时时间
```

这回我们需要配置 6 台实例，因此，需要 6 个配置文件，其它的配置文件都是类似的，只需要将对应的端口号和相关的文件名修改一下即可。
我这里是配置了 6379、6380、6381、7379、7380、7381 六台实例。

另外还需要注意的是，要去把之前的 rdb 文件和 aof 文件删掉，新建立分布式集群的时候，节点中是不能有数据的。

接下来所有实例全部启动，这个就不用多说了吧。然后我们就启动 Cluster 分布式集群，一行命令就搞定。
```text
➜  redis-cli --cluster create 127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:7379 127.0.0.1:7380 127.0.0.1:7381 --cluster-replicas 1

>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 127.0.0.1:7380 to 127.0.0.1:6379
Adding replica 127.0.0.1:7381 to 127.0.0.1:6380
Adding replica 127.0.0.1:7379 to 127.0.0.1:6381
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379
slots:[0-5460],[15495] (5461 slots) master
M: 1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380
slots:[5461-10922] (5462 slots) master
M: 47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381
slots:[10923-16383] (5461 slots) master
S: 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379
replicates 47cf0ad16ddb3b80d8055334434dffd7f74229cc
S: 0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380
replicates 1d48fc74ac96354c19a454bddbbf9b1dac62ac21
S: e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381
replicates 1adc88d15f82dc793e122cb274a1ba312c62d9c4
# 确认这样划分吗？
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
.
>>> Performing Cluster Check (using node 127.0.0.1:6379)
M: 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379
slots:[0-5460] (5461 slots) master
1 additional replica(s)
S: 0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380
slots: (0 slots) slave
replicates 1d48fc74ac96354c19a454bddbbf9b1dac62ac21
M: 47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381
slots:[10923-16383] (5461 slots) master
1 additional replica(s)
M: 1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380
slots:[5461-10922] (5462 slots) master
1 additional replica(s)
S: 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379
slots: (0 slots) slave
replicates 47cf0ad16ddb3b80d8055334434dffd7f74229cc
S: e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381
slots: (0 slots) slave
replicates 1adc88d15f82dc793e122cb274a1ba312c62d9c4
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
好长一串吧？输出的内容也很多，我们一个一个来看。
```text
redis-cli --cluster create 使用客户端命令行工具，
然后添加参数 --cluster create 就可以创建 Cluster ，后面跟着所有的主机信息。

--cluster-replicas 1 表示有一个复制节点，也就是从机，
这样 6 台实例就会划分成三主三从（强大不？），你也可以配置多台从机，设置成 2 的话就是一个主节点有两个从节点
，这样的话三主节点的话就一共需要 9 台实例。

Can I set the above configuration? (type 'yes' to accept): 会让你输入 yes 或 no ，
表示的就是是不是同意上面输出的划分方式，一般我们 yes 就行了

M 开头的表示主机，S 开头的表示从机。
```
好了，这就配置完了。不算复杂，也没踫到什么坑，总体来说就和之前的主从与哨兵一样，非常傻瓜式的配置，对于我这种低端码农来说非常友好。

配置完了，那就来验证一下吧。redis-cli 命令直接连接任意一台实例即可，但是要增加一个参数 -c，否则客户端还是会以单实例的方式去连接，
连接是没问题的，但是使用起来会有问题，你可以自己去试试哦。
```text
➜  etc redis-cli -c -p 6381

127.0.0.1:6381> set a 111
OK
127.0.0.1:6381> set b 121
-> Redirected to slot [3300] located at 127.0.0.1:6379
OK
127.0.0.1:6379> get b
"121"
127.0.0.1:6379> get a
-> Redirected to slot [15495] located at 127.0.0.1:6381
"111"

127.0.0.1:6381> role
1) "master"
2) (integer) 374
3) 1) 1) "127.0.0.1"
2) "7379"
3) "374"
```
我们直接连接的是6381的实例，然后测试添加数据，a 数据没问题，但是设置 b 数据的时候，貌似好像发生了其它操作，
我们的连接也从 6381 变成了 6379 ，然后在 6379 去 GET a ，又跳回了 6381 ，这是为啥呢？不急，我们后面再说。
再看一下角色，当前的 6381 是主机，有一台从机是 7379 。

上面的配置信息不知道你有没有详细地去看，如果没有的话，还可以通过 CLUSTER NODES命令来查看当前分布式集群中所有的节点信息。
```text
127.0.0.1:6379> CLUSTER nodes
0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380@17380 slave 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 0 1656396446877 1 connected
1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379@16379 myself,master - 0 1656396445000 1 connected 0-5460
47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381@16381 master - 0 1656396445865 3 connected 10923-16383
1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380@16380 master - 0 1656396446000 2 connected 5461-10922
8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379@17379 slave 47cf0ad16ddb3b80d8055334434dffd7f74229cc 0 1656396445000 3 connected
e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381@17381 slave 1adc88d15f82dc793e122cb274a1ba312c62d9c4 0 1656396446000 2 connected
```
很明显，7379是一台从机，它的SLAVE指向的是 47cf0ad16ddb3b80d8055334434dffd7f74229cc 这个实例 ID ，
而这个实例 ID 对应的正是 6381 。

另外，还有 CLUSTER INFO命令可以查看集群的整体信息。
```text
127.0.0.1:6380> CLUSTER info
cluster_state:ok   # 状态
cluster_slots_assigned:16384 # 槽数
cluster_slots_ok:16384 # 已分配槽数
cluster_slots_pfail:0
cluster_slots_fail:0
cluster_known_nodes:6 # 已知节点
cluster_size:3 # 3个主实例
cluster_current_epoch:6
cluster_my_epoch:2
cluster_stats_messages_ping_sent:9948
cluster_stats_messages_pong_sent:9215
cluster_stats_messages_meet_sent:1
cluster_stats_messages_sent:19164
cluster_stats_messages_ping_received:9215
cluster_stats_messages_pong_received:9949
cluster_stats_messages_received:19164
```

## Slot槽与批量操作
好了，现在就要解释 Cluster 中非常重要的一个关键知识点了。那就是数据分槽，或者叫键的分槽更明确一些。

我们操作的所有的Key，通过一个算法计算到一个大小为16384的槽空间中。
在CLUSTER NODES的节点信息中，可以看到每台主库后面都跟着 0-5460、10923-16383、5461-10922这样的数字，
它们代表的就是这个主库所占用的槽空间。比如说 6379 占用的是 0-5460这个区间，
如果我们的 Key 通过一个算法计算后得到的结果是 100 的话，那么正好就落在这个区间，
这个 Key 就会被存储在 6379 这台主机上。因此，在操作相关的 Key 时，Cluster 集群会自动为我们切换主库，
也就是上面测试时会来回跳的原因。最终，根据不同的 Key，数据会被分散地存储到不同的节点上，从而形成分布式集群。

那么这个算法是什么算法呢？其实非常简单。
```text
CRC16(KEY)%16384
```
在 PHP 中，也有类似的 crc32() 这个函数，大家可以自己试一下。
CRC 算法本质上是一个对数据进行循环校验位计算的算法，简单地理解的话，就是它可以把字符串变成一串数字，类似于 md5() 这样。

咱们来试试，CLUSTER KEYSLOT 可以查看给定的 Key 计算出来的结果。
```text
127.0.0.1:6379> CLUSTER KEYSLOT a
(integer) 15495
127.0.0.1:6379> CLUSTER KEYSLOT b
(integer) 3300
```
是不是，b 这个 Key 没超过 5460 ，所以它会存放到 6379 节点上。

大概原理清楚了吧？这种 Key 的分布划分其实也是有问题的，那就是分布不均衡。
当然，这也是很多分布式集群的通病，假如大部分 Key 都机缘巧合地被划分到了 6379 上，就会造成 6379 很大也很繁忙，其它节点则很空闲。
这也不只是 Redis 的问题，包括 MySQL 的分库分表也会有这样的问题，怎么做均衡要从业务上考虑，这不是我们现在讨论的重点了。

不知道你发现了一个问题没？如果是设置或者获取一条数据都需要来回跳的话，那么 MSET 这种命令还能用吗？
```text
127.0.0.1:6379> MSET a 111 b 222
(error) CROSSSLOT Keys in request don't hash to the same slot
```
抱歉，直接就报错了，不同的键要放到不同的槽中，直接把Redis干懵了，所以它说咱不能这么玩。不过另有一种方式可以达到这样的效果。
```text
127.0.0.1:6379> mset {user:1:}aa 11 {user:1:}bb 22
OK
127.0.0.1:6379> get {user:1:}aa
"11"
127.0.0.1:6379> mset {user:2:}aa 111 {user:2:}bb 222
-> Redirected to slot [5542] located at 127.0.0.1:6380
OK
127.0.0.1:6380> mget {user:2:}aa {user:2:}bb
1) "111"
2) "222"
```
上面的 {} ，代表就是【键哈希标签（Keys hash tags）】，只要出现 {} ，就会按照花括号里面的内容进行Key的计算，
将这些 Key 放到同一个主库实例中。

放后面也是可以的。
```text
127.0.0.1:7379> set test:1:{abc} 111
OK
127.0.0.1:6380> set test:2:{abc} 222
OK
127.0.0.1:6380> set test:2:{abc}{d} 222
OK
127.0.0.1:6380> keys *
1) "{user:2:}aa"
2) "{user:2:}bb"
3) "test:2:{abc}"
4) "test:1:{abc}"
5) "test:2:{abc}{d}"
```
另外注意的是，它必须是完整的一对，而且是以第一对出现的为基准，上面最后一条的 {abc}{d} 其实还是以 {abc} 为基准的。
```text
127.0.0.1:6381> mset test:3:{a 111 test:4:{a 222
(error) CROSSSLOT Keys in request don't hash to the same slot

127.0.0.1:6381> mset test:3:{a{b} 111 test:4:{a{b} 222
OK
```
注意再注意，事务 和 管道 操作也要考虑这个来回切换的问题，大家自己试试哦。

## 故障测试
好了，数据是分布了，概念我们也解释了，还剩下的一块就是它的主从切换了。
前面我们配置了 6 台实例，自动帮我们划分成了三主三从。然后在客户端操作的时候其实我们一直来回的是在主库之前横跳，
大家可以试试，你去连接一台从库，只要运行一个命令，马上又会跳到一台主库上。那么从库有啥用？

其实呀，从库最主要的是对于我们的高可用进行保障的。
不像MySQL，我们在读多写少的场景下需要大量的负载均衡到从库上读数据。
对于Redis来说，本身不管读写的速度都是非常快的，因此，在Cluster中，从库更多的是在主库出问题后能够马上顶上来保障服务不中断的。

好了，不多说，直接测试吧，先KILL掉一台主机，我们直接关了6381好了。接下来看下集群信息。
```text
127.0.0.1:6379> CLUSTER NODES
0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380@17380 slave 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 0 1656408009782 1 connected
1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379@16379 myself,master - 0 1656408010000 1 connected 0-5460
# 6381
47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381@16381 master,fail - 1656407991543 1656407989000 3 disconnected
1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380@16380 master - 0 1656408011000 2 connected 5461-10922
# 7379
8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379@17379 master - 0 1656408008769 7 connected 10923-16383
e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381@17381 slave 1adc88d15f82dc793e122cb274a1ba312c62d9c4 0 1656408011812 2 connected

```
好吧，我这里有点快，直接就已经切换完成了。之前 6381 是主库，7379 是从库。
现在显示 6381 已经 disconnected 了，7379 也变成了 master 。好嘛，直接把主从复制和哨兵的活都干了。

OK，重启恢复 6381 试试。
```text
127.0.0.1:6379> CLUSTER NODES
0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380@17380 slave 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 0 1656408085000 1 connected
1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379@16379 myself,master - 0 1656408084000 1 connected 0-5460
# 6381
47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381@16381 slave 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 0 1656408085729 7 connected
1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380@16380 master - 0 1656408084000 2 connected 5461-10922
#7379
8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379@17379 master - 0 1656408084000 7 connected 10923-16383
e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381@17381 slave 1adc88d15f82dc793e122cb274a1ba312c62d9c4 0 1656408084718 2 connected
```
和哨兵一样，6381 变成了 7379 的小弟了。

具体的切换步骤就不进行分析了，之前我们已经详细的看过哨兵的切换过程，它们之间大同小异，
毕竟 Cluster 整体的后台架构还是要复杂一些，所以相互之间的通信步骤和选举机制也会更复杂一些。

## 添加删除节点与分槽操作
先来看怎么添加节点，我们直接看一下 --cluster 命令参数还有哪些子参数可以用。
```text
➜ redis-cli --cluster help
Cluster Manager Commands:
create         host1:port1 ... hostN:portN
--cluster-replicas <arg>
check          host:port
--cluster-search-multiple-owners
info           host:port
fix            host:port
--cluster-search-multiple-owners
--cluster-fix-with-unreachable-masters
reshard        host:port
--cluster-from <arg>
--cluster-to <arg>
--cluster-slots <arg>
--cluster-yes
--cluster-timeout <arg>
--cluster-pipeline <arg>
--cluster-replace
rebalance      host:port
--cluster-weight <node1=w1...nodeN=wN>
--cluster-use-empty-masters
--cluster-timeout <arg>
--cluster-simulate
--cluster-pipeline <arg>
--cluster-threshold <arg>
--cluster-replace
add-node       new_host:new_port existing_host:existing_port
--cluster-slave
--cluster-master-id <arg>
del-node       host:port node_id
call           host:port command arg arg .. arg
--cluster-only-masters
--cluster-only-replicas
set-timeout    host:port milliseconds
import         host:port
--cluster-from <arg>
--cluster-from-user <arg>
--cluster-from-pass <arg>
--cluster-from-askpass
--cluster-copy
--cluster-replace
backup         host:port backup_directory
help

For check, fix, reshard, del-node, set-timeout you can specify the host and port of any working node in the cluster.

Cluster Manager Options:
--cluster-yes  Automatic yes to cluster commands prompts
```
是不是感觉又发现新大陆了，上次我们只是用了一个 create 命令而已，还有这一大堆命令呢。
别的不说，从名字就发现了 add-node是添加节点，del-node是删除节点。

### 添加新节点
首先还是先建立配置文件和启动普通实例，这一块就不多说了，直接配置和启动 6382 以及 7382 实例。然后我们先添加主节点。
```text
➜ redis-cli --cluster add-node 127.0.0.1:6382 127.0.0.1:6379
>>> Adding node 127.0.0.1:6382 to cluster 127.0.0.1:6379
>>> Performing Cluster Check (using node 127.0.0.1:6379)
M: 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379
slots:[0-5460] (5461 slots) master
1 additional replica(s)
S: 0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380
slots: (0 slots) slave
replicates 1d48fc74ac96354c19a454bddbbf9b1dac62ac21
S: 47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381
slots: (0 slots) slave
replicates 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed
M: 1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380
slots:[5461-10922] (5462 slots) master
1 additional replica(s)
M: 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379
slots:[10923-16383] (5461 slots) master
1 additional replica(s)
S: e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381
slots: (0 slots) slave
replicates 1adc88d15f82dc793e122cb274a1ba312c62d9c4
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
>>> Send CLUSTER MEET to node 127.0.0.1:6382 to make it join the cluster.
[OK] New node added correctly.
```
命令很简单，add-node 后面跟着两个参数，第一个是新节点，第二是已有集群中随便一个节点就可以了。
这就添加完成了，我们看一下当前集群的信息。
```text
➜ redis-cli -p 6382
127.0.0.1:6382> CLUSTER NODES
47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381@16381 slave 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 0 1656470300000 7 connected
0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380@17380 slave 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 0 1656470300620 1 connected
# 6382
19fbf50b3536d2ab18a116bfa1e15f424df9af25 127.0.0.1:6382@16382 myself,master - 0 1656470299000 0 connected
1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379@16379 master - 0 1656470300116 1 connected 0-5460
8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379@17379 master - 0 1656470301000 7 connected 10923-16383
1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380@16380 master - 0 1656470300620 2 connected 5461-10922
e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381@17381 slave 1adc88d15f82dc793e122cb274a1ba312c62d9c4 0 1656470301632 2 connected
```
可以看到，当前的节点已经连接上了，啥都正常，但是，它没有槽的信息。
如果没有槽信息，Key 也就不会分配到这个节点上，目前这个节点还是废物阶段，那么我们就来配置它的 Slot 。

### 为新节点分配槽
为节点分配槽信息，使用的是 --cluster reshard命令参数。
```text
➜ redis-cli --cluster reshard 127.0.0.1:6382
## 当前集群信息
>>> Performing Cluster Check (using node 127.0.0.1:6382)
M: 19fbf50b3536d2ab18a116bfa1e15f424df9af25 127.0.0.1:6382
slots: (0 slots) master
S: 47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381
slots: (0 slots) slave
replicates 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed
S: 0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380
slots: (0 slots) slave
replicates 1d48fc74ac96354c19a454bddbbf9b1dac62ac21
M: 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379
slots:[333-5460] (5128 slots) master
1 additional replica(s)
M: 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379
slots:[11256-16383] (5128 slots) master
1 additional replica(s)
M: 1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380
slots:[5795-10922] (5128 slots) master
1 additional replica(s)
S: e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381
slots: (0 slots) slave
replicates 1adc88d15f82dc793e122cb274a1ba312c62d9c4
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
## 想要移动多少到新的节点？
How many slots do you want to move (from 1 to 16384)? 1000
## 接收的 ID 是多少？就是新添加节点的 ID
What is the receiving node ID? 19fbf50b3536d2ab18a116bfa1e15f424df9af25
## 确认槽点的来源，all 表示所有节点平均搞 1000 个过来
Please enter all the source node IDs.
Type 'all' to use all the nodes as source nodes for the hash slots.
Type 'done' once you entered all the source nodes IDs.
Source node #1: all
## 开始检查
……………………
Moving slot 6119 from 1adc88d15f82dc793e122cb274a1ba312c62d9c4
Moving slot 6120 from 1adc88d15f82dc793e122cb274a1ba312c62d9c4
Moving slot 6121 from 1adc88d15f82dc793e122cb274a1ba312c62d9c4
Moving slot 6122 from 1adc88d15f82dc793e122cb274a1ba312c62d9c4
Moving slot 6123 from 1adc88d15f82dc793e122cb274a1ba312c62d9c4
Moving slot 6124 from 1adc88d15f82dc793e122cb274a1ba312c62d9c4
……………………
## 确定按这个分槽计划来？
Do you want to proceed with the proposed reshard plan (yes/no)? yes
……………………
Moving slot 11253 from 127.0.0.1:7379 to 127.0.0.1:6382:
Moving slot 11254 from 127.0.0.1:7379 to 127.0.0.1:6382:
……………………
```
内容比较长，所以我也添加了注释，不过咱们还是来一条一条的看一下。
```text
How many slots do you want to move (from 1 to 16384)? 1000 表示要给它多少槽，这里我给了1000个槽过来
What is the receiving node ID? 谁来接收这1000个槽，当然是新添加的这个节点的 ID
Please enter all the source node IDs 从哪些节点分配槽过来？all 就是其它所有节点平均分配，也可以填 ID 最后再加个 done 完成这一块的配置
接着输出一堆移动的计划，比如 6119 从 1adc88d15f82dc793e122cb274a1ba312c62d9c4 也就是 6379 分过来
Do you want to proceed with the proposed reshard plan (yes/no)? yes 确定这么分？yes 之后就开始正式分槽了，数据也就跟着槽带过来了
```
好了，现在看下 6382 上是不是有数据了。
```text
➜  redis-cli -c -p 6382
127.0.0.1:6382> keys *
1) "{user:1:}bb"
2) "{user:1:}aa"
3) "{user:2:}aa"
4) "{user:2:}bb"

127.0.0.1:6382> CLUSTER NODES
47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381@16381 slave 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 0 1656471120293 7 connected
0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380@17380 slave 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 0 1656471121811 1 connected
19fbf50b3536d2ab18a116bfa1e15f424df9af25 127.0.0.1:6382@16382 myself,master - 0 1656471120000 8 connected 0-332 5461-5794 10923-11255
1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379@16379 master - 0 1656471120000 1 connected 333-5460
8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379@17379 master - 0 1656471119788 7 connected 11256-16383
1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380@16380 master - 0 1656471121304 2 connected 5795-10922
e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381@17381 slave 1adc88d15f82dc793e122cb274a1ba312c62d9c4 0 1656471121000 2 connected
```
再看一下 NODES 信息，四台主库，新添加的主库槽信息有点乱，是啊，毕竟是其它三台平均分过来的，
所以新主库的槽信息是 0-332 5461-5794 10923-11255这个样子的。

### 添加新的从库
新来的兄弟有点孤单啊，咱们给它把从库加上怎么样？让 7382 成为它的从库，非常简单。
```text
➜  redis-cli --cluster add-node 127.0.0.1:7382 127.0.0.1:6382 --cluster-slave --cluster-master-id 19fbf50b3536d2ab18a116bfa1e15f424df9af25
```
再来看 NODES 的信息。嗯，完美，数据也同步复制到从库了。
```text
➜ redis-cli -c -p 7382
127.0.0.1:7382> CLUSTER NODES
47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381@16381 slave 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 0 1656471293998 7 connected
8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379@17379 master - 0 1656471294000 7 connected 11256-16383
19fbf50b3536d2ab18a116bfa1e15f424df9af25 127.0.0.1:6382@16382 master - 0 1656471294000 8 connected 0-332 5461-5794 10923-11255
e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381@17381 slave 1adc88d15f82dc793e122cb274a1ba312c62d9c4 0 1656471292583 2 connected
0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380@17380 slave 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 0 1656471293593 1 connected
b09db43f11a3cd0b3e994d8780bca6b7faf4eb85 127.0.0.1:7382@17382 myself,slave 19fbf50b3536d2ab18a116bfa1e15f424df9af25 0 1656471293000 8 connected
1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380@16380 master - 0 1656471294099 2 connected 5795-10922
1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379@16379 master - 0 1656471292583 1 connected 333-5460

127.0.0.1:7382> keys *
1) "{user:2:}aa"
2) "{user:2:}bb"
3) "{user:1:}aa"
4) "{user:1:}bb"
```

## 删除节点
好了，接下来咱们就再试试怎么删除刚刚新添加的节点。使用的是 --cluster del-node 命令参数。
```text
➜ redis-cli --cluster del-node 127.0.0.1:7382 b09db43f11a3cd0b3e994d8780bca6b7faf4eb85
>>> Removing node b09db43f11a3cd0b3e994d8780bca6b7faf4eb85 from cluster 127.0.0.1:7382
>>> Sending CLUSTER FORGET messages to the cluster...
>>> Sending CLUSTER RESET SOFT to the deleted node.

➜ redis-cli -c -p 7382
127.0.0.1:7382> CLUSTER NODES
b09db43f11a3cd0b3e994d8780bca6b7faf4eb85 127.0.0.1:7382@17382 myself,master - 0 1656471407000 0 connected
```
删除从库没压力，直接就删了，再登进 7382 看的话，它现在就是自己一个孤家寡人了。当然，原来的集群中也没有它的信息了。
```text
➜ redis-cli -c -p 6382
127.0.0.1:6382> CLUSTER NODES
47cf0ad16ddb3b80d8055334434dffd7f74229cc 127.0.0.1:6381@16381 slave 8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 0 1656471472540 7 connected
0bef7a24915f5e3c69cc70e7e51443f5a214cf7b 127.0.0.1:7380@17380 slave 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 0 1656471472642 1 connected
19fbf50b3536d2ab18a116bfa1e15f424df9af25 127.0.0.1:6382@16382 myself,master - 0 1656471471000 8 connected 0-332 5461-5794 10923-11255
1d48fc74ac96354c19a454bddbbf9b1dac62ac21 127.0.0.1:6379@16379 master - 0 1656471472000 1 connected 333-5460
8d0be91ca6c5e809f3915e23a2ab26d8b14fe5ed 127.0.0.1:7379@17379 master - 0 1656471472000 7 connected 11256-16383
1adc88d15f82dc793e122cb274a1ba312c62d9c4 127.0.0.1:6380@16380 master - 0 1656471471126 2 connected 5795-10922
e39a71d3484308bcc1fda21d5f919b9e707e21f0 127.0.0.1:7381@17381 slave 1adc88d15f82dc793e122cb274a1ba312c62d9c4 0 1656471472136 2 connected
```
好了，我们再来删 6382 这台主库。

```text
➜ redis-cli --cluster del-node 127.0.0.1:6382 19fbf50b3536d2ab18a116bfa1e15f424df9af25
>>> Removing node 19fbf50b3536d2ab18a116bfa1e15f424df9af25 from cluster 127.0.0.1:6382
[ERR] Node 127.0.0.1:6382 is not empty! Reshard data away and try again.
```
踫到问题了吧，现在的 6382 上是有槽数据的，如果已经分配了槽，那么这个节点是不能直接被删除的。
首先，数据会丢，其次，这些槽也不知道该跟谁了。因此，我们需要先把槽转给别人。还是使用 --cluster reshard来操作。
```text
………………
# 要移动多少槽？
How many slots do you want to move (from 1 to 16384)? 16384
# 给谁？注意，这里不能给 6382 了，要把它的给别人
What is the receiving node ID? 1d48fc74ac96354c19a454bddbbf9b1dac62ac21
# 从 6382 上把槽拿出来
Please enter all the source node IDs.
Type 'all' to use all the nodes as source nodes for the hash slots.
Type 'done' once you entered all the source nodes IDs.
Source node #1: 19fbf50b3536d2ab18a116bfa1e15f424df9af25
Source node #2: done
………………
```
这里就没有贴那么多代码了，只是贴上来关键部分。
```text
How many slots do you want to move (from 1 to 16384)? 这里要填的只要比 6382 的大就行了，意思就是要把 6382 的槽清空
What is the receiving node ID? 这里也不能再填 6382 的 ID 了，我们要把它的槽给别人
Please enter all the source node IDs 现在这里填的 6382 的
```
其它的选项就和之前添加新节点时一样了，这样原来 6382 的槽就转给了 1d48fc74ac96354c19a454bddbbf9b1dac62ac21 也就是 6379 。好了，现在你可以用 --cluster nodes查看一下集群信息，确认没问题的话再删除节点就没问题了。
```text
➜ redis-cli --cluster del-node 127.0.0.1:6382 90ac7cdc531bdbb88d47a1980cdf9535cede0b7f
>>> Removing node 90ac7cdc531bdbb88d47a1980cdf9535cede0b7f from cluster 127.0.0.1:6382
>>> Sending CLUSTER FORGET messages to the cluster...
>>> Sending CLUSTER RESET SOFT to the deleted node.
```
上面的分槽方式，会将之前我们分配的 1000 个槽全部转给 6379 ，这样其实 6379 就比另外两个槽的数据多了。其实正常转的话，我们应该多次去分槽，然后 How many slots do you want to move (from 1 to 16384)? 时指定大小，比如要给 6379 的就只分配 334 个，因为之前 6379 也是分了 0-333这些槽过来。另外两台也是类似的操作，相当于就是把之前拿过来的再平均还回去了。不仅限于 3 个节点，N 个节点直接就操作 N 次，然后还 槽数/N 的数量回去就好了。

## PHP 连接集群
Cluster 分布式集群的学习就完成了，最后再说一点就是在 PHP 这种客户端上如何连接或者使用 Cluster 呢？
非常简单，phpredis 扩展或者 predis 扩展都有类似的对象可以直接使用，我们直接看 phpredis 的使用。不清楚 php redis 和 predis 的小伙伴自己百度下，之前我也在 Laravel 系列的文章课程中讲过 【Laravel系列4.7】连接redis以及缓存应用https://mp.weixin.qq.com/s/DF3oo3c4RTfU_WvBSKXrpA 。
```text
$c = new \RedisCluster(null, ['127.0.0.1:6379','127.0.0.1:6380','127.0.0.1:6381',
'127.0.0.1:7379','127.0.0.1:7380','127.0.0.1:7381']);
var_dump($c);
// object(RedisCluster)#1 (0) {
// }
```
RedisCluster 对象就是连接 Cluster 集群的，它的参数第一个 null 是一个名称的意思，啥意思？
就是其实我们可以在 .ini 的配置文件中配置 redis 的一些相关信息。然后在这里可以直接通过这个名称找到相关的配置，
这样就不用第二个参数了。
```text
# 在 redis.ini or php.ini 文件中
redis.clusters.seeds = "mycluster[]=localhost:6379&test[]=localhost:6380"
redis.clusters.timeout = "mycluster=5"
redis.clusters.read_timeout = "mycluster=10"

# 在代码中
$redisClusterPro = new RedisCluster('mycluster');
$redisClusterDev = new RedisCluster('test');
```
从 RedisCluster 对象输出的结果来看，它里面直接就显示了当前主库的信息。另外还有一点就是，其实我们第二个参数不用写那么多，
只要随便写一个当前集群中的节点信息就可以了。

$c = new \RedisCluster("aaa", ['127.0.0.1:6379']);
然后试试吧，和普通的 Redis 对象一样的操作就行了。
```text
$c->set("a1", "111");
$c->set("b1", "111");
$c->incr("b1");
$c->lPush("c1", 1, 2, 3);

print_r($c->get("a1")); echo PHP_EOL;
print_r($c->get("b1")); echo PHP_EOL;
print_r($c->lRange("c1", 0, -1));
// 111
// 112
// Array
// (
//   [0] => 3
//   [1] => 2
//   [2] => 1
// )
```

去命令行看看，数据肯定是存在的啦！
```text
127.0.0.1:6379> get a1
-> Redirected to slot [7785] located at 127.0.0.1:6380
"111"
```

## 总结
完了？还没完，下一篇我们还要继续学习怎么动态添加删除节点，怎么在 PHP 中连接集群。
最后再总结一下 Redis Cluster 的一些特点吧。
```text
高可用：主从、哨兵都有了
高并发：数据分布存储了
分布数量：按槽数来说的话，理论上可以分 16384 台实例，官方说 1000 台良好运行，如果更多的话，可能会产生严重的通信开销导致性能损失及带宽占用
Solt 槽的 CRC16 算法值得学习，可以应用在日常的 MySQL 分表之类的业务开发中
批量操作需要考虑 Key 分布的问题，要使用键哈希标签
不支持默认的那个 16 表库空间了，默认全是 0 库
复制节点只能有一层，也就不能从库再挂从库了
```

## 参考文档：
https://redis.io/docs/manual/scaling/