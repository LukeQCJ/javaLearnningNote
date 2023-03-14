package com.luke;

import java.util.*;
import java.util.stream.Collectors;

public class Problem34 {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("J", 11);
        map.put("Q", 12);
        map.put("K", 13);
        map.put("A", 14);
        // 存放 发的5张牌的 map
        Map<Integer, List<String>> cardMap = new HashMap<>();
        Scanner sc = new Scanner(System.in);
        // 循环5次发牌
        for (int i = 0; i < 5; ++i) {
            // 一张一张发牌
            String[] split = sc.nextLine().split(" ");
            String cardValue = split[0];
            String cardType = split[1];
            if (map.containsKey(cardValue)) { // 处理 J、Q、K、A
                Integer key = map.get(cardValue);
                if (cardMap.containsKey(key)) {
                    cardMap.get(key).add(cardType);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(cardType);
                    cardMap.put(key, list);
                }
                continue;
            }
            // 处理 1、2、3、4、5、6、7、8、9、10
            int intCardValue = Integer.parseInt(cardValue);
            if (cardMap.containsKey(intCardValue)) {
                cardMap.get(intCardValue).add(cardType);
            } else {
                List<String> list = new ArrayList<>();
                list.add(cardType);
                cardMap.put(intCardValue, list);
            }
        }
        sc.close();
        // 判断牌型
        judgeCard(cardMap);
    }

    /**
     * 判断牌型
     * @param cardMap 手上的牌map
     */
    public static void judgeCard(Map<Integer, List<String>> cardMap) {
        // 判断牌型 默认 牌型七：其它组合。
        int res = 7;
        int len = cardMap.size();
        boolean hasA = cardMap.containsKey(14);
        if (len == 2) {
            // 当card值只有2种：
            //  牌型二：如果出现四张一样大小的牌，则称其为四元。
            //  牌型五：如果出现三张相同大小的牌和另外两张相同的牌，则称其为三元二目。
            for (Map.Entry<Integer, List<String>> item : cardMap.entrySet()) {
                int cardTypeSize = item.getValue().size();
                if (cardTypeSize == 1 || cardTypeSize == 4) {
                    res = 2;
                } else {
                    res = 5;
                }
            }
        }
        if (len == 3) {
            // 当card值只有2种：
            //  牌型六：如果出现三张相同大小的牌和另外两张不同的牌，则称其为三元。
            //  牌型七：其它组合。比如2张A、2张K、1张2
            boolean sanYuan = false;
            for (Map.Entry<Integer, List<String>> item : cardMap.entrySet()) {
                int cardTypeSize = item.getValue().size();
                if (cardTypeSize == 3) {
                    sanYuan = true;
                    break;
                }
            }
            // 是否为牌型六，反之则为牌型七
            if (sanYuan) {
                res = 6;
            }
        }
        if (len == 5) {
            boolean shunZi;
            // 判断是不是顺子
            if (hasA) {
                cardMap.put(0, cardMap.get(14)); // 到存在A是用于判断顺子A、1、2、3、4或者10、J、Q、K、A
            }
            List<Integer> cardValues = cardMap.keySet().stream().sorted().collect(Collectors.toList());
            if (hasA) {
                // 当存在A，cardValues大小为6，需要判断前5张和后5张的最后一张牌与第一张牌大4，表示顺序递增1，为顺子
                List<Integer> cards1 = cardValues.subList(0, 5);
                List<Integer> cards2 = cardValues.subList(1, 6);
                int i1 = cards1.get(4) - cards1.get(0);
                int i2 = cards2.get(4) - cards2.get(0);
                shunZi = i1 == 4 || i2 == 4;
            } else { // 否则直接取最后一张牌与最后一张牌比较，相差4，则为顺子
                int i = cardValues.get(4) - cardValues.get(0);
                shunZi = i == 4;
            }
            // 牌型四：如果出现五张连续的牌，则称其为顺子
            if (shunZi) {
                res = 4;
            }
            // 判断是不是同花
            boolean tongHua = false;
            Set<String> set = new HashSet<>();
            cardMap.values().forEach(set::addAll);
            if (set.size() == 1){
                tongHua = true;
            }
            if (tongHua) { // 牌型三：如果五张牌的花色一样，则称其为同花。
                res = 3;
            }
            if (tongHua && shunZi) { // 牌型一：五张牌连续出现，且花色相同，则称其为同花顺。
                res = 1;
            }
        }
        System.out.println(res);
    }
}
