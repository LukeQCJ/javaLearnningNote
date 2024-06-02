# Python NumPy Array(数组) reshape

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy Array(数组) reshape。

## 1、reshape array(数组)
reshape意味着更改数组的形状。

**数组的形状**是**每个维度中的元素数量**。

通过重塑(reshape)，我们可以添加或删除维度或更改每个维度中的元素数量。

## 2、reshape 从 1-D 到 2-D
例如：

将以下具有12个元素的1-D数组转换为2-D数组。 最外面的维度将具有4个数组，每个数组包含3个元素：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])

newarr = arr.reshape(4, 3)

print(newarr)
```
output:
```text
[[ 1  2  3]
 [ 4  5  6]
 [ 7  8  9]
 [10 11 12]]
```

## 3、reshape 从 1-D 到 3-D
例如：

将以下具有12个元素的1-D数组转换为3-D数组。 最外面的维度将具有2个数组，其中包含3个数组，每个数组包含2个元素：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])

newarr = arr.reshape(2, 3, 2)

print(newarr)
```
output:
```text
[[[ 1  2]
  [ 3  4]
  [ 5  6]]

 [[ 7  8]
  [ 9 10]
  [11 12]]]
```

## 4、reshape成任何形状
只要reshape所需的元素在两种形状中均相等。

可以将8个元素的一维数组reshape为4个元素的2行二维数组，但不能将其reshape为3个元素的3行二维数组，因为这需要3x3 = 9个元素。

例如：

尝试将具有8个元素的1D数组转换为每个维度中具有3个元素的2D数组（将产生错误）：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7, 8])

newarr = arr.reshape(3, 3)

print(newarr)
```
报错：
```text
ValueError: cannot reshape array of size 8 into shape (3,3)
```

## 5、判断返回 copy 或 view
例如：

判断返回的数组是copy还是view：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7, 8])

print(arr.reshape(2, 4).base)
```
output:
```text
[1 2 3 4 5 6 7 8]
```

上面的示例返回原始数组，因此它是一个view。

## 6、未知的维度
可以使用一个"unknown"维度。

这意味着您不必在整形方法中为尺寸之一指定确切的数字。

传递-1作为值，NumPy将为您计算该数字。

例如：

将8个元素的1D数组转换为2x2元素的3D数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7, 8])

newarr = arr.reshape(2, 2, -1)

print(newarr)
```
output:
```text
[[[1 2]
  [3 4]]

 [[5 6]
  [7 8]]]
```

注意：我们不能将-1传递给多个维度。

## 7、展平数组
**展平数组**是指将多维数组转换为一维数组。

可以使用reshape(-1)来做到这一点。

例如：

将数组转换为一维数组：
```text
import numpy as np

arr = np.array([[1, 2, 3], [4, 5, 6]])

newarr = arr.reshape(-1)

print(newarr)
```
output:
```text
[1 2 3 4 5 6]
```

注意：有很多功能可以更改numpy中的数组形状(shape)。
flatten，ravel以及重新排列元素rot90，flip，fliplr，flipud等。
这些属于numpy的“中级至高级”部分。