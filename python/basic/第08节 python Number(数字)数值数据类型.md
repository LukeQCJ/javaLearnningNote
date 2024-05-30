# Python Number(数字)数值数据类型

数值类型是数值型数据，支持整型、浮点、布尔类型和复数。

数值型即数值数据，用于表示数量，并可以进行数值运算。数值型数据由整数、小数、布尔值和复数组成，
分别对应整型类型、浮点类型、布尔类型和复数类型。

本文主要介绍Python中的数值数据类型，包括int、float、complex、random，以及相关的示例代码。

**Python 支持四种不同的数值类型**：
- 整型(Int) : 通常被称为是整型或整数，是正或负整数，不带小数点。
- 长整型(long integers) : 无限大小的整数，整数最后是一个大写或小写的L。
- 浮点型(floating point real values) : 浮点型由整数部分与小数部分组成，
  浮点型也可以使用科学计数法表示（2.5e2 = 2.5 x 102 = 250）
- 复数(complex numbers) : 复数由实数部分和虚数部分构成，可以用a + bj,或者complex(a,b)表示， 
  复数的实部a和虚部b都是浮点型。

| int    | long                  | float      | complex    |
|--------|-----------------------|------------|------------|
| 10     | 51924361L             | 0.0        | 3.14j      |
| 100    | -0x19323L             | 15.20      | 45.j       |
| -786   | 0122L                 | -21.9      | 9.322e-36j |
| 080    | 0xDEFABCECBDAECBFBAEl | 32.3+e18   | .876j      |
| -0490  | 535633629843L         | -90.       | -.6545+0J  |
| -0x260 | -052318172735L        | -32.54e100 | 3e+26J     |
| 0x69   | -4721885298529L       | 70.2-E12   | 4.53e-7j   |

长整型也可以使用小写"L"，但是还是建议您使用大写"L"，避免与数字"1"混淆。Python使用"L"来显示长整型。

Python还支持复数，复数由实数部分和虚数部分构成，可以用a + bj,或者complex(a,b)表示， 复数的实部a和虚部b都是浮点型。

除了L标识的长整类型，Python中还有三种数值类型：
- int
- float
- complex

在为数字类型的变量赋值时会创建它们：

例子，
```text
x = 1    # int
y = 2.8  # float
z = 1j   # complex
```

要验证Python中任何对象的类型，请使用以下type()函数：

例子，
```text
print(type(x))
print(type(y))
print(type(z))
```
结果：
```text
<class 'int'>
<class 'float'>
<class 'complex'>
```

## 1、int整数数据类型
Int或整数是无限长度的正整数或负整数（无小数）。

例子，

整数：
```text
x = 1
y = 35656222554887711
z = -3255522
print(type(x))
print(type(y))
print(type(z))
```
结果：
```text
<class 'int'>
<class 'int'>
<class 'int'>
```

## 2、float浮点数据类型
浮点数或"floating point number" 是一个正数或负数，包含一个或多个小数。

例子，

float类型:
```text
x = 1.10
y = 1.0
z = -35.59
print(type(x))
print(type(y))
print(type(z))
```
结果：
```text
<class 'float'>
<class 'float'>
<class 'float'>
```

浮点数也可以是带有"e"的科学数字，以表示10的幂。

例子，

float类型:
```text
x = 35e3
y = 12E4
z = -87.7e100
print(type(x))
print(type(y))
print(type(z))
```
结果：
```text
<class 'float'>
<class 'float'>
<class 'float'>
```

## 3、complex复数数据类型
复数以"j"表示为虚部：

例子，

复数：
```text
x = 3+5j
y = 5j
z = -5j
print(type(x))
print(type(y))
print(type(z))
```
结果：
```text
<class 'complex'>
<class 'complex'>
<class 'complex'>
```

## 4、类型转换
可以从一种类型转变成另一种同int()， float()和complex()方法：

例如，

从一种类型转换为另一种类型：
```text
x = 1    # int
y = 2.8  # float
z = 1j   # complex

# int转换成float:
a = float(x)
# float转换成int:
b = int(y)
# int转换成complex:
c = complex(x)

print(a)
print(b)
print(c)
print(type(a))
print(type(b))
print(type(c))
```
结果：
```text
1.0
2
(1+0j)
<class 'float'>
<class 'int'>
<class 'complex'>
```

注意：不能将复数转换为另一种数字类型。

## 5、random随机数
Python没有产生random()随机数的功能，但是Python有一个称为的内置模块random，可用于产生随机数：

例子，

导入random模块，并显示1到9之间的随机数：
```text
import random
print(random.randrange(1, 10))
```
结果：
```text
9
```

## 6、Python 数值类型转换
| 转换方法                   | 说明                            |
|------------------------|-------------------------------|
| int(x [,base ])        | 将x转换为一个整数                     |
| long(x [,base ])       | 将x转换为一个长整数                    |
| float(x )              | 将x转换到一个浮点数                    |
| complex(real [,imag ]) | 创建一个复数                        |
| str(x )                | 将对象 x 转换为字符串                  |
| repr(x )               | 将对象 x 转换为表达式字符串               |
| eval(str )             | 用来计算在字符串中的有效Python表达式,并返回一个对象 |
| tuple(s )              | 将序列 s 转换为一个元组                 |
| list(s )               | 将序列 s 转换为一个列表                 |
| chr(x )                | 将一个整数转换为一个字符                  |
| unichr(x )             | 将一个整数转换为Unicode字符             |
| ord(x )                | 将一个字符转换为它的整数值                 |
| hex(x )                | 将一个整数转换为一个十六进制字符串             |
| oct(x )                | 将一个整数转换为一个八进制字符串              |

## 7、Python math 模块、cmath 模块

Python 中数学运算常用的函数基本都在 math 模块、cmath 模块中。

Python math 模块提供了许多对**浮点数的数学运算**函数。

Python cmath 模块包含了一些用于**复数运算**的函数。

cmath 模块的函数跟 math 模块函数基本一致，区别是 cmath 模块运算的是复数，math 模块运算的是数学运算。

要使用 math 或 cmath 函数必须先导入：
```text
import math
```

查看 math 查看包中的内容:
```text
>>> import math
>>> dir(math)
['__doc__', '__file__', '__loader__', '__name__', '__package__', '__spec__', 'acos', 'acosh', 
'asin', 'asinh', 'atan', 'atan2', 'atanh', 'ceil', 'copysign', 'cos', 'cosh', 'degrees', 'e', 
'erf', 'erfc', 'exp', 'expm1', 'fabs', 'factorial', 'floor', 'fmod', 'frexp', 'fsum', 'gamma',
 'gcd', 'hypot', 'inf', 'isclose', 'isfinite', 'isinf', 'isnan', 'ldexp', 'lgamma', 'log', 
 'log10', 'log1p', 'log2', 'modf', 'nan', 'pi', 'pow', 'radians', 'sin', 'sinh', 'sqrt', 
 'tan', 'tanh', 'tau', 'trunc']
>>>
```

下文会介绍各个函数的具体应用。

1）查看 cmath 查看包中的内容
```text
>>> import cmath
>>> dir(cmath)
['__doc__', '__file__', '__loader__', '__name__', '__package__', '__spec__', 'acos', 'acosh', 
'asin', 'asinh', 'atan', 'atanh', 'cos', 'cosh', 'e', 'exp', 'inf', 'infj', 'isclose', 
'isfinite', 'isinf', 'isnan', 'log', 'log10', 'nan', 'nanj', 'phase', 'pi', 'polar', 'rect', 
'sin', 'sinh', 'sqrt', 'tan', 'tanh', 'tau']
>>>
```

2）实例
```text
>>> import cmath
>>> cmath.sqrt(-1)
1j
>>> cmath.sqrt(9)
(3+0j)
>>> cmath.sin(1)
(0.8414709848078965+0j)
>>> cmath.log10(100)
(2+0j)
>>>
```

## 8、Python数学函数

| 函数              | 返回值 ( 描述 )                                    |
|-----------------|-----------------------------------------------|
| abs(x)          | 返回数字的绝对值，如abs(-10) 返回 10                      |
| ceil(x)         | 返回数字的上入整数，如math.ceil(4.1) 返回 5                |
| cmp(x, y)       | 如果 x < y 返回 -1, 如果 x == y 返回 0, 如果 x > y 返回 1 |
| exp(x)          | 返回e的x次幂(ex),如math.exp(1) 返回2.718281828459045  |
| fabs(x)         | 返回数字的绝对值，如math.fabs(-10) 返回10.0               |
| floor(x)        | 返回数字的下舍整数，如math.floor(4.9)返回 4                |
| log(x)          | 如math.log(math.e)返回1.0,math.log(100,10)返回2.0  |
| log10(x)        | 返回以10为基数的x的对数，如math.log10(100)返回 2.0          |
| max(x1, x2,...) | 返回给定参数的最大值，参数可以为序列。                           |
| min(x1, x2,...) | 返回给定参数的最小值，参数可以为序列。                           |
| modf(x)         | 返回x的整数部分与小数部分，两部分的数值符号与x相同，整数部分以浮点型表示。        |
| pow(x, y)       | x**y 运算后的值。                                   |
| round(x [,n])   | 返回浮点数x的四舍五入值，如给出n值，则代表舍入到小数点后的位数。             |
| sqrt(x)         | 返回数字x的平方根                                     |

## 9、Python随机数函数

随机数可以用于数学，游戏，安全等领域中，还经常被嵌入到算法中，用以提高算法效率，并提高程序的安全性。

Python包含以下常用随机数函数：

| 函数                                | 描述                                                        |
|-----------------------------------|-----------------------------------------------------------|
| choice(seq)                       | 从序列的元素中随机挑选一个元素，比如random.choice(range(10))，从0到9中随机挑选一个整数。 |
| randrange ([start,] stop [,step]) | 从指定范围内，按指定基数递增的集合中获取一个随机数，基数默认值为 1。                       |
| random()                          | 随机生成下一个实数，它在[0,1)范围内。                                     |
| seed([x])                         | 改变随机数生成器的种子seed。如果你不了解其原理，你不必特别去设定seed，Python会帮你选择seed。   |
| shuffle(lst)                      | 将序列的所有元素随机排序                                              |
| uniform(x, y)                     | 随机生成下一个实数，它在[x,y]范围内。                                     |

## 10、Python三角函数

Python包括以下三角函数：

| 函数          | 描述                                     |
|-------------|----------------------------------------|
| acos(x)     | 返回x的反余弦弧度值。                            |
| asin(x)     | 返回x的反正弦弧度值。                            |
| atan(x)     | 返回x的反正切弧度值。                            |
| atan2(y, x) | 返回给定的 X 及 Y 坐标值的反正切值。                  |
| cos(x)      | 返回x的弧度的余弦值。                            |
| hypot(x, y) | 返回欧几里德范数 sqrt(x*x + y*y)。              |
| sin(x)      | 返回的x弧度的正弦值。                            |
| tan(x)      | 返回x弧度的正切值。                             |
| degrees(x)  | 将弧度转换为角度,如degrees(math.pi/2) ， 返回90.0。 |
| radians(x)  | 将角度转换为弧度                               |

## 11、Python数学常量

| 常量 | 描述                   |
|----|----------------------|
| pi | 数学常量 pi（圆周率，一般以π来表示） |
| e  | 数学常量 e，e即自然常数（自然常数）。 |