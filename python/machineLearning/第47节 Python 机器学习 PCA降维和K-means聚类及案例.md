# Python 机器学习 PCA降维和K-means聚类及案例

主成分分析（PCA）和K-Means聚类是两种常用的机器学习技术，在数据预处理和无监督学习中尤其有用。

PCA是一种【降维技术】，能够通过【减少】数据集的【特征数目】来简化数据集，同时保留大部分变异性。

K-Means是一种【聚类算法】，能够将数据分成几个不相交的群组或“簇”。

## 1、PCA降维
PCA（主成分分析）是一种常用的数据降维技术，可以减少数据集的维度，同时尽可能保留原始数据的变异性。

Python中，我们经常使用scikit-learn库来实现PCA降维。

常用参数如下，

| 参数名            | 说明                                                                                                      |
|----------------|---------------------------------------------------------------------------------------------------------|
| n_components   | 类型: int,float, None, 'mle'; 默认值: None。指定要保留的主成分数量。可以是具体数值、保留的方差比例、'mle'，或未指定（保留所有特征）。                   |
| copy           | 类型: bool; 默认值: True。是否在运行算法前复制数据。如果为 False，原始数据会被覆盖。                                                    |
| whiten         | 类型: bool; 默认值: False。是否对数据进行白化，使输出的所有主成分具有相同的方差。                                                        |
| svd_solver     | 类型: 'auto', 'full', 'arpack', 'randomized'; 默认值: 'auto'。选择使用的SVD求解器类型。根据数据类型和 n_components 自动选择，或指定求解器。 |
| tol            | 类型: float; 默认值: 0.0。仅当 svd_solver='arpack' 时使用，表示奇异值分解的容忍度。                                             |
| iterated_power | 类型: int或'auto'; 默认值: 'auto'。随机SVD求解器的幂方法的迭代次数。                                                          |
| random_state   | 类型: int, RandomState, None; 默认值: None。随机数生成器的种子，或RandomState实例，或None。在某些 svd_solver 下使用。                |

使用代码，

```text
from sklearn.decomposition import PCA
from sklearn.datasets import make_classification
import matplotlib.pyplot as plt

# 2. 生成一个高维数据集
X, y = make_classification(n_samples=100, n_features=20, n_informative=2, n_redundant=0, random_state=42)

# 3. 应用PCA降维到2维
pca = PCA(n_components=2)
X_pca = pca.fit_transform(X)

# 4. 查看降维结果
plt.scatter(X_pca[:, 0], X_pca[:, 1], c=y)
plt.xlabel('Principal Component 1')
plt.ylabel('Principal Component 2')
plt.title('PCA Result')
plt.draw()
plt.show()
```
output:
```text
X.shape: (100, 20)
X_pca.shape: (100, 2)
```

## 2、K-means聚类
K-means是一种广泛使用的聚类算法，用于将数据分成多个类或群组，使得同一群组内的数据点相似度较高，而不同群组间的数据点相似度较低。

Python中，我们经常使用scikit-learn库的KMeans类来实现。

常用参数如下，

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
from sklearn.cluster import KMeans
import numpy as np
import matplotlib.pyplot as plt

# 示例数据
X = np.array([[1, 2], [1, 4], [1, 0],
              [10, 2], [10, 4], [10, 0]])

# 初始化KMeans
kmeans = KMeans(n_clusters=3, random_state=0).fit(X)

# 聚类中心点
centroids = kmeans.cluster_centers_

# 数据点的标签
labels = kmeans.labels_

# 可视化结果
plt.scatter(X[:, 0], X[:, 1], c=labels, s=50, cmap='viridis')
plt.scatter(centroids[:, 0], centroids[:, 1], c='red', s=200, alpha=0.5)
plt.draw()
plt.show()
```

## 3、用户对物品类别的喜好细分案例
用户对物品类别的喜好细分案例中，可以结合使用PCA（主成分分析）和K-means聚类算法来分析用户对不同物品的喜好，并将用户细分成不同的群体。
这种方法可以从高维数据中提取出重要的特征，并基于这些特征将用户分组，以便更好地理解不同用户群体的行为和偏好。

```text
import numpy as np
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler

# 随机生成一个示例数据集
# 若我们有150个用户，10种物品
np.random.seed(42)
X = np.random.randint(1, 5, size=(150, 10))

# 数据标准化
X_std = StandardScaler().fit_transform(X)

# 使用PCA进行降维，以便更好地进行聚类分析
pca = PCA(n_components=2)  # 降至2维以便可视化
X_pca = pca.fit_transform(X_std)

# 使用K-means进行聚类
k = 3  # 基于先前的分析决定将用户分为3个群体
kmeans = KMeans(n_clusters=k, random_state=42)
y_kmeans = kmeans.fit_predict(X_pca)

# 可视化聚类结果
plt.figure(figsize=(8, 6))
plt.scatter(X_pca[:, 0], X_pca[:, 1], c=y_kmeans, s=50, cmap='viridis')

centers = kmeans.cluster_centers_
plt.scatter(centers[:, 0], centers[:, 1], c='red', s=200, alpha=0.5)
plt.title("User Preference Clustering")
plt.xlabel("PCA 1")
plt.ylabel("PCA 2")
plt.show()
```