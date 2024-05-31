# Python 数学函数模块(Math)

Python具有一组内置的数学函数，包括一个扩展的数学模块，可让您对数字执行数学任务。 
Python math模块中定义了一些数学函数。
由于这个模块属于编译系统自带，因此它可以被无条件调用。该模块还提供了与用标准C定义的数学函数的接口。
本文主要介绍 Python 数学函数模块(Math)，以及相关示例代码。

## 1、内置的数学函数

min()和max()函数可用于查找可迭代的最小值或最大值：

例如：
```text
x = min(5, 10, 25)
y = max(5, 10, 25)
print(x)
print(y)
```
output:
```text
5
25
```

abs()函数返回指定数字的绝对（正）值：

例如：
```text
x = abs(-7.25)
print(x)
```
output:
```text
7.25
```

pow（x，y）函数将x的值返回为y的幂（xy） 。

例如：

返回4的3次方的值(等同于4 * 4 * 4):
```text
x = pow(4, 3)
print(x)
```
output:
```text
64
```

## 2、Math模块
Python还有一个名为math的内置模块，该模块扩展了数学函数的列表。

要使用它，必须导入math模块：
```text
import math
```

导入math模块后，就可以开始使用该模块的方法和常量了。

例如，math.sqrt（）方法返回数字的平方根：

例如：
```text
import math

x = math.sqrt(64)
print(x)
```

math.ceil()方法将数字向上舍入到最接近的整数，而math.floor()方法将数字向下舍入到最接近的整数，并返回结果：

例如：
```text
import math

x = math.ceil(1.4)
y = math.floor(1.4)

print(x) # returns 2
print(y) # returns 1
```

math.pi常量返回PI的值（3.14 ...）：

例如：
```text
import math

x = math.pi
print(x)
```

## 3、完整Math模块参考
在Python Math模块(Module)中，会找到属于Math模块的所有方法和常量的完整参考。
