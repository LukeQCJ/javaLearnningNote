
上回讲主从复制时，我们留了一个问题，那就是手动切换主从库实在是太麻烦了。
别担心，Redis 的作者们早就为我们想到这个情况啦，哨兵功能就是为了这个而生的。

## 哨兵 Redis Sentinel
其实想想也知道，哨兵就是帮我们进行主从自动切换的功能。
当主库发生问题，无法访问时，哨兵会马上检测到，并排除网络问题在一定时间后确认主库确实是挂了，
这时，就会将某一台从库设置为新的主库，其它的从库也自动配置成连接到这台新的主库。

而当原来老的主库恢复访问后，哨兵会将它重新加入到集群中，但是，这个老的主库只能成为一台从库了。（新王登基，旧王称臣）。

怎么配置呢？和主从复制同样的简单的没朋友。貌似Redis的开发者们真的是非常友好啊，各种工具都是简单易用的。
你可以先在你的配置文件目录看看有没有一个叫做redis-sentinel.conf的文件，这个文件就是哨兵的配置文件。
接下来我们直接运行它，使用redis-server就可以，只是要加个 --sentinel参数。
如果没有的话，只需要创建一个配置文件，然后有下面这一行配置就行了。
```text
sentinel monitor mymaster 127.0.0.1 6379 1
```
它的意思是 sentinel monitor <哨兵集群名字> <监控的主库IP> <监控的主库端口> <选举主库时的需要几个哨兵同意> ，
这个意思比较清晰吧？我们示例中，哨兵名字就是 mymaster ，监控的是 6379 这个库，只需要1个哨兵同意就可以切换主库。
最后这个参数的具体意思我们后面再说。
```text
➜  redis-server redis-sentinel.conf --sentinel
………………
48882:X 22 Jun 2022 10:06:06.458 # +monitor master mymaster 127.0.0.1 6379 quorum 1
48882:X 22 Jun 2022 10:06:06.459 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
48882:X 22 Jun 2022 10:06:06.460 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
```
好了，看到上面的输出了之后，你的哨兵服务就已经正常启动了。除了redis-server命令外，使用redis-sentinel命令也可以启动哨兵实例。
接下来我们就直接可以进行测试了，先关闭主库，我的电脑是Mac下的brew安装的redis，自动运行的6379，所以我也用brew去关闭。
```text
// 关闭 6379
➜  brew services stop redis
Stopping `redis`... (might take a while)
==> Successfully stopped `redis` (label: homebrew.mxcl.redis)
```
接下来我们就要看看看 6380、6381 和哨兵实例都产生了什么变化。
如果你是直接运行，没有后台运行的话，就可以明显的看到这三个服务实例在输出内容。
```text
// 6380
………………………………
4449:S 24 Jun 2022 11:37:42.652 * Connecting to MASTER 127.0.0.1:6379
4449:S 24 Jun 2022 11:37:42.653 * MASTER <-> REPLICA sync started
4449:S 24 Jun 2022 11:37:42.653 # Error condition on socket for SYNC: Connection refused
4449:S 24 Jun 2022 11:37:42.919 * Connecting to MASTER 127.0.0.1:6381
4449:S 24 Jun 2022 11:37:42.919 * MASTER <-> REPLICA sync started
4449:S 24 Jun 2022 11:37:42.919 * REPLICAOF 127.0.0.1:6381 enabled (user request from 'id=5 addr=127.0.0.1:61954 laddr=127.0.0.1:6380 fd=9 name=sentinel-295debd7-cmd age=102 idle=0 flags=x db=0 sub=0 psub=0 multi=4 qbuf=329 qbuf-free=44721 argv-mem=4 obl=45 oll=0 omem=0 tot-mem=62500 events=r cmd=exec user=default redir=-1')
4449:S 24 Jun 2022 11:37:42.921 # CONFIG REWRITE executed with success.
4449:S 24 Jun 2022 11:37:42.921 * Non blocking connect for SYNC fired the event.
4449:S 24 Jun 2022 11:37:42.921 * Master replied to PING, replication can continue...
4449:S 24 Jun 2022 11:37:42.921 * Trying a partial resynchronization (request bacd481115f145bc151206de41a6d2f7d5b7f408:5346).
4449:S 24 Jun 2022 11:37:42.921 * Successful partial resynchronization with master.
4449:S 24 Jun 2022 11:37:42.921 # Master replication ID changed to b0b5a4d75d8ec37f3d12f83dc58db6713007446b
4449:S 24 Jun 2022 11:37:42.921 * MASTER <-> REPLICA sync: Master accepted a Partial Resynchronization.

// 6381
……………………
4447:S 24 Jun 2022 11:37:42.824 * Connecting to MASTER 127.0.0.1:6379
4447:S 24 Jun 2022 11:37:42.825 * MASTER <-> REPLICA sync started
4447:S 24 Jun 2022 11:37:42.825 # Error condition on socket for SYNC: Connection refused
4447:M 24 Jun 2022 11:37:42.860 * Discarding previously cached master state.
4447:M 24 Jun 2022 11:37:42.860 # Setting secondary replication ID to bacd481115f145bc151206de41a6d2f7d5b7f408, valid up to offset: 5346. New replication ID is b0b5a4d75d8ec37f3d12f83dc58db6713007446b
4447:M 24 Jun 2022 11:37:42.860 * MASTER MODE enabled (user request from 'id=7 addr=127.0.0.1:61952 laddr=127.0.0.1:6381 fd=9 name=sentinel-295debd7-cmd age=102 idle=0 flags=x db=0 sub=0 psub=0 multi=4 qbuf=202 qbuf-free=44848 argv-mem=4 obl=45 oll=0 omem=0 tot-mem=62500 events=r cmd=exec user=default redir=-1')
4447:M 24 Jun 2022 11:37:42.862 # CONFIG REWRITE executed with success.
4447:M 24 Jun 2022 11:37:42.921 * Replica 127.0.0.1:6380 asks for synchronization
4447:M 24 Jun 2022 11:37:42.921 * Partial resynchronization request from 127.0.0.1:6380 accepted. Sending 156 bytes of backlog starting from offset 5346.

// 哨兵 26379
…………………………
4456:X 24 Jun 2022 11:37:42.698 # +sdown master mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.698 # +odown master mymaster 127.0.0.1 6379 #quorum 1/1
4456:X 24 Jun 2022 11:37:42.698 # +new-epoch 1
4456:X 24 Jun 2022 11:37:42.699 # +try-failover master mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.701 # +vote-for-leader 295debd78028b566248887589442ee31cae931d5 1
4456:X 24 Jun 2022 11:37:42.701 # +elected-leader master mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.701 # +failover-state-select-slave master mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.792 # +selected-slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.792 * +failover-state-send-slaveof-noone slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.859 * +failover-state-wait-promotion slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.863 # +promoted-slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.863 # +failover-state-reconf-slaves master mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:42.918 * +slave-reconf-sent slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:43.917 * +slave-reconf-inprog slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:43.917 * +slave-reconf-done slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:44.018 # +failover-end master mymaster 127.0.0.1 6379
4456:X 24 Jun 2022 11:37:44.018 # +switch-master mymaster 127.0.0.1 6379 127.0.0.1 6381
4456:X 24 Jun 2022 11:37:44.018 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6381
```
从上面的输出可以看到。
从库6380输出：
```text
6380 会不停的输出 Error condition on socket for SYNC: Connection refused，
在哨兵切换之后， 紧接着会输出 Connecting to MASTER 127.0.0.1:6381 ，这就明显看出来了，
现在主库变成了 6381 ，6380 依然还是从库，只是换了主库。
```
从库6381输出：
```text
6381 同样也是先不停的输出连不上 6379 了，接着就和 6380 不同了，
它会多出一个 MASTER MODE enable .... 相关的内容，也就是开启主库模式了。
```
26379 是默认的哨兵的端口，它也发现 6379 挂了，现在就开始进行故障切换：
```text
failover-state-select-slave.. 表示要从之前主库的从库中找一个新的主库
selected-slave.... 说明这个 6381 合适
failover-state-send-slaveof-noone 是向新的主库发送指令，这个指令就是 SLAVEOF no one ，也就是把自己转成主库
promoted-slave... 表示所有的哨兵确认 6381 已经变成主库了
failover-state-reconf-slaves... 表示开始发送配置信息进行切换，也就是从库执行 SLAVEOF 127.0.0.1 6381
failover-end 表明切换完成
switch-master 就是说切换完成了，哨兵也开始监控新的主库
```
好了，现在来验证一下吧。进入 6381 看看 ROLE 命令有什么变化。
```text
// 6381
127.0.0.1:6381> role
1) "master"
2) (integer) 13801
3) 1) 1) "127.0.0.1"
2) "6380"
3) "13668"
```
接下来我们重启 6379 ，好嘛，太上皇也只能听命于新皇上了，之前的小甜甜也变成了现在的牛夫人了。
```text
// 重启 6379
➜  brew services start redis
==> Successfully started `redis` (label: homebrew.mxcl.redis)


// 6381
127.0.0.1:6381> role
1) "master"
2) (integer) 30932
3) 1) 1) "127.0.0.1"
2) "6380"
3) "30799"
2) 1) "127.0.0.1"
2) "6379"
3) "30799"
```

## PHP中使用
好用是好用，问题是我们在实际的业务应用中要怎么使用呢？
其实想想也知道，我们肯定要跟哨兵建立起连接，要不我们怎么知道现在谁是主库呢。
其实多数语言的客户端都提供了对应的对象或者方法，咱们还是用 PHP 来看一下。
```text
$sentinel = new \RedisSentinel('127.0.0.1', 26379);
$instance = $sentinel->getMasterAddrByName('mymaster');
// Array
// (
//     [0] => 127.0.0.1
//     [1] => 6381
// )

$redis = new \Redis();
$redis->connect($instance[0], (int)$instance[1]);
$info = $redis->info("Server");
echo $info['tcp_port']; // 6381
```
是的，在 PHP 的 Redis 扩展中，直接就有 RedisSentinel 对象，没有系统的学 Redis 之前，我真不知道还有这个。
通过这个对象的 getMasterAddrByName() 方法，传入我们在配置文件中设置的那个哨兵集群名称，就可以获得当前这个集群中主库的信息。

其实呀，哨兵实例也是可以通过 redis-cli 连接的，所以上面的 getMasterAddrByName() 也是一个 Redis 中的命令。
```text
➜ redis-cli -p 26379
127.0.0.1:26379> SENTINEL get-master-addr-by-name mymaster
1) "127.0.0.1"
2) "6379"
```

## 多哨兵选举
接下来，我们要继续看哨兵中更复杂的功能，就是多哨兵选举。这个内容呀，如果做过分布式开发的同学应该不会陌生。
就是说对于一个服务来说，当它的主机挂了，如果需要确定由哪个从机来顶替主机的位置话，往往会通过多个监控服务进行选举投票产生结果。
而且，大部分程序都会要求监控程序是单数，比如 3、5、7 个监控程序，因为如果是偶数的话，可能会出现选举结果票数相同的问题。

对于Redis中的哨兵来说，也是支持这种监控形式的。那么就有人要问了，为啥呀？还不是为了高可用和性能最佳化。
咱们先来看看怎么配置。先把redis-sentinel.conf中的配置修改下，改成需要2台哨兵同意才能进行切换。
```text
sentinel monitor mymaster 127.0.0.1 6379 2
```
然后第一步就是增加配置文件，没错，和多个Redis实例一起运行一样，哨兵也可以通过修改端口号进行多个实例同时运行。
第二步就是一个一个的启动起来。
```text
➜  vim redis-sentinel_80.conf
include /usr/local/etc/redis-sentinel.conf
port 26380
pidfile "/var/run/redis-sentinel_80.pid"

➜  vim redis-sentinel_81.conf
include /usr/local/etc/redis-sentinel.conf
port 26381
pidfile "/var/run/redis-sentinel_81.pid"

➜  redis-sentinel redis-sentinel_80.conf
➜  redis-sentinel redis-sentinel_81.conf
➜  redis-sentinel redis-sentinel.conf
```
注意看我上面的启动顺序，如果反了可是有问题的哦。如果先运行 26379 的主配置文件，那么其它两个新的哨兵是加不进来的。为什么呢？
因为哨兵在启动后就会往它们自己的配置文件中写入一些数据，在这其中有个 myid 是用于标识当前这台哨兵的 id 信息的。
上面的配置文件我们都是使用 include 去复用 26379 的配置文件的，如果先启动 26379 ，就会生成 26379 的 myid ，
另外两台一看有 myid 了，也就不会生成自己的了（仅限单机测试共用配置文件的情况下）。
因此，在整个哨兵集群中还是只会看到一台哨兵服务器。
```text
➜  vim redis-sentinel_80.conf
include /usr/local/etc/redis-sentinel.conf
port 26380
pidfile "/var/run/redis-sentinel_80.pid"
# Generated by CONFIG REWRITE
protected-mode no
user default on nopass sanitize-payload ~* &* +@all
dir "/private/tmp"
sentinel myid 03fbd63817310971644e879bd35bf5317b9ec726
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel config-epoch mymaster 4
sentinel leader-epoch mymaster 4
sentinel known-replica mymaster 127.0.0.1 6380
sentinel known-replica mymaster 127.0.0.1 6381
sentinel current-epoch 4
sentinel known-sentinel mymaster 127.0.0.1 26379 db7998b0415376b03b3b1daddf9192a1ab03db6c
sentinel known-sentinel mymaster 127.0.0.1 26381 07aefcb93ac7a6df48820873abb67d5c80e7a9e2
```
下次重启哨兵的时候，最好把所有哨兵的配置文件中的 # Generated by CONFIG REWRITE 以下的内容全部删掉。为什么呢？
你自己稍微修改下配置文件试试就知道。

好了，回归正题，最后一台哨兵启动起来之后，你可以看看三台哨兵输出的内容，都可以看到下面这些东西。
```text
5495:X 24 Jun 2022 14:22:44.506 # Sentinel ID is 4b45c338f5195b97bda8fec95d027ded95063a36
………………
5495:X 24 Jun 2022 14:22:45.451 * +sentinel sentinel 8b0cb442c190569b7a257bc62cad99b782511a45 127.0.0.1 26380 @ mymaster 127.0.0.1 6379
5495:X 24 Jun 2022 14:22:46.396 * +sentinel sentinel 273bd99ab214d8daaafbd6dd3822e851ef7a218e 127.0.0.1 26381 @ mymaster 127.0.0.1 6379
```
注意看，三台哨兵之间已经建立起连接了，不需要我们去设置什么 SLAVEOF 之类的命令，原理咱们下个小节再看。
然后它们的 myid 都不相同，现在这种情况下哨兵集群就搭建完成了。

然后你就可以测试一下了，先停掉主库，看看切换正常不正常，如果不出意外，应该是能正常切换的。
然后恢复主库，接下来停掉两台哨兵，再停掉主库，看看还能不能自动切换。

## 切换流程与通信
相信上面的配置大家都没问题，接下来就是真正的进阶部分了，整个哨兵的切换流程是怎么样的呢？
其实官方文档已经写得很详细了，咱们就再简单概括一下。
```text
每个哨兵以每秒钟一次的频率向它已配置的主库发送 PING 命令，这就是心跳检测
如果一个实例的 PING 回复时间超过了 redis-sentinel.conf 中 down-after-milliseconds指定的值，那么这个实例就会被标记为主观下线
当主库被标记为主观下线之后，那么所有的哨兵会继续按秒确认它是不是真的下线了，如果只有一台哨兵就不存在这个情况啦，在有多个哨兵的情况下，当所有哨兵都确认之后，它就会转换一个状态，被标记为客观下线
本来正常情况下，哨兵会每 10 秒向主从库发送 info 命令，但当出现客观下线的主库后，会变成 1 秒一次
最后，多数哨兵通过之后，执行切换流程，就是上面我们看到哨兵的日志中输出的流程
```
现在还有一个问题，主从配置的时候，我们需要通过 SLAVEOF来确定主从，但为啥多台哨兵就直接可以互相通信了呢？

其实呀，多台哨兵监控的都是同一个主库，在这种情况下，哨兵会默认每两秒向主库发送 sentinel:hello 命令，并携带自己的信息，
就是 IP 地址、端口、runid（myid）这些信息。主库也会返回所有监控它的哨兵的信息，这样一台哨兵也就知道了还有别的领导在盯着这货，
于是，哨兵和哨兵之间也建立起了联系。其实，这就是应用我们之前讲过的 Pub/Sub 机制来实现的，
大家都订阅同一个哨兵频道了（__sentinel__:hello）。同时，哨兵也会修改自己的配置信息，也就是我们前面说的，
配置文件中生成的那些内容，会加上别的哨兵的信息。因此，哨兵在重启的时候，特别是某台哨兵修改了配置文件的时候，
一定要去清理配置文件中之前生成的那些信息。

## 总结
配置不难吧，原理其实也不难理解，就是心跳检测有问题了就向别的从库发送命令实现自动切换。但是真正的源码实现可不是我这种水平的可以分析出来的，而且大部分的面试中，能说出哨兵怎么配，需要注意什么，大概的流程是怎样的，就已经能吊打不少同行了。

好了，主从配置这块的内容就没有了，下一篇我们将学习到的是更变态的内容，分布式的部署 Redis ，而且直接在分布式部署中，就解决了主从和哨兵自动切换的问题。也就是说，一套 Redis Cluster 分布式集群架构，就是自带主从、哨兵以及分布式部署能力的。期待值拉满没有？

## 参考文档：
https://redis.io/docs/manual/sentinel/