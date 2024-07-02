# 共享变量
## 广播变量
### 问题提出：
当本地的list对象和RDD数据对象有关联时，会将本地的list对象发送到Executor的每个分区内进行使用，
也就是说，每个Executor内可能存放了多份相同的数据；
但是由于一个Executor代表了一个进程，进程内的资源是共享的，所以每个Executor内只保留一份数据就可以；
这样的操作我们通过广播来实现；

### 具体操作：
将本地的list对象标识为广播变量对象：
```text
# 本地list对象
lst = [1, 2, 3]
# 封装为广播变量对象
broadcast = sc.broadcast(lst)
# 使用广播变量，即从广播变量对象中取出本地的list对象
value = broadcast.value
print(value)
```
这样由于在数据传输之前，我们已经把本地数据对象封装成了广播变量对象，
Spark在进行数据传输的时候就会给每个Executor只传送一份数据，Executor内部的各个线程（分区）就可以共享这一份数据；
### 适用场景：
本地集合对象和分布式集合对象（RDD）进行关联的时候；
需要把本地集合对象标记为广播变量对象，可以减少IO传输次数，并减少Executor的内存占用。

为什么要使用本地集合对象？
```text
集合占用内存较小时，使用本地集合对象可以提升性能，避免了大量的shuffle。
```

```text
在Driver端定义一个共享的变量，如果不使用广播变量，各个线程在运行的时候，都需要将这个变量拷贝到自己的线程中，
对网络传输，内存的使用都是一种浪费，而且不影响效率。

如果使用广播变量，会将变量在每个executor上放置一份，各个线程直接读取executor上的变量即可，
不需要拉取到task中，减少副本的数量，对网络和内存都降低了，从而提升效率。

广播变量是只读的，各个task只能读取数据，不能修改。
广播变量只能声名在Driver端，不能声名在各个分区task运行代码里，否则会报错。

相关API：
    设置广播变量：广播变量的对象 = sc.broadcase(变量值)
    获取广播变量：广播变量的对象.value
```

## 累加器
### 问题提出：
如何对map算子计算中的数据进行计数累加？

### 代码实现：
```text
# 2个分区数
rdd = sc.parallelize([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 2)
count = 0  # 用于计数


def map_func(data):
    global count
    count += 1
    print(data, count)


rdd.map(map_func).collect()
print(count)
```
结果：
```text
1 1
2 2
3 3
4 4
5 5
6 1
7 2
8 3
9 4
10 5
0 # 注意
```
最终输出的结果为0；
这是因为最初定义的count来自于driver对象，
当map算子需要count对象时（count += 1），driver会将count对象复制一份，发送给每个executor
（注意，这里是复制发送，而不是发送内存地址）；
所以，executor中的count对象进行累加，与driver中的count对象是无关的，最终打印输出的还是driver中的对象；

**如何解决这一问题？—— 使用累加器。**

将上述代码中的count对象替换成累加器对象即可：
```text
rdd = sc.parallelize([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 2)

# Spark提供的累加器变量, 参数是初始值
acmlt = sc.accumulator(0)


def map_func(data):
    global acmlt
    acmlt += 1
    print(data, acmlt)


rdd.map(map_func).collect()
print(acmlt)
```
结果：
```text
1 1
2 2
3 3
4 4
5 5
6 1
7 2
8 3
9 4
10 5
10 # 注意
```
累加器对象可以从各个executor对象中收集运行结果并作用于其自身（类似于内存指针）。

注意事项：
由于rdd的重新构建，累加器代码可能被多次重复执行。

解决方法：使用缓存或checkpoint即可。
