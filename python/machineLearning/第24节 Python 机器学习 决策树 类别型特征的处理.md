# Python 机器学习 决策树 类别型特征的处理

Python 机器学习中，特别是在决策树算法的应用中，处理类别型特征是一个非常重要的步骤。
类别型特征（也称为分类变量）是指那些取值为固定几个类别的特征，例如性别（男/女）、颜色（红/蓝/绿）等。

相对于数值型特征，类别型特征不能直接用于大多数机器学习模型的数学计算，因此需要通过某些方法转换成模型能够处理的形式。

## 1、标签编码（Label Encoding）
标签编码是将每个类别标签转换为一个整数值的方法。
处理类别型特征时，我们经常需要将文本标签转换为模型可以理解的数值形式。

标签编码（Label Encoding）是一种在预处理类别型特征时常用的技术，它将每个类别标签映射到一个整数。
若有一个颜色特征包含三个类别：红、蓝、绿。
通过标签编码，这些类别可能分别被编码为0、1、2。
这种方法简单直接，但有一个主要缺点：转换后的数值可能会被模型误解为具有某种顺序或大小关系，这在实际类别之间不存在时可能导致模型性能下降。

Python 的scikit-learn库提供了一个LabelEncoder类，用于执行标签编码。

常用方法如下，

| 方法/属性	                | 描述                                                                         |
|-----------------------|----------------------------------------------------------------------------|
| fit(y)	               | 学习输入的一维数组y中所有唯一类别标签与整数之间的映射关系。                                             |
| transform(y)	         | 将基于fit方法学到的映射关系应用于输入数组y，将类别标签转换为整数标签。                                      |
| fit_transform(y)	     | 组合了fit和transform的功能，首先学习类别与整数的映射关系，然后将这个映射应用于输入数组y，一步到位地完成学习映射和转换标签的过程。    |
| inverse_transform(y)	 | 将整数标签数组y转换回原始的类别标签，这在将模型预测的输出转换为可解释的类别标签时非常有用。                             |
| classes_	             | 在调用fit方法之后可用，包含按字典顺序排列的唯一类别标签。这显示了LabelEncoder学习到的类别。用于查看所有的类别标签及其对应的整数映射。 |

使用代码，
```text
from sklearn.preprocessing import LabelEncoder

# 示例类别型特征
categories = ['dog', 'cat', 'bird', 'dog', 'cat']

# 创建LabelEncoder对象
encoder = LabelEncoder()

# 训练编码器并转换特征
encoded_labels = encoder.fit_transform(categories)
print("Encoded labels:", encoded_labels)
print("Classes:", encoder.classes_)

# 解码
decoded_labels = encoder.inverse_transform(encoded_labels)
print("Decoded labels:", decoded_labels)
```
output:
```text
Encoded labels: [2 1 0 2 1]
Classes: ['bird' 'cat' 'dog']
Decoded labels: ['dog' 'cat' 'bird' 'dog' 'cat']
```

## 2、独热编码（One-Hot Encoding）
在处理决策树模型时，经常会遇到类别型（categorical）特征。这些特征通常是非数值型的，例如文本或标签数据。

为了使这些特征能够在机器学习模型中被有效处理，我们通常需要将它们转换为数值型数据。

独热编码（One-Hot Encoding）是一种常用的处理方法，它可以将类别型特征转换为数值型特征，使得模型能够理解并利用这些数据。
独热编码是将类别型特征转换为一系列二进制列的过程，每个数值被转换为一个全是0和一个是1的向量。
向量的长度等于该特征的类别数量，其中一个位置标记为1（表示该类别为当前特征值），其余位置为0。

可以使用scikit-learn库中OneHotEncoder来实现独热编码。

常用参数如下，

| 参数	             | 类型及说明                                                                       |
|-----------------|-----------------------------------------------------------------------------|
| categories	     | 'auto' 或二维数组: 指定每个特征的类别。默认为 'auto'，意味着类别将从训练数据中自动推断。也可以手动指定。                |
| drop	           | None, 'first', 数组, 'if_binary':指定是否以及如何从每个特征的独热编码类别中删除某个类别以避免共线性。           |
| dtype	          | 数据类型: 指定输出矩阵的数据类型，默认为 numpy.float64。可以根据需要改变。                               |
| handle_unknown	 | 'error' 或 'ignore':指定当遇到未知的类别时如何处理。默认为 'error'，抛出错误；'ignore' 时，用全0向量表示未知类别。 |

使用代码，
```text
from sklearn.preprocessing import OneHotEncoder
import numpy as np

# 示例数据，包含两个特征
data = np.array([
    ['红色', '小型'],
    ['绿色', '中型'],
    ['蓝色', '大型'],
    ['绿色', '小型'],
    ['红色', '大型']
])

# 创建OneHotEncoder实例
# - categories='auto' 表示自动从数据中推断类别
# - drop=None 表示不从独热编码中删除任何类别
# - sparse=False 输出密集矩阵
# - dtype=np.float32 指定输出数据类型为 float32
# - handle_unknown='ignore' 遇到未知类别时，进行忽略处理
encoder = OneHotEncoder(categories='auto', drop=None, dtype=np.float32, handle_unknown='ignore')

# 训练编码器并转换数据
encoded_data = encoder.fit_transform(data)

# 打印转换后的数据
print("转换后的数据:")
print(encoded_data)

# 打印特征名称，以了解每列对应的是哪个原始特征的哪个类别
print("\n特征名称:")
print(encoder.get_feature_names(input_features=['颜色', '大小']))
```
output:
```text
转换后的数据:
  (0, 0)	1.0
  (0, 5)	1.0
  (1, 1)	1.0
  (1, 3)	1.0
  (2, 2)	1.0
  (2, 4)	1.0
  (3, 1)	1.0
  (3, 5)	1.0
  (4, 0)	1.0
  (4, 4)	1.0

特征名称:
['颜色_红色' '颜色_绿色' '颜色_蓝色' '大小_中型' '大小_大型' '大小_小型']
```

## 3、二进制编码
二进制编码（Binary Encoding）是处理类别型特征的一种方法，
它是一种介于独热编码（One-Hot Encoding）和标签编码（Label Encoding）之间的方法。
二进制编码首先将类别特征转换为整数，然后将这些整数转换为二进制形式，最后将二进制的每位数作为一个独立的特征。
此方法相比独热编码可以显著减少特征的数量，但仍保持了一定程度的区分能力。

```text
from sklearn.preprocessing import LabelEncoder
import pandas as pd

# 示例数据
data = {'category': ['dog', 'cat', 'fish', 'dog', 'cat']}
df = pd.DataFrame(data)

# 使用LabelEncoder进行标签编码
label_encoder = LabelEncoder()
integer_encoded = label_encoder.fit_transform(df['category'])

# 将整数转换为二进制编码
binary_encoded = [bin(i)[2:] for i in integer_encoded]  # 将整数转换为二进制字符串，并去除前缀'0b'

# 确保所有二进制编码长度一致，补零
max_length = max(len(i) for i in binary_encoded)
binary_encoded = [i.zfill(max_length) for i in binary_encoded]

# 转换为DataFrame
binary_encoded_df = pd.DataFrame([list(i) for i in binary_encoded], columns=[f'bit_{j}' for j in range(max_length)])
print(binary_encoded_df)
```
output:
```text
  bit_0 bit_1
0     0     1
1     0     0
2     1     0
3     0     1
4     0     0
```