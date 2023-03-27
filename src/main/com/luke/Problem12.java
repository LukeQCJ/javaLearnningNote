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
        // 采用双指针: 左右指针 + map保存字符索引
        Map<Character,Integer> characterIndexMap = new HashMap<>();
        for (int left = 0, right = 0; right < s.length(); right++) {
            if (characterIndexMap.containsKey(s.charAt(right))) {
                // 如果出现重复字符，则left指针 = 前一个相同字符的索引 + 1,这样就把重复字符移除了
                // 例如: 字符串abcaefghk, left = 1, 指向字符b
                left = Math.max(left, characterIndexMap.get(s.charAt(right)) + 1);
            }
            characterIndexMap.put(s.charAt(right), right);
            int curLen = right - left + 1;
            maxLen = Math.max(maxLen, curLen);
        }
        return maxLen;
    }
}
