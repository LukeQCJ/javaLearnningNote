# 第03节 Redis内存回收策略

今天的内容很偏理论，不过也只是对于官方文档的一个补充而已，所以大家也不必有很大的心理负担。
理论的东西，多看几遍就理解了，读书百遍其义自现嘛。即使不理解，当八股文背下来也没啥坏处。

## 内存过期回收算法
目前市面上，你能听到的过期回收算法基本就是两种，一个叫 LRU一个叫 LFU。
当然，还有别的算法，不过我也不知道，不知为不知，咱们就先来学习知道的东西。

它们俩都是内存管理的一种【页面置换】算法。就是说，【把没用的内存页转换出来】，或者说是淘汰掉。
假如说，如果我们没有这样的淘汰机制，那么内存里的数据一直存在，最后的结果是啥？当然就是内存被撑爆了。

一般情况下，我们会为缓存数据设置过期时间，但是，假如所有数据都没有设置过期时间呢？这样的数据一直放着撑爆内存是早晚的事嘛。
所以说，我们就需要有一种过期回收的淘汰机制，避免内存无限制的增长最终导致服务完全不可用。

LRU：最近最少使用算法（Least Recently Used），意思就是【直接淘汰最长时间没有被使用的内存页】。

LFU：最不经常使用算法（Least Frequently Used），【淘汰一段时间内的，使用次数最少的内存页】。

在 Redis4.0 之前，Redis 只支持 LRU ，同时 LRU 也是 Memcached 的默认算法。
但是，LRU有一个很严重的问题，那就是一个N久不被使用的页，突然被使用了一下，它就变成最新的数据了。然后可能又是过N久才会使用到一次。
而LFU则是在一定时间范围内对访问数据进行了限定。因此，如果是热点数据比较多，使用LFU会更好一些。
而热点不那么明显，或者热点数据总量并不多的话，使用LRU也是完全没问题的，并且这也是多数缓存系统所推荐的算法。

那么在Redis中如何来进行相关的配置呢？我们一个一个来看。

### maxmemroy
这个配置之前其实我们也讲过，它是设置当前Redis实例最大可用内存限制。
比如我们设置成100MB的话，那么当前这个实例中就只能存放100MB的数据。如果不设置它的话，就会一直向系统申请内存，
也就是以整个机器和系统的可用内存为边界的。

通常情况下，我们会单独用一台机器专门部署Redis，所以这个值可设可不设。
通过CONFIG相关的命令也可以进行运行时的配置。比如我本地就没有设置这个值：
```text
127.0.0.1:6379> CONFIG GET maxmemory
1) "maxmemory"
2) "0"
```

### maxmemory-policy
在之前的文章中，其实我们已经学过这个配置，当时是讲一个LFU相关的命令，不知道大家还记得不，
文章在这里Redis基础学习：通用命令（二）https://mp.weixin.qq.com/s/eiMRo_iJv0EjgGCBFTrfLg 。

当时我们是为了演示 OBJECT FREQ 命令查看到 LFU 算法的引用计数情况来修改的这个配置。
它的实际意思就是当达到maxmemory设置的最大内存之后，使用何种方式进行内存的回收淘汰。具体可设置的值包括下面几种：
```text
1) noeviction: 内存达到限制后，新添加进来的值不会被保存。如果是做了主从的数据库，则对主库适用。

2) allkeys-lru: 保留最近使用的，删除最近最少使用的。（LRU）
3) allkeys-lfu: 保留使用频率高的，删除使用频率低的。（LFU）
4) allkeys-random: 随机删除，为新添加的数据腾出空间。

5) volatile-lru: 删除在过期集合中的最少使用的。（LRU）
6) volatile-lfu: 删除在过期集合中的使用频率低的。（LFU）
7) volatile-random: 随机删除在过期集合中的。
8) volatile-ttl: 删除使用频率最低的，并在过期集合中的，并删除最短生存时间的。
```

啥叫过期集合？在我们DEL一条数据后，这条数据所在的内存页并不是马上就被回收淘汰的。
这些数据会进入到一个过期集合中，或者说是相关的KEY会被设置为已经过期的。

在Redis中，EXPIRE这类命令能关联到一个有额外内存开销的KEY。当KEY执行过期操作时，Redis会确保按照规定时间删除它们。

上面带volatile的都是优先去处理这些过期集合中的数据，另外Redis也会在后台有一个线程专门去处理已经过期的KEY，但是，内存是页的，
假设当前的KEY所在的页全部都过期了，那么这个内存页就会直接被回收，所以，如果这个内存页中还有其它的数据是没有过期的，
那么这个内存页是不会被回收的。因此，如果你发现Redis占用的内存大于了设置的maxmemory时也不要惊讶。
此外，如果所有的KEY都没有设置过期时间，那么这些配置和noeviction是没啥区别的。

### 怎么配置？
```text
127.0.0.1:6379> CONFIG GET maxmemory-policy
1) "maxmemory-policy"
2) "noeviction"
```
默认情况下，maxmemory-policy走的是noeviction这个配置。也就是没有任何算法来处理，就是添加不了新数据了。
具体要配置那种，还是要根据业务情况来定。

有【热点数据】，肯定是allkeys-lfu，当然allkeys-lru也没问题。
数据小，碎片化多，用allkeys-lfu，大的文章之类的单个KEY的数据比较大，可以考虑allkeys-lru。

随机访问，或者循环访问，直接就allkeys-random好了。

大部分业务数据都有设置过期时间，那就volatile-ttl。

### 回收的工作过程
对于回收的操作，可以分几步来看：
```text
1) 客户端运行添加命令
2) Redis检查内存使用情况，如果大于maxmemory的限制，则根据maxmemory-policy的策略进行淘汰。
3) 执行命令
```
也就是说，当内存到达maxmemory的边界时，我们会不断的在这个边界范围内来回跳转，超过它，然后回收后又返回到限制内。
如果有某个命令导致大量的内存被使用，那么内存限制就会被明显的超过。

## 总结
很理论吧？还好内容也不多，一定要好好消化一下哦。
在 Redis 中，LRU和LFU也是采用的近似算法，有兴趣的同学可以通过下面的链接去官网进行更深入的研究。

## 参考文档：
https://redis.io/docs/manual/eviction/