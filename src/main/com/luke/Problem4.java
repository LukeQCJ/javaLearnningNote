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
    }

    /**
     *  O(n)的时间复杂度 (还可以考虑分治思想优化到log(n))
     * @param numArray 整数数组
     * @return int
     */
    public static int maxSubArray(int[] numArray) {
        int pre = 0, maxAns = numArray[0];
        for (int x : numArray) {
            pre = Math.max(pre + x, x);
            maxAns = Math.max(maxAns, pre);
        }
        return maxAns;
    }
}
