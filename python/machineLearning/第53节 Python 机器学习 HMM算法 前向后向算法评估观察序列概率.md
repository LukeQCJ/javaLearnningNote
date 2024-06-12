# Python 机器学习 HHM算法 前向后向算法评估观察序列概率

机器学习和统计模型中，尤其是在处理隐马尔可夫模型（Hidden Markov Models, HMM）时，
前向后向算法是一个重要的算法，用于评估给定模型参数下观察序列的概率。
这个算法实际上包含两部分：前向算法（Forward Algorithm）和后向算法（Backward Algorithm），
它们分别用于计算特定时间点前所有可能状态序列的概率（前向）和之后所有可能状态序列的概率（后向）。

## 1、前向算法
前向算法是隐马尔可夫模型（HMM）中用于计算观测序列概率的一种算法。
它是解决HMM的评估问题的有效方法，旨在确定给定模型参数时，某个特定观测序列出现的概率。
前向算法通过动态规划逐步构建前向概率表，并最终得到整个观测序列的概率。

```text
import numpy as np


def forward_algorithm(observations, state_transition_matrix, emission_matrix, initial_state_distribution):
    num_observations = len(observations)

    num_states = state_transition_matrix.shape[0]

    # 初始化前向概率矩阵
    alpha = np.zeros((num_observations, num_states))

    # 初始化
    alpha[0, :] = initial_state_distribution * emission_matrix[:, observations[0]]

    # 递归
    for t in range(1, num_observations):
        for i in range(num_states):
            alpha[t, i] = np.dot(alpha[t - 1, :], state_transition_matrix[:, i]) * emission_matrix[i, observations[t]]

    # 终止
    prob = np.sum(alpha[num_observations - 1, :])
    
    return prob, alpha


# 示例使用
# 定义HMM模型参数
state_transition_matrix = np.array([[0.7, 0.3], [0.4, 0.6]])
emission_matrix = np.array([[0.2, 0.4, 0.4], [0.5, 0.4, 0.1]])
initial_state_distribution = np.array([0.6, 0.4])
observations = [0, 1, 2]  # 观测序列，这里假设观测值已转换为索引

# 计算
prob, alpha = forward_algorithm(observations, state_transition_matrix, emission_matrix, initial_state_distribution)
print("Probability of the observed sequence:", prob)
```
output:
```text
Probability of the observed sequence: 0.034064000000000004
```

## 2、后向算法
后向算法（Backward Algorithm）是一种用于隐马尔可夫模型（HMM）的算法，
主要用于计算给定模型参数情况下，观测序列的概率。
与前向算法相似，后向算法也是一种动态规划算法，用于高效计算观测序列的概率，但是它是从序列的末尾开始向前计算概率值。

```text
import numpy as np


def backward_algorithm(A, B, initial_dist, observations):
    num_states = A.shape[0]

    T = len(observations)

    # 初始化后向概率矩阵
    beta = np.zeros((num_states, T))
    beta[:, T - 1] = 1  # 在最后一个观测，所有状态的后向概率都设置为1

    # 递归计算后向概率
    for t in reversed(range(T - 1)):
        for i in range(num_states):
            beta[i, t] = sum(A[i, j] * B[j, observations[t + 1]] * beta[j, t + 1] for j in range(num_states))

    # 计算整个观测序列的概率
    total_prob = sum(initial_dist[i] * B[i, observations[0]] * beta[i, 0] for i in range(num_states))

    return beta, total_prob


# 示例使用的HMM参数和观测序列
A = np.array([[0.7, 0.3], [0.4, 0.6]])
B = np.array([[0.2, 0.4, 0.4], [0.5, 0.4, 0.1]])
initial_dist = np.array([0.6, 0.4])
observations = [0, 1, 2]  # 整数索引代表观测

beta, total_prob = backward_algorithm(A, B, initial_dist, observations)
print("后向概率矩阵:\n", beta)
print("观测序列的总概率:", total_prob)
```
output:
```text
后向概率矩阵:
 [[0.1132 0.31   1.    ]
 [0.1024 0.22   1.    ]]
观测序列的总概率: 0.034064
```

## 3、应用
前向后向算法通常用于计算给定模型参数下，整个观察序列出现的概率。这通过将最终时间点的所有前向概率相加来完成。
确定最可能的隐藏状态序列。虽然解码通常使用Viterbi算法，但前向后向算法提供了必要的概率框架来进行解码。
调整模型参数以最大化给定观察序列的概率。
通过Baum-Welch算法（也称为前向后向算法的一个特例）进行。

```text
import numpy as np

# 定义模型参数
A = np.array([[0.7, 0.3], [0.4, 0.6]])  # 状态转移概率矩阵
B = np.array([[0.1, 0.9], [0.8, 0.2]])  # 观察概率矩阵
pi = np.array([0.5, 0.5])  # 初始状态概率分布

# 观察序列（用数字表示，0:携带伞, 1:不携带伞）
observations = [0, 1, 0]


# 前向算法
def forward(A, B, pi, observations):
    N = A.shape[0]  # 状态数量
    T = len(observations)  # 观察序列长度
    fwd = np.zeros((N, T))
    fwd[:, 0] = pi * B[:, observations[0]]
    for t in range(1, T):
        for n in range(N):
            fwd[n, t] = np.sum(fwd[:, t - 1] * A[:, n]) * B[n, observations[t]]
    return fwd


# 后向算法
def backward(A, B, observations):
    N = A.shape[0]  # 状态数量
    T = len(observations)  # 观察序列长度
    bwd = np.zeros((N, T))
    bwd[:, -1] = 1
    for t in range(T - 2, -1, -1):
        for n in range(N):
            bwd[n, t] = np.sum(bwd[:, t + 1] * A[n, :] * B[:, observations[t + 1]])
    return bwd


# 计算前向概率
fwd = forward(A, B, pi, observations)
prob_fwd = np.sum(fwd[:, -1])

# 计算后向概率
bwd = backward(A, B, observations)
prob_bwd = np.sum(pi * B[:, observations[0]] * bwd[:, 0])

print(fwd, prob_fwd, bwd, prob_bwd)
```
output:
```text
[[0.05     0.1755   0.014325]
 [0.4      0.051    0.0666  ]] 0.08092500000000002 [[0.2265 0.31   1.    ]
 [0.174  0.52   1.    ]] 0.08092500000000001
```