# 华为机试

## 第1题：
集五福作为近年来大家喜闻乐见迎新春活动，集合爱国福、富强福、和谐福、友善福、敬业福即可分享超大红包

以0和1组成的长度为5的字符串代表每个人所得到的福卡，每一位代表一种福卡，1表示已经获得该福卡，单类型福卡不超过1张，随机抽取一个小于10人团队，求该团队最多可以集齐多少套五福？

输入描述:
输入若干个"11010"、”00110"的由0、1组成的长度等于5位字符串,代表的指定团队中每个人福卡获得情况
注意1：1人也可以是一个团队
注意2：1人可以有0到5张福卡，但福卡不能重复

输出描述:
输出该团队能凑齐多少套五福

示例1
输入
11001，11101
输出
0

示例2
输入
11101，10111
输出
1


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
