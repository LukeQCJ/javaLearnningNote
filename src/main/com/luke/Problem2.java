package com.luke;

import java.util.Scanner;

public class Problem2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 文章内容
        String content = sc.nextLine();
        // 开始索引
        int startIndex = sc.nextInt();
        // 结束索引
        int endIndex = sc.nextInt();
        String[] words = content.split(" ");
        StringBuilder newContent = new StringBuilder("");
        // 处理开始索引前的字符串，按照原来的顺序输出
        for (int i = 0; i < startIndex; i++) {
            newContent.append(words[i]).append(" ");
        }
        // 处理开始索引与结束索引之间的字符串，按照逆序输出
        for (int i = endIndex; i >= startIndex; i--) {
            newContent.append(words[i]).append(" ");
        }
        // 处理结束索引后的字符串，按照原来的顺序输出
        for (int i = endIndex + 1; i < words.length; i++) {
            if (i == words.length - 1) {
                newContent.append(words[i]);
            } else {
                newContent.append(words[i]).append(" ");
            }
        }
        System.out.println(newContent.toString());
        sc.close();
    }
}
