# Python 异步编程 多进程

异步编程是以进程、线程、协程、函数/方法作为执行任务程序的基本单位，结合回调、事件循环、信号量等机制，以提高程序整体执行效率和并发能力的编程方式。
本文主要介绍Python 中multiprocessing多进程的使用，以及相关的示例代码。

## 1、同步和异步
通常来说，程序都是顺序执行，同一时刻只会发生一件事。
如果一个函数方法依赖于另一个函数方法的结果，它只能等待那个函数方法结束才能继续执行，从用户的角度来说，整个程序才算运行完毕。
同步是指完成事务的逻辑，先执行第一个事务，如果阻塞了，会一直等待，直到这个事务完成，再执行第二个事务，顺序执行异步是和同步相对的，
异步是指在处理调用这个事务的之后，不会等待这个事务的处理结果，直接处理第二个事务去了，通过状态、通知、回调来通知调用者处理结果。
多线程和多进程都是通过异步的方式处理事物。

## 2、Python 多进程
进程是资源的集合，是最小的资源单位。是一个程序在一个数据集上的一次动态执行过程。
进程一般由程序、数据集、进程控制块三部分组成。
多进程适合执行计算密集型任务（如：视频编码解码、数据处理、科学计算等）、可以分解为多个并行子任务并能合并子任务执行结果的任务，
以及在内存使用方面没有任何限制且不强依赖于I/O操作的任务。

Python的多线程只能运行在单核上，各个线程以并发的方法异步运行。
而多进程可以利用CPU的多核，进程数取决于计算机CPU的处理器个数，由于运行在不同的核上，各个进程的运行是并行的。
当进程数量大于CPU的内核数量时，等待运行的进程会等到其他进程运行完让出内核为止。如果CPU单核，就无法运行多进程并行。
可以使用multiprocessing库查看CPU核数。

代码如下，
```text
from multiprocessing import cpu_count
print(cpu_count())
```

## 3、Python 多进程的使用
Python中的多线程其实并不是真正的多线程，如要充分地使用多核CPU的资源，在Python中大部分情况需要使用多进程。
Python提供了好用的多进程包multiprocessing，只需要定义一个函数，Python会完成其他所有事情。
借助这个包，可以轻松完成从单进程到并发执行的转换。
multiprocessing支持子进程、通信和共享数据、执行不同形式的同步，提供了Process、Queue、Pipe、Lock等组件。

1）创建函数执行单个进程
```text
import multiprocessing
import time


def worker(interval):
    n = 5
    while n > 0:
        print("The time is {0}".format(time.ctime()))
        time.sleep(interval)
        n -= 1


if __name__ == "__main__":
    p = multiprocessing.Process(target=worker, args=(3,))
    p.start()
    print("p.pid:", p.pid)
    print("p.name:", p.name)
    print("p.is_alive:", p.is_alive())
```
output:
```text
p.pid: 16048
p.name: Process-1
p.is_alive: True
The time is Sat Jun  1 18:00:08 2024
The time is Sat Jun  1 18:00:11 2024
The time is Sat Jun  1 18:00:14 2024
The time is Sat Jun  1 18:00:17 2024
The time is Sat Jun  1 18:00:20 2024
```

2）创建函数执行多个进程
```text
import multiprocessing
import time


def worker_1(interval):
    print("worker_1")
    time.sleep(interval)
    print("end worker_1")


def worker_2(interval):
    print("worker_2")
    time.sleep(interval)
    print("end worker_2")


def worker_3(interval):
    print("worker_3")
    time.sleep(interval)
    print("end worker_3")


if __name__ == "__main__":
    p1 = multiprocessing.Process(target=worker_1, args=(2,))
    p2 = multiprocessing.Process(target=worker_2, args=(3,))
    p3 = multiprocessing.Process(target=worker_3, args=(4,))

    p1.start()
    p2.start()
    p3.start()

    print("The number of CPU is:" + str(multiprocessing.cpu_count()))
    for p in multiprocessing.active_children():
        print("child   p.name:" + p.name + "\tp.id" + str(p.pid))
```
output:
```text
The number of CPU is:12
child   p.name:Process-2	p.id9252
child   p.name:Process-1	p.id3092
child   p.name:Process-3	p.id13072
worker_1
worker_3
worker_2
end worker_1
end worker_2
end worker_3
```

3）通过自定义进程类
```text
import multiprocessing
import time


class ClockProcess(multiprocessing.Process):
    def __init__(self, interval):
        multiprocessing.Process.__init__(self)
        self.interval = interval

    def run(self):
        n = 5
        while n > 0:
            print("the time is {0}".format(time.ctime()))
            time.sleep(self.interval)
            n -= 1


if __name__ == '__main__':
    p = ClockProcess(3)
    p.start()
```
output:
```text
the time is Sat Jun  1 18:01:33 2024
the time is Sat Jun  1 18:01:36 2024
the time is Sat Jun  1 18:01:39 2024
the time is Sat Jun  1 18:01:42 2024
the time is Sat Jun  1 18:01:45 2024
```
注意：调用进程的start()方法时，会自动调用run()方法。

## 4、Python 进程锁
多个进程需要访问共享资源，也需要通过锁机制来解决数据一致性等问题。
```text
import multiprocessing


def worker_with(lock, f):
    with lock:
        fs = open(f, 'a+')
    n = 10
    while n > 1:
        fs.write("Lock 通过 with\n")
        n -= 1
    fs.close()


def worker_no_with(lock, f):
    lock.acquire()
    try:
        fs = open(f, 'a+')
        n = 10
        while n > 1:
            fs.write("Lock 直接 操作\n")
            n -= 1
        fs.close()
    finally:
        lock.release()


if __name__ == "__main__":
    lock = multiprocessing.Lock()
    f = "file.txt"
    w = multiprocessing.Process(target=worker_with, args=(lock, f))
    nw = multiprocessing.Process(target=worker_no_with, args=(lock, f))
    w.start()
    nw.start()
```

## 5、Semaphore信号量
Semaphore是用来控制对共享资源的访问量，可以控制同一时刻进程的并发数量。
```text
import multiprocessing
import time


def worker(s, i):
    s.acquire()
    print(multiprocessing.current_process().name + "acquire")
    time.sleep(i)
    print(multiprocessing.current_process().name + "release\n")
    s.release()


if __name__ == "__main__":
    s = multiprocessing.Semaphore(2)
    for i in range(5):
        p = multiprocessing.Process(target=worker, args=(s, i * 2))
        p.start()
```

## 6、Event事件
Event事件用于主线程控制其他线程的执行，以实现进程间同步通信。

| 方法               | 描述                                                        |
|------------------|-----------------------------------------------------------|
| Event().wait()   | 插入在进程中插入一个标记（flag） 默认为 false，然后flag为false时 程序会停止运行 进入阻塞状态 |
| Event().set()    | 使flag为Ture 然后程序会停止运行 进入运行状态                               |
| Event().clear()  | 使flag为false 然后程序会停止运行 进入阻塞状态                              |
| Event().is_set() | 判断flag 是否为True 是的话 返回True 不是 返回false                      |

代码如下，
```text
import multiprocessing
import time


def wait_for_event(event: multiprocessing.Event):
    print("wait_for_event: starting")
    event.wait()
    print("wait_for_event: event.is_set()->" + str(event.is_set()))


def wait_for_event_timeout(event: multiprocessing.Event, t: float):
    print("wait_for_event_timeout:starting")
    event.wait(t)
    print("wait_for_event_timeout:event.is_set->" + str(event.is_set()))


if __name__ == "__main__":
    e = multiprocessing.Event()
    w1 = multiprocessing.Process(name="block", target=wait_for_event, args=(e,))
    w2 = multiprocessing.Process(name="non-block", target=wait_for_event_timeout, args=(e, 2))

    w1.start()
    w2.start()

    time.sleep(3)

    e.set()
    print("main: event is set")
```

## 7、Queue安全队列
Queue是多进程安全的队列，可以使用Queue实现多进程之间的数据传递。
**put方法**用以插入数据到队列中，put()方法还有两个可选参数：blocked和timeout。
如果blocked为True（默认值），并且timeout为正值，该方法会阻塞timeout指定的时间，直到该队列有剩余的空间。
如果超时，会抛出Queue.Full异常。
如果blocked为False，但该Queue已满，会立即抛出Queue.Full异常。

get()方法可以从队列读取并且删除一个元素。同样，get()方法有两个可选参数：blocked和timeout。
如果blocked为True（默认值），并且timeout为正值，那么在等待时间内没有取到任何元素，会抛出Queue.Empty异常。
如果blocked为False，有两种情况存在，如果Queue有一个值可用，则立即返回该值，否则，如果队列为空，则立即抛出Queue.Empty异常。

代码如下，
```text
import multiprocessing


def writer_proc(queue: multiprocessing.Queue):
    try:
        queue.put(1)
        print("生产数据")
    except:
        pass


def reader_proc(queue: multiprocessing.Queue):
    try:
        v = queue.get()
        print(f"获取数据{v}")
    except:
        pass


if __name__ == "__main__":
    q = multiprocessing.Queue()
    writer = multiprocessing.Process(target=writer_proc, args=(q,))
    writer.start()

    reader = multiprocessing.Process(target=reader_proc, args=(q,))
    reader.start()

    reader.join()
    writer.join()
```

## 8、Pipe管道
Pipe方法返回(p1, p2)代表一个管道的两个端。
Pipe方法有duplex参数，如果duplex参数为True(默认值)，则管道是全双工模式，即p1和p2均可收发。duplex为False，p1只能接受消息，p2只能发送消息。

send()和recv()方法分别为发送和接受消息的方法。
如在全双工模式下，可以调用p1.send()发送消息，p1.recv()接收消息。如没消息可接收，recv()方法会一直阻塞。如果管道已经被关闭，则recv()方法会抛出EOFError。

```text
import multiprocessing
import os

from typing import List


def consumer(pipe):
    con1, con2 = pipe
    print('consumer con1 id', id(con1))
    print('consumer con2 id', id(con2))
    print("process consumer id", os.getpid())

    con2.close()  # input pipe close 1
    while True:
        try:
            item = con1.recv()
        except EOFError:
            print('EOFError')
            break
        print(item)
    print('consumer done')


def producer(listArr: List[int], conn):
    for item in listArr:
        conn.send(item)


if __name__ == '__main__':
    (conn1, conn2) = multiprocessing.Pipe()
    cons_p = multiprocessing.Process(target=consumer, args=((conn1, conn2),))
    cons_p.start()

    conn1.close()  # output pipe close 1

    arr = [1, 2, 3, 4, 5]
    producer(arr, conn2)
    print('   main conn1 id', id(conn1))
    print('   main conn2 id', id(conn2))
    conn2.close()  # input pipe close 2
```

## 9、Pool进程池
Pool类可提供指定数量的进程供用户调用，当新的请求提交到Pool中时，如果池还没有满，就会创建一个新的进程来执行请求。 
如果池满，请求就会先等待，直到池中有进程结束，才会创建新的进程来执行这些请求。对于性能优化和数量限制很有用。

1）使用非阻塞进程池

| 方法                                            | 描述                                          |
|-----------------------------------------------|---------------------------------------------|
| apply_async(func[, args[, kwds[, callback]]]) | 此方法是是非阻塞。                                   |
| apply(func[, args[, kwds]])                   | 此方法是阻塞的。                                    |
| close()                                       | 关闭pool，使其不在接受新的任务。                          |
| terminate()                                   | 结束工作进程，不在处理未完成的任务。                          |
| join()                                        | 主进程阻塞，等待子进程的退出，join方法要在close或terminate之后使用。 |

代码如下，
```text
# coding: utf-8
import multiprocessing
import time


def func(message: str):
    print("message:", message)
    time.sleep(3)
    print("end")


if __name__ == "__main__":
    pool = multiprocessing.Pool(processes=3)
    for i in range(4):
        msg = "hello %d" % i
        # 维持执行的进程总数为processes，当一个进程执行完毕后会添加新的进程进去
        pool.apply_async(func, (msg,))

    print("开始执行")
    pool.close()
    # 调用join之前，先调用close函数，否则会出错。执行完close后不会有新的进程加入到pool,join函数等待所有子进程结束
    pool.join()
    print("子进程执行完成")
```
output:
```text
开始执行
message: hello 0
message: hello 1
message: hello 2
end
end
end

message: hello 3
end
子进程执行完成
```

2）使用阻塞进程池
```text
# coding: utf-8
import multiprocessing
import time


def func(message: str):
    print("message:", message)
    time.sleep(3)
    print("end")


if __name__ == "__main__":
    pool = multiprocessing.Pool(processes=3)
    for i in range(4):
        msg = "hello %d" % i
        # 维持执行的进程总数为processes，当一个进程执行完毕后会添加新的进程进去
        pool.apply(func, (msg,))

    print("开始执行")
    pool.close()
    # 调用join之前，先调用close函数，否则会出错。执行完close后不会有新的进程加入到pool,join函数等待所有子进程结束
    pool.join()
    print("子进程执行完成")
```
output:
```text
message: hello 0
end
message: hello 1
end
message: hello 2
end
message: hello 3
end
开始执行
子进程执行完成
```

相关文档： Python multiprocessing 多进程间通信传递DataFrame的方法