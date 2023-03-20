package com.luke;

import java.util.*;

public class Problem16 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String wordDictString = sc.nextLine();
        wordDictString = wordDictString.substring(1, wordDictString.lastIndexOf(']'));
        String[] wordDict = wordDictString.replaceAll("\"", "").split(",");

        System.out.println(wordBreak(s, Arrays.asList(wordDict)));

        System.out.println(wordBreak2(s, Arrays.asList(wordDict)));

        sc.close();
    }

    public static boolean wordBreak(String s, List<String> wordDict) {
        // 动态规划 状态数组dp
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true; // 边界条件: dp[0] = true 表示空串且合法
        for (int i = 1; i <= s.length(); i++) {
            for (int k = 0; k < i; k++) {
                // s[0~(i - 1)]子串合法 = s[0~(k-1)]合法 && 存在s[k~(i-1)]包括在wordDict中合法
                String substring = s.substring(k, i);  // 表示[k,i)子串
                boolean isInWordDictPreviousK = dp[k]; // 表示[0,k)子串是否在wordDict字典中
                dp[i] = isInWordDictPreviousK && wordDict.contains(substring);
                if (dp[i]) {
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    public static List<String> wordBreak2(String s, List<String> wordDict) {
        // 为了快速判断一个单词是否在单词集合中，需要将它们加入哈希表
        Set<String> wordSet = new HashSet<>(wordDict);
        int len = s.length();
        // 第 1 步：动态规划计算是否有解
        // dp[i] 表示「长度」为 i 的 s 前缀子串可以拆分成 wordDict 中的单词
        // 长度包括 0 ，因此状态数组的长度为 len + 1
        boolean[] dp = new boolean[len + 1];
        // 0 这个值需要被后面的状态值参考，如果一个单词正好在 wordDict 中，dp[0] 设置成 true 是合理的
        dp[0] = true;
        for (int right = 1; right <= len; right++) {
            // 如果单词集合中的单词长度都不长，从后向前遍历是更快的
            for (int left = right - 1; left >= 0; left--) {
                // substring 不截取 s[right]，dp[left] 的结果不包含 s[left]
                if (wordSet.contains(s.substring(left, right)) && dp[left]) {
                    dp[right] = true;
                    // 这个 break 很重要，一旦得到 dp[right] = True ，不必再计算下去
                    break;
                }
            }
        }
        // 第 2 步：回溯算法搜索所有符合条件的解
        List<String> res = new ArrayList<>();
        if (dp[len]) {
            Deque<String> path = new ArrayDeque<>();
            dfs(s, len, wordSet, dp, path, res);
            return res;
        }
        return res;
    }

    /**
     * s[0:len) 如果可以拆分成 wordSet 中的单词，把递归求解的结果加入 res 中
     *
     * @param s       字符串
     * @param len     长度为 len 的 s 的前缀子串
     * @param wordSet 单词集合，已经加入哈希表
     * @param dp      预处理得到的 dp 数组
     * @param path    从叶子结点到根结点的路径
     * @param res     保存所有结果的变量
     */
    private static void dfs(String s, int len, Set<String> wordSet,
                            boolean[] dp, Deque<String> path, List<String> res) {
        if (len == 0) {
            res.add(String.join(" ", path));
            return;
        }
        // 可以拆分的左边界从 len - 1 依次枚举到 0
        for (int i = len - 1; i >= 0; i--) {
            String suffix = s.substring(i, len);
            if (wordSet.contains(suffix) && dp[i]) {
                path.addFirst(suffix); // 添加 回溯的关键步骤
                dfs(s, i, wordSet, dp, path, res);
                path.removeFirst();    // 删除 回溯的关键步骤
            }
        }
    }
}
