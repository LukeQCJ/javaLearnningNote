package com.luke;

import java.util.*;

public class Problem23 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        System.out.println(combine(n, k));
        System.out.println(combine2(n, k));
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
        // 为什么i要从start开始，因为防止重复
        for (int i = start; i <= n; i++) {
            path.addLast(i);
            backTrack(n, k, i + 1, path, res);
            path.removeLast();
        }
    }

    public static List<List<Integer>> combine2(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if (k <= 0 || n < k) {
            return result;
        }
        Deque<Integer> path = new ArrayDeque<>();
        backTrack2(n, k, 1, path, result);
        return result;
    }

    public static void backTrack2(int n, int k, int start, Deque<Integer> path, List<List<Integer>> res) {
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }
        // i 要从start开始，因为防止重复；
        // i 循环到 n - (k - path.size() + 1)后就没必要继续了，因为后面都不能满足获取k个元素；
        for (int i = start; i <= n - (k - path.size()) + 1; i++) {
            path.addLast(i);
            backTrack2(n, k, i + 1, path, res);
            path.removeLast();
        }
    }
}
