# Python 机器学习 线性回归 正则化线性模型

Python 机器学习中，正则化是一种减少模型过拟合的技术，通过在损失函数中添加一个正则化项来实现。

对于线性回归模型，常见的正则化方法有Lasso回归（L1正则化）、岭回归（L2正则化）和弹性网络回归（同时使用L1和L2正则化）。
这些方法可以调整模型的复杂度，提高模型的泛化能力。

## 1、欠拟合（Underfitting）和过拟合（Overfitting）
在机器学习中，欠拟合（Underfitting）和过拟合（Overfitting）是两种常见的问题，它们影响模型的泛化能力，即模型对新数据的预测能力。
理解欠拟合和过拟合以及如何解决这些问题是提高机器学习模型性能的关键。
通过适当的模型选择、调参和使用合适的数据处理技术，可以有效地管理这两种情况，提高模型的泛化能力。

### 1）欠拟合（Underfitting）

欠拟合发生在模型过于简单，不能捕捉到数据的基本结构时。
具体来说，模型没有足够的能力（或参数）来学习数据中的变化，导致它在训练集和测试集上都表现不佳。
欠拟合一般训练集和测试集的准确率都很低，模型过于简单，没有捕获数据的复杂性。

解决欠拟合的方法一般是使用更复杂的模型，如果可能，可以引入更多相关特征。如果使用了正则化（如L1、L2），减少正则化强度可能有助于模型学习更多的信息。

### 2）过拟合（Overfitting）

过拟合发生在模型过于复杂，以至于它不仅学习了数据的真实分布，也学习了噪声。
这种模型在训练集上表现很好，但是在新数据或测试集上表现不佳，因为它失去了泛化的能力。

过拟合一般表现为训练集上的准确率很高，但测试集的准确率低，模型过于复杂，捕捉了数据中的噪声。

解决过拟合的方法一般可以使用更多的数据可以帮助模型更好地泛化。
选择更简单的模型或减少模型复杂度（如降低多项式特征的度）。
增加正则化，如L1或L2正则化，可以限制模型权重的大小，防止模型过于依赖训练数据中的小波动。
通过交叉验证选择模型和参数，确保模型在不同的数据子集上都有良好的表现。
在决策树和深度学习等模型中，通过剪枝去除不必要的分支或层。

## 2、岭回归（Ridge Regression）
岭回归通过在损失函数中添加L2正则化项（系数的平方和）来约束模型的系数，从而防止系数值过大。

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
参考文档：Python 机器学习 线性回归和岭回归

## 3、Lasso回归（Lasso Regression）
Lasso回归通过在损失函数中添加L1正则化项（系数的绝对值之和）来实现，它可以将某些系数压缩至0，从而实现变量的选择。
Lasso回归通过【约束】【模型的系数】减少过拟合，提高模型的泛化能力。
Lasso回归可以通过Lasso类在sklearn.linear_model模块中实现。常用参数如下，

| 参数            | 描述                                                               |
|---------------|------------------------------------------------------------------|
| alpha         | 控制正则化项的强度。默认值为1.0。alpha越大，正则化效果越强，系数越倾向于0。                       |
| fit_intercept | 是否计算截距项。默认为True。如果为False，模型不会计算截距项。                              |
| normalize     | 是否在拟合前标准化输入变量（已弃用）。默认为False。建议使用StandardScaler进行标准化。             |
| precompute    | 是否使用预计算的Gram矩阵来加速计算。默认为'auto'。'auto'时，算法会基于数据的大小和特征的数量决定是否预计算。   |
| max_iter      | 优化算法的最大迭代次数。默认值为1000。                                            |
| tol           | 优化的容忍度。默认值为1e-4。 如果优化的迭代改进小于此值，算法将停止迭代。                          |
| selection     | 用于更新系数的策略。默认为'cyclic'。'cyclic'表示循环方式逐个更新特征的系数，'random'表示以随机顺序更新。 |

使用代码：
```text
from sklearn.linear_model import Lasso
from sklearn.datasets import make_regression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

# 生成一些回归数据
X, y = make_regression(n_samples=1000, n_features=20, noise=0.1, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 选择alpha=0.1，增加max_iter到5000，以及设置tol为1e-4
lasso = Lasso(alpha=0.1, max_iter=5000, tol=1e-4)

# 训练模型
lasso.fit(X_train, y_train)

# 预测测试集
y_pred = lasso.predict(X_test)

# 计算MSE
mse = mean_squared_error(y_test, y_pred)
print(f'Mean Squared Error: {mse}')

# 查看系数，注意一些系数可能为0，表示相应的特征被模型排除
print(f'Coefficients: {lasso.coef_}')
```
output:
```text
Mean Squared Error: 0.11693222305278325
Coefficients: [79.9133635  98.47386981  5.47230989  0.         86.35246698 -0.
 69.32668578  0.         -0.          0.         18.48774681 39.5317376
 -0.          2.98734092  0.         26.28231764 -0.         86.78228306
  0.          0.        ]
```

## 4、弹性网络回归（Elastic Net Regression）
弹性网络回归（Elastic Net Regression）是一种结合了岭回归（Ridge Regression）和套索回归（Lasso Regression）的线性回归模型，
通过正则化过程来改进模型的预测精度和解释能力。
弹性网络回归结合了L1和L2正则化，通过调整两者之间的比例，可以在岭回归和Lasso回归之间进行权衡。
ElasticNet类是一种结合了L1和L2先验作为正则项的线性回归模型。

常用参数如下，

| 参数             | 描述                                                                             |
|----------------|--------------------------------------------------------------------------------|
| alpha	         | 浮点数，默认为1.0。正则化项的强度，控制模型的复杂度。较大的值意味着更强的正则化。                                     |
| l1_ratio	      | 浮点数，默认为0.5。ElasticNet混合正则化中L1和L2的比例。l1_ratio=1为纯Lasso回归，l1_ratio=0为纯Ridge回归。   |
| fit_intercept	 | 布尔值，默认为True。是否计算截距。如果为False，无截距将被使用，在这种情况下，数据应该已经居中。                           |
| normalize	     | 布尔值，默认为False。在拟合模型之前是否对回归器X进行标准化。若fit_intercept=False，则忽略此参数。（0.24版本后，使用将发出警告） |
| max_iter	      | 整数，默认为1000。优化算法的最大迭代次数。                                                        |
| tol	           | 浮点数，默认为1e-4。优化的容忍度。如果优化的更新小于此值，则算法停止迭代。                                        |
| selection	     | 字符串，默认为'cyclic'。用于优化问题的变量选择方法。'cyclic'表示循环选择特征，'random'表示随机选择特征。               |

使用代码：
```text
from sklearn.linear_model import ElasticNet
from sklearn.datasets import make_regression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

# 生成回归数据集
X, y = make_regression(n_features=100, n_informative=10, noise=0.1, random_state=42)

# 划分数据集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 初始化弹性网络模型
elastic_net = ElasticNet(alpha=1.0, l1_ratio=0.5, fit_intercept=True, max_iter=1000, tol=1e-4, selection='cyclic')

# 训练模型
elastic_net.fit(X_train, y_train)

# 预测测试集
y_pred = elastic_net.predict(X_test)

# 计算MSE
mse = mean_squared_error(y_test, y_pred)
print("测试集上的均方误差：", mse)
```
output:
```text
测试集上的均方误差： 6450.0487653593345
```