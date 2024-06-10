# Python 机器学习 回归决策树

决策树是一种常用于分类和回归的机器学习算法。
在回归问题中，决策树用于预测连续值的输出，如价格或温度等。

回归决策树是一种用于回归问题的决策树模型。与分类决策树（输出离散标签）不同，回归决策树的目标是预测一个连续的数值。
回归决策树的工作原理与分类树相似，但在决定分支如何划分时，它们寻求最小化的是目标变量的方差或均方误差（MSE），而不是不纯度（如基尼不纯度或熵）。

## 1、理解回归决策树
回归决策树是决策树算法在回归问题上的应用，用于预测连续值而不是类别标签。
与分类决策树类似，回归决策树通过递归地将数据集分割成越来越小的子集来构建树结构，
但不同的是，它在每个叶节点上预测的是一个数值（目标变量的平均值或中位数等）而非类别。
通过选择最佳特征和切分点来递归分割数据集，直至满足某个停止条件，如达到树的最大深度或节点中的最小样本数。
在每个叶节点上，它会预测一个数值，通常是该节点所有样本目标值的平均值。
构建过程中会考虑减少目标变量（如房价）的总方差，而选择特征和切分点。
为避免过拟合，可以采用剪枝技术，包括预剪枝和后剪枝。

## 2、数据集
sklearn（Scikit-learn）库中，糖尿病数据集（Diabetes Dataset）是一个用于回归分析的标准数据集，包含了442个病人的10个基线变量，
目标变量是一年后疾病进展的量化度量。
这些基线变量包括年龄、性别、体质指数、平均血压和六个血清测量值。
Python 的 scikit-learn 库中，可以直接加载并使用这个数据集。

糖尿病数据集的特征：

| 特征                    | 描述               |
|-----------------------|------------------|
| 年龄 (age)              | 年龄               |
| 性别 (sex)              | 性别               |
| 体质指数 (bmi)            | 体质指数             |
| 平均血压 (bp)             | 平均血压             |
| S1 (总血清胆固醇)           | 总血清胆固醇           |
| S2 (低密度脂蛋白胆固醇)        | 低密度脂蛋白胆固醇        |
| S3 (高密度脂蛋白胆固醇)        | 高密度脂蛋白胆固醇        |
| S4 (总胆固醇 / 高密度脂蛋白胆固醇) | 总胆固醇 / 高密度脂蛋白胆固醇 |
| S5 (血清甲状腺素水平的对数)      | 血清甲状腺素水平的对数      |
| S6 (一年后的血糖水平)         | 一年后的血糖水平         |

使用代码，
```text
from sklearn.datasets import load_diabetes

# 加载糖尿病数据集
diabetes = load_diabetes()

# 获取特征集和响应变量
X, y = diabetes.data, diabetes.target

# 查看数据维度
print("特征集维度:", X.shape)
print("响应变量维度:", y.shape)

# 打印前5个样本的特征和目标值，以便观察数据
print("前5个样本的特征:\n", X[:5])
print("前5个样本的目标值:", y[:5])
```

## 3、训练回归决策树模型
Python中训练一个回归决策树模型，通常会使用scikit-learn库，一个广泛使用的机器学习库。

在实际应用中，可能需要调整模型的参数，如决策树的深度、最小划分样本数等，以获得更好的性能。

此外，回归决策树容易过拟合，因此可能需要使用如随机森林等更复杂的模型来提高预测的准确性。

scikit-learn 中的 DecisionTreeRegressor() 类用于回归决策树模型，常用参数如下，

| 参数                    | 描述                                                                         |
|-----------------------|----------------------------------------------------------------------------|
| criterion             | 衡量分裂质量的函数。常用的有“mse”（均方误差），“friedman_mse”（费里德曼均方误差）和“mae”（平均绝对误差）。默认是“mse”。 |
| splitter              | 选择分裂节点的策略。“best”表示选择最佳分裂，“random”表示选择最佳随机分裂。默认为“best”。                     |
| max_depth             | 树的最大深度。如果为None，则节点将扩展直到所有叶子都是纯净的，或直到所有叶子包含的样本数小于min_samples_split指定的数量。    |
| min_samples_split     | 分裂内部节点所需的最小样本数。可以是整数或浮点数（表示百分比）。默认值为2。                                     |
| min_samples_leaf      | 叶节点上所需的最小样本数。可以是整数或浮点数（表示百分比）。默认值为1。                                       |
| max_features          | 寻找最佳分裂时考虑的最大特征数。可以是整数、浮点数、字符串（“auto”，“sqrt”，“log2”）或None。                  |
| random_state          | 控制随机数生成器的种子。可以是整数、RandomState实例或None。                                      |
| max_leaf_nodes        | 以最佳优先方式使用max_leaf_nodes生成树。如果为None，则生成的树不受限制。                              |
| min_impurity_decrease | 如果节点的分裂导致不纯度的减少大于或等于这个值，则节点将被分裂。用于控制树的增长和防止过拟合。                            |

使用代码，

```text
# 导入必要的库
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeRegressor
from sklearn.metrics import mean_squared_error

# 加载糖尿病数据集
diabetes_data = load_diabetes()
X = diabetes_data.data
y = diabetes_data.target

# 将数据集分割为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建回归决策树模型
# 这里我们指定一些参数作为示例，你可以根据需要调整这些参数
regressor = DecisionTreeRegressor(criterion='friedman_mse',
                                  max_depth=5,
                                  min_samples_split=20,
                                  min_samples_leaf=10,
                                  max_features='sqrt',
                                  random_state=42)

# 训练模型
regressor.fit(X_train, y_train)

# 预测测试集结果
y_pred = regressor.predict(X_test)

# 计算并打印均方误差
mse = mean_squared_error(y_test, y_pred)
print(f'Mean Squared Error: {mse}')
```
output:
```text
Mean Squared Error: 3782.339191808713
```

## 4、可视化决策树
可视化决策树在机器学习和数据科学中有多个重要作用，使其成为模型解释和分析的有力工具。
可视化决策树是一个很好的方法来理解和解释决策树模型的决策过程。
决策树的可视化不仅增强了模型的可解释性，还为模型的评估、优化和使用提供了重要的视角。
这使得决策树成为一个非常实用的工具，尤其是在需要清晰解释模型决策过程的应用中。

```text
# 导入必要的库
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeRegressor, plot_tree
import matplotlib.pyplot as plt

# 加载糖尿病数据集
diabetes_data = load_diabetes()
X = diabetes_data.data
y = diabetes_data.target

# 将数据集分割为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建回归决策树模型
# 这里我们指定一些参数作为示例，你可以根据需要调整这些参数
regressor = DecisionTreeRegressor(criterion='friedman_mse',
                                  max_depth=5,
                                  min_samples_split=20,
                                  min_samples_leaf=10,
                                  max_features='sqrt',
                                  random_state=42)

# 训练模型
regressor.fit(X_train, y_train)

# 预测测试集结果
y_pred = regressor.predict(X_test)

# 可视化决策树
plt.figure(figsize=(20, 10))
plot_tree(regressor, filled=True, feature_names=diabetes_data.feature_names)
plt.draw()
plt.show()
```