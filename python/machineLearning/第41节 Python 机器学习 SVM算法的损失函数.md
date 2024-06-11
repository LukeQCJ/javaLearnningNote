# Python 机器学习 SVM算法的损失函数

支持向量机（SVM）的【核心】是找到一个能够【最大化类别间隔】的【决策边界】，这意味着它不仅试图将不同的类别正确分开，
还努力使最近的数据点（即支持向量）与决策边界之间的距离最大化。
这一目标通过【最小化一个特定的损失函数】来实现，该损失函数可以在惩罚分类错误的同时，优化间隔宽度。

## 1、间隔损失（Hinge Loss）
SVM的一个关键特点是使用了所谓的【间隔损失（Hinge Loss）】，用于确保数据点不仅被正确分类，而且还要远离决策边界。
对于每个数据点，间隔损失函数代码如下，
```text
import numpy as np


def hinge_loss(y_true, y_pred):
    """
        计算间隔损失（Hinge Loss）
    参数:
        y_true -- 真实标签，numpy数组，元素为+1或-1
        y_pred -- 预测值，numpy数组，对应于决策函数的输出
    返回:
        loss -- 计算得到的损失值
    """
    loss = np.maximum(0, 1 - y_true * y_pred)
    return np.mean(loss)


# 示例使用
y_true = np.array([1, -1, 1, -1, 1])  # 真实标签
y_pred = np.array([0.8, -0.5, 1.2, -2.0, 0.5])  # 模型预测值

# 计算损失
loss = hinge_loss(y_true, y_pred)
print(f'Hinge Loss: {loss}')
```
output:
```text
Hinge Loss: 0.24
```

如果一个数据点被正确分类，并且其离决策边界的距离大于等于1，则该点的间隔损失为0。
如果一个数据点被错误分类，或者它的距离小于1（即使它被正确分类），它的损失就会增加。

### 1）使用间隔损失的原因

间隔损失在SVM中被广泛使用的原因是它强制模型不仅要正确分类数据点，还要最大化不同类别之间的决策边界。
这个“间隔”提高了模型的泛化能力，因为它不仅关注于正确分类训练数据，还确保分类决策有足够的“信心”。

### 2）间隔损失与SVM目标函数

【间隔损失】，也称为【铰链损失（Hinge Loss）】，是在训练分类器，尤其是SVM时经常使用的一种损失函数。

SVM的基本目标是找到一个最优的超平面，使得正负样本之间的间隔最大化，同时最小化分类误差。

SVM的目标函数由两部分组成：间隔的宽度的某种度量，以及间隔损失的总和。

【间隔损失】和【SVM的目标函数】是核心概念，理解这些可以帮助我们更好地掌握SVM的工作原理和如何优化模型。

**SVM通过优化这个【目标函数】来训练模型，确保模型不仅可以正确地分类训练数据，同时还能对未知数据进行有效的泛化**。

优化SVM的间隔损失通常通过凸优化方法来完成，如梯度下降或序列最小优化（SMO）算法。
通过最小化间隔损失，SVM尝试找到能够最大化分类间隔的超平面，从而提高模型的泛化能力。

## 2、正则化项
为了防止模型过拟合，SVM在其优化目标中加入了正则化项。
正则化项通常采用权重向量的范数。

在SVM中，最常用的正则化形式是【L2正则化】，它对权重向量的每个元素的平方求和。
将正则化项添加到损失函数中可以促使学习算法找到一个简单的模型，模型对训练数据以外的新数据更具有泛化能力。

**将间隔损失和正则化项结合起来，可以得到SVM的完整损失函数**，代码如下，
```text
import numpy as np


def svm_loss(W, X, y, reg):
    """
        W: 权重矩阵，大小为[D, C]，其中D是特征数量，C是类别数量
        X: 输入数据，大小为[N, D]，其中N是数据点数量
        y: 输入数据的标签，大小为[N]，每个值是对应数据点的类别
        reg: 正则化强度
    返回:
        loss: SVM的损失值
        dW: 损失对权重的梯度
    """
    # 计算分数矩阵
    scores = X.dot(W)
    # 获取正确分类的分数
    correct_class_scores = scores[np.arange(scores.shape[0]), y].reshape(-1, 1)
    # 计算间隔
    margins = np.maximum(0, scores - correct_class_scores + 1)
    margins[np.arange(X.shape[0]), y] = 0  # 正确分类的间隔设为0
    # 计算损失：间隔损失加上正则化损失
    return np.sum(margins) / X.shape[0] + reg * np.sum(W * W)


# 权重矩阵、输入数据、标签和正则化参数
W = np.random.randn(3072, 10) * 0.0001  # 有3072个特征和10个类别
X = np.random.randn(500, 3072)  # 有500个数据点
y = np.random.randint(10, size=500)  # 随机生成500个类别标签
reg = 0.1  # 正则化强度

# 计算SVM的损失
loss = svm_loss(W, X, y, reg)
print("SVM Loss:", loss)
```
output:
```text
SVM Loss: 8.998427679286264
```

## 3、自定义SVM损失函数和梯度下降
SVM的损失函数目的在找到能够最大化边界间隔的最优超平面。
最常用的SVM损失函数是合页损失（Hinge Loss）函数，
为了避免过拟合，通常在损失函数中加入正则化项，最常见的是L2正则化，其目的是限制模型权重的大小，从而增加模型的泛化能力。

梯度下降是一种优化算法，用于最小化SVM的损失函数。
通过计算损失函数对于每个参数的梯度，梯度下降算法可以知道如何调整参数以减少损失。
在每次迭代中，它都会根据梯度的方向和大小，更新权重和偏置，以此逐步逼近最小损失对应的参数值。

SVM的损失函数定义了模型的优化目标，即最大化间隔同时惩罚分类错误和间隔内的点，
而梯度下降则提供了一种实际的方法来达到这个优化目标，通过迭代更新模型参数以最小化损失函数，从而找到最优的分类超平面。

```text
import numpy as np
from sklearn.datasets import make_blobs
from sklearn.preprocessing import StandardScaler


# 定义SVM损失函数和梯度
def svm_loss_and_gradient(W, X, y, C):
    n = X.shape[0]
    scores = np.dot(X, W)
    correct_class_scores = scores[np.arange(n), y].reshape(-1, 1)
    margins = np.maximum(0, scores - correct_class_scores + 1)
    margins[np.arange(n), y] = 0
    loss = np.sum(margins) / n + 0.5 * C * np.sum(W * W)

    binary = margins > 0
    binary = binary.astype(int)
    row_sum = np.sum(binary, axis=1)
    binary[np.arange(n), y] = -row_sum.T
    dw = np.dot(X.T, binary)

    dw /= n
    dw += C * W

    return loss, dw


# 定义梯度下降函数
def gradient_descent(W, X, y, learning_rate, num_iters, C):
    loss_hist = []
    for i in range(num_iters):
        loss, grad = svm_loss_and_gradient(W, X, y, C)
        W -= learning_rate * grad
        loss_hist.append(loss)
        if i % 100 == 0:
            print(f"Iteration {i}: Loss = {loss}")
    return W, loss_hist


# 生成模拟数据
X, y = make_blobs(n_samples=500, centers=2, random_state=42)
X = StandardScaler().fit_transform(X)  # 数据标准化
y[y == 0] = -1  # SVM中的标签需要是-1和1
num_features = X.shape[1]
num_classes = 2

# 初始化权重
W = np.random.randn(num_features, num_classes) * 0.01

# 设置梯度下降参数
learning_rate = 1e-3
num_iters = 1000
C = 1.0

# 运行梯度下降优化权重
W, loss_history = gradient_descent(W, X, y, learning_rate, num_iters, C)

# 输出最终损失
print(loss_history[-1])
```
output:
```text
Iteration 0: Loss = 1.000167982218512
Iteration 100: Loss = 1.0001375184465582
Iteration 200: Loss = 1.0001125793153065
Iteration 300: Loss = 1.0000921629246993
Iteration 400: Loss = 1.0000754490704264
Iteration 500: Loss = 1.0000617662931899
Iteration 600: Loss = 1.000050564903621
Iteration 700: Loss = 1.0000413948991622
Iteration 800: Loss = 1.0000338878857453
Iteration 900: Loss = 1.000027742277999
1.0000227566740023
```