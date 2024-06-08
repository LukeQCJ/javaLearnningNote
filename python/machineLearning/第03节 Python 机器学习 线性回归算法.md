# Python 机器学习 线性回归算法

线性回归是一种预测数值型数据的监督学习算法。
线性回归是统计学和机器学习中最基础且广泛应用的预测模型之一。
实现在建立自变量（X）和因变量（Y）之间的线性关系。
在 Python 的机器学习库 scikit-learn 中，可以方便地使用线性回归模型进行数据分析和预测。

## 1、理解线性回归
线性回归是统计学中最基本且广泛使用的预测模型之一，实现在研究自变量（X）和因变量（Y）之间的线性关系。
线性回归模型可以是简单的单变量线性回归，也可以是涉及多个自变量的多元线性回归线性回归模型试图学习系数 w 和截距 b，
使得预测值 y_pred = w * X + b 尽可能接近实际值 Y，通常通过最小化误差的平方和来求解 w 和 b。

线性回归广泛应用于经济学、生物学、工程学等领域，用于预测分析、趋势分析和变量之间关系的研究。
它是许多复杂预测模型的基础，理解线性回归对于深入学习更高级的机器学习和统计模型非常重要。

## 2、scikit-learn
scikit-learn 是 Python 中一个强大的机器学习库，它提供了各种常用机器学习算法的简单易用的实现。

使用 scikit-learn，可以快速进行数据预处理、模型训练、评估和预测，从而进行有效的机器学习分析。
使用 scikit-learn 库实现线性回归算法是一种快速、高效进行数据分析和预测的方法。

1）安装命令
```text
pip install scikit-learn
```

2）导入所需模块
```text
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, r2_score
```

## 3、数据集
糖尿病数据集（Diabetes）包含了442个病人的10个生理特征（年龄、性别、体重、血压等）和一年后的疾病进展情况。

目标变量是一种量化疾病进展的指标，这个数据集常用于测试和示范回归模型。

Python 的 scikit-learn 库中，可以直接加载并使用这个数据集。

糖尿病数据集的特征：

| 特征                    | 描述               |
|-----------------------|------------------|
| 年龄 (age)              | 年龄               |
| 性别 (sex)              | 性别               |
| 体质指数 (bmi)            | 体质指数             |
| 平均血压 (bp)             | 平均血压             |
| S1 (总血清胆固醇)           | 总血清胆固醇           |
| S2 (低密度脂蛋白胆固醇)        | 低密度脂蛋白胆固醇        |
| S3 (高密度脂蛋白胆固醇)        | 高密度脂蛋白胆固醇        |
| S4 (总胆固醇 / 高密度脂蛋白胆固醇) | 总胆固醇 / 高密度脂蛋白胆固醇 |
| S5 (血清甲状腺素水平的对数)      | 血清甲状腺素水平的对数      |
| S6 (一年后的血糖水平)         | 一年后的血糖水平         |

使用 scikit-learn 加载鸢尾花数据集代码如下，
```text
from sklearn.datasets import load_diabetes
import pandas as pd

# 加载数据集
diabetes = load_diabetes()

# 特征矩阵和目标数组
X, y = diabetes.data, diabetes.target

# 转换为 DataFrame 以便于观察
feature_names = diabetes.feature_names
df = pd.DataFrame(X, columns=feature_names)
df['Progression'] = y

# 显示前几行数据
print(df.head())
```
output:
```text
        age       sex       bmi  ...        s5        s6  Progression
0  0.038076  0.050680  0.061696  ...  0.019908 -0.017646        151.0
1 -0.001882 -0.044642 -0.051474  ... -0.068330 -0.092204         75.0
2  0.085299  0.050680  0.044451  ...  0.002864 -0.025930        141.0
3 -0.089063 -0.044642 -0.011595  ...  0.022692 -0.009362        206.0
4  0.005383 -0.044642 -0.036385  ... -0.031991 -0.046641        135.0

[5 rows x 11 columns]
```

## 4、划分数据集
使用train_test_split()函数将数据集划分为【训练集】和【测试集】，train_test_split()函数是Scikit-Learn库中一个非常重要的工具。

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
from sklearn.model_selection import train_test_split
from sklearn.datasets import load_diabetes

# 加载糖尿病数据集
diabetes = load_diabetes()
X, y = diabetes.data, diabetes.target

# 划分数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.3, random_state=42, shuffle=True, stratify=None)

# 输出划分后的数据集大小
print("训练集特征大小:", X_train.shape)
print("测试集特征大小:", X_test.shape)
print("训练集标签大小:", y_train.shape)
print("测试集标签大小:", y_test.shape)
```
output:
```text
训练集特征大小: (309, 10)
测试集特征大小: (133, 10)
训练集标签大小: (309,)
测试集标签大小: (133,)
```

## 5、训练线性回归模型
Python 中使用 scikit-learn 训练线性回归模型是一个直接且简单的过程。
线性回归是一种用于预测数值目标变量的线性模型，基于一个或多个自变量。它尝试建立自变量和目标变量之间的线性关系。
scikit-learn 中的 LinearRegression()类 用于拟合线性回归模型。

常用参数如下，

| 参数            | 类型   | 默认值        | 描述                                                                                  |
|---------------|------|------------|-------------------------------------------------------------------------------------|
| fit_intercept | bool | True       | 是否计算截距。如果为False，假设数据已经居中。                                                           |
| normalize     | bool | False（已弃用） | 在拟合之前归一化输入变量（如果fit_intercept=True）。**从0.24版本开始，此参数已弃用。** 建议使用 StandardScaler 进行标准化。 |
| copy_X        | bool | True       | 如果为True ，则会复制X；否则，可能会被覆盖。                                                           |
| n_jobs        | int  | None       | 用于计算的作业数。None 意味着1，除非在joblib.parallel_backend上下文中。-1意味着使用所有处理器。用于加速大型数据集的计算。        |
| positive      | bool | False      | 当设置为 True 时，强制系数为正数。适用于某些模型系数需要为正的场景。                                               |

使用代码：
```text
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, r2_score
from sklearn.preprocessing import StandardScaler

# 加载糖尿病数据集
diabetes = load_diabetes()
X, y = diabetes.data, diabetes.target

# 划分数据为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# （可选）使用StandardScaler进行数据标准化
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 初始化LinearRegression模型
# 注意：由于已经手动进行了数据标准化，这里的normalize参数将保持为False（它的默认值）
lr = LinearRegression(fit_intercept=True, copy_X=True, n_jobs=None, positive=False)

# 使用训练集训练模型
lr.fit(X_train_scaled, y_train)

# 使用测试集进行预测
y_pred = lr.predict(X_test_scaled)

# 评估模型性能
mse = mean_squared_error(y_test, y_pred)
r2 = r2_score(y_test, y_pred)

print(f"模型的均方误差(MSE): {mse:.2f}")
print(f"模型的R^2分数: {r2:.2f}")
```
output:
```text
模型的均方误差(MSE): 2900.17
模型的R^2分数: 0.45
```
