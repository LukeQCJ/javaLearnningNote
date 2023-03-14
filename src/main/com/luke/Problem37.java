package com.luke;

import java.util.Scanner;

public class Problem37 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = Integer.parseInt(sc.nextLine()); // 每个面试官的面试场数限制
        int n = Integer.parseInt(sc.nextLine()); // 当天面试总场数
        int[][] schedule = new int[n][2];
        int[] visited = new int[n];
        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            schedule[i][0] = Integer.parseInt(input.split(" ")[0]); // 开始时间
            schedule[i][1] = Integer.parseInt(input.split(" ")[1]); // 结束时间
            visited[i] = 0;
        }
        int interviewTotalCount = 0;
        int interviewerCount = 0;
        int interviewCount = 0;
        int[] beforeActivity = null;
        do {
            for (int i = 0; i < n; i++) {
                // 如果当前面试为未开始状态，且前一次面试的结束时间小于等于当前面试的开始时间
                if (visited[i] == 0
                        && (beforeActivity == null || beforeActivity[1] <= schedule[i][0])) {
                    visited[i] = 1; // 标识第i个面试已经开始或完成
                    beforeActivity = schedule[i];
                    interviewTotalCount++; // 面试已完成计数+1
                    interviewCount++; // 某一个面试官面试场数+1
                    if (interviewCount == m) { // 如果面试场数达到上限
                        interviewerCount++; // 面试官计数+1
                        beforeActivity = null;
                        interviewCount = 0;
                        break;
                    }
                }
            }
            if (0 < interviewCount && interviewCount < m) {
                interviewerCount++;
                beforeActivity = null;
                interviewCount = 0;
            }
        } while (interviewTotalCount < n);

        System.out.println(interviewerCount);
        sc.close();
    }
}
