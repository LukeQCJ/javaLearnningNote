# Python 机器学习 集成学习 GBDT

梯度提升决策树（Gradient Boosted Decision Trees，GBDT）是一种流行的集成学习方法，用于回归和分类问题。

**GBDT通过顺序地添加决策树，每一棵树都尝试纠正前一棵树的错误，从而提高模型的预测准确率。**
此方法的【核心】在于利用【梯度下降算法】最小化【损失函数】。

## 1、理解梯度提升决策树（Gradient Boosted Decision Trees，GBDT）
梯度提升决策树（Gradient Boosting Decision Trees，GBDT）是一种流行的集成学习方法，它结合了多个决策树模型来提高预测的准确性。
GBDT 属于 Boosting 类别，主要思想是每一步建立一个决策树来修正前一步的错误，通过迭代方式逐渐减少模型的偏差，从而提升模型整体的预测性能。

梯度提升决策树（GBDT）是一种高效且灵活的机器学习算法，它开始于一个基准预测器，通常是一个简单的决策树，用于对训练数据进行初步预测。
这个基准可以是回归问题的数据平均值或分类问题的最频繁类。
随后，GBDT通过迭代地添加新的决策树来逐步改善模型，每棵新树都专注于修正之前树所犯的预测误差，具体做法是在训练数据上拟合前一步所有树的预测残差。
在这个过程中，GBDT采用【梯度下降算法】来寻找每个决策树的最优分裂路径，目的是最小化整体模型的损失函数。

GBDT还引入了学习率参数来调节每棵树对最终模型的贡献程度，较小的学习率意味着需要更多的树来训练模型，以期获得更好的性能。
此外，模型中树的数量是影响模型复杂度和拟合能力的另一个关键因素，尽管增加树的数量可以提升模型性能，但过多的树可能会导致过拟合。

GBDT的优点在于其出色的性能和适用性，能够自动处理缺失值，并进行特征选择，有效地处理高维数据。
它既可以用于解决分类问题，也适用于回归及其他类型的预测任务。
然而，GBDT的主要挑战在于其需要调整的多个参数，如学习率和树的数量，这可能会使得模型训练变得复杂。
此外，GBDT在处理大规模数据集时可能会面临较长的训练时间，以及当数据噪声较大或者树的数量过多时，过拟合的风险。

## 2、GBDT的使用场景
GBDT的应用场景十分广泛：
在分类问题中，如金融欺诈检测、客户流失预测、疾病诊断等领域有着广泛的应用；
在回归问题中，例如房价预测、股票价格预测、销售额预测等方面也有很好的效果；
此外，GBDT也常用于搜索引擎结果的排序问题和作为特征转换工具，以及在一定程度上用于异常检测。

GBDT 被广泛应用于各种机器学习任务中，包括但不限于搜索排名、推荐系统、点击率预测等领域。
一些著名的GBDT实现包括XGBoost、LightGBM和CatBoost，它们在处理大规模数据时都有出色的表现，
并且提供了更高效的算法和更丰富的功能来满足不同的需求。

## 3、GBDT的使用
GBDT通过迭代地训练决策树来最小化损失函数，每棵树学习的是前一棵树预测的残差。这种方法可以有效地提高模型的准确性。

可以使用scikit-learn库的 GradientBoostingClassifier 和 GradientBoostingRegressor 类来实现GBDT模型。

### 1）GradientBoostingClassifier

GradientBoostingClassifier是Sklearn库中一个实现了梯度提升决策树算法的强大分类器。
它利用梯度提升框架通过顺序地添加【弱预测模型（通常是决策树）】，来最小化损失函数，有效地提高模型的预测准确性。常用参数如下，

| 参数                 | 描述                                                       |
|--------------------|----------------------------------------------------------|
| loss	              | 损失函数类型。默认为'deviance'，代表对数似然损失。'exponential'用于AdaBoost算法。 |
| learning_rate	     | 学习率。较小的值意味着需要更多的树来维持训练误差。                                |
| n_estimators	      | 提升阶段的数量，即最终模型中树的数量。增加此数值通常会提高模型的复杂度。                     |
| max_depth	         | 单个树的最大深度。用于控制树的复杂性，防止过拟合。                                |
| min_samples_split	 | 节点分裂所需的最小样本数。较大的值可防止在树的顶部创建过多的节点。                        |
| min_samples_leaf	  | 叶节点所需的最小样本数。用于对树进行平滑处理，防止过拟合。                            |
| max_features	      | 寻找最佳分割时要考虑的特征数量。可以帮助改善模型的性能并减少训练时间。                      |
| subsample	         | 用于拟合单个基学习器的样本比例。小于1.0会导致随机梯度提升，有助于减少方差。                  |
| random_state       | 控制随机种子。用于重复模型结果的一致性。                                     |
| max_leaf_nodes	    | 每个树的最大叶节点数。以max_leaf_nodes定义的方式限制树的生长可以更好地控制模型的复杂性。      |
| warm_start	        | 设置为True时，允许增量训练。即使用更多的估计器增量地增强现有模型。                      |

使用代码，
```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.metrics import accuracy_score

# 加载鸢尾花数据集
iris = load_iris()
X = iris.data
y = iris.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 创建GradientBoostingClassifier模型实例
# 这里我们指定了一些常用参数，比如n_estimators、learning_rate和max_depth
gbc = GradientBoostingClassifier(n_estimators=100, learning_rate=1.0, max_depth=1, random_state=42)

# 训练模型
gbc.fit(X_train, y_train)

# 使用训练好的模型进行预测
y_pred = gbc.predict(X_test)

# 计算并打印模型的准确率
accuracy = accuracy_score(y_test, y_pred)
print(f"Model accuracy: {accuracy:.4f}")
```
output:
```text
Model accuracy: 0.9556
```

## 2）GradientBoostingRegressor

GradientBoostingRegressor是一种集成学习算法，属于Boosting家族，用于回归问题。
它通过顺序地添加预测弱回归模型（通常是决策树），以尝试纠正前一个模型的残差，从而提高整体模型的准确性。常用参数如下，

| 参数	                | 描述                                                              |
|--------------------|-----------------------------------------------------------------|
| loss	              | 优化的损失函数。默认是'ls'（最小二乘法）。其他选项包括'lad'（最小绝对偏差），'huber'，和'quantile'。 |
| learning_rate	     | 每个树的贡献率，也称为步长。较小的值意味着需要更多的树来维持模型性能。                             |
| n_estimators	      | 执行的提升阶段数量，即构建的树的数量。增加此值会使模型更复杂，并可能导致过拟合。                        |
| max_depth	         | 单个回归估计器的最大深度。用于控制过拟合。                                           |
| min_samples_split	 | 拆分内部节点所需的最小样本数。                                                 |
| min_samples_leaf	  | 叶子节点所需的最小样本数。有助于提供模型的平滑效果。                                      |
| max_features	      | 查找最佳分割时要考虑的特征数量。可以提高效率并减少过拟合。                                   |
| subsample	         | 用于拟合单个基础学习器的样本比例。小于1.0将导致随机梯度提升。                                |
| random_state	      | 控制随机种子。当subsample< 1.0时，用于产生随机样本。                               |
| max_leaf_nodes	    | 在每棵树中的最大叶子节点数。用于控制树的形状。                                         |
| warm_start	        | 若设置为True，则重用上一次调用的解决方案来拟合并添加更多的估计器到集合中，否则，拟合一个全新的模型。            |

使用代码，
```text
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.metrics import mean_squared_error

# 加载糖尿病数据集
diabetes = load_diabetes()
X = diabetes.data
y = diabetes.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建GradientBoostingRegressor模型实例
# 这里我们调整一些参数作为示例
gb_regressor = GradientBoostingRegressor(n_estimators=100,
                                         learning_rate=0.1,
                                         max_depth=3,
                                         min_samples_split=2,
                                         min_samples_leaf=1,
                                         max_features=None,
                                         random_state=42)

# 训练模型
gb_regressor.fit(X_train, y_train)

# 预测测试集
y_pred = gb_regressor.predict(X_test)

# 计算模型的均方误差（MSE）
mse = mean_squared_error(y_test, y_pred)
print(f'Mean Squared Error: {mse}')
```
output:
```text
Mean Squared Error: 2906.4600940881232
```