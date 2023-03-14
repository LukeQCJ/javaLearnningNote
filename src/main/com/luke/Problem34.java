package com.luke;

import java.util.*;

public class Problem34 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = Integer.parseInt(sc.nextLine());
        // 利用TreeSet来保存目录ID和自动排序
        TreeSet<Integer> dirSet = new TreeSet<>();
        int[][] relations = new int[m][2];
        for (int i = 0; i < m; i++) {
            String input = sc.nextLine();
            relations[i][0] = Integer.parseInt(input.split(" ")[0]); // child ID
            relations[i][1] = Integer.parseInt(input.split(" ")[1]); // parent ID
            dirSet.add(relations[i][0]);
            dirSet.add(relations[i][1]);
        }
        int deleteId = sc.nextInt();
        // 利用队列 实现广度优先遍历
        dirSet.remove(0);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(deleteId);
        while (!queue.isEmpty()) {
            Integer id = queue.poll();
            for (int[] relation : relations) {
                // 如果父节点为id值，就在dirSet中删除父目录id和子目录id，并入队子目录id以待下层处理
                if (relation[1] == id) {
                    dirSet.remove(id);
                    dirSet.remove(relation[0]);
                    queue.add(relation[0]);
                }
            }
        }
        for (Integer id : dirSet) {
            System.out.print(id + " ");
        }
        System.out.println();
        sc.close();
    }
}
