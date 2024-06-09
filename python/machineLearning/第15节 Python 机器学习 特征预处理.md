# Python 机器学习 特征预处理

Python 中进行机器学习时，特征预处理是一个非常重要的步骤。
它涉及对原始数据进行处理，以使其更适合机器学习模型。
特征预处理的【目的】是**提高模型的性能和准确性**。
Python 中提供了多种数据预处理方法和工具，可以根据实际情况选择合适的方法进行处理。

## 1、缩放特征（Feature Scaling）
特征预处理是一个重要的步骤，而特征缩放（Feature Scaling）是其中的一个关键环节。

【特征缩放】通常用于【标准化】【数据集中各个特征的范围】，使它们在相似的尺度上。
这一步骤对于许多机器学习算法特别重要，尤其是那些基于距离的算法（如 K-近邻）和梯度下降法（如线性回归、逻辑回归、神经网络）。

### 1）最小-最大缩放 (Min-Max Scaling)

最小-最大缩放将所有特征缩放到一个给定的范围内，通常是 0 到 1。公式如下：
```text
import numpy as np


def min_max_scaling(X):
    """
    最小-最大缩放
    Args:
        X: 特征矩阵
    Returns:
        缩放后的特征矩阵
    """
    # 计算每个特征的最小值和最大值
    x_min = np.min(X, axis=0)  # axis = 0 表示在X特征矩阵的列上取最小值，axis = 1 表示在X特征矩阵的行上取最小值
    x_max = np.max(X, axis=0)  # axis 用法同min
    # 缩放特征
    X_scaled = (X - x_min) / (x_max - x_min)
    return X_scaled


# 示例
X = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])

X_scaled = min_max_scaling(X)
print(X_scaled)
```
output:
```text
[[0.  0.  0. ]
 [0.5 0.5 0.5]
 [1.  1.  1. ]]
```

使用 MinMaxScaler 类来实现：
```text
from sklearn.preprocessing import MinMaxScaler
import numpy as np

# 示例数据
data = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]], dtype=np.float64)

# 创建缩放器并应用
scaler = MinMaxScaler()
scaled_data = scaler.fit_transform(data)
print(scaled_data)
```
output:
```text
[[0.  0.  0. ]
 [0.5 0.5 0.5]
 [1.  1.  1. ]]
```

### 2）标准化 (Standardization)缩放

标准化将特征缩放，使其具有零均值和单位方差。公式如下：
```text
import numpy as np


def standardize(X):
    """
    对数据进行标准化
    Args:
        X: 输入数据，numpy 数组
    Returns:
        标准化后的数据
    """
    # 计算均值和标准差
    mu = np.mean(X, axis=0)
    sigma = np.std(X, axis=0)
    # 标准化
    return (X - mu) / sigma


# 示例
X = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
Z = standardize(X)
print(Z)
```
output:
```text
[[-1.22474487 -1.22474487 -1.22474487]
 [ 0.          0.          0.        ]
 [ 1.22474487  1.22474487  1.22474487]]
```

使用 StandardScaler 类：
```text
from sklearn.preprocessing import StandardScaler
import numpy as np

# 示例数据
data = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]], dtype=np.float64)

# 创建缩放器并应用
scaler = StandardScaler()
scaled_data = scaler.fit_transform(data)
print(scaled_data)
```
output:
```text
[[-1.22474487 -1.22474487 -1.22474487]
 [ 0.          0.          0.        ]
 [ 1.22474487  1.22474487  1.22474487]]
```

### 3）范数缩放 (Normalization)

范数缩放通常是将每个样本缩放到单位范数（每个样本的向量长度为1）。可以使用 Normalizer 类来实现：
```text
from sklearn.preprocessing import Normalizer
import numpy as np

# 示例数据
data = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]], dtype=np.float64)

# 创建缩放器并应用
scaler = Normalizer()
scaled_data = scaler.fit_transform(data)
print(scaled_data)
```
output:
```text
[[0.26726124 0.53452248 0.80178373]
 [0.45584231 0.56980288 0.68376346]
 [0.50257071 0.57436653 0.64616234]]
```

## 2、缺失值处理
特征预处理其中一个关键的环节是处理缺失值。
缺失值会影响模型的性能，因此需要采取适当的策略来处理它们。

Python 中有多种方法可以处理缺失值，主要使用 Pandas 和 Scikit-learn 库。

如果数据集不是很大，并且缺失值不是太多，可以考虑直接删除含有缺失值的行或列。
也可以用一些具体的值来填充缺失值，例如用平均值、中位数或众数等。

Scikit-learn 提供了 SimpleImputer 类，这是处理缺失值的另一种常用方法。在某些情况下，可能需要使用更复杂的方法来填充缺失值。

```text
import pandas as pd
from sklearn.impute import SimpleImputer
import numpy as np

# 假设 df 是一个包含缺失值的 DataFrame
# 示例数据
data = {
    'Feature1': [1, np.nan, 3, 4, 5],
    'Feature2': [6, 7, np.nan, 9, 10],
    'Feature3': [11, 12, 13, np.nan, 15]
}
df = pd.DataFrame(data)

# 删除含有缺失值的行
df_dropped_rows = df.dropna()
print("Dropped rows:")
print(df_dropped_rows)

# 删除含有缺失值的列
df_dropped_columns = df.dropna(axis=1)
print("Dropped columns:")
print(df_dropped_columns)

# 用平均值填充
df_filled_mean = df.fillna(df.mean())
print("Filled mean:")
print(df_filled_mean)

# 用中位数填充
df_filled_median = df.fillna(df.median())
print("Filled median:")
print(df_filled_median)

# 用众数填充
df_filled_mode = df.fillna(df.mode().iloc[0])
print("Filled mode:")
print(df_filled_mode)

# 使用 SimpleImputer 用平均值填充
imputer_mean = SimpleImputer(missing_values=np.nan, strategy='mean')
df_imputed_mean = pd.DataFrame(imputer_mean.fit_transform(df), columns=df.columns)
print("Imputed mean:")
print(df_imputed_mean)

# 使用 SimpleImputer 用中位数填充
imputer_median = SimpleImputer(missing_values=np.nan, strategy='median')
df_imputed_median = pd.DataFrame(imputer_median.fit_transform(df), columns=df.columns)
print("Imputed median:")
print(df_imputed_median)

# 使用 SimpleImputer 用众数填充
imputer_mode = SimpleImputer(missing_values=np.nan, strategy='most_frequent')
df_imputed_mode = pd.DataFrame(imputer_mode.fit_transform(df), columns=df.columns)
print("Imputed mode:")
print(df_imputed_mode)
```
output:
```text
Dropped rows:
   Feature1  Feature2  Feature3
0       1.0       6.0      11.0
4       5.0      10.0      15.0
Dropped columns:
Empty DataFrame
Columns: []
Index: [0, 1, 2, 3, 4]
Filled mean:
   Feature1  Feature2  Feature3
0      1.00       6.0     11.00
1      3.25       7.0     12.00
2      3.00       8.0     13.00
3      4.00       9.0     12.75
4      5.00      10.0     15.00
Filled median:
   Feature1  Feature2  Feature3
0       1.0       6.0      11.0
1       3.5       7.0      12.0
2       3.0       8.0      13.0
3       4.0       9.0      12.5
4       5.0      10.0      15.0
Filled mode:
   Feature1  Feature2  Feature3
0       1.0       6.0      11.0
1       1.0       7.0      12.0
2       3.0       6.0      13.0
3       4.0       9.0      11.0
4       5.0      10.0      15.0
Imputed mean:
   Feature1  Feature2  Feature3
0      1.00       6.0     11.00
1      3.25       7.0     12.00
2      3.00       8.0     13.00
3      4.00       9.0     12.75
4      5.00      10.0     15.00
Imputed median:
   Feature1  Feature2  Feature3
0       1.0       6.0      11.0
1       3.5       7.0      12.0
2       3.0       8.0      13.0
3       4.0       9.0      12.5
4       5.0      10.0      15.0
Imputed mode:
   Feature1  Feature2  Feature3
0       1.0       6.0      11.0
1       1.0       7.0      12.0
2       3.0       6.0      13.0
3       4.0       9.0      11.0
4       5.0      10.0      15.0
```

## 3、编码分类特征
当数据集包含分类（非数值）特征时，通常需要对这些特征进行编码，以便机器学习算法能够处理它们。

分类特征的编码主要有两种方式：**标签编码（Label Encoding）** 和 **独热编码（One-Hot Encoding）**。

### 1）标签编码

标签编码将每个类别映射到一个整数。这种方法的一个问题是，它可能导致算法错误地假设数据中的数字有序。

```text
from sklearn.preprocessing import LabelEncoder

# 假设有一个分类特征
categories = ['red', 'blue', 'green']

# 创建 LabelEncoder 对象
label_encoder = LabelEncoder()

# 应用标签编码
encoded_labels = label_encoder.fit_transform(categories)
print(encoded_labels)
```
output:
```text
[2 0 1]
```

### 2）独热编码

独热编码创建了一个二进制列，用于每个类别。对于类别的每个值，独热编码都会创建一个新的列（特征），其中只有一个是 1，其他都是 0。

使用scikit-learn：
```text
from sklearn.preprocessing import OneHotEncoder
import numpy as np

# 示例数据
categories = np.array([['Red'], ['Green'], ['Blue'], ['Green'], ['Red']])

# 创建 OneHotEncoder 实例
encoder = OneHotEncoder()

# 训练编码器并转换数据
one_hot_encoded = encoder.fit_transform(categories)

print(one_hot_encoded)
```
output:
```text
  (0, 2)	1.0
  (1, 1)	1.0
  (2, 0)	1.0
  (3, 1)	1.0
  (4, 2)	1.0
```

使用 pandas:
```text
import pandas as pd

# 示例数据
df = pd.DataFrame({'Color': ['Red', 'Green', 'Blue', 'Green', 'Red']})

# 使用 pandas 的 get_dummies 方法进行独热编码
one_hot_encoded = pd.get_dummies(df)

print(one_hot_encoded)
```
output:
```text
   Color_Blue  Color_Green  Color_Red
0       False        False       True
1       False         True      False
2        True        False      False
3       False         True      False
4       False        False       True
```

## 4、生成多项式特征
生成多项式特征是一种常见的预处理方法，特别是在使用线性模型时。

【多项式特征】可以帮助模型【捕捉数据中的非线性关系】。

特别适用于【提高线性模型的复杂度】，使其能够学习更复杂的数据模式。

```text
from sklearn.preprocessing import PolynomialFeatures
import numpy as np

# 示例数据
X = np.array([[2], [3], [4]])
# 创建一个度为 2 的 PolynomialFeatures 转换器
poly = PolynomialFeatures(degree=2)
# 转换数据
X_poly = poly.fit_transform(X)
# 输出结果
print(X_poly)
```
output:
```text
[[ 1.  2.  4.]
 [ 1.  3.  9.]
 [ 1.  4. 16.]]
```

## 5、使用 Python 进行特征预处理
scikit-learn 库提供了大量的工具来进行特征预处理。
使用鸢尾花数据集，并人为添加了一些分类特征和缺失值。
然后定义了一个预处理管道，其中包括对数值特征的标准化和对分类特征的独热编码。
使用随机森林分类器构建了一个模型，并在训练数据上训练它，然后在测试数据上评估其性能。代码如下，

```text
import numpy as np
import pandas as pd
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.impute import SimpleImputer
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

# 加载鸢尾花数据集
iris = load_iris()
X = pd.DataFrame(iris.data, columns=iris.feature_names)
y = iris.target

# 假设我们添加一些包含缺失值和分类特征的数据
# 添加分类特征
X['flower_category'] = np.random.choice(['Category 1', 'Category 2', 'Category 3'], size=len(X))

# 人为添加一些缺失值
X.iloc[::10, 1] = np.nan

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 定义数值特征和分类特征
numeric_features = iris.feature_names
categorical_features = ['flower_category']

# 创建预处理步骤
numeric_transformer = Pipeline(steps=[
    ('imputer', SimpleImputer(strategy='median')),
    ('scaler', StandardScaler())])

categorical_transformer = Pipeline(steps=[
    ('imputer', SimpleImputer(strategy='constant', fill_value='missing')),
    ('onehot', OneHotEncoder(handle_unknown='ignore'))])

# 组合预处理步骤
preprocessor = ColumnTransformer(
    transformers=[
        ('num', numeric_transformer, numeric_features),
        ('cat', categorical_transformer, categorical_features)])

# 创建完整的训练管道
pipeline = Pipeline(steps=[('preprocessor', preprocessor),
                           ('classifier', RandomForestClassifier(random_state=42))])

# 训练模型
pipeline.fit(X_train, y_train)

# 进行预测
y_pred = pipeline.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f"Model accuracy: {accuracy:.4f}")
```
output:
```text
Model accuracy: 0.9778
```