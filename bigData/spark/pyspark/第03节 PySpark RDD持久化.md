# RDD的持久化
RDD 的数据是过程数据，因此需要持久化存储。

RDD之间进行相互迭代的计算，新的RDD的生成代表着旧的RDD的消失。
这样的特性可以最大化地利用资源，老旧地RDD可以及时地从内存中清理，从而给后续地计算腾出空间。

示例：
```text
rdd1 = sc.parallelize(["hello world luke", "hello world lps"])
rdd2 = rdd1.flatMap(lambda x: str(x).split(" "))
rdd3 = rdd2.map(lambda x: (x, 1))
rdd4 = rdd3.reduceByKey(lambda a, b: a + b)
print(rdd4.collect())

rdd5 = rdd3.map(lambda _: _[1] + 1 if _[0] == "hello" or _[0] == "world" else 0)
rdd6 = rdd5.filter(lambda x: x > 1)
print(rdd6.collect())
```
rdd3地第一次使用是在构建rdd4的时候，构建完rdd4之后rdd3就不存在了；
而第二次使用rdd3的时候，由于其已经不存在，需要根据RDD的血缘关系，从rdd重新执行，构建出来rdd3，供rdd5使用；

## RDD的缓存
可以通过缓存技术，将RDD存储在内存中或者磁盘上，这样就不用重复构建rdd了。

常用的缓存API如下：
```text
rdd3.cache() # 缓存到内存中
rdd3.persist(storageLevel.MEMORY_ONLY) # 仅内存缓存
rdd3.persist(storageLevel.MEMORY_ONLY_2) # 仅内存缓存，2个副本
rdd3.persist(storageLevel.DISK_ONLY) # 仅缓存硬盘上
rdd3.persist(storageLevel.DISK ONLY_2) # 仅缓存硬盘上，2个副本
rdd3.persist(storageLevel.DISK ONLY_3) # 仅缓存硬盘上，3个副本
rdd3.persist(storageLevel.MEMORY_AND_DISK) # 先放内存，不够放硬盘
rdd3.persist(storageLeveL.MEMORY_AND_DISK_2)# 先放内存，不够放硬盘，2个副本
rdd3.persist(storageLevel.0FF_HEAP) # 堆外内存(系统内存)
```
一般使用rdd.persist(StorageLevel.MEMORY_AND_DISK)，优先缓存在磁盘上；

如果是内存比较小的集群，可以只缓存到磁盘上；

手动清理缓存的API：rdd.unpersist()

**缓存的特点：缓存被认为是不安全的，因此保留RDD之间的血缘关系**

因为缓存的数据有丢失的风险，内存中的缓存可能由于断电/空间不足被清理；磁盘上的缓存可能由于磁盘损坏丢失等等，
所以需要保留血缘关系，从而避免数据丢失；

**RDD的缓存是如何保存的？**

采取分散存储：RDD的每个分区自行将其数据保存在其所在的Executor内存和磁盘上。

## RDD的CheckPoint
CheckPoint也是保存RDD的一种机制，但只支持磁盘存储；

与缓存相比，CheckPoint被认为是安全的，也不会保存RDD之间的血缘关系；

**CheckPoint的存储：**

集中收集存储：CheckPoint集中收集各个分区的数据存储在HDFS上；

API：
```text
# 设置存储路径，如果是local模式，可以选用本地文件系统
# 如果是集群模式，一定要设置hdfs路径
sc.setCheckpointDir(path)
# 存储
rdd.checkpoint()
# 清除
rdd.unpersist()
```

## 缓存和CheckPoint的对比
```text
1）CheckPoint不管分区数量多少，风险是一样的，缓存分区越多，风险越高。
2）CheckPoint支持写入HDFS，缓存不行，HDFS是高可靠存储，CheckPoint被认为是安全的。
3）CheckPoint不支持内存，缓存可以，缓存如果写内存性能比CheckPoint要好一些。
4）CheckPoint因为设计认为是安全的，所以不保留血缘关系，而缓存因为设计上认为不安全，所以保留。
```

**Cache 和 CheckPoint的性能对比：**
```text
1）Cache性能更好，因为是分散存储，各个Executor并行执行，效率高，可以保存到内存中(占内存)，更快。
2）CheckPoint比较慢，因为是集中存储，涉及到网络IO，但是存储到HDFS上更加安全(多副本)。
```
注意：Cache和CheckPoint两个API都不是action类型的，如果要想正常工作，后面必须有action类型的算子。
