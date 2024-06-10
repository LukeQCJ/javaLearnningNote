# Python 机器学习 XGBoost简单案例

XGBoost 是一个非常流行的机器学习库，用于执行梯度提升算法，特别适用于分类问题，回归问题和排名问题等。
可以尝试更改 train_test_split 的参数或者 XGBClassifier 的超参数，比如 max_depth 和 n_estimators，来看看这些改变对模型性能的影响。

## 1、二元分类问题
二元分类是机器学习中一个常见的问题类型，它涉及将实例划分为两个类别中的一个。
这类问题在许多应用领域都非常重要，如医疗诊断、垃圾邮件检测、信贷审批等。

```text
import xgboost as xgb
from sklearn.datasets import load_breast_cancer
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, roc_auc_score

# 加载数据
data = load_breast_cancer()
X = data.data
y = data.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建XGBoost分类器
xgb_clf = xgb.XGBClassifier(objective='binary:logistic', max_depth=3, learning_rate=0.1, n_estimators=100)

# 训练模型
xgb_clf.fit(X_train, y_train)

# 预测测试数据
y_pred = xgb_clf.predict(X_test)
y_pred_prob = xgb_clf.predict_proba(X_test)[:, 1]

# 评估模型
accuracy = accuracy_score(y_test, y_pred)
roc_auc = roc_auc_score(y_test, y_pred_prob)

print(f"Accuracy: {accuracy:.2f}")
print(f"ROC AUC: {roc_auc:.2f}")
```
output:
```text
Accuracy: 0.96
ROC AUC: 0.99
```

## 2、多类分类问题
多类分类问题是指一个分类任务需要将实例分配到三个或更多个类别中的一种。
这与二元分类问题不同，后者仅涉及两个类别（如是与否，正与负）。
多类分类的典型例子包括将邮件归类为垃圾邮件、正常邮件或促销邮件，或者识别图片中的物体属于哪一个类别（如车、飞机、猫等）。

对于多类分类问题，XGBoost 可以使用其内置的 multi:softmax 或 multi:softprob 目标函数来处理。

```text
import xgboost as xgb
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 生成模拟的多类分类数据集
X, y = make_classification(n_samples=1000, n_features=20, n_informative=15, n_classes=3, n_clusters_per_class=1,
                           random_state=42)

# 分割数据集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 创建 DMatrix 对象
dtrain = xgb.DMatrix(X_train, label=y_train)
dtest = xgb.DMatrix(X_test, label=y_test)

# 设置参数
params = {
    'objective': 'multi:softmax',  # 使用 softmax 多类分类
    'num_class': 3,  # 类别数
    'max_depth': 6,  # 树的最大深度
    'eta': 0.3,  # 学习率
    'verbosity': 0  # 0 (silent), 1 (warning), 2 (info), 3 (debug)
}

# 训练模型
num_round = 100  # 训练轮数
model = xgb.train(params, dtrain, num_round)

# 预测
predictions = model.predict(dtest)

# 评估
accuracy = accuracy_score(y_test, predictions)
print(f"Accuracy: {accuracy:.2f}")
```
output:
```text
Accuracy: 0.93
```

## 3、回归问题
机器学习中的回归问题是一种监督学习任务，它的目标是预测一个或多个连续数值的输出，这些输出是基于输入特征的。
回归问题与分类问题不同，分类问题的目标是预测离散的类别标签，而回归问题是预测一个连续的量。

在回归问题中，模型尝试找到输入变量和输出变量之间的关系，通常是通过拟合一个或多个方程的形式来实现。

```text
import xgboost as xgb
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

# 加载数据
datas = load_diabetes()
X, y = datas.data, datas.target

# 分割数据为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 初始化XGBoost回归模型
model = xgb.XGBRegressor(objective='reg:squarederror', colsample_bytree=0.3, learning_rate=0.1,
                         max_depth=5, alpha=10, n_estimators=10)

# 训练模型
model.fit(X_train, y_train)

# 预测测试集
y_pred = model.predict(X_test)

# 计算并打印MSE
mse = mean_squared_error(y_test, y_pred)
print(f"MSE: {mse}")
```
output:
```text
MSE: 3870.8655907693933
```

## 4、特征重要性分析
特征重要性分析是一种评估模型中各个特征对预测结果影响程度的技术。
简而言之，它帮助我们理解哪些特征在模型的决策过程中扮演了核心角色，以及它们对最终预测结果的贡献大小。

```text
import xgboost as xgb
from xgboost import plot_importance
import matplotlib.pyplot as plt
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split

# 加载数据集
data = load_diabetes()
X = data['data']
y = data['target']

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建 XGBoost 模型
model = xgb.XGBRegressor(objective='reg:squarederror')
model.fit(X_train, y_train)

# 特征重要性
feature_importances = model.feature_importances_

# 可视化特征重要性
plt.figure(figsize=(10, 8))
plot_importance(model, height=0.5, xlabel='F Score', ylabel='Features', importance_type='weight')
plt.show()
```

## 5、超参数调优
超参数调优是一个优化过程，旨在找到最佳的超参数配置，使得机器学习模型在给定的任务上表现最好。
超参数是在学习过程开始之前设置的参数，它们控制了模型的训练过程和模型的结构（例如树的深度、学习率或神经网络中的层数）。

| 超参数              | 描述                                      |
|------------------|-----------------------------------------|
| max_depth        | 树的最大深度。深度大可以增加模型的复杂度，但可能导致过拟合。          |
| min_child_weight | 决定最小叶子节点样本权重和。增大此参数可以防止过拟合。             |
| gamma            | 节点分裂所需的最小损失函数下降值。设置较高的值会使模型保守，有助于防止过拟合。 |
| subsample        | 训练每棵树时使用的数据比例，防止过拟合。                    |
| colsample_bytree | 建立树时的列采样比例。                             |
| learning_rate    | 学习率，也叫作 eta。                            |

参数都可以在模型训练阶段通过网格搜索、随机搜索或其他优化方法来调整，以达到最佳的模型性能。
```text
import xgboost as xgb
from sklearn.model_selection import GridSearchCV
from sklearn.datasets import load_diabetes
from sklearn.metrics import mean_squared_error

# 载入数据
data = load_diabetes()
X = data.data
y = data.target

# 定义 XGBoost 模型
model = xgb.XGBRegressor(objective='reg:squarederror')

# 设置要调优的超参数网格
param_grid = {
    'max_depth': [3, 5, 7],
    'min_child_weight': [1, 5, 10],
    'gamma': [0.5, 1, 1.5],
    'subsample': [0.6, 0.8, 1.0],
    'colsample_bytree': [0.6, 0.8, 1.0],
    'learning_rate': [0.01, 0.1, 0.2]
}

# 设置网格搜索
grid_search = GridSearchCV(estimator=model, param_grid=param_grid, scoring='neg_mean_squared_error', cv=3, verbose=1)

# 执行网格搜索
grid_search.fit(X, y)

# 最佳参数和最佳模型的评分
print("Best parameters:", grid_search.best_params_)
print("Best score:", -grid_search.best_score_)

# 使用最佳模型进行预测
best_model = grid_search.best_estimator_
predictions = best_model.predict(X)
mse = mean_squared_error(y, predictions)
print("MSE:", mse)
```
output:
```text
Fitting 3 folds for each of 729 candidates, totalling 2187 fits
Best parameters: {'colsample_bytree': 0.6, 'gamma': 0.5, 'learning_rate': 0.1, 'max_depth': 3, 'min_child_weight': 10, 'subsample': 0.6}
Best score: 3250.1525202996477
MSE: 1393.9512131039603
```

---
# 什么是参数，超参数?
## 前言
【参数】是模型中可被学习和调整的参数，通过训练数据进行学习和优化；

而【超参数】则是手动设置的参数，用于控制模型的行为和性能，超参数的选择和优化对模型性能有重要影响。

## 一、参数是什么？
参数是模型中可被学习和调整的参数，通常是通过训练数据来自动学习的，以最小化损失函数或优化目标。

在深度学习中，参数通常是指神经网络中的权重和偏差。

这些参数是通过反向传播算法，根据训练数据中的梯度信息自动调整的，以最小化损失函数。

参数的学习是模型训练的过程，目标是找到最佳的参数配置，使得模型能够对新的未见过的数据进行准确的预测。

## 二、超参数是什么
超参数则是在算法运行之前手动设置的参数，用于控制模型的行为和性能。

这些超参数的选择会影响到模型的训练速度、收敛性、容量和泛化能力等方面。

例如，学习率、迭代次数、正则化参数、隐藏层的神经元数量等都是常见的超参数。

超参数的选择通常是一个试错的过程，需要根据经验和领域知识进行调整。

## 三，常使用的超参数有哪些
学习率（Learning Rate）：这是影响模型训练速度和稳定性的关键参数。学习率设置得过大可能会导致模型无法收敛，设置得过小则会使训练过程过于缓慢。

动量参数（Momentum）：这是用于加速梯度下降的参数，可以增加梯度的方向性，从而帮助模型更快地收敛。

网络层数（Number of Layers）：这是决定模型复杂度和表达能力的参数。一般来说，增加网络层数可以使模型更好地学习复杂的特征，但同时也增加了模型的参数数量和计算复杂度。

隐层节点数（Number of Hidden Nodes）：这是决定模型隐层大小和表达能力的参数。一般来说，增加隐层节点数可以使模型更好地学习复杂的特征，但同时也增加了模型的参数数量和计算复杂度。

学习率下降幅度（Learning Rate Decay）：这是用于控制学习率在训练过程中下降的参数。通过逐渐降低学习率，可以让模型在训练后期更加精细地逼近最优解。

mini-batch大小（Mini-batch Size）：这是决定每次更新时使用梯度下降的样本数量的参数。一般来说，使用较大的mini-batch可以提高训练速度和稳定性，但可能会降低模型的泛化能力。

正则化参数（Regularization Parameters）：这是用于控制正则化效果的参数，可以防止过拟合现象的发生。常用的正则化方法包括L1正则化、L2正则化和Dropout等。

批处理次数（Number of Batches）：这是决定每次训练过程中进行梯度更新的次数的参数。一般来说，增加批处理次数可以提高训练速度和稳定性，但可能会增加计算资源和时间成本。

优化器选择（Optimizer）：这是用于优化神经网络权重的算法选择。常用的优化器包括梯度下降法、随机梯度下降法、Adam等。

初始权重设置（Initial Weights）：这是用于初始化神经网络权重的参数。不同的初始权重设置可能会影响模型的收敛速度和最终性能。
