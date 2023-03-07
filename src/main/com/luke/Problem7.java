package com.luke;

import java.util.*;

public class Problem7 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 输入
        String inputString = in.nextLine();
        String[] numStrArray = inputString.split(",");
        // 解析为整数数组
        int[] numArray = new int[numStrArray.length];
        for (int i = 0; i < numArray.length; i++) {
            numArray[i] = Integer.parseInt(numStrArray[i]);
        }
        // 构造优先队列
        PriorityQueue<PrintTask> priorityQueue =
            new PriorityQueue<>(
                (a, b)
                    ->  // 如果两个任务的优先级相同，就按照任务入队的原始顺序
                (Objects.equals(a.priority, b.priority) ? a.order - b.order
                        // 如果两个任务的优先级不同，则按照优先级排序
                    : b.priority - a.priority)
            );
        for (int i = 0; i < numArray.length; i++) {
            priorityQueue.offer(new PrintTask(numArray[i], i)); // 构造虚拟打印任务实例在优先级队列
        }
        int count = 1;
        while (!priorityQueue.isEmpty()) {
            PrintTask task = priorityQueue.poll();
            System.out.print(task.order);
            if (count != numArray.length) {
                System.out.print(",");
                count++;
            }
        }
        System.out.println();
    }

    /**
     * 打印任务实体类
     */
    static class PrintTask {
        Integer priority;
        Integer order;

        public PrintTask(Integer priority, Integer order) {
            this.priority = priority;
            this.order = order;
        }
    }
}
