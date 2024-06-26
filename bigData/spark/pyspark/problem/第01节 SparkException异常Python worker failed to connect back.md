今天看文档学下pyspark，代码第一次运行就报错SparkException: Python worker failed to connect back.

意思就是spark找不到Python的位置。设置个环境变量就可以了

```text
import os

os.environ['PYSPARK_PYTHON'] = "D://apps//anaconda3//python.exe" # 放Python的位置
```