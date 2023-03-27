package com.luke;

import java.util.*;

public class Problem23 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        System.out.println(combine(n, k));
        sc.close();
    }

    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if (k <= 0 || n < k) {
            return result;
        }
        Deque<Integer> path = new ArrayDeque<>();
        backTrack(n, k, 1, path, result);
        return result;
    }

    public static void backTrack(int n, int k, int start, Deque<Integer> path, List<List<Integer>> res) {
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i <= n; i++) {
            path.addLast(i);
            backTrack(n, k, i + 1, path, res);
            path.removeLast();
        }
    }
}
