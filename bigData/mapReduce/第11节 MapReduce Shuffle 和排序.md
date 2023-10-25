**mapper 的输出结果被传输到 reducer 的过程被称为 shuffle**，并且在传输到 reducer 之前，数据会被按 key 排序。
下面会详细介绍这两个过程。

![shuffleAndSortFlow01.png](img/11/shuffleAndSortFlow01.png)

## Hadoop MapReduce Shuffle和排序
在学习 shuffle 和排序之前，可以先复习一下 MapReduce 的其他阶段，
比如 Mapper，Reducer，Combiner，partitioner 以及 InputFormat。

MapReduce 框架的**Shuffle阶段**指的是**把 map的输出结果从Mapper传输到Reducer的过程**。
MapReduce 的**排序阶段**包括对**map输出的合并和排序**两个步骤。**mapper的输出数据会被按key分组和排序**。
每一个reducer获取所有具有相同key的值。**MapReduce框架的shuffle和排序阶段是同时发生的**。

### Shuffle
我们都知道，数据从 mapper 传输到 reducer 的过程被称为 shuffle。
所以，MapReduce Shuffle 阶段对于 reducer 来说是有必要的，否则，reducer 就没任务输入数据。
由于 shuffle 可以在 map 阶段完成之前就启动，所以这会节省一些运行时间，以更少的时间完成任务。

### 排序
由 mapper 生成的 key 会被 MapReduce 框架自动排序，
比如，在 reducer 启动之前，所有由 mapper 生成的中间结果键值对会按 key 进行排序而不是按 value 排序。
传输给 reducer 的 value 并没有排序，它们的顺序是任意的。

Hadoop 中的排序帮助 reducer 轻松区分何时应该启动新的 reduce 任务。这会为 reducer 节省一些时间。
在已排序的输入数据中，当下一个 key 和 前一个 key 不一样的时候，reducer 就会启动一个新的 reduce 任务。
每个 reduce 任务以键值对作为输入并输出键值对数据。

需要注意的是，如果你通过下面的代码把 reducer 任务的个数设置为0，那么作业就不会有 shuffle 和排序阶段了。
```text
setNumReduceTasks(0)
```

这时，MapReduce 作业在 map 阶段完成后就结束的，而且，map 阶段不包括任何类型的排序操作，所以这样的 map 阶段执行速度会更快。

## MapReduce 二次排序
如果你想对 reducer 的键值对的 value 进行排序，那么，就需要用到二次排序技术了，
二次排序技术可以让我们对传输到每个 reducer 的键值对的 value 以降序或者升序排序。