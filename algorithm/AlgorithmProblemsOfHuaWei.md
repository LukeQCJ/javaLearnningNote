# 华为机试

## 第1题：
**题目：集五福作为近年来大家喜闻乐见迎新春活动，集合爱国福、富强福、和谐福、友善福、敬业福即可分享超大红包.**
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;以0和1组成的长度为5的字符串代表每个人所得到的福卡，每一位代表一种福卡，1表示已经获得该福卡，单类型福卡不超过1张，随机抽取一个小于10人团队，求该团队最多可以集齐多少套五福？
>
> **输入描述:**
> 
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;输入若干个"11010"、”00110"的由0、1组成的长度等于5位字符串,代表的指定团队中每个人福卡获得情况
> 
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注意1：1人也可以是一个团队
> 
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注意2：1人可以有0到5张福卡，但福卡不能重复
>
> **输出描述:**
> 
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;输出该团队能凑齐多少套五福

> **示例1:**
> 
> 输入: 
> 11001，11101
> 
> 输出: 
> 0
>
> **示例2:**
> 
> 输入:
> 11101，10111
> 
> 输出:
> 1

**代码：**
```
import java.util.Arrays;
import java.util.Scanner;
 
/**
 * 
 */
public class Main {
 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String[] split = input.split(",");
        int[] counts = new int[]{0, 0, 0, 0, 0};
        for (int i = 0; i < split.length; i++) {
            char[] ticket = split[i].toCharArray();
            for (int j = 0; j < ticket.length; j++) {
                if (ticket[j] == '1') {
                    counts[j]++;
                }
            }
        }
        Arrays.sort(counts);
        System.out.println(counts[0]);
    }
}
```

## 第2题：
> 题目:【按索引范围翻转文章片段】
> 
> 输入一个英文文章片段，翻转指定区间的单词顺序，标点符号和普通字母一样处理。例如输入字符串“I am a developer.”，区间[0，3]，则输出“developer.a am I”。
>
> 输入描述：
> 使用换行隔开三个参数，第一个参数为英文文章内容即英文字符串，第二个参数为翻转起始单词下标（下标从0开始），第三个参数为结束单词下标。
>
> 输出描述：
> 翻转后的英文文章片段，所有单词之间以一个半角空格分隔进行输出。
>
> 示例1：
> 
> 输入：
> 
> I am a developer.
> 
> 1
> 
> 2
>
> 输出：
> 
> I a am developer.
