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
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        int n = in.nextInt();
        int[] times = new int[n];
        int[] pays = new int[n];
        for (int i = 0; i < n; i++) {
            times[i] = in.nextInt();
            pays[i] = in.nextInt();
        }
        // dp[i][j]表示 在选择第i分工作时在j时间内小明能赚到的最多工资
        int[][] dp = new int[n + 1][T + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= T; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j >= times[i - 1]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - times[i - 1]] + pays[i - 1]);
                }
            }
        }
        System.out.println(dp[n][T]);
    }
}
```