package com.luke;

import java.util.*;

public class Problem12 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inputString = sc.nextLine();
        System.out.println(lengthOfLongestSubstring(inputString));
        sc.close();
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int maxLen = 0;
        Set<Character> set = new HashSet<>();
        // 采用双指针: 左右指针
        for (int left = 0, right = 0; left <= right && right < s.length(); right++) {
            char ch = s.charAt(right);
            if (set.contains(ch)) { // 当存在重复的字符时
                left = right; // 左指针left就会移动到right同一个位置
                set.clear();  // 重置set中的元素
            }
            set.add(ch);      // 向set中添加元素
            int curLen = right - left + 1;
            maxLen = Math.max(maxLen, curLen);
        }
        return maxLen;
    }
}
