# Python 机器学习 线性回归的损失和优化

机器学习中，线性回归是一种预测连续值的算法，例如房价、销售额等。

线性回归模型试图找到一条直线（在多维空间中则是一个平面或超平面），这条直线能够尽可能准确地表示输入变量（特征）和输出变量（目标值）之间的关系。

模型的损失（Loss）和优化（Optimization）是建立有效线性回归模型的两个关键方面。

## 1、线性回归算法
线性回归是统计学中最基本且广泛使用的预测模型之一，实现在研究自变量（X）和因变量（Y）之间的线性关系。

线性回归模型可以是简单的单变量线性回归，
也可以是涉及多个自变量的多元线性回归线性回归模型试图学习系数 w 和截距 b，使得预测值 y_pred = w * X + b 尽可能接近实际值 Y，
通常通过最小化误差的平方和来求解 w 和 b。

线性回归广泛应用于经济学、生物学、工程学等领域，用于预测分析、趋势分析和变量之间关系的研究。
它是许多复杂预测模型的基础，理解线性回归对于深入学习更高级的机器学习和统计模型非常重要。

参考文档：Python 机器学习 线性回归算法

## 2、损失函数
线性回归中最常用的损失函数是【均方误差（Mean Squared Error, MSE）】。
通常称为均方误差（MSE）损失，是模型预测与实际数据之间差异的量度。

线性回归模型试图找到一组参数，使得给定输入数据集上的损失函数值最小。公式如下，
```text
import numpy as np

# 假设 X 是输入特征矩阵，y 是目标值向量
X = np.array([[1, 2], [1, 3], [1, 4], [1, 5]])  # 添加了一列1作为x0，以便处理截距项
y = np.array([5, 7, 9, 11])

# 初始化模型参数，theta0为截距，theta1, theta2为斜率
theta = np.array([0.1, 0.2])


# 线性回归模型的预测函数
def predict(X, theta):
    return X.dot(theta)


# 计算损失函数（MSE）
def compute_loss(X, y, theta):
    m = len(y)
    y_pred = predict(X, theta)
    loss = (1 / (2 * m)) * np.sum(np.square(y_pred - y))
    return loss


# 计算损失值
loss = compute_loss(X, y, theta)
print(f"Loss: {loss}")
```
output:
```text
Loss: 27.945
```

## 3、优化算法
优化算法的目的是找到最佳的模型参数（通常是权重和偏置），以最小化损失函数。线性回归中最常用的优化算法如下，

### 1）梯度下降（Gradient Descent）

【梯度下降】是一种优化算法，用于最小化损失函数。
最常用的优化算法之一，通过计算损失函数相对于模型参数的梯度来【更新】参数。

**【梯度】指示了损失函数增加的方向，因此通过向相反方向调整参数可以减少损失**。

```text
import numpy as np

# 生成一些随机数据
np.random.seed(42)
X = 2 * np.random.rand(100, 1)  # 输入特征
y = 4 + 3 * X + np.random.randn(100, 1)  # 目标变量

# 梯度下降参数
learning_rate = 0.01  # 学习率
n_iterations = 1000  # 迭代次数
m = len(X)  # 样本数量

# 初始化模型参数
w = np.random.randn(1, 1)  # 权重
b = np.random.randn()  # 偏差

# 梯度下降优化
for iteration in range(n_iterations):
    gradients_w = -2 / m * X.T.dot(y - X.dot(w) - b)
    gradients_b = -2 / m * np.sum(y - X.dot(w) - b)
    w = w - learning_rate * gradients_w
    b = b - learning_rate * gradients_b

print(f"Model parameters:\nWeight: {w[0][0]}\nBias: {b}")
```
output:
```text
Model parameters:
Weight: 2.7755308615708514
Bias: 4.208960473926773
```

### 2）随机梯度下降（Stochastic Gradient Descent, SGD）

随机梯度下降是一种优化算法，用于最小化训练数据集上的损失函数。
与传统的梯度下降不同，SGD每次迭代只选取一个样本来计算梯度，从而加快了计算速度，特别是在数据集很大时。

```text
import numpy as np

# 生成模拟数据
np.random.seed(0)
X = 2 * np.random.rand(100, 1)
y = 4 + 3 * X + np.random.randn(100, 1)

# 添加偏置项到输入变量
X_b = np.c_[np.ones((100, 1)), X]

# SGD参数
n_iterations = 50
t0, t1 = 5, 50  # 学习计划的超参数


# 学习率计划函数
def learning_schedule(t):
    return t0 / (t + t1)


# 随机初始化参数
theta = np.random.randn(2, 1)

# SGD实现
for iteration in range(n_iterations):
    for i in range(len(X_b)):
        random_index = np.random.randint(len(X_b))
        xi = X_b[random_index:random_index + 1]
        yi = y[random_index:random_index + 1]
        gradients = 2 * xi.T.dot(xi.dot(theta) - yi)
        eta = learning_schedule(iteration * len(X_b) + i)
        theta -= eta * gradients

print("Theta:", theta)
```
output:
```text
Theta: [[4.23027035]
 [2.97750696]]
```

### 3）小批量梯度下降（Mini-batch Gradient Descent）

小批量梯度下降（Mini-batch Gradient Descent）是一种优化算法，用于训练机器学习模型，特别是在进行线性回归时。
这种方法结合了批量梯度下降和随机梯度下降的特点，通过在每次迭代中使用一个小批量的数据（而不是整个数据集或单个数据点），以期达到更快的收敛速度和更高的稳定性。

```text
import numpy as np

# 生成模拟数据
np.random.seed(42)
X = 2 * np.random.rand(100, 1)
y = 4 + 3 * X + np.random.randn(100, 1)


def initialize_parameters(n):
    """初始化参数"""
    w = np.zeros((n, 1))
    b = 0
    return w, b


def compute_loss(X, y, w, b):
    """计算损失"""
    m = len(y)
    y_pred = X.dot(w) + b
    loss = (1 / (2 * m)) * np.sum((y_pred - y) ** 2)
    return loss


def compute_gradients(X, y, w, b):
    """计算梯度"""
    m = len(y)
    y_pred = X.dot(w) + b
    dw = (1 / m) * X.T.dot(y_pred - y)
    db = (1 / m) * np.sum(y_pred - y)
    return dw, db


def update_parameters(w, b, dw, db, learning_rate):
    """更新参数"""
    w = w - learning_rate * dw
    b = b - learning_rate * db
    return w, b


def mini_batch_gradient_descent(X, y, learning_rate=0.01, n_iterations=1000, batch_size=20):
    n_features = X.shape[1]
    w, b = initialize_parameters(n_features)
    for i in range(n_iterations):
        shuffled_indices = np.random.permutation(len(y))
        X_shuffled = X[shuffled_indices]
        y_shuffled = y[shuffled_indices]
        for j in range(0, len(y), batch_size):
            Xi = X_shuffled[j:j + batch_size]
            yi = y_shuffled[j:j + batch_size]
            dw, db = compute_gradients(Xi, yi, w, b)
            w, b = update_parameters(w, b, dw, db, learning_rate)
    return w, b


# 训练模型
w, b = mini_batch_gradient_descent(X, y)
print(f"模型参数: w = {w.flatten()}, b = {b}")
```
output:
```text
模型参数: w = [2.76960929], b = 4.214264874675579
```

### 4）正规方程（Normal Equation）

正规方程是一种数学公式，能够直接计算出线性回归模型的最佳参数（权重），而无需迭代优化（如梯度下降法）。公式如下，

```text
import numpy as np

# 假设X是特征矩阵，y是目标变量向量
# 为X添加一列1，用于计算偏置项（截距）
X = np.array([[1, 1], [1, 2], [2, 2], [2, 3]])
y = np.array([6, 8, 9, 11])
X_b = np.c_[np.ones((4, 1)), X]  # 添加一列1

# 使用正规方程计算最佳参数
theta_best = np.linalg.inv(X_b.T.dot(X_b)).dot(X_b.T).dot(y)

print("最佳参数 (截距和斜率):", theta_best)
```
output:
```text
最佳参数 (截距和斜率): [3. 1. 2.]
```