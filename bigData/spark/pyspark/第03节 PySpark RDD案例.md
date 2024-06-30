# RDD 案例一：搜狗搜索点击行为分析
案例数据如下，详情见[SogouQ.txt](./data/03/SogouQ.txt)
```text
00:00:00	2982199073774412	[360��ȫ��ʿ]	8 3	download.it.com.cn/softweb/software/firewall/antivirus/20067/17938.html
00:00:00	07594220010824798	[������������]	1 1	news.21cn.com/social/daqian/2008/05/29/4777194_1.shtml
00:00:00	5228056822071097	[75810����]	14 5	www.greatoo.com/greatoo_cn/list.asp?link_id=276&title=%BE%DE%C2%D6%D0%C2%CE%C5
00:00:00	6140463203615646	[����]	62 36	www.jd-cd.com/jd_opus/xx/200607/706.html

........
```
需求：
```text
1、统计搜索关键词的top5
2、统计每个用户、每个搜索词的点击次数的top5
```
代码实现：
```text
from pyspark import SparkContext, SparkConf
import os
import jieba

os.environ['SPARK_HOME'] = '/export/server/spark'
os.environ['PYSPARK_PYTHON'] = '/root/anaconda3/bin/python3'
os.environ['PYSPARK_DRIVER_PYTHON'] = '/root/anaconda3/bin/python3'

if __name__ == '__main__':
    # 1、创建SparkContext核心对象
    conf = SparkConf().setAppName("sogou").setMaster("local[*]")
    sc = SparkContext(conf=conf)
    path = 'file:///export/data/workspace/ky06_pyspark/_02_SparkCore/data/SogouQ.txt'
    rdd = sc.textFile(name=path)
    # print(rdd.count())
    rdd_filter = rdd.filter(lambda line: str(line).strip() != '' and len(str(line).split()) == 6)
    rdd_map = rdd_filter.map(lambda line: (
        line.split()[0],
        line.split()[1],
        line.split()[2][1:-1],
        line.split()[3],
        line.split()[4],
        line.split()[5]
    ))
    # 缓存，提升性能，当多个rdd共用一个rdd时，可以使用缓存，当第一次执行时会缓存结果，方便其他rdd使用
    rdd_map = rdd_map.cache()

    # 1、统计 搜索关键词的top5
    rdd_res = (rdd_map.flatMap(lambda fields: jieba.cut(fields[2]))
               .map(lambda keyword: (keyword, 1))
               .reduceByKey(lambda agg, curr: agg + curr)
               .sortBy(lambda res: res[1], ascending=False))
    print(rdd_res.take(5))

    # 2、统计 每个用户、每个搜索词的点击次数的top5
    # rdd_res1 = (rdd_map.map(lambda fields: ((fields[1], fields[2]), 1))
    #             .reduceByKey(lambda agg, curr: agg + curr)
    #             .sortBy(lambda res: res[1], ascending=False))
    # print(rdd_res1.take(5))

    res1 = (rdd_map.map(lambda fields: ((fields[1], fields[2]), 1))
            .reduceByKey(lambda agg, curr: agg + curr)
            .top(5, lambda res: res[1]))
    print(res1)
```
# RDD 案例一：搜狗搜索点击行为分析
案例数据如下，详情见[access.log](./data/03/access.log)
```text
194.237.142.21 - - [18/Sep/2019:06:49:18 +0000] "GET /wp-content/uploads/2019/07/rstudio-git3.png HTTP/1.1" 304 0 "-" "Mozilla/4.0 (compatible;)"
183.49.46.228 - - [18/Sep/2019:06:49:23 +0000] "-" 400 0 "-" "-"
163.177.71.12 - - [18/Sep/2019:06:49:33 +0000] "HEAD / HTTP/1.1" 200 20 "-" "DNSPod-Monitor/1.0"
163.177.71.12 - - [18/Sep/2019:06:49:33 +0000] "HEAD / HTTP/1.1" 200 20 "-" "DNSPod-Monitor/1.0"
163.177.71.12 - - [18/Sep/2019:06:49:36 +0000] "HEAD / HTTP/1.1" 200 20 "-" "DNSPod-Monitor/1.0"

......

```
需求：
```text
1、统计网站的总访问量PV和独立用户量UV
2、统计访问URL的top10
```
代码实现：
```text
from pyspark import SparkContext, SparkConf
import os

os.environ['SPARK_HOME'] = '/export/server/spark'
os.environ['PYSPARK_PYTHON'] = '/root/anaconda3/bin/python3'
os.environ['PYSPARK_DRIVER_PYTHON'] = '/root/anaconda3/bin/python3'

if __name__ == '__main__':
    # 1、创建SparkContext核心对象
    conf = SparkConf().setAppName("click").setMaster("local[*]")
    sc = SparkContext(conf=conf)

    path = 'file:///export/data/workspace/ky06_pyspark/_02_SparkCore/data/access.log'
    rdd = sc.textFile(name=path)
    print(rdd.count())
    rdd_filter = rdd.filter(lambda line: str(line).strip() != '' and len(str(line).split()) >= 12)
    # 1、统计网站的总访问量PV和独立用户量UV
    print(rdd_filter.count())  # PV
    print(rdd_filter.map(lambda line: str(line).split()[0]).distinct().count())  # UV
    # 2、统计访问URL的top10
    res = (rdd_filter.map(lambda line: (str(line).split()[6], 1))
           .reduceByKey(lambda agg, curr: agg + curr)
           .top(10, lambda field: field[1]))
    print(res)
```