package src.main.com.luke;

import java.util.Scanner;
import java.util.Stack;

public class Problem41 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(getMinStep(n));
    }

    public static int getMinStep(int n) {
        Stack<Integer> stack = new Stack<>();
        // 注意，是已经按照由大到小排序的
        int[] stepTypes = {3, 2};
        backTrack(stepTypes, n, stack);
        return stack.size();
    }

    public static boolean backTrack(int[] stepTypes, int total, Stack<Integer> stack) {
        if (total == 0) {
            return true;
        }
        if (total < 0) {
            return false;
        }
        // 回溯
        for (int stepType : stepTypes) {
            total -= stepType;
            stack.add(stepType);
            if (backTrack(stepTypes, total, stack)) {
                return true;
            }
            stack.pop();
            total += stepType;
        }
        return false;
    }
}
