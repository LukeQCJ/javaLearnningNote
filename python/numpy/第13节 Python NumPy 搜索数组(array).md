# Python NumPy 搜索 数组(array)

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 搜索 数组(array)。

## 1、搜索数组(array)

可以在数组中搜索某个值，并返回匹配的索引。

要搜索数组，请使用where()方法。

例如：

查找值为4的索引：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 4, 4])

x = np.where(arr == 4)

print(x)
```
output:
```text
(array([3, 5, 6], dtype=int64),)
```

上面的示例将返回一个元组：（array（[3，5，6]，）

这意味着值4出现在索引3、5和6处。

例如：

找到索引的值是偶数:
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7, 8])

x = np.where(arr%2 == 0)

print(x)
```
output:
```text
(array([1, 3, 5, 7], dtype=int64),)
```

例如：

查找值奇数的索引：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7, 8])

x = np.where(arr%2 == 1)

print(x)
```
output:
```text
(array([0, 2, 4, 6], dtype=int64),)
```

## 2、搜索排序
searchsorted()方法，它在数组中执行二进制搜索，并返回要插入指定值以维护搜索顺序的索引。

searchsorted()方法被用于已排序的数组。

例如：

找到应该插入值7的索引：
```text
import numpy as np

arr = np.array([6, 7, 8, 9])

x = np.searchsorted(arr, 7)

print(x)
```
output:
```text
1
```

解释的示例：应该在索引1上插入数字7，以保持排序顺序。

该方法从左侧开始搜索，并返回第一个索引，其中数字7不再大于下一个值。

**从右边搜索**

默认情况下，返回最左边的索引，但是我们可以给side ='right'来返回最右边的索引。

例如：

从右开始找到应该插入值7的索引：
```text
import numpy as np

arr = np.array([6, 7, 8, 9])

x = np.searchsorted(arr, 7, side='right')

print(x)
```
output:
```text
2
```
示例说明：数字7应该插入到索引2上，以保持排序顺序。

该方法从右侧开始搜索，并返回第一个索引，其中数字7不再小于下一个值。

**多个值**

要搜索多个值，请使用具有指定值的数组。

例如：

查找应在其中插入值2、4和6的索引：
```text
import numpy as np

arr = np.array([1, 3, 5, 7])

x = np.searchsorted(arr, [2, 4, 6])

print(x)
```
output:
```text
[1 2 3]
```

返回值是一个数组：[1 2 3]包含三个索引，其中将在原始数组中插入2、4、6以维持顺序。