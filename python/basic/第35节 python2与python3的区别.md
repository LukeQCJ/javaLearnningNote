# Python2和Python3的区别

大部分Python2程序都需要进行些修改才能正常的运行在Python3的环境下。
为了简化这个转换过程，Python3自带了一个2to3的实用脚本。这个脚本会将Python2程序源文件作为输入，然后自动转换到Python３，但并不是所有内容都可以自动转换。
本文主要介绍一下Python2和Python3的区别。

## 1、除法运算符(/)
Python2两上整数相除得到是整数，其中一个数是小数，则结果也是小数，而Python3中的两个整数相除结果就是小数。

例如，
```text
print(7 / 5)
print(-7 / 5)
```

Python2中输出：
```text
1
-2
```

Python3中输出：
```text
1.4
-1.4
```

## 2、print 函数
Python3中print函数的括号不能省略。

例如，
```text
print 'Hello, cjavapy'      # Python 3.x 不支持
print('https://www.cjavapy.com')
```

## 3、Unicode编码
Python 2 中，隐式 str 类型是 ASCII。但是在 Python 3.x 中，隐式 str 类型是 Unicode。Python 2.x 也支持 Unicode。
```text
print(type('default string '))
print(type(b'string with b '))
print(type('default string '))
print(type(u'string with b '))
```

Python2中输出：
```text
<type 'str'>
<type 'str'>
<type 'str'>
<type 'unicode'>
```

Python3中输出：
```text
<class 'str'>
<class 'bytes'>
<class 'str'>
<class 'str'>
```

## 4、xrange
Python 2中有 range 和 xrange 两个方法。
其区别在于，range返回一个list，在被调用的时候即返回整个序列；
xrange返回一个iterator，在每次循环中生成序列的下一个数字。

Python 3中不再支持 xrange 方法，Python 3中的 range 方法就相当于 Python 2中的 xrange 方法。

例如，
```text
for x in xrange(1, 5):
    print(x)
for x in range(1, 5):
    print(x)
```

## 5、错误处理
在 Python 3 中处理异常稍有改变，在 Python 3 中我们现在使用 as 作为关键词。

Python2:
```text
try:
    trying_to_check_error
except NameError, err: # 在Python 3.x中不支持
    print err, 'Error Caused'
```

Python3:
```text
try:
    trying_to_check_error
except NameError as err: # 'as'在Python 3.x中使用
    print (err, 'Error Caused')
```

## 6、八进制字面量表示
八进制数必须写成0o777，原来的形式0777不能用了；二进制必须写成0b111。

新增了一个bin()函数用于将一个整数转换成二进制字串。Python 2.6已经支持这两种语法。

在Python 3.x中，表示八进制字面量的方式只有一种，就是0o1000。

## 7、不等运算符
Python 2.x中不等于有两种写法 != 和 <>

Python 3.x中去掉了<>, 只有 != 一种写法。

## 8、去掉了repr表达式``
Python 2.x 中反引号``相当于repr函数的作用

Python 3.x 中去掉了``这种写法，只允许使用repr函数。

## 9、多个模块被改名（根据PEP8）
| Python2的名字   | Python3的名字   |
|--------------|--------------|
| _winreg      | winreg       |
| ConfigParser | configparser |
| copy_reg     | copyreg      |
| Queue        | queue        |
| SocketServer | socketserver |
| repr         | reprlib      |

## 10、面向对象区别
参考文档：Python2 和Python3 面向对象区别

