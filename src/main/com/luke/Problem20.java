package com.luke;

import java.util.*;

public class Problem20 {
    private static final String LET = "let";
    private static final String ADD = "add";
    private static final String MUlT = "mult";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(evaluate(s));
        sc.close();
    }

    public static int evaluate(String expressionStr) {
        return cal(expressionStr, new HashMap<>());
    }

    private static int cal(String expressionStr, Map<String, Integer> preVariableScope) {
        // 一、解析 命令表达式字符串
        String[] instructions = splitExpression(expressionStr);
        // 二、计算命令表达式
        String command = instructions[0]; // 命令 let add mult
        int commandLength = instructions.length;
        // 二、计算命令表达式: 1、LET命令 let表达式格式: let v1 e1 ..... vn en expr
        if (LET.equals(command)) {
            // 1)、参数解析: 先处理变量对的赋值: i是变量,i+1是变量i的值
            Map<String, Integer> curVariableScope = new HashMap<>(preVariableScope);
            for (int i = 1; i < commandLength - 2; i += 2) {
                String vi = instructions[i]; // vi
                String eiStr = instructions[i + 1]; // ei
                int eiValue;
                // 遇到括号证明 ei仍然是一个表达式
                if (eiStr.charAt(0) == '(') {
                    // 递归 计算表达式
                    eiValue = cal(eiStr, curVariableScope);
                } else {
                    //获取eiStr的值，是数字还是变量，变量的话需要从map中获取
                    if (curVariableScope.containsKey(eiStr)) {
                        eiValue = curVariableScope.get(eiStr);
                    } else {
                        eiValue = Integer.parseInt(eiStr);
                    }
                }
                curVariableScope.put(vi, eiValue);
            }
            // 2)、计算最终值: 处理let的最后一个表达式expr
            String endExpression = instructions[commandLength - 1];
            if (endExpression.charAt(0) == '(') { // 表达式
                // 递归 计算表达式
                return cal(endExpression, curVariableScope);
            } else if (curVariableScope.containsKey(endExpression)) { // 变量
                return curVariableScope.get(endExpression);
            } else { // 数字
                return Integer.parseInt(endExpression);
            }
        // 二、计算命令表达式: 2、ADD或MULT命令 ADD命令 add表达式: add e1 e2; MULT命令: mult表达式: mult e1 e2
        } else {
            int[] variables = new int[2];
            // 1)、参数解析: 计算 e1 和 e2
            for (int i = 1; i < commandLength; i++) {
                String ei = instructions[i];
                if (ei.charAt(0) == '(') { // 判断ei是否为表达式
                    // 递归 计算表达式
                    variables[i - 1] = cal(ei, preVariableScope);
                } else if (preVariableScope.containsKey(ei)) { // 变量赋值从上一个变量作用域获取
                    variables[i - 1] = preVariableScope.get(ei);
                } else { // 数字直接赋值
                    variables[i - 1] = Integer.parseInt(ei);
                }
            }
            // 2)、计算最终值
            if (ADD.equals(command)) { // 加法
                return variables[0] + variables[1];
            } else if (MUlT.equals(command)) { // 乘法
                return variables[0] * variables[1];
            } else {
                return -1;
            }
        }
    }

    /**
     * 解析表达式字符串
     * @param expression 表达式字符串
     * @return 表达式数组
     */
    private static String[] splitExpression(String expression) {
        List<String> expressions = new ArrayList<>();
        // 1、去掉两个括号
        expression = expression.substring(1, expression.length() - 1);
        int left = 0, right = 0;
        while (right < expression.length()) {
            char ch = expression.charAt(right);
            // 2、空格 则分割表达式的命令格式
            if (ch == ' ') {
                expressions.add(expression.substring(left, right));
                left = right + 1;
            // 3、括号 则表示表达式,需要在下一阶段计算时候解析 即递归处理
            } else if (ch == '(') {
                int count = 0;
                while (right < expression.length()) {
                    // 进行括号匹配
                    ch = expression.charAt(right);
                    if (ch == '(') { // 左括号表示表达式开始
                        count++;
                    } else if (ch == ')') { // 右括号表示表达式结束
                        count--;
                    }
                    right++;
                    if (count == 0) { // 当表达式都匹配完毕，就跳出循环
                        break;
                    }
                }
                expressions.add(expression.substring(left, right)); // 将表达式放入返回list中
                left = right + 1;
            }
            right++;
        }
        // 4、如果表达式expression不是以括号结尾
        if (left < expression.length()) {
            expressions.add(expression.substring(left));
        }
        // list转数组
        return expressions.toArray(new String[0]);
    }
}
