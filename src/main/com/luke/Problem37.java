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
        int interviewerQuantity = getLeastInterviewerQuantity(m, n, schedule, visited);
        // 打印面试官个数
        System.out.println(interviewerQuantity);
        sc.close();
    }

    private static int getLeastInterviewerQuantity(int limitInterviewCount, int n, int[][] schedule, int[] visited) {
        if (schedule == null) {
            return 0;
        }
        int interviewTotalCount = 0; // 面试场数
        int interviewerCount = 0; // 面试官人数
        int curInterviewerInterviewCount = 0; // 当前面试官的面试场数
        int[] beforeActivity = null;
        do {
            for (int i = 0; i < n; i++) {
                if (schedule[i] == null) {
                    return 0;
                }
                // 如果当前面试为未开始状态，且前一次面试的结束时间小于等于当前面试的开始时间，则可以进行面试
                if (visited[i] == 0
                        && (beforeActivity == null || beforeActivity[1] <= schedule[i][0])) {
                    visited[i] = 1; // 标识第i个面试已经开始或完成
                    beforeActivity = schedule[i];
                    interviewTotalCount++; // 面试场数+1
                    curInterviewerInterviewCount++; // 当前面试官的面试场数+1
                    if (curInterviewerInterviewCount == limitInterviewCount) { // 如果当前面试官的面试场数达到上限，则新增一个面试官
                        interviewerCount++; // 面试官计数+1
                        beforeActivity = null; // 状态变量复位
                        curInterviewerInterviewCount = 0; // 状态变量复位
                        break;
                    }
                }
            }
            // 当有的面试官因为面试的时间范围只能面试少于m次时，比如有的面试的结束时间晚于在其他或者某个面试的开始时间
            if (0 < curInterviewerInterviewCount && curInterviewerInterviewCount < limitInterviewCount) {
                interviewerCount++;
                beforeActivity = null; // 状态变量复位
                curInterviewerInterviewCount = 0; // 状态变量复位
            }
        } while (interviewTotalCount < n);
        return interviewerCount;
    }
}
