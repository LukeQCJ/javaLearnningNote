package com.luke;

import java.util.*;

public class Problem1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        // 所有人员的卡片 字符串
        String[] memberArray = input.split(",");
        int[] counts = new int[]{0, 0, 0, 0, 0};
        // 累加各种的类型的卡片和在counts数组
        for (String cardStr : memberArray) {
            char[] cardArray = cardStr.toCharArray();
            for (int j = 0; j < cardArray.length; j++) {
                if (cardArray[j] == '1') {
                    counts[j]++;
                }
            }
        }
        Arrays.sort(counts);
        // 最少数量的类型的卡片数，就是该团队最多能凑齐的福卡套数
        System.out.println(counts[0]);
        sc.close();
    }
}
