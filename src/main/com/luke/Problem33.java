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
        Map<Character,Integer> charMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (charMap.containsKey(s.charAt(i))) {
                Integer count = charMap.get(s.charAt(i));
                charMap.put(s.charAt(i), count + 1);
            } else {
                charMap.put(s.charAt(i), 1);
            }
        }
        Queue<Map.Entry<Character, Integer>> queue = new PriorityQueue<>(
                (o1, o2) -> {
                    if (o2.getValue() - o1.getValue() != 0) {
                        return o2.getValue() - o1.getValue();
                    }
                    Character c1 = o1.getKey();
                    Character c2 = o2.getKey();
                    boolean isUpperC1 = c1 >= 'A' && c1 <= 'Z';
                    boolean isUpperC2 = c2 >= 'A' && c2 <= 'Z';
                    if ((isUpperC1 && isUpperC2) || (!isUpperC1 && !isUpperC2)) {
                        return c1 - c2;
                    }
                    if (isUpperC1) {
                        return 1;
                    }
                    return -1;
                }
        );
        queue.addAll(charMap.entrySet());
        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            Map.Entry<Character,Integer> entry = queue.poll();
            result.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        return result.toString();
    }
}
