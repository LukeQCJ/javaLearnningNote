package com.luke;

import java.util.Scanner;

public class Problem28 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int time = sc.nextInt();
        int count = sc.nextInt();
        // 初始化工作清单和报酬清单
        int[] times = new int[count];
        int[] rewards = new int[count];
        for (int i = 0; i < count; i++) {
            int workTime = sc.nextInt();
            int reward = sc.nextInt();
            times[i] = workTime;
            rewards[i] = reward;
        }
        // dp[i][j] 表示在选择第i份工作的时候在j时间内小明能赚到的最多工资
        int[][] dp = new int[count + 1][time + 1];
        dp[0][0] = 0; // 表示在没有选择工作，没有花时间的工资
        for (int i = 1; i <= count; i++) {
            for (int j = 0; j <= time; j++) {
                // 表示在第i - 1份工作j时间内的报酬与第i份工作在j时间内的工资报酬是一样的，因为这个时候第i份工作还没有开始
                // 即 前一份工作结束的报酬和后一份工作开始的报酬是一样的
                dp[i][j] = dp[i - 1][j];
                if (j - times[i - 1] >= 0) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - times[i - 1]] + rewards[i - 1]);
                }
            }
        }
        System.out.println(dp[count][time]);
        sc.close();
    }
}
