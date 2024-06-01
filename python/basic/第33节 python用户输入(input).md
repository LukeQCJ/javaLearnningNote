# Python 用户输入(input)

在使用python的时候，有时候我们需要和用户进行交互，让用户输入一些内容，然后根据内容在做处理。
下面给大家介绍在Python中如何让用户输入内容。

## 1、用户输入(input)
Python允许用户输入。

这意味着我们可以要求用户输入。

该方法在Python 3.6中与Python 2.7有所不同。

Python 3.6使用input()方法。

Python 2.7使用raw_input()方法。

下面的示例询问用户名，当您输入用户名时，它会显示在屏幕上：

Python 3.6
```text
username = input("Enter username:")
print("Username is: " + username)
```

Python 2.7
```text
username = raw_input("Enter username:")
print("Username is: " + username)
```

当涉及到input()函数时，Python停止执行，并在用户给出一些输入后继续执行。

## 2、python2和python3区别
在python2里，如果使用input语句，⽤户输入的内容如果是一个字符串，会把这个字符串当做一个变量使用；如果输入的是一个数字，会把这个数字当做数字类型。
如果想要输⼊一个字符串，需要给这个字符串加引号，或者使用 raw_input。

在python3里，用户输入的所有内容都会被当做字符串来存储。
python3里的input功能和python2里的 raw_input 功能一致。