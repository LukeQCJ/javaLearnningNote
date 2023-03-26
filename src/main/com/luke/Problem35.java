package src.main.com.luke;

import java.util.*;

public class Problem35 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int H = sc.nextInt();
        int N = sc.nextInt();
        int[] heights = new int[N];
        for (int i = 0; i < N; i++) {
            int h = sc.nextInt();
            heights[i] = h;
        }
        shenGaoCha(H,N,heights);
        sc.close();
    }

    public static void shenGaoCha(int base, int length, int[] nums) {
        // 1、问题解决的关键: 由于题目指出身高值不重复，则使用map来构造身高和标准身高的差的绝对值
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < length; i++) {
            map.put(nums[i], Math.abs(nums[i] - base));
        }
        // 2、放入list中，并排序
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort((o1, o2) -> {
            //绝对值升序
            int compare = o1.getValue() - o2.getValue();
            //如果身高一样，按身高升序
            if (compare == 0) {
                return o1.getKey().compareTo(o2.getKey());
            } else {
                return compare;
            }
        });
        // 3、遍历打印结果
        for (Map.Entry<Integer, Integer> entry : entries) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println();
    }
}
