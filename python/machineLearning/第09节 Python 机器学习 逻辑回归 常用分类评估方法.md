# Python 机器学习 逻辑回归 常用分类评估方法

Python 机器学习中，逻辑回归是一种广泛用于分类问题的算法。
对于逻辑回归模型的评估，有多种方法可以衡量其性能。
评估方法可以提供不同角度的模型性能信息，有助于理解模型在特定任务上的表现。
在实际应用中，选择哪种评估方法取决于具体问题的需求和数据的特性。

## 1、准确率（Accuracy）
准确率是最简单、最直观的分类评估指标，它计算的是所有分类正确的样本占总样本的比例。
虽然准确率是一个很直观的评估指标，但它在某些情况下可能会产生误导。
特别是在数据集不平衡的情况下，即一个类的样本数量远多于另一个类的样本数量。
在这种情况下，即使模型只是简单地将所有样本预测为多数类，也可能得到很高的准确率，但这并不意味着模型具有好的分类性能。

公式表示为：
```text
(TP+TN)/(TP+FN+FP+TN)
```

| 指标	                  | 描述                 |
|----------------------|--------------------|
| TP（True Positives）	  | 真正例，即模型正确预测为正类的数量。 |
| TN（True Negatives）	  | 真负例，即模型正确预测为负类的数量。 |
| FP（False Positives）	 | 假正例，即模型错误预测为正类的数量。 |
| FN（False Negatives）	 | 假负例，即模型错误预测为负类的数量。 |

示例代码：
```text
from sklearn.datasets import load_iris
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 加载数据集
iris = load_iris()
X = iris.data
y = iris.target

# 因为逻辑回归是用于二分类问题，我们这里只取两个类别的数据
# 选择类别为0和1的数据
X = X[y != 2]
y = y[y != 2]

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 创建逻辑回归模型实例
log_reg = LogisticRegression()

# 训练模型
log_reg.fit(X_train, y_train)

# 进行预测
y_pred = log_reg.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')
```

## 2、混淆矩阵（Confusion Matrix）
混淆矩阵是一个表格，用于评估分类模型的性能，它显示了实际类别与模型预测类别的对应情况。

混淆矩阵包含四个部分：

| 指标	                       | 描述                    |
|---------------------------|-----------------------|
| 真正例（True Positives, TP）	  | 模型正确预测为正类的数量。         |
| 假正例（False Positives, FP）	 | 模型错误预测为正类的数量（实际上是负类）。 |
| 真负例（True Negatives, TN）	  | 模型正确预测为负类的数量。         |
| 假负例（False Negatives, FN）	 | 模型错误预测为负类的数量（实际上是正类）。 |

基于混淆矩阵，我们可以计算出几个关键的性能指标，如精确率（Precision）、召回率（Recall）和F1分数（F1 Score）。

混淆矩阵（Confusion Matrix）是一种特别有用的工具，用于评估分类模型的性能，它展示了实际值与模型预测值之间的关系。

示例代码：
```text
from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import confusion_matrix
import matplotlib.pyplot as plt
import seaborn as sns

# 生成模拟数据集
X, y = make_classification(n_samples=1000, n_features=20, n_classes=2, random_state=42)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=42)

# 初始化逻辑回归模型
model = LogisticRegression()

# 训练模型
model.fit(X_train, y_train)

# 预测测试集
y_pred = model.predict(X_test)

# 生成混淆矩阵
cm = confusion_matrix(y_test, y_pred)

# 使用Seaborn绘制混淆矩阵
plt.figure(figsize=(10, 7))
sns.heatmap(cm, annot=True, fmt='d', cmap='Blues')
plt.xlabel('Predicted')
plt.ylabel('True')
plt.title('Confusion Matrix')
plt.show()
```

## 3、精确率（Precision）和召回率（Recall）
精确率是预测为正例的样本中，实际为正例的比例，而召回率是实际为正例的样本中，被正确预测为正例的比例。

【精确率】是指在所有被模型预测为正类的样本中，真正属于正类的样本所占的比例。
公式如下：
```text
Precision = TP/TP+FP
```

【召回率】是指在所有真实为正例的样本中，被模型正确预测为正例的样本所占的比例。
公式如下：
```text
Recall = TP/TP+FN
```

精确率高时，表示模型在预测正类时更加准确，但可能会错过一些真正的正类样本。
召回率高时，表示模型能找回更多的真正的正类样本，但同时也可能将更多的负类样本错误地预测为正类。

实际应用中，根据业务需求的不同，可能会更关注精确率或召回率。
例如，在垃圾邮件检测中，可能会更倾向于提高精确率以避免重要邮件被错误地分类为垃圾邮件；
而在疾病筛查中，可能会更倾向于提高召回率以确保尽可能少地错过任何病例。

示例代码：
```text
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import precision_score, recall_score
from sklearn.datasets import load_breast_cancer

# 加载乳腺癌数据集
data = load_breast_cancer()
X, y = data.data, data.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 训练逻辑回归模型
model = LogisticRegression(penalty='l2', C=1.0, solver='lbfgs', max_iter=5000, random_state=42)
model.fit(X_train, y_train)

# 进行预测
y_pred = model.predict(X_test)

# 计算精确率和召回率
precision = precision_score(y_test, y_pred)
recall = recall_score(y_test, y_pred)

print(f"Precision: {precision}")
print(f"Recall: {recall}")
```
output:
```text
Precision: 0.9459459459459459
Recall: 0.9859154929577465
```

## 4、F1 分数（F1 Score）
F1 分数是精确率和召回率的【调和平均数】，同时考虑精确率和召回率，能够平衡这两个指标，尤其是在数据集不平衡的情况下非常有用。
F1 分数的公式如下：
```text
F1 = 2 × (Precision × Recall /Precision + Recall)
```

F1分数的取值范围为0到1，其中1表示模型的性能最好，0表示性能最差。F1分数能够平衡精确率和召回率，使得模型在这两个方面都相对表现良好。

在实际应用中，F1分数是一个非常重要的指标，尤其是在处理不平衡数据集时，它可以提供比单纯的精确率或召回率更全面的性能评估。

示例代码：
```text
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import f1_score
from sklearn.datasets import load_breast_cancer

# 加载乳腺癌数据集
data = load_breast_cancer()
X, y = data.data, data.target

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 训练逻辑回归模型
model = LogisticRegression(penalty='l2', C=1.0, solver='lbfgs', max_iter=5000, random_state=42)
model.fit(X_train, y_train)

# 进行预测
y_pred = model.predict(X_test)

# 计算F1分数
f1 = f1_score(y_test, y_pred)

print(f'F1 Score: {f1}')
```
output:
```text
F1 Score: 0.9655172413793103
```

## 5、ROC 曲线和 AUC 分数
ROC 曲线（受试者操作特征曲线）是一个图形化的评估方法，它展示了在不同阈值下，模型的真正例率（TPR）和假正例率（FPR）之间的关系。
AUC 分数（曲线下面积）衡量了ROC曲线下的整体区域大小，提供了一个将模型性能总结为单一数值的方法。

ROC曲线（Receiver Operating Characteristic curve）和AUC分数（Area Under the Curve）是评估分类模型性能的两个关键指标，
尤其在评估模型的区分能力方面。

### 1）ROC曲线

ROC曲线是一个用于评价二分类系统性能的图表，
它通过将真正率（TPR, True Positive Rate）和假正率（FPR, False Positive Rate）以图形方式表示出来，提供了一种判断模型优劣的直观方法。

真正例率 (TPR) 定义为真正例数除以实际正例数，即TPR = TP / (TP + FN)，其中TP是真正例的数量，FN是假负例的数量。

假正例率 (FPR) 定义为假正例数除以实际负例数，即FPR = FP / (FP + TN)，其中FP是假正例的数量，TN是真负例的数量。

ROC曲线的横轴是假正率（FPR），纵轴是真正率（TPR）。曲线越接近左上角，表示模型的性能越好。

### 2）AUC分数

AUC分数衡量的是ROC曲线下的面积大小，它是一个介于0到1之间的数值。
AUC分数可以被解释为模型随机选择一个正例和一个负例时，正例的得分高于负例得分的概率。AUC分数越接近于1，模型的分类性能就越好。

### 3）示例代码
```text
import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import roc_curve, auc

# 生成模拟数据
X = np.array([[1, 2], [3, 4], [5, 6], [7, 8], [9, 10]])
y = np.array([0, 1, 1, 0, 1])

# 训练逻辑回归模型
model = LogisticRegression()
model.fit(X, y)

# 计算预测概率
y_pred_proba = model.predict_proba(X)[:, 1]

# 计算 ROC 曲线和 AUC 值
fpr, tpr, thresholds = roc_curve(y, y_pred_proba)
auc = auc(fpr, tpr)

# 绘制 ROC 曲线
plt.plot(fpr, tpr, label="ROC curve (AUC = %0.2f)" % auc)
plt.legend()
plt.draw()
plt.show()
```
