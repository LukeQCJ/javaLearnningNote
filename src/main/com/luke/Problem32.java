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
        int curNotBrokenContinuousValidValueCount = 0; // 未故障的连续正确值的计数
        int curErrorValueCount = 0; // 当前m个周期中错误值的个数
        int curBrokenContinuousValidValueCount = 0; // 故障后连续的正确值的计数
        int curContinuousCycleCount = 0; // 未故障的当前连续周期计数
        for (int i = 0; i < s.length; i++) {
            // 工具正常情况下 未故障的当前连续周期计数
            if (!isBroken) {
                curContinuousCycleCount++; // 未故障的当前连续周期计数+1
            }

            // 每个周期值的逻辑判断处理
            boolean isValid = isValid(s,i);
            if (!isValid) { // 如果是错误值，则需要进行错误值逻辑处理
                int lastValidValue = findLastValidValue(s,i);
                if (lastValidValue == -1) {
                    curErrorValueCount++;
                } else { // 如果找到最近一个正确值，则当前值=最近一个正确值，即当前值为正确的
                    s[i] = lastValidValue;
                    isValid = true;
                }
            }

            // 故障后，开始统计故障后的正确值的个数
            if (isBroken && isValid) { // 此处重新判断新的值的有效性
                curBrokenContinuousValidValueCount++; // p个周期内 采样值 一直要有效
            } else if (isBroken) {
                curBrokenContinuousValidValueCount = 0; // 否则 重新计数
            }

            // 1、故障恢复判断: 判断 采集工具 故障恢复：连续p次正确数据
            if (isBroken && curBrokenContinuousValidValueCount >= p) {
                isBroken = false;
                // 故障恢复后，丢弃故障恢复之前的数据
                curContinuousCycleCount = 0;
                curErrorValueCount = 0;
                curBrokenContinuousValidValueCount = 0;
                continue;
            }

            // 2、故障判断: 判断 采集工具 故障：m个周期内t次错误数据
            if (!isBroken && curErrorValueCount >= t && curContinuousCycleCount <= m) {
                isBroken = true;
                curContinuousCycleCount = 0;
                curNotBrokenContinuousValidValueCount = 0;
                continue;
            }

            if (isValid && !isBroken) { // 未故障 连续 正确值 的个数 统计
                curNotBrokenContinuousValidValueCount++; // 正确值的最长连续周期数+1
                // 连续有效值个数最大值 更新
                maxLen = Math.max(maxLen, curNotBrokenContinuousValidValueCount);
            } else {
                curNotBrokenContinuousValidValueCount = 0;
            }
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
            return false;
        }
        return s[index] > 0
                && s[index - 1] <= s[index] && s[index] < s[index - 1] + 10;
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
