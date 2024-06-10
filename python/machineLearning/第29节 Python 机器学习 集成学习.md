# Python 机器学习 集成学习

集成学习是一种机器学习方法，它通过构建并【组合】多个【学习器】来完成学习任务，旨在【提高预测的准确性】。

集成学习背后的基本思想是**多个模型的组合通常会比单个模型表现得更好**。这些模型可以是同种类型的，也可以是不同类型的。
集成学习通常能在各种机器学习任务中取得很好的效果，尤其是复杂的问题和大规模数据集。

## 1、Bagging
集成学习是一种机器学习范式，通过构建并组合多个学习器来提高预测性能。

Bagging（Bootstrap Aggregating，引导聚合）是集成学习中的一种常见技术，它通过组合多个模型减少方差，提高稳定性和准确性。
弱学习器通常是在原始数据集的不同子集上训练得到的，子集是通过有放回随机抽样（即自助采样法）从原始数据集中生成的。

Bagging 算法又称算法，是一种集成学习算法，通常用于降低噪声数据集中的方差。 
在 Bagging 方法中，用替换法来选择训练集中的随机数据样本，这意味着可多次选择单个数据点。 
在生成多个数据样本后，将单独训练这些弱模型，根据任务类型（如回归或分类），这些预测的平均值或多数值会产生更准确的估计值。

值得注意的是，随机森林算法被认为是 Bagging 算法的扩展，利用装袋和特征的随机性来创建一个不相关的决策树森林。

可以使用scikit-learn库中的BaggingRegressor或BaggingClassifier来实现Bagging方法。

### 1）BaggingRegressor

BaggingRegressor 是 scikit-learn 中的一个集成学习算法，用于提高回归模型的稳定性和准确性。
Bagging（Bootstrap Aggregating）通过在原始数据集上构建多个独立的预测器，然后将它们的预测结果进行平均或多数投票来提高模型的性能。
对于回归问题，通常是取预测值的平均作为最终预测结果。

常用参数如下，

| 参数                 | 描述                                                    |
|--------------------|-------------------------------------------------------|
| base_estimator     | 默认为None。基估计器，如果为None，则默认使用决策树。可以设置为任何支持样本加权的回归器。      |
| n_estimators       | 默认为10。基估计器的数量。增加数量通常会提高模型的稳定性和性能，但也会增加计算成本。           |
| max_samples        | 默认为1.0。用于训练每个基估计器的样本数量。可以是小于1的浮点数表示比例，或直接设置为整数表示具体数量。 |
| max_features       | 默认为1.0。用于训练每个基估计器的特征数量。可以是小于1的浮点数表示比例，或直接设置为整数表示具体数量。 |
| bootstrap          | 默认为True。是否对样本进行有放回抽样。设置为 False 时将执行无放回抽样。             |
| bootstrap_features | 默认为False。是否对特征进行抽样。                                   |
| oob_score          | 默认为False。是否使用袋外样本来评估模型的性能。仅当bootstrap=True时可用。        |
| n_jobs             | 默认为None。用于拟合和预测的并行作业数。设置为-1表示使用所有处理器。                 |
| random_state       | 默认为None。控制随机数生成器的种子。它确保了每次运行代码时基估计器的随机样本抽取是一致的。       |
| verbose            | 默认为0。控制构建基估计器时的详细程度，0表示不输出任何过程信息。                     |

使用代码，

```text
# 导入必要的库
from sklearn.model_selection import train_test_split
from sklearn.ensemble import BaggingRegressor
from sklearn.datasets import load_diabetes
from sklearn.metrics import mean_squared_error, r2_score

# 加载数据
diabetes = load_diabetes()
X, y = diabetes.data, diabetes.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建BaggingRegressor模型
bagging_regressor = BaggingRegressor(n_estimators=100, random_state=42)

# 训练模型
bagging_regressor.fit(X_train, y_train)

# 预测测试集
y_pred = bagging_regressor.predict(X_test)

# 计算并打印评估指标
mse = mean_squared_error(y_test, y_pred)
r2 = r2_score(y_test, y_pred)
print("Mean Squared Error:", mse)
print("R^2 Score:", r2)
```
output:
```text
Mean Squared Error: 2970.6593977528096
R^2 Score: 0.4393026967216356
```

### 2）BaggingClassifier

BaggingClassifier是scikit-learn中一个强大的集成学习算法，用于提高分类算法的稳定性和准确性，通过将多个模型组合在一起减少方差，避免过拟合。

Bagging（Bootstrap Aggregating）通过对原始数据集进行多次重采样来创建多个训练集，然后对每个训练集训练一个基模型。
最终的预测是通过对所有基模型的预测结果进行聚合（例如，通过投票或平均）来完成的。

常用参数，

| 参数                 | 描述                                                                  |
|--------------------|---------------------------------------------------------------------|
| base_estimator     | 默认None (DecisionTreeClassifier)。指定基础分类器的类型。任何适合于样本权重的分类器都可以作为基础分类器。 |
| n_estimators       | 默认10。指定基础分类器的数量，即集成中包含的分类器个数。                                       |
| max_samples        | 默认1.0。控制用于训练每个基础分类器的数据集的大小。float类型表示总样本数的比例；int类型表示具体样本数。           |
| max_features       | 默认1.0。控制用于训练每个基础分类器时使用的特征数量。float类型表示总特征数的比例；int类型表示具体特征数。          |
| bootstrap          | 默认True。表示是否使用样本的自助采样来构建每个基础分类器使用的数据集。                               |
| bootstrap_features | 默认False。表示是否对特征进行自助采样。                                              |
| oob_score          | 默认False。如果为True，则在训练结束后自动使用袋外样本来估计泛化准确度。                            |
| random_state       | None。控制随机化的种子数。对于重现结果很重要。                                           |
| n_jobs             | 默认None。指定并行运行的任务数。如果设置为-1，则使用所有可用的CPU核心。                            |
| warm_start         | 默认False。如果设置为True，则可以在已有的模型上继续训练新的模型，而不是从头开始。                       |

使用代码，
```text
# 导入必要的库
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.ensemble import BaggingClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score

# 加载数据集
iris = load_iris()
X, y = iris.data, iris.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 创建基础分类器的实例
base_estimator = DecisionTreeClassifier()

# 初始化BaggingClassifier
bagging_clf = BaggingClassifier(
    base_estimator=base_estimator,  # 基础分类器
    n_estimators=100,  # 分类器数量
    max_samples=0.8,  # 训练每个分类器使用的样本比例
    max_features=0.8,  # 训练每个分类器使用的特征比例
    bootstrap=True,  # 使用样本的自助采样
    bootstrap_features=False,  # 不对特征进行自助采样
    oob_score=True,  # 计算袋外分数来评估模型的泛化误差
    random_state=42,  # 随机种子
    n_jobs=-1  # 使用所有可用的CPU核心
)

# 训练模型
bagging_clf.fit(X_train, y_train)

# 预测测试集
y_pred = bagging_clf.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f"模型准确率: {accuracy * 100:.2f}%")

# 如果计算了袋外分数，可以这样获取
if bagging_clf.oob_score:
    print(f"袋外分数（OOB score）: {bagging_clf.oob_score_:.2f}")
```
output:
```text
模型准确率: 100.00%
袋外分数（OOB score）: 0.93
```

## 2、Boosting
Python的机器学习领域，集成学习是一种常用的策略，旨在通过结合多个学习器的预测结果来提高整体模型的性能。

Boosting（提升方法）是集成学习的一种方法，
其【核心思想】是按顺序训练一系列的弱学习器（即，比随机猜测略好的模型），每一个后续的学习器都尝试纠正前一个学习器的错误。

Boosting的目标是将这些弱学习器组合成一个强学习器。

### 1）AdaBoost (Adaptive Boosting)

AdaBoost（自适应增强）是Boosting方法中的一种，它通过增加之前被错误分类观测的权重，使新的分类器更加关注那些难以分类的观测。

使用scikit-learn中的 AdaBoostRegressor 或 AdaBoostClassifier 可以很容易地实现AdaBoost算法。

AdaBoostClassifier：
```text
from sklearn.ensemble import AdaBoostClassifier
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split

# 生成模拟数据
X, y = make_classification(n_samples=1000, n_features=20, n_informative=2, n_redundant=10, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 创建AdaBoost模型
ada_clf = AdaBoostClassifier(n_estimators=100, random_state=42)

# 训练模型
ada_clf.fit(X_train, y_train)

# 预测测试集
y_pred = ada_clf.predict(X_test)

# 评估模型
accuracy = ada_clf.score(X_test, y_test)
print(f'Accuracy: {accuracy}')
```
output:
```text
Accuracy: 0.8933333333333333
```

AdaBoostRegressor：
```text
from sklearn.datasets import make_regression
from sklearn.model_selection import train_test_split
from sklearn.ensemble import AdaBoostRegressor
from sklearn.metrics import mean_squared_error

# 生成模拟数据集
X, y = make_regression(n_samples=1000, n_features=20, n_informative=2, noise=0.1, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 初始化AdaBoost回归器
ada_reg = AdaBoostRegressor(n_estimators=100, learning_rate=1.0, loss='linear', random_state=42)

# 训练模型
ada_reg.fit(X_train, y_train)

# 预测测试集
y_pred = ada_reg.predict(X_test)

# 计算均方误差
mse = mean_squared_error(y_test, y_pred)
print(f"Mean Squared Error: {mse}")
```
output:
```text
Mean Squared Error: 76.85781669075568
```

### 2）Gradient Boosting

Gradient Boosting（梯度增强）是另一种Boosting方法，
它通过连续地添加新的模型，专注于减少前一个模型的残差（即，真实值与预测值之间的差异），来提高模型的性能。

scikit-learn提供了 GradientBoostingClassifier 和GradientBoostingRegressor 来实现Gradient Boosting算法。

**GradientBoostingClassifier：**
```text
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split

# 生成模拟数据
X, y = make_classification(n_samples=1000, n_features=20, n_informative=2, n_redundant=10, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)
# 创建Gradient Boosting模型
gb_clf = GradientBoostingClassifier(n_estimators=100, learning_rate=1.0, max_depth=1, random_state=42)

# 训练模型
gb_clf.fit(X_train, y_train)

# 预测测试集
y_pred = gb_clf.predict(X_test)

# 评估模型
accuracy = gb_clf.score(X_test, y_test)
print(f'Accuracy: {accuracy}')
```
output:
```text
Accuracy: 0.8766666666666667
```

**GradientBoostingRegressor：**
```text
from sklearn.model_selection import train_test_split
from sklearn.datasets import make_classification
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.metrics import mean_squared_error

# 生成模拟数据
X, y = make_classification(n_samples=1000, n_features=20, n_informative=2, n_redundant=10, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 初始化Gradient Boosting回归器
gbr = GradientBoostingRegressor(n_estimators=100, learning_rate=0.1, max_depth=3, random_state=42)

# 训练模型
gbr.fit(X_train, y_train)

# 预测测试集
y_pred = gbr.predict(X_test)

# 评估性能
mse = mean_squared_error(y_test, y_pred)
print(f"Mean Squared Error: {mse}")
```
output:
```text
Mean Squared Error: 0.07487600846506935
```
