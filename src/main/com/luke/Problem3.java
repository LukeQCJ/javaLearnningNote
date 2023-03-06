package com.luke;

import java.util.*;
import java.util.Scanner;

public class Problem3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String inputString = in.nextLine();
        intervalIntersection1(inputString);
        in.close();
    }

    /**
     *
     * @param inputString 输入字符串 1 3 4 5 3 6
     */
    public static void intervalIntersection2(String inputString) {
        String[] intStrArray = inputString.split(" ");
        // 区间list
        List<IntegerInterval> rawIntervalList = new ArrayList<>();
        for (int i = 0; i < intStrArray.length; i+=2) {
            rawIntervalList.add(
                    new IntegerInterval(Integer.parseInt(intStrArray[i]),
                            Integer.parseInt(intStrArray[i + 1])));
        }
        // 区间交集
        List<IntegerInterval> midIntervalIntersectionList = new ArrayList<>();
        for (int i = 0; i < rawIntervalList.size(); i++) {
            for (int j = i + 1; j < rawIntervalList.size(); j++) {
                int left = Math.max(rawIntervalList.get(i).left, rawIntervalList.get(j).left);
                int right = Math.min(rawIntervalList.get(i).right, rawIntervalList.get(j).right);
                if (left <= right) {
                    midIntervalIntersectionList.add(new IntegerInterval(left, right));
                }
            }
        }
        // 排序 由小到大
        midIntervalIntersectionList.sort(
                (o1, o2) -> o1.left.equals(o2.left) ? o1.right - o2.right : o1.left - o2.left);
        // 合并区间
        List<IntegerInterval> mergeIntervalList = new ArrayList<>();
        int index = 0;
        for (IntegerInterval interval : midIntervalIntersectionList) {
            // 如果前一个区间的右边界小于当前区间的左边界,则将当前区间直接添加到mergeIntervalList中
            if (index == 0 || interval.left > mergeIntervalList.get(index - 1).right) {
                index++;
                mergeIntervalList.add(interval);
            } else {
                mergeIntervalList.get(index - 1).right =
                        Math.max(mergeIntervalList.get(index - 1).right, interval.right);
            }
        }
        // 输出结果
        for (int i = 0; i < mergeIntervalList.size(); i++) {
            System.out.print(mergeIntervalList.get(i).left);
            System.out.print(" ");
            System.out.print(mergeIntervalList.get(i).right);
            if (i != mergeIntervalList.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     *
     * @param inputString 输入字符串 1 3 4 5 3 6
     */
    public static void intervalIntersection1(String inputString) {
        String[] str = inputString.split(" ");
        int[] arr = new int[str.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(str[i]);
        }
        // 先计算交集
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < arr.length; i += 2) {
            for (int j = i + 2; j < arr.length; j += 2) {
                int left = Math.max(arr[i], arr[j]);
                int right = Math.min(arr[i + 1], arr[j + 1]);
                if (left <= right) {
                    res.add(new int[]{left, right});
                }
            }
        }
        // 计算完交集，按从小到大排序，左边界升序，相同，有边界升序
        int[][] ans = res.toArray(new int[res.size()][]);
        Arrays.sort(ans, (a, b) -> (a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]));
        // 求交集的并集
        int[][] result = new int[ans.length][2];
        int index = -1;
        for (int[] an : ans) {
            if (index == -1 || an[0] > result[index][1]) {
                result[++index] = an;
            } else {
                result[index][1] = Math.max(result[index][1], an[1]);
            }
        }
        int[][] last = Arrays.copyOf(result, index + 1);
        for (int i = 0; i < last.length; i++) {
            System.out.print(last[i][0]);
            System.out.print(" ");
            System.out.print(last[i][1]);
            if (i != last.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * 整数区间 实体类
     */
    static class IntegerInterval {
        Integer left;
        Integer right;

        public IntegerInterval(Integer left, Integer right) {
            this.left = left;
            this.right = right;
        }
    }
}
