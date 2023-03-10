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
        for (int left = 0, right = 0, curFlaw = 0; right < strLen;) {
            boolean isVowelRight = vowelString.contains(s.charAt(right) + "");
            if (!isVowelRight) {
                curFlaw++; // 瑕疵度+1
            }
            while (curFlaw > flaw) { // 如果瑕疵度超了，就需要左移来降低瑕疵度
                boolean isVowelLeft = vowelString.contains(s.charAt(left) + "");
                if (!isVowelLeft) {
                    curFlaw--; // 瑕疵度-1
                }
                if (left >= right) { // 防止left越界
                    break;
                }
                left++; // 左下标右移1位
            }
            boolean isVowelLeft = vowelString.contains(s.charAt(left) + "");
            // 满足 瑕疵度 且 是元音子串，就计算最大长度
            if (curFlaw == flaw && isVowelLeft && isVowelRight) {
                maxLen = Math.max(maxLen, right - left + 1);
            }
            right++; // 右下标右移1位
        }
        return maxLen;
    }
}
