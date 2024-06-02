# Python NumPy 数据类型

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。
本文主要介绍Python NumPy 数据类型。

## 1、Python中的数据类型
默认情况下，Python具有以下数据类型：
- strings-用于表示文本数据，文本用引号引起来。 例如。 “A B C D”
- integer-用于表示整数。 例如。 -1，-2，-3
- float-用于表示实数。 例如。 1.2、42.42
- boolean-用于表示True或False。
- complex-用于以复数形式表示数字。 例如, 1.0 + 2.0j，1.5 + 2.5j

## 2、NumPy中数据类型
NumPy有一些额外的数据类型，并且引用一个字符的数据类型，例如i用于整数，u用于无符号整数等。

以下是NumPy中所有数据类型的列表以及用于表示它们的字符。

- i-整数
- b-布尔值
- u-无符号整数
- f-浮动
- c-复杂的浮点数
- m-timedelta
- M-日期时间
- O-对象
- S-字符串
- U-Unicode字符串
- V-其他类型的固定内存块（void）

## 3、判断数组的数据类型
NumPy数组对象具有一个名为dtype的属性，该属性返回数组的数据类型：

例如：

获取数组对象的数据类型：
```text
import numpy as np

arr = np.array([1, 2, 3, 4])

print(arr.dtype)
```
output:
```text
int32
```

例如：

获取包含字符串的数组的数据类型：
```text
import numpy as np

arr = np.array(['c', 'python', 'cjavapy'])

print(arr.dtype)
```
output:
```text
<U7
```

## 4、创建具有已定义数据类型的数组
我们使用array()函数创建数组，该函数可以使用可选参数：dtype，它允许我们定义所需的数组元素数据类型：

例如：

创建一个数据类型为字符串的数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4], dtype='S')

print(arr)
print(arr.dtype)
```
output:
```text
[b'1' b'2' b'3' b'4']
|S1
```

对于i，u，f，S和U，我们也可以将尺寸定义。

例如：

创建一个数据类型为4字节整数的数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4], dtype='i4')

print(arr)
print(arr.dtype)
```
output:
```text
[1 2 3 4]
int32
```

## 5、值不能被转换问题
如果给出了不能强制转换元素的类型，则NumPy将引发ValueError。

ValueError：在Python中，如果传递给函数的参数的类型意外/不正确，则会引发ValueError。

例如：

不能将非整数字符串（例如，'a'）转换为整数（将引发错误）：
```text
import numpy as np

arr = np.array(['a', '2', '3'], dtype='i')
```
output:
```text
ValueError: invalid literal for int() with base 10: 'a'
```

## 6、转换现有数组上的数据类型
更改现有数组的数据类型的最佳方法是使用astype()方法制作该数组的副本。

astype()函数创建数组的副本，并允许您将数据类型指定为参数。

数据类型可以使用字符串指定，例如'f'表示浮点数，'i'表示整数等。也可以直接使用数据类型，例如float代表浮点数，int代表整数。

例如：

通过使用'i'作为参数值，将数据类型从float更改为整数：
```text
import numpy as np

arr = np.array([1.1, 2.1, 3.1])
print(arr)
print(arr.dtype)

newarr = arr.astype('i')

print(newarr)
print(newarr.dtype)
```
output:
```text
[1.1 2.1 3.1]
float64
[1 2 3]
int32
```

例如：

通过使用int作为参数值，将数据类型从float更改为整数：
```text
import numpy as np

arr = np.array([1.1, 2.1, 3.1])
print(arr)
print(arr.dtype)

newarr = arr.astype(int)

print(newarr)
print(newarr.dtype)
```
output:
```text
[1.1 2.1 3.1]
float64
[1 2 3]
int32
```

例如：

将数据类型从整数更改为布尔值：
```text
import numpy as np

arr = np.array([1, 0, 3])
print(arr)
print(arr.dtype)

newarr = arr.astype(bool)

print(newarr)
print(newarr.dtype)
```
output:
```text
[1 0 3]
int32
[ True False  True]
bool
```
