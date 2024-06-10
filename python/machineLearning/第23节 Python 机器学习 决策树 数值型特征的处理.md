# Python 机器学习 决策树 数值型特征的处理

Python 机器学习中，特征提取是将原始数据转换为能够被模型有效利用的格式的过程。

对于决策树模型而言，特征提取尤其重要，因为好的特征可以显著提升模型的预测性能。
在实际应用中，需要根据具体情况选择合适的特征提取方法。

数值型特征是机器学习中常见的一种特征类型，它指的是可以表示为数字的特征。
特别是在构建决策树模型时，对数值型特征的处理是一个重要步骤。
决策树能够直接处理数值型数据，但如何有效地利用这些数值型特征对模型的性能有重要影响。

## 1、分箱（Binning）
【分箱】是将连续的数值型特征分成一系列的区间的过程。
这种方法有助于减少数值型特征的复杂性，使模型更容易捕捉到特征与目标变量之间的关系。
分箱可以是等宽的（每个箱子的范围宽度相同）或等深的（每个箱子包含相同数量的观测值）。

在 scikit-learn 中，可以使用 KBinsDiscretizer 类来实现分箱。常用参数如下，

| 参数	      | 描述与默认值                                                                                                                        |
|----------|-------------------------------------------------------------------------------------------------------------------------------|
| n_bins   | 类型：int 或一系列的 int。默认值：5。指定区间的数量。单一整数时，所有特征使用相同数量的区间；一系列整数时，为每个特征指定不同数量的区间。                                                     |
| encode   | 类型：{'onehot', 'onehot-dense', 'ordinal'}。默认值：'onehot'。离散化后的输出编码格式。'onehot'返回稀疏矩阵，'onehot-dense'返回密集数组，'ordinal'返回序号。          |
| strategy | 类型：{'uniform', 'quantile', 'kmeans'}。默认值：'quantile'。定义如何计算区间的策略。'uniform'每个区间宽度相同，'quantile'每个区间样本数相同，'kmeans'基于K-均值聚类确定区间边界。 |

```text
import numpy as np
from sklearn.preprocessing import KBinsDiscretizer

# 创建数据集
X = np.array([[2], [4], [6], [8], [10], [12], [14], [16], [18], [20]]).astype(float)

# 应用 KBinsDiscretizer，尝试不同的 encode 和 strategy 参数
# 使用 quantile 策略和 ordinal 编码
est_quantile_ordinal = KBinsDiscretizer(n_bins=3, encode='ordinal', strategy='quantile')
Xt_quantile_ordinal = est_quantile_ordinal.fit_transform(X)
print("Quantile strategy with ordinal encoding:\n", Xt_quantile_ordinal)

# 使用 uniform 策略和 onehot-dense 编码
est_uniform_onehot = KBinsDiscretizer(n_bins=3, encode='onehot-dense', strategy='uniform')
Xt_uniform_onehot = est_uniform_onehot.fit_transform(X)
print("Uniform strategy with onehot-dense encoding:\n", Xt_uniform_onehot)

# 使用 kmeans 策略和 onehot 编码
est_kmeans_onehot = KBinsDiscretizer(n_bins=3, encode='onehot', strategy='kmeans')
Xt_kmeans_onehot = est_kmeans_onehot.fit_transform(X)
print("KMeans strategy with onehot encoding:\n", Xt_kmeans_onehot.toarray())
```
output:
```text
Quantile strategy with ordinal encoding:
 [[0.]
 [0.]
 [0.]
 [1.]
 [1.]
 [1.]
 [2.]
 [2.]
 [2.]
 [2.]]
Uniform strategy with onehot-dense encoding:
 [[1. 0. 0.]
 [1. 0. 0.]
 [1. 0. 0.]
 [0. 1. 0.]
 [0. 1. 0.]
 [0. 1. 0.]
 [0. 0. 1.]
 [0. 0. 1.]
 [0. 0. 1.]
 [0. 0. 1.]]
KMeans strategy with onehot encoding:
 [[1. 0. 0.]
 [1. 0. 0.]
 [1. 0. 0.]
 [1. 0. 0.]
 [0. 1. 0.]
 [0. 1. 0.]
 [0. 1. 0.]
 [0. 0. 1.]
 [0. 0. 1.]
 [0. 0. 1.]]
```

## 2、数值型特征的离散化
特征提取是机器学习预处理过程中的一个重要步骤，尤其是将数值型特征离散化，可以帮助改善模型的性能，特别是对于决策树模型。

离散化（也称为分箱或分段）是将连续特征的值范围划分为一系列区间，并将这些区间转换为离散的值。
离散化有多种方法，包括等宽分箱、等频分箱、基于聚类的分箱等。

在Python中，使用pandas和scikit-learn库可以方便地进行特征的离散化处理。

```text
import pandas as pd
from sklearn.preprocessing import KBinsDiscretizer

# 创建示例数据
data = pd.DataFrame({
    'feature': [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
})

# 等宽分箱
# 等宽分箱将特征的值域分成具有相同宽度的区间。可以使用 pandas的cut函数实现
data['feature_eq_width'] = pd.cut(data['feature'], bins=3, labels=False)

# 等频分箱
# 等频分箱将特征的值域分成具有相同数据点数量的区间。可以使用pandas的qcut函数实现
data['feature_eq_freq'] = pd.qcut(data['feature'], q=3, labels=False)

# 基于聚类的分箱
# 基于聚类的分箱是根据特征值的聚类结果来进行分箱的。
X = data[['feature']].values
est = KBinsDiscretizer(n_bins=3, encode='ordinal', strategy='kmeans')
data['feature_kmeans'] = est.fit_transform(X)
print(data)
```
output:
```text
   feature  feature_eq_width  feature_eq_freq  feature_kmeans
0        1                 0                0             0.0
1        2                 0                0             0.0
2        3                 0                0             0.0
3        4                 0                0             0.0
4        5                 1                1             1.0
5        6                 1                1             1.0
6        7                 1                1             1.0
7        8                 2                2             2.0
8        9                 2                2             2.0
9       10                 2                2             2.0
```

## 3、标准化 (Standardization)
标准化是将所有特征缩放到相同的尺度，使它们的均值为0，方差为1。

这对于许多机器学习算法是非常重要的，尤其是那些基于距离计算的算法。
不过，对于决策树来说，由于其模型结构的特点，标准化不是必需的。

可以使用sklearn.preprocessing.StandardScaler来实现标准化，常用参数如下，

| 参数        | 详细说明                                                                                                 |
|-----------|------------------------------------------------------------------------------------------------------|
| copy      | 类型: bool, 默认值: True, 如果为 True，则会对数据进行复制，然后进行标准化处理。如果为 False，则直接在原数据上进行操作，这样可以节省内存空间，但原数据会被改变。        |
| with_mean | 类型: bool, 默认值: True, 如果为 True，则会在标准化过程中中心化数据，即减去均值使数据的均值为0。对于稀疏矩阵，中心化会改变矩阵的稀疏结构，因此可能需要将此参数设置为 False。 |
| with_std  | 类型: bool, 默认值: True, 如果为 True，则会在标准化过程中缩放数据，即除以数据的标准差使得数据的标准差为1。这样做可以保证所有特征的尺度一致。                    |

使用代码，

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score

# 加载数据集
data = load_iris()
X = data.data
y = data.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建 StandardScaler 对象
# 使用 copy=True 以确保原始数据不被修改
# 设置 with_mean=True 和 with_std=True 来启用数据的中心化和缩放
scaler = StandardScaler(copy=True, with_mean=True, with_std=True)
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 训练决策树分类器
clf = DecisionTreeClassifier(random_state=42)
clf.fit(X_train_scaled, y_train)

# 预测测试集
y_pred = clf.predict(X_test_scaled)

# 评估模型性能
accuracy = accuracy_score(y_test, y_pred)
print(f"模型准确率: {accuracy:.2f}")
```
output:
```text
模型准确率: 1.00
```

## 4、归一化 (Normalization)
归一化是将特征缩放到一个指定的最小和最大值（通常是0到1）之间，以便在相同的尺度上比较不同的特征。

与标准化一样，归一化对于决策树模型来说不是必需的，但在某些情况下对于其他模型可能会有帮助。

可以使用sklearn.preprocessing.MinMaxScaler来实现归一化，常用参数如下，

| 参数名           | 描述                                                                                           |
|---------------|----------------------------------------------------------------------------------------------|
| feature_range | 类型: 元组 (min, max)，默认值为 (0, 1)。指定缩放后数据的范围，默认情况下，特征会被缩放到 0 和 1 之间。可以修改此参数以缩放到不同的范围。            |
| copy          | 类型: 布尔值，True 或 False，默认为 True。如果为 True，则会创建原始数据的副本然后进行缩放。如果为 False，则直接在原始数据上进行缩放。            |
| clip          | 类型: 布尔值，True 或 False，默认为 False。如果为 True，则将transform 方法应用到数据时，缩放后的数据会被裁剪到feature_range指定的范围内。 |

使用代码，

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import accuracy_score

# 加载iris数据集
iris = load_iris()
X, y = iris.data, iris.target

# 将数据集分为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 使用MinMaxScaler进行数据预处理
scaler = MinMaxScaler(feature_range=(0, 1))
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 创建决策树模型
clf = DecisionTreeClassifier(random_state=42)

# 使用缩放后的训练数据训练模型
clf.fit(X_train_scaled, y_train)

# 使用缩放后的测试数据评估模型
y_pred = clf.predict(X_test_scaled)

# 打印模型准确率
print("Accuracy:", accuracy_score(y_test, y_pred))
```
output:
```text
Accuracy: 1.0
```