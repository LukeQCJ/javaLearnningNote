# Python 异步编程 协程(async/await)

异步编程是以进程、线程、协程、函数/方法作为执行任务程序的基本单位，结合回调、事件循环、信号量等机制，以提高程序整体执行效率和并发能力的编程方式。
本文主要介绍Python 中协程(async/await)的使用，以及相关的示例代码。

## 1、同步和异步
通常来说，程序都是顺序执行，同一时刻只会发生一件事。
如果一个函数方法依赖于另一个函数方法的结果，它只能等待那个函数方法结束才能继续执行，从用户的角度来说，整个程序才算运行完毕。
同步是指完成事务的逻辑，先执行第一个事务，如果阻塞了，会一直等待，直到这个事务完成，再执行第二个事务，顺序执行异步是和同步相对的，
异步是指在处理调用这个事务的之后，不会等待这个事务的处理结果，直接处理第二个事务去了，通过状态、通知、回调来通知调用者处理结果。
多线程和多进程都是通过异步的方式处理事物。

## 2、Python 协程(async/await)
协程，英文叫做 Coroutine，又称微线程，纤程，协程是一种用户态的轻量级线程。

协程拥有自己的寄存器上下文和栈。
协程调度切换时，将寄存器上下文和栈保存到其他地方，在切回来时，恢复先前保存的寄存器上下文和栈。
因此协程能保留上一次调用时的状态，即所有局部状态的一个特定组合，每次过程重入时，就相当于进入上一次调用的状态。

协程的本质是个单进程，协程相对于多进程来说，无需线程上下文切换的开销，无需原子操作锁定及同步的开销，编程模型也很简单。

可以使用协程来实现异步操作，如在网络爬虫场景下，我们发出一个请求之后，需要等待一定的时间才能得到响应，
但其实在这个等待过程中，程序可以干许多其他的事情，等到响应得到之后才切换回来继续处理，这样可以充分利用 CPU 和其他资源，这也是异步协程的优势。
一个线程内的多个协程是串行执行的，不能利用多核，协程不适合计算密集型的场景。协程适合I/O 阻塞型。

注意：对协程的执行顺序有要求，但执行代码非原子操作，且被 await 语句隔开。可以使用锁解决。锁的使用可以参考下面多线程文档中的介绍。

相关文档：Python 异步编程 多线程

## 3、Python 协程(async/await)的使用
从 Python 3.4 开始，Python 中加入了协程的概念，但此版本的协程依然是以生成器对象为基础，在 Python 3.5 则增加了 async/await，使得协程的实现更加方便。

| 术语         | 描述                                                                                                                        |
|------------|---------------------------------------------------------------------------------------------------------------------------|
| event_loop | 事件循环，相当于一个无限循环，可以把一些函数注册到这个事件循环上，当满足条件发生时， 就会调用对应的处理方法。                                                                   |
| coroutine  | 中文翻译叫协程，在 Python 中常指代为协程对象类型，可以将协程对象注册到时间循环中，它会被事件循环调用。可以使用 async 关键字来定义一个方法，这个方法在调用时不会立即被执行，而是返回一个协程对象。                  |
| task       | 任务，它是对协程对象的进一步封装，包含了任务的各个状态。                                                                                              |
| future     | 代表将来执行或没有执行的任务的结果，实际上和 task 没有本质区别。另外还需要了解 async/await 关键字，它是从 Python 3.5 才出现的，专用于定义协程。其中，async 定义一个协程，await 用来挂起阻塞方法的执行。 |

1）定义协程
```text
import asyncio


async def execute(x):
    print('x = ', x)


coroutine = execute(1)
print('协程:', coroutine)
print('After calling execute')

loop = asyncio.get_event_loop()
loop.run_until_complete(coroutine)
print('After calling loop')
```

2）使用loop.create_task()创建task任务
```text
import asyncio


async def execute(x):
    print('Number:', x)
    return x


coroutine = execute(1)
print('协程:', coroutine)
print('After calling execute')

loop = asyncio.get_event_loop()
task = loop.create_task(coroutine)
print('Task:', task)

loop.run_until_complete(task)

print('Task:', task)
print('After calling loop')
```

3）使用asyncio.ensure_future()创建task任务
```text
import asyncio


async def execute(x):
    print('Number:', x)
    return x


coroutine = execute(1)
print('协程:', coroutine)
print('After calling execute')

task = asyncio.ensure_future(coroutine)
print('Task:', task)

loop = asyncio.get_event_loop()
loop.run_until_complete(task)

print('Task:', task)
print('After calling loop')
```

4）绑定回调函数方法

可以为某个 task 绑定一个回调函数方法。
```text
import asyncio
import requests


async def request():
    url = 'https://www.baidu.com'
    status = requests.get(url)
    return status


def callback(task):
    print('Status:', task.result())


coroutine = request()
t = asyncio.ensure_future(coroutine)
t.add_done_callback(callback)
print('Task:', t)

loop = asyncio.get_event_loop()
loop.run_until_complete(t)
print('Task:', t)
```

5）调用 result() 获取结果

如不用回调方法，直接在 task 运行完毕之后也可以直接调用 result() 方法获取结果。
```text
import asyncio
import requests


async def request():
    url = 'https://www.baidu.com'
    status = requests.get(url)
    return status


coroutine = request()
task = asyncio.ensure_future(coroutine)
print('Task:', task)

loop = asyncio.get_event_loop()
loop.run_until_complete(task)
print('Task:', task)
print('Task Result:', task.result())
```

## 4、多任务协程
如需执行多次请求，则可以创建多个task的列表，执行asyncio 的 wait() 方法即可执行。

```text
import asyncio
import requests


async def request():
    url = 'https://www.baidu.com'
    status = requests.get(url)
    return status


tasks = [asyncio.ensure_future(request()) for _ in range(5)]
print('Tasks:', tasks)

loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))

for task in tasks:
    print('Task Result:', task.result())
```