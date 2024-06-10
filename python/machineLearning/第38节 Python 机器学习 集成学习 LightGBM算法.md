# Python 机器学习 LightGBM算法

LightGBM是一种高效的梯度提升框架，由微软开发，旨在处理大规模数据集并具有高性能。
它基于决策树算法，但采用了一些优化策略，使得在训练速度和内存使用上有显著的改进。

## 1、理解 LightGBM
LightGBM基于梯度提升决策树 (GBDT) 的机器学习算法，因其速度快、精度高而受到广泛关注。
它已被证明在各种任务中表现出色，包括分类、回归和排序。

LightGBM基于决策树的提升方法，即通过集成多个决策树来构建强大的模型。
每个决策树都是一个分类器，通过对输入特征进行递归分割来进行预测。

LightGBM是一种强大的机器学习工具，特别适用于处理大规模数据集和需要高性能的任务。
通过理解其基本原理和特性，可以更好地利用LightGBM构建高效和准确的预测模型。

其【核心技术】包括基于直方图的决策树算法，这种方法通过将连续特征值离散化到直方图中来减少计算复杂性和内存占用。
LightGBM 采用叶子优先的生长策略，选择增益最大的叶子进行分裂，虽然可能导致树的不平衡但通常能达到更好的准确率。

它支持特征并行和数据并行来提高处理效率，并对稀疏数据进行了优化，自动处理缺失值和零值优化。
LightGBM 支持多种机器学习任务，包括分类、回归和排序等，提供了丰富的调整参数以适应不同的数据特性和需求。
这些技术特点使得 LightGBM 在大规模数据集上的训练不仅快速高效，而且能够维持高性能。

## 2、演进过程
LightGBM 是由微软研究院开发的，作为梯度提升机（GBM）算法的一种高效实现，它在多个方面优化了传统的梯度提升方法。
LightGBM 自推出以来，因其高效性和灵活性，在机器学习和数据科学领域获得了广泛的应用。

LightGBM 的主要演进过程和技术特点：

| 演进过程         | 内容                                                              |
|--------------|-----------------------------------------------------------------|
| GBM 的局限性     | 传统的 GBM 或 XGBoost 在处理大规模数据集时效率低下。                               |
| 直方图优化算法      | 使用基于直方图的决策树算法，将连续特征分桶到离散的 bins 中，减少内存占用，提高计算速度。                 |
| 叶子优先的决策树生长策略 | 采用叶子优先策略进行树的生长，选择增益最大的叶子进行分裂，可快速降低误差，尤其适用于数据集不均匀的情况。            |
| 特征并行和数据并行    | 支持特征并行减少通信成本，数据并行通过数据分片提高效率。                                    |
| 缓存优化         | 算法设计中考虑内存访问模式，优化数据访问效率，减少缓存不命中。                                 |
| 支持类别特征       | 直接处理类别特征，无需独热编码，简化数据预处理流程，降低数据维度。                               |
| GoAI 支持      | 集成 GPU 加速计算，支持 NVIDIA 的 GPU Dataframe (cuDF) 等，提高 GPU 系统上的训练速度。 |

**1）技术创新**

基于直方图的优化不仅降低了内存的使用，还减少了在节点分裂时需要比较的数据点数量，大大加速了计算速度。

叶子优先生长策略使得模型在面对具有复杂决策边界的数据集时能够更快地学习到数据的关键特征。

高效的类别特征处理对类别特征的优化处理使得 LightGBM 在处理具有大量类别特征的数据集时，效率更高，而不损失模型的准确性。

**2）应用领域**

LightGBM 被广泛应用于各种机器学习领域，包括但不限于金融风险控制、网络安全、生物信息学、电商推荐系统等。
它的高效性和准确性使得在大规模数据集上的应用成为可能，特别是在需要实时预测的场景中。

## 3、 LightGBM
要使用 LightGBM，需要先安装它。可以使用 pip 或 conda 安装 LightGBM：
```text
pip install lightgbm
```

使用 lgb.train() 函数时，有一些常用的参数可以用来控制 LightGBM 模型的训练过程。常用参数如下，

| 参数                    | 类型                                   | 说明                                          |
|-----------------------|--------------------------------------|---------------------------------------------|
| params                | dict                                 | 模型参数字典， 包括目标函数、评估指标、提升类型等                   |
| train_set             | lgb.Dataset 或类似的数据结构                 | 训练数据集                                       |
| num_boost_round       | int                                  | 模型迭代轮数                                      |
| valid_sets            | list of lgb.Dataset 或类似的数据结构用于验证的数据集 |
| early_stopping_rounds | int                                  | 当验证指标在连续的 early_stopping_rounds轮中没有提升时，停止训练 |
| init_model            | str 或 lgb.Booster                    | 初始模型文件名或模型对象                                |
| feature_name          | list of str                          | 特征名称                                        |
| categorical_feature   | list of str 或 int                    | 指定哪些特征是分类特征，可以是特征名称或索引                      |
| callbacks             | list of callables                    | 回调函数列表，在训练过程中执行额外操作                         |
| evals_result          | dict                                 | 存储训练过程中的评估结果                                |
| verbose_eval          | bool 或 int                           | 是否显示评估结果，0 表示不显示，非零值表示每隔多少轮显示一次             |
| learning_rates        | list of float 或 callable             | 学习率调整策略                                     |
| keep_training_booster | bool                                 | 是否保留训练过程中的 Booster 对象                       |
| callbacks             | list of callables                    | 回调函数列表， 在训练过程中执行额外操作                        |

## 4、使用实例
使用 Python 的 lightgbm 库和 sklearn 库中的数据集来训练一个模型，目标是根据花的特征预测花的类别。

```text
import lightgbm as lgb
import numpy as np
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 加载数据集
data = datasets.load_breast_cancer()
X = data.data
y = data.target
# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
# 创建 LightGBM 数据结构
train_data = lgb.Dataset(X_train, label=y_train)
test_data = lgb.Dataset(X_test, label=y_test, reference=train_data)
# 设置参数
params = {
    'boosting_type': 'gbdt',
    'objective': 'binary',
    'metric': 'binary_logloss',
    'num_leaves': 31,
    'learning_rate': 0.05,
    'feature_fraction': 0.9,
    'bagging_fraction': 0.8,
    'bagging_freq': 5,
    'verbose': 0
}
# 训练模型
num_round = 100
bst = lgb.train(params, train_data, num_round, valid_sets=[test_data])
# 预测
y_pred = bst.predict(X_test, num_iteration=bst.best_iteration)
# 将预测结果转换为二进制输出
y_pred_binary = np.where(y_pred > 0.5, 1, 0)
# 计算准确率
accuracy = accuracy_score(y_test, y_pred_binary)
print(f'Accuracy: {accuracy:.2f}')
```