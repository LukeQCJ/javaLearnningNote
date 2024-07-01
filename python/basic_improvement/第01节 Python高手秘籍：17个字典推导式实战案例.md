探索字典推导式的奥秘。
别看它名字复杂，其实它就是一种让你的代码变得更加简洁、优雅的秘密武器。

## 第一站：基础岛
### 案例1：简单转换
想象一下，你有一堆人的名字（列表），你想把它们变成大写形式的字典键值对。
```text
names = ['Alice', 'Bob', 'Charlie']
upper_names = {name: name.upper() for name in names}
print(upper_names)
```
output:
```text
{'Alice': 'ALICE', 'Bob': 'BOB', 'Charlie': 'CHARLIE'}
```
这行代码的意思是：“对于names里的每个名字，创建一个新的键值对，键是名字本身，值是名字的大写形式。”简单吧？

## 第二站：映射大陆
### 案例2：数值翻倍
你得到了一个数字列表，想得到一个新字典，其中每个数字翻倍。
```text
numbers = [1, 2, 3]
doubled = {num: num * 2 for num in numbers}
print(doubled)
```
output:
```text
{1: 2, 2: 4, 3: 6}
```
“每一个数字，乘以2，装进字典”，搞定！

## 第三站：条件海域
### 案例3：筛选偶数
只保留列表中偶数的键值对。
```text
numbers = [1, 2, 3, 4, 5]
even_only = {num: "Even" for num in numbers if num % 2 == 0}
print(even_only)
```
output:
```text
{2: 'Even', 4: 'Even'}
```
“如果数字能被2整除，就让它成为字典的一部分。”

## 第四站：复合键值
### 案例4：姓名与年龄
给定两个列表，创建一个字典，名字作为键，年龄作为值。
```text
names = ['Alice', 'Bob']
ages = [24, 30]
name_age = {name: age for name, age in zip(names, ages)}
print(name_age)
```
output:
```text
{'Alice': 24, 'Bob': 30}
```
“手拉手，一对一对进字典。”

## 第五站：嵌套冒险
### 案例5：嵌套列表转字典
假设你有嵌套列表，想提取每个子列表的第一个元素作为键，整个子列表作为值。
```text
data = [['John', 28], ['Anna', 24], ['Peter', 35]]
nested_dict = {item[0]: item for item in data}
print(nested_dict)
```
output:
```text
{'John': ['John', 28], 'Anna': ['Anna', 24], 'Peter': ['Peter', 35]}
```
“每个小队的第一个成员当队长，整个小队跟着走。”

## 第六站：集合挑战
### 案例6：列表去重
将两个列表的交集作为键，它们在原列表中的索引作为值。
```text
list1 = [1, 2, 3, 4]
list2 = [2, 3, 4, 5]
indexes = {val: (i, j) for i, x in enumerate(list1) for j, y in enumerate(list2) if x == y}
print(indexes)
```
output:
```text
{2: (1, 0), 3: (2, 1), 4: (3, 2)}
```
“找到共同点，记住他们的家。”

## 第七站：字符串乐园
### 案例7：字符频率
统计字符串中每个字符出现的次数。
```text
text = "hello world"
freq = {char: text.count(char) for char in text}
print(freq)
```
output:
```text
{'h': 1, 'e': 1, 'l': 3, 'o': 2, ' ': 1, 'w': 1, 'r': 1, 'd': 1}
```
“每个字母，数一数你的朋友们。”

## 第八站：逻辑迷宫
### 案例8：基于条件的映射
创建一个字典，键是数字，值是该数字是否为质数的判断结果。
```text
def is_prime(n):
    return n > 1 and all(n % i for i in range(2, int(n ** 0.5) + 1))


numbers = list(range(1, 11))
prime_check = {num: is_prime(num) for num in numbers}
print(prime_check)
```
output:
```text
{1: False, 2: True, 3: True, 4: False, 5: True, 6: False, 7: True, 8: False, 9: False, 10: False}
```
“对于每个数字，做个质数测试。”

## 第九站：字典合并
### 案例9：合并字典
合并两个字典，相同键的值相加。
```text
dict1 = {"a": 1, "b": 2}
dict2 = {"b": 3, "c": 4}
merged = {k: dict1.get(k, 0) + dict2.get(k, 0) for k in set(dict1) | set(dict2)}
print(merged)
```
output:
```text
{'a': 1, 'c': 4, 'b': 5}
```
“你一半，我一半，合并起来不重叠。”

## 第十站：转换工厂
### 案例10：JSON到字典
解析简单的JSON字符串到字典。
```text
import json
json_str = '{"name": "Alice", "age": 30}'
parsed_json = json.loads(json_str)
print(parsed_json)
```
output:
```text
{'name': 'Alice', 'age': 30}
```
“从字符串的魔法中解放数据。”

## 第十一站：高级筛选
### 案例11：基于多个条件的字典生成
从一个大字典中根据多个条件筛选出子字典。
```text
big_dict = {'a': 1, 'b': 2, 'c': 3, 'd': 4, 'e': 5}
filtered = {k: v for k, v in big_dict.items() if 2 < v < 5}
print(filtered)
```
output:
```text
{'c': 3, 'd': 4}
```
“严格筛选，只留下符合条件的。”

## 第十二站：终极挑战
### 案例12：字典中字典的创造
创建一个字典，每个键对应另一个字典，根据一些复杂的规则。
```text
keys = ['A', 'B', 'C']
values = [{'x': 1, 'y': 2}, {'x': 3, 'y': 4}, {'x': 5, 'y': 6}]
compound_dict = {k: {f'val_{i}': v[i] for i in v} for k, v in zip(keys, values)}
print(compound_dict)
```
output:
```text
{'A': {'val_x': 1, 'val_y': 2}, 'B': {'val_x': 3, 'val_y': 4}, 'C': {'val_x': 5, 'val_y': 6}}
```
“字典里还有字典，层层嵌套的秘密。”

# 高级篇：进阶技巧与应用
## 第十三站：列表内字典的处理
### 案例13：转换列表为字典列表
想象你有一个列表，每个元素都是包含名字和年龄的列表，你想转换成字典列表。
```text
people_info = [["Alice", 28], ["Bob", 30]]
people_dicts = [{name: age} for name, age in people_info]
print(people_dicts)
```
output:
```text
[{'Alice': 28}, {'Bob': 30}]
```
这里，我们循环遍历列表，将每个子列表转换成一个字典，并添加到新的列表中。

## 第十四站：字典的深度操作
### 案例14：扁平化嵌套字典
有时你需要处理嵌套字典，比如将所有顶层键值对提取出来。
```text
nested_dict = {'a': {'x': 1}, 'b': {'y': 2, 'z': 3}}
flat_dict = {k: v for nested_key in nested_dict for k, v in nested_dict[nested_key].items()}
print(flat_dict)
```
output:
```text
{'x': 1, 'y': 2, 'z': 3}
```
这段代码通过两层循环，遍历了嵌套字典的所有第一层键值对。

## 第十五站：结合函数与推导式
### 案例15：使用函数增强表达
利用函数来增强字典推导式的功能，比如计算平方。
```text
nums = [1, 2, 3, 4]
square_dict = {num: (lambda x: x * x)(num) for num in nums}
print(square_dict)

# 或更简洁地使用 pow 函数
square_dict = {num: pow(num, 2) for num in nums}
print(square_dict)
```
output:
```text
{1: 1, 2: 4, 3: 9, 4: 16}
{1: 1, 2: 4, 3: 9, 4: 16}
```
这里展示了如何在字典推导式中直接调用函数，增加表达的灵活性。

## 第十六站：元组与字典推导的结合
### 案例16：基于元组的映射
如果你有一个元组列表，每个元组代表键值对，可以用推导式快速转换。
```text
pairs = [('name', 'Alice'), ('age', 28)]
mapping = dict(pairs)
print(mapping)
```
output:
```text
{'name': 'Alice', 'age': 28}
```
虽然不是传统意义上的字典推导，但使用 dict() 直接转换元组列表也是一种简洁的方法。

## 第十七站：动态属性设置
### 案例17：对象属性到字典
如果你想要将类的实例属性转化为字典，可以这样做：
```text
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age


person = Person('Alice', 28)
person_dict = {attr: getattr(person, attr) for attr in dir(person) if not attr.startswith("__")}
print(person_dict)
```
output:
```text
{'age': 28, 'name': 'Alice'}
```
这段代码通过遍历实例的所有属性并排除特殊方法，展示了如何将对象属性转化为字典。