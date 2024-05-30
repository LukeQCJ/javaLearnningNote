# Python 入门教程

本文主要介绍 Python入门，需要了解的 Python安装，Python相关命令，以及运用命令运行Python程序代码的方法，
以及相关示例代码。

## 1､Python安装

许多PC和Mac将已经安装了 python。

要检查Windows PC上是否安装了 python，请在开始栏中搜索 Python或在命令行（cmd.exe）上运行以下命令：
```text
C:\Users\py> python --version
```

要检查是否在Linux或Mac上安装了python，请在linux上打开命令行，或者在Mac上打开Terminal，然后键入：
```text
python --version
```

如果发现计算机上未安装 python，则可以从以下网站免费下载 python：https://www.python.org/

如有需要安装 Python IDE，可以参考文档如下： PyCharm Windows下载及安装步骤教程

## 2､ Python快速入门

Python是一种解释型编程语言，这意味着作为开发人员，需要在文本编辑器中编写Python（.py）文件，
然后将这些文件放入 python解释器中以执行。

在命令行上运行 python文件的方式如下：
```text
C:\Users\py> python helloworld.py
```

其中“ helloworld.py”是python文件的名称。

让我们编写第一个 Python文件，称为helloworld.py，可以在任何文本编辑器中完成。
```text
helloworld.py
print("Hello, World!")
```

就那么简单。保存文件。打开命令行，导航到保存文件的目录，然后运行：
```text
C:\Users\py> python helloworld.py
```

输出应为：
```text
Hello, World!
```

到此，已经编写并执行了第一个 Python程序。

## 3､Python命令行

要在 python中测试少量代码，有时最快捷，最简单的方法是不将代码写入文件中。
之所以可以这样做，是因为 Python本身可以作为命令行运行。

在Windows，Mac或Linux命令行上键入以下内容：
```text
C:\Users\py> python
```

或者，如果“ python”命令不起作用，则可以尝试“ py”：
```text
C:\Users\py> py
```

从那里可以编写任何 python，包括本教程前面的hello world示例：

```text
C:\Users\py> python
Python 3.6.4 (default, Sep 23 2018, 17:08:23) [GCC 4.8.5 20150623 (Red Hat 4.8.5-28)] 
on linux Type "help", "copyright", "credits" or "license" for more information.
>>> print("Hello, World!")
```

上面写着"Hello, World!"在命令行中：
```text
C:\Users\py> python
Python 3.6.4 (default, Sep 23 2018, 17:08:23) [GCC 4.8.5 20150623 (Red Hat 4.8.5-28)] 
on linux Type "help", "copyright", "credits" or "license" for more information.
>>> print("Hello, World!")
Hello, World!
```

在 python命令行中完成操作后，只要键入以下内容即可退出 python命令行界面：
```text
exit()
```