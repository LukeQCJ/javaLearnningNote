# Python NumPy 数组索引

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 数组索引及访问数组元素。

## 1、普通索引
普通索引是指使用单个整数或整数列表来索引数组中的元素。

1）索引数组元素

数组索引与访问数组元素相同。

可以通过引用其索引号来访问数组元素。

NumPy数组中的索引以0开头，这意味着第一个元素的索引为0，第二个元素的索引为1等。

例如：

从以下数组中获取第一个元素：
```text
import numpy as np

arr = np.array([1, 2, 3, 4])

print(arr[0])
```

例如：

从以下数组中获取第二个元素。
```text
import numpy as np

arr = np.array([1, 2, 3, 4])

print(arr[1])
```

例如：

从以下数组中获取第三和第四个元素并将其添加。
```text
import numpy as np

arr = np.array([1, 2, 3, 4])

print(arr[2] + arr[3])
```

2）索引二维数组

要访问二维数组中的元素，我们可以使用逗号分隔的整数来表示元素的维数和索引。

例如：

访问第一行第二列的元素：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]])

print('arr[0, 1]: ', arr[0, 1])
```
output:
```text
arr[0, 1]:  2
```

例如：

访问第二行第五列的元素：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]])

print('arr[1, 4]: ', arr[1, 4])
```
output:
```text
arr[1, 4]:  10
```

3）索引三维数组

索引三维数组中的元素，可以使用逗号分隔的整数来表示元素的尺寸和索引。

例如：

访问第一个数组的第二个数组的第三个元素:
```text
import numpy as np

arr = np.array([[[1, 2, 3], [4, 5, 6]], [[7, 8, 9], [10, 11, 12]]])

print(arr[0, 1, 2])
```
output:
```text
6
```

示例说明
```text
arr[0, 1, 2]输出的值为6.

第一个数字表示第一维，它包含两个数组：
[[1、2、3]，[4、5、6]]
和：
[[7、8， 9]，[10，11，12]]
因为我们选择了0，所以剩下的第一个数组是：
[[1、2、3]，[4 ，5，6]]

第二个数字代表第二维，它还包含两个数组：
[1、2、3]
和：
[4、5、6]
，因为我们选择了1，剩下第二个数组：
[4，5，6]

第三个数字代表第三个维度，其中包含三个值：
4
5
6
由于我们选择了2，因此我们以第三个值：
6
```

## 2、高级索引
高级索引允许使用布尔值或数组来索引数组中的元素。

**1）整数数组索引**

语法：arr[indices]

使用一个整数数组作为索引，可以用来选择数组中的特定元素。如二维数组操作的就是行。
```text
import numpy as np

a = np.array([[1, 2], [3, 4], [5, 6]])
idx = np.array([[0, 2], [1, 2]])
print(a[idx])  # 使用整数数组索引,这是一个数组，而不能使用print(a[[0, 2],[1,2]])
```
output:
```text
[[[1 2]
  [5 6]]

 [[3 4]
  [5 6]]]
```

**2）花式索引**

语法：arr[ind1, ind2, ...]

花式索引指的是**利用多个整数数组进行索引**。
花式索引根据**索引数组的值**作为目标数组的某个轴的下标来取值。

花式索引跟切片不一样，它总是将数据复制到新数组中。
如二维数组是行列对应的元素。如二维数组操作的是行列对应的元素。

```text
import numpy as np

a = np.array([[1, 2], [3, 4], [5, 6]])
idx = np.array([[0, 2], [1, 2], [0, 1]])

print(a[[0, 1, 1]])  # 花式索引
print("---------")
print(a[[0, 1, 1], [0, 0, 0]])
```
output:
```text
[[1 2]
 [3 4]
 [3 4]]
---------
[1 3 3]
```

**3）布尔索引**

布尔索引是一种用于根据**元素的值**来选择要返回的元素的索引方法。
```text
import numpy as np

# 创建一个 2D 数组
data = np.array([[1, 2, 3, 4], [5, 6, 7, 8]])

# 创建一个布尔数组
mask = data > 5

# 使用布尔索引
print(data[mask])
# 输出：[6 7 8]
```

4）使用示例
```text
import numpy as np

# 创建一个示例数组
arr = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9])

# 使用整数数组进行高级索引
indices = np.array([1, 3, 5])
result = arr[indices]
print(result)  # 输出：[2 4 6]

# 使用布尔数组进行高级索引
mask = arr > 5
result = arr[mask]
print(result)  # 输出：[6 7 8 9]

# 使用高级索引修改数组元素
arr[indices] = 0
print(arr)  # 输出：[1 0 3 0 5 0 7 8 9]

# 使用二维整数数组进行高级索引
matrix = np.array([[1, 2, 3],
                   [4, 5, 6],
                   [7, 8, 9]])
row_indices = np.array([0, 2])
col_indices = np.array([1, 2])
result = matrix[row_indices, col_indices]
print(result)  # 输出：[2 9]

# 使用布尔数组进行高级索引和修改
mask = matrix > 3
matrix[mask] = 0
print(matrix)
# 输出：
# [[1 2 3]
#  [0 0 0]
#  [0 0 0]]
```

## 3、负索引
使用负索引从头开始访问数组。

例如：

打印第二行的最后一个元素：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]])

print('Last element from 2nd dim: ', arr[1, -1])
```
output:
```text
Last element from 2nd dim:  10
```
