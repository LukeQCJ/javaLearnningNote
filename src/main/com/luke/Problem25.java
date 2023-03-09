package com.luke;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Problem25 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String[] sizeList = sc.nextLine().split(",");
        String[] requestList = sc.nextLine().split(",");
        //用treeMap将大小和数量对应起来，并按大小排序
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for(String s : sizeList){
            Integer size = Integer.parseInt(s.split(":")[0]);
            Integer num = Integer.parseInt(s.split(":")[1]);
            map.put(size, num);
        }

        StringBuilder sb = new StringBuilder();
        List<Integer> resourceList = new ArrayList<>(map.keySet()); //有参构造！！
        for(String reqStr : requestList){ // 遍历 请求list
            int flag = 0;
            for(int resource : resourceList){ // 遍历资源list
                int req = Integer.parseInt(reqStr);
                if(req <= resource && map.get(resource) > 0){ // 如果有合适的资源，则申请成功
                    sb.append("true").append(",");
                    map.put(resource, map.get(resource) - 1); //更新内存池
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                sb.append("false").append(",");
            }
        }
        System.out.println(sb.substring(0, sb.length() - 1));
    }
}
