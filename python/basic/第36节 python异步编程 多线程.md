# Python 异步编程 多线程

异步编程是以进程、线程、协程、函数/方法作为执行任务程序的基本单位，结合回调、事件循环、信号量等机制，以提高程序整体执行效率和并发能力的编程方式。
本文主要介绍Python 中threading多线程的使用，以及相关的示例代码。

## 1、同步和异步
通常来说，程序都是顺序执行，同一时刻只会发生一件事。
如果一个函数方法依赖于另一个函数方法的结果，它只能等待那个函数方法结束才能继续执行，从用户的角度来说，整个程序才算运行完毕。
同步是指完成事务的逻辑，先执行第一个事务，如果阻塞了，会一直等待，直到这个事务完成，再执行第二个事务，顺序执行异步是和同步相对的，
异步是指在处理调用这个事务的之后，不会等待这个事务的处理结果，直接处理第二个事务去了，通过状态、通知、回调来通知调用者处理结果。
多线程和多进程都是通过异步的方式处理事物。

## 2、Python 多线程
**线程**也叫轻量级进程，是操作系统能够进行运算调度的最小单位，它被包涵在进程之中，是进程中的实际运作单位。
线程自己不拥有系统资源，只拥有一点儿在运行中必不可少的资源，但它可与同属一个进程的其他线程共享进程所拥有的全部资源。
一个线程可以创建和撤销另一个线程，同一个进程中的多个线程之间可以并发执行。

Python有GIL全局解释器锁，同一时间只能有一个线程执行，则不能利用多核优势。
如多线程进程是CPU密集型的，则多线程并不能带来效率上的提升，相反还可能会因线程的频繁切换，导致效率下降；
如IO密集型，多线程进程可以利用IO阻塞等待时的空闲时间执行其他线程，则可以提升效率。

## 3、Python 多线程的使用
Python中使用多线程需要import threading，具体使用如下，
```text
import threading
import time


def listen_work(name):
    i = 5
    while i > 0:
        time.sleep(1)
        print(name, "正在处理文件")
        i -= 1


def download_work(name):
    i = 3
    while i > 0:
        time.sleep(2)
        print(name, "正在下载文件")
        i -= 1


if __name__ == '__main__':
    p1 = threading.Thread(target=listen_work, args=("多线程应用",))
    p2 = threading.Thread(target=download_work, args=("多线程应用",))
    p1.start()
    p2.start()
```

## 4、Python 多线程中的锁
多线程中锁的作用是为了保证数据的一致性，对锁内的资源（变量）进行锁定，避免在读取时同时其它线程进行修改。以达到我们的预期效果。

1）条件变量同步

Python提供了threading.Condition 对象用于条件变量线程的支持，除了提供RLock()或Lock()的方法外，还提供了 wait()、notify()、notifyAll()方法。

| 方法          | 描述                      |
|-------------|-------------------------|
| wait()      | 条件不满足时调用，线程会释放锁并进入等待阻塞； |
| notify()    | 条件创造后调用，通知等待池激活一个线程；    |
| notifyAll() | 条件创造后调用，通知等待池激活所有线程。    |

代码如下，
```text
import threading
import time
from random import randint


class Producer(threading.Thread):
    def run(self):
        global StoreList
        while True:
            val = randint(0, 100)
            print('生产者', self.name, ':Append' + str(val), StoreList)
            if lock_con.acquire():  # 获取锁
                StoreList.append(val)
                lock_con.notify()  # 通知消费者
                lock_con.release()  # 释放锁
            time.sleep(3)


class Consumer(threading.Thread):
    def run(self):
        global StoreList
        while True:
            lock_con.acquire()  # 获取锁
            if len(StoreList) == 0:
                lock_con.wait()  # 等待锁
            print('消费者', self.name, ":Delete" + str(StoreList[0]), StoreList)
            del StoreList[0]
            lock_con.release()  # 释放锁
            time.sleep(0.25)


if __name__ == "__main__":
    StoreList = []
    lock_con = threading.Condition()  # 条件同步变量
    threads = []
    for i in range(5):
        threads.append(Producer())
        threads.append(Consumer())
    for t in threads:
        t.start()
    for t in threads:
        t.join()
    print('---- end ----')
```

2）条件同步

条件同步用于不同时访问共享资源的条件环境。

| 方法            | 描述                                          |
|---------------|---------------------------------------------|
| event.isSet() | 返回event的状态值；                                |
| event.wait()  | 如果 event.isSet()==False将阻塞线程；               |
| event.set()   | 设置event的状态值为True，所有阻塞池的线程激活进入就绪状态，等待操作系统调度； |
| event.clear() | 恢复event的状态值为False。                          |

代码如下，
```text
import threading
import time


class Master(threading.Thread):
    def run(self):
        print("Master: 开始执行")
        event.isSet() or event.set()
        time.sleep(5)
        print("Master: 执行完成")
        event.isSet() or event.set()


class Worker(threading.Thread):
    def run(self):
        event.wait()
        print("Worker: 正在执行")
        time.sleep(0.25)
        event.clear()
        event.wait()
        print("Worker: 任务完成!")


if __name__ == "__main__":
    event = threading.Event()
    threads = []
    for i in range(5):
        threads.append(Worker())
        threads.append(Master())
    for t in threads:
        t.start()
    for t in threads:
        t.join()
```

3）同步队列

Python中的Queue对象也提供了对线程同步的支持。
使用Queue对象可以实现多个生产者和多个消费者形成的FIFO的队列。Queue.Queue类即是一个队列的同步实现。
队列长度可为无限或者有限。

put()方法在队尾插入一个项目。
put()有两个参数，第一个item为必需的，为插入项目的值；第二个block为可选参数，默认为1。
如果队列当前为空且block为1，put()方法就使调用线程暂停,直到空出一个数据单元。
如果block为0，put()方法将引发Full异常。


q.get([block[, timeout]])方法从队头删除并返回一个项目。
可选参数为block，默认为True。如果队列为空且block为True，get()就使调用线程暂停，直至有项目可用。
如果队列为空且block为False，队列将引发Empty异常，timeout等待时间。

| 方法               | 描述                                         |
|------------------|--------------------------------------------|
| qsize()          | 返回队列的大小                                    |
| empty()          | 如果队列为空，返回True,反之False                      |
| full()           | 如果队列满了，返回True,反之False。与 maxsize 大小对应       |
| get_nowait()     | 相当q.get(False)                             |
| put_nowait(item) | 相当q.put(item, False)                       |
| task_done()      | 在完成一项工作之后，q.task_done() 函数向任务已经完成的队列发送一个信号 |
| join()           | 实际上意味着等到队列为空，再执行别的操作                       |

```text
import threading
import queue
from time import sleep
from random import randint


class Producer(threading.Thread):
    def run(self):
        i = 5
        while i > 0:
            r = randint(0, 100)
            q.put(r)
            print("生产者生产： %s" % r)
            sleep(1)
            i -= 1


class Consumer(threading.Thread):
    def run(self):
        i = 5
        while True:
            if not q.empty() and i > 0:
                re = q.get()
                print('消费者消费： %s' % re)
                sleep(0.5)
                i -= 1
            if i == 0:
                break


if __name__ == '__main__':
    q = queue.Queue(10)
    producer = Producer()
    producer.start()
    consumer = Consumer()
    consumer.start()
```
注意：多线程使用锁时，要防止死锁的产生，死锁的原因有多种，但是本质就是对资源不合理的竞争锁导致的。两个线程就陷入了相互等待的局面就是死锁。