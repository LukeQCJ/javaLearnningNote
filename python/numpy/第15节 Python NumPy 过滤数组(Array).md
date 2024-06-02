# Python NumPy 过滤数组(Array)

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 过滤器 数组(Array)。

## 1、过滤数组
从现有数组中取出一些元素并从中创建新数组称为过滤。

在NumPy中，使用布尔索引列表过滤数组。

布尔值索引列表是与数组中的索引相对应的布尔值列表。

如果索引处的值为True，则该元素包含在过滤后的数组中，如果索引处的值为False，则该元素将从过滤后的数组中排除。

例如：

从索引0和2上的元素创建一个数组:
```text
import numpy as np

arr = np.array([41, 42, 43, 44])

x = [True, False, True, False]

newarr = arr[x]

print(newarr)
```
output:
```text
[41 43]
```

上面的示例将返回[41，43]

因为新过滤器仅包含过滤器数组具有值True的值，所以在这种情况下，索引为0和2。

## 2、定义过滤器数组
在上面的示例中，我们对True和False值进行了硬编码，但通常的用途是根据条件创建过滤器数组。

例如：

创建一个仅返回大于42的值的过滤器数组：
```text
import numpy as np

arr = np.array([41, 42, 43, 44])

# Create an empty list
filter_arr = []

# 遍历arr中的每个元素
for element in arr:
    # 如果元素大于42，将值设为True，否则为False:
    if element > 42:
        filter_arr.append(True)
    else:
        filter_arr.append(False)

newarr = arr[filter_arr]

print(filter_arr)
print(newarr)
```
output:
```text
[False, False, True, True]
[43 44]
```

例如：

创建一个过滤器数组，该数组仅返回原始数组中的偶数元素：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

# Create an empty list
filter_arr = []

# 遍历arr中的每个元素
for element in arr:
    # 如果元素完全可被2整除，则将值设为True，否则为False
    if element % 2 == 0:
        filter_arr.append(True)
    else:
        filter_arr.append(False)

newarr = arr[filter_arr]

print(filter_arr)
print(newarr)
```
output:
```text
[False, True, False, True, False, True, False]
[2 4 6]
```

## 3、直接从数组定义过滤器
上面的示例是NumPy中很常见的任务，NumPy提供了解决该问题的好方法。

我们可以在我们的条件下直接替换数组而不是iterable变量。

例如：

创建一个仅返回大于42的值的过滤器数组：
```text
import numpy as np

arr = np.array([41, 42, 43, 44])

filter_arr = arr > 42

newarr = arr[filter_arr]

print(filter_arr)
print(newarr)
```
output:
```text
[False False  True  True]
[43 44]
```

例如：

创建一个过滤器数组，该数组仅返回原始数组中的偶数元素：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

filter_arr = arr % 2 == 0

newarr = arr[filter_arr]

print(filter_arr)
print(newarr)
```
output:
```text
[False  True False  True False  True False]
[2 4 6]
```
