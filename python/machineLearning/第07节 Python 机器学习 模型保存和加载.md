# Python 机器学习 模型保存和加载

Python 机器学习中，模型保存和加载是两个非常重要的操作。

模型保存可以将训练好的模型保存到文件，以便以后使用。

模型加载可以将保存的文件加载到内存，以便进行预测或评估。

最常用保存和加模型的库包括pickle和joblib，另外在使用特定的机器学习库，如scikit-learn、TensorFlow或PyTorch时，它们也提供了自己的保存和加载机制。

## 1、pickle
pickle模块是Python的一部分，提供了一个简单的方式来序列化和反序列化一个Python对象结构。
训练好的模型通常需要被保存，以便于未来进行预测时能够直接加载使用，而不需要重新训练。

pickle模块是Python中一个常用的进行对象序列化和反序列化的模块，它可以将Python对象转换为字节流，从而能够将对象保存到文件中，或者从文件中恢复对象。

### 1）模型的保存

pickle.dump()方法用于将Python对象序列化并保存到文件中。常用参数如下，

| 参数              | 类型        | 描述                                                                    |
|-----------------|-----------|-----------------------------------------------------------------------|
| obj             | 对象        | 要被序列化的Python对象。                                                       |
| file            | 文件对象      | 一个打开的文件对象，必须以二进制写模式打开（'wb'）。                                          |
| protocol        | 整数/None   | 指定pickle数据格式的版本号。如果省略，则使用默认的协议。可选的协议版本号从0到5，其中更高的版本提供了更高的效率和新的功能。     |
| fix_imports     | 布尔值       | 仅在Python 2和Python 3之间的互操作性中使用。默认为True，为了使pickle文件在不同的Python版本间能够互相兼容。 |
| buffer_callback | 回调函数/None | 一个可选的回调函数，用于pickle协议版本5中，为了提供对大型数据的优化处理机制。仅在Python 3.8及以上版本中可用。       |

使用代码：
```text
import pickle

# 创建一个复杂的数据结构
my_data = {
    'name': 'Python',
    'version': 3.8,
    'features': ['Speed', 'Flexibility', 'Community'],
    'rank': 1
}

# 指定pickle文件的名称
filename = 'my_data.pickle'

# 使用最新的pickle协议版本进行序列化（Python 3.8及以上支持协议5）
protocol_version = pickle.HIGHEST_PROTOCOL

# 序列化对象到文件
with open(filename, 'wb') as file:
    # 使用dump()方法并指定协议版本
    # 在这个例子中，fix_imports默认即可，因为我们不考虑跨Python主版本的兼容性
    pickle.dump(my_data, file, protocol=protocol_version)

# 打开文件
with open(filename, 'r', errors='ignore') as f:
    # 读取文件内容
    content = f.read()

# 打印文件内容
print(content)
```

### 2）模型的加载

要使用pickle.load()方法，首先需要有一个已经以二进制模式打开的文件，该文件包含了之前使用pickle.dump()方法序列化的Python对象。
```text
import pickle

# 创建一个复杂的数据结构
my_data = {
    'name': 'Python',
    'version': 3.8,
    'features': ['Speed', 'Flexibility', 'Community'],
    'rank': 1
}

# 指定pickle文件的名称
filename = 'my_data.pickle'

# 使用最新的pickle协议版本进行序列化（Python 3.8及以上支持协议5）
protocol_version = pickle.HIGHEST_PROTOCOL

# 序列化对象到文件
with open(filename, 'wb') as file:
    # 使用dump()方法并指定协议版本
    # 在这个例子中，fix_imports默认即可，因为我们不考虑跨Python主版本的兼容性
    pickle.dump(my_data, file, protocol=protocol_version)

# 打开文件
with open(filename, 'r', errors='ignore') as f:
    # 读取文件内容
    content = f.read()

# 打印文件内容
print(content)
# 打开包含序列化数据的文件
with open(filename, 'rb') as file:
    my_object = pickle.load(file)
    print(my_object)
```

### 3）使用pickle保存和加载模型
```text
import pickle
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.datasets import load_iris
from sklearn.metrics import accuracy_score

# 加载示例数据集，如鸢尾花数据集
iris = load_iris()
X = iris.data
y = iris.target

# 使用train_test_split函数划分数据集
# 测试集占比30%，保持类别比例，设置随机种子为42以确保结果一致性
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42, stratify=y)

# 创建KNN分类器实例
knn = KNeighborsClassifier(
    n_neighbors=3,  # 设置邻居的数量为3
    weights='distance',  # 设置权重为距离的倒数
    algorithm='kd_tree',  # 使用KD树算法
    leaf_size=40,  # 设置KD树/球树的叶子大小
    p=1,  # 设置Minkowski距离的幂参数为1（曼哈顿距离）
    metric='euclidean'  # 使用欧氏距离作为距离度量
)

knn.fit(X_train, y_train)
# 预测和评估
y_pred = knn.predict(X_test)
print("Accuracy:", accuracy_score(y_test, y_pred))

# 使用pickle保存模型
with open('model.pkl', 'wb') as file:
    pickle.dump(knn, file)

# 使用pickle加载模型
with open('model.pkl', 'rb') as file:
    loaded_model = pickle.load(file)

# 使用加载的模型进行预测
predictions = loaded_model.predict(X_test)
print(predictions)
```

## 2、joblib
joblib是一个非常适合于大数据的序列化工具，特别是对于包含大量numpy数组的数据结构。
由于机器学习模型往往包含大量的numpy数组，因此joblib在保存和加载机器学习模型方面比pickle更加高效
。如未安装，则可以使用pip install joblib进行安装。

### 1）保存模型

使用joblib.dump方法可以将训练好的模型保存到文件中。常用参数如下，

| 参数          | 类型         | 描述                                                                                                          |
|-------------|------------|-------------------------------------------------------------------------------------------------------------|
| value       | Python对象	  | 要序列化的Python对象。可以是几乎任何类型的对象，如机器学习模型、numpy数组等。                                                                |
| filename    | 字符串	       | 用于存储序列化对象的文件名。可以是完整的文件路径。如果文件名以特定的压缩扩展名结尾（如.gz、.bz2、.xz或.lzma），则自动应用相应的压缩。                                  |
| compress	   | 布尔值/整数/元组	 | 控制文件压缩的选项。如果为布尔值且为True，使用默认的压缩方式（通常是compress=3）。如果为整数，指定压缩级别（0-9）。也可以是(compressor, level)形式的元组来精确控制压缩方式和级别。 |
| protocol	   | 整数	        | 指定用于序列化的pickle协议版本。如果为None，则使用默认的pickle协议。通过指定协议，可以帮助保持与旧版本Python的兼容性。                                      |
| cache_size	 | （已弃用）	     | 此参数在最新版本的joblib中已不再使用。                                                                                      |

使用代码：
```text
import numpy as np
from joblib import dump

# 创建一个Numpy数组
array = np.arange(100).reshape(10, 10)

# 保存数组到磁盘，不使用压缩
dump(array, 'array.joblib')

# 使用压缩保存数组到磁盘
# compress参数可以是一个整数，指定压缩级别。这里使用3作为压缩级别的示例。
dump(array, 'array_compressed.joblib', compress=3)

# 使用更细粒度的压缩控制，指定压缩方式和级别
# 例如，使用gzip压缩方式，压缩级别为9（最大压缩）
dump(array, 'array_finely_compressed.joblib', compress=('gzip', 9))
# 打开文件
with open('array_finely_compressed.joblib', 'r', errors='ignore') as f:
    # 读取文件内容
    content = f.read()

# 打印文件内容
print(content)
```

### 2）加载模型

使用joblib.load方法可以从文件加载之前保存的模型。
```text
import numpy as np
from joblib import dump, load

# 创建一个Numpy数组
array = np.arange(100).reshape(10, 10)

# 保存数组到磁盘，不使用压缩
dump(array, 'array.joblib')

# 使用压缩保存数组到磁盘
# compress参数可以是一个整数，指定压缩级别。这里使用3作为压缩级别的示例。
dump(array, 'array_compressed.joblib', compress=3)

# 使用更细粒度的压缩控制，指定压缩方式和级别
# 例如，使用gzip压缩方式，压缩级别为9（最大压缩）
dump(array, 'array_finely_compressed.joblib', compress=('gzip', 9))
# 打开文件
with open('array_finely_compressed.joblib', 'r', errors='ignore') as f:
    # 读取文件内容
    content = f.read()

# 打印文件内容
print(content)
# 打开包含序列化数据的文件
with open('array_finely_compressed.joblib', 'rb') as file:
    my_object = load(file)
    print(my_object)
```

### 3）使用joblib保存和加载模型
```text
from joblib import dump, load
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.datasets import load_iris
from sklearn.metrics import accuracy_score

# 加载示例数据集，如鸢尾花数据集
iris = load_iris()
X = iris.data
y = iris.target

# 使用train_test_split函数划分数据集
# 测试集占比30%，保持类别比例，设置随机种子为42以确保结果一致性
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42, stratify=y)

# 创建KNN分类器实例
knn = KNeighborsClassifier(
    n_neighbors=3,  # 设置邻居的数量为3
    weights='distance',  # 设置权重为距离的倒数
    algorithm='kd_tree',  # 使用KD树算法
    leaf_size=40,  # 设置KD树/球树的叶子大小
    p=1,  # 设置Minkowski距离的幂参数为1（曼哈顿距离）
    metric='euclidean'  # 使用欧氏距离作为距离度量
)

knn.fit(X_train, y_train)
# 预测和评估
y_pred = knn.predict(X_test)
print("Accuracy:", accuracy_score(y_test, y_pred))

# 使用pickle保存模型
with open('model.joblib', 'wb') as file:
    dump(knn, file)

# 使用pickle加载模型
with open('model.joblib', 'rb') as file:
    loaded_model = load(file)

# 使用加载的模型进行预测
predictions = loaded_model.predict(X_test)
print(predictions)
```

## 3、特定库的保存和加载机制
TensorFlow / Keras 使用model.save(filepath)保存模型，使用keras.models.load_model(filepath)加载模型。
PyTorch使用torch.save(model.state_dict(), filepath)保存模型的状态字典，使用model.load_state_dict(torch.load(filepath))加载模型状态字典。
每种方法都有其特定的使用场景和优缺点，选择合适的方法可以帮助更有效地管理和复用机器学习模型。