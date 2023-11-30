【变量】是一种使用方便的【占位】符，用于【引用计算机内存地址】，变量创建后会占用一定的内存空间。

基于变量的数据类型，操作系统会进行内存分配并且决定什么将被储存在保留内存中。
因此，通过给变量分配不同的数据类型，你可以在这些变量中存储整数，小数或者字母。

## 变量声明
在学习如何声明变量与常量之前，我们先来了解一些变量与常量。
* 变量：在程序运行过程中，其值可能发生改变的量叫做变量。如：时间，年龄。
* 常量：在程序运行过程中，其值不会发生变化的量叫做常量。如：数值 3，字符'A'。

在 Scala 中，使用关键词 "var" 声明变量，使用关键词 "val" 声明常量。

声明变量实例如下：
```text
var myVar : String = "Foo"
var myVar : String = "Too"
```
以上定义了变量 myVar，我们可以修改它。

声明常量实例如下：
```text
val myVal : String = "Foo"
```
以上定义了常量 myVal，它是不能修改的。如果程序尝试修改常量 myVal 的值，程序将会在编译时报错。

## 变量类型声明
变量的类型在变量名之后等号之前声明。

定义变量的类型的语法格式如下：
```text
var VariableName : DataType [=  Initial Value]
或
val VariableName : DataType [=  Initial Value]
```

## 变量类型引用
在 Scala 中声明变量和常量不一定要指明数据类型，在没有指明数据类型的情况下，其数据类型是通过变量或常量的初始值推断出来的。

所以，如果在没有指明数据类型的情况下声明变量或常量必须要给出其初始值，否则将会报错。
```text
var myVar = 10;
val myVal = "Hello, Scala!";
```
以上实例中，myVar 会被推断为 Int 类型，myVal 会被推断为 String 类型。

## Scala 多个变量声明
Scala 支持多个变量的声明：
```text
val xmax, ymax = 100  // xmax, ymax都声明为100
```

如果方法返回值是元组，我们可以使用 val 来声明一个元组：
```text
scala> val pa = (40,"Foo")
pa: (Int, String) = (40,Foo)
```
