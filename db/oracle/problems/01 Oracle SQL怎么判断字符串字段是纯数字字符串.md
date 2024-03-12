# 一、REGEXP_LIKE函数(正则表达式)

ORACLE中的支持正则表达式的函数主要有下面四个：
* REGEXP_LIKE ：与LIKE的功能相似
* REGEXP_INSTR ：与INSTR的功能相似
* REGEXP_SUBSTR ：与SUBSTR的功能相似
* REGEXP_REPLACE ：与REPLACE的功能相似

它们在用法上与Oracle SQL 函数LIKE、INSTR、SUBSTR 和REPLACE 用法相同，
但是它们使用POSIX正则表达式代替了老的百分号（%）和通配符（_）字符。

POSIX正则表达式由标准的元字符（metacharacters）所构成：
```text
'^' 匹配输入字符串的开始位置，在方括号表达式中使用，此时它表示不接受该字符集合。
'$' 匹配输入字符串的结尾位置。如果设置了RegExp对象的Multiline属性，则 $ 也匹配'/n'或'/r'。
'.' 匹配除换行符之外的任何单字符。
'?' 匹配前面的子表达式零次或一次。
'+' 匹配前面的子表达式一次或多次。
'*' 匹配前面的子表达式零次或多次。
'|' 指明两项之间的一个选择。例子'^([a-z]+|[0-9]+)$'表示所有小写字母或数字组合成的字符串。
'( )' 标记一个子表达式的开始和结束位置。
'[]' 标记一个中括号表达式。
'{m,n}' 一个精确地出现次数范围，m=<出现次数<=n，'{m}'表示出现m次，'{m,}'表示至少出现m次。
/num 匹配 num，其中 num 是一个正整数。对所获取的匹配的引用。
```

字符簇：
```text
[[:alpha:]] 任何字母。
[[:digit:]] 任何数字。
[[:alnum:]] 任何字母和数字。
[[:space:]] 任何白字符。
[[:upper:]] 任何大写字母。
[[:lower:]] 任何小写字母。
[[:punct:]] 任何标点符号。
[[:xdigit:]] 任何16进制的数字，相当于[0-9a-fA-F]。
```

各种操作符的运算优先级：
```text
/转义符
(), (?:), (?=), [] 圆括号和方括号
*, +, ?, {n}, {n,}, {n,m} 限定符
^, $, anymetacharacter 位置和顺序
```

示例：
```text
--创建表
create table fzq
(
id varchar(4),
value varchar(10)
);
--数据插入
insert into fzq values('1','1234560');
insert into fzq values('2','1234560');
insert into fzq values('3','1b3b560');
insert into fzq values('4','abc');
insert into fzq values('5','abcde');
insert into fzq values('6','ADREasx');
insert into fzq values('7','123  45');
insert into fzq values('8','adc  de');
insert into fzq values('9','adc,.de');
insert into fzq values('10','1B');
insert into fzq values('10','abcbvbnb');
insert into fzq values('11','11114560');
insert into fzq values('11','11124560');
--regexp_like
--1)查询value中以1开头60结束的记录并且长度是7位
select * from fzq where value like '1____60';
select * from fzq where regexp_like(value,'1....60');
--查询value中以1开头60结束的记录并且长度是7位并且全部是数字的记录。
--使用like就不是很好实现了。
select * from fzq where regexp_like(value,'1[0-9]{4}60');
--也可以这样实现，使用字符集。
select * from fzq where regexp_like(value,'1[[:digit:]]{4}60');

-- 查询value中不是纯数字的记录
select * from fzq where not regexp_like(value,'^[[:digit:]]+$');

-- 查询value中不包含任何数字的记录。
select * from fzq where regexp_like(value,'^[^[:digit:]]+$');

--查询以12或者1b开头的记录.不区分大小写。
select * from fzq where regexp_like(value,'^1[2b]','i');
--查询以12或者1b开头的记录.区分大小写。
select * from fzq where regexp_like(value,'^1[2B]');

-- 查询数据中包含空白的记录。
select * from fzq where regexp_like(value,'[[:space:]]');

--查询所有包含小写字母或者数字的记录。
select * from fzq where regexp_like(value,'^([a-z]+|[0-9]+)$');

--查询任何包含标点符号的记录。
select * from fzq where regexp_like(value,'[[:punct:]]');
```
理解它的语法就可以了。其它的函数用法类似。

放在SELECT子句使用：
```text
-- 判断是不是纯数字字符串
select CASE WHEN regexp_like('123', '^[0-9]+$') THEN 'CHAR' ELSE 'NUMBER' END from dual;
select CASE WHEN regexp_like('123', '^[[:digit:]]+$') THEN 'CHAR' ELSE 'NUMBER' END from dual;
-- 不能单独使用
select regexp_like('123', '^[0-9]+$') from dual;
```

# 二、TRANSLATE函数

1.translate 与replace类似是替换函数，但translate是一次替换多个单个的字符。

2.基本用法，字符对应替换。
```text
select translate('1234567','123' ,'abc') from dual ; --1替换为a,2替换为b,3替换为c
结果：abc4567 。
```

3.如果 没有对应字符则替换为null;
```text
select translate('1234567','123' ,'ab') from dual；--3替换为null;
结果：ab4567.
```

4.如果对应字符过多，不影响
```text
select translate('1234567','123' ,'abccd') from dual；
结果：abc4567
```

5.如果替换字符整个为空字符 ，则直接返回null
```text
select translate('1234567','123' ,'') from dual；
结果：null;
```

6.如果想筛掉对应字符，应传入一个不相关字符，同时替换字符也加一个相同字符；
```text
select translate('1234567','&123' ,'&') from dual;
结果：4567；
```

7,如果相同字符对应多个字符，按第一个；
```text
select translate('12334567','1233' ,‘abcd') from dual;
结果：abcc4567;
```

8,如果想保留某些特定字符筛选掉其他的，比如筛掉汉字保留数字
```text
先把数字筛选掉，
select translate('你师看了3三楼2的6开8发','#0123456789' ,'#') from dual

再用筛选出的汉字去筛选原来的语句留下数字，
select 
    translate('你师看了3三楼2的6开8发','#'||translate('你师看了3三楼2的6开8发','#0123456789' ,'#'),'#') 
from dual；

结果：3268；
```

9，还有其他灵活用法，比如我可以判断两个字符串如果：字符串都是数字字符，然后数字字符的顺序不同，且每个字符只出现一次，

我可以判断他们包含的数字是不是完全一致；

比如比较123 和132；

```text
select 1 from dual 
where translate('0123456789','123' ,'aaaaaaaaaa') = translate('0123456789','132' ,'aaaaaaaaaa')
```

结果：1 ，也就是where中的等式成立；