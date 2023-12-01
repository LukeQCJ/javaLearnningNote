【闭包】是一个【函数】，【返回值】依赖于声明【在函数外部的一个或多个变量】。

闭包通常来讲可以简单的认为是，可以**访问**【一个函数】里面【局部变量】的【另外一个函数】。

如下面这段匿名的函数：
```text
val multiplier = (i:Int) => i * 10 
```
 
函数体内有一个变量 i，它作为函数的一个参数。如下面的另一段代码：
```text
val multiplier = (i:Int) => i * factor
```

在 multiplier 中有两个变量：i 和 factor。
其中的一个 i 是函数的【形式参数】，在 multiplier 函数被调用时，i 被赋予一个新的值。
然而，factor不是形式参数，而是【自由变量】，考虑下面代码：
```text
var factor = 3  
val multiplier = (i:Int) => i * factor  
```

这里我们引入一个自由变量 factor，这个变量定义在函数外面。

这样定义的函数变量 multiplier 成为一个"闭包"，因为它引用到函数外面定义的变量，
定义这个函数的过程是将这个自由变量捕获而构成一个封闭的函数。

实例
```text
object Test {  
    def main(args: Array[String]) {  
        println( "muliplier(1) value = " +  multiplier(1) )  
        println( "muliplier(2) value = " +  multiplier(2) )  
    }  
    var factor = 3  
    val multiplier = (i:Int) => i * factor  
}
```

执行以上代码，输出结果为：
```text
$ scalac Test.scala  
$  scala Test  
muliplier(1) value = 3  
muliplier(2) value = 6  
```
