# Python 机器学习 SVM算法

支持向量机（SVM）是一种强大的机器学习模型，用于解决分类、回归和异常检测问题。

SVM在高维空间中非常有效，尤其适用于情况复杂或数据维度超过样本数量的情形。

SVM的【核心思想】是找到最优的超平面（在二维空间中是一条直线），这个超平面能够最大化不同类别之间的边界。

## 1、理解SVM算法
支持向量机（SVM）是一种广泛应用于分类和回归任务的【监督学习算法】。
它通过【最大间隔原则】来寻找最优决策边界，这意味着算法旨在找到一条线（在高维空间中是一个超平面），最大化地分开不同类别的数据点。
决策边界的确定依赖于【距离边界最近的那些数据点】，被称为【支持向量】，这些点的特性决定了边界的方向和位置。

在处理线性不可分的数据集时，SVM采用【核技巧】通过映射将原始数据投影到更高维的空间，以便找到一个合适的线性决策边界。
算法背后的数学原理涉及到使用拉格朗日乘子法最小化一个目标函数，同时满足一系列约束条件，以确定最佳的权重向量和偏差项。

SVM以其在小数据集和高维数据处理中的出色性能而闻名，能够通过选择合适的【核函数】来有效处理各种线性不可分的情况，
使其成为机器学习领域中一个非常受欢迎的工具。

## 2、scikit-learn
支持向量机（SVM）是一种广泛应用于分类、回归和其他预测任务的强大机器学习算法。

Python中，Scikit-learn是一个流行的机器学习库，它提供了易于使用的SVM实现。

1）安装命令
```text
pip install scikit-learn
```

2）导入所需模块
```text
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn import svm
from sklearn.metrics import accuracy_score
```

## 3、数据集
乳腺癌数据集（Breast Cancer Wisconsin dataset）是一个常用于机器学习的公开数据集，主要用于分类任务。
这个数据集包含乳腺癌肿瘤的特征以及肿瘤是良性还是恶性的标签。
特征包括细胞核的形状、大小和纹理等细节。
数据集由569个样本组成，每个样本包括30个特征和一个目标标签，表示肿瘤是良性（B）还是恶性（M）。
常用于练习数据预处理、特征选择、以及训练不同的分类器，例如逻辑回归、决策树、随机森林和支持向量机等。

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

| 参数名          | 描述                                                                              |
|--------------|---------------------------------------------------------------------------------|
| arrays       | 需要划分的数据，通常是特征集和标签集。例如：X_train, X_test, y_train, y_test = train_test_split(X, y) |
| test_size    | 测试集所占的比例或数量。例如：train_test_split(X, y, test_size=0.2)表示测试集占总数据集的20%。             |
| train_size   | 训练集所占的比例或数量。例如：train_test_split(X, y, train_size=0.8)表示训练集占总数据集的80%。            |
| random_state | 控制随机数生成器的种子。例如：train_test_split(X, y, random_state=42)确保每次划分结果相同。               |
| shuffle      | 是否在划分前随机打乱数据。 例如：train_test_split(X, y, shuffle=True)默认会打乱数据。                   |
| stratify     | 确保训练集和测试集具有相同的类比例。 例如：train_test_split(X, y, stratify=y)确保类别分布均衡。               |

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

## 5、训练SVM模型
Python中使用scikit-learn库训练支持向量机（SVM）模型是一个直接而高效的过程，可以应用于分类、回归甚至是异常值检测任务。

SVC（Support Vector Classification）类是支持向量机（SVM）用于分类任务的实现。
它提供了一个富有弹性的方式来执行线性和非线性分类。
SVC类是在sklearn.svm模块中定义的。

常用参数如下，

| 参数                       | 描述                                                                                                          |
|--------------------------|-------------------------------------------------------------------------------------------------------------|
| C	                       | 正则化参数，控制错误项的惩罚程度，较小的值指定更强的正则化。                                                                              |
| kernel	                  | 核函数类型（'linear', 'poly', 'rbf', 'sigmoid','precomputed' 或 callable）。默认是'rbf'。用于在任意的数据上执行线性和非线性分类。            |
| degree	                  | 多项式核函数的阶数（'poly'）。仅当使用多项式核函数时有意义。                                                                           |
| gamma	                   | 'rbf', 'poly' 和 'sigmoid'核函数的系数。如果是'auto'，则使用1/特征数量的倒数作为值。默认是'scale'。                                       |
| coef0	                   | 核函数中的独立项。它只对'poly'和'sigmoid'核有用。                                                                            |
| shrinking	               | 是否使用收缩启发式方法，默认为True。                                                                                        |
| probability	             | 是否启用概率估计，默认为False。必须在调用fit方法前启用，启用后提供predict_proba和predict_log_proba方法。                                     |
| tol	                     | 停止训练的容忍度，默认为1e-3。                                                                                           |
| cache_size	              | 核函数缓存大小（以MB为单位），默认为200。增加此值可以加快训练速度，特别是对于大型数据集。                                                             |
| class_weight	            | 设置每个类别的参数C的乘数，默认为None，即所有类别的权重都是一样的。也可以设置为'balanced'自动调整权重，使得每个类别的样本数量成比例。                                  |
| max_iter	                | 硬限制在求解器中迭代的次数，或者-1表示无限制，默认为-1。                                                                              |
| decision_function_shape	 | 多分类策略。'ovo'表示一对一，'ovr'表示一对其余，默认是'ovr'。只有在decision_function_shape为'ovr'时才有predict_proba和predict_log_proba方法。 |

使用代码，
```text
from sklearn.datasets import load_breast_cancer
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score, confusion_matrix, classification_report

# 加载数据集
data = load_breast_cancer()
X = data.data
y = data.target

# 划分数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 数据标准化
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

# 初始化SVC模型
svc = SVC(kernel='linear', C=1.0, probability=True)

# 训练模型
svc.fit(X_train, y_train)

# 预测测试集
y_pred = svc.predict(X_test)

# 模型评估
accuracy = accuracy_score(y_test, y_pred)
conf_matrix = confusion_matrix(y_test, y_pred)
class_report = classification_report(y_test, y_pred)

print(f'Accuracy: {accuracy}\n')
print(f'Confusion Matrix:\n{conf_matrix}\n')
print(f'Classification Report:\n{class_report}')
```
output:
```text
Accuracy: 0.9766081871345029

Confusion Matrix:
[[ 61   2]
 [  2 106]]

Classification Report:
              precision    recall  f1-score   support

           0       0.97      0.97      0.97        63
           1       0.98      0.98      0.98       108

    accuracy                           0.98       171
   macro avg       0.97      0.97      0.97       171
weighted avg       0.98      0.98      0.98       171
```
