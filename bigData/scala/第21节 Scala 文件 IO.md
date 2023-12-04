Scala 进行文件写操作，直接用的都是 java中 的 I/O 类 （java.io.File)：

实例
```text
import java.io._

object Test {
    def main(args: Array[String]) {
        val writer = new PrintWriter(new File("test.txt" ))
        writer.write("菜鸟教程")
        writer.close()
    }
}
```
执行以上代码，会在你的当前目录下生产一个 test.txt 文件，文件内容为"菜鸟教程":
```text
$ scalac Test.scala
$ scala Test
$ cat test.txt
菜鸟教程
```

## 从屏幕上读取用户输入
有时候我们需要接收用户在屏幕输入的指令来处理程序。实例如下：

实例
```text
import scala.io._
object Test {
    def main(args: Array[String]) {
        print("请输入菜鸟教程官网 : " )
        val line = StdIn.readLine()
        println("谢谢，你输入的是: " + line)
    }
}
```
Scala2.11 后的版本 Console.readLine 已废弃，使用 scala.io.StdIn.readLine() 方法代替。

执行以上代码，屏幕上会显示如下信息:
```text
$ scalac Test.scala
$ scala Test
请输入菜鸟教程官网 : www.runoob.com
谢谢，你输入的是: www.runoob.com
```

## 从文件上读取内容
从文件读取内容非常简单。我们可以使用 Scala 的 Source 类及伴生对象来读取文件。
以下实例演示了从 "test.txt"(之前已创建过) 文件中读取内容:

实例
```text
import scala.io.Source

object Test {
    def main(args: Array[String]) {
        println("文件内容为:" )
        Source.fromFile("test.txt" ).foreach {
            print
        }
    }
}
```
执行以上代码，输出结果为:
```text
$ scalac Test.scala
$ scala Test
文件内容为:
菜鸟教程
```
