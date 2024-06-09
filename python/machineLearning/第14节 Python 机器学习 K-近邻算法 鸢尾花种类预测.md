# Python 机器学习 K-近邻算法 鸢尾花种类预测

K-近邻算法（K-Nearest Neighbors, KNN）是一种简单而强大的机器学习算法，适用于分类和回归任务。
可以使用scikit-learn库的KNN算法来预测鸢尾花（Iris）的种类。
鸢尾花数据集是机器学习领域中常用的一个数据集，包含了150个鸢尾花样本，
每个样本有四个特征：萼片长度、萼片宽度、花瓣长度和花瓣宽度，以及样本的种类（Setosa、Versicolour、Virginica）。

## 1、鸢尾花数据集
鸢尾花数据集是机器学习中常用的经典数据集之一，由英国统计学家 R. A. Fisher 于 1936 年收集整理。
该数据集包含 150 个样本，每个样本对应一种鸢尾花。并包含 4 个特征：
- 花萼长度
- 花萼宽度
- 花瓣长度
- 花瓣宽度

根据这 4 个特征，可以将鸢尾花分为 3 类：
- 山鸢尾 (Iris setosa)
- 变色鸢尾 (Iris versicolor)
- 维吉尼亚鸢尾 (Iris virginica)

## 2、K-近邻算法 (KNN) 种类预测
K-近邻算法 (KNN) 是一种简单有效的机器学习算法，常用于【分类】和【回归】任务。

KNN 算法通过计算数据点与训练数据集中所有数据点的距离，来确定数据点的类别或预测值。
在种类预测任务中，KNN 算法可以用于预测数据点所属的类别。
对于测试数据集中的每个样本，计算其与训练数据集中的所有样本的距离。
常用的距离度量方法包括欧几里得距离、曼哈顿距离、闵可夫斯基距离等。
根据距离计算的结果，找到与测试样本距离最近的 K 个样本。
根据 K 近邻的类别，预测测试样本的类别。
通常情况下，采用多数投票的方式进行预测，即 K 近邻中出现最多的类别就是测试样本的预测类别。

参考文档：Python 机器学习 K-近邻算法 常用距离度量方法

## 3、预测和评估
使用 Python 机器学习库 scikit-learn 中的 K-近邻算法 (KNN) 来预测鸢尾花的种类，并进行模型评估。
使用鸢尾花（Iris）数据集，应用K-近邻（KNN）算法，
同时比较了三种不同的距离度量方法：欧几里得距离（Euclidean）、曼哈顿距离（Manhattan）和切比雪夫距离（Chebyshev）。代码如下，

1）显示报告
```text
# 导入所需的库
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score, confusion_matrix, classification_report

# 加载鸢尾花数据集
iris = load_iris()
X = iris.data
y = iris.target

# 划分数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3)

# KNN模型使用不同的距离度量
distance_metrics = ['euclidean', 'manhattan', 'chebyshev']
for metric in distance_metrics:
    # 创建KNN分类器
    knn = KNeighborsClassifier(n_neighbors=3, metric=metric)

    # 在训练集上训练模型
    knn.fit(X_train, y_train)

    # 使用模型对测试集进行预测
    y_pred = knn.predict(X_test)

    # 计算并打印模型准确率
    accuracy = accuracy_score(y_test, y_pred)
    print(f'Accuracy: {accuracy:.2f}')

    # 显示混淆矩阵
    conf_matrix = confusion_matrix(y_test, y_pred)
    print('Confusion Matrix:')
    print(conf_matrix)

    # 显示分类报告
    class_report = classification_report(y_test, y_pred)
    print('Classification Report:')
    print(class_report)
```

2）显示图表
```text
import matplotlib.pyplot as plt
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score

# 加载鸢尾花数据集
iris = datasets.load_iris()
X = iris.data
y = iris.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 不同距离度量的准确率
distance_metrics = ['euclidean', 'manhattan', 'chebyshev', 'minkowski']
accuracies = []

# 对每种距离度量方法训练KNN模型并计算准确率
for metric in distance_metrics:
    knn = KNeighborsClassifier(n_neighbors=3, metric=metric)
    knn.fit(X_train, y_train)
    y_pred = knn.predict(X_test)
    accuracy = accuracy_score(y_test, y_pred)
    accuracies.append(accuracy)

# 绘制结果图表
plt.figure(figsize=(10, 6))
plt.bar(distance_metrics, accuracies, color='skyblue')
plt.xlabel('Distance Metric')
plt.ylabel('Accuracy')
plt.title('Accuracy with Different Distance Metrics')
plt.ylim(0.9, 1.05)
for i, acc in enumerate(accuracies):
    plt.text(i, acc, f"{acc:.2f}", ha='center')
plt.show()
```

