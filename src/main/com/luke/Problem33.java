package src.main.com.luke;

import java.util.*;

public class Problem33 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(statisticsChar(s));
        sc.close();
    }

    public static String statisticsChar(String s) {
        Queue<CharStatisticsInfo> queue = new PriorityQueue<>(
                (o1,o2) -> o1.c == o2.c ? o2.count - o1.count : o1.c - o2.c
        );
        Map<Character,Integer> charMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (charMap.containsKey(s.charAt(i))) {
                Integer count = charMap.get(s.charAt(i));
                charMap.put(s.charAt(i), count + 1);
            } else {
                charMap.put(s.charAt(i), 1);
            }
        }
        for (Map.Entry<Character,Integer> entry : charMap.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            queue.add(new CharStatisticsInfo(key,value));
        }
        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            CharStatisticsInfo charStatisticsInfo = queue.poll();
            result.append(charStatisticsInfo.c).append(":").append(charStatisticsInfo.count).append(";");
        }
        return result.toString();
    }

    static class CharStatisticsInfo {
        char c;
        int count;

        CharStatisticsInfo(char c, int count) {
            this.c = c;
            this.count = count;
        }
    }
}
