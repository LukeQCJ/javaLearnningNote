# Python 机器学习 决策树算法

决策树是一种常用的机器学习算法，它适用于分类和回归任务。
其核心思想是基于数据特征对实例进行分类的过程，可以视为一系列规则的集合。

决策树易于理解和解释，能够处理数值型和类别型数据，并且可以可视化决策过程。

决策树的主要优点是模型的可解释性强，但同时也容易过拟合。
为了解决过拟合问题，可以通过剪枝参数如max_depth、min_samples_split等来限制树的大小。

## 1、理解决策树
决策树是一种常用的机器学习算法，既可以用于【分类】问题也可以用于【回归】问题。
它通过从数据中学习【决策规则】来预测目标变量。

决策树的【核心思想】是基于数据特征的决策规则将数据集分割成越来越小的子集，直至每个子集足够"纯"，即子集中的数据大部分属于同一类别。
在这个过程中，每个内部节点代表一个决策规则，每个分支代表决策规则的输出，每个叶节点代表一个预测结果。

决策树可以通过【逻辑图形】很容易地展示和理解，不需要统计知识即可解释模型的预测结果。
能够处理数值型和类别型的特征。
能够处理多输出的分类问题。

## 2、scikit-learn
scikit-learn 是 Python 中一个强大的机器学习库，它提供了各种常用机器学习算法的简单易用的实现。
使用 scikit-learn，可以快速进行数据预处理、模型训练、评估和预测，从而进行有效的机器学习分析。

决策树是一种常用的机器学习算法，适用于分类和回归任务。它通过学习从【数据特征】到输出标签的决策规则来建模。

在Python中，可以使用scikit-learn库中的DecisionTreeClassifier或DecisionTreeRegressor类来实现决策树模型。

1）安装命令
```text
pip install scikit-learn
```

2）导入所需模块
```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score
from sklearn import tree
```

## 3、数据集
乳腺癌数据集包含了569个样本，每个样本有30个特征。
这些特征是从数字化图像中计算出来的，用于描述图像中的细胞核的特点，目标是预测肿瘤是良性还是恶性。

Python 的 scikit-learn 库中，可以直接加载并使用这个数据集。
通常用于二分类问题，它包含了乳腺癌肿瘤的特征和肿瘤是良性还是恶性的标签。

乳腺癌数据集的特征：

| 特征编号 | 特征名称                    | 描述                     |
|------|-------------------------|------------------------|
| 1    | mean radius             | 平均半径（肿瘤中心到周边点的平均距离）    |
| 2    | mean texture            | 平均纹理（灰度值的标准差）          |
| 3    | mean perimeter          | 平均周长                   |
| 4    | mean area               | 平均面积                   |
| 5    | mean smoothness         | 平均平滑度（局部变化的长度）         |
| 6    | mean compactness        | 平均紧凑度（周长^2 / 面积 - 1.0） |
| 7    | mean concavity          | 平均凹度（轮廓凹部的严重程度）        |
| 8    | mean concave points     | 平均凹点（轮廓凹部的数量）          |
| 9    | mean symmetry           | 平均对称性                  |
| 10   | mean fractal dimension  | 平均分形维数（“海岸线近似” - 1）    |
| 11   | radius error            | 半径误差                   |
| 12   | texture error           | 纹理误差                   |
| 13   | perimeter error         | 周长误差                   |
| 14   | area error              | 面积误差                   |
| 15   | smoothness error        | 平滑度误差                  |
| 16   | compactness error       | 紧凑度误差                  |
| 17   | concavity error         | 凹度误差                   |
| 18   | concave points error    | 凹点误差                   |
| 19   | symmetry error          | 对称性误差                  |
| 20   | fractal dimension error | 分形维数误差                 |
| 21   | worst radius            | 最大半径（同1，但是是肿瘤最大半径的平均值） |
| 22   | worst texture           | 最大纹理（同2，但是是最大的平均值）     |
| 23   | worst perimeter         | 最大周长                   |
| 24   | worst area              | 最大面积                   |
| 25   | worst smoothness        | 最大平滑度                  |
| 26   | worst compactness       | 最大紧凑度                  |
| 27   | worst concavity         | 最大凹度                   |
| 28   | worst concave points    | 最大凹点                   |
| 29   | worst symmetry          | 最大对称性                  |
| 30   | worst fractal dimension | 最大分形维数                 |

使用 scikit-learn 加载乳腺癌数据集代码如下，

```text
from sklearn.datasets import load_breast_cancer

# 加载乳腺癌数据集
data = load_breast_cancer()

# 输出数据集的描述
print(data.DESCR)

# 获取特征和目标变量
X, y = data.data, data.target

# 输出特征名称和标签名称
print("Feature names:", data.feature_names)
print("Target names:", data.target_names)

# 输出数据集的形状
print("Shape of X (features):", X.shape)
print("Shape of y (target):", y.shape)
```

## 4、划分数据集
使用train_test_split()函数将数据集划分为训练集和测试集，train_test_split()函数是Scikit-Learn库中一个非常重要的工具。

常用参数如下，

| 参数           | 描述及类型                                                                                    |
|--------------|------------------------------------------------------------------------------------------|
| arrays       | 输入数据，可以是列表、numpy数组、pandas的DataFrame或Series，通常包括特征数据X 和标签 y。                              |
| test_size    | 测试集占比，介于0到1之间的浮点数或整数，指定测试集在数据集中所占的比例或样本数量。                                               |
| train_size   | 训练集占比，介于0到1之间的浮点数或整数，指定训练集在数据集中所占的比例或样本数量。通常，只设置 test_size 或 train_size 之一即可。            |
| random_state | 控制数据分割的随机性。整数或RandomState实例。通过设置相同的值，可以确保每次运行代码时数据以相同的方式分割。                              |
| shuffle      | 是否在分割之前对数据进行洗牌。布尔值，默认为True。如果设置为 False，则不打乱数据，直接按顺序分割。                                   |
| stratify     | 用于进行分层抽样的标签数组。数组型数据。设置这个参数后，train_test_split会以此数组中的类分布比例来分割数据，确保训练集和测试集中各类数据的比例与原始数据集相同。 |

使用代码：
```text
from sklearn.datasets import load_breast_cancer
from sklearn.model_selection import train_test_split

# 加载乳腺癌数据集
data = load_breast_cancer()
X = data.data
y = data.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42, stratify=y)

# 输出分割结果
print("训练集样本数量:", X_train.shape[0])
print("测试集样本数量:", X_test.shape[0])
print("训练集中每类的样本比例:", [sum(y_train == i) for i in range(len(data.target_names))])
print("测试集中每类的样本比例:", [sum(y_test == i) for i in range(len(data.target_names))])
```
output:
```text
训练集样本数量: 455
测试集样本数量: 114
训练集中每类的样本比例: [170, 285]
测试集中每类的样本比例: [42, 72]
```

## 5、训练决策树模型
Python中使用scikit-learn库训练逻辑回归模型是一种常见的机器学习任务。

DecisionTreeClassifier是Scikit-learn库中用于分类任务的决策树模型。
它非常灵活和强大，可以通过调整不同的参数来优化模型的性能和防止过拟合。

常用参数如下，

| 参数	                    | 描述                                                           |
|------------------------|--------------------------------------------------------------|
| criterion	             | 用于衡量分割质量的函数。 默认值/选项: "gini"、"entropy"                        |
| splitter	              | 用于在节点处选择分割策略的方法。 默认值/选项: "best"、"random"                     |
| max_depth	             | 树的最大深度。 默认值/选项: None                                         |
| min_samples_split	     | 分割内部节点所需的最小样本数。 默认值/选项: 2                                    |
| min_samples_leaf	      | 在叶节点处需要的最小样本数。 默认值/选项: 1                                     |
| max_features	          | 寻找最佳分割时要考虑的特征数量。 默认值/选项: int、float、"auto"、"sqrt"、"log2"、None |
| random_state	          | 控制分割器的随机性。用于复现结果。 默认值/选项: None                               |
| max_leaf_nodes	        | 以最佳优先方式增长的最大叶节点数。 默认值/选项: None                               |
| min_impurity_decrease	 | 如果节点分割导致不纯度的减少大于或等于这个值，则节点将会分割。 默认值/选项: 0.0                  |
| class_weight	          | 类别权重的形式。 默认值/选项: None、"balanced"、dict                        |

使用代码：
```text
from sklearn.datasets import load_breast_cancer
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score

# 加载乳腺癌数据集
data = load_breast_cancer()
X, y = data.data, data.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 创建决策树模型实例
# 我们将使用一些自定义参数，但你可以根据需要调整它们
clf = DecisionTreeClassifier(
    criterion='gini',  # 使用基尼不纯度作为分割质量的衡量标准
    splitter='best',  # 选择最佳分割
    max_depth=None,  # 不限制树的最大深度
    min_samples_split=2,  # 分割内部节点所需的最小样本数
    min_samples_leaf=1,  # 在叶节点处需要的最小样本数
    max_features=None,  # 考虑所有特征
    random_state=42,  # 设置随机状态以复现结果
    max_leaf_nodes=None,  # 不限制最大叶节点数
    min_impurity_decrease=0.0,  # 不考虑不纯度减少的最小值
    class_weight=None  # 不使用类别权重
)

# 训练模型
clf.fit(X_train, y_train)

# 预测测试集
y_pred = clf.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')

# 还可以使用其他评估指标，如混淆矩阵、精确度、召回率等，来进一步评估模型性能。
```
output:
```text
Accuracy: 0.9415204678362573
```

DecisionTreeRegressor是Scikit-learn库中用于回归任务的决策树模型。它通过学习数据中的决策规则来预测连续值的输出。

常用参数如下，

| 参数	                    | 描述                                                                  |
|------------------------|---------------------------------------------------------------------|
| criterion	             | 衡量分割质量的函数。默认值/选项："mse"（默认）、"friedman_mse"、"mae"、"poisson"           |
| splitter	              | 选择每个节点分割策略的函数。默认值/选项："best"（默认）、"random"                            |
| max_depth	             | 树的最大深度。默认值/选项：None（不限制）                                             |
| min_samples_split	     | 分割内部节点所需的最小样本数。默认值/选项：2                                             |
| min_samples_leaf	      | 在叶节点上所需的最小样本数。默认值/选项：1                                              |
| max_features	          | 寻找最佳分割时要考虑的特征数量。默认值/选项：int, float, "auto", "sqrt", "log2", None（默认） |
| random_state	          | 控制分割器的随机性及特征选择的随机性。默认值/选项：None                                      |
| max_leaf_nodes	        | 以最佳优先方式增长树时最大的叶子节点数。默认值/选项：None（不限制）                                |
| min_impurity_decrease	 | 节点的不纯度减少必须大于或等于该值，才会进行分割。默认值/选项：0                                   |

使用代码：

```text
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeRegressor
from sklearn.metrics import mean_squared_error

# 加载糖尿病数据集
X, y = load_diabetes(return_X_y=True)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 初始化决策树回归模型
# 这里我们使用一些自定义参数，例如最大深度和最小样本分裂
decision_tree_regressor = DecisionTreeRegressor(max_depth=4, min_samples_split=20, random_state=42)

# 训练模型
decision_tree_regressor.fit(X_train, y_train)

# 预测测试集
y_pred = decision_tree_regressor.predict(X_test)

# 计算模型性能
mse = mean_squared_error(y_test, y_pred)
print(f"Mean Squared Error: {mse}")

# 可选：输出决策树的深度和叶节点数
print(f"Model Depth: {decision_tree_regressor.get_depth()}")
print(f"Number of Leaves: {decision_tree_regressor.get_n_leaves()}")
```
output:
```text
Mean Squared Error: 3466.661005904052
Model Depth: 4
Number of Leaves: 13
```