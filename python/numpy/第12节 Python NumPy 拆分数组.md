# Python NumPy 拆分数组

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 拆分数组。

## 1、使用array_split()拆分NumPy数组
拆分是联接的反向操作。

联接将多个数组合并为一个，拆分将一个数组拆分为多个。

我们使用array_split()拆分数组，将要拆分的数组和拆分次数传递给它。

例如：

将数组分为3部分：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6])

newarr = np.array_split(arr, 3)

print(newarr)
```
output:
```text
[array([1, 2]), array([3, 4]), array([5, 6])]
```

注意：返回值是一个包含三个数组的数组。

如果数组中的元素少于要求的数量，它将从末尾进行相应调整。

例如：

将数组分成4个部分:
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6])

newarr = np.array_split(arr, 4)

print(newarr)
```
output:
```text
[array([1, 2]), array([3, 4]), array([5]), array([6])]
```

注意：我们还有可用的方法split()，但是当元素较少用于拆分的源数组中时，它不会调整元素，如上面的示例，array_split()正常工作，但split()会失败。
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6])

newarr = np.split(arr, 3)

print(newarr)
```
output:
```text
[array([1, 2]), array([3, 4]), array([5, 6])]
```

```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6])

newarr = np.split(arr, 4)

print(newarr)
```
报错:
```text
ValueError: array split does not result in an equal division
```

## 2、拆分成数组
array_split()方法的返回值是一个包含每个拆分的数组。

如果你把一个数组分成3个数组，可以像任何数组元素一样从结果中访问它们:

例如：

访问拆分的数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6])

newarr = np.array_split(arr, 3)

print(newarr[0])
print(newarr[1])
print(newarr[2])
```
output:
```text
[1 2]
[3 4]
[5 6]
```

## 3、拆分二维数组
拆分二维数组时，请使用相同的语法。

使用array_split()方法，传入要拆分的数组和要拆分的次数。

例如：

将二维数组拆分为三个二维数组。
```text
import numpy as np

arr = np.array([[1, 2], [3, 4], [5, 6], [7, 8], [9, 10], [11, 12]])

newarr = np.array_split(arr, 3)

print(newarr)
```
output:
```text
[array([[1, 2], [3, 4]]),
 array([[5, 6], [7, 8]]),
  array([[ 9, 10], [11, 12]])]
```

上面的示例返回三个2-D数组。

让我们看另一个示例，这次2-D数组中的每个元素都包含3个元素。

例如：

将二维数组拆分为三个二维数组。
```text
import numpy as np

arr = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12], [13, 14, 15], [16, 17, 18]])

newarr = np.array_split(arr, 3)

print(newarr)
```
output:
```text
[array([[1, 2, 3], [4, 5, 6]]),
 array([[ 7,  8,  9], [10, 11, 12]]),
 array([[13, 14, 15], [16, 17, 18]])]
```

上面的示例返回三个2-D数组。

此外，您可以指定要进行拆分的轴。

下面的示例还返回三个2-D数组，但它们沿行（axis=1）分开。

例如：

沿行将2-D数组拆分为三个2-D数组。
```text
import numpy as np

arr = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12], [13, 14, 15], [16, 17, 18]])

newarr = np.array_split(arr, 3, axis=1)

print(newarr)
```
output:
```text
[array([[ 1],
       [ 4],
       [ 7],
       [10],
       [13],
       [16]]),
 array([[ 2],
       [ 5],
       [ 8],
       [11],
       [14],
       [17]]),
 array([[ 3],
       [ 6],
       [ 9],
       [12],
       [15],
       [18]])]
```

另一种解决方案是使用与hstack()相反的hsplit()

例如：

使用hsplit()方法将二维数组沿行拆分为三个二维数组。
```text
import numpy as np

arr = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12], [13, 14, 15], [16, 17, 18]])

newarr = np.hsplit(arr, 3)

print(newarr)
```
output:
```text
[array([[ 1],
       [ 4],
       [ 7],
       [10],
       [13],
       [16]]),
 array([[ 2],
       [ 5],
       [ 8],
       [11],
       [14],
       [17]]),
 array([[ 3],
       [ 6],
       [ 9],
       [12],
       [15],
       [18]])]
```

注意：与vstack()和dstack()类似的替代版本可以作为vsplit()和dsplit()。