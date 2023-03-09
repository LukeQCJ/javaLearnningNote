package com.luke;

import java.util.*;

public class Problem19 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int M = sc.nextInt();
        Set<Integer> numSet = new TreeSet<>();
        for (int i = 0; i < M; i++) {
            int num = sc.nextInt();
            numSet.add(num);
        }
        int N = sc.nextInt();
        if (2 * N > numSet.size()) {
            System.out.println(-1);
        } else {
            List<Integer> sortedList = new ArrayList<>(numSet);
            int result = 0;
            for (int i = 0; i < N; i++) {
                result += sortedList.get(i);
            }
            int count = N;
            for (int i = sortedList.size() - 1; count > 0; i--, count--) {
                result += sortedList.get(i);
            }
            System.out.println(result);
        }
    }
}
