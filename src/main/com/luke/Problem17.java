package com.luke;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Problem17 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            // 数据输入
            int n = scan.nextInt();
            // 小朋友意向map
            Map<String, String> intentionMap = new HashMap<>();
            // 人员数组
            String[] childrenArray = new String[n];//只保存第一个同学
            for (int i = 0; i < n; i++) {
                String s = scan.nextLine();
                String[] relation = s.split(" ");
                intentionMap.put(relation[0], relation[1]);
                childrenArray[i] = relation[0]; // 只保存第一个同学
            }
            System.out.println(computeGroup(n, intentionMap, childrenArray));
        }
    }

    private static int computeGroup(int n, Map<String, String> intentionMap, String[] childrenArray) {
        // 小朋友集合,用于判断是否已经处理过
        HashSet<String> childrenSet = new HashSet<>();
        int groupCount = 0;
        for (int i = 0; i < n; i++) { // 遍历每个小朋友，查看每个小朋友的意向
            if (!childrenSet.contains(childrenArray[i])){
                String child = childrenArray[i];
                while (!childrenSet.contains(child)){
                    childrenSet.add(child);
                    child = intentionMap.get(child); // 通过意向小朋友迭代找同组的下一个小朋友
                }
                groupCount++;
            }
        }
        return groupCount;
    }
}
