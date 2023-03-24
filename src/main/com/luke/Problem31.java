package src.main.com.luke;

import java.util.*;

public class Problem31 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        // 回溯
        System.out.println(getPermutation(n, k));
        // 回溯 + 剪枝
        System.out.println(getPermutation2(n, k));
        // 回溯 + 剪枝 + 减少内存
        System.out.println(getPermutation3(n, k));
        sc.close();
    }

    public static String getPermutation(int n, int k) {
        boolean[] visited = new boolean[n + 1];
        List<String> ansList = new ArrayList<>();
        backTrack(n, new StringBuilder(), visited, ansList);
        return ansList.get(k - 1);
    }

    private static void backTrack(int n, StringBuilder path, boolean[] visited, List<String> ansList) {
        if (path.length() == n) {
            ansList.add(path.toString());
            return;
        }
        for (int i = 1; i <= n; i++) {
            if (visited[i]) {
                continue;
            }
            path.append(i);
            visited[i] = true;
            backTrack(n, path, visited, ansList);
            visited[i] = false;
            path.deleteCharAt(path.length() - 1);
        }
    }

    public static String getPermutation2(int n, int k) {
        boolean[] visited = new boolean[n + 1];
        List<String> ansList = new ArrayList<>();
        backTrack2(n, k, new StringBuilder(), visited, ansList);
        return ansList.get(k - 1);
    }

    private static boolean backTrack2(int n, int k, StringBuilder path, boolean[] visited, List<String> ansList) {
        if (path.length() == n) {
            ansList.add(path.toString());
            return ansList.size() == k;
        }
        for (int i = 1; i <= n; i++) {
            if (visited[i]) {
                continue;
            }
            path.append(i);
            visited[i] = true;
            if (backTrack2(n, k, path, visited, ansList)) {
                return true;
            }
            visited[i] = false;
            path.deleteCharAt(path.length() - 1);
        }
        return false;
    }

    private static String ans = "";
    private static int K;

    public static String getPermutation3(int n, int k) {
        boolean[] visited = new boolean[n + 1];
        K = k;
        backTrack3(n, new StringBuilder(), visited);
        return ans;
    }

    private static boolean backTrack3(int n, StringBuilder sb, boolean[] visited) {
        if (sb.length() == n) {
            if (--K == 0) {
                ans = sb.toString();
                return true;
            }
            return false;
        }
        for (int i = 1; i <= n; i++) {
            if (visited[i]) {
                continue;
            }
            sb.append(i);
            visited[i] = true;
            //剪枝，找到答案了就直接返回，不要继续了
            if (backTrack3(n, sb, visited)) {
                return true;
            }
            visited[i] = false;
            sb.deleteCharAt(sb.length() - 1);
        }
        return false;
    }
}
