# Python 机器学习 SVM算法原理

支持向量机（Support Vector Machine, SVM）是一种强大的监督学习算法，用于分类、回归以及异常检测任务。

SVM在【高维空间】中寻找最优的决策边界（即超平面），以便最大化正负样本间的间隔。
它在机器学习领域尤其受欢迎，因为它可以处理线性和非线性数据，并且对于高维数据效果很好。
SVM的核心思想是找到一个最优的超平面，以此来区分不同的类别。

## 1、最大间隔原则
SVM的核心思想是找到一个超平面，使得它能够把不同类别的数据分开，并且确保最近的数据点到这个超平面的距离（即间隔）尽可能大。
这个最近的数据点被称为支持向量，它们直接影响到最终的决策边界。
通过最大化间隔，SVM旨在提高模型的泛化能力。
在二维空间中，超平面可以理解为一条直线，而在更高维度的空间中，超平面是将数据空间划分为两部分的n-1维的子空间。
SVM寻找的是能够正确分类所有训练样本的同时，使得到最近的数据点（支持向量）的距离最大化的超平面。

| 术语         | 定义                                                                |
|------------|-------------------------------------------------------------------|
| 超平面        | 在N维空间中，超平面是一个N-1维的子空间，用来分割不同的数据点。对于二维空间（平面），超平面就是一条线。             |
| 间隔（Margin） | 间隔定义为最近的数据点到决策边界的距离。SVM的目标是最大化这个间隔，从而创建最优的决策边界。                   |
| 支持向量       | 支持向量是离分隔超平面最近的那些数据点。这些点直接影响到超平面的位置和方向，因为超平面的最优位置是通过最大化这些点的间隔来确定的。 |
| 最优超平面      | 最优超平面是那个能够最大化正负类别之间间隔的超平面。它是由支持向量确定的，并且具有最好的泛化能力。                 |

理解最大间隔的原理示例代码如下，
```text
import numpy as np
from cvxopt import matrix, solvers

# 二维数据，标签为+1和-1
X = np.array([[3, 3], [4, 3], [1, 1]])
y = np.array([1, 1, -1])

# 构造拉格朗日乘子法中的P, q, A, b矩阵
n_samples, n_features = X.shape
K = np.zeros((n_samples, n_samples))
for i in range(n_samples):
    for j in range(n_samples):
        K[i, j] = np.dot(X[i], X[j])
P = matrix(np.outer(y, y) * K)
q = matrix(-np.ones(n_samples))
G = matrix(-np.eye(n_samples))
h = matrix(np.zeros(n_samples))
A = matrix(y, (1, n_samples), 'd')
b = matrix(0.0)

# 解决凸优化问题
solution = solvers.qp(P, q, G, h, A, b)
alphas = np.array(solution['x'])

# 计算w
w = sum(alphas[i] * y[i] * X[i] for i in range(n_samples))
# 计算b
cond = (alphas > 1e-4).reshape(-1)
b = y[cond] - np.dot(X[cond], w)
b = b[0]

print(f'w: {w}')
print(f'b: {b}')
```
output:
```text
     pcost       dcost       gap    pres   dres
 0: -2.6873e-01 -6.7301e-01  4e+00  2e+00  1e+00
 1: -1.4232e-01 -5.9278e-01  5e-01  3e-16  6e-16
 2: -2.3773e-01 -2.7513e-01  4e-02  6e-17  5e-16
 3: -2.4959e-01 -2.5037e-01  8e-04  6e-17  4e-16
 4: -2.5000e-01 -2.5000e-01  8e-06  6e-17  2e-16
 5: -2.5000e-01 -2.5000e-01  8e-08  3e-17  3e-16
Optimal solution found.
w: [0.50000008 0.5       ]
b: -2.0000002232258765
```

## 2、支持向量和决策边界
支持向量机（Support Vector Machine, SVM）是一种强大的监督学习算法，用于分类、回归和异常检测任务。

SVM 的核心思想是在特征空间中找到一个最优的超平面，以此来区分不同的类别。
这个超平面的选择旨在最大化其与最近训练样本点（支持向量）之间的距离，从而提高分类器的泛化能力。

SVM 在许多机器学习应用中表现优异，特别是在文本分类、图像识别和生物信息学等领域。
然而，当特征维数远大于样本数时，SVM 的性能可能会下降，且计算复杂度较高，这时需要仔细选择合适的核函数和调整参数。

### 1）支持向量（Support Vectors）
【支持向量】是**距离决策边界最近的那些数据点**。
这些点直接影响了决策边界的位置和方向，因为SVM的优化目标就是最大化这些点到决策边界的距离。

换句话说，只有支持向量才会影响超平面的定位，其他点（更远离决策边界的点）则不会。

### 2）决策边界（Decision Boundary）
决策边界是一个超平面，它在特征空间中分隔不同的类别。
在二维空间中，这个超平面就是一条直线。

SVM 的目标是找到最优的决策边界，即是类别之间的间隔（margin）最大化的边界。

### 3）代码
```text
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
import matplotlib.pyplot as plt
import numpy as np

# 加载数据集
iris = datasets.load_iris()
X = iris.data[:, :2]  # 为了便于可视化，仅使用前两个特征
y = iris.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 特征缩放
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

# 训练SVM模型
model = SVC(kernel='linear')
model.fit(X_train, y_train)


# 可视化决策边界
def plot_decision_boundary(X, y, model, title):
    x_min, x_max = X[:, 0].min() - 1, X[:, 0].max() + 1
    y_min, y_max = X[:, 1].min() - 1, X[:, 1].max() + 1
    xx, yy = np.meshgrid(np.arange(x_min, x_max, 0.02),
                         np.arange(y_min, y_max, 0.02))
    Z = model.predict(np.c_[xx.ravel(), yy.ravel()])
    Z = Z.reshape(xx.shape)
    plt.contourf(xx, yy, Z, alpha=0.8)
    plt.scatter(X[:, 0], X[:, 1], c=y, edgecolors='g')
    plt.xlabel('Sepal length')
    plt.ylabel('Sepal width')
    plt.title(title)
    plt.show()


plot_decision_boundary(X_train, y_train, model, 'SVM Decision Boundary with Iris Dataset')
```

## 3、核技巧
核技巧是SVM处理非线性可分数据集的一种方法。它允许SVM在原始特征空间中找到【非线性决策边界】，而无需显式地将数据映射到高维空间。

【核函数】可以被看作是一个衡量两个数据点在某个高维空间中相似度的函数，而不需要显式地计算它们在这个高维空间的坐标。

核函数有多种选择，常见的包括线性核、多项式核、径向基函数（RBF，也称为高斯核）和sigmoid核。

通过核函数，数据被隐式地映射到一个更高维的特征空间，
在这个空间中，原本线性不可分的数据可能变得线性可分，从而SVM可以找到一个分割超平面。

使用核技巧后，SVM的优化问题变为只依赖于数据点之间的内积，而核函数正是用来计算这些内积的。
这意味着SVM的训练只依赖于数据点之间的相对位置，而不是它们在特征空间中的绝对位置。

通过核技巧，SVM可以有效地处理复杂的非线性问题，使其成为一种非常强大且灵活的机器学习工具。
在实际应用中，选择合适的核函数和调整核函数的参数（如RBF核的γ）是获得良好性能的关键。

```text
from sklearn.svm import SVC
from sklearn.datasets import make_blobs
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 生成模拟数据
X, y = make_blobs(n_samples=100, centers=2, random_state=6)

# 分割数据集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 初始化SVM分类器，使用RBF核
clf = SVC(kernel='rbf', gamma='scale')

# 训练模型
clf.fit(X_train, y_train)

# 进行预测
y_pred = clf.predict(X_test)

# 计算准确率
print("Accuracy:", accuracy_score(y_test, y_pred))
```
output:
```text
Accuracy: 1.0
```

## 4、调整SVM参数
使用 SVM 时，调整合适的参数对于模型的性能至关重要。
参数 C 控制了模型对误分类的惩罚程度。
较小的 C 值会通过允许更多的误分类来增加间隔的大小，而较大的 C 值会减少误分类，但可能导致模型过拟合。

不同的核函数可以帮助模型在高维空间中找到最优超平面。选择哪个核函数以及如何设置其参数，通常需要通过交叉验证来确定。

```text
from sklearn.datasets import make_blobs
from sklearn.model_selection import train_test_split
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score

# 生成模拟数据
X, y = make_blobs(n_samples=100, centers=2, random_state=6)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 初始化SVM模型
svm_model = SVC(kernel='linear', C=1.0)

# 训练模型
svm_model.fit(X_train, y_train)

# 预测
y_pred = svm_model.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')
```
output:
```text
Accuracy: 1.0
```