# Python 机器学习 SVM的核方法

支持向量机（SVM）是一种广泛应用于分类、回归和其他任务的强大机器学习模型。

SVM 的核心思想是在特征空间中找到一个超平面，以最大化地隔开不同类别的数据点。

当数据不是线性可分时，SVM 可以通过使用核技巧将数据映射到更高维度的空间，在这个空间中数据可能是线性可分的，从而实现非线性分类。

## 1、核方法（Kernel Trick）
核技巧是 SVM 中的一个关键概念，它允许算法有效地在高维空间中工作，而无需直接计算在这个空间中的点。
这通过定义一个核函数实现，该函数计算输入空间中任意两个点的高维特征空间内的内积，而无需显式地将点映射到该空间。

核方法（Kernel Trick）是支持向量机（SVM）中一个非常重要的概念，
使得SVM能够有效地在高维空间中找到最优决策边界，而不需要显式地将数据映射到这些高维空间。
这个技巧在处理非线性可分问题时尤其有用。


核方法的基本思想是通过一个【非线性映射】将原始输入空间（可能是非线性可分的）转换到一个更高维的特征空间，
在这个新的空间中数据更有可能是线性可分的。
然而，直接在高维空间中工作是计算上不可行的，因为特征空间的维度可能非常大，甚至是无穷的。

核技巧的巧妙之处在于它允许我们在这些高维空间中进行计算，而不需要显式地执行映射。
这是通过定义一个核函数来实现的，核函数可以计算出原始输入空间中任意两个点映射后的内积，而无需知道映射后的具体坐标。

## 2、常用的核函数
核函数的选择以及相应参数的设定，很大程度上取决于数据集的特性和问题本身。
通常，【线性核】用于线性可分的数据，当数据非线性可分时，可以尝试【RBF核】，因为它的灵活性最高。
【多项式核】和【Sigmoid核】可以根据特定问题的需要来尝试。

【核函数参数的选择】通常通过【交叉验证】来完成，以找到最佳的模型性能。

### 1）线性核（Linear Kernel）

线性核是最简单的核函数之一，它基本上不会增加特征空间的维度，因此适用于线性可分的数据集。
线性核由于其简单性，在数据线性可分时非常有效，同时也比较容易解释和理解。

在处理大型数据集或特征数量远大于样本数量的情况下，使用线性核的SVM通常是一个不错的选择。

```text
from sklearn.svm import SVC
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 生成模拟数据
X, y = make_classification(n_samples=100, n_features=2, n_informative=2, n_redundant=0, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建SVM分类器，使用线性核
clf = SVC(kernel='linear')

# 训练模型
clf.fit(X_train, y_train)

# 预测测试集
y_pred = clf.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')
```
output:
```text
Accuracy: 0.95
```

### 2）多项式核（Polynomial Kernel）

多项式核（Polynomial Kernel）是支持向量机（SVM）中一种常用的核函数，
它可以将输入空间的数据映射到一个高维特征空间，使得原本在输入空间中线性不可分的数据在高维特征空间中变得线性可分。

多项式核函数的能力在于通过核技巧（Kernel Trick），避免了直接在高维空间中计算点积的高昂计算成本。
适用于数据集的非线性特征是相互作用的情况。可以显式地控制多项式的阶数和其他参数，增加模型的灵活性。

多项式核函数的参数（特别是degree）在很大程度上决定了模型的复杂度和拟合能力。
较高的度可能导致模型在训练数据上过拟合，而较低的度可能导致欠拟合，因此选择合适的参数对于获得良好的模型性能至关重要。

```text
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score

# 生成模拟数据
X, y = make_classification(n_samples=100, n_features=2, n_informative=2, n_redundant=0, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 数据标准化
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

# 初始化并训练SVM模型，使用多项式核
svc = SVC(kernel='poly', degree=3, gamma='auto', coef0=1)
svc.fit(X_train, y_train)

# 预测测试集
y_pred = svc.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')
```
output:
```text
Accuracy: 1.0
```

### 3）径向基函数核（RBF Kernel）或高斯核

径向基函数核（Radial Basis Function Kernel，简称RBF核）或高斯核是支持向量机（SVM）中最流行和广泛使用的核函数之一。
RBF核通过一个非线性转换将输入空间映射到一个高维特征空间，使得原本线性不可分的数据在新的特征空间中变得线性可分。

RBF核的作用可以直观地解释为：它测量两个样本点之间的“相似性”，并通过指数函数将这种相似性转换成一个(0, 1)之间的值。
如果两个样本点非常相似（即它们之间的距离很小），那么RBF核的值将接近1；如果它们相差很大（即距离很远），核的值就会接近0。

```text
import numpy as np


def rbf_kernel(x1, x2, gamma):
    """
        计算RBF核
    参数:
        x1, x2 -- 输入向量
        gamma -- RBF核的参数
    返回:
        RBF核的计算结果
    """
    distance = np.linalg.norm(x1 - x2) ** 2
    return np.exp(-gamma * distance)


# 示例使用
x1 = np.array([1, 2, 3])
x2 = np.array([2, 3, 4])
gamma = 0.1

# 计算RBF核
kernel_value = rbf_kernel(x1, x2, gamma)
print(f'RBF Kernel Value: {kernel_value}')
```
output:
```text
RBF Kernel Value: 0.7408182206817179
```

### 4）Sigmoid 核

Sigmoid核是一种核函数，形式上类似于神经网络中使用的sigmoid激活函数。

Sigmoid核将数据映射到-1到1的范围内，类似于神经网络中的激活函数。它可以用于二分类问题，尤其是当数据呈现非线性分布时。
然而，Sigmoid核并不总是保证满足核函数的正定性质，因此在使用时需要谨慎选择参数，以避免模型训练不稳定。
虽然Sigmoid核在某些情况下可以提供不错的性能，但它可能不如其他核函数（如RBF核）那样广泛适用于各种数据集。
因此，选择核函数时应考虑数据的具体特征和分布情况。

```text
from sklearn.svm import SVC
from sklearn.datasets import make_moons
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score

# 生成非线性可分数据集
X, y = make_moons(n_samples=100, noise=0.1, random_state=42)

# 数据标准化
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# 划分数据集
X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.3, random_state=42)

# 创建带有Sigmoid核的SVC模型
svc = SVC(kernel='sigmoid', C=1.0)

# 训练模型
svc.fit(X_train, y_train)

# 预测测试集
y_pred = svc.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy with Sigmoid Kernel: {accuracy}')
```
output:
```text
Accuracy with Sigmoid Kernel: 0.7333333333333333
```