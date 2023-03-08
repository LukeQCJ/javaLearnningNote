package com.luke;

import java.util.Scanner;

public class Problem8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 输入NE
        String NEString = sc.nextLine();
        String[] NEArray = NEString.split(" ");
        int N = Integer.parseInt(NEArray[0]);
        int E = Integer.parseInt(NEArray[1]);
        long area = 0; // 面积
        int lastX = 0; // 前一个绘制点的横坐标,初始在原点，值为0
        int lastY = 0; // 前一个绘制点的纵坐标,初始在原点，值为0
        for (int i = 0; i < N; i++) {
            // 输入指令X offsetY
            String command = sc.nextLine();
            String[] wordArray = command.split(" ");
            // 当前点的横坐标
            int X = Integer.parseInt(wordArray[0]);
            // 纵坐标的偏移量
            int offsetY = Integer.parseInt(wordArray[1]);
            // 纵坐标 = 前一个节点的纵坐标lastY + 纵坐标偏移量offsetY
            int Y = lastY + offsetY;
            // 面积累加
            area += (long) (X - lastX) * Math.abs(lastY);
            // 前一个绘制更新
            lastX = X;
            lastY = Y;
        }
        area += (long) (E - lastX) * Math.abs(lastY);
        System.out.println(area);
        sc.close();
    }
}
