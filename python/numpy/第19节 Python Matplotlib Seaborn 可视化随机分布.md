# Python Matplotlib Seaborn 可视化随机分布

Matplotlib 是 Python 的绘图库。
它可与 NumPy 一起使用，提供了一种有效的 MatLab 开源替代方案。 
它也可以和图形工具包一起使用，如 PyQt 和 wxPython。

本文主要介绍Python Matplotlib Seaborn 可视化随机分布。

更新matplotlib。
```text
pip install --user --upgrade matplotlib
```

## 1、使用Seaborn可视化随机分布
Seaborn是一个在下面使用Matplotlib绘制图的库。 它将用于可视化随机分布。

## 2、安装Seaborn
如果您已经在系统上安装了Python和PIP，请使用以下命令进行安装：
```text
C:\Users\Your Name>pip install seaborn
```

如果您使用Jupyter，请使用以下命令安装Seaborn：
```text
C:\Users\Your Name>!pip install seaborn
```

## 3、Distplots
Distplot代表分布图，它以数组作为输入并绘制与数组中点的分布相对应的曲线。

## 4、Import Matplotlib
```text
import matplotlib.pyplot as plt
```
您可以在我们的Matplotlib教程中了解Matplotlib模块。

## 5、Import Seaborn
```text
import seaborn as sns
```

## 6、绘制 Displot
例如：
```text
import matplotlib.pyplot as plt
import seaborn as sns

sns.distplot([0, 1, 2, 3, 4, 5])

plt.show()
```

## 7、绘制没有柱状图的分布图
例如：
```text
import matplotlib.pyplot as plt
import seaborn as sns

sns.distplot([0, 1, 2, 3, 4, 5], hist=False)

plt.show()
```

注意：在本文中，使用sns.distplot（arr，hist = False）可视化随机分布。