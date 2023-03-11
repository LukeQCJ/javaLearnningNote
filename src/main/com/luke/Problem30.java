package com.luke;

import java.util.Scanner;

public class Problem30 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int flaw = Integer.parseInt(sc.nextLine());
        String s = sc.nextLine();
        System.out.println(lengthOfFlawLongestVowelSubstring(s, flaw));
        sc.close();
    }

    public static int lengthOfFlawLongestVowelSubstring(String s, int flaw) {
        String vowelString = "aeiouAEIOU";
        if (s == null || s.length() == 0) {
            return 0;
        }
        int strLen = s.length();
        int maxLen = 0;
        System.out.println("===========================================================");
        for (int left = 0, right = 0, curFlaw = 0; right < strLen;) {
            System.out.println("s = " + s);
            boolean isVowelRight = vowelString.contains(s.charAt(right) + "");
            if (!isVowelRight) {
                System.out.println("s[right] = " + s.charAt(right));
                curFlaw++; // 瑕疵度+1
                System.out.println("当前瑕疵度+1");
            }
            System.out.println("当前瑕疵度 为 " + curFlaw + " 目标瑕疵度为 " + flaw);
            while (curFlaw > flaw) { // 如果瑕疵度超了，就需要左移来降低瑕疵度
                System.out.println("当前瑕疵度 为 " + curFlaw + "大于 目标瑕疵度 " + flaw);
                boolean isVowelLeft = vowelString.contains(s.charAt(left) + "");
                System.out.println("当前left = " + left + ", 当前right = " + right);
                if (left >= right) { // 防止left越界
                    break;
                }
                left++; // 左下标右移1位
                System.out.println("left右移1位");
                if (!isVowelLeft) {
                    curFlaw--; // 瑕疵度-1
                    System.out.println("当前瑕疵度-1");
                    System.out.println("当前瑕疵度为: " + curFlaw);
                }
            }
            System.out.println("当前瑕疵度 为 " + curFlaw + " 目标瑕疵度为 " + flaw);
            boolean isVowelLeft = vowelString.contains(s.charAt(left) + "");
            // 满足 瑕疵度 且 是元音子串，就计算最大长度
            if (curFlaw == flaw && isVowelLeft && isVowelRight) {
                System.out.println("有满足条件的left~right的子串");
                if (left == right){
                    System.out.println("当前符合条件的瑕疵子串为: " + s.charAt(left));
                } else if (left < right){
                    System.out.println("当前符合条件的瑕疵子串为: " + s.substring(left,right + 1));
                }
                maxLen = Math.max(maxLen, right - left + 1);
                System.out.println("计算出来的最大长度为: " + maxLen);
            } else {
                System.out.println("当前left~right中没有满足条件的子串");
            }
            if (0 == right) {
                System.out.println("当前处理的S=" + s.charAt(0));
            } else {
                System.out.println("当前处理的S=" + s.substring(0,right + 1));
            }
            System.out.println("当前right = " + right);
            right++; // 右下标右移1位
            System.out.println("右移1位, right = " + right);
            System.out.println("=================================================================");
        }
        return maxLen;
    }
}
