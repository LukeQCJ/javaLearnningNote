## 问题
### 数据
```text
1    李雷    男    18    100
1    李雷    男    18    100
1    李雷    男    18    100
2    韩梅梅    女    17    90
2    韩梅梅    女    17    90
2    韩梅梅    女    17    90
3    小明    男    19    80
3    小明    男    19    80
3    小明    男    19    80
```
上面表中有多个重复的数据，请去重。

正确数据如下：
```text
1    李雷    男    18    100
2    韩梅梅    女    17    90
3    小明    男    19    80
```

### 解决方案
1、使用distinct关键字去重，但是这个不推荐使用。因为distinct只使用一个reduce。
```text
select distinct * from student;
```

2、使用group by去重，可选字段多的时候不推荐使用。
```text
select * from student group by id,name
```

3、使用row_number() over()去重，这个是最推荐的方法。
```text
select * from (
    select 
        *,
        row_number() over(partition by id,name order by id) as rn 
    from student
) t where t.rn = 1
```