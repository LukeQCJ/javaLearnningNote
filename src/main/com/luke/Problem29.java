package com.luke;

import java.util.*;

public class Problem29 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        josephusCircle(100, m);
        sc.close();
    }

    public static void josephusCircle(int n, int m) {
        Queue<Integer> queue = new LinkedList<>();
        // 加入数据
        for (int i = 1; i <= n; i++) {
            queue.add(i);
        }
        // 开始游戏
        int p = 1; // 报数计数器
        while (queue.size() >= m) {
            Integer temp = queue.poll();
            if (p < m) {
                queue.add(temp);
                p++; // 报数+1
            } else {
                p = 1; // 报数重置为1
            }
        }
        // 打印输出
        while (!queue.isEmpty()) {
            Integer temp = queue.poll();
            System.out.print(temp + ",");
        }
        System.out.println();
    }
}
