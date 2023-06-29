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