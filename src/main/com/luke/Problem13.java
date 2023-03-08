package com.luke;

import java.util.Scanner;
import java.util.Stack;

public class Problem13 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inputString = sc.nextLine();
        System.out.println(getLongestVowelStringLength(inputString));
        sc.close();
    }

    public static int getLongestVowelStringLength(String s) {
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
