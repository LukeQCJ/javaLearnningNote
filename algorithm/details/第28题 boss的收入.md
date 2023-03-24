## 赚最多的工资

> **【题目描述：】**
> 
> 小明今天到公司，老板给了他一份可随意选择的工作内容清单，
> 
> 上面写着对应的[工作时长]及[报酬]，请问小明该怎样选择才能在今天赚到最多的工资呢？

> **示例：**
> 
> 输入:
> 
> 40 3
> 
> 20 10
> 
> 20 20
> 
> 10 5
>
> 说明:
> 
> 40是今天最多工作的时间 3是可选择的工作数量
> 
> 20 10 是第一件工作 耗时20 收入10
> 
> 20 20 是第二件工作 耗时20 收入20
> 
> 10 5 是第三件工作 耗时10 收入5

> **输出：**
> 
> 30
>
> 说明：
> 30是今天最多收入30(做第一件工作和第二件工作的收入)

代码：
动态规划
```
import java.util.Scanner;

public class Problem28 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int time = sc.nextInt(); // 工作最多时长
        int count = sc.nextInt(); // 可供选择的工作个数,即工作清单的工作条数
        // 初始化工作清单和报酬清单
        int[] costTimes = new int[count];
        int[] rewards = new int[count];
        for (int i = 0; i < count; i++) {
            int workTime = sc.nextInt();
            int reward = sc.nextInt();
            costTimes[i] = workTime;
            rewards[i] = reward;
        }
        int maxReward = getMaxReward(time, count, costTimes, rewards);
        System.out.println(maxReward);
        sc.close();
    }

    private static int getMaxReward(int longestWorkTime, int count, int[] costTimes, int[] rewards) {
        // dp[workNum][curTime] 表示在选择第workNum份工作的时候在curTime时间内小明能赚到的最多工资
        int[][] dp = new int[count + 1][longestWorkTime + 1];
        dp[0][0] = 0; // 表示在没有选择工作，没有花时间的工资
        for (int workNum = 1; workNum <= count; workNum++) {
            for (int curTime = 0; curTime <= longestWorkTime; curTime++) {
                // 表示在第workNum - 1份工作curTime时间内的报酬与第workNum份工作在curTime时间内的工资报酬是一样的，因为这个时候第workNum份工作还没有开始
                // 即 前一份工作结束的报酬和后一份工作开始的报酬是一样的
                dp[workNum][curTime] = dp[workNum - 1][curTime];
                if (curTime - costTimes[workNum - 1] >= 0) {
                    dp[workNum][curTime] = Math.max(dp[workNum][curTime],
                            dp[workNum - 1][curTime - costTimes[workNum - 1]] + rewards[workNum - 1]);
                }
            }
        }
        return dp[count][longestWorkTime];
    }
}
```