package com.luke;

import java.util.Arrays;
import java.util.Scanner;

public class Problem18 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String heightString = sc.nextLine();
        String weightString = sc.nextLine();
        String[] heights = heightString.split(" ");
        String[] weights = weightString.split(" ");
        student[] sts = new student[n];
        for (int i = 0; i < n; i++) {
            sts[i] = new student(i + 1, Integer.parseInt(heights[i]), Integer.parseInt(weights[i]));
        }
        // 排序
        Arrays.sort(sts, (o1, o2) -> o1.height == o2.height ? o1.weight - o2.weight : o1.height - o2.height);

        for (student s : sts) {
            System.out.print(s.num + " ");
        }
    }

    static class student {
        int num;
        int height;
        int weight;

        public student(int n, int h, int w) {
            this.num = n;
            this.height = h;
            this.weight = w;
        }
    }
}
