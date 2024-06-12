# Python 机器学习 HMM算法 维特比算法和鲍姆-韦尔奇算法

在机器学习和信号处理领域，特别是在处理隐马尔可夫模型（Hidden Markov Models, HMM）时，
维特比算法（Viterbi Algorithm）和鲍姆-韦尔奇算法（Baum-Welch Algorithm）是两个核心算法，用于不同的目的。
维特比算法用于解决解码问题，即在给定观察序列的情况下找到最可能的状态序列。
它通过动态规划来实现，广泛应用于需要从观测中推断隐含状态序列的场景。
鲍姆-韦尔奇算法用于HMM的参数学习，是一种EM算法的实现。
它通过迭代优化模型参数，使得模型最好地解释观测数据，适用于当模型参数未知，需要从数据中学习这些参数的情况。

## 1、维特比算法（Viterbi Algorithm）
维特比算法是一种动态规划算法，用于在给定观察序列的情况下，找到最可能的状态序列（即解码问题）。
简而言之，它解决的是给定观测序列和模型参数时，确定最有可能产生这些观测的隐藏状态序列的问题。
维特比算法广泛应用于语音识别、生物信息学（如DNA序列分析）和其他领域，
其中需要从一系列观测数据中推断出最可能的隐含状态序列。

算法通过构建一个路径概率图（通常是一个表格），利用动态规划逐步计算和记录每个状态的最大概率路径。
对于每个时间点，它记录下达到该状态的最大概率以及达到该概率的路径。
最终，算法回溯这些记录，找到概率最高的路径作为最可能的状态序列。

```text
import numpy as np

# 定义模型参数
A = np.array([[0.7, 0.3], [0.4, 0.6]])  # 状态转移概率矩阵
B = np.array([[0.1, 0.9], [0.8, 0.2]])  # 观察概率矩阵
pi = np.array([0.5, 0.5])  # 初始状态概率分布
observations = [0, 1, 0]  # 观察序列，0: 携带伞, 1: 不携带伞


# 实现维特比算法
def viterbi(A, B, pi, observations):
    N = A.shape[0]  # 状态数量

    T = len(observations)  # 观察序列长度
    V = np.zeros((N, T))  # 存储每一步的最大概率
    path = np.zeros((N, T), dtype=int)  # 存储路径

    # 初始化
    V[:, 0] = pi * B[:, observations[0]]

    # 动态规划
    for t in range(1, T):
        for n in range(N):
            seq_probs = V[:, t - 1] * A[:, n] * B[n, observations[t]]
            V[n, t] = np.max(seq_probs)
            path[n, t] = np.argmax(seq_probs)

    # 回溯找到最优路径
    best_path = np.zeros(T, dtype=int)
    best_path[-1] = np.argmax(V[:, -1])
    for t in range(T - 2, -1, -1):
        best_path[t] = path[best_path[t + 1], t + 1]

    return best_path, np.max(V[:, -1])


# 应用维特比算法
best_path, best_prob = viterbi(A, B, pi, observations)

# 转换状态序列为文字
state_names = ['晴天', '雨天']
best_path_names = [state_names[state] for state in best_path]

print(best_path_names, best_prob)
```
output:
```text
['雨天', '晴天', '雨天'] 0.03456000000000001
```

## 2、鲍姆-韦尔奇算法（Baum-Welch Algorithm）
鲍姆-韦尔奇算法是一种特殊的期望最大化（Expectation-Maximization, EM）算法，
用于未知参数的统计模型中参数的估计，特别是在HMM中。它主要用于学习给定观察序列时HMM的参数（即训练HMM）。
鲍姆-韦尔奇算法被用于从观测数据中学习HMM的模型参数，例如状态转移概率、观测概率和初始状态概率。
它在语言模型、遗传序列分析等领域有广泛应用。

算法交替执行两个步骤，期望步骤（E步）和最大化步骤（M步）。
在E步，算法使用当前的模型参数估计隐藏状态的概率分布；
在M步，算法更新模型参数以最大化在E步中计算得到的期望对数似然。
这个过程反复迭代，直到模型参数收敛。

```text
import numpy as np

# 随机初始化模型参数
np.random.seed(42)  # 确保示例的可重复性
A_init = np.random.rand(2, 2)
B_init = np.random.rand(2, 2)
pi_init = np.random.rand(2)
# 归一化
A_init /= A_init.sum(axis=1)[:, np.newaxis]
B_init /= B_init.sum(axis=1)[:, np.newaxis]
pi_init /= pi_init.sum()

# 假设的观察序列（0: X, 1: Y）
observations = np.random.choice(2, 10)


# 鲍姆-韦尔奇算法（Baum-Welch Algorithm）
def baum_welch(observations, A_init, B_init, pi_init, iterations=100):
    N = A_init.shape[0]  # 状态数量

    M = B_init.shape[1]  # 观察符号数量
    T = len(observations)  # 观察序列长度

    for _ in range(iterations):
        # E步骤：使用当前参数计算前向概率和后向概率
        alpha = np.zeros((N, T))
        beta = np.zeros((N, T))
        alpha[:, 0] = pi_init * B_init[:, observations[0]]
        beta[:, -1] = 1

        # 计算alpha
        for t in range(1, T):
            for j in range(N):
                alpha[j, t] = np.sum(alpha[:, t - 1] * A_init[:, j]) * B_init[j, observations[t]]

        # 计算beta
        for t in range(T - 2, -1, -1):
            for i in range(N):
                beta[i, t] = np.sum(A_init[i, :] * B_init[:, observations[t + 1]] * beta[:, t + 1])

        # 计算gamma和xi（辅助变量）
        gamma = np.zeros((N, T))
        xi = np.zeros((N, N, T - 1))
        for t in range(T - 1):
            denom = np.sum([alpha[i, t] * beta[i, t] for i in range(N)])
            for i in range(N):
                gamma[i, t] = (alpha[i, t] * beta[i, t]) / denom
                for j in range(N):
                    xi[i, j, t] = (alpha[i, t] * A_init[i, j] * B_init[j, observations[t + 1]] * beta[j, t + 1]) / denom
        gamma[:, -1] = alpha[:, -1] * beta[:, -1] / np.sum(alpha[:, -1] * beta[:, -1])

        # M步骤：更新参数
        pi_init = gamma[:, 0]
        A_init = np.sum(xi, 2) / np.sum(gamma[:, :-1], axis=1)[:, np.newaxis]
        for k in range(M):
            mask = observations == k
            B_init[:, k] = np.sum(gamma[:, mask], axis=1) / np.sum(gamma, axis=1)

    return A_init, B_init, pi_init


# 应用鲍姆-韦尔奇算法
A_final, B_final, pi_final = baum_welch(observations, A_init, B_init, pi_init)

print(A_final, B_final, pi_final)
```
output:
```text
[[7.74180351e-04 9.99225820e-01]
 [9.84521691e-01 1.54783089e-02]] [[2.03063722e-001 7.96936278e-001]
 [6.43568328e-135 1.00000000e+000]] [3.21433807e-243 1.00000000e+000]
```

## 3、应用示例
Python中应用维特比算法和鲍姆-韦尔奇算法通常涉及使用隐马尔可夫模型（HMM）。
这两种算法在机器学习领域中有广泛应用，例如在语音识别、自然语言处理、生物信息学等领域。

### 1）维特比算法应用

如我们有一个简单的天气模型，状态包括晴朗和雨天，观测包括人们穿的是T恤、夹克或雨衣。
我们想要根据观测序列来预测最可能的天气序列。

```text
# 状态集合
states = ['晴朗', '雨天']
# 观测序列
observations = ['夹克', 'T恤', '雨衣']
# 观测集合
obs_dict = {'T恤': 0, '夹克': 1, '雨衣': 2}
# 状态转移概率
trans_prob = {'晴朗': {'晴朗': 0.7, '雨天': 0.3}, '雨天': {'晴朗': 0.4, '雨天': 0.6}}
# 观测概率
emit_prob = {'晴朗': {'T恤': 0.6, '夹克': 0.3, '雨衣': 0.1}, '雨天': {'T恤': 0.1, '夹克': 0.4, '雨衣': 0.5}}
# 初始状态概率
start_prob = {'晴朗': 0.5, '雨天': 0.5}


def viterbi(obs, states, start_p, trans_p, emit_p):
    V = [{}]
    for st in states:
        V[0][st] = {"prob": start_p[st] * emit_p[st][obs[0]], "prev": None}
    for t in range(1, len(obs)):
        V.append({})
        for st in states:
            max_tr_prob = max(V[t - 1][prev_st]["prob"] * trans_p[prev_st][st] for prev_st in states)
            for prev_st in states:
                if V[t - 1][prev_st]["prob"] * trans_p[prev_st][st] == max_tr_prob:
                    max_prob = max_tr_prob * emit_p[st][obs[t]]
                    V[t][st] = {"prob": max_prob, "prev": prev_st}
                    break
    opt = []
    max_prob = max(value["prob"] for value in V[-1].values())
    previous = None
    for st, data in V[-1].items():
        if data["prob"] == max_prob:
            opt.append(st)
            previous = st
            break
    for t in range(len(V) - 2, -1, -1):
        opt.insert(0, V[t + 1][previous]["prev"])
        previous = V[t + 1][previous]["prev"]

    print('最可能的天气序列是: ' + ' '.join(opt))
    print('序列概率: {:.5f}'.format(max_prob))


# 应用Viterbi算法
viterbi(observations, states, start_prob, trans_prob, emit_prob)
```
output:
```text
最可能的天气序列是: 晴朗 晴朗 雨天
序列概率: 0.00945
```

### 2）鲍姆-韦尔奇算法应用

如有一个系统，它在两种状态下运行（例如“高效”和“低效”），并且能够观察到的输出是某种性能指标（例如“高”、“中”、“低”）。
我们的目标是基于观察到的性能指标序列来学习状态转移概率、观测概率和初始状态概率。

```text
import numpy as np

# 定义状态空间
states = ["高效", "低效"]

# 定义观测空间
observations = ["高", "中", "低"]

# 定义初始状态概率
startprob = np.array([0.6, 0.4])

# 定义状态转移概率矩阵
transmat = np.array([[0.7, 0.3],
                     [0.2, 0.8]])

# 定义观测概率矩阵
emissionprob = np.array([[0.4, 0.3, 0.3],
                         [0.2, 0.5, 0.3]])

# 观测序列
observed_sequence = ["高", "中", "低", "高", "中"]


# 使用鲍姆-韦尔奇算法训练模型
def baum_welch(observations, startprob, transmat, emissionprob):
    n_states = len(states)
    n_observations = len(observations)

    # 初始化
    alpha = np.zeros((n_states, n_observations))
    beta = np.zeros((n_states, n_observations))
    gamma = np.zeros((n_states, n_observations))
    xi = np.zeros((n_states, n_states, n_observations - 1))

    # 前向算法
    alpha[:, 0] = startprob * emissionprob[:, observations[0]]
    for t in range(1, n_observations):
        for i in range(n_states):
            alpha[i, t] = (alpha[:, t - 1] * transmat[i, :]) * emissionprob[i, observations[t]]

    # 后向算法
    beta[:, n_observations - 1] = np.ones(n_states)
    for t in range(n_observations - 2, -1, -1):
        for i in range(n_states):
            beta[i, t] = (transmat[:, i] * emissionprob[:, observations[t + 1]] * beta[:, t + 1]).sum()

    # 计算gamma和xi
    for t in range(n_observations):
        for i in range(n_states):
            gamma[i, t] = alpha[i, t] * beta[i, t] / alpha[:, t].sum()
            for j in range(n_states):
                xi[i, j, t] = alpha[i, t] * transmat[i, j] * emissionprob[j, observations[t + 1]] * beta[j, t + 1] / alpha[:, t].sum()

    # 更新参数
    startprob = gamma[:, 0]
    transmat = xi.sum(axis=2) / gamma[:, :-1].sum(axis=1)[:, None]
    emissionprob = (gamma[:, None, :] * observations).sum(axis=2) / gamma.sum(axis=1)[:, None]

    return startprob, transmat, emissionprob


# 训练模型
startprob, transmat, emissionprob = baum_welch(observed_sequence, startprob, transmat, emissionprob)

# 打印结果
print("初始状态概率:", startprob)
print("状态转移概率矩阵:", transmat)
print("观测概率矩阵:", emissionprob)
```
