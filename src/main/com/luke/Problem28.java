package com.luke;

import java.util.Scanner;

public class Problem28 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int time = sc.nextInt(); // 工作最多时长
        int count = sc.nextInt(); // 可供选择的工作个数,即工作清单的工作条数
        // 初始化工作清单和报酬清单
        int[] costTimes = new int[count];
        int[] rewards = new int[count];
        for (int i = 0; i < count; i++) {
            int workTime = sc.nextInt();
            int reward = sc.nextInt();
            costTimes[i] = workTime;
            rewards[i] = reward;
        }
        int maxReward = getMaxReward(time, count, costTimes, rewards);
        System.out.println(maxReward);
        sc.close();
    }

    private static int getMaxReward(int longestWorkTime, int count, int[] costTimes, int[] rewards) {
        // dp[workNum][curTime] 表示在选择第workNum份工作的时候在curTime时间内小明能赚到的最多工资
        int[][] dp = new int[count + 1][longestWorkTime + 1];
        dp[0][0] = 0; // 表示在没有选择工作，没有花时间的工资
        for (int workNum = 1; workNum <= count; workNum++) {
            for (int curTime = 0; curTime <= longestWorkTime; curTime++) {
                // 表示在第workNum - 1份工作curTime时间内的报酬与第workNum份工作在curTime时间内的工资报酬是一样的，因为这个时候第workNum份工作还没有开始
                // 即 前一份工作结束的报酬和后一份工作开始的报酬是一样的
                dp[workNum][curTime] = dp[workNum - 1][curTime];
                if (curTime - costTimes[workNum - 1] >= 0) {
                    dp[workNum][curTime] = Math.max(dp[workNum][curTime],
                            dp[workNum - 1][curTime - costTimes[workNum - 1]] + rewards[workNum - 1]);
                }
            }
        }
        return dp[count][longestWorkTime];
    }
}
