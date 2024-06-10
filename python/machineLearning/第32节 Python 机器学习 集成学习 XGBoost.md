# Python 机器学习 集成学习 XGBoost算法

XGBoost（eXtreme Gradient Boosting）是一个高效且灵活的梯度增强库，它是决策树算法与梯度增强框架的结合，广泛应用于机器学习竞赛和工业实践中。
XGBoost提供了一个优化的分布式梯度提升算法，能够解决许多数据科学中的实际问题。

## 1、理解XGBoost算法
XGBoost（eXtreme Gradient Boosting）是一个高效的机器学习算法，广泛应用于分类、回归和排序问题中，
特别是在数据科学竞赛如Kaggle中非常受欢迎。
XGBoost是基于梯度提升（Gradient Boosting）算法的优化实现，能够自动利用CPU的多核心进行并行处理，
同时也支持分布式计算，可以快速精确地处理大规模数据。

XGBoost采用梯度提升框架，其工作原理是通过迭代地添加新模型来修正前一轮迭代的错误，每次迭代都旨在减少模型的损失函数。
它从用一个常数初始化模型开始，然后在每一轮迭代中，根据当前模型的预测值与实际值的差异（即梯度信息）来训练新的决策树，并将这些新树加入到现有模型中。
此外，它会计算每个训练样本的损失函数梯度和二阶导数，并使用这些梯度信息来构建决策树，每次分裂都会选择最优的分裂点。
这个过程包括正则化考量，并会继续迭代直至达到预设的迭代次数或模型性能不再显著提升。

为了加速计算和提升模型性能，XGBoost实施了多种优化技术。
它使用近似算法来快速选择分裂点，利用多线程并行计算以优化树的构建过程。
此外，它采用深度优先策略并在达到最大深度后剪枝，以避免过拟合。
XGBoost还能自动处理缺失值，免去用户进行额外的缺失值填充操作。
这些特点和优化技术使XGBoost成为一种在各种数据科学竞赛和实际应用中广受欢迎的强大机器学习工具。

## 2、scikit-learn
Python的机器学习生态系统中，scikit-learn 是一个广泛使用的库，提供了大量简单且高效的工具用于数据挖掘和数据分析。
虽然 scikit-learn 本身不直接提供XGBoost算法的实现，但XGBoost项目提供了一个与 scikit-learn 兼容的API。
也可以利用XGBoost的强大功能，同时享受到 scikit-learn 的易用性，包括与 scikit-learn 的众多工具和流水线（pipelines）的兼容性。

1）安装命令
```text
pip install scikit-learn

pip install xgboost
```

2）导入所需模块
```text
import xgboost as xgb
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
```

## 3、数据集
Iris 数据集是一个经典的数据集，包含了150个样本，每个样本有4个特征（花萼长度、花萼宽度、花瓣长度、花瓣宽度），
用于预测花的种类（Setosa、Versicolour、Virginica）。
```text
from sklearn.datasets import load_iris

# 加载Iris数据集
iris = load_iris()
X = iris.data
y = iris.target

print(iris.feature_names)  # 输出特征名
print(iris.target_names)  # 输出目标名
print(X[:5])  # 输出前5个样本的特征
print(y[:5])  # 输出前5个样本的标签
```
output:
```text
['sepal length (cm)', 'sepal width (cm)', 'petal length (cm)', 'petal width (cm)']
['setosa' 'versicolor' 'virginica']
[[5.1 3.5 1.4 0.2]
 [4.9 3.  1.4 0.2]
 [4.7 3.2 1.3 0.2]
 [4.6 3.1 1.5 0.2]
 [5.  3.6 1.4 0.2]]
[0 0 0 0 0]
```

## 4、训练XGBoost模型
要使用XGBoost算法训练模型，可以加载的Iris数据集。
使用XGBoost的Python包来训练一个分类模型，以预测Iris数据集中花的种类。

XGBClassifier的常用参数如下，

| 参数                                 | 描述                                                 |
|------------------------------------|----------------------------------------------------|
| n_estimators                       | 弱学习器的数量，默认值为100。                                   |
| learning_rate                      | 学习率，用于缩减每个弱学习器的贡献，防止过拟合，默认值为0.3。                   |
| max_depth                          |
| 树的最大深度，增加这个值会使模型更复杂，可能导致过拟合，默认值为6。 |
| min_child_weight                   | 决定最小叶子节点样本权重和，默认值为1。                               |
| gamma                              | 树的叶子节点做进一步分裂所需的最小损失函数下降值，默认值为0。                    |
| subsample                          | 训练模型的子样本占整个样本集合的比例，默认值为1。                          |
| colsample_bytree                   | 在建立树时对特征进行采样的比例，默认值为1。                             |
| objective                          | 学习任务和相应的学习目标，二分类问题常用'binary:logistic'。             |
| eval_metric                        | 模型评估标准，如'logloss' 或'auc' 等。                        |
| use_label_encoder                  | 控制是否使用标签编码器，默认值取决于XGBoost的版本，从1.3.0版本开始建议设置为False。 |
| random_state                       | 随机数种子，用于产生可重复的结果。                                  |

1）使用XGBoost进行分类
```text
import xgboost as xgb
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 加载数据集
data = load_iris()
X = data.data
y = data.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 训练XGBoost模型
model = xgb.XGBClassifier(use_label_encoder=False, eval_metric='logloss')
model.fit(X_train, y_train)

# 预测测试集
y_pred = model.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f"Accuracy: {accuracy}")
```
output:
```text
Accuracy: 1.0
```

2）使用XGBoost进行回归
```text
import xgboost as xgb
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

# 加载数据集
data = load_iris()
X = data.data
y = data.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 训练XGBoost模型
model = xgb.XGBRegressor(objective='reg:squarederror')
model.fit(X_train, y_train)

# 预测测试集
y_pred = model.predict(X_test)

# 计算均方误差
mse = mean_squared_error(y_test, y_pred)
print(f"Mean Squared Error: {mse}")
```
output:
```text
Mean Squared Error: 0.001874504788475783
```
