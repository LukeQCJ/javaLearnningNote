package com.luke;

import java.util.Scanner;

public class Problem27 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        splitString(s);
        sc.close();
    }

    public static void splitString(String s) {
        if (s == null || s.length() == 0) {
            return;
        }
        StringBuilder temp = new StringBuilder(s);
        while (temp.length() > 8) {
            System.out.println(temp.substring(0,8));
            temp = new StringBuilder(temp.substring(8));
        }
        while (temp.length() < 8 && temp.length() > 0) {
            temp.append("0");
        }
        System.out.println(temp);
    }
}
