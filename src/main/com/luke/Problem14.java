package com.luke;

import java.util.Arrays;
import java.util.Scanner;

public class Problem14 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inputString = sc.nextLine();
        String input = inputString.substring(1, inputString.length() - 1);
        String[] numsStrArray = input.split(",");
        int[] nums = new int[numsStrArray.length];
        for (int i = 0; i < numsStrArray.length; i++) {
            nums[i] = Integer.parseInt(numsStrArray[i]);
        }
        String result = largestNumber(nums);
        System.out.println(result);
        sc.close();
    }

    public static String largestNumber(int[] nums) {
        int length = nums.length;
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            array[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(array, (a, b) -> b.concat(a).compareTo(a.concat(b)));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ("".equals(sb.toString()) && "0".equals(array[i])) {
                continue;
            }
            sb.append(array[i]);
        }
        return "".equals(sb.toString()) ? "0" : sb.toString();
    }
}
