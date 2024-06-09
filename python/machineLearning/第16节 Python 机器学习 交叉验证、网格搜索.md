# Python 机器学习 交叉验证、网格搜索

Python 的机器学习项目中，交叉验证（Cross-Validation）和网格搜索（Grid Search）是两种重要的技术，通常用于【模型选择】和【超参数优化】。

交叉验证和网格搜索也是机器学习中常用的两种技术，可以有效地提高模型的性能。

## 1、交叉验证（Cross-Validation）
【交叉验证】是一种【评估模型泛化性能的方法】。
它涉及将数据集分成几个部分，通常是“折叠”（folds），然后将模型在一个折叠上进行测试，而在其余的折叠上进行训练。
这个过程会重复多次，每次选择不同的折叠作为测试集。

Python 的 scikit-learn 库中，提供了多种交叉验证的方法和工具。

### 1）K折交叉验证

数据集被分为K个大小相同的子集。
每个子集轮流作为验证集，其余的K-1个子集用于训练。

使用 scikit-learn 库中cross_val_score() 进行 K 折交叉验证，cross_val_score() 函数是一个非常有用的工具，用于评估机器学习模型的性能。
通过交叉验证，它可以估算模型在未知数据上的表现。

常用参数如下，

| 参数           | 描述                                                                              |
|--------------|---------------------------------------------------------------------------------|
| estimator    | 评估的机器学习模型。必须是拟合数据并计算分数的对象。例如：分类器RandomForestClassifier()。                       |
| X            | 用于训练模型的数据。类数组类型，如 NumPy 数组或 Pandas DataFrame。                                   |
| y            | 目标变量。类数组类型。                                                                     |
| groups       | 用于分组交叉验证的标签。类数组类型。                                                              |
| scoring      | 评估模型性能的评分标准。字符串、可调用对象或 None。例如：'accuracy','neg_mean_squared_error','roc_auc' 等。 |
| cv           | 交叉验证拆分策略。整数、交叉验证生成器或可迭代对象。例如：整数 k（k 折交叉验证），KFold对象等。                            |
| n_jobs       | 并行执行交叉验证的作业数。整数，表示使用的 CPU 核心数。-1 表示使用所有可用的核心。                                   |
| verbose      | 详细程度。整数。较高的数值表示更详细的输出。                                                          |
| fit_params   | 传递给估计器的 fit 方法的额外参数。字典。                                                         |
| pre_dispatch | 在并行执行之前，预先分派的作业数。整数或字符串。例如：'2*n_jobs'。                                          |

使用代码：
```text
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import cross_val_score
from sklearn.datasets import make_classification

# 创建一个简单的数据集
X, y = make_classification(n_samples=1000, n_features=20, random_state=42)

# 创建随机森林分类器实例
clf = RandomForestClassifier(random_state=42)

# 使用 cross_val_score 进行交叉验证
scores = cross_val_score(
    estimator=clf,  # 使用的评估器
    X=X,  # 特征数据
    y=y,  # 目标数据
    groups=None,  # 没有用于分组的标签
    cv=5,  # 5折交叉验证
    scoring='accuracy',  # 使用准确率作为评分标准
    n_jobs=-1,  # 使用所有可用的 CPU 核心
    verbose=0,  # 不输出详细信息
    fit_params=None,  # 没有额外的 fit 方法参数
    pre_dispatch='2*n_jobs'  # 在并行执行之前预先分派的作业数
)

# 打印每折的分数
print("Accuracy scores for each fold:", scores)
```
output:
```text
Accuracy scores for each fold: [0.93  0.905 0.9   0.905 0.855]
```

### 2）留一交叉验证（LOOCV）

对于数据集中的每一个数据点，模型都会在除了这个点之外的所有数据上进行训练，然后在这个点上进行测试。

可以使用 scikit-learn 库中LeaveOneOut() 进行留一交叉验证。

```text
from sklearn.model_selection import LeaveOneOut
from sklearn.svm import SVC
import numpy as np
from sklearn.datasets import load_iris

# 加载鸢尾花数据集
iris = load_iris()

# 定义模型
model = SVC()

# 定义留一交叉验证
loo = LeaveOneOut()

# 训练和评估模型
scores = []
for train_index, test_index in loo.split(iris.data):
    X_train, X_test = iris.data[train_index], iris.data[test_index]
    y_train, y_test = iris.target[train_index], iris.target[test_index]
    model.fit(X_train, y_train)
    score = model.score(X_test, y_test)
    scores.append(score)

# 计算平均得分
average_score = np.mean(scores)

print(f"平均得分：{average_score}")
```
output:
```text
平均得分：0.9666666666666667
```

## 2、网格搜索（Grid Search）
网格搜索是一种【寻找最优模型参数（超参数】）的方法。
它会系统地遍历多种参数的组合，通过交叉验证来评估每种组合的效果，从而找到最佳的参数设置。

scikit-learn 库中 GridSearchCV() 用于对估计器的参数值进行穷举搜索，以找到最优的参数组合。

常用参数如下，

| 参数                 | 描述                         |
|--------------------|----------------------------|
| estimator          | 用于网格搜索的估计器对象。              |
| param_grid         | 一个字典或字典列表，定义了要搜索的参数及其可能的值。 |
| scoring            | 用于评估参数组合的性能的评分策略。          |
| n_jobs             | 指定并行运行的作业数。                |
| cv                 | 交叉验证生成器或可迭代的次数。            |
| refit              | 找到最优参数后，是否用整个数据集重新训练模型。    |
| verbose            | 控制搜索过程中输出的详细程度。            |
| pre_dispatch       | 控制分派到并行执行的作业总数。            |
| error_score        | 设置在参数设置导致错误时要赋予的分数。        |
| return_train_score | 如果设置为 True，将包括训练分数。        |

使用代码：
```text
from sklearn.datasets import load_iris
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import train_test_split

# 加载数据集
iris = load_iris()
X = iris.data
y = iris.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# 设置参数网格
param_grid = {
    'n_estimators': [100, 200, 300],
    'max_depth': [5, 8, 15, None],
    'min_samples_split': [2, 5, 10],
    'min_samples_leaf': [1, 2, 4]
}

# 创建 RandomForestClassifier 实例
rf = RandomForestClassifier()

# 创建 GridSearchCV 实例
grid_search = GridSearchCV(
    estimator=rf,
    param_grid=param_grid,
    scoring='accuracy',  # 可以根据需要选择不同的评分标准
    n_jobs=-1,  # 使用所有可用的 CPU 核心
    cv=5,  # 5 折交叉验证
    verbose=2,  # 输出详细信息
    refit=True  # 使用找到的最佳参数重新训练模型
)

# 执行网格搜索
grid_search.fit(X_train, y_train)

# 输出最佳参数
print("最佳参数：", grid_search.best_params_)
print("最佳模型得分：", grid_search.best_score_)

# 使用测试集评估模型
test_accuracy = grid_search.score(X_test, y_test)
print("测试集准确率：", test_accuracy)
```
output:
```text
最佳参数： {'max_depth': 5, 'min_samples_leaf': 2, 'min_samples_split': 10, 'n_estimators': 200}
最佳模型得分： 0.9523809523809523
测试集准确率： 1.0
```
