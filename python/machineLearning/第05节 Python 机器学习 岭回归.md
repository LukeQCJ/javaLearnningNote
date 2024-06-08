# Python 机器学习 岭回归

Python 机器学习中，机器学习领域的线性回归和岭回归是两种常用的回归分析方法，用于预测一个或多个自变量（或称为特征）和因变量（或称为目标变量）之间的关系。
这两种方法都试图找到最佳的线性组合来预测目标变量，但它们在处理数据的方法上有所不同。

线性回归和岭回归都是常用的线性回归模型。
- 线性回归简单易理解，但容易过拟合。
- 岭回归可以解决过拟合问题，但会增加模型偏差。
在选择模型时，需要根据实际情况进行权衡。

## 1、岭回归
岭回归（Ridge Regression）是线性回归的一种改进，主要用于处理特征之间存在多重共线性的情况。
多重共线性意味着输入特征之间高度相关，这可能会导致线性回归模型的参数估计非常不稳定，使模型过于敏感于训练数据中的随机误差。公式代码如下，

```text
import numpy as np
from numpy.linalg import inv


def ridge_regression(X, y, lambda_):
    """
    岭回归实现
    :param X: 特征矩阵，shape (n_samples, n_features)
    :param y: 目标变量，shape (n_samples,)
    :param lambda_: 正则化系数
    :return: 岭回归系数
    """
    # 增加一个维度以便能够计算截距项
    X = np.hstack([np.ones((X.shape[0], 1)), X])
    # 计算系数 (w)：(X^T X + λI)^(-1) X^T y
    I = np.eye(X.shape[1])  # 单位矩阵
    w = inv(X.T.dot(X) + lambda_ * I).dot(X.T).dot(y)
    return w


# 示例数据
X = np.array([[1, 2], [3, 4], [5, 6]])
y = np.array([1, 2, 3])
lambda_ = 1  # 正则化系数

# 岭回归计算
coefficients = ridge_regression(X, y, lambda_)
print("岭回归系数:", coefficients)
```
output:
```text
岭回归系数: [0.10778443 0.20359281 0.31137725]
```

Python的机器学习库scikit-learn中，岭回归是通过Ridge类实现的。
岭回归（Ridge Regression）是一种用于多元回归的技术，尤其适用于当数据点比变量少或存在多重共线性（即输入变量高度相关）的情况。
通过引入正则化项（L2惩罚项）来限制参数的大小，从而避免过拟合，提高模型的泛化能力。

常用参数如下，

| 参数	            | 描述                                                                            |
|----------------|-------------------------------------------------------------------------------|
| alpha	         | 正则化强度；必须是正数，默认值为1.0。较大的值表示更强的正则化。                                             |
| fit_intercept	 | 是否计算截距项。如果设置为False，预计数据已居中。默认值为True。                                          |
| normalize	     | 在拟合之前是否应该标准化输入变量。从0.24版本开始，建议使用StandardScaler进行标准化。默认值为False。                 |
| copy_X	        | 是否应该复制X值，或直接在原始数据上操作。如果设置为False，原始数据可能会被覆盖。默认值为True。                          |
| max_iter	      | 求解器的最大迭代次数。对于sag求解器，这表示求解器运行的最大迭代次数。默认值依求解器而异。                                |
| tol	           | 求解器的收敛标准。如果在一次迭代中估计的自变量的变化小于此值，则认为已经收敛。                                       |
| solver	        | 用于计算的求解器，包括'auto'、'svd'、'cholesky'、'lsqr'、'sparse_cg'、'sag'和'saga'。默认为'auto'。 |
| random_state	  | 当solver为sag或saga时，用于随机数生成器的种子。                                                |

使用代码，
```text
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.linear_model import Ridge
from sklearn.metrics import mean_squared_error
from sklearn.preprocessing import StandardScaler

# 2. 加载糖尿病数据集
X, y = load_diabetes(return_X_y=True)

# 3. 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 3.5 对数据进行标准化处理（可选，但推荐）
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 4. 初始化Ridge回归模型并设置参数
ridge_reg = Ridge(alpha=1.0, fit_intercept=True, solver='auto', random_state=42)

# 5. 训练模型
ridge_reg.fit(X_train_scaled, y_train)

# 6. 评估模型性能
y_pred = ridge_reg.predict(X_test_scaled)
mse = mean_squared_error(y_test, y_pred)
print(f"Mean Squared Error: {mse}")
```
output:
```text
Mean Squared Error: 2891.9980521084167
```

## 2、线性回归和岭回归对比
线性回归和岭回归都是用于数据分析和预测的回归方法，
主要区别在于处理多重共线性和正则化的方式，线性回归适用于变量之间不存在或较少共线性的情况。
岭回归适用于变量之间存在高度共线性或者当你需要通过正则化来防止过拟合的情况。当数据集较小，变量之间相对独立时，可以优先考虑使用线性回归。
当面对高维数据（特征多）或特征之间存在多重共线性时，岭回归是一个更好的选择，因为它可以提高模型的稳定性和泛化能力。
通过引入正则化项，岭回归不仅解决了多重共线性问题，而且还有助于提高模型在未知数据上的预测能力，是处理复杂数据集的有力工具。

| 方面   | 线性回归      | 岭回归       |
|------|-----------|-----------|
| 优点   | 简单易理解     | 可以解决过拟合问题 |
| 缺点   | 容易过拟合     | 会导致模型偏差增加 |
| 应用场景 | 数据量小、特征数少 | 数据量大、特征数多 |