# Python 机器学习 集成学习 随机森林

集成学习是机器学习中一种强大的方法，它通过组合多个模型来提高预测的准确性和鲁棒性。

随机森林是集成学习中一个非常流行的算法，它属于bagging类型的算法，主要用于分类和回归任务。
【随机森林】通过构建多个【决策树】并将它们的预测结果进行【汇总】来工作。
该方法既可以减少过拟合，也可以提高模型的准确性。

## 1、随机森林
随机森林是一种流行且强大的集成学习算法，通过结合多个决策树的预测来提升模型的性能。
它基于【Bagging（Bootstrap Aggregating，引导聚合）】和【特征随机选择原则】，
可以通过构建【多个决策树】并将它们的预测结果进行【聚合】来减少模型的方差，从而防止过拟合并提高准确率。
在构建每棵树时，随机森林从原始数据集中进行有放回的随机抽样以形成训练子集，并在分裂节点时随机选择一部分特征，这增加了模型的多样性和健壮性。

对于分类问题，随机森林通过投票机制确定最终预测；
对于回归问题，则取所有树的预测平均值。

可以使用scikit-learn库中的RandomForestClassifier或RandomForestRegressor来实现。

### 1）RandomForestClassifier

RandomForestClassifier是实现随机森林算法的一个类，专门用于解决分类问题。

这个分类器集成了多个决策树分类器的预测，通过【投票机制】来提高整体的分类准确率。

常用参数如下，

| 参数                     | 描述                                                            |
|------------------------|---------------------------------------------------------------|
| n_estimators	          | 森林中树木的数量，默认为100。                                              |
| criterion	             | 分裂质量的衡量函数，可为gini或entropy，默认为gini。                             |
| max_depth	             | 树的最大深度。如果为None，则节点扩展直到所有叶子都纯净或包含的样本数小于min_samples_split指定的数量。 |
| min_samples_split	     | 分裂内部节点所需的最小样本数，可以是整数或浮点数（百分比），默认为2。                           |
| min_samples_leaf	      | 叶节点需要的最小样本数量，可以是整数或浮点数（百分比），默认为1。                             |
| max_features           | 寻找最佳分裂时考虑的最大特征数，可以是整数、浮点数、auto、sqrt或log2，默认为auto。             |
| bootstrap	             | 是否在构建树时使用bootstrap样本，默认为True。                                 |
| oob_score	             | 是否使用袋外样本来估计准确率，默认为False。                                      |
| n_jobs	                | 拟合和预测时并行运行的作业数，默认为None。使用-1代表使用所有可用的CPU核心。                    |
| random_state	          | 控制随机数生成器的种子，用于结果的可重现性。                                        |
| max_leaf_nodes	        | 以最佳优先方式增长树时最大的叶子节点数。如果为None，则不限制叶子节点数。                        |
| min_impurity_decrease	 | 如果节点的分裂导致的不纯度减少（加权平均）大于或等于这个值，则会进行分裂。                         |

使用代码，
```text
# 导入必要的库
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

# 加载数据集
iris = load_iris()
X = iris.data
y = iris.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 创建随机森林分类器实例
# 在这里，我们指定一些参数作为示例
rf_classifier = RandomForestClassifier(n_estimators=100,  # 树的数量
                                       criterion='gini',  # 分裂质量的衡量标准
                                       max_depth=5,  # 树的最大深度
                                       min_samples_split=2,  # 分裂内部节点所需的最小样本数
                                       min_samples_leaf=1,  # 叶节点需要的最小样本数量
                                       max_features='sqrt',  # 寻找最佳分裂时考虑的最大特征数
                                       bootstrap=True,  # 是否在构建树时使用bootstrap样本
                                       random_state=42)  # 控制随机数生成器的种子

# 训练模型
rf_classifier.fit(X_train, y_train)

# 预测测试集
y_pred = rf_classifier.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')
```
output:
```text
Accuracy: 1.0
```

### 2）RandomForestRegressor

RandomForestRegressor是用于回归任务的随机森林实现。这个类提供了一个易于使用的接口，用于训练随机森林模型预测连续值。常用参数如下，

| 参数                     | 描述                                                                      |
|------------------------|-------------------------------------------------------------------------|
| n_estimators	          | 树的数量。默认值为100。                                                           |
| criterion	             | 用于测量分裂质量的函数。对于回归任务，常用的有“mse”（均方误差）和“mae”（平均绝对误差）。默认是“mse”。              |
| max_depth	             | 树的最大深度。如果为None，则节点扩展直到所有叶子都是纯净的，或者直到所有叶子包含的样本数小于min_samples_split。      |
| min_samples_split	     | 分裂内部节点所需的最小样本数。如果为整数，则为最小样本数；如果为浮点数，则为样本总数的百分比。默认值为2。                   |
| min_samples_leaf	      | 叶节点上所需的最小样本数。这个参数限制了树的生长，可以防止过拟合。如果为整数，则为最小样本数；如果为浮点数，则为样本总数的百分比。默认值为1。 |
| max_features	          | 寻找最佳分裂时考虑的最大特征数。可以是整数、浮点数、字符串（“auto”，“sqrt”，“log2”）或None。默认是“auto”。     |
| bootstrap	             | 是否在构建树时使用bootstrap样本。默认为True。                                           |
| oob_score	             | 是否使用袋外样本来估计R^2分数。只有当bootstrap=True时可用。默认为False。                         |
| n_jobs	                | 拟合和预测的过程中并行运行的作业数。-1意味着使用所有处理器。默认值为None。                                |
| random_state	          | 控制随机数生成器的种子。可以是整数、RandomState实例或None。默认为None。                           |
| max_leaf_nodes	        | 以最佳优先方式生成树的最大叶节点数。如果为None，则叶节点数量不受限制。                                   |
| min_impurity_decrease	 | 如果节点的分裂导致的不纯度减少大于这个值，则节点将被分裂。默认为0。                                      |

使用代码，
```text
# 导入必要的库
from sklearn.datasets import load_boston
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
import numpy as np

# 加载波士顿房价数据集
boston = load_boston()
X = boston.data
y = boston.target

# 将数据分割为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建随机森林回归模型实例
# 在这里，我们指定了一些参数作为示例
random_forest_regressor = RandomForestRegressor(n_estimators=100,  # 树的数量
                                                max_depth=10,  # 树的最大深度
                                                min_samples_split=2,  # 分裂内部节点所需的最小样本数
                                                min_samples_leaf=1,  # 叶节点上所需的最小样本数
                                                max_features='sqrt',  # 寻找最佳分裂时考虑的最大特征数
                                                bootstrap=True,  # 是否在构建树时使用bootstrap样本
                                                random_state=42)  # 控制随机数生成器的种子

# 训练模型
random_forest_regressor.fit(X_train, y_train)

# 使用模型进行预测
y_pred = random_forest_regressor.predict(X_test)

# 计算测试集上的均方误差
mse = mean_squared_error(y_test, y_pred)
print(f'Test Mean Squared Error: {mse}')

# 打印特征重要性
print("Feature importances:", np.round(random_forest_regressor.feature_importances_, 3))

# 根据需要，可以调整这些参数以优化模型的性能
```

## 2、随机森林与Bagging区别
随机森林和Bagging是两种集成学习算法，它们通过组合多个模型的预测结果来提升整体预测的准确性和稳定性。

Bagging是一种通用方法，通过对原始数据进行有放回的抽样来创建多个子训练集，进而训练多个基学习器，最后通过平均或投票机制聚合预测结果。

随机森林则是Bagging的一种特殊形式，专门应用于决策树，不仅在训练过程中采用自助采样来增加数据的多样性，
还引入了【随机特征选择机制】，即在每个决策树的分裂点上，从一个随机选择的特征子集中选择最佳分裂特征。
这种对特征随机选择的方法进一步增强了模型间的多样性，有助于降低预测的【方差】，
使随机森林在众多场景下相比单纯的Bagging方法（尤其是使用决策树作为基模型时）具有更好的性能。

简而言之，虽然随机森林和Bagging在核心上共享集成多个模型以提高性能的理念，随机森林通过引入随机特征选择为决策树集成提供了额外的多样性和准确性。

```text
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor, BaggingRegressor
from sklearn.tree import DecisionTreeRegressor
from sklearn.metrics import mean_squared_error

# 加载糖尿病数据集
diabetes_data = load_diabetes()
X = diabetes_data.data
y = diabetes_data.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建随机森林回归器
rf_regressor = RandomForestRegressor(n_estimators=100, random_state=42)

# 创建Bagging回归器，使用决策树作为基学习器
bagging_regressor = BaggingRegressor(base_estimator=DecisionTreeRegressor(),
                                     n_estimators=100, random_state=42)

# 训练随机森林模型
rf_regressor.fit(X_train, y_train)

# 训练Bagging模型
bagging_regressor.fit(X_train, y_train)

# 随机森林预测
rf_predictions = rf_regressor.predict(X_test)

# Bagging预测
bagging_predictions = bagging_regressor.predict(X_test)

# 计算均方误差
rf_mse = mean_squared_error(y_test, rf_predictions)
bagging_mse = mean_squared_error(y_test, bagging_predictions)

print(f"Random Forest MSE: {rf_mse}")
print(f"Bagging MSE: {bagging_mse}")
```
output:
```text
Random Forest MSE: 2945.2906089887642
Bagging MSE: 2970.6593977528096
```

## 3、使用场景
随机森林是一种强大的集成学习算法，因其高准确性、出色的抗过拟合能力和易用性，在多种机器学习任务中得到广泛应用。
它不仅能够高效处理分类和回归问题，提高预测的精度和稳定性，
还能在特征选择中发挥重要作用，帮助识别影响预测结果的关键因素，特别适用于高维数据分析。

此外，随机森林能够处理缺失数据，利用数据的内在结构估计缺失值，使其能够应对不完整的数据集。
在【异常检测】、【推荐系统】以及【图像分类与识别】等领域也展现出其强大的应用能力。
这些特性使得随机森林成为数据科学家和机器学习工程师在解决各种数据科学问题时的首选工具之一，
无论是在医疗诊断、房地产市场分析、信用卡欺诈检测还是网络安全等领域，随机森林都能提供高效且可靠的解决方案。

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

# 加载Iris数据集
iris = load_iris()
X = iris.data
y = iris.target

# 将数据集分割为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 创建随机森林分类器实例
rf_classifier = RandomForestClassifier(n_estimators=100, random_state=42)

# 训练模型
rf_classifier.fit(X_train, y_train)

# 预测测试集结果
y_pred = rf_classifier.predict(X_test)

# 计算模型的准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')
```
output:
```text
Accuracy: 1.0
```