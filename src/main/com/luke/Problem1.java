package com.luke;

import java.util.*;

public class Problem1 {
    public static void main1(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        // 所有人员的卡片 字符串
        String[] memberArray = input.split(",");
        int[] counts = new int[]{0, 0, 0, 0, 0};
        // 累加各种的类型的卡片和在counts数组
        for (String cardStr : memberArray) {
            char[] cardArray = cardStr.toCharArray();
            for (int j = 0; j < cardArray.length; j++) {
                if (cardArray[j] == '1') {
                    counts[j]++;
                }
            }
        }
        Arrays.sort(counts);
        // 最少数量的类型的卡片数，就是该团队最多能凑齐的福卡套数
        System.out.println(counts[0]);
        sc.close();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String t = sc.nextLine();
        System.out.println(minWindow(s,t));
        sc.close();
    }

    public static String minWindow(String s, String t) {
        if (s == null || s.length() == 0 || t == null || t.length() == 0) {
            return "";
        }
        int sLen = s.length();
        int tLen = t.length();
        Map<Character, Integer> charMap = new HashMap<>();
        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        String minStr = "";
        while (right < sLen) {
            char rightChar = s.charAt(right);
            if (t.contains(rightChar + "")) {
                charMap.put(rightChar, charMap.getOrDefault(rightChar, 0) + 1);
            }
            right++;
            boolean moveLeft = false;
            while (charMap.size() >= tLen) {
                char leftChar = s.charAt(left);
                if (charMap.containsKey(leftChar)) {
                    charMap.put(leftChar, charMap.get(leftChar) - 1);
                    if (charMap.get(leftChar) == 0) {
                        moveLeft = true;
                        minLen = Math.min(minLen, right - left + 1);
                        minStr = s.substring(left, right);
                        charMap.remove(leftChar);
                    }
                }
                left++;
            }
            while (moveLeft && !t.contains(s.charAt(left) + "")) {
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : minStr;
    }
}
