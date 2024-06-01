# Python File文件处理 打开文件(open函数)

文件处理是任何Web应用程序的重要组成部分。 
Python具有创建，读取，更新和删除文件的几种功能。

本文主要介绍 Python中open()函数方法的使用，打开一个文件的方法。

## 1、文件处理
在Python中处理文件的关键功能是open()函数。

open()函数采用两个参数；filename和mode。

打开文件有四种不同的方法(模式):
- "r"-读取-默认值。打开文件进行读取，如果文件不存在则错误
- "a"-附加-打开文件进行追加，如果文件不存在则创建文件
- "w"-写入-打开文件进行写入，如果不存在则创建文件
- "x"-创建-创建指定的文件，如果文件存在则返回错误

另外，您可以指定文件应以二进制还是文本模式处理:
- "t"- 文本 - 默认值. Text mode。 
- "b"- 二进制 - Binary mode (例如，图片文件)

## 2、open()函数
要打开一个文件进行读取，只需指定文件的名称:
```text
f = open("demofile.txt")
```

与上面的代码相同的写法：
```text
f = open("demofile.txt", "rt")
```

因为用于读取的"r"和用于文本的"t"是默认值，所以您无需指定它们。

注意：确保文件存在，否则会收到错误消息提示。

mode 参数有：

| 模式	 | 描述                                                                                |
|-----|-----------------------------------------------------------------------------------|
| t	  | 文本模式 (默认)。                                                                        |
| x	  | 写模式，新建一个文件，如果该文件已存在则会报错。                                                          |
| b	  | 二进制模式。                                                                            |
| +	  | 打开一个文件进行更新(可读可写)。                                                                 |
| U	  | 通用换行模式（不推荐）。                                                                      |
| r	  | 以只读方式打开文件。文件的指针将会放在文件的开头。这是默认模式。                                                  |
| rb	 | 以二进制格式打开一个文件用于只读。文件指针将会放在文件的开头。这是默认模式。一般用于非文本文件如图片等。                              |
| r+	 | 打开一个文件用于读写。文件指针将会放在文件的开头。                                                         |
| rb+ | 	以二进制格式打开一个文件用于读写。文件指针将会放在文件的开头。一般用于非文本文件如图片等。                                    |
| w	  | 打开一个文件只用于写入。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。                      |
| wb	 | 以二进制格式打开一个文件只用于写入。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。一般用于非文本文件如图片等。  |
| w+	 | 打开一个文件用于读写。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。                       |
| wb+ | 	以二进制格式打开一个文件用于读写。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。一般用于非文本文件如图片等。  |
| a	  | 打开一个文件用于追加。如果该文件已存在，文件指针将会放在文件的结尾。也就是说，新的内容将会被写入到已有内容之后。如果该文件不存在，创建新文件进行写入。       |
| ab	 | 以二进制格式打开一个文件用于追加。如果该文件已存在，文件指针将会放在文件的结尾。也就是说，新的内容将会被写入到已有内容之后。如果该文件不存在，创建新文件进行写入。 |
| a+	 | 打开一个文件用于读写。如果该文件已存在，文件指针将会放在文件的结尾。文件打开时会是追加模式。如果该文件不存在，创建新文件用于读写。                 |
| ab+ | 	以二进制格式打开一个文件用于追加。如果该文件已存在，文件指针将会放在文件的结尾。如果该文件不存在，创建新文件用于读写。                      |

默认为文本模式，如果要以二进制模式打开，加上b 。

## 3、 Python2与 python3 打开文件的区别
在 python3中操作文件只有一种选择就是open()，
而在python2中则有两种方式：file()与open() 两者都能够打开文件，对文件进行操作，也具有相似的用法和参数，
但是，这两种文件打开方式有本质的区别，
file为文件类，用file()来打开文件，相当于这是在构造文件类，
而用open()打开文件，是用 python的内建函数来操作，我们一般使用open()打开文件进行操作，而用file当做一个类型，比如type(f) is file。

---

# Python File文件处理 读取文件(read)

文件处理是任何Web应用程序的重要组成部分。 
Python具有创建，读取，更新和删除文件的几种功能。
本文主要介绍 Python中打开一个文件读取文件中数据的方法。

## 1、打开一个文件读取数据
假设我们有以下文件，位于与Python相同的文件夹中：

demofile.txt

```text
Hello! Welcome to demofile.txt
This file is for testing purposes.
www.cjavapy.com
```

要打开文件，请使用内置的open()函数。

open()函数返回一个文件对象，该对象具有用于读取文件内容的read()方法：

例如：
```text
f = open("demofile.txt", "r")
print(f.read())
```
output:
```text
Hello! Welcome to demofile.txt
This file is for testing purposes.
www.cjavapy.com
```

如果文件位于其他位置，则必须指定文件路径，如下所示：

例如：

在其他位置打开文件：
```text
f = open("D:\\myfiles\welcome.txt", "r")
print(f.read())
```

## 2、read读取文件中部分数据
默认情况下，read()方法返回整个文本，但是您也可以指定要返回的字符数：

例如：

返回文件的前5个字符：
```text
f = open("demofile.txt", "r")
print(f.read(5))
```
output:
```text
Hello
```

## 3、readline()读取一行
可以使用readline()方法返回一行：

例如：

读取文件的一行：
```text
f = open("demofile.txt", "r")
print(f.readline())
```
output:
```text
Hello! Welcome to demofile.txt
```

通过两次调用readline()，可以阅读前两行：

例如：

读取文件中的两行:
```text
f = open("demofile.txt", "r")
print(f.readline())
print(f.readline())
```
output:
```text
Hello! Welcome to demofile.txt

This file is for testing purposes.
```

通过遍历文件的每一行，您可以逐行读取整个文件：

例如：

逐行循环遍历文件：
```text
f = open("demofile.txt", "r")
for x in f:
    print(x)
```
output:
```text
Hello! Welcome to demofile.txt

This file is for testing purposes.

www.cjavapy.com
```

## 4、close关闭文件
最好在完成处理后始终关闭文件。

例如：

完成后关闭文件：
```text
f = open("demofile.txt", "r")
print(f.readline())
f.close()
```
output:
```text
Hello! Welcome to demofile.txt
```

注意：您应该始终关闭文件，在某些情况下，由于缓冲的原因，只有在关闭文件后才能显示对文件所做的更改。

---

# Python File文件处理 创建/写入文件(write)

文件处理是任何Web应用程序的重要组成部分。 
Python具有创建，读取，更新和删除文件的几种功能。
本文主要介绍 Python中向文件中写入数据的方法。

## 1、写入存在的文件
要写入现有文件，必须在open()函数中添加一个参数:
- "a"-追加-将追加到文件末尾
- "w"-写-将覆盖任何现有内容

例如：

打开文件“demofile2.txt”，并将内容附加到该文件：
```text
f = open("demofile2.txt", "a")
f.write("cjavapy is a website!")
f.close()

# 打开并读取文件后追加:
f = open("demofile2.txt", "r")
print(f.read())
```
output:
```text
cjavapy is a website!
```

例如：

打开文件"demofile3.txt"，并覆盖内容:
```text
f = open("demofile3.txt", "w")
f.write("hello,cjavapy!!!")
f.close()

# 打开并读取文件后追加:
f = open("demofile3.txt", "r")
print(f.read())
```
output:
````text
hello,cjavapy!!!
````

注意："w"方法将覆盖整个文件。

## 2、创建一个新文件
要在 Python中创建新文件，请使用带有以下参数之一的open()方法：
- "x"-创建-将创建文件，如果文件存在则返回错误
- "a"-追加-如果指定的文件不存在，将创建一个文件
- "w"-写入-如果指定的文件不存在，将创建一个文件

例如：

创建一个名为“myfile.txt”的文件：
```text
f = open("myfile.txt", "x")
```

结果：创建了一个新的空文件！

例如：

创建一个新的文件，如果它不存在:
```text
f = open("myfile.txt", "w")
```

---

# Python File文件处理 删除文件(remove)

文件处理是任何Web应用程序的重要组成部分。 
Python具有创建，读取，更新和删除文件的几种功能。
本文主要介绍 Python中删除文件和文件夹的方法。

## 1、删除文件
要删除文件，必须导入OS模块并运行其os.remove()函数：

例如：

删除文件“demofile.txt”：
```text
import os
os.remove("demofile3.txt")
```

## 2、判断文件是否存在
为避免出现错误，您可能想要在尝试删除文件之前检查文件是否存在：

例如：

检查文件是否存在，然后将其删除：
```text
import os

if os.path.exists("demofile.txt"):
    os.remove("demofile.txt")
else:
    print("文件不存在")
```

## 3、删除文件夹
要删除整个文件夹，请使用os.rmdir()方法：

例如：

删除文件夹：
```text
import os

os.rmdir("myfolder")
```

注意：只能删除空文件夹。


