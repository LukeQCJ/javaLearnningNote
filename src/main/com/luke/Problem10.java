package src.main.com.luke;

import java.util.Scanner;

public class Problem10 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int num = input.nextInt();
        int count = cal(num);
        System.out.println(count);
    }

    private static int cal(int num) {
        if (num < 3) {
            return 1;
        }
        return cal(num - 1) + cal(num - 3);
    }
}
