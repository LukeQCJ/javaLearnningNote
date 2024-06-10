# Python 机器学习 决策树 信息增益和信息增益率

在决策树算法中，【信息增益（Information Gain）】和【信息增益率（Gain Ratio）】是两种常用的标准来选择【最佳分裂特征】。
它们都是基于熵（Entropy）的概念，用于【量化】通过选择某个特征进行分裂后数据集不确定性的减少。

信息增益倾向于选择取值较多的特征，而信息增益率则试图减少这种偏好，但有时可能会过分偏向于取值较少的特征。

在实际应用中，选择哪种标准取决于具体问题和数据集的特性。

## 1、信息增益（Information Gain）

信息增益（Information Gain，IG）是指由于分割而导致数据集不确定性（熵）的减少。
信息增益是选择数据分割点（即决策树节点）的一种方法。

信息增益基于熵的概念，旨在选择能够最有效减少数据集不确定性的特征。

公式代码如下，

```text
import numpy as np


def entropy(y):
    """计算给定数据集的熵"""
    class_counts = np.bincount(y)
    probabilities = class_counts / len(y)
    return -np.sum([p * np.log2(p) for p in probabilities if p > 0])


def information_gain(y, y_left, y_right):
    """计算分割后的信息增益"""
    parent_entropy = entropy(y)
    left_entropy = entropy(y_left)
    right_entropy = entropy(y_right)
    n = len(y)
    n_left = len(y_left)
    n_right = len(y_right)
    ig = parent_entropy - (n_left / n) * left_entropy - (n_right / n) * right_entropy
    return ig


# 示例数据
y = np.array([0, 0, 1, 1, 1, 1, 0])  # 原始数据集标签
y_left = np.array([0, 0, 1])  # 分割后的左子集标签
y_right = np.array([1, 1, 0, 0])  # 分割后的右子集标签

# 计算信息增益
ig = information_gain(y, y_left, y_right)
print("信息增益:", ig)
```
output:
```text
信息增益: 0.020244207153756077
```

## 2、分裂信息量（Split Information）

在决策树算法中，除了使用熵（Entropy）来衡量数据的混乱程度外，还可以使用分裂信息量（Split Information）来衡量特征的分裂能力。
分裂信息量越小，表示特征的分裂能力越好。

分裂信息量的计算通常是使用特征的信息增益（Information Gain）来衡量的。

信息增益是熵与分裂后的子节点的熵之差。

在计算信息增益时，也需要考虑分裂后每个子节点的样本比例。

分裂信息是用来衡量按某特征分裂数据时产生的子集分布的“广度”和“均匀度”。

特征的每个值都会创建一个子集，分裂信息量化了这些子集大小的不均衡程度。

公式代码如下，

```text
import math


def entropy(p):
    if p == 0 or p == 1:
        return 0
    return -p * math.log2(p) - (1 - p) * math.log2(1 - p)


def information_gain(p_parent, p_children, ent_children):
    ent_parent = entropy(p_parent)
    ent_children_combined = sum((p * ent for p, ent in zip(p_children, ent_children)))
    return ent_parent - ent_children_combined


def split_information(p_children):
    return sum((-p * math.log2(p) if p != 0 else 0) for p in p_children)


# 示例：假设分裂后两个子节点的样本比例分别为0.6和0.4
p_children = [0.6, 0.4]
# 计算分裂信息量
split_info = split_information(p_children)
print("分裂信息量为:", split_info)
```
output:
```text
分裂信息量为: 0.9709505944546686
```

## 2、信息增益率（Gain Ratio）

信息增益率是信息增益和分裂信息量的比值，通过使用信息增益率，决策树算法能够更平衡地选择分裂属性，有助于构建更准确和有效的预测模型。

信息增益率是对信息增益的一个改进，旨在减少对具有大量值的特征的偏好。

```text
import math


def entropy(data):
    """计算数据集的熵"""
    total = len(data)
    label_counts = {}
    for feat_vec in data:
        current_label = feat_vec[-1]
        if current_label not in label_counts:
            label_counts[current_label] = 0
        label_counts[current_label] += 1
    ent = 0.0
    for key in label_counts:
        prob = float(label_counts[key]) / total
        ent -= prob * math.log2(prob)
    return ent


def split_dataset(data, axis, value):
    """根据特定特征分割数据集"""
    ret_data_set = []
    for feat_vec in data:
        if feat_vec[axis] == value:
            reduced_feat_vec = feat_vec[:axis]
            reduced_feat_vec.extend(feat_vec[axis + 1:])
            ret_data_set.append(reduced_feat_vec)
    return ret_data_set


def gain_ratio(data, feature_i):
    """计算给定特征的信息增益率"""
    total = len(data)
    feat_values = {example[feature_i] for example in data}
    info_gain = entropy(data)
    split_info = 0.0
    for value in feat_values:
        sub_data_set = split_dataset(data, feature_i, value)
        prob = len(sub_data_set) / float(total)
        info_gain -= prob * entropy(sub_data_set)
        split_info -= prob * math.log2(prob)
    if split_info == 0:  # 避免除以0
        return 0
    return info_gain / split_info


# 示例数据集
data = [[1, 'yes'],
        [1, 'yes'],
        [1, 'no'],
        [0, 'no'],
        [0, 'no']]

# 如我们想要按照第一个特征（索引为0）计算信息增益率
feature_index = 0

print("信息增益率:", gain_ratio(data, feature_index))
```
output:
```text
信息增益率: 0.4325380677663126
```