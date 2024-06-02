# Python NumPy ufunc 简单算术

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc 简单算术。

## 1、简单算术
可以直接在NumPy数组之间使用算术运算符+ -  */，但是本节讨论了它的扩展，
我们有一些函数可以接受任何类似数组的对象，比如列表、元组等，并有条件地执行算术运算。

有条件的算术：意味着我们可以定义应进行算术运算的条件。

所有讨论的算术函数都采用一个where参数，我们可以在其中指定该条件。

## 2、add相加
add()函数将两个数组的内容相加，然后将结果返回到新数组中。

例如：

将arr1中的值添加到arr2中的值：
```text
import numpy as np

arr1 = np.array([10, 11, 12, 13, 14, 15])
arr2 = np.array([20, 21, 22, 23, 24, 25])

newarr = np.add(arr1, arr2)

print(newarr)
```
output:
```text
[30 32 34 36 38 40]
```

上面的示例将返回[30 32 34 36 38 40]，它是10 + 20、11 + 21、12 + 22等的总和。

## 3、subtract相减
subtract()函数将一个数组的值与另一个数组的值相减，然后将结果返回一个新数组。

例如：

从arr1中的值减去arr2中的值：
```text
import numpy as np

arr1 = np.array([10, 20, 30, 40, 50, 60])
arr2 = np.array([20, 21, 22, 23, 24, 25])

newarr = np.subtract(arr1, arr2)

print(newarr)
```
output:
```text
[-10  -1   8  17  26  35]
```

上面的示例将返回[-10 -1 8 17 26 35]，这是10-20、20-21、30-22等的结果。

## 4、multiply相乘
multiply()函数将一个数组中的值与另一个数组中的值相乘，然后将结果返回到新数组中。

例如：

将arr1中的值与arr2中的值相乘：
```text
import numpy as np

arr1 = np.array([10, 20, 30, 40, 50, 60])
arr2 = np.array([20, 21, 22, 23, 24, 25])

newarr = np.multiply(arr1, arr2)

print(newarr)
```
output:
```text
[ 200  420  660  920 1200 1500]
```

上面的示例将返回[200 420 660 920 1200 1500]，这是10 * 20、20 * 21、30 * 22等的结果。

## 5、divide相除
divide()函数的作用是:将一个数组中的值与另一个数组中的值相除，并将结果返回到一个新数组中。

例如：

将arr1中的值除以arr2中的值：
```text
import numpy as np

arr1 = np.array([10, 20, 30, 40, 50, 60])
arr2 = np.array([3, 5, 10, 8, 2, 33])

newarr = np.divide(arr1, arr2)

print(newarr)
```
output:
```text
[ 3.33333333  4.          3.          5.         25.          1.81818182]
```

上面的示例将返回[3.33333333 4. 3. 5. 25. 1.81818182]，这是10/3、20/5、30/10等的结果。

## 6、power幂
power()函数将值从第一个数组增加到第二个数组的幂，然后将结果返回到新数组中。

例如：

Raise the valules in arr1 to the power of values in arr2:
```text
import numpy as np

arr1 = np.array([10, 20, 30, 40, 50, 60])
arr2 = np.array([3, 5, 6, 8, 2, 33])

newarr = np.power(arr1, arr2)

print(newarr)
```
output:
```text
[      1000    3200000  729000000 -520093696       2500          0]
```

上面的示例将返回[1000 3200000 729000000 6553600000000 2500 0]，
它是10*10*10、20*20*20*20*20、30*30*30*30*30*30等的结果。

## 7、remainder余数
mod()和remainder()函数都返回第一个数组除以相对应的第二个数组中值的余数，并以新的形式返回结果数组。

例如：

返回余数：
```text
import numpy as np

arr1 = np.array([10, 20, 30, 40, 50, 60])
arr2 = np.array([3, 7, 9, 8, 2, 33])

newarr = np.mod(arr1, arr2)

print(newarr)
```
output:
```text
[ 1  6  3  0  0 27]
```

上面的示例将返回[1 6 3 0 0 27]，这是将10除以3（10％3），20除以7（20％7）和30除以9（30％9）所得的余数。

使用remainder()函数时，您将获得相同的结果：

例如：

返回余数：
```text
import numpy as np

arr1 = np.array([10, 20, 30, 40, 50, 60])
arr2 = np.array([3, 7, 9, 8, 2, 33])

newarr = np.remainder(arr1, arr2)

print(newarr)
```
output:
```text
[ 1  6  3  0  0 27]
```

## 8、divmod商和余数
divmod()函数返回商和mod。 返回值是两个数组，第一个数组包含商，第二个数组包含mod。

例如：

返回商和模：
```text
import numpy as np

arr1 = np.array([10, 20, 30, 40, 50, 60])
arr2 = np.array([3, 7, 9, 8, 2, 33])

newarr = np.divmod(arr1, arr2)

print(newarr)
```
output:
```text
(array([ 3,  2,  3,  5, 25,  1]), array([ 1,  6,  3,  0,  0, 27]))
```

上面的示例将返回：(array([3，2，3，5，25，1]），array（[1，6，3，0，0，27]))。
第一个数组表示商， 将10除以3、20除以7、30除以9等时的整数值。第二个数组代表相同除法的余数。

## 9、absolute绝对值
absolute()和abs()函数在元素方面执行相同的绝对值操作，但我们应使用absolute()以避免与python内置的math.abs()混淆

例如：

返回绝对值：
```text
import numpy as np

arr = np.array([-1, -2, 1, 2, 3, -4])

newarr = np.absolute(arr)

print(newarr)
```
output:
```text
[1 2 1 2 3 4]
```

上面的示例将返回[1 2 1 2 3 4]。