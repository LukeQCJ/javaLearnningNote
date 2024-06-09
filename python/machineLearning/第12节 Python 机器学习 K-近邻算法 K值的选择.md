# Python 机器学习 K-近邻算法 K值的选择

K-近邻（KNN）算法中，K值的选择对模型的性能有着显著的影响。
选择适当的K值是确保良好分类或回归性能的关键。
KNN 算法中的 K 值是指用来计算距离的近邻点的个数。
K 值的选择是 KNN 算法中的一个重要问题。
K 值的选择会影响 KNN 算法的性能。

## 1、选择说明
K-近邻算法通过查找测试数据点的K个最近的邻居来进行预测。
这些邻居的类别（对于分类问题）或值（对于回归问题）用于决定测试点的类别或值。K是一个正整数，通常较小。

**1）避免过小的K值**
```text
K值过小可能会导致模型过于复杂，容易受到数据中噪声的影响，从而导致过拟合。
避免在K-近邻算法中选择过小的K值是非常重要的，因为过小的K值会导致模型过于敏感，容易受到数据中噪声的影响，从而引起过拟合。
当K值很小时，模型可能会对训练数据中的异常值和噪声过于敏感。
```

**2）避免过大的K值**
```text
在使用K-近邻（KNN）算法时，避免选择过大的K值是很重要的。
过大的K值可能导致模型过于简化，无法捕捉到数据的关键特征，进而导致欠拟合。
如果数据集很小，选择一个较大的K值可能不合适，因为这会导致每个预测都依赖于数据集中的大部分点。
考虑数据集的特征数量和种类。
在高维数据集上，可能需要选择更小的K值，因为在高维空间中找到“近邻”变得更加困难。
```

**3）选择奇数K值**
```text
在使用K-近邻算法（KNN）时，选择奇数的K值可以是一个有益的策略，尤其是在解决分类问题时。
这是因为奇数的K值有助于避免在决策过程中出现平票的情况。
尽管奇数的K值可以帮助避免平票问题，但选择哪个奇数作为K值仍然需要通过测试和验证来确定。
```

4）考虑数据集大小
```text
选择K-近邻算法（KNN）的K值时，考虑数据集的大小是非常重要的。
数据集的大小不仅影响K值的选择，还影响算法的性能和准确性。
对于较小的数据集，较小的K值通常更好，因为较大的K值可能导致模型过于简化，无法捕捉数据中的细微差异。
但需要注意，K值过小（如1或2）可能导致模型对噪声过于敏感，从而过拟合。
在大型数据集上，可以考虑使用更大的K值。
这有助于减少噪声的影响，并提高预测的稳定性。
大型数据集通常包含更多的噪声和异常值，较大的K值有助于在这些情况下保持模型的健壮性。
```

## 2、操作方法

### 1）交叉验证

使用交叉验证来评估不同K值的性能。
通常，可以从较小的K值开始，逐渐增加，观察模型性能如何变化。
通过交叉验证来选择最佳的K值是一种有效的方法。

交叉验证是一种统计分析方法，用于评估机器学习模型的泛化能力。
它通过将数据集分成多个小的子集来进行，每次使用其中一个子集作为验证集，其余作为训练集。
这种方法可以帮助我们理解不同K值对模型性能的影响，并选择最优的K值。

```text
from sklearn.model_selection import cross_val_score
from sklearn.neighbors import KNeighborsClassifier
from sklearn.datasets import load_iris
import matplotlib.pyplot as plt

# 加载数据集
iris = load_iris()
X, y = iris.data, iris.target

# 试验不同的K值
k_range = range(1, 31)
k_scores = []

for k in k_range:
    knn = KNeighborsClassifier(n_neighbors=k)
    scores = cross_val_score(knn, X, y, cv=10, scoring='accuracy')  # 10折交叉验证
    k_scores.append(scores.mean())

# 找到最佳K值
best_k = k_range[k_scores.index(max(k_scores))]
print(f"最佳的K值是：{best_k}")

# 设置 Matplotlib 可以显示中文
plt.rcParams['font.sans-serif'] = ['Heiti TC']  # 设置字体为黑体
plt.rcParams['axes.unicode_minus'] = False  # 正常显示负号
"""
#Windows 中设置字体
plt.rcParams['font.sans-serif'] = ['Microsoft YaHei']  # 设置字体为微软雅黑
plt.rcParams['axes.unicode_minus'] = False  # 正常显示负号
"""
# 绘制K值与准确度的关系图
plt.plot(k_range, k_scores)
plt.xlabel(u'K value')
plt.ylabel(u'cross_val_score avg accuracy')
plt.title(u'K value & cross_val_score accuracy relationship')
plt.draw()
plt.show()
```

### 2）误差分析

绘制K值与误差率的图表，选择误差最小的K值。
如果存在多个具有相似误差的K值，选择更简单的模型（较大的K值）。
通过误差分析来选择K值是一种有效的方法。
误差分析通常涉及评估不同K值下模型的性能，以便找到平衡偏差和方差的最佳K值。

```text
import matplotlib.pyplot as plt
from sklearn.neighbors import KNeighborsClassifier
from sklearn.model_selection import train_test_split
from sklearn.datasets import load_iris
from sklearn.metrics import accuracy_score

# 加载数据集
iris = load_iris()
X, y = iris.data, iris.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# 测试不同的K值
k_range = range(1, 40)
accuracy_scores = []

for k in k_range:
    knn = KNeighborsClassifier(n_neighbors=k)
    knn.fit(X_train, y_train)
    y_pred = knn.predict(X_test)
    accuracy_scores.append(accuracy_score(y_test, y_pred))

# 绘制K值和准确率之间的关系
plt.plot(k_range, accuracy_scores)
plt.xlabel('Value of K for KNN')
plt.ylabel('Testing Accuracy')
plt.draw()
plt.show()
```

### 3）平方根法则

一种常用的启发式方法是选择数据点总数的平方根作为K值。
如有N个数据点，那么选择K值为sqrt(N)。
这个方法的一个变体是选择数据点总数的平方根的整数部分。
使用平方根法则来选择K-近邻算法（KNN）的K值是一种常见的经验法则。
这个方法简单而直观，尤其适用于初步探索和快速实验。
平方根法则建议将K值设置为训练集大小的平方根。

```text
from sklearn.model_selection import cross_val_score
from sklearn.neighbors import KNeighborsClassifier
from sklearn.datasets import load_iris
import numpy as np

# 加载数据集
iris = load_iris()
X, y = iris.data, iris.target

# 应用平方根法则选择K值
n_samples = X.shape[0]
k = int(np.sqrt(n_samples))

# 创建K-近邻模型
knn = KNeighborsClassifier(n_neighbors=k)

# 使用交叉验证评估模型
scores = cross_val_score(knn, X, y, cv=10, scoring='accuracy')
print(f"平均准确率: {scores.mean()}，使用K值: {k}")
```
output:
```text
平均准确率: 0.9733333333333334，使用K值: 12
```

详细文档：Python 机器学习 K-近邻算法