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
        Stack<Long> newStack = compressStack(stack);
        // 翻转栈
        Stack<Long> tempStack = new Stack<>();
        while (!newStack.isEmpty()) {
            tempStack.add(newStack.pop());
        }
        System.out.println(tempStack);
        sc.close();
    }

    public static Stack<Long> compressStack(Stack<Long> stack) {
        // 辅助队列，用于保存在累加过程中的过程元素
        Deque<Long> deque = new ArrayDeque<>();
        // 结果栈
        Stack<Long> resultStack = new Stack<>();
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
            if (sum == N1) { // 1、 sum == N1 刚好相等，则放入stack中，sum 变成新的N1
                sum += N1;
                stack.add(sum);
                N1 = 0; // 由于sum放入了处理栈stack中成为一个元素，则N1回归当未处理的状态，即N1=0
                sum = 0; // 同上
                deque.clear();
            } else if (sum > N1) { // 2、 sum > N1 当一组元素的和sum > N1时，此时N1就不需要后续参与计算，直接放入结果栈resultStack中
                resultStack.add(N1);
                // 找出新的N1
                if (!deque.isEmpty()) {
                    N1 = deque.removeFirst();
                } else {
                    N1 = 0;
                }
                sum = 0;
                deque.addLast(ele);
                while (!deque.isEmpty()) {
                    Long e = deque.removeLast();
                    stack.add(e);
                }
            } else {
                deque.addLast(ele);
            }
        }
        resultStack.add(N1);
        if (deque.isEmpty()) { // 如果元素刚好计算完，没有剩余的元素，就直接返回midStack
            return resultStack;
        }
        while (!deque.isEmpty()) { // 3、当deque中的元素之和小于N1时，需要再次放入stack栈中
            Long ele = deque.removeLast();
            stack.add(ele);
        }
        Stack<Long> innerNewStack = compressStack(stack); // 将剩余的元素计算完
        // 递归后的结果放入resultStack中
        resultStack.addAll(innerNewStack);
        return resultStack;
    }
}
