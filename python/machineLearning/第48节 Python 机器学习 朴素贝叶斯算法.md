# Python 机器学习 朴素贝叶斯算法

朴素贝叶斯(Naive Bayes)算法是一种基于【贝叶斯定理】和【特征条件独立假设】的简单概率分类器。
尽管简单，朴素贝叶斯分类器在很多情况下表现出意外的准确性，并广泛应用于垃圾邮件过滤、文本分类和情感分析等领域。

## 1、理解朴素贝叶斯
朴素贝叶斯（Naive Bayes）是基于【贝叶斯定理】的一种简单概率分类器，其特点是在特征间假设了强独立性。
尽管这种假设在实际中往往不成立，朴素贝叶斯在诸如文本分类和垃圾邮件识别等领域却展现出了良好的性能。

【贝叶斯定理】提供了一种计算【条件概率】的方法，即在已知某条件下事件发生的概率。
朴素贝叶斯利用这一原理来计算给定特征集下某个类别的后验概率。
这个计算过程涉及到将类别的先验概率与特征给定类别的概率相乘，然后规范化这些概率。

【朴素贝叶斯的主要变体】包括【高斯朴素贝叶斯】、【多项式朴素贝叶斯】和【伯努利朴素贝叶斯】，它们分别适用于不同类型的数据分布和分类问题。
这种算法的优点在于实现简单、计算效率高、适用于高维数据，且能处理缺失数据和不相关特征。
然而，由于忽略了特征间的相关性，朴素贝叶斯的模型精度可能受到影响。
尽管有这些限制，朴素贝叶斯因其在实际应用中的有效性而广受欢迎，特别是在需要快速处理大量数据并做出预测决策的场景中。

## 2、scikit-learn
朴素贝叶斯分类器是一种基于【贝叶斯定理】的【简单概率分类器】，假设各特征之间相互独立。

在scikit-learn库中，提供了几种朴素贝叶斯模型，包括高斯朴素贝叶斯（GaussianNB）、多项式朴素贝叶斯（MultinomialNB）和伯努利朴素贝叶斯（BernoulliNB），
适用于不同类型的数据集。

1）安装命令
```text
pip install scikit-learn
```

2）导入所需模块
```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import GaussianNB
from sklearn.metrics import accuracy_score
```

## 3、数据集
手写数字数据集，通常称为digits数据集，是机器学习和模式识别领域中用于分类算法实验的一个经典数据集。
它包含了从0到9的手写数字的1797个样本，每个样本是一个8x8像素的图像，因此每个图像可以被视为一个64维的向量。
这个数据集通常用于测试和演示分类算法，如支持向量机（SVM）、随机森林、k近邻（k-NN）和线性判别分析（LDA）等。

手写数字数据集（通常称为"Digits"数据集）包含了1797个手写数字的图像，每个图像为8x8像素的灰度图。
这个数据集经常被用作机器学习入门项目，特别是在分类算法的教学和实践中。

使用 scikit-learn 加载手写数字数据集代码如下，
```text
from sklearn.datasets import load_digits

digits = load_digits()
X = digits.data  # 特征矩阵
y = digits.target  # 目标值
print(X)
print(y)
```
output:
```text
[[ 0.  0.  5. ...  0.  0.  0.]
 [ 0.  0.  0. ... 10.  0.  0.]
 [ 0.  0.  0. ... 16.  9.  0.]
 ...
 [ 0.  0.  1. ...  6.  0.  0.]
 [ 0.  0.  2. ... 12.  0.  0.]
 [ 0.  0. 10. ... 12.  1.  0.]]
[0 1 2 ... 8 9 8]
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

使用代码，
```text
from sklearn.datasets import load_digits
from sklearn.model_selection import train_test_split

# 加载手写数字数据集
digits = load_digits()

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(digits.data, digits.target, test_size=0.2, random_state=42)

# 输出分割后的结果大小
print("训练集数据点数量: ", X_train.shape[0])
print("测试集数据点数量: ", X_test.shape[0])
```
output:
```text
训练集数据点数量:  1437
测试集数据点数量:  360
```

## 5、训练朴素贝叶斯模型
朴素贝叶斯分类器是一种基于贝叶斯定理的简单概率分类器，假设特征之间相互独立。

对于手写数字识别这样的任务，高斯朴素贝叶斯通常是一个不错的选择，因为它假设特征遵循正态分布。Python 的scikit-learn 库中，使用 GaussianNB类实现，
常用参数如下，

| 参数            | 类型  | 描述                                                            |
|---------------|-----|---------------------------------------------------------------|
| priors        | 数组  | 类别的先验概率。形状为(n_classes,)。如果指定，则先验不再根据数据自动调整。可以用于基于先前知识调整模型的偏好。 |
| var_smoothing | 浮点数 | 为了保证计算稳定性，所有特征的方差都会增加一小部分的最大方差。用于防止在计算概率时出现除以零的错误。            |

使用代码，

```text
# 导入必要的库
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import GaussianNB
from sklearn.metrics import accuracy_score

# 加载手写数字数据集
digits = datasets.load_digits()
X = digits.data
y = digits.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

# 初始化高斯朴素贝叶斯分类器
gnb = GaussianNB()

# 训练模型
gnb.fit(X_train, y_train)

# 预测测试集
y_pred = gnb.predict(X_test)

# 计算并打印准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'模型准确率: {accuracy}')
```
output:
```text
模型准确率: 0.825
```