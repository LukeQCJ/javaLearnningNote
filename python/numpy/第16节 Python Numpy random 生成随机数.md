# Python Numpy random 生成随机数

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python Numpy random生成随机数。

# 1、什么是随机数(Random Numbers)?
随机数并不意味着每次都有不同的数字。 随机意味着无法在逻辑上预测的事物。

# 2、伪随机和真随机
计算机在程序上工作，程序是权威的指令集。 因此，这意味着还必须有某种算法来生成随机数。

如果有一个程序可以生成随机数，则可以预测它，因此它并不是真正的随机数。通过生成算法生成的随机数称为**伪随机数**。

我们可以做真正的随机数吗？

为了在我们的计算机上生成一个真正的随机数，我们需要从某个外部来源获取随机数据。外部来源通常是我们的击键，鼠标移动，网络数据等。

我们不需要真正的随机数，除非它与安全有关(如，加密密钥)或应用的基础是随机性(如，数字轮盘赌轮)。

在本教程中，我们将使用伪随机数。

## 3、生成随机数
NumPy提供了random模块来处理随机数。

例如：

生成一个从0到100的随机整数：
```text
from numpy import random

x = random.randint(100)

print(x)
```
output:
```text
21
```

## 4、生成随机浮点数(float)
随机模块的rand()方法返回0到1之间的随机浮点数。

例如：

生成从0到1的随机浮点数：
```text
from numpy import random

x = random.rand()

print(x)
```
output:
```text
0.9437515875745334
```

## 5、生成随机数组
在NumPy中，使用数组，可以使用上面示例中的两种方法来创建随机数组。

**Integers**

randint()方法采用一个size参数，可以在其中指定数组的形状。

例如：

生成一维数组，其中包含5个从0到100的随机整数：
```text
from numpy import random

x=random.randint(100, size=(5))

print(x)
```
output:
```text
[43 61 52 42 19]
```

例如：

生成具有3行的2-D数组，每行包含5个从0到100的随机整数：
```text
from numpy import random

x = random.randint(100, size=(3, 5))

print(x)
```
output:
```text
[[47 67 55 71 47]
 [68 70 46  2 14]
 [94 78 99  0  7]]
```

**Floats**

rand()方法还允许您指定数组的形状。

例如：

生成一个包含5个随机浮点数(float)的1-D数组:
```text
from numpy import random

x = random.rand(5)

print(x)
```
output:
```text
[0.75189738 0.80796226 0.48290976 0.39752563 0.57543172]
```

例如：

生成具有3行的二维数组，每行包含5个随机数：
```text
from numpy import random

x = random.rand(3, 5)

print(x)
```
output:
```text
[[0.62531061 0.34868197 0.94936502 0.41282763 0.25219621]
 [0.93872404 0.66156595 0.51768478 0.43732453 0.15246604]
 [0.59538556 0.31246794 0.1080154  0.92102356 0.09397173]]
```

## 6、从数组中生成随机数
choice()方法允许您基于值数组生成随机值。

choice()方法将数组作为参数，并随机返回其中一个值。

例如：

返回数组中的值之一：
```text
from numpy import random

x = random.choice([3, 5, 7, 9])

print(x)
```
output:
```text
9
```

choice()方法还允许您返回值的array。

添加size参数以指定数组的形状。

例如：

生成一个二维数组，由数组参数(3,5,7,9)中的值组成:
```text
from numpy import random

x = random.choice([3, 5, 7, 9], size=(3, 5))

print(x)
```
output:
```text
[[5 7 7 3 9]
 [7 7 3 5 7]
 [3 3 9 9 5]]
```
