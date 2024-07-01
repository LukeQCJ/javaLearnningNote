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

```text
缓存:
    一般当一个RDD的计算非常的耗时昂贵(计算规则比较复杂)，或者说这个RDD需要被重复(多方)使用，
    此时可以将这个RDD计算完的结果缓存起来，便于后续的使用，从而提升效率。
    
    通过缓存也可以提升RDD的容错能力，当后续计算失败后，尽量不让RDD进行回溯所有的依赖链条，从而减少重新计算时间。
    
注意:
    缓存仅仅是一种临时的存储，缓存数据可以保存到内存(executor内存空间)，也可以保存到磁盘中，
    甚至支持将缓存数据保存到堆外内存中(executor以外的系统内容)。
    
    由于临时存储，可能会存在数据丢失，所以缓存操作，并不会将RDD之间的依赖关系给截断掉(丢失掉)，因为当缓存失效后，可以基于原有依赖关系重新计算。
    
    缓存的API都是LAZY的，如果需要触发缓存操作，必须后续跟上一个action算子，一般建议使用count。
    如果不添加action算子，只有当后续遇到第一个action算子后，才会触发缓存。
    
如何使用缓存:
1）设置缓存的API:
    rdd.cache()：执行缓存操作，仅能将数据缓存到内存中。
    rdd.persist(缓存的级别(位置))：执行缓存操作，默认将数据缓存到内存中，当然也可以自定义缓存位置。

2）手动清理缓存的API：
    rdd.unpersist()
    默认情况下，当整个spark应用程序执行完成后，缓存也会自动失效的，自动删除。

3）常用的缓存级别:
    MEMORY_ONLY：仅缓存到内存中
    DISK_ONLY：仅缓存到磁盘
    MEMORY_AND_DISK：内存 + 磁盘优先缓存到内存中，当内存不足的时候，剩余数据缓存到磁盘中
    OFF_HEAP：缓存到堆外内存
    最为常用的级别：MEMORY_AND_DISK
```

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

```text
checkpoint比较类似于缓存操作，只不过缓存将数据保存在内存或者磁盘删，而checkpoint是将数据保存到磁盘或者HDFS。

checkpoint提供了更加安全可靠的持久话的方案，确保RDD的数据不会发生丢失，
一旦构建checkpoint操作后，会将RDD之间的依赖关系（血缘关系）进行截断，
后续计算出了问题，可以直接从检查点的位置恢复数据。

checkpoint主要作用：
    容错，也可以在一定程度上提升性能，但是不如缓存。
    后续计算失败后，从检查地直接恢复数据，不需要重新计算。
    
相关API：
1）设置检查点保存数据位置：
    sc.setCheckpointDir("路径地址")
2）在对应RDD开启检查点
    rdd.checkpoint()
    rdd.count()
    
注意：
    如果运行在集群模式中，checkpoint的保存路径地址必须是HDFS，如果是local模式，可以支持在本地路径。
    checkpoint数据不会自动删除，必须同事手动方式将其删除掉。
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


面试题：Spark提供了两种持久化的方案，一种缓存操作，一种为checkpoint方案，请问有什么区别呢？
```text
1）存储位置不同：
    缓存：存储在内存或磁盘或堆外内存。
    检查点：可以将数据存储在磁盘或HDFS中，在集群模式下，仅能保存在HDFS中。
2）血缘关系：
    缓存：不会截断RDD之间的血缘关系，因为缓存数据有可能失效，当失效后，需要重新回溯计算操作。
    检查点：会截断RDD之间的血缘关系，因为检查点将数据保存到更加安全可靠的位置，认为数据不会发生丢失问题，
          当执行失败的时候，也不需要重新回溯计算。
3）生命周期：
    缓存：当程序执行完成后，或者手动调用unpersist()方法，缓存会被删除。
    检查点：即便程序退出后，检查点的数据依然是存在的，不会删除，需要手动删除。
```

思考：既然持久化的方案有两种，那么在生产环境中，应该使用什么方案呢？
```text
一般建议将两种持久化的方案一同作用于项目环境中，先设置缓存，然后在设置检查地，最后统一出发执行。
（底层：会将数据先缓存好，然后将缓存好的数据，保存到checkpoint对应的路径中，后续在使用的时候，优先从缓存中读取，
如果缓存中没有，会从checkpoint中获取，同时再把读取数据放置到缓存中。）

不建议：先设置检查点，然后设置缓存，最后统一触发
（虽然读取的时候，没啥区别，但是在保存数据的时候，会存在两次磁盘IO操作，而上面的只有一次磁盘IO操作）。

不能：先设置缓存，然后立即触发，再设置检查点，然后立即触发，这种方案不管是缓存还是检查点一般只能生效其中一个（优先缓存）
```
