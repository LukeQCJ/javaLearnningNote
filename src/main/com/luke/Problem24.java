package com.luke;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.Stack;

public class Problem24 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        // 解析输入栈
        String[] intStrArray = s.split(" ");
        Stack<Long> stack = new Stack<>();
        for (String value : intStrArray) {
            stack.add(Long.parseLong(value));
        }
        // 按照公式压缩栈
        Stack<Long> newStack = getStackRemainElement(stack);
        // 翻转栈
        Stack<Long> tempStack = new Stack<>();
        while (!newStack.isEmpty()) {
            tempStack.add(newStack.pop());
        }
        System.out.println(tempStack);
        sc.close();
    }

    public static Stack<Long> getStackRemainElement(Stack<Long> stack) {
        // 辅助队列，用于保存在累加过程中的过程元素
        Deque<Long> deque = new ArrayDeque<>();
        // 压缩后的栈
        Stack<Long> midStack = new Stack<>();
        // 公式中代表N1
        long N1 = 0;
        long sum = 0L;
        while (!stack.isEmpty()) {
            Long ele = stack.pop();
            if (N1 == 0) {
                N1 = ele;
                continue;
            }
            sum += ele;
            if (sum == N1) {
                sum += N1;
                stack.add(sum);
                N1 = 0;
                sum = 0;
                deque.clear();
            } else if (sum > N1) {
                midStack.add(N1);
                if (!deque.isEmpty()) {
                    N1 = deque.removeFirst();
                } else {
                    N1 = 0;
                }
                sum = 0;
                deque.addLast(ele);
                while (!deque.isEmpty()) {
                    stack.add(deque.removeLast());
                }
            } else {
                deque.addLast(ele);
            }
        }
        midStack.add(N1);
        // 当deque中的元素之和小于N1时，需要再次放入栈中迭代
        Stack<Long> tempStack = new Stack<>();
        while (!deque.isEmpty()) {
            Long ele = deque.removeLast();
            tempStack.add(ele);
        }
        if (tempStack.isEmpty()) {
            return midStack;
        }
        Stack<Long> innerNewStack = getStackRemainElement(tempStack);
        // 迭代后的结果放入返回栈中
        midStack.addAll(innerNewStack);
        return midStack;
    }
}
