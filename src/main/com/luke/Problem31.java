package src.main.com.luke;

import java.util.*;

public class Problem31 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        System.out.println(getPermutation(n, k));
        sc.close();
    }

    private static String ans = "";
    private static int K;

    public static String getPermutation(int n, int k) {
        boolean[] visited = new boolean[n + 1];
        K = k;
        backTrack(n, new StringBuilder(), visited);
        return ans;
    }

    private static boolean backTrack(int n, StringBuilder sb, boolean[] visited) {
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
            //剪纸，找到答案了就直接返回，不要继续了
            if (backTrack(n, sb, visited)) {
                return true;
            }
            visited[i] = false;
            sb.deleteCharAt(sb.length() - 1);
        }
        return false;
    }
}
