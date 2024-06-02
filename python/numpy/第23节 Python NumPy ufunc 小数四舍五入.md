# Python NumPy ufunc 小数四舍五入

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc 小数四舍五入。

## 1、四舍五入方式
在NumPy中主要有五种四舍五入方式：
- truncation
- fix
- rounding
- floor
- ceil

## 2、trunc和fix
删除小数，然后返回最接近零的浮点数。 使用trunc()和fix()函数。

例如：

截断以下数组的元素:
```text
import numpy as np

arr = np.trunc([-3.1666, 3.6667])

print(arr)
```
output:
```text
[-3.  3.]
```

例如：

同样的示例，使用fix()：
```text
import numpy as np

arr = np.fix([-3.1666, 3.6667])

print(arr)
```
output:
```text
[-3.  3.]
```

## 3、around
如果>=5，则around()函数在小数点或数字之前增加1，否则不执行任何操作。

例如。 四舍五入到小数点后一位，3.16666为3.2

例如：

将3.1666舍入到小数点后两位：
```text
import numpy as np

arr = np.around(3.1666, 2)

print(arr)
```
output:
```text
3.17
```

## 4、floor
floor()函数将小数点四舍五入到最接近的低位整数。

例如。 3.166的底限是3。

例如：

以下数组的元素:
```text
import numpy as np

arr = np.floor([-3.1666, 3.6667])

print(arr)
```
output:
```text
[-4.  3.]
```

注意：floor()函数返回浮点数，与trunc()函数返回整数不同。

## 5、ceil
ceil()函数将十进制四舍五入为最接近的整数。

例如。 3.166的上限为4。

例如：

列出以下数组的元素：
```text
import numpy as np

arr = np.ceil([-3.1666, 3.6667])

print(arr)
```
output:
```text
[-3.  4.]
```
