# Python 机器学习 决策树 特征选择

Python机器学习中，特征选择是在构建机器学习模型时优化模型性能的重要步骤。
它通过从原始数据集中选择最相关的特征来减少模型的复杂性，提高训练速度，降低过拟合的风险，并可能提高模型的预测性能。

特征选择不仅可以提高模型的性能，还可以提供对数据和所解决问题的深入理解，特别是在【特征工程】阶段决定哪些特征是重要的。

## 1、单变量特征选择（Univariate feature selection）
单变量特征选择（Univariate feature selection）是通过基于单变量统计测试来选择最佳特征的方法。
它适用于选择与目标变量最相关的特征。

在scikit-learn中，可以使用SelectKBest或SelectPercentile类来实现单变量特征选择。

### 1）SelectKBest

SelectKBest是scikit-learn库中的一个特征选择工具，它用于从数据集中选择K个最好的特征。
特征选择是减少数据维度、提高模型准确率、减少过拟合风险、缩短训练时间的有效方法。

SelectKBest通过统计测试来评估每个特征的重要性，然后选择得分最高的K个特征。

可以指定要保留的特征数目，常用参数如下，

| 参数         | 描述                                                                                                                                                                           |
|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| score_func | 用于计算每个特征得分的函数。这个函数应该根据你的数据和问题的类型（分类或回归）来选择。常用的函数包括：对于分类问题，chi2（卡方检验）、f_classif（ANOVA F值）、mutual_info_classif（互信息）；对于回归问题，f_regression（F值回归测试）、mutual_info_regression（互信息回归）。 |
| k          | 要选择的顶部特征数量。可以是一个整数，指定选择的特征数量；也可以是'all'，选择所有特征。正确选择k的值对于模型的性能至关重要。                                                                                                            |

使用代码，

```text
from sklearn.datasets import load_iris
from sklearn.feature_selection import SelectKBest, chi2

# 步骤1：加载数据集
iris = load_iris()
X, y = iris.data, iris.target

# 步骤2：创建SelectKBest实例
selector = SelectKBest(score_func=chi2, k=2)

# 步骤3：拟合并转换数据
X_new = selector.fit_transform(X, y)

# 步骤4：显示选择的特征
print("形状变化：", X.shape, "->", X_new.shape)
print("选择的特征：", selector.get_support())
print("所有特征得分：", selector.scores_)

# 可选：显示得分最高的特征的名称（如有特征名称）
feature_names = iris.feature_names
selected_features = [feature_names[i] for i in range(len(feature_names)) if selector.get_support()[i]]
print("选择的特征名称：", selected_features)
```
output:
```text
形状变化： (150, 4) -> (150, 2)
选择的特征： [False False  True  True]
所有特征得分： [ 10.81782088   3.7107283  116.31261309  67.0483602 ]
选择的特征名称： ['petal length (cm)', 'petal width (cm)']
```

### 2）SelectPercentile

SelectPercentile是scikit-learn中一个用于特征选择的方法，它选择数据集中得分最高的百分比的特征。
这种方法通常用于降维、提高模型准确率以及提高算法运行效率。

常用参数如下，

| 参数         | 描述                                                                                                                                                       |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| score_func | 函数，用于计算特征的得分。应该接受X和y两个数组，并返回一个(scores, pvalues)元组 或一个分数数组。适用于分类问题的函数如f_classif、mutual_info_classif、chi2， 适用于回归问题的函数如f_regression、mutual_info_regression。 |
| percentile | 整数，表示要保留的特征的百分比。例如，如果设置为10，则选择得分最高的10%的特征。                                                                                                               |

使用代码，
```text
from sklearn.datasets import load_iris
from sklearn.feature_selection import SelectPercentile, f_classif
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

# 加载数据集
X, y = load_iris(return_X_y=True)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 实例化SelectPercentile，选择得分最高的50%的特征
selector = SelectPercentile(score_func=f_classif, percentile=50)

# 使用训练数据拟合并转换数据
X_train_selected = selector.fit_transform(X_train, y_train)

# 使用相同的选择器转换测试数据（不要重新拟合测试数据！）
X_test_selected = selector.transform(X_test)

# 查看原始特征数量和筛选后的特征数量
print("Original feature count:", X_train.shape[1])
print("Reduced feature count:", X_train_selected.shape[1])

# 使用随机森林分类器训练模型
clf = RandomForestClassifier(random_state=42)
clf.fit(X_train_selected, y_train)

# 预测测试集
y_pred = clf.predict(X_test_selected)

# 评估模型
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy of the model with selected features: {accuracy}')
```
output:
```text
Original feature count: 4
Reduced feature count: 2
Accuracy of the model with selected features: 1.0
```

## 2、基于模型的特征选择
基于模型的特征选择是一种使用机器学习算法本身来选择特征的方法。这种方法利用特定模型的属性来选择重要性高的特征。
这不仅可以提高模型的性能，还可以提供对数据的深入理解。

在scikit-learn中，可以通过SelectFromModel来实现基于模型的特征选择。

常用参数如下，

| 参数	          | 描述                                                            |
|--------------|---------------------------------------------------------------|
| estimator	   | 用于特征选择的学习器。该评估器应该已经被训练过，并且具有 coef_ 或 feature_importances_ 属性。 |
| threshold	   | 用于选择特征的阈值。特征的重要性得分高于这个阈值的会被选中。                                |
| prefit       | 如果为 True，则假定传入的评估器已经被训练过，将直接进行特征选择。                           |
| norm_order   | 在评估特征的重要性时使用的范数顺序。仅在评估器的 coef_ 是多维的时候使用。                      |
| max_features | 要选择的最大特征数量。如果不为 None， 则在满足阈值条件的特征中进一步限制选择的特征数量。               |

使用代码，
```text
from sklearn.datasets import load_iris
from sklearn.ensemble import RandomForestClassifier
from sklearn.feature_selection import SelectFromModel

# 加载数据集
X, y = load_iris(return_X_y=True)

# 创建随机森林分类器并训练
estimator = RandomForestClassifier(n_estimators=100, random_state=42)
estimator.fit(X, y)

# 使用SelectFromModel进行特征选择
sfm = SelectFromModel(estimator=estimator, threshold='median', prefit=True, max_features=2)
X_transformed = sfm.transform(X)

# 查看结果
print("Original number of features:", X.shape[1])
print("Number of features after selection:", X_transformed.shape[1])
print("Selected features:", sfm.get_support())
```
output:
```text
Original number of features: 4
Number of features after selection: 2
Selected features: [False False  True  True]
```

## 3、递归特征消除（RFE）
递归特征消除（Recursive Feature Elimination, RFE）是一种特征选择方法，旨在通过递归减少特征集的规模来找出最有影响力的特征。
它是通过递归地构建模型并选择最重要的特征（基于权重），去掉最不重要的特征，然后在剩余的特征上重复这个过程，直到达到指定的特征数量为止。

RFE的实现在sklearn.feature_selection.RFE类中，

常用参数如下，

| 参数	                   | 描述                                                                    |
|-----------------------|-----------------------------------------------------------------------|
| estimator	            | 实现了fit方法并具有coef_或feature_importances_属性的学习器。用来训练数据并获取特征的重要性。          |
| n_features_to_select	 | 要选择的特征数量。如果未指定，则默认选择一半的特征。                                            |
| step	                 | 每次迭代要移除的特征数。可以是大于0的整数或在0到1之间的浮点数。如果是浮点数，则表示要移除的特征的比例。                 |
| verbose	              | 控制执行的冗余程度。其值越高，日志信息越多。                                                |
| importance_getter	    | 字符串或可调用函数，用于获取estimator的属性以获取特征的重要性。默认使用coef_或feature_importances_属性。 |

使用代码，
```text
from sklearn.datasets import make_friedman1
from sklearn.feature_selection import RFE
from sklearn.svm import SVR

# 生成一个示例数据集
X, y = make_friedman1(n_samples=100, n_features=10, random_state=0)

# 创建一个SVM回归器作为估计器
estimator = SVR(kernel="linear")

# 创建RFE模型。选择5个特征，每次迭代移除1个特征
selector = RFE(estimator, n_features_to_select=5, step=1, verbose=0)

# 在数据上训练RFE模型
selector.fit(X, y)

# 打印选中的特征（返回一个布尔数组）
print("Selected features:", selector.support_)

# 打印特征的排名（1表示被选中的特征）
print("Feature ranking:", selector.ranking_)
```
output:
```text
Selected features: [ True  True False  True  True False False  True False False]
Feature ranking: [1 1 4 1 1 5 6 1 2 3]
```

## 4、使用特征选择改进决策树模型
特征选择是机器学习中减少模型复杂度、提高效率和性能的有效方法。

在决策树模型中，通过选择最重要的特征，可以提升模型的准确性，同时减少过拟合的风险。

在Python的scikit-learn库中，有多种特征选择方法可用于改进决策树模型。
选择可以直接应用于决策树模型，以选择最有信息量的特征。对于提高模型的泛化能力和减少计算成本非常有效。

```text
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.feature_selection import SelectKBest, chi2, SelectFromModel
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score

# 加载数据
iris = load_iris()
X, y = iris.data, iris.target

# 划分数据集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 使用SelectKBest选择最好的K个特征
k = 3
select_k_best = SelectKBest(chi2, k=k)
X_train_k_best = select_k_best.fit_transform(X_train, y_train)
X_test_k_best = select_k_best.transform(X_test)

# 使用基于模型的特征选择
model_for_selection = DecisionTreeClassifier()
select_from_model = SelectFromModel(model_for_selection)
X_train_model_based = select_from_model.fit_transform(X_train, y_train)
X_test_model_based = select_from_model.transform(X_test)

# 训练决策树模型（SelectKBest）
clf_k_best = DecisionTreeClassifier()
clf_k_best.fit(X_train_k_best, y_train)
predictions_k_best = clf_k_best.predict(X_test_k_best)
accuracy_k_best = accuracy_score(y_test, predictions_k_best)

# 训练决策树模型（SelectFromModel）
clf_model_based = DecisionTreeClassifier()
clf_model_based.fit(X_train_model_based, y_train)
predictions_model_based = clf_model_based.predict(X_test_model_based)
accuracy_model_based = accuracy_score(y_test, predictions_model_based)

# 输出结果
print(f"Accuracy with top {k} features (SelectKBest): {accuracy_k_best:.2f}")
print(f"Accuracy with model-based selection (SelectFromModel): {accuracy_model_based:.2f}")
```
output:
```text
Accuracy with top 3 features (SelectKBest): 1.00
Accuracy with model-based selection (SelectFromModel): 1.00
```