# 华为机试 

具体有哪些参考题目：见 [华为OD机试题目](https://blog.csdn.net/u012657708/article/details/131076402)。

38.数字排列


## 经验

一定要读懂题意，不然没法做。

一共考了华为OD机试题2次，有分为A卷和B卷。

我一直考的都是B卷，题型的话：
- 第一题：【动态规划 或者 回溯】类的题，这部分都是比较难的题，我比较欠缺的。
- 第二题：【对象排序 或者top K】相关的题，解决思路主要是在使用优先队列等集合，写对象排序的逻辑，排序逻辑条件一般是多条件。
- 第三题：【图论 或 矩阵】相关的题 或者 【根据二叉树遍历结果，然后重构二叉树】，主要是考察二叉树各种遍历思维、以及图论的【深度优先】、【广度优先】等思路，以及一些额外的条件遍历。


```text
机考介绍
1. 机考为三道算法题目，难度为2道简单，1道中等，需要自己处理输入输出，分值为100分、100分、200分。总分400分，平台在牛客。
2. 摄像头一定要开，避免离开座位，左右晃头，以免系统误判作弊。
3. 考试过程须控制好做题节奏，切记不要在一道题目上花费过多的时间，合理分配时间。

刷题建议
1. 遇到不会的题怎么办？
万事开头难，好在算法的比我们之前做的数学题死板多了。一开始遇到题目不会是非常非常常见
的，我建议遇到不会的题目或者自己想了半天还没有思路的题目，这么做：
    1. 直接看评论区答案（牛客按照热度降序，LeetCode按照点赞数降序，注意LeetCode有时候官
    方题解非常绕，可以优先看非官方的）。争取记住看答案后就明白这么做的原因然后背住大
    概的思路。
    2. 第二天按照自己的记忆和理解再刷一遍这个题。
    3. 第五天再刷一次。
就是说按照遗忘曲线的规律来刷题，并且整理套路（套路可以看评论区的高票答案）。
我觉得刷题不仅在多，而且还要让我们刷的每一个题目有价值。
其实刷题多了就会发现大多数题目套路都是一致的。

2. 题目都有好多解法，怎么办？
对于让人眼前一亮，属于奇技淫巧，我强烈建议记下来学习一波。
大多数情况下可以参考下面题型汇总，对于题型汇总中的高权重题型，建议掌握下这个方法，非高频的以后再说。

3. 牛客还是LeetCode？
两个平台在我看来都可以，但是考虑到最终机考在牛客，
如果平时比较习惯刷LeetCode，一定要在牛客上下面的高频考题都刷几题，熟悉输入输出。下面附上刷题链接。

牛客
https://www.nowcoder.com/exam/oj?tab=%E7%AE%97%E6%B3%95%E7%AF%87&topicId=196&page=1
可以根据知识点来过滤题型

牛客-华为考题
https://www.nowcoder.com/ta/huawei

LeetCode
https://leetcode-cn.com/problemset/all/
可以根据标签来过滤题型

4. 要不要写输入输出？
答案是要的。虽然现在很多牛客的题目没要求构造输入输出（牛客叫核心代码模式），但是实际
考试还是需要的（牛客叫ACM模式），平时刷题需要注意区分，多刷需要自己构造的。尤其是二叉
树之类的题目，输入输出构造会比较花时间，练习的时候多留意输入输出构造，机试可以节省很
多时间。

5. 怎么鉴定我刷题成果？
请参考下文的必会题目，如果对于这些题目都有思路并且能解答，那么恭喜你，可以准备考试
了！

6. 实际、独立、按时练习
实际：不要只看解法，要实际去练，脑和手并不一致。
独立：独立完成，事后再看解析，做题过程不要看答案。
按时：全心投入，100分题要在40分钟内完成答题，200分题要在70分钟内完成

题型汇总
按照算法的难度和频率大致可以分为必会和进阶两种类型，每个子项排序表示考察的频率，序号越低，
考察频率越高，比如数组是考察频率最高的。大家实际刷题中可以根据题库提供的功能筛选，按照热
度、题解数等进行筛选。

必会
1. 数组（遍历、排序、二分、双指针、滑动窗口）
2. 字符串
3. 排序
4. 贪心（有题型）
5. 递归
6. 循环
7. 滑窗（有题型）
8. 栈
9. 进制转换
10. 位运算
11. 队列
12. 哈希表（有题型）
13. 链表
14. 线性表
15. 二分查找（有题型）

进阶
1. 图
2. 树
3. DFS搜索（有题型）
4. BFS搜索（有题型）
5. 动态规划（有题型）
6. 前缀和（有题型）
7. 排列组合
8. 矩阵
9. 双指针（有题型）
10. 回溯（有题型）
11. 状态机
12. 并查集
13. 正则表达式
14. 分治
15. 枚举
16. 统计

建议
1. 必会部分知识点倾向于出现在100分题中，进阶知识点倾向于出现在200分题中。
建议必会部分优先掌握1-10知识点，进阶部分优先掌握1-4知识点，这部分出现频次高，短时间内刷题性价比高。
2. 对于进阶部分，图往往伴随着深度优先和广度优先出现，
我建议优先广度优先深度优先、二叉树的遍历（能应付二叉树路径统计等题型）。
其余有精力再准备。对于链表、广度优先和深度优先，
LeetCode和牛客上有很多现成的答题模板，大家可以当做公式一样进行参考。

参考套路
1. 史上最全遍历二叉树详解
https://leetcode-cn.com/problems/binary-tree-preorder-traversal/solution/leetcodesuan-fa-xiu-lian-dong-hua-yan-shi-xbian-2/
2. BFS算法框架详解
https://leetcode-cn.com/problems/open-the-lock/solution/wo-xie-liao-yi-tao-bfs-suan-fa-kuang-jia-jian-dao-/
3. 图文详解 BFS, DFS
https://leetcode-cn.com/circle/article/YLb5l4/
4. labuladong 的算法小抄
https://github.com/labuladong/fucking-algorithm
比较全，但是建议只看题型汇总中的高频题型

必会题目
题目的答案语言可能比较单一,大家可以根据题目内容进行关键字搜索,找到自己语言的答案,
答案为别人写的,不是官方参考答案,大家可以当做有思路的参考。这部分题目一定要知道具体的做法。
搜索题目的时候发现了https://blog.nowcoder.net/zhuanlan/v0Eoqj这篇专栏，不建议大家购买，
但是里面的题目描述的确比较详细，大家可以参考，然后根据描述去搜索答案。
这部分的题目尽量都要掌握。
1. 字符串分割
https://leetcode-cn.com/circle/discuss/niKSMZ/
2. 组成最大数
https://python.iitter.com/%E9%A6%96%E9%A1%B5/248622.html
3. 统计射击比赛成绩
http://www.amoscloud.com/?p=3561
4. 字符串序列判定
https://www.nowcoder.com/questionTerminal/5382ff24fbf34a858b15f93e2bd85307
5. 数据分类
http://www.amoscloud.com/?p=2414
6. 5键键盘的输出
https://blog.nowcoder.net/n/c7bb482cddb647b5965c2f55ef13f7da
7. 检查是否存在满足条件的数字组合
http://www.amoscloud.com/?p=2825
8. 数组拼接
https://cxybb.com/article/weixin_41398052/106045155
9. 数列描述
https://blog.nowcoder.net/n/fc9be58c1a994072af9a77cd25cd3411
10. 考勤信息
http://www.amoscloud.com/?p=3038
11. 按单词下标区间翻转文章内容
https://blog.nowcoder.net/n/c157854438cc46629f0e5e33a94a4988
12. 最大括号深度
https://blog.nowcoder.net/n/316f6f2d6b494e28a1e4ca81b0a76988
13. 字符串加密
https://www.codeleading.com/article/89584473419/
14. 整数对最小和
http://www.4k8k.xyz/article/u013598405/114239804#%E7%AC%AC%E4%BA%8C%E9%A2%98%C2%A0%20%E6%95%B4%E6%95%B0%E5%AF%B9%E6%9C%80%E5%B0%8F%E5%92%8C
15. 求字符串中所有整数的最小和
https://ask.csdn.net/questions/7423645
16. 乱序整数序列两数之和绝对值最小
http://www.amoscloud.com/?p=3257
17. 非严格递增连续数字序列
http://www.amoscloud.com/?p=2964
18. 分积木
https://blog.nowcoder.net/n/36e682ed2a0a455cbbfa4dc4dd24e280
19. 连续字母长度
http://www.amoscloud.com/?p=3034
20. 滑动窗口最大和
https://leetcode-cn.com/problems/sliding-window-maximum/
21. 素数之积
https://icode.best/i/62685042254334
22. 仿LISP运算
https://www.codetd.com/article/6784237
23. 贪吃蛇
https://blog.nowcoder.net/n/42420d1a2d324c32838f7f23e4da45f3
24. 解密犯罪时间
https://www.its203.com/article/weixin_44224529/117932485?2022-03-31
25. 求满足条件的最长子串的长度
https://www.jianshu.com/p/edc1efd18a67
26. 机器人走迷宫
https://blog.nowcoder.net/n/0bcd2d2047f4464bae29dedd5104c308?from=nowcoder_improve
27. 高效的任务规划
https://leetcode-cn.com/circle/discuss/EC2mv8/view/zT3KcL/
28. 二叉树遍历
https://www.codeleading.com/article/43985735740/
29. 书籍叠放
https://www.codeleading.com/article/11185696007/
30. 区间交集
https://blog.nowcoder.net/n/fd28c4bd1367426eb973a3e62e79a24e
31. 分月饼
https://javamana.com/2021/12/20211206054724872r.html
32. 找最小数
https://blog.nowcoder.net/n/60b56945100944cc987cefdd02db1b08
33. 简易内存池
https://blog.51cto.com/u_15127575/3271270
34. 服务失效判断
https://blog.nowcoder.net/n/82d7ba4e145e48e2b203b1ee63d9b153
35. 图像物体的边界
https://leetcode-cn.com/circle/discuss/Bu1fD6/view/oIQ50T/
36. 跳格子游戏
https://blog.nowcoder.net/n/8971e4919a324ada973297044015397e
37. 数组二叉树
https://www.idchg.com/info/86685/
题目可以参考这个博客，不 建议购买https://blog.nowcoder.net/n/b3f4a031eea2422c9c42e0f71dc6e161
38. 考古学家
https://blog.nowcoder.net/n/9876a8a7bf104ec7ab88c350f268b8de
39. 解压报文
https://www.codeleading.com/article/50015743571/
40. 最长的指定瑕疵度的元音子串
https://www.icode9.com/content-1-1259108.html
41. 目录删除
https://www.cnblogs.com/skyshi/p/15969831.html
42. 火锅
https://cdmana.com/2022/03/202203260546548598.html
65. 服务器广播
https://www.codeleading.com/article/14505696344/
66. 二叉树的广度优先遍历
https://www.cnblogs.com/gcter/p/15469584.html
67. 找单词
https://blog.nowcoder.net/n/581f8f196a8a4f98a0a05334daa4b6b1?from=nowcoder_improve
68. 招聘
http://www.noobyard.com/article/p-vvnvrixx-px.html
某公司组织一场公开招聘活动...
69. 斗地主之顺子
https://blog.nowcoder.net/n/d2039c7bb5d74c7da969f731f036d700

参考题目
牛客 https://www.nowcoder.com/ta/huawei

简单题
1. HJ12 字符串反转
2. HJ11 数字颠倒
3. HJ54 表达式求积
4. HJ106 字符逆序
5. HJ76 尼科彻斯定力
6. HJ75 公共子串计算
7. HJ86 求最大连续bit数
8. HJ85 最长回文子串
9. HJ100 等差数列
10. HJ87 密码强度等级

中等题
1. HJ10 字符个数统计
2. HJ46 截取字符串
3. HJ60 查找组成一个偶数最接近的两个素数
4. HJ40 统计字符
5. HJ14 字符串排序
6. HJ5 进制转换
7. HJ59 找出字符串中第一个只出现一次的字符
8. HJ58 输入n个整数，输出其中最小的K个
9. HJ81 字符串字符匹配
LeetCode https://leetcode-cn.com
1. 剑指offer 62 题： 圆圈中最后剩下的数字
2. 3：无重复字符的最长子串

采用滑窗法
3. 14：最长公共前缀
4. 151：翻转字符串里的词
5. 2047：字符串中的单词数
6. 581：最短无序连续子数组
7. 1071：字符串的最大公因子
8. 1111：有效括号的嵌套度
9. 面试题 17.08： 马戏团人
```

