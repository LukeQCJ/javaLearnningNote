# Python 机器学习 K-近邻算法 KD树

在使用K-近邻（KNN）算法时，kd树（k-dimensional tree）是一种用于减少计算距离次数从而提高搜索效率的数据结构。

kd树是一种特殊的二叉树，用于存储k维空间中的数据点，使得搜索最近邻点更加高效。
KD树的构造过程是将数据分割成更小的区域，直到每个区域满足特定的终止条件。

## 1、构建KD树
在k维空间中的数据点集合中构建kd树，通常通过递归方式进行。
在每一层递归中，选择一个维度并在该维度上找到【中位数点】，以此点为界将数据集分割成两部分，递归地在每部分上重复这一过程。
k-d树每个节点都是k维数值点，而且每个非叶节点可以被认为是用一个轴-垂直的超平面将空间分割成两半。
KD树的构建和查询效率与数据分布有关。
对于某些特殊的数据分布，KD树可能不比简单的线性搜索快。
维数据（维度的数量很大）可能会导致KD树效率降低，原因是由于“维度的诅咒”。
在这些情况下，可能需要考虑其他的数据结构或算法。

从k个维度中选择一个轴。
通常，KD树的构建从第一个维度开始，然后沿着树的每个分支，依次选择下一个维度。
在每个更深的树级别上，选择下一个维度。
这通常通过模运算实现，有些实现选择方差最大的维度作为切分轴。
这种方法基于这样的假设：方差大的维度可能更有利于将数据分得更均匀。
某些高级的实现会分析数据在不同维度上的分布，选择最能够提高树效率的维度进行切分。
在选择的轴上找到中位数作为分割点，把数据分割成两部分。
可以有效地将搜索空间减少到与查询点更接近的区域。
对每个子集重复上述过程，选择轴并找到分割点，直到满足终止条件，如子集大小低于预设阈值或达到树的最大深度。
每个分割点成为树的一个节点，具有两个子节点：一个代表左子集，另一个代表右子集。
这样递归构造下去，直到叶节点。

```text
import json


class Node:
    def __init__(self, point, left=None, right=None):
        self.point = point
        self.left = left
        self.right = right


def build_kdtree(points, depth=0):
    n = len(points)
    if n == 0:
        return None

    k = len(points[0])  # 假设所有点在K维空间
    axis = depth % k  # 选择轴

    points.sort(key=lambda x: x[axis])
    median_index = n // 2

    return Node(
        point=points[median_index],
        left=build_kdtree(points[:median_index], depth + 1),
        right=build_kdtree(points[median_index + 1:], depth + 1)
    )


def kd_tree_to_dict(node):
    if node is None:
        return None

    return {
        "point": node.point,
        "left": kd_tree_to_dict(node.left),
        "right": kd_tree_to_dict(node.right)
    }


# 构建KD树
pts = [(3, 6), (17, 15), (13, 15), (6, 12), (9, 1), (2, 7), (10, 19)]
kdtree = build_kdtree(pts)

# 将KD树转换为字典
kdtree_dict = kd_tree_to_dict(kdtree)

# 将字典转换为JSON字符串
kdtree_json = json.dumps(kdtree_dict, indent=4)
print(kdtree_json)
```

## 2、搜索最近邻
K-近邻算法（KNN）中，KD树可以用来高效地搜索最近邻。
从根节点开始，向下遍历树直到叶子节点，同时记录下路径上的节点。
在回溯过程中，检查其他子树是否有可能包含更近的点。
如果可能，搜索那个子树。
选择距离查询点最近的点作为最近邻。

```text
from sklearn.neighbors import KDTree
import numpy as np

# 示例数据
X = np.array([[1, 2], [3, 4], [5, 6], [7, 8], [9, 10]])

# 构建KD树
kdtree = KDTree(X, leaf_size=2)

# 查询点
query_point = np.array([[2, 3]])

# 执行最近邻搜索
dist, ind = kdtree.query(query_point, k=1)

print("Nearest point:", X[ind])
print("Distance:", dist)
```
output:
```text
Nearest point: [[[1 2]]]
Distance: [[1.41421356]]
```

## 3、 kd树优势
相比于逐一计算所有数据点的距离，kd树通过减少需要计算的距离数来提高效率，特别是在数据维度不是非常高的情况下。
KD树是一种非常有效的结构，特别是在处理具有少量维度（如几十个维度或更少）的数据集时。
然而，它的性能可能会随着维度的增加而下降，这种现象称为“维度的诅咒”。
在高维数据集上，其他方法（如基于图的方法或近似算法）可能更有效。

## 4、scikit-learn 中的使用
在scikit-learn库中，KD树通常在后台自动构建，以加速KNN查询。
如使用KNeighborsClassifier或KNeighborsRegressor时，可以通过设置algorithm='kd_tree'来指定使用KD树。

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report, confusion_matrix

# 加载数据集
iris = load_iris()
X = iris.data
y = iris.target

# 划分数据集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 初始化KNN模型，使用KD树
knn = KNeighborsClassifier(n_neighbors=3, algorithm='kd_tree')

# 训练模型
knn.fit(X_train, y_train)

# 进行预测
y_pred = knn.predict(X_test)

# 打印结果
print(confusion_matrix(y_test, y_pred))
print(classification_report(y_test, y_pred))
```
output:
```text
[[10  0  0]
 [ 0  9  0]
 [ 0  0 11]]
              precision    recall  f1-score   support

           0       1.00      1.00      1.00        10
           1       1.00      1.00      1.00         9
           2       1.00      1.00      1.00        11

    accuracy                           1.00        30
   macro avg       1.00      1.00      1.00        30
weighted avg       1.00      1.00      1.00        30
```

详细文档：Python 机器学习 K-近邻算法