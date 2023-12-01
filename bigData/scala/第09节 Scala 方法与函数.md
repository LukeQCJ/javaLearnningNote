Scala 有方法与函数，二者在语义上的区别很小。
Scala【方法是类的一部分】，而【函数是一个对象可以赋值给一个变量】。换句话来说在类中定义的函数即是方法。

Scala 中的方法跟 Java 的类似，方法是组成类的一部分。

Scala 中的函数则是一个完整的对象，Scala 中的函数其实就是继承了 Trait 的类的对象。

Scala 中使用 val 语句可以定义函数，def 语句定义方法。

```text
class Test{
    def m(x: Int) = x + 3
    val f = (x: Int) => x + 3
}
```
注意：有些翻译上函数(function)与方法(method)是没有区别的。

## 方法声明
Scala 方法声明格式如下：
```text
def functionName ([参数列表]) : [return type]
```

如果你不写等于号和方法主体，那么方法会被隐式声明为抽象(abstract)，包含它的类型于是也是一个抽象类型。

## 方法定义
方法定义由一个 def 关键字开始，紧接着是可选的参数列表，一个冒号 : 和方法的返回类型，一个等于号 = ，最后是方法的主体。

Scala 方法定义格式如下：
```text
def functionName ([参数列表]) : [return type] = {
    function body
    return [expr]
}
```

以上代码中 return type 可以是任意合法的 Scala 数据类型。参数列表中的参数可以使用逗号分隔。

以下方法的功能是将两个传入的参数相加并求和：

实例
```text
object add {
    def addInt( a:Int, b:Int ) : Int = {
        var sum:Int = 0
        sum = a + b
        return sum
    }
}
```
如果方法没有返回值，可以返回为 Unit，这个类似于 Java 的 void, 实例如下：

实例
```text
object Hello {
    def printMe( ) : Unit = {
        println("Hello, Scala!")
    }
}
```

## 方法调用
Scala 提供了多种不同的方法调用方式：

以下是调用方法的标准格式：
```text
functionName( 参数列表 )
```

如果方法使用了实例的对象来调用，我们可以使用类似java的格式 (使用 . 号)：
```text
[instance.]functionName( 参数列表 )
```

以上实例演示了定义与调用方法的实例:

实例
```text
object Test {
    def main(args: Array[String]) {
        println( "Returned Value : " + addInt(5,7) );
    }
    
    def addInt( a:Int, b:Int ) : Int = {
        var sum:Int = 0
        sum = a + b
        return sum
    }
}
```
执行以上代码，输出结果为：
```text
$ scalac Test.scala
$ scala Test
Returned Value : 12
```

Scala 也是一种函数式语言，所以函数是 Scala 语言的核心。以下一些函数概念有助于我们更好的理解 Scala 编程：
```text
函数概念解析接案例
函数传名调用(Call-by-Name)	指定函数参数名
函数 - 可变参数	递归函数
默认参数值	高阶函数
内嵌函数	匿名函数
偏应用函数	函数柯里化(Function Currying)
```
