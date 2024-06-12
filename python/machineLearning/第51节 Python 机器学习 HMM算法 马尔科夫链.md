# Python 机器学习 HMM算法 马尔科夫链

隐马尔可夫模型（HMM）是一种统计模型，用来描述一个含有隐含未知参数的马尔可夫过程。

在HMM中，系统被认为是一个马尔可夫链在不同状态之间随机转换，但每次状态转换的结果不能直接观察到，只能通过一些可观察到的输出数据间接推测。
HMM广泛应用于语音识别、自然语言处理、生物信息学等领域。

## 1、马尔科夫链简介
马尔科夫链是一种【随机过程】，其下一状态只依赖于当前状态，与之前的状态无关。

马尔科夫链是一种【数学系统】，它经历从一个状态到另一个状态的转换，其特点是下一个状态的选择仅依赖于当前状态，而与之前的状态无关。
这种特性称为【无记忆性】或【马尔科夫性质】。

马尔科夫链是一种【数学模型】，用于描述从一个状态到另一个状态的转换过程，这些转换具有“无记忆”的性质，
即未来的状态仅依赖于当前状态，与过去的状态无关。

马尔科夫链通常用于描述明确、可观测的状态序列的生成过程。
简单来说，马尔科夫链是状态序列的生成模型，其中每个状态都是直接可观测的，且转换概率是已知的。

```text
import numpy as np

# 定义状态空间
states = ["A", "B", "C"]

# 定义状态转移概率矩阵
transmat = np.array([[0.5, 0.3, 0.2],
                     [0.2, 0.5, 0.3],
                     [0.1, 0.4, 0.5]])

# 定义初始状态概率
startprob = np.array([0.6, 0.3, 0.1])

# 随机生成 10 个状态
current_state = np.random.choice(states, p=startprob)
states_sequence = [current_state]
for i in range(10):
    next_state = np.random.choice(states, p=transmat[states.index(current_state)])
    states_sequence.append(next_state)
    current_state = next_state

# 打印状态序列
print("States sequence:", states_sequence)
```
output:
```text
States sequence: ['A', 'B', 'A', 'A', 'B', 'B', 'C', 'B', 'B', 'C', 'A']
```

## 2、马尔科夫链与HMM的关系
隐马尔可夫模型是【马尔科夫链的扩展】，它引入了【隐状态】的概念。

在HMM中，系统在不同的隐状态之间转换，这些隐状态本身不直接可观测。
相反，每个隐状态会生成某些可观测的输出（观察值），观察值的分布依赖于当前的隐状态。

因此，HMM用于解决那些状态不直接可观测，但状态生成的观察值可观测的问题。
HMM的关键在于学习或推断隐状态序列以及状态转换和输出观察值的概率模型。

隐马尔可夫模型（HMM）与马尔科夫链之间存在密切的关系，归根到底，HMM是基于马尔科夫链概念发展而来的。

在HMM中，系统的【状态转换】遵循【马尔科夫性质】，即隐状态之间的转换构成了一个马尔科夫链。
不同之处在于，马尔科夫链的状态是直接可观测的，而HMM的隐状态则无法直接观测，只能通过产生的观察值间接进行推断。
这一区别导致马尔科夫链主要适用于那些状态可直接观测的场景，例如天气变化或股票价格的预测。

而HMM则适用于需要通过观测事件或结果间接推断状态的复杂场景，如语音识别和生物序列分析等。

相比之下，HMM的模型结构更为复杂，因为它不仅涉及隐状态之间的转移概率，
还包括从隐状态到观察值的生成过程，包括学习或推断观察概率和初始状态概率。

总的来说，HMM通过引入隐状态和观察值之间的关系，
在马尔科夫链的基础上进行了扩展，大大拓宽了模型的应用范围，使其能够处理那些状态不直接可观测的更加复杂的问题。

## 3、马尔科夫链案例
马尔科夫链是一种无记忆的随机过程，即下一个状态的概率仅依赖于当前状态，而与之前的状态无关。
这里有一个简化的天气预测模型，它假设天气只有两种状态：晴天（Sunny）和雨天（Rainy），
转移概率：从晴天转为晴天的概率是0.9，转为雨天的概率是0.1。
从雨天转为雨天的概率是0.5，转为晴天的概率是0.5。

```text
import numpy as np

# 定义状态和转移矩阵
states = ['Sunny', 'Rainy']
transition_matrix = np.array([[0.9, 0.1], [0.5, 0.5]])

# 初始状态
current_state = 0  # 假设从“Sunny”开始

# 预测接下来的5天天气
for i in range(5):
    current_state = np.random.choice([0, 1], p=transition_matrix[current_state])
    print(f"Day {i + 1}: {states[current_state]}")
```
output:
```text
Day 1: Sunny
Day 2: Sunny
Day 3: Sunny
Day 4: Sunny
Day 5: Sunny
```

使用马尔科夫链模拟股市模型是一个经典的机器学习案例，可以帮助我们理解和预测股市的状态转换。
可以将模拟一个股市，它只有两种状态：上涨（"Up"）和下跌（"Down"）。
使用马尔科夫链来表示这两种状态之间的转换概率。

```text
import numpy as np

# 定义状态和转换概率矩阵
states = ["Up", "Down"]
transition_matrix = np.array([[0.6, 0.4],  # P(Up|Up), P(Down|Up)
                              [0.4, 0.6]])  # P(Up|Down), P(Down|Down)

# 初始化状态
current_state = 0  # 假设初始状态为"Up"
n_days = 20  # 模拟20个交易日

# 模拟股市状态变化
for i in range(n_days):
    print(f"Day {i + 1}: Market is {'Up' if current_state == 0 else 'Down'}")
    current_state = np.random.choice([0, 1], p=transition_matrix[current_state])

# 最终状态
print(f"Final state: {'Up' if current_state == 0 else 'Down'}")
```
output:
```text
Day 1: Market is Up
Day 2: Market is Down
Day 3: Market is Down
Day 4: Market is Down
Day 5: Market is Down
Day 6: Market is Up
Day 7: Market is Down
Day 8: Market is Down
Day 9: Market is Down
Day 10: Market is Down
Day 11: Market is Down
Day 12: Market is Up
Day 13: Market is Up
Day 14: Market is Up
Day 15: Market is Up
Day 16: Market is Down
Day 17: Market is Up
Day 18: Market is Down
Day 19: Market is Down
Day 20: Market is Down
Final state: Up
```