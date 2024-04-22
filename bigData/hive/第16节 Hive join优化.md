# 一、小表join大表
小表Join大表采用Map Join（在hive中先后顺序无所谓, 但是在HIVE建议小表在前, 大表在后）

在hive中需要开启Map Join的支持, 默认就是True

```text
-- 开启Map Join的支持
set hive.auto.convert.join=true;
-- 小表数据量的最大阈值: 默认 20971520(20M)
set hive.auto.convert.join.noconditionaltask.size=512000000
```
Map Join 支持所有表类型(内部表, 外部表, 分桶表, 分区表)

# 二、中型表join大表
使用分桶Map Join( Bucket Map Join)

前提：
* Join两个表必须是分桶表
* 一个表的分桶数量是另一个表的分桶数量的整倍数
* 分桶列 必须 是 Join条件的列
* 必须建立在Map Join场景中

开启Bucket Map Join 支持

代码如下（示例）：
```text
set hive.optimize.bucketmapjoin = true;
```

# 三、大表join大表
SMB(Sort Merge Bucket) Map Join

条件：
* 两个表必须是分桶表
* 开启SMB Map Join 支持

```text
-- 开启Bucket Map Join
set hive.optimize.bucketmapjoin = true;

-- 开启 SMB Join支持
set hive.auto.convert.sortmerge.join=true;
set hive.auto.convert.sortmerge.join.noconditionaltask=true;
set hive.optimize.bucketmapjoin.sortedmerge = true;

-- 开启自动尝试使用SMB Join
set hive.optimize.bucketmapjoin.sortedmerge = true

-- 开启强制排序
set hive.enforce.sorting=true;
```
- 两个表的分桶的数量是一致的
- 分桶列 必须是 Join条件列 , 同时必须保证按照分桶字段列进行排序
- 建表的时候:
  ```text
    create table test_smb_2(
    mid string,
    age_id string
    ) CLUSTERED BY(mid) SORTED BY(mid)INTO 500 BUCKETS;
    让桶内数据可以自动排序
  ```
  
# 四、join数据倾斜的处理
在reduce中，某一个，或者某几个的分组k2对应value的数据比较多， 从而引起数据倾斜问题。

方案一
- 当join表满足map join，bucket map join以及SMB Map join的使用条件时，可以使用相应的map join解决数据倾斜。


方案二
- 将那些产生倾斜的k2和对应value数据, 从当前这个MR移植出去, 单独找一个MR来处理即可, 处理后, 和之前MR的汇总结果即可。

- **运行期处理方案**
  
  在执行MR的时候，会动态统计每一个k2的值出现的重复的数量，当这个重复的数量达到一定阈值后，认为当前这个k2的数据存在数据倾斜，
  自动将其剔除，交由给一个单独的MR来处理即可，两个MR处理完成后，将结果，基于union all合并在一起即可。
    ```text
    set hive.optimize.skewjoin=true;
    -- 设置当某个key值出现的重复次数达到一定值后，系统将该key值放入新的map进行处理（需要自己进行根据实际情况设置）
    set hive.skewjoin.key=100000;
    ```
- **编译期处理方案**
  
  在创建这个表的时候，我们就可以预知到后续插入到这个表中，那些key的值会产生倾斜，在建表的时候，将其提前配置设置好即可，
  在后续运行的时候，程序会自动将设置的k2的值数据单独找一个MR来进行单独的处理操作，处理后，再和原有MR进行union all的合并操作。
    ```text
    set hive.optimize.skewjoin.compiletime=true;
    建表
    CREATE TABLE list_bucket_single (key STRING, value STRING)
    -- 倾斜的字段和需要拆分的key值
    SKEWED BY (key) ON (1,5,6)
    -- 为倾斜值创建子目录单独存放
    [STORED AS DIRECTORIES];
    ```
  该方案需要在创建表是就声明那个字段的那些值需要进行另外的map处理。

# 总结
不管是Bucket Map Join，SMB Map Join都是基于map Join的，中标，大表就拆成小表就就可以进行join了，
数据倾斜的解决方案原理都是将重复较多的数据，全部到一个reduce端的数据放在单独的map端进行处理，
在和原来的map处理好的数据进行union all。
