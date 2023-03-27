package com.luke;

import java.util.Scanner;
import java.util.Stack;

public class Problem13 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inputString = sc.nextLine();
        System.out.println(getLongestVowelStringLength(inputString));
        System.out.println(getLongestVowelStringLength2(inputString));
        sc.close();
    }

    /**
     * 中间变量curLen统计元音子串的长度
     * @param s 字符串
     * @return int
     */
    public static int getLongestVowelStringLength(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        String vowelString = "aeiouAEIOU";
        int maxLen = 0;
        int curLen = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (vowelString.contains(c+"")) {
                curLen++;
            } else {
                maxLen = Math.max(maxLen, curLen);
                curLen = 0;
            }
            if (i == s.length() - 1) {
                maxLen = Math.max(maxLen, curLen);
            }
        }
        return maxLen;
    }

    /**
     * 中间变量stack保存元音子串
     * @param s 字符串
     * @return int
     */
    public static int getLongestVowelStringLength2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        String vowelString = "aeiouAEIOU";
        Stack<Character> stack = new Stack<>();
        int maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (vowelString.contains(c+"")) {
                stack.push(c);
            } else {
                maxLen = Math.max(maxLen,stack.size());
                stack.clear();
            }
        }
        return Math.max(maxLen,stack.size());
    }
}
