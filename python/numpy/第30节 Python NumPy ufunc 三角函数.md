# Python NumPy ufunc 三角函数

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。
本文主要介绍Python NumPy ufunc 三角函数。

## 1、三角函数
NumPy提供ufuncs sin()，cos()和tan()，以弧度表示值并产生相应的sin，cos和tan值。

例如：

查找PI/2的正弦值：
```text
import numpy as np

x = np.sin(np.pi/2)

print(x)
```
output:
```text
1.0
```

例如：

查找arr中所有值的正弦值：
```text
import numpy as np

arr = np.array([np.pi/2, np.pi/3, np.pi/4, np.pi/5])

x = np.sin(arr)

print(x)
```
output:
```text
[1.         0.8660254  0.70710678 0.58778525]
```

## 2、将角度转换成弧度
默认情况下，所有三角函数均以弧度为参数，但是在NumPy中，我们也可以将弧度转换为度，反之亦然。

注意：弧度值为pi /180 * degree_values。

例如：

将以下数组arr中的所有值转换为弧度：
```text
import numpy as np

arr = np.array([90, 180, 270, 360])

x = np.deg2rad(arr)

print(x)
```
output:
```text
[1.57079633 3.14159265 4.71238898 6.28318531]
```

## 3、将弧度转换成角度
例如：

将以下数组arr中的所有值转换为角度：
```text
import numpy as np

arr = np.array([np.pi/2, np.pi, 1.5*np.pi, 2*np.pi])

x = np.rad2deg(arr)

print(x)
```
output:
```text
[ 90. 180. 270. 360.]
```

## 4、求角度
从sin，cos，tan值中找到角度。 例如。 sin，cos和 tan inverse（arcsin，arccos，arctan）。

NumPy提供ufuncs arcsin()，arccos()和arctan()，它们可以为给定的相应sin，cos和tan值生成弧度值。

例如：

找到1.0的角度：
```text
import numpy as np

x = np.arcsin(1.0)

print(x)
```
output:
```text
1.5707963267948966
```

## 5、求数组中每个值的角度
例如：

计算数组中所有正弦值的角度
```text
import numpy as np

arr = np.array([1, -1, 0.1])

x = np.arcsin(arr)

print(x)
```
output:
```text
[ 1.57079633 -1.57079633  0.10016742]
```

## 6、直角三角形的斜边
在NumPy中使用**毕达哥拉斯定理**找到斜边。

NumPy提供了hypot()函数，该函数采用基本值和垂直值，并根据毕达哥拉斯定理计算斜边。

例如：

求4个底和3个垂直的斜边：
```text
import numpy as np

base = 3
perp = 4

x = np.hypot(base, perp)

print(x)
```
output:
```text
5.0
```