package src.main.com.luke;

import java.util.Scanner;

public class Problem32 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s1 = sc.nextLine();
        String[] arr1 = s1.split(" ");
        int M = Integer.parseInt(arr1[0]); // 故障确认周期
        int T = Integer.parseInt(arr1[1]); // 故障次数门限
        int P = Integer.parseInt(arr1[2]); // 故障恢复周期数
        String s2 = sc.nextLine();
        String[] arr2 = s2.split(" ");
        int[] S = new int[arr2.length];
        for (int i = 0; i < arr2.length; i++) {
            S[i] = Integer.parseInt(arr2[i]);
        }
        System.out.println(getLongestContinuousCycleNum(M,T,P,S));
        for (int j : S) {
            System.out.print(j + " ");
        }
        System.out.println();
        sc.close();
    }

    /**
     *
     * @param m 故障确认周期
     * @param t 故障次数门限
     * @param p 故障恢复周期数
     * @param s 采样数据序列
     * @return int 正确值的最长连续周期数
     */
    public static int getLongestContinuousCycleNum(int m, int t, int p, int[] s) {
        boolean isBroken = false; // 是否故障
        int maxLen = 0;
        int continuousCycleValidValueCount = 0; // 最长连续生命周期计数
        int curErrorValueCount = 0; // 当前m个周期中错误值的个数
        int curValidContinuousValueCount = 0; // 故障后的正确值计数
        int curContinuousCycleCount = 0; // 当前连续周期计数
        for (int i = 0; i < s.length; i++) {
            curContinuousCycleCount++;
            if (!isValid(s,i)) {
                int lastValidValue = findLastValidValue(s,i);
                if (lastValidValue == -1) {
                    curErrorValueCount++;
                    continue;
                } else { // 如果找到最近一个正确值，则当前值=最近一个有效值，即当前值为有效
                    s[i] = lastValidValue;
                }
                // 故障后，开始统计故障后的正确值的个数
                if (isBroken && isValid(s,i)) {
                    curValidContinuousValueCount++;
                }
                // 判断 采集工具 故障恢复：连续p次正确数据
                if (isBroken && curValidContinuousValueCount >= p) {
                    isBroken = false;
                    // 故障恢复后，丢弃故障恢复之前的数据
                    continuousCycleValidValueCount = 0;
                    curErrorValueCount = 0;
                    curValidContinuousValueCount = 0;
                    continue;
                }
                // 判断 采集工具 故障：m个周期内t次错误数据
                if (!isBroken && curErrorValueCount >= t && curContinuousCycleCount <= m) {
                    isBroken = true;
                    maxLen = Math.max(maxLen,continuousCycleValidValueCount);
                    continuousCycleValidValueCount = 0;
                    continue;
                }
            }
            continuousCycleValidValueCount++; // 正确值的最长连续周期数+1
        }
        return maxLen;
    }

    /**
     * 判断s[index]是否有效
     * @param s 采样数据序列
     * @param index 索引
     * @return true/false
     */
    public static boolean isValid(int[] s, int index) {
        if (index <= 0) {
            return s[0] > 0;
        }
        return s[index] > 0
                && s[index] >= s[index - 1] && s[index] < s[index - 1] + 10;
    }

    /**
     * 获取index最近的有效值
     * @param s 采样数据序列
     * @param index 索引
     * @return 最近的有效值
     */
    public static int findLastValidValue(int[] s, int index) {
        if (index - 1 < 0) { // 表示没有有效值
            return -1;
        }
        for (int i = index - 1; i >= 0 ; i--) {
            if (isValid(s,i)) {
                return s[i];
            }
        }
        return -1;
    }
}
