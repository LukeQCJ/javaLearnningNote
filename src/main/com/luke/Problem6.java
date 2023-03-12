package src.main.com.luke;

import java.util.Scanner;

public class Problem6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String key = sc.nextLine();
        String msg = sc.nextLine();
        System.out.println(encodeString(msg,key));
        sc.close();
    }

    /**
     * 根据密钥key加密msg
     * @param msg 待加密的内容
     * @param key 密钥
     * @return 加密后的内容
     */
    public static String encodeString(String msg, String key) {
        String standardTable = "abcdefghijklmnopqrstuvwxyz";
        String newTable = getNewTable(standardTable, key);
        return encodeStringByTable(msg, newTable);
    }

    /**
     * 生成新的字母表
     * @param standardTable 标准字母表
     * @param key 密钥key
     * @return newTable
     */
    public static String getNewTable(String standardTable, String key) {
        StringBuilder newTable = new StringBuilder();
        String table = standardTable;
        for(char c : key.toCharArray()) {
            if (newTable.indexOf(c + "") == -1) {
                newTable.append(c); // 将c添加到newTable前部
            }
            table = table.replace(c + "", "");// 删除table中的c
        }
        newTable.append(table); // 将table剩下的部分追加到newTable后面
        return newTable.toString();
    }

    /**
     * 根据字母表table,加密msg
     * @param msg 要加密的数据
     * @param table 字母表
     * @return encodedMsg 加密后的内容
     */
    public static String encodeStringByTable(String msg, String table) {
        StringBuilder encodedMsg = new StringBuilder();
        for (char c : msg.toCharArray()) {
            int index = c - 'a';
            char newChar = table.toCharArray()[index];
            encodedMsg.append(newChar);
        }
        return encodedMsg.toString();
    }
}
