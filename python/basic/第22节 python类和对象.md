# Python 类和对象

Python从设计之初就已经是一门面向对象的语言，正因为如此，在Python中创建一个类和对象是很容易的。
本章节我们将详细介绍Python的面向对象编程。
如果你以前没有接触过面向对象的编程语言，那你可能需要先了解一些面向对象语言的一些基本特征，
在头脑里头形成一个基本的面向对象的概念，这样有助于你更容易的学习Python的面向对象编程。

## 1、Python 类和对象
Python是一种面向对象的编程语言。

Python中的几乎所有东西都是对象，包括其属性和方法。

## 2、定义类
要创建一个类，请使用关键字class：

例如：

创建一个名为MyClass的类，并带有一个名为x的属性：
```text
class MyClass:
    x = 5
```

## 3、创建对象
现在我们可以使用名为MyClass的类来创建对象：

例如：

创建一个名为p1的对象，并打印x的值：
```text
p1 = MyClass()
print(p1.x)
```

## 4、__init__()方法
上面的示例是类和对象的最简单形式，在实际应用中并没有真正的用处。

要了解类的含义，我们必须了解内置的__init__()函数。

所有类都有一个名为__init__()的函数，它总是**在类初始化时执行**。

使用__init__()函数将值分配给对象属性，或创建对象时必须执行的其他操作：

例如：

创建一个名为Person的类，使用__init__()函数为名称和年龄分配值：
```text
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age


p1 = Person("cjavapy", 3)

print(p1.name)
print(p1.age)
```
output:
```text
cjavapy
3
```

注意：每当使用该类创建新对象时，都会自动调用__init__()函数。

## 5、构造函数(构造器)
构造函数，也被称为构造器，指的是当创建对象的时候，被自动调用的函数。
构造函数包括__new__和__init__。

1）如果类中未定义构造函数，当创建对象的时候，系统会自动调用构造函数

2）上述代码中 p1 = Person("cjavapy", 3) 的代码中p1获取的是__new__的返回值

3）如果将__new__显式的定义出来，则__new__的返回值必须是一个对象：super().__new__(cls)

4）先调用__new__，然后调用__init__

5）__new__()和__init__()是在创建对象的过程中自动调用的，无需手动调用

例如，
```text
class Person2:
    __slots__ = ('name', 'age')  # _slots__是类中的一个特性，简单理解为“槽的意思”，类只拥有放在槽里的属性

    def __new__(cls, *args, **kwargs):
        print("创建对象")
        # super().__new__(cls)表示创建一个对象
        return super().__new__(cls)

    def __init__(self, name, age):
        print("初始化对象")
        # 动态绑定属性
        self.name = name
        self.age = age


p2 = Person2('cjavapy', 11)
print(p2)
print(p2.name, p2.age)
```
output:
```text
创建对象
初始化对象
<__main__.Person2 object at 0x00000180BD5B5720>
cjavapy 11
```

## 6、对象方法
对象也可以包含方法。对象中的方法是属于该对象的函数。

让我们在Person类中创建一个方法：

例如：

插入一个输出问候语的函数，并在p1对象上执行它:
```text
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def myfunc(self):
        print("Hello my name is " + self.name)


p1 = Person("cjavapy", 3)
p1.myfunc()
```
output:
```text
Hello my name is cjavapy
```

注意：self参数是对该类当前实例的引用，用于访问属于该类的变量。

## 7、self参数
self参数是对该类**当前实例的引用**，用于访问属于该类的变量。

它不必命名为self，可以随意命名，但它必须是该类中任何函数的第一个参数：

例如：

使用cjavapy和abc代替self：
```text
class Person:
    def __init__(cjavapy, name, age):
        cjavapy.name = name
        cjavapy.age = age

    def myfunc(abc):
        print("Hello my name is " + abc.name)


p1 = Person("cjavapy", 3)
p1.myfunc()
```
output:
```text
Hello my name is cjavapy
```

## 8、修改对象属性
类属性是类本身的属性，无论这个类创建了多少的对象，其类属性依然只有一个，所以对象与对象之间可以共享类属性。
对象属性则是对象的属性，会根据对象的创建而创建，销毁而销毁，对象与对象之间不能共享对象属性。

可以像这样修改对象的属性：

例如：

将p1的年龄设置为40：
```text
p1.age = 40
```

## 9、删除对象属性
可以使用del关键字删除对象的属性：

例如：

从p1对象中删除age属性:
```text
del p1.age
```

## 10、删除对象
可以使用del关键字删除对象：

例如：

删除p1对象：
```text
del p1
```

## 11、pass语句
class定义不能为空，但是如果出于某种原因，有一个没有内容的class定义，需要使用pass语句，就不会报错。

例如：
```text
class Person:
    pass
```

## 12、Python面向对象
- **类(Class)**: 用来描述具有相同的属性和方法的对象的集合。
  它定义了该集合中每个对象所共有的属性和方法。对象是类的实例。
- **类变量**：类变量在整个实例化的对象中是公用的。类变量定义在类中且在函数体之外。
  类变量通常不作为实例变量使用。
- **数据成员**：类变量或者实例变量，用于处理类及其实例对象的相关的数据。
- **方法重写**：如果从父类继承的方法不能满足子类的需求，可以对其进行改写，
  这个过程叫方法的覆盖（override），也称为方法的重写。
- **局部变量**：定义在方法中的变量，只作用于当前实例的类。
- **实例变量**：在类的声明中，属性是用变量来表示的。
  这种变量就称为实例变量，是在类声明的内部但是在类的其他成员方法之外声明的。
- **继承**：即一个派生类（derived class）继承基类（base class）的字段和方法。
  继承也允许把一个派生类的对象作为一个基类对象对待。
  例如，有这样一个设计：一个Dog类型的对象派生自Animal类，这是模拟"是一个（is-a）"关系（例图，Dog是一个Animal）。
- **实例化**：创建一个类的实例，类的具体对象。
- **方法**：类中定义的函数。
- **对象**：通过类定义的数据结构实例。对象包括两个数据成员（类变量和实例变量）和方法。