# Python 机器学习 SVM 数字识别器案例

数字识别是机器学习中的一个经典问题，通常使用MNIST数据集来进行实践。

MNIST数据集包含了手写数字的灰度图像，每张图像的大小为28x28像素，总共有10个类别（0到9）。

数据标准化对于SVM等基于距离计算的算法是非常重要的，它可以帮助改善模型的性能。
准确率是衡量分类模型性能的一个常用指标，但在实际应用中，还可以考虑其他指标，如混淆矩阵、精确率、召回率等。

## 1、MNIST数据集
MNIST 是一个手写数字（0到9）的大型数据集，广泛用于训练和测试在图像处理领域的机器学习模型。

Scikit-learn 提供了一些工具来加载和使用这些数据。
Scikit-learn 的 datasets 模块提供了加载流行数据集的功能，
但需要注意的是，Scikit-learn 自带的是 MNIST 数据集的小版本。
对于完整的 MNIST 数据集，我们通常会使用其他库（如 TensorFlow 或 PyTorch）来加载。

```text
from sklearn.datasets import load_digits
import matplotlib.pyplot as plt

digits = load_digits()
# 显示第一张图片及其标签
plt.imshow(digits.images[0], cmap='gray')
plt.title('Label: {}'.format(digits.target[0]))
plt.show()

# 数据集大小
print("数据集图片数: ", len(digits.images))
print("每张图片的维度: ", digits.images[0].shape)
```
output:
```text
数据集图片数:  1797
每张图片的维度:  (8, 8)
```

## 2、训练SVM模型
Python中训练一个支持向量机（SVM）模型通常涉及使用scikit-learn库，Python中最流行的机器学习库之一。
训练模型并进行预测后，使用适当的评估指标来评估模型的性能。

在分类任务中，常用的评估指标包括准确率（accuracy）、召回率（recall）、精确率（precision）和F1分数等。

使用scikit-learn训练SVM模型是一个直接且高效的过程。
关键步骤包括数据准备、选择合适的SVM模型和核函数、模型训练以及性能评估。
为了获得最佳性能，可能需要尝试不同的模型参数和核函数。

```text
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score

# 加载数据集
iris = datasets.load_iris()
X = iris.data
y = iris.target

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 数据标准化
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

# 创建SVM分类器实例
clf = SVC(kernel='linear', C=1.0, random_state=42)

# 训练模型
clf.fit(X_train, y_train)

# 预测测试集
y_pred = clf.predict(X_test)

# 评估模型
accuracy = accuracy_score(y_test, y_pred)
print(accuracy)
```
output:
```text
0.9666666666666667
```

## 3、实现数字识别器
SVM 数字识别器是一种使用 SVM 算法进行数字识别的系统。

SVM 算法是一种二分类算法，它通过找到最大间隔的超平面来将数据点划分为两类。
SVM 数字识别器的原理是将数字图像转换为特征向量，然后使用 SVM 算法对特征向量进行分类。

SVM 识别是使用 SVM 模型对新的数字图像进行识别的过程。
识别过程中，SVM 模型会将新的数字图像映射到特征空间，然后根据超平面来判断新的数字图像属于的类别。

```text
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score
import matplotlib.pyplot as plt

# 加载数据集
digits = datasets.load_digits()

# 获取特征和标签
X = digits.data
y = digits.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 数据标准化
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 创建SVM分类器
clf = SVC(kernel='linear')

# 训练模型
clf.fit(X_train_scaled, y_train)

# 预测测试集
y_pred = clf.predict(X_test_scaled)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')

# 可视化预测结果的前几个示例
_, axes = plt.subplots(nrows=1, ncols=4, figsize=(10, 3))
for ax, image, prediction in zip(axes, X_test, y_pred[:4]):
    ax.set_axis_off()
    image = image.reshape(8, 8)
    ax.imshow(image, cmap=plt.cm.gray_r, interpolation='nearest')
    ax.set_title(f'Prediction: {prediction}')
plt.show()
```
output:
```text
Accuracy: 0.975
```