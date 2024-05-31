# Python 面向对象的继承

在OOP（Object Oriented Programming）程序设计中，
当我们定义一个class的时候，可以从某个现有的class 继承，新的class称为子类（Subclass），
而被继承的class称为基类、父类或超类（Base class、Super class）。

本文主要介绍 Python中面向对象的继承。

# 1、 Python 继承(Inheritance)
继承允许我们定义一个类，该类继承另一个类的所有方法和属性。

父类是从其继承的类，也称为基类。

子类是从另一个类（也称为派生类）继承的类。

# 2、定义父类
任何类都可以是父类，因此语法与创建类相同：

例如：

创建一个名为Person的类，该类具有firstname和lastname属性，以及一个printname方法：
```text
class Person:
    def __init__(self, fname, lname):
        self.firstname = fname
        self.lastname = lname

    def printname(self):
        print(self.firstname, self.lastname)


# 使用Person类创建一个对象，然后执行printname方法:

x = Person("liangliang", "xiaoming")
x.printname()
```
output:
```text
liangliang xiaoming
```

## 3、定义子类
要创建一个从另一个类继承功能的类，请在创建子类时将父类作为参数传递：

例如：

创建一个名为Student的类，该类将继承Person类的属性和方法：
```text
class Student(Person):
    pass
```

注意：如果还没想好类的实现，暂时不添加任何其他属性或方法时，请使用pass关键字。

实现继承后，Student类拥有与Person类相同的属性和方法。

例如：

使用Student类创建一个对象，然后执行printname方法：
```text
x = Student("marry", "levi")
x.printname()
```
output:
```text
marry levi
```

## 4、定义 __init__()方法
到目前为止，我们已经创建了一个子类，该子类从其父类继承属性和方法。

我们要向子类添加__init__()函数（而不是pass关键字）。

注意：每当使用该类创建新对象时，都会自动调用__init__()函数。

例如：

将__init__()函数添加到Student类中：
```text
class Student(Person):
    def __init__(self, fname, lname):
        # add properties etc.
```

当添加__init__()函数时，子类将不再继承父级的__init__()函数。

Note:子类的__init__()函数覆盖了父类的的__init__()函数的继承。

为了保持父级的__init__()函数的继承，请添加对父级的__init__()函数的调用：

例如：
```text
class Student(Person):
    def __init__(self, fname, lname):
        Person.__init__(self, fname, lname)
```

现在已经成功地添加了子类的__init__()方法，并保留了父类的继承，我们准备在子类的__init__()方法中添加功能。

## 5、使用super()方法
Python还有一个super()函数，它将使子类从其父类继承所有方法和属性：

例如：
```text
class Student(Person):
    def __init__(self, fname, lname):
        super().__init__(fname, lname)
```

通过使用super()函数，不必使用父元素的名称，它将自动从其父元素继承方法和属性。

## 6、定义属性
例如：

将名为graduationyear的属性添加到Student类中：
```text
class Student(Person):
    def __init__(self, fname, lname):
        super().__init__(fname, lname)
        self.graduationyear = 2019
```

在下面的示例中，年份2019应该是变量，并在创建Student类对象时传递，为此，在__init__()中添加另一个参数：

例如：

添加year参数，并在创建对象时传递正确的年份:
```text
class Student(Person):
    def __init__(self, fname, lname, year):
        super().__init__(fname, lname)
        self.graduationyear = year

x = Student("levi", "cjavapy", 2019)
```

## 7、定义方法
例如：

在Student中添加一个方法say:
```text
class Student(Person):
    def __init__(self, fname, lname, year):
        super().__init__(fname, lname)
        self.graduationyear = year

    def say(self):
        print("hello", self.firstname, self.lastname, "to the class of", self.graduationyear)
```

如果在子类中定义与父类中的函数同名的方法，则父方法的继承将被覆盖。