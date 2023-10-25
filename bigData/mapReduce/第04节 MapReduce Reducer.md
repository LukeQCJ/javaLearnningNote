Reducer获取Mapper的输出数据（键值对），并对这些数据逐个处理，最终把最终处理结果存储到HDFS。
通常在Hadoop的Reducer阶段，我们会做聚合计算、求和计算等操作。

## 什么是Hadoop Reducer
Reducer处理mapper的输出数据，在处理这些数据的时候，它会生成一组新的输出数据。最后把数据存储到HDFS。

Hadoop Reducer以mapper输出的中间结果（键值对）作为输入数据，并对这些数据逐一执行reducer函数，
比如可以对这些数据做聚合、过滤、合并等操作。Reducer首先按key处理键值对的值，然后生成输出数据（零或多个键值对）。
key相同的键值对数据将会进入同一个reducer，并且reducer是并行执行的，因为reducer之间不存在依赖关系。
reducer的数量由用户自己决定，默认值是1。

## MapReduce Reducer阶段
通过前面章节的介绍我们已经知道，Hadoop MapReduce的Reducer过程包含3个阶段。下面对他们进行逐个详细介绍。

### Reducer的Shuffle阶段
在本阶段，来自mapper的已经排好序的数据将作为Reducer的输入数据。
**在Shuffle阶段，MapReduce通过HTTP协议拉取mapper输出数据的相应分区数据**。

### Reducer的排序阶段
在该阶段，来自不同mapper的输入数据将会按key重新做排序。**shuffle和排序阶段是同时进行的**。

### Reduce阶段
在shuffle和排序完成之后，reduce任务对键值对数据做聚合操作。
之后，OutputCollector.collect()方法把reduce任务的输出结果写到HDFS。Reducer的输出不做排序。

## Reducer任务的数量
一个作业的Reduce任务数量是怎么确定的呢？以及如何修改Reduce数量？下面我们带着问题来详细了解一下。

我们可以通过 Job.setNumreduceTasks(int) 方法设置 reduce 的数量。一般合适的reduce任务数量可以通过下面公式计算：
```text
(0.95 或者 1.75) * ( 节点数 * 每个节点最大的容器数量)
```
使用0.95的时候，当map任务完成后，reducer会立即执行并开始传输map的输出数据。
使用1.75的时候，第一批reducer任务将在运行速度更快的节点上执行完成，而第二批reducer任务的执行在负载平衡方面做得更好。