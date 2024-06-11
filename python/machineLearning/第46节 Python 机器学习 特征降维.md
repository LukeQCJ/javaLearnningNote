# Python 机器学习 特征降维

机器学习领域，特征降维是一种减少数据集中变量数量的技术，以便更容易地进行可视化、数据处理和模型训练。
特征降维可以帮助提高模型的训练效率，减少计算成本，同时有时还能提高模型的性能。

## 1、主成分分析（PCA）
主成分分析（PCA，Principal Component Analysis）是最常见的线性特征降维方法。
PCA通过线性变换将原始数据转换到新的坐标系统中，使得这一数据集的第一个坐标轴上的方差最大（即第一个主成分），
第二个坐标轴上的方差次之，以此类推。这样，PCA可以用较少的维度来尽可能多地保留原始数据的信息。

主成分分析（PCA）首先是数据标准化，确保每个特征缩放到具有单位方差和零均值，以便平等地考虑所有特征。
接下来，构建数据的协方差矩阵来识别不同特征之间的关系，这是发现数据中主要变化方向的基础。
随后，通过计算协方差矩阵的特征值和对应的特征向量，可以找到PCA转换的最优方向。
这些特征向量指示了数据中最重要的结构，而特征值的大小反映了它们各自的重要性。
根据这些特征值的大小，选择最重要的n个特征向量来构成一个投影矩阵，这个矩阵定义了从原始特征空间到新的特征子空间的转换。
最后，这个投影矩阵被用来将原始数据转换到新的特征子空间，从而完成数据的降维，同时保留了数据中最关键的变异信息。

scikit-learn库中，PCA类的常用参数如下，

| 参数名            | 说明                                                                                                      |
|----------------|---------------------------------------------------------------------------------------------------------|
| n_components   | 类型: int,float, None, 'mle'; 默认值: None。指定要保留的主成分数量。可以是具体数值、保留的方差比例、'mle'，或未指定（保留所有特征）。                   |
| copy           | 类型: bool; 默认值: True。是否在运行算法前复制数据。如果为 False，原始数据会被覆盖。                                                    |
| whiten         | 类型: bool; 默认值: False。是否对数据进行白化，使输出的所有主成分具有相同的方差。                                                        |
| svd_solver     | 类型: 'auto', 'full', 'arpack', 'randomized'; 默认值: 'auto'。选择使用的SVD求解器类型。根据数据类型和 n_components 自动选择，或指定求解器。 |
| tol            | 类型: float; 默认值: 0.0。仅当svd_solver='arpack' 时使用，表示奇异值分解的容忍度。                                              |
| iterated_power | 类型: int或'auto'; 默认值: 'auto'。 随机SVD求解器的幂方法的迭代次数。                                                         |
| random_state   | 类型: int, RandomState, None; 默认值: None。随机数生成器的种子，或RandomState实例，或None。在某些 svd_solver 下使用。                |

使用代码，
```text
from sklearn.decomposition import PCA
from sklearn.datasets import load_iris
import matplotlib.pyplot as plt

# 加载示例数据集
data = load_iris()
X = data.data
y = data.target

# 创建PCA对象，指定保留的主成分数量为2
# 同时演示了如何设置svd_solver和random_state参数
pca = PCA(n_components=2, svd_solver='randomized', random_state=42)

# 拟合数据并应用降维
X_pca = pca.fit_transform(X)

# 可视化降维后的数据
plt.figure(figsize=(8, 6))
for target, color in zip(range(3), ['r', 'g', 'b']):
    plt.scatter(X_pca[y == target, 0], X_pca[y == target, 1], color=color, label=data.target_names[target])
plt.legend()
plt.xlabel('Principal Component 1')
plt.ylabel('Principal Component 2')
plt.title('PCA of IRIS dataset')
plt.show()
```

## 2、线性判别分析（LDA）
线性判别分析（Linear Discriminant Analysis，LDA）是一种常用于降维的技术，特别是在分类问题中。
LDA不仅用于降维，还可以用于分类。
它试图找到一个能够最大化类间差异和最小化类内差异的特征子空间。

与主成分分析（PCA）不同，LDA是一种监督学习算法，需要利用标签信息来指导降维过程。

LDA的核心思想是最大化不同类别数据之间的距离同时最小化同一类别数据之间的距离。
这通过计算类内散布矩阵和类间散布矩阵来实现，然后找到一个线性组合（或转换矩阵），使得当数据被映射到一个新的空间后，达到上述目标。

线性判别分析（LDA）是一种用于降维和分类的统计方法，其过程首先涉及计算类内散布矩阵，以衡量同一类别中数据点与其平均点之间的差异，
反映了同类数据的分散程度。随后，计算类间散布矩阵来捕捉不同类别数据点平均点之间的差异，从而反映类别间的分离度。
这两个矩阵被组合起来构建一个比例矩阵，用于衡量类内差异与类间差异的比例。
接下来，对该散布矩阵的比例进行特征值分解，以找出能最大化类间分散度与类内分散度比例的方向。
根据特征值的大小，选择最重要的特征向量，这些向量定义了降维后的新空间方向。
最后，利用这些选定的特征向量将原始数据映射到新的降维空间，实现数据的有效分类与降维。
这个过程旨在通过最大化类别间差异性和最小化类别内差异性来提升数据的分类性能。

scikit-learn中，LDA类的常用参数如下，

| 参数               | 类型      | 默认值    | 描述                                                         |
|------------------|---------|--------|------------------------------------------------------------|
| n_components     | 整数      | None   | 降维后的维数（要保留的成分数）。默认情况下，为类别数减一（n_classes - 1）。               |
| solver           | 字符串     | 'svd'  | 求解LDA的算法。可选值：'svd', 'lsqr', 'eigen'。'svd'不计算协方差矩阵，适用于高维数据。 |
| shrinkage        | 字符串或浮点数 | None   | 用于'lsqr'和'eigen'求解器的正则化参数。可以是'auto'或0到1之间的浮点数。             |
| priors           | 数组      | None   | 类别的先验概率。形状为(n_classes,)。如果未指定，则根据数据自动调整。                   |
| store_covariance | 布尔值     | False  | 是否计算并存储类内协方差矩阵或精度矩阵（如果使用了收缩）。仅当solver='lsqr'或'eigen'时可用。   |
| tol              | 浮点数     | 0.0001 | 奇异值分解的奇异值阈值，用于秩估计。仅当solver='svd'时可用。                       |

使用代码，
```text
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.neighbors import KNeighborsClassifier

# 加载Iris数据集
X, y = load_iris(return_X_y=True)

# 分割数据集为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 初始化LDA模型
lda = LinearDiscriminantAnalysis(n_components=2, solver='svd')

# 拟合模型并降维
X_train_lda = lda.fit_transform(X_train, y_train)
X_test_lda = lda.transform(X_test)

# 使用降维后的数据训练KNN分类器
knn = KNeighborsClassifier(n_neighbors=3)
knn.fit(X_train_lda, y_train)

# 预测测试集
y_pred = knn.predict(X_test_lda)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'准确率: {accuracy}')
```
output:
```text
准确率: 1.0
```

## 3、t-分布随机邻域嵌入（t-SNE）
t-分布随机邻域嵌入（t-SNE）是一种强大的机器学习算法，用于可视化和降维高维数据。
它由Laurens van der Maaten和Geoffrey Hinton在2008年提出。
t-SNE通过在低维空间中模拟高维数据点之间的相似性，有效地揭示了数据在多维空间中的结构，特别是在数据可视化方面表现出色。
t-SNE广泛应用于各种领域，包括生物信息学、计算机视觉和社会网络分析等，以识别高维数据中的模式和聚类。

Python中的sklearn.manifold.TSNE类 常用参数如下，

| 参数名                | 默认值         | 描述                                          |
|--------------------|-------------|---------------------------------------------|
| n_components       | 2           | 嵌入空间的维度。通常设置为2用于数据可视化。                      |
| perplexity         | 30          | 复杂度参数，影响到局部与全局数据的平衡。推荐值在5到50之间。             |
| early_exaggeration | 12          | 在优化的早期，控制高维空间中的簇在低维空间中是如何分开的。               |
| learning_rate      | 200         | 学习率，用于控制梯度下降的步长。推荐值在10到1000之间。              |
| n_iter             | 1000        | 优化的最大迭代次数。                                  |
| random_state       | None        | 随机数种子，用于结果的可复现性。如果指定了一个int值，TSNE将总是产生相同的输出。 |
| metric             | "euclidean" | 计算两个实例之间距离的度量。TSNE支持多种距离度量。                 |
| init               | "random"    | 嵌入的初始化方式。可以设置为"pca"使用PCA的结果作为初始化。           |
| verbose            | 0           | 日志详细程度。较高的值表示输出更多的信息。                       |

使用代码，
```text
from sklearn.manifold import TSNE
import numpy as np
import matplotlib.pyplot as plt

# 假设 X 是一个高维数据集，这里我们用随机数据来模拟
np.random.seed(42)  # 保证示例的可复现性
X = np.random.rand(100, 20)  # 100个样本，每个样本20维

# 使用TSNE进行降维
tsne = TSNE(n_components=2, perplexity=30, learning_rate=200, n_iter=1000, random_state=42, init='pca', verbose=1)
X_embedded = tsne.fit_transform(X)

# 结果可视化
plt.figure(figsize=(10, 6))
plt.scatter(X_embedded[:, 0], X_embedded[:, 1])
plt.title('TSNE降维结果')
plt.xlabel('Dimension 1')
plt.ylabel('Dimension 2')
plt.show()
```
