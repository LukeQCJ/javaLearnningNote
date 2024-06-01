# Python pip

pip 是 Python 包管理工具，该工具提供了对Python 包的查找、下载、安装、卸载的功能。
目前如果你在 python.org 下载最新版本的安装包，则是已经自带了该工具。Python 2.7.9 + 或 Python 3.4+ 以上版本都自带 pip 工具。

## 1、PIP是什么?
PIP是Python软件包或模块的软件包管理器。

注意：如果您具有Python 3.4或更高版本，则默认情况下包括PIP。

## 2、Package是什么?
软件包（Package）包含模块所需的所有文件。

模块是可以包含在项目中的Python代码库。

## 3、判断是否安装了PIP
将命令行进入到Python下面的Scripts目录，并键入以下内容:

例如：

检查PIP版本：
```text
C:\Users\Your Name\AppData\Local\Programs\Python\Python36-32\Scripts>pip --version
```

## 4、安装PIP
如果没有安装PIP，则可以从以下页面下载并安装它：https://pypi.org/project/pip/

## 5、下载安装Package
下载软件包非常容易。

打开命令行界面，并告诉PIP下载所需的软件包。

将命令行进入到Python下面Scripts目录的位置，然后输入以下内容：

例如：

下载 “camelcase” 的软件包：
```text
C:\Users\Your Name\AppData\Local\Programs\Python\Python36-32\Scripts>pip install camelcase
```

已经下载并安装了第一个软件包！

## 6、使用Package
一旦安装了软件包，就可以使用了。

将“camelcase”包导入到您的项目中。

例如：

Import 导入使用 "camelcase":
```text
import camelcase

c = camelcase.CamelCase()

txt = "hello world"

print(c.hump(txt))
```

## 7、搜索查找Packages
在https://pypi.org/上找到更多软件包。

## 8、删除Package
使用uninstall命令删除一个包:

例如：

卸载 “camelcase” 的软件包：
```text
C:\Users\Your Name\AppData\Local\Programs\Python\Python36-32\Scripts>pip uninstall camelcase
```

PIP包管理器将要求确认要删除camelcase软件包：
```text
Uninstalling camelcase-02.1:
Would remove:
c:\users\Your Name\appdata\local\programs\python\python36-32\lib\site-packages\camecase-0.2-py3.6.egg-info
c:\users\Your Name\appdata\local\programs\python\python36-32\lib\site-packages\camecase\*
Proceed (y/n)?
```

按y，该软件包将被删除。

## 9、查看安装的软件包（Packages）
使用list命令列出您的系统上安装的所有包:

例如：

列出已安装的软件包:
```text
C:\Users\Your Name\AppData\Local\Programs\Python\Python36-32\Scripts>pip list Package         Version
-----------------------
camelcase       0.2
mysql-connector 2.1.6
pip             18.1
pymongo         3.6.1
setuptools      39.0.1
```

---

## Python pip 配置使用国内镜像源

Python pip默认使用国外的镜像，在下载安装软件包时速度比较非常慢，要配置成国内镜像源。

本文主要介绍Python pip 配置使用国内镜像源的方法。

## 1、国内镜像源地址
1）国内清华大学的源
```text
https://pypi.tuna.tsinghua.edu.cn/simple
```

2）国内中国科学技术大学源
```text
https://pypi.mirrors.ustc.edu.cn/simple
```

3）国内豆瓣源
```text
http://pypi.douban.com/simple/
```

## 2、配置使用国内镜像源方法
1）通过命令来临时指定

可以直接在 pip 命令中使用 -i参数来指定镜像地址，例如：
```text
pip install numpy -i https://pypi.tuna.tsinghua.edu.cn/simple
```

2）Linux上通过修改~/.pip/pip.conf配置文件

打开编辑配置文件 ~/.pip/pip.conf，内容修改成如下：
```text
[global]
index-url = https://pypi.tuna.tsinghua.edu.cn/simple
[install]
trusted-host = https://pypi.tuna.tsinghua.edu.cn
```

查看配置：
```text
pip config list 
```
  
输出：
```text
global.index-url='https://pypi.tuna.tsinghua.edu.cn/simple'
install.trusted-host='https://pypi.tuna.tsinghua.edu.cn'
```

通过上面命令来验证是否修改成功。

3）Windows上通配置文件修改方法

需要在当前对用户目录下（C:\Users\xx\pip，xx 表示当前使用的系统用户）创建一个 pip.ini在pip.ini文件中输入以下内容：
```text
[global]
index-url = https://pypi.tuna.tsinghua.edu.cn/simple
[install]
trusted-host = pypi.tuna.tsinghua.edu.cn
```

通过pip config list命令查看配置是否修改成功。