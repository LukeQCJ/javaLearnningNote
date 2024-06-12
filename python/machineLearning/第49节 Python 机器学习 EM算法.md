# Python 机器学习 EM算法

EM算法（期望最大化算法，Expectation-Maximization Algorithm）是一种迭代优化技术，
广泛用于含有隐变量（latent variables）的概率模型参数估计。

EM算法的【目标】是找到模型参数的最大似然估计，尤其是当模型中存在无法直接观测的隐藏数据时。
它通过迭代两个步骤来逼近最大似然解：E步（期望步）和M步（最大化步）。

## 1、理解EM算法
EM算法（Expectation-Maximization）是一种迭代优化技术，主要用于参数估计问题，特别是在统计模型中存在未观察到的隐变量或数据不完整时。
通过交替执行两个步骤——期望步（E步）和最大化步（M步）——EM算法逐步改进对参数的估计。
在E步，算法计算当前参数估计下隐变量的期望，相当于对隐变量进行一个“软”分配；
而在M步，根据E步的结果更新参数，以最大化观测数据的似然函数。
这个过程类似于“猜测-验证”，不断迭代以改进参数估计，直到收敛。

EM算法的【优势】在于其广泛的适用性和对隐变量处理的灵活性，能够保证似然函数的值在迭代过程中不减少，从而确保收敛性。
然而，算法可能收敛到局部最大值，并且初始化参数的选择、迭代停止的标准等实践中的问题需要特别注意。

EM算法在高斯混合模型（GMM）、聚类、分类以及缺失数据处理等多个领域有着广泛应用，
尽管面临挑战，其在处理含隐变量的复杂问题中的有效性仍被广泛认可。

## 2、scikit-learn
Python中，使用scikit-learn库可以通过高斯混合模型（Gaussian Mixture Model, GMM）来实现期望最大化（Expectation-Maximization, EM）算法。

GaussianMixture类提供了使用EM算法估计参数的功能，适用于聚类和密度估计等任务。

1）安装命令
```text
pip install scikit-learn
```

2）导入所需模块
```text
from sklearn.mixture import GaussianMixture
from sklearn.datasets import make_blobs
import matplotlib.pyplot as plt
import numpy as np
```

3、数据集
Scikit-learn 的 EM 算法通常用于聚类分析，比如使用高斯混合模型（Gaussian Mixture Model, GMM）。

糖尿病数据集（Diabetes Dataset）是 Scikit-learn 库中的一个标准数据集，用于回归分析。
尽管这个数据集通常用于回归任务，可以尝试使用 EM 算法对其进行聚类分析，从而发现数据中潜在的模式或组别。
数据集包含了442个病人的10个基线变量，目标变量是一年后疾病进展的量化度量。
这些基线变量包括年龄、性别、体质指数、平均血压和六个血清测量值。 

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

使用代码，
```text
from sklearn.datasets import load_diabetes

# 加载糖尿病数据集
X, y = load_diabetes(return_X_y=True)

# 查看数据维度
print("特征集维度:", X.shape)
print("响应变量维度:", y.shape)

# 打印前5个样本的特征和目标值，以便观察数据
print("前5个样本的特征:\n", X[:5])
print("前5个样本的目标值:", y[:5])
```
output:
```text
特征集维度: (442, 10)
响应变量维度: (442,)
前5个样本的特征:
 [[ 0.03807591  0.05068012  0.06169621  0.02187239 -0.0442235  -0.03482076
  -0.04340085 -0.00259226  0.01990749 -0.01764613]
 [-0.00188202 -0.04464164 -0.05147406 -0.02632753 -0.00844872 -0.01916334
   0.07441156 -0.03949338 -0.06833155 -0.09220405]
 [ 0.08529891  0.05068012  0.04445121 -0.00567042 -0.04559945 -0.03419447
  -0.03235593 -0.00259226  0.00286131 -0.02593034]
 [-0.08906294 -0.04464164 -0.01159501 -0.03665608  0.01219057  0.02499059
  -0.03603757  0.03430886  0.02268774 -0.00936191]
 [ 0.00538306 -0.04464164 -0.03638469  0.02187239  0.00393485  0.01559614
   0.00814208 -0.00259226 -0.03198764 -0.04664087]]
前5个样本的目标值: [151.  75. 141. 206. 135.]
```

## 5、使用EM算法聚类分析
Scikit-learn 的 EM 算法通常用于聚类分析，比如使用高斯混合模型（Gaussian Mixture Model, GMM）。
糖尿病数据集（Diabetes Dataset）是 Scikit-learn 库中的一个标准数据集，用于回归分析。
尽管这个数据集通常用于回归任务，可以尝试使用 EM 算法对其进行聚类分析，从而发现数据中潜在的模式或组别。

使用 Scikit-learn 的 GaussianMixture 类在糖尿病数据集上应用 EM 算法。

常用参数如下，

| 参数名              | 描述                                                                                                                                        |
|------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| n_components	    | 模型中的组件数（即高斯分布的数量，也可以理解为聚类的数量）。默认值为 1。                                                                                                     |
| covariance_type	 | 协方差参数类型。可选值有 'full'（每个组件有自己的一般协方差矩阵）、'tied'（所有组件共享一个协方差矩阵）、'diag'（每个组件有自己的对角协方差矩阵）、'spherical'（每个组件的协方差矩阵是由单个方差组成，假设特征之间不相关）。默认值为 'full'。 |
| random_state	    | 随机数生成器的种子。用于结果的可重现性。如果为None，则随机数生成器是np.random 使用的RandomState实例。默认值为None。                                                                  |
| tol	             | EM算法的收敛阈值。算法会停止迭代，如果在两次迭代之间的对数似然下降小于此阈值。默认值为 1e-3。                                                                                        |
| max_iter	        | EM算法的最大迭代次数。默认值为 100。                                                                                                                     |
| n_init	          | 初始化次数。算法会运行n_init次，并选择具有最大似然的结果。默认值为 1。                                                                                                   |
| init_params	     | 参数初始化方法。可选值有'kmeans'（使用k-means算法初始化）和'random'（随机初始化）。默认值为'kmeans'。                                                                        |

使用代码，
```text
from sklearn.datasets import load_diabetes
from sklearn.mixture import GaussianMixture
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt

# 加载糖尿病数据集
X, y = load_diabetes(return_X_y=True)

# 数据标准化
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# 应用高斯混合模型聚类
n_components = 2  # 假设我们想要将数据聚类为2个组
gmm = GaussianMixture(n_components=n_components, random_state=0)
gmm.fit(X_scaled)

# 预测每个点的簇标签
labels = gmm.predict(X_scaled)

# 可视化结果（选择前两个特征进行可视化）
plt.scatter(X_scaled[:, 0], X_scaled[:, 1], c=labels, cmap='viridis')
plt.xlabel('Feature 1')
plt.ylabel('Feature 2')
plt.title('Cluster assignment with GMM')
plt.draw()
plt.show()
```