# Python 机器学习 集成学习 XGBoost和GBDT的区别

XGBoost（eXtreme Gradient Boosting）和GBDT（Gradient Boosting Decision Tree）
都是梯度提升树（Gradient Boosting Tree）的算法，它们有很多相似之处，但也有一些区别。
XGBoost相比于传统的GBDT在速度、正则化、并行化等方面有所改进，在实际应用中通常更常用。

## 1、正则化策略
XGBoost在目标函数中引入了【正则化项】，包括L1正则化（Lasso）和L2正则化（Ridge），以控制模型的复杂度，避免过拟合。

GBDT通常只使用了【一阶导数】，没有直接对模型复杂度进行正则化。

正则化策略是用于控制模型的复杂度，以防止过拟合的策略。

在XGBoost中，可以通过设置超参数来调整正则化项的权重，包括：

| 参数               | 描述              |
|------------------|-----------------|
| alpha            | L1正则化项的权重。      |
| lambda           | L2正则化项的权重。      |
| max_depth        | 控制树的最大深度。       |
| min_child_weight | 叶子节点的最小样本权重。    |
| gamma            | 控制节点分裂的最小损失下降量。 |

在GBDT中，正则化策略通常包括设置树的最大深度、叶子节点的最小样本数等参数。

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.ensemble import GradientBoostingClassifier
import xgboost as xgb

# 加载数据集
iris = load_iris()
X_train, X_test, y_train, y_test = train_test_split(iris.data, iris.target, test_size=0.2, random_state=42)

# 使用GBDT进行分类
gbdt = GradientBoostingClassifier(max_depth=3, min_samples_split=2, min_samples_leaf=1, min_weight_fraction_leaf=0.0,
                                  subsample=1.0, max_leaf_nodes=None, min_impurity_decrease=0.0,
                                  init=None, random_state=None, max_features=None,
                                  verbose=0, warm_start=False,
                                  validation_fraction=0.1, n_iter_no_change=None, tol=0.0001)
gbdt.fit(X_train, y_train)
gbdt_accuracy = gbdt.score(X_test, y_test)
print("GBDT模型准确率:", gbdt_accuracy)

# 使用XGBoost进行分类
xgb_model = xgb.XGBClassifier(max_depth=3, learning_rate=0.1, n_estimators=100,
                              objective='multi:softmax', num_class=3,
                              alpha=0.1, reg_lambda=0.1)
xgb_model.fit(X_train, y_train)
xgb_accuracy = xgb_model.score(X_test, y_test)
print("XGBoost模型准确率:", xgb_accuracy)
```
output:
```text
GBDT模型准确率: 1.0
XGBoost模型准确率: 1.0
```

## 2、支持并行化
XGBoost使用了一种称为“块结构”的数据结构，可以有效地支持并行化，提高了训练速度。

GBDT的训练过程很难并行化，通常是串行进行的，导致训练速度较慢。

XGBoost和Gradient Boosting Decision Tree
（GBDT，例如使用Scikit-Learn的GradientBoostingClassifier或GradientBoostingRegressor）都是基于梯度增强的机器学习算法，
用于解决分类和回归问题。
两者都利用了弱学习器（通常是决策树）的集成来提升性能和预测能力，但它们在并行化方面有所不同。

```text
import xgboost as xgb
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error

# 加载数据
data = load_diabetes()
X = data.data
y = data.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建XGBoost模型
xgb_model = xgb.XGBRegressor(n_estimators=100, learning_rate=0.1, max_depth=3, n_jobs=-1)
# 创建GBDT模型
gbdt_model = GradientBoostingRegressor(n_estimators=100, learning_rate=0.1, max_depth=3, random_state=42)

# 训练模型
xgb_model.fit(X_train, y_train)
gbdt_model.fit(X_train, y_train)

# 预测
xgb_pred = xgb_model.predict(X_test)
gbdt_pred = gbdt_model.predict(X_test)

# 评估模型
xgb_mse = mean_squared_error(y_test, xgb_pred)
gbdt_mse = mean_squared_error(y_test, gbdt_pred)

print("XGBoost Model MSE:", xgb_mse)
print("GBDT Model MSE:", gbdt_mse)
```
output:
```text
XGBoost Model MSE: 2959.455813706508
GBDT Model MSE: 2906.4600940881232
```

## 3、特征分裂点选择
XGBoost使用了一种称为“Approximate Greedy Algorithm”的方法，通过近似计算特征分裂点，加速了特征选择的过程。
GBDT使用了精确的贪心算法来选择最佳的特征分裂点，计算量较大。

XGBoost（eXtreme Gradient Boosting）和GBDT（Gradient Boosting Decision Tree）
都是基于梯度提升树（Gradient Boosting Decision Tree）算法的变种，它们在特征分裂点选择上有一些区别。
XGBoost和GBDT都提供了特征重要性的信息，可以通过特征重要性来判断特征在模型中的影响程度。
但在特征分裂点选择上的算法不同，因此得到的特征重要性可能会略有不同。

```text
import xgboost as xgb
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.ensemble import GradientBoostingClassifier

# 生成虚拟数据集
X, y = make_classification(n_samples=1000, n_features=20, n_classes=2, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 使用XGBoost进行训练
xgb_model = xgb.XGBClassifier(max_depth=3, learning_rate=0.1, n_estimators=100, silent=True)
xgb_model.fit(X_train, y_train)

# 使用GBDT进行训练
gbdt_model = GradientBoostingClassifier(max_depth=3, learning_rate=0.1, n_estimators=100)
gbdt_model.fit(X_train, y_train)

# 打印XGBoost特征重要性
print("XGBoost Feature Importances:")
print(xgb_model.feature_importances_)

# 打印GBDT特征重要性
print("\nGBDT Feature Importances:")
print(gbdt_model.feature_importances_)
```
output:
```text
XGBoost Feature Importances:
[0.03056403 0.03052899 0.02743551 0.02458193 0.02195637 0.36925033
 0.02535702 0.02212941 0.02002764 0.02126945 0.01907394 0.04761912
 0.02303191 0.03504826 0.09094372 0.01976577 0.03503923 0.02374289
 0.09076075 0.02187383]

GBDT Feature Importances:
[0.00547554 0.01410264 0.01161585 0.0010553  0.00223023 0.66376368
 0.01219786 0.01050487 0.00450535 0.00556248 0.00198691 0.01861231
 0.01123746 0.00551356 0.17419292 0.0089164  0.00735361 0.00470791
 0.03404875 0.00241638]
```

## 4、缺失值处理
XGBoost可以自动处理缺失值，无需对缺失值进行预处理。
GBDT需要对缺失值进行处理，通常采用一种称为“堆积法”（Imputation）的方法来处理缺失值。

```text
import xgboost as xgb
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split

# 加载数据集
iris = load_iris()
X, y = iris.data, iris.target

# 人为创建缺失值
import numpy as np

X_with_missing = np.copy(X)
X_with_missing[0, 0] = np.nan  # 将第一个样本的第一个特征设为缺失值

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X_with_missing, y, test_size=0.2, random_state=42)

# 使用XGBoost训练模型
xgb_model = xgb.XGBClassifier()
xgb_model.fit(X_train, y_train)

# 使用GBDT训练模型
gbdt_model = xgb.XGBClassifier(tree_method='hist')  # 设置tree_method为'hist'以使用histogram算法
gbdt_model.fit(X_train, y_train)

from sklearn.metrics import accuracy_score

# 在测试集上评估XGBoost模型
y_pred_xgb = xgb_model.predict(X_test)
accuracy_xgb = accuracy_score(y_test, y_pred_xgb)
print("XGBoost 模型准确率:", accuracy_xgb)

# 在测试集上评估GBDT模型
y_pred_gbdt = gbdt_model.predict(X_test)
accuracy_gbdt = accuracy_score(y_test, y_pred_gbdt)
print("GBDT 模型准确率:", accuracy_gbdt)
```
output:
```text
XGBoost 模型准确率: 1.0
GBDT 模型准确率: 1.0
```

## 5、分裂节点方式
XGBoost的分裂节点方式相比于GBDT更加精确，可以选择任意的分裂点位置。GBDT只能选择特征的某个取值作为分裂点。

```text
import xgboost as xgb
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
from sklearn.ensemble import GradientBoostingRegressor

# 加载数据集
datas = load_diabetes()
X = datas.data
y = datas.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 使用GBDT训练模型
gbdt = GradientBoostingRegressor()
gbdt.fit(X_train, y_train)

# 使用XGBoost训练模型
params = {'objective': 'reg:squarederror', 'eval_metric': 'rmse'}
dtrain = xgb.DMatrix(X_train, label=y_train)
dtest = xgb.DMatrix(X_test, label=y_test)
num_rounds = 100
xgboost_model = xgb.train(params, dtrain, num_rounds)

# 使用训练好的模型进行预测
y_pred_gbdt = gbdt.predict(X_test)
y_pred_xgboost = xgboost_model.predict(dtest)

# 计算均方误差
mse_gbdt = mean_squared_error(y_test, y_pred_gbdt)
mse_xgboost = mean_squared_error(y_test, y_pred_xgboost)

print("GBDT均方误差:", mse_gbdt)
print("XGBoost均方误差:", mse_xgboost)
```
output:
```text
GBDT均方误差: 2920.9706781582304
XGBoost均方误差: 3351.001637862091
```