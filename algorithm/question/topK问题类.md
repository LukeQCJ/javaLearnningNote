# 题目一：
```text
2512. 奖励最顶尖的 K 名学生

给你两个字符串数组 positive_feedback 和 negative_feedback ，分别包含表示正面的和负面的词汇。不会 有单词同时是正面的和负面的。

一开始，每位学生分数为 0 。每个正面的单词会给学生的分数 加 3 分，每个负面的词会给学生的分数 减  1 分。

给你 n 个学生的评语，用一个下标从 0 开始的字符串数组 report 和一个下标从 0 开始的整数数组 student_id 表示，其中 student_id[i] 表示这名学生的 ID ，这名学生的评语是 report[i] 。每名学生的 ID 互不相同。

给你一个整数 k ，请你返回按照得分 从高到低 最顶尖的 k 名学生。如果有多名学生分数相同，ID 越小排名越前。

 
示例 1：
输入：
    positive_feedback = ["smart","brilliant","studious"], 
    negative_feedback = ["not"], 
    report = ["this student is studious","the student is smart"], 
    student_id = [1,2], 
    k = 2
输出：
    [1,2]
解释：
两名学生都有 1 个正面词汇，都得到 3 分，学生 1 的 ID 更小所以排名更前。

示例 2：
输入：
    positive_feedback = ["smart","brilliant","studious"], 
    negative_feedback = ["not"], 
    report = ["this student is not studious","the student is smart"], 
    student_id = [1,2], 
    k = 2
输出：
    [2,1]
解释：
- ID 为 1 的学生有 1 个正面词汇和 1 个负面词汇，所以得分为 3-1=2 分。
- ID 为 2 的学生有 1 个正面词汇，得分为 3 分。
学生 2 分数更高，所以返回 [2,1] 。
 

提示：
1 <= positive_feedback.length, negative_feedback.length <= 104
1 <= positive_feedback[i].length, negative_feedback[j].length <= 100
positive_feedback[i] 和 negative_feedback[j] 都只包含小写英文字母。
positive_feedback 和 negative_feedback 中不会有相同单词。
n == report.length == student_id.length
1 <= n <= 104
report[i] 只包含小写英文字母和空格 ' ' 。
report[i] 中连续单词之间有单个空格隔开。
1 <= report[i].length <= 100
1 <= student_id[i] <= 109
student_id[i] 的值 互不相同 。
1 <= k <= n
```
解题：
```java
package com.luke;

import java.util.*;

public class Test {

    public static void main(String[] args) {
        String[] positive_feedback =
                {"yewmhbgnq", "vqhhuaejqw"};
        String[] negative_feedback =
                {"zjeyq", "oyuetqe", "ks", "vuvannpwa", "qfd", "xv", "aauvtxrdt", "gml", "eaky", "mwip"};
        String[] report =
                {"lqiuderzod mwip vqhhuaejqw xrn aqjzkqjsi riuood yewmhbgnq xv nmcvqm onhkkmy", "uccz yewmhbgnq rcxdaqicbe vqhhuaejqw yewmhbgnq e vqhhuaejqw b yewmhbgnq vqhhuaejqw", "eaky yewmhbgnq eaky dwdzl yewmhbgnq yewmhbgnq ntqpnqtmnb qfd ks gvumi", "ecutvv vqhhuaejqw vqhhuaejqw inu vqhhuaejqw vqhhuaejqw uu eodzum zjeyq fxebx", "vqhhuaejqw m vuvannpwa mwip atvjp vqhhuaejqw eaky yewmhbgnq vqhhuaejqw yewmhbgnq"};
        int[] student_id =
                {581094748, 245764367, 905556075, 127263030, 415068015};

        List<Integer> topK = topStudents(positive_feedback, negative_feedback, report, student_id, 4);
        for (int i : topK) {
            System.out.println(i);
        }
    }

    public static List<Integer> topStudents(
            String[] positive_feedback, String[] negative_feedback,
            String[] report, int[] student_id, int k) {
        // 使用哈希表存放所有单词，key为单词，value为分数 3或-1
        Map<String, Integer> words = new HashMap<>();
        for (String word : positive_feedback) {
            words.put(word, 3);
        }
        for (String word : negative_feedback) {
            words.put(word, -1);
        }
        // 使用堆进行解决问题，主要是排序
        Queue<int[]> queue =
                new PriorityQueue<>((o1, o2) -> {
                    int scoreRes = Integer.compare(o1[1], o2[1]);
                    int idRes = Integer.compare(o1[0], o2[0]);
                    return scoreRes == 0 ? idRes : - scoreRes;
                });
        // 将元素放入堆
        for (int i = 0; student_id.length > i; i++) {
            int score = getScore(words, report[i]);
            queue.add(new int[] {student_id[i], score});
        }
        // 取出K个元素，即为topK
        List<Integer> topK = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            topK.add(Objects.requireNonNull(queue.poll())[0]);
        }
        return topK;
    }

    public static int getScore(Map<String, Integer> wordMap,
                        String comment) {
        String[] words = comment.split(" ");
        int score= 0;
        for (String word : words) {
            score += wordMap.getOrDefault(word, 0);
        }
        return score;
    }
}
```