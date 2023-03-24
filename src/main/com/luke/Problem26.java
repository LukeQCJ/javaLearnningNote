package com.luke;

import java.util.Scanner;
import java.util.Stack;

public class Problem26 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(decodeString(s));
        sc.close();
    }

    public static String decodeString(String s) {
        // 1、迭代的退出条件
        if (s == null || s.length() == 0) {
            return "";
        }
        int len = s.length();
        // 2、解码流程开始
        Stack<Integer> numStack = new Stack<>(); // 保存字符串的重复次数
        Stack<String> strStack = new Stack<>();  // 保存同一层级的字符串
        StringBuilder tempStr = new StringBuilder(); // 临时变量,用于记录过程字符串
        StringBuilder numStr = new StringBuilder();  // 用于记录过程字符串 重复的次数
        int squareBracketLevel = 0; // 中括号的层数
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            // 1) 处理数字字符
            if (Character.isDigit(c)) {
                if (squareBracketLevel > 0) { // 如果数字包含在中括号里面，则拼接进tempStr待内层解析
                    tempStr.append(c);
                    continue;
                }
                // 处理以字母序列开头的子串,如adc2[ab]xx
                if ("".equals(numStr.toString()) && !"".equals(tempStr.toString())) {
                    numStr.append("1");
                    numStack.add(Integer.parseInt(numStr.toString()));
                    strStack.add(tempStr.toString());
                    tempStr = new StringBuilder();
                    numStr = new StringBuilder();
                }
                numStr.append(c);
            // 2) 处理左方括号[字符
            } else if (c == '[') {
                if (squareBracketLevel > 0) { // 不是第一层括号,则直接追加到字符串中，待下一层处理
                    tempStr.append(c);
                }
                squareBracketLevel++; // 括号层数+1
            // 3) 处理右方括号]字符
            } else if (c == ']') {
                squareBracketLevel--;
                if (squareBracketLevel <= 0) { // 如果是第一层括号，则将数字、字符串放入 栈 中，以待后续处理
                    if ("".equals(numStr.toString())) {
                        numStr.append("1");
                    }
                    numStack.add(Integer.parseInt(numStr.toString()));
                    strStack.add(tempStr.toString());
                    tempStr = new StringBuilder();
                    numStr = new StringBuilder();
                } else { // 不是第一层括号,则直接追加到字符串中，待下一层去递归处理
                    tempStr.append(c);
                }
            // 4) 处理字母字符
            } else if (Character.isLetter(c)) {
                tempStr.append(c);
                // 处理以字母序列结尾的子串,如abc2[a]xxx
                if (i == len - 1) {
                    if ("".equals(numStr.toString())) {
                        numStr.append("1");
                    }
                    numStack.add(Integer.parseInt(numStr.toString()));
                    strStack.add(tempStr.toString());
                }
            }
        }
        // 3、开始借助strStack和numStack拼接字符串
        int size = numStack.size();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String s1 = strStack.get(i);
            String temp = s1;
            // 如果是待解析的字符串就解析
            if (s1.contains("[")) {
                temp = decodeString(s1); // 递归解析
            }
            Integer num = numStack.get(i);
            for (int j = 0; j < num; j++) {
                result.append(temp);
            }
        }
        return result.toString();
    }
}
