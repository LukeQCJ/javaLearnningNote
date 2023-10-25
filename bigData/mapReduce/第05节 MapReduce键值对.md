MapReduce把处理过程分成两个阶段：Map阶段和Reduce阶段。
每个阶段的输入数据和输出数据都是以键值对的形式出现。

![mapReduceStepMapFlow01.png](img/05/mapReduceStepMapFlow01.png)

## MapReduce 键值对的生成
让我们来了解一下 MapReduce 框架是怎么生成键值对的。
MapReduce 过程中，在数据传输给 mapper 之前，数据首先被转换成键值对，因为 mapper 只能处理键值对形式的数据。

Hadoop MapReduce 生成键值对的过程如下：

InputSplit：这个是数据的逻辑表示形式。单个独立的 Mapper 处理的数据就是由 InputSplit 提供的。

RecordReader：它和 InputSplit 交互，并且把数据分片转换成适合 Mapper 读取的记录，这里的记录其实就是数据的键值对形式。
默认情况下，RecordReader 使用 TextInputFormat 把数据转换成键值对。
RecordReader 和 InputSplit 的交互直到文件读取完成才停止。

在MapReduce框架里面，map函数处理某个键值对并发出一定数量的键值对，Reduce函数处理按相同键分组的值，并发出另一组键值对作为输出。
map 的输出类型应该和 reduce 的输入类型一致。比如：
* Map: (K1, V1) -> list (K2, V2)
* Reduce: {(K2, list (V2 }) -> list (K3, V3)

K2，V2 数据类型是要保持一致的。

## Hadoop 中生成键值对需要依赖什么？
Hadoop 中键值对的生成取决于数据集和所需的输出。
通常，键值对在4个位置指定：Map 输入、Map 输出、Reduce 输入和 Reduce 输出。

### Map 输入
map 输入默认把**数据文件的行数**作为键（key），**数据行对应的内容**作为值。
可以对 InputFormat 进行定制化开发，可以修改这个默认设置。

### Map 输出
Map 主要负责过滤数据，并为数据基于键分组提供基础。

Key：它可能是字段、文本、对象，Reducer 端会根据 Key 对数据做分组和聚合。
Value：它可能是字段、文本、对象。它会被单独的 reduce 函数处理。

### Reduce 输入
Map 的输出作为 Reduce的输入，也就是说，Reduce 的输入数据和 Map 输出数据是一样的。

### Reduce 输出
这个取决于所需要的输出是怎么样的。

## MapReduce 键值对举例
假如有个存储在 HDFS 的文件的内容如下：
```text
John is Mark Joey is John
```

利用 InputFormat，我们就可以知道这个文件是怎么被分片和读取的。默认情况下，RecordReader 使用 TextInputFormat 把这个文件转换成键值对数据。

Key：这个是键值对的键，它是数据行的偏移量。
Value：值就是数据行具体内容，不包含行终止符。

根据上文件的内容，具体的 Key 和 Value 的内容如下：
```text
Key 是 0
Value 是  John is Mark Joey is John
```
