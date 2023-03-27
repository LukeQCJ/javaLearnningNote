package com.luke;

import java.util.Scanner;

public class Problem4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // [-2,1,-3,4,-1,2,1,-5,4]
        String inputString = sc.nextLine();
        // substring(start,end) 包括start,不包括end
        String tempStr = inputString.substring(1,inputString.length() - 1);
        String[] intStrArray = tempStr.split(",");
        int[] numArray = new int[intStrArray.length];
        for (int i = 0; i < intStrArray.length; i++) {
            numArray[i] = Integer.parseInt(intStrArray[i]);
        }
        int result = maxSubArray(numArray);
        System.out.println(result);
        int result1 = maxSubArray1(numArray);
        System.out.println(result1);
        int result2 = maxSubArray2(numArray);
        System.out.println(result2);
    }

    /**
     *  O(n^2)的时间复杂度,时间复杂度太大,建议优化,,空间复杂度 为O(1)
     * @param nums 整数数组
     * @return int
     */
    public static int maxSubArray(int[] nums) {
        int maxAns = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int curMax = nums[i];
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                curMax = Math.max(curMax, sum);
            }
            maxAns = Math.max(maxAns, curMax);
        }
        return maxAns;
    }

    /**
     *  O(n)的时间复杂度,空间复杂度 为O(n)
     * @param nums 整数数组
     * @return int
     */
    public static int maxSubArray1(int[] nums) {
        int maxAns = nums[0];
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
            maxAns = Math.max(maxAns, dp[i]);
        }
        return maxAns;
    }

    /**
     *  O(n)的时间复杂度 (还可以考虑分治思想优化到log(n)),空间复杂度 为O(1)
     * @param nums 整数数组
     * @return int
     */
    public static int maxSubArray2(int[] nums) {
        int pre = 0, maxAns = nums[0];
        for (int x : nums) {
            pre = Math.max(pre + x, x);
            maxAns = Math.max(maxAns, pre);
        }
        return maxAns;
    }
}
