# Python 机器学习 决策树 中文和英文特征提取

Python 机器学习中，在使用决策树模型时，对于包含中文和英文特征的数据集进行特征提取是一个常见的需求。

特征提取的【目的】是将原始数据转换为模型可以理解的格式。
通常涉及到将文本数据编码为数值数据，以及可能的降维。
有效地对包含中文和英文的数据集进行特征提取，从而为机器学习模型的训练做好准备。

## 1、对于英文特征提取
对于包含英文文本的特征，在使用决策树等机器学习模型之前，通常需要进行特征提取或转换，以将文本数据转换为模型可以处理的数值形式。
文本特征提取的目标是将文本转换为一组数值特征，这些特征能够代表原始文本的某些重要属性。

### 1） 词袋模型（Bag of Words, BoW）

词袋模型是一种简单的文本表示技术，其中每个文档被表示为一个词汇表中词语的出现次数，而不考虑词序和语法。

在词袋模型中，每个文档转换为一个长向量，此向量的长度等于词汇表中的词语数量，每个元素是特定词语在文档中出现的次数。

```text
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 示例文本数据和标签
texts = [
    "the quick brown fox jumps over the lazy dog",
    "never jump over the lazy dog quickly",
    "the quick brown fox is quick",
    "a quick brown dog outpaces a quick fox"
]
labels = [0, 0, 1, 1]  # 假设我们有两个类别：0和1

# 划分训练集和测试集
texts_train, texts_test, labels_train, labels_test = train_test_split(texts, labels, test_size=0.25, random_state=42)

# 初始化词袋模型
vectorizer = CountVectorizer()

# 使用训练数据训练词袋模型，并转换训练数据
X_train = vectorizer.fit_transform(texts_train)

# 转换测试数据（使用与训练数据相同的转换）
X_test = vectorizer.transform(texts_test)

# 训练决策树分类器
clf = DecisionTreeClassifier(random_state=42)
clf.fit(X_train, labels_train)

# 预测测试集
labels_pred = clf.predict(X_test)

# 计算准确率
accuracy = accuracy_score(labels_test, labels_pred)
print(f"Accuracy: {accuracy}")
```
output:
```text
Accuracy: 1.0
```

### 2） TF-IDF（Term Frequency-Inverse Document Frequency）

TF-IDF是一种用于信息检索和文本挖掘的常用加权技术。

TF-IDF是两个统计量的乘积：词频（TF）和逆文档频率（IDF）。
这里，词频是一个词在文档中出现的次数，逆文档频率是衡量一个词的信息量的指标，它随着词出现在越多文档中而减少。
TF-IDF值越高，这个词在当前文档中的重要性越大。

```text
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# 示例文本数据（简单的句子或文档）
documents = [
    "the sky is blue",
    "the sun is bright",
    "the sun in the sky is bright",
    "we can see the shining sun, the bright sun"
]

# 目标标签（简单起见，这里使用0和1）
y = [0, 0, 1, 1]

# 初始化TF-IDF向量化器
vectorizer = TfidfVectorizer()

# 将文本数据转换为TF-IDF特征矩阵
X = vectorizer.fit_transform(documents)

# 划分训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=42)

# 初始化决策树分类器
clf = DecisionTreeClassifier(random_state=42)

# 训练分类器
clf.fit(X_train, y_train)

# 预测测试集
y_pred = clf.predict(X_test)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)
print(f"Accuracy: {accuracy}")
```
output:
```text
Accuracy: 0.0
```

## 2、对于中文特征提取
对于包含中文特征的数据集，在使用决策树或任何其他机器学习模型之前，进行恰当的特征提取是非常关键的。
中文数据的处理可能比英文更复杂，因为中文是一种表意文字，单个字符可能就是一个有意义的单位，而且中文文本没有像英文那样的明显分词界限。

### 1）中文分词

对于处理中文文本数据，特别是在机器学习的决策树模型中，中文分词是一个非常关键的步骤。
中文与英文不同，英文单词之间有明显的空格分隔，而中文句子中的词语是连续的，没有明显的界限，因此需要通过分词技术将句子分割成词语序列。

Python中，有多个库可以用于中文分词，如jieba、HanLP、SnowNLP等。这些库使用不同的算法来识别和分割中文文本中的词语。
其中，jieba是最流行和易于使用的一个。

```text
import jieba

# 定义一段中文文本
text = "机器学习是人工智能领域中的一项重要技术。"

# 使用jieba的精确模式进行分词
words = jieba.cut(text, cut_all=False)

# 转换成列表并打印出来
words_list = list(words)
print(words_list)
```
output:
```text
['机器', '学习', '是', '人工智能', '领域', '中', '的', '一项', '重要', '技术', '。']
```

### 2）去除停用词

中文文本与英文文本处理有所不同，主要是因为中文文本需要进行分词处理，
而且中文停用词（即在文本中频繁出现但对于理解文本主题贡献不大的词，如“的”、“了”、“在”等）的去除也是提高模型性能的关键步骤。

```text
import jieba
from sklearn.feature_extraction.text import CountVectorizer

# 定义停用词
stop_words = set(["的", "了", "在", "是", "我"])

# 示例文本
texts = ["我爱北京天安门", "天安门上太阳升"]

# 分词并去除停用词
texts_processed = []
for text in texts:
    words = jieba.cut(text)
filtered_words = [word for word in words if word not in stop_words]
texts_processed.append(" ".join(filtered_words))

# 初始化CountVectorizer
vectorizer = CountVectorizer()

# 使用CountVectorizer转换文本
X = vectorizer.fit_transform(texts_processed)

# 查看特征名和特征矩阵
feature_names = vectorizer.get_feature_names()
feature_matrix = X.toarray()

# 打印结果
print("特征名:", feature_names)
print("特征矩阵:\n", feature_matrix)
```
output:
```text
特征名: ['天安门', '太阳升']
特征矩阵:
 [[1 1]]
```

### 3）特征向量化

在中文文本处理完成分词和去除停用词之后，接下来的步骤是将文本转换成一种机器学习算法可以处理的数值形式。

```text
import jieba
from sklearn.feature_extraction.text import TfidfVectorizer

# 示例中文文本
texts = ["我爱北京天安门", "天安门上太阳升", "我们爱着我们的祖国", "欢迎来到北京"]

# 分词
texts_cut = [" ".join(jieba.cut(text)) for text in texts]

# 特征向量化
tfidf_vectorizer = TfidfVectorizer()
tfidf_matrix = tfidf_vectorizer.fit_transform(texts_cut)

# 输出TF-IDF特征矩阵
print(tfidf_matrix.toarray())

# 输出特征名称
print(tfidf_vectorizer.get_feature_names())
```
output:
```text
[[0.70710678 0.70710678 0.         0.         0.         0.
  0.         0.        ]
 [0.         0.6191303  0.78528828 0.         0.         0.
  0.         0.        ]
 [0.         0.         0.         0.81649658 0.         0.
  0.40824829 0.40824829]
 [0.48693426 0.         0.         0.         0.61761437 0.61761437
  0.         0.        ]]
['北京', '天安门', '太阳升', '我们', '来到', '欢迎', '爱着', '祖国']
```

## 3、组合中英文特征提取
如数据集中同时包含中文和英文文本数据，可以分别对中英文数据进行特征提取，
然后使用诸如hstack方法（来自scipy.sparse）将两者的特征矩阵合并起来，以便在机器学习模型中使用。

对于包含中文和英文的混合数据，特征提取变得更加复杂，因为需要同时考虑中文的特点（如分词问题）和英文的处理方式。
可以将中文和英文的处理流程分开进行，最后将得到的特征向量合并为一个大的特征集，用于训练模型。

```text
import pandas as pd
from scipy.sparse import hstack
from sklearn.feature_extraction.text import CountVectorizer
import jieba

# 示例数据集
data = {
    'Chinese': ['我爱北京天安门', '机器学习很有趣', '熊猫是中国的国宝'],
    'English': ['I love machine learning', 'Pandas are cute', 'Beijing is the capital of China']
}

df = pd.DataFrame(data)

# 对中文进行分词
df['Chinese'] = df['Chinese'].apply(lambda x: " ".join(jieba.cut(x)))

# 创建英文和中文的向量化对象
vectorizer_en = CountVectorizer()
vectorizer_cn = CountVectorizer()

# 向量化英文特征
english_features = vectorizer_en.fit_transform(df['English']).toarray()

# 向量化中文特征
chinese_features = vectorizer_cn.fit_transform(df['Chinese']).toarray()

# 将中英文特征组合起来
combined_features = hstack([english_features, chinese_features])

# 显示组合后的特征
print(combined_features, vectorizer_en.get_feature_names(), vectorizer_cn.get_feature_names())
```
