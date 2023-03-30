package com.luke;

import java.util.Arrays;
import java.util.Scanner;

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
        String input = sc.nextLine();
        String[] arr = input.split(" ");
        int[] nums = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            nums[i] = Integer.parseInt(arr[i]);
        }
        int target = sc.nextInt();
        System.out.println(searchInsert(nums,target));
    }

    public static int searchInsert(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return left + 1;
    }

    public static void test () {

    }
}
