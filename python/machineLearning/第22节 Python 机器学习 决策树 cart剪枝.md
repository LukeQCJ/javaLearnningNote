# Python 机器学习 决策树 cart剪枝

Python 机器学习中，CART（Classification And Regression Trees）算法用于构建决策树，用于分类和回归任务。

剪枝（Pruning）是一种避免决策树过拟合的技术，通过减少树的大小来提高模型的泛化能力。

CART剪枝分为【预剪枝】和【后剪枝】两种主要方式。

## 1、预剪枝（Pre-Pruning）
【预剪枝】涉及在决策树完全生成之前停止树的增长。可以通过设置一些停止条件来实现，

1）树达到预定的最大深度（max_depth）

2）节点中的样本数量少于预定阈值（min_samples_split）

3）分割后的节点的信息增益小于某个阈值，

4）节点中样本的纯度（比如，用基尼指数或熵测量）已经足够高。

预剪枝简单易实现，但可能过于保守，有时会导致模型欠拟合。

## 2、后剪枝（Post-Pruning）
【后剪枝】，也称为剪枝，是在决策树完全生成之后进行的。
它通过删除树的部分子树或节点来减少树的复杂度，选择那些能够提高交叉验证数据集准确率的剪枝。

后剪枝策略包括成本复杂度剪枝（Cost Complexity Pruning）、错误率降低剪枝（Reduced Error Pruning）和最小错误剪枝（Minimum Error Pruning）。

成本复杂度剪枝（Cost Complexity Pruning）是通过最小化一个称为成本复杂度的函数来实现剪枝。这个函数是树的错误率和树的复杂度的加权和。

错误率降低剪枝（Reduced Error Pruning）是从叶节点开始，尝试移除每个节点，如果移除后对验证集的分类准确性没有影响或者有所提高，则进行剪枝。

最小错误剪枝（Minimum Error Pruning）是在每个节点上应用一个简单的启发式规则，如果剪枝不会导致错误率增加，则执行剪枝。

## 3、cart剪枝的作用
决策树通过递归地选择最佳属性将数据集分割，构建出一个树状的分类模型。
但一个没有限制的决策树很容易过度拟合训练数据，导致模型在未知数据上的泛化能力下降。

为了解决这个问题，决策树剪枝技术被提出来，以提高决策树模型的泛化能力。
通过剪掉不必要的节点，减少模型对训练数据中噪声的拟合，从而提高模型在未见数据上的泛化能力。
剪枝后的决策树模型更简洁，易于理解和解释，有利于提高模型的可解释性。
简化后的模型在预测时计算量更小，预测速度更快。
剪枝是提高决策树模型泛化能力和效率的重要技术之一，是决策树算法中不可或缺的一部分。
在实际应用中，通过适当选择剪枝策略和参数，可以大幅提升模型的性能。

```text
from sklearn.datasets import load_iris
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 加载iris数据集
iris = load_iris()
X = iris.data
y = iris.target
# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)
# 训练一个决策树模型（未剪枝）
clf = DecisionTreeClassifier(random_state=42)
clf.fit(X_train, y_train)
# 预测测试集
y_pred = clf.predict(X_test)
# 评估模型
accuracy_without_pruning = accuracy_score(y_test, y_pred)
# 训练一个决策树模型（使用代价复杂度剪枝）
clf_pruned = DecisionTreeClassifier(random_state=42, ccp_alpha=0.01)  # ccp_alpha是剪枝的复杂度参数
clf_pruned.fit(X_train, y_train)
# 预测测试集
y_pred_pruned = clf_pruned.predict(X_test)
# 评估模型
accuracy_with_pruning = accuracy_score(y_test, y_pred_pruned)
print(accuracy_without_pruning, accuracy_with_pruning)
```
output:
```text
1.0 1.0
```

## 4、cart剪枝的应用
CART（Classification and Regression Trees）算法用于构建决策树，既可以用于分类问题也可以用于回归问题。

一棵完全生长的决策树往往会过于复杂，导致过拟合，即在训练数据上表现很好但在未见过的数据上表现不佳。

为了解决这个问题，可以采用剪枝（pruning）技术来简化决策树，提高模型的泛化能力。

CART 决策树的构建过程采用【贪心算法】，不断地划分数据集，直到满足停止条件。

DecisionTreeClassifier 是 scikit-learn 中用于解决分类问题的决策树算法实现。

常用参数如下，

| 参数                    | 描述                                                                    |
|-----------------------|-----------------------------------------------------------------------|
| criterion             | 用于衡量分裂质量的函数。支持的标准有 'gini'（基尼不纯度）和 'entropy'（信息增益）。                    |
| splitter              | 选择每个节点处分裂策略的策略。支持的策略有 'best'（选择最佳分裂）和 'random'（选择最佳随机分裂）。             |
| max_depth             | 树的最大深度。如果为 None，则节点扩展直到所有叶子都是纯净的，或直到所有叶子包含小于 min_samples_split 样本的数量。 |
| min_samples_split     | 分裂内部节点所需的最小样本数。                                                       |
| min_samples_leaf      | 一个叶节点所需的最小样本数。                                                        |
| max_features          | 寻找最佳分裂时要考虑的特征数量。可以是整数、浮点数、'auto'、'sqrt' 或 'log2'。                     |
| random_state          | 控制估计器的随机性。即使当 splitter 设置为 'best' 时，每次分裂时特征也总是随机置换的。                  |
| max_leaf_nodes        | 以最佳先行方式生长树，最大叶节点数。如果为 None，则叶节点数量不受限制。                                |
| min_impurity_decrease | 如果此分裂导致不纯度的减少大于或等于此值，则会分裂节点。                                          |
| class_weight          | 与类相关的权重，形式为 {class_label: weight}。如果未给出，则所有类假定有权重一。                   |
| ccp_alpha             | 用于最小化代价复杂度剪枝的复杂度参数。将选择代价复杂度最大且小于 ccp_alpha 的子树。                       |

使用代码，
```text
from sklearn.datasets import load_iris
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.tree import plot_tree
import matplotlib.pyplot as plt

# 加载数据集
iris = load_iris()
X = iris.data
y = iris.target

# 分割数据集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 训练决策树模型
tree = DecisionTreeClassifier(random_state=42)
tree.fit(X_train, y_train)

# 成本复杂度剪枝参数
path = tree.cost_complexity_pruning_path(X_train, y_train)
ccp_alphas, impurities = path.ccp_alphas, path.impurities

# 对每个ccp_alpha训练一个决策树并评估其性能
trees = []
for ccp_alpha in ccp_alphas:
    tree = DecisionTreeClassifier(random_state=0, ccp_alpha=ccp_alpha)
    tree.fit(X_train, y_train)
    trees.append(tree)

# 选择最佳的ccp_alpha值（可根据测试集性能来选择）
# 这里简化了选择过程，实际应用中应该使用交叉验证等方法

# 可视化决策树
plt.figure(figsize=(20, 10))
plot_tree(trees[-1], filled=True, feature_names=iris.feature_names, class_names=iris.target_names)
plt.draw()
plt.show()
```
