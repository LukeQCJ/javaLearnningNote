# Python 机器学习 K-近邻算法 常用距离度量方法

K-近邻（K-Nearest Neighbors，KNN）算法中，选择合适的距离度量是非常重要的，因为它决定了【如何计算数据点之间的“相似性”】。
不同的距离度量可能会导致不同的KNN模型性能。选择哪种距离度量取决于数据的类型和问题的性质。
可以通过交叉验证来比较不同距离度量对模型性能的影响，以选择最合适的一种。

## 1、欧几里得距离（Euclidean Distance）
距离的度量最常用的距离度量方法，适用于连续型数据。它是在多维空间中两点间的“直线”距离。它表示两个点在n维空间中的实际距离。

Python中，可以使用多种方法来计算两个点之间的欧几里得距离。代码如下，

### 1）使用math模块
```text
import math


def euclidean_distance(point1, point2):
    return math.sqrt(sum((p1 - p2) ** 2 for p1, p2 in zip(point1, point2)))


# 测试
point1 = (1, 2, 3)
point2 = (4, 5, 6)
print(euclidean_distance(point1, point2))
```
output:
```text
5.196152422706632
```

### 2）使用NumPy
```text
import numpy as np


def euclidean_distance_np(point1, point2):
    return np.sqrt(np.sum((np.array(point1) - np.array(point2)) ** 2))


# 测试
p1 = (1, 2, 3)
p2 = (4, 5, 6)
print(euclidean_distance_np(p1, p2))
```
output:
```text
5.196152422706632
```

## 2、曼哈顿距离（Manhattan Distance）
KNN算法的关键之一是距离度量，它决定了如何计算特征空间中两点之间的距离。

曼哈顿距离（Manhattan Distance）是KNN中常用的距离度量之一。
曼哈顿距离，也称为城市街区距离，是通过计算两个点在标准坐标系上的绝对轴距总和来衡量的。

### 1）使用math模块
```text
def manhattan_distance(point1, point2):
    return sum(abs(p1 - p2) for p1, p2 in zip(point1, point2))


# 示例点
pt1 = (1, 2, 3)
pt2 = (4, 5, 6)

# 计算曼哈顿距离
distance = manhattan_distance(pt1, pt2)
print(distance)
```
output:
```text
9
```

### 2）使用NumPy
```text
import numpy as np


def manhattan_distance_numpy(point1, point2):
    return np.sum(np.abs(np.array(point1) - np.array(point2)))


# 示例点
pt1 = np.array([1, 2, 3])
pt2 = np.array([4, 5, 6])

# 计算曼哈顿距离
distance = manhattan_distance_numpy(pt1, pt2)
print(distance)
```
output:
```text
9
```

## 3、切比雪夫距离（Chebyshev Distance）
切比雪夫距离（Chebyshev Distance）是一种度量两个点之间距离的方法，在机器学习的K-近邻（KNN）算法中经常被使用作为距离度量的一种方式。
切比雪夫距离特别适用于那些【各个维度之间相对重要性相同】的情况。
切比雪夫距离在机器学习中可以用于KNN算法，特别是在那些【最大差异】最重要的场景中。

### 1）使用math模块
```text
def chebyshev_distance(p1, p2):
    # 确保输入数据有效
    if len(p1) != len(p2):
        raise ValueError("两个点的坐标数目不一致")

    # 计算每个坐标的差的绝对值
    max_diff = 0
    for i in range(len(p1)):
        diff = abs(p1[i] - p2[i])
        if diff > max_diff:
            max_diff = diff

    # 返回最大差值
    return max_diff


# 示例
pt1 = [1, 2, 3]
pt2 = [4, 5, 6]

distance = chebyshev_distance(pt1, pt2)

print(f"切比雪夫距离：{distance}")
```
output:
```text
切比雪夫距离：3
```

### 2）使用NumPy
```text
import numpy as np

p = np.array([1, 2, 3])
q = np.array([4, 6, 9])

chebyshev_dist = np.max(np.abs(p - q))
print(chebyshev_dist)
```
output:
```text
6
```

## 4、闵可夫斯基距离（Minkowski Distance）
使用K-近邻算法（KNN）时，距离度量是一个核心概念。

闵可夫斯基距离（Minkowski Distance）是一种广泛应用的距离度量，它是欧几里得距离和曼哈顿距离的一种推广。

通常建议对数据进行标准化或归一化处理，特别是当使用基于距离的算法时，因为距离度量对尺度非常敏感。

### 1）使用math模块
```text
import numpy as np


def minkowski_distance(p1, p2, p):
    # 确保输入数据有效
    if len(p1) != len(p2):
        raise ValueError("两个点的坐标数目不一致")
    # 计算每个坐标的差的绝对值的 p 次方
    sum_diff = 0
    for i in range(len(p1)):
        diff = abs(p1[i] - p2[i])
        sum_diff += diff ** p
    # 返回距离的 p 次方根
    return np.power(sum_diff, 1 / p)


# 示例
pt1 = [1, 2, 3]
pt2 = [4, 5, 6]
pt = 2

distance = minkowski_distance(pt1, pt2, pt)
print(f"闵可夫斯基距离：{distance}")
```
output:
```text
闵可夫斯基距离：5.196152422706632
```

### 2）使用NumPy
```text
import numpy as np


def minkowski_distance(p1, p2, p):
    # 将列表转换为 NumPy 数组
    p1 = np.array(p1)
    p2 = np.array(p2)
    # 计算每个坐标的差的绝对值的 p 次方
    diff = np.abs(p1 - p2) ** p
    # 返回距离的 p 次方根
    return np.power(np.sum(diff), 1 / p)


# 示例
pt1 = [1, 2, 3]
pt2 = [4, 5, 6]
pt = 2

distance = minkowski_distance(pt1, pt2, pt)
print(f"闵可夫斯基距离：{distance}")
```
output:
```text
闵可夫斯基距离：5.196152422706632
```

## 5、汉明距离（Hamming Distance）
汉明距离（Hamming Distance）是一种用于度量两个相同长度序列之间的差异的方法。
在机器学习和特别是在K-近邻算法中，汉明距离常用于处理分类变量或二进制数据。

汉明距离是两个字符串相同位置的【不同】字符的【数量】。

1）使用math模块
```text
def hamming_distance(seq1, seq2):
    if len(seq1) != len(seq2):
        raise ValueError("Sequences must be of equal length")
    return sum(el1 != el2 for el1, el2 in zip(seq1, seq2))


# 示例
s1 = [1, 0, 1, 1, 0, 1]
s2 = [0, 1, 1, 0, 0, 1]
distance = hamming_distance(s1, s2)
print(f"Hamming Distance: {distance}")
```
output:
```text
Hamming Distance: 3
```

2）使用NumPy
```text
import numpy as np


def hamming_distance(x, y):
    # 确保两个数组的形状相同
    if x.shape != y.shape:
        raise ValueError("两个数组的形状不一致")
    # 计算不同位的个数
    return np.sum(x != y)


# 示例
x = np.array([1, 0, 1, 1, 1])
y = np.array([1, 0, 0, 1, 1])

distance = hamming_distance(x, y)

print(f"汉明距离：{distance}")
```
output:
```text
汉明距离：1
```

## 6、scikit-learn中使用
在scikit-learn库中，需要使用不同的距离度量方法，也就是在初始化K-近邻（KNN）模型时设置metric参数。代码如下，

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report

# 加载数据集
iris = load_iris()
X = iris.data
y = iris.target

# 划分数据集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# 初始化不同的KNN模型
knn_euclidean = KNeighborsClassifier(n_neighbors=3, metric='euclidean')
knn_manhattan = KNeighborsClassifier(n_neighbors=3, metric='manhattan')
knn_chebyshev = KNeighborsClassifier(n_neighbors=3, metric='chebyshev')
knn_minkowski = KNeighborsClassifier(n_neighbors=3, metric='minkowski', p=2)
knn_hamming = KNeighborsClassifier(n_neighbors=3, metric='hamming')

# 训练并评估每个模型
models = [knn_euclidean, knn_manhattan, knn_chebyshev, knn_minkowski, knn_hamming]
metrics = ['euclidean', 'manhattan', 'chebyshev', 'minkowski', 'hamming']

for model, metric in zip(models, metrics):
    model.fit(X_train, y_train)
    y_pred = model.predict(X_test)
    print(f"Metrics for {metric} distance:")
    print(classification_report(y_test, y_pred))
```

详细文档：Python 机器学习 K-近邻算法