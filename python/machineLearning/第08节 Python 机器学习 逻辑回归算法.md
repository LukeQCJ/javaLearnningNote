# Python 机器学习 逻辑回归算法

逻辑回归（Logistic Regression）是一种广泛使用的机器学习算法，尽管名称中有“回归”二字，但它实际上是用于解决分类问题的，特别是二分类问题。

逻辑回归通过使用逻辑函数（通常是Sigmoid函数）将线性回归的输出映射到0和1之间，以此来预测某个样本属于某个类别的概率。

Python 中，可以使用scikit-learn库中的LogisticRegression类很方便地实现逻辑回归模型。

## 1、理解逻辑回归
逻辑回归建立在线性回归之上。

在线性回归中，模型预测的是一个连续的数值。

而在逻辑回归中，线性回归的输出被输入到Sigmoid函数中，用于预测某个类别的概率。
Sigmoid函数是一个S形的曲线，它将任意实数映射到(0, 1)区间，适合用来表达概率。

逻辑回归广泛应用于各种二分类问题，如垃圾邮件检测、疾病诊断、客户流失预测等。
它的优点是模型简单、易于实现、计算效率高，而且输出的是概率值，这对于许多需要概率解释的应用非常有用。

## 2、scikit-learn
scikit-learn 是 Python 中一个强大的机器学习库，它提供了各种常用机器学习算法的简单易用的实现。
使用 scikit-learn，可以快速进行数据预处理、模型训练、评估和预测，从而进行有效的机器学习分析。
scikit-learn库提供了简单易用的逻辑回归实现，可以用来处理二分类和多分类问题。

### 1）安装命令
```text
pip install scikit-learn
```

### 2）导入所需模块
```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
```

### 3、数据集
乳腺癌数据集包含了569个样本，每个样本有30个特征。
这些特征是从数字化图像中计算出来的，用于描述图像中的细胞核的特点，目标是预测肿瘤是良性还是恶性。
Python 的 scikit-learn 库中，可以直接加载并使用这个数据集。通常用于二分类问题，它包含了乳腺癌肿瘤的特征和肿瘤是良性还是恶性的标签。

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

Wine数据集是对意大利同一地区种植的三种不同品种的酒进行化学分析的结果，包含了178个样本，每个样本有13个特征。
这些特征是从酒的化学成分分析中得到的，目标是根据化学分析预测酒的品种。
Python 的 scikit-learn 库中，可以直接加载并使用这个数据集。常用于分类问题的数据集，其中包含了三种不同来源的葡萄酒的化学成分分析结果。

Wine数据集的特征：

| 特征编号 | 特征名称                         | 描述                 |
|------|------------------------------|--------------------|
| 1    | Alcohol                      | 酒精度数               |
| 2    | Malic acid                   | 苹果酸含量              |
| 3    | Ash                          | 灰分                 |
| 4    | Alcalinity of ash            | 灰分的碱性              |
| 5    | Magnesium                    | 镁含量                |
| 6    | Total phenols                | 总酚含量               |
| 7    | Flavanoids                   | 黄烷醇类化合物含量          |
| 8    | Nonflavanoid phenols         | 非黄烷醇类酚类化合物含量       |
| 9    | Proanthocyanins              | 原花青素含量             |
| 10   | Color intensity              | 颜色强度               |
| 11   | Hue                          | 色调                 |
| 12   | OD280/OD315 of diluted wines | 稀释葡萄酒的OD280/OD315值 |
| 13   | Proline                      | 脯氨酸含量              |

使用 scikit-learn 加载Wine数据集代码如下，
```text
from sklearn.datasets import load_wine

# 加载Wine数据集
wine = load_wine()

# 输出数据集概述
data_summary = {
    'feature_names': wine.feature_names,
    'target_names': wine.target_names,
    'data_shape': wine.data.shape,
    'target_shape': wine.target.shape
}

print(data_summary)
```

## 4、划分数据集
使用train_test_split函数将数据集划分为训练集和测试集，train_test_split函数是Scikit-Learn库中一个非常重要的工具。

常用参数如下，

| 参数名          | 描述                                                                              |
|--------------|---------------------------------------------------------------------------------|
| arrays       | 需要划分的数据，通常是特征集和标签集。例如：X_train, X_test, y_train, y_test = train_test_split(X, y) |
| test_size    | 测试集所占的比例或数量。例如：train_test_split(X, y, test_size=0.2)表示测试集占总数据集的20%。             |
| train_size   | 训练集所占的比例或数量。例如：train_test_split(X, y, train_size=0.8)表示训练集占总数据集的80%。            |
| random_state | 控制随机数生成器的种子。例如：train_test_split(X, y, random_state=42)确保每次划分结果相同。               |
| shuffle      | 是否在划分前随机打乱数据。例如：train_test_split(X, y, shuffle=True)默认会打乱数据。                    |
| stratify     | 确保训练集和测试集具有相同的类比例。例如：train_test_split(X, y, stratify=y)确保类别分布均衡。                |

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

## 5、训练逻辑回归模型
Python中使用scikit-learn库训练逻辑回归模型是一种常见的机器学习任务。
逻辑回归通常用于分类问题，特别是二分类问题。
LogisticRegression类是用于训练逻辑回归模型的。它适用于二分类和多分类问题，并提供了广泛的参数来调整模型。

常用参数如下，

| 参数	           | 描述                                                                     |
|---------------|------------------------------------------------------------------------|
| penalty	      | 指定惩罚（正则化项）的种类，可选值有'l1'、'l2'、'elasticnet'、'none'。默认值为'l2'。              |
| C	            | 正则化强度的逆，较小的值指定更强的正则化。默认值为1.0。                                          |
| solver	       | 优化问题的算法，可选值包括'newton-cg'、'lbfgs'、'liblinear'、'sag'、'saga'。默认值为'lbfgs'。 |
| max_iter      | 求解器的最大迭代次数。默认值为100。                                                    |
| random_state	 | 随机数生成器的种子，用于数据的随机分割，当'solver'是'sag'、'saga'或'liblinear'时使用。默认值为None。    |
| tol	          | 停止准则的容忍度，当损失函数的改变小于此值时，优化过程将停止。默认值为1e-4。                               |
| multi_class	  | 多分类问题的策略，可选值为'auto'、'ovr'、'multinomial'。默认值为'auto'。                    |
| class_weight	 | 与类相关联的权重，可以是字典、'balanced'或None。默认值为None。                               |

使用代码：
```text
from sklearn.datasets import load_breast_cancer
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score

# 加载乳腺癌数据集
data = load_breast_cancer()
X, y = data.data, data.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建逻辑回归模型实例
# 使用L2正则化，'lbfgs'求解器，增加最大迭代次数以确保收敛
log_reg = LogisticRegression(penalty='l2', C=1.0, solver='lbfgs', max_iter=5000, random_state=42)

# 训练模型
log_reg.fit(X_train, y_train)

# 使用模型进行预测
y_pred = log_reg.predict(X_test)

# 计算并打印准确率
accuracy = accuracy_score(y_test, y_pred)
print("Accuracy on the breast cancer dataset: {:.2f}%".format(accuracy * 100))
```
output:
```text
Accuracy on the breast cancer dataset: 95.61%
```
