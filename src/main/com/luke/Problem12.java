package com.luke;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
        Map<Character,Integer> map = new HashMap<>();
        for (int left = 0, right = 0; right < s.length(); right++) {
            if (map.containsKey(s.charAt(right))) {
                left = Math.max(left, map.get(s.charAt(right)) + 1);
            }
            map.put(s.charAt(right), right);
            int curLen = right - left + 1;
            maxLen = Math.max(maxLen, curLen);
        }
        return maxLen;
    }
}
