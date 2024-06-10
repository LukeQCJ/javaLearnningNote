# Python 机器学习 决策树 分类原理

决策树是机器学习中用于分类和回归的一种非常流行的模型。它模拟了【人类决策过程】的方式，通过一系列的问题来推导出最终的决策结果。
在分类问题中，决策树通过从数据集中学习决策规则来预测目标变量的类别。在分类问题中，决策树通过从数据的特征中学习一系列规则来预测目标变量的类别。

决策树的【核心思想】是【基于特征】对数据集进行分割，使得每个分割后的子集比原始数据集在目标变量上的分布更加纯净。
过程可以看作是在特征空间中进行递归划分，最终得到的树状结构可以直观地展示出决策过程。

## 1、决策树分类原理
决策树是一种常用的机器学习模型，可以用于分类和回归任务。
决策树由一系列节点和边组成，每个节点代表一个特征，每个边代表一个特征的取值。

决策树的【分类原理】是通过一系列的判断和条件分支，将样本划分为不同的类别。
决策树在分类过程中通过从根节点开始，基于实例的特征值沿树向下遍历至叶节点来决定实例的类别。
在每个节点，根据特征值的测试结果选择分支，直至到达叶节点，此时实例被分配到该叶节点关联的类别，叶节点的类型标签就是最终的分类结果。

决策树的优点在于其过程的可解释性强，能够处理数值型和类别型特征，且不需要数据预处理。
然而，决策树也容易过拟合，且对数据的小变化敏感，可能导致生成完全不同的树结构。

通过调整参数如树的深度和最小分割样本数，可以在模型的复杂性和泛化能力之间找到平衡，优化模型性能。

### 1）选择最佳特征

选择最佳特征是构建决策树时的关键步骤。目标是找到最佳的分割点，以便尽可能准确地分类实例。

从当前数据集的特征中选择最佳的分裂特征。
这个选择基于特定的标准，比如信息增益（在ID3算法中使用）、信息增益比（在C4.5算法中使用）或基尼不纯度（在CART算法中使用）。

### 2）分裂节点

在分类问题中，决策树的目标是根据输入特征将数据点分到预先定义的类别中。

决策树的核心在于如何选择最佳的分裂属性和分裂点，以便在每个节点上清晰地区分不同类别的数据。

决策树构建过程中的关键步骤是【确定每个节点上的最佳分裂】。
这个过程涉及评估每个特征对数据的分割效果，以选择最能提高数据集纯度的特征。

构建决策树时，算法会遍历所有可能的特征及其可能的分裂点，根据选定的评判标准（如信息增益或基尼不纯度）来评估分裂的效果。
该过程从根节点开始，递归地在每个节点上重复，直到满足停止条件。

### 3）递归构建树

对每个子节点重复步骤 1）和步骤2），直到满足特定的停止条件，如节点中的所有实例都属于同一类别、达到预设的最大深度、节点中的实例数少于预设的最小分裂数等。

### 4）剪枝

构建决策树后，可能会出现过拟合的现象，即模型在训练数据上表现很好，但是在未见过的数据上表现不佳。

为了降低过拟合的风险，需要对决策树进行剪枝。
为了避免过拟合，可以对已经生成的树进行剪枝。

剪枝可以是预剪枝（在构建树的过程中提前停止树的增长）或后剪枝（在树完全生成后去除一些子树）。
剪枝通过减少决策树的大小来降低过拟合风险，预剪枝是在决策树构建过程中就停止树的进一步分裂。
后剪枝是在决策树构建完毕后进行的。
一种常见的后剪枝技术是代价复杂度剪枝（Cost Complexity Pruning），该方法考虑到错误率和树的复杂度，通过删除一些子树然后用叶节点替代来减少树的大小。
剪枝是提高决策树泛化能力的重要手段。
通过合理的剪枝，可以使决策树模型在未知数据上有更好的表现。

可视化决策树代码如下，
```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.tree import plot_tree
import matplotlib.pyplot as plt

# 加载iris数据集
iris = load_iris()
X = iris.data
y = iris.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 初始化决策树分类器
clf = DecisionTreeClassifier(max_depth=3, random_state=42)

# 训练决策树模型
clf.fit(X_train, y_train)

# 使用模型进行预测
predictions = clf.predict(X_test)

# 打印预测结果
print(predictions)

# 可选：查看模型的决策树结构
plt.figure(figsize=(20, 10))
plot_tree(clf, filled=True, feature_names=iris.feature_names, class_names=iris.target_names, rounded=True)
plt.draw()
plt.show()
```
output:
```text
[1 0 2 1 1 0 1 2 1 1 2 0 0 0 0 1 2 1 1 2 0 2 0 2 2 2 2 2 0 0]
```

## 2、熵（entropy）
熵是信息论中的一个概念，用来衡量随机变量不确定性的度量。

在决策树中，它用来衡量数据集的纯度（或混乱度）。
如果一个数据集内的所有记录都属于同一个类别，则称该数据集为完全纯净，其熵为0；
如果数据集中的记录均匀分布在多个类别中，则熵值最大，数据集的混乱度最高。

在构建决策树时，利用熵来评估分裂后数据集的混乱程度。
对于每个可能的分裂，计算分裂后各个子集的熵，并利用这些熵值来计算加权平均后的总熵，这个过程称为信息增益（Information Gain）计算。
信息增益表示的是分裂前后熵的减少量，也就是说，通过这个分裂我们减少了多少不确定性。

公式代码如下，

```text
import math


def calculate_entropy(data):
    """计算给定数据集的熵"""
    label_count = {}
    # 统计每个标签出现的次数
    for record in data:
        label = record[-1]  # 假设标签在每条记录的最后一个位置
        if label not in label_count:
            label_count[label] = 0
        label_count[label] += 1
    # 计算熵
    entropy = 0.0
    total = len(data)
    for label in label_count:
        probability = label_count[label] / total
        entropy -= probability * math.log(probability, 2)  # 以2为底的对数
    return entropy


# 示例数据集：[天气, 是否进行户外活动]
data = [
    ['晴', '是'],
    ['阴', '是'],
    ['雨', '否'],
    ['晴', '是'],
    ['晴', '否'],
    ['阴', '否'],
    ['雨', '是'],
    ['雨', '否'],
]

print(f"数据集的熵: {calculate_entropy(data)}")
```
output:
```text
数据集的熵: 1.0
```

## 3、分裂标准
决策树构建的关键是选择最优特征及其分裂点。为了实现这一点，决策树使用了各种算法来计算特征的选择标准。

### 1）信息增益（ID3算法）

**信息增益**是基于熵（entropy:/ˈen.trə.pi/）的概念，**熵**是度量数据集纯度最常用的一种方式。

信息增益表示在知道某特征的信息之后使得类别的信息熵减少的程度。**选择信息增益最大的特征作为分裂特征**。

### 2）信息增益比（C4.5算法）

信息增益比是信息增益和特征熵的比值。它解决了信息增益倾向于选择取值较多的特征的问题。

### 3）基尼不纯度（CART算法）

基尼不纯度是衡量数据集的不纯度的一种方法，类似于熵的概念但计算上更简单。

基尼不纯度越小，数据集的纯度越高。CART（分类与回归树）算法使用基尼不纯度来选择分裂特征。

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score

# 加载数据集
iris = load_iris()
X, y = iris.data, iris.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 使用不同的分裂标准训练决策树模型
criteria = ['gini', 'entropy']
for criterion in criteria:
    # 初始化决策树分类器
    clf = DecisionTreeClassifier(criterion=criterion, random_state=42)
    # 训练模型
    clf.fit(X_train, y_train)
    # 在测试集上进行预测
    y_pred = clf.predict(X_test)
    # 计算并打印准确率
    accuracy = accuracy_score(y_test, y_pred)
    print(f"Criterion: {criterion}, Accuracy: {accuracy:.4f}")
```
output:
```text
Criterion: gini, Accuracy: 1.0000
Criterion: entropy, Accuracy: 1.0000
```

决策树的结果很容易理解和解释，非专业人士也能理解模型决策逻辑。不需要归一化或标准化数据。
既能处理数值型数据，也能处理类别数据。
但容易过拟合，特别是当树很深时，模型可能会过于复杂，捕捉到训练数据中的噪声。
并且稳定性差，小的数据变化可能导致生成完全不同的树。