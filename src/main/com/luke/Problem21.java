package src.main.com.luke;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Problem21 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(parseInputString(s));
        sc.close();
    }

    /**
     * 解析输入按键字符串，返回内容
     * @param input 输入的字符串
     * @return 内容
     */
    public static String parseInputString(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        // 解析输入字符串
        StringBuilder result = new StringBuilder();
        boolean isNumberMode = true;
        String previousChar = null;
        int sameCharCount = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '#') {
                isNumberMode = !isNumberMode;
                continue;
            }
            // 1、数字模式: 直接拼接字符后返回
            if (isNumberMode) {
                result.append(c);
                continue;
            }
            if (previousChar == null) {
                previousChar = c + "";
            }
            // 2、英文模式
            boolean isSameWithPreviousChar = previousChar.equals(c + "");
            if (c == '/') { // 终止符"/"之后要处理之前的输入内容
                char targetChar = getTargetChar(previousChar,sameCharCount);
                result.append(targetChar);
                sameCharCount = 0; // 非待字符需要清0
                previousChar = null; // 非待字符需要清空
            } else if (isSameWithPreviousChar && c != '0') {
                sameCharCount++; // 相同字符计数+1
            } else {
                char targetChar = getTargetChar(previousChar,sameCharCount);
                result.append(targetChar);
                sameCharCount = 1; // 待字符需要置为1
                previousChar = c + ""; // 待字符需要置为当前字符c，待后续处理
            }
            if (i == input.length() - 1) {
                // 在英文模式下,需要处理未解析previousChar
                char targetChar = getTargetChar(previousChar,sameCharCount);
                result.append(targetChar);
            }
        }
        return result.toString();
    }

    public static char getTargetChar(String c, int sameCharCount) {
        // 获取九宫格
        Map<String, String> jiuGongGe = generateJiuGongGe();
        String keyContent = jiuGongGe.get(c);
        int charIndex = (sameCharCount - 1) % keyContent.length();
        return keyContent.charAt(charIndex);
    }

    /**
     * 生成九宫格map
     * @return 九宫格
     */
    public static Map<String, String> generateJiuGongGe() {
        Map<String,String> jiuGongGe = new HashMap<>();
        jiuGongGe.put("1",",.");
        jiuGongGe.put("2","abc");
        jiuGongGe.put("3","def");
        jiuGongGe.put("4","ghi");
        jiuGongGe.put("5","jkl");
        jiuGongGe.put("6","mno");
        jiuGongGe.put("7","pqrs");
        jiuGongGe.put("8","tuv");
        jiuGongGe.put("9","wxyz");
        jiuGongGe.put("#","#");
        jiuGongGe.put("0"," ");
        jiuGongGe.put("/","/");
        return jiuGongGe;
    }
}
