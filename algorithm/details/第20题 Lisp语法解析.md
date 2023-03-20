## Lisp 语法解析

> 【题目描述】
> 
> 给你一个类似 Lisp 语句的字符串表达式 expression，求出其计算结果。
>
> 表达式语法如下所示:
>
> 表达式 可以为整数，let 表达式，add 表达式，mult 表达式，或赋值的变量。
> 
> 表达式的结果总是一个整数。(整数可以是正整数、负整数、0)
> 
> (1) let 表达式采用"(let v1 e1 v2 e2 ... vn en expr)" 的形式，
>> 其中，let 总是以字符串"let"来表示，接下来会跟随一对或多对交替的变量和表达式，
>> 也就是说，第一个变量v1被分配为表达式e1的值，第二个变量v2被分配为表达式e2的值，
>> 依次类推；最终 let 表达式的值为expr表达式的值。
>
> (2) add 表达式表示为"(add e1 e2)" ，其中add 总是以字符串"add" 来表示，
>> 该表达式总是包含两个表达式 e1、e2 ，最终结果是e1表达式的值与e2表达式的值之 和 。
>
> (3) mult 表达式表示为"(mult e1 e2)"，其中mult 总是以字符串 "mult" 表示，
>> 该表达式总是包含两个表达式 e1、e2，最终结果是e1 表达式的值与e2表达式的值之 积 。

> 在该题目中，变量名以小写字符开始，之后跟随 0 个或多个小写字符或数字。
> 
> 为了方便，"add" ，"let" ，"mult" 会被定义为 "关键字" ，不会用作变量名。

> 最后，要说一下 作用域 的概念。
>> 计算变量名所对应的表达式时，在计算上下文中，首先检查最内层作用域（按括号计），然后按顺序依次检查外部作用域。
>> 测试用例中每一个表达式都是合法的。有关作用域的更多详细信息，请参阅示例。

> **示例 1：**
>> 输入：expression = "(let x 2 (mult x (let x 3 y 4 (add x y))))"
>> 
>> 输出：14
>> 
>> 解释：
>> 
>> 计算表达式 (add x y), 在检查变量 x 值时，
>> 在变量的上下文中由最内层作用域依次向外检查。
>> 首先找到 x = 3, 所以此处的 x 值是 3 。

> **示例 2：**
>> 输入：expression = "(let x 3 x 2 x)"
>> 
>> 输出：2
>> 
>> 解释：let 语句中的赋值运算按顺序处理即可。

> **示例 3：**
>> 输入：expression = "(let x 1 y 2 x (add x y) (add x y))"
>> 
>> 输出：5
>> 
>> 解释：
>> 
>> 第一个 (add x y) 计算结果是 3，并且将此值赋给了 x 。
>> 
>> 第二个 (add x y) 计算结果是 3 + 2 = 5 。

> 提示：
>> 1 <= expression.length <= 2000
>> 
>> exprssion 中不含前导和尾随空格
>> 
>> expressoin 中的不同部分（token）之间用单个空格进行分隔
>> 
>> 答案和所有中间计算结果都符合 32-bit 整数范围
>> 
>> 测试用例中的表达式均为合法的且最终结果为整数

> 来源：力扣（LeetCode）
> 
> 链接：https://leetcode.cn/problems/parse-lisp-expression
> 
> 解决方案：https://leetcode.cn/problems/parse-lisp-expression/solution/lisp-by-jiang-hui-4-hhzv/

代码: 
```
class Solution {
    private static final String LET = "let";
    private static final String ADD = "add";
    private static final String MUlT = "mult";

    public int evaluate(String expression) {
        return cal(expression, new HashMap<>());
    }

    private int cal(String expression, Map<String, Integer> preScope) {
        String[] strs = splitExpression(expression);
        if (LET.equals(strs[0])) {
            //let表达式都是 let v1 e1 ..... expr
            //先处理赋值 i是变量 i+1是变量i的值
            Map<String, Integer> curScope = new HashMap<>(preScope);
            for (int i = 1; i < strs.length - 2; i += 2) {
                int e1;
                //遇到括号证明 e1仍然是一个表达式
                if (strs[i + 1].charAt(0) == '(') {
                    //递归调用cal 解析表达式
                    e1 = cal(strs[i + 1], curScope);
                } else {
                    //获取str[i]的值，是数字还是变量，变量的话需要从map中获取
                    if (curScope.containsKey(strs[i + 1])) {
                        e1 = curScope.get(strs[i + 1]);
                    } else {
                        e1 = Integer.parseInt(strs[i + 1]);
                    }
                }
                curScope.put(strs[i], e1);
            }
            //处理let的最后一个表达式expr
            if (strs[strs.length - 1].charAt(0) == '(') {
                return cal(strs[strs.length - 1], curScope);
            } else {
                //有可能是变量或者数字 比如:(let x 3 x 2 x) 最后是变量
                //如果scope中存在该字符串，一定是变量
                if (curScope.containsKey(strs[strs.length - 1])) {
                    return curScope.get(strs[strs.length - 1]);
                }
                return Integer.parseInt(strs[strs.length - 1]);
            }
        } else {
            //不是let 一定是add or mult
            int[] ans = new int[2];
            //add和mult处理相似 所以合并处理
            //分别计算e1和e2
            for (int i = 1; i <= 2; i++) {
                if (strs[i].charAt(0) == '(') {
                    //表达式 递归处理
                    ans[i - 1] = cal(strs[i], preScope);
                } else {
                    //数字或者变量
                    if (preScope.containsKey(strs[i])) {
                        ans[i - 1] = preScope.get(strs[i]);
                    } else {
                        ans[i - 1] = Integer.parseInt(strs[i]);
                    }
                }
            }
            return ADD.equals(strs[0]) ? ans[0] + ans[1] : ans[0] * ans[1];
        }
    }

    private String[] splitExpression(String expression) {
        List<String> res = new ArrayList<>();
        //去掉两个括号
        expression = expression.substring(1, expression.length() - 1);
        int i = 0, j = 0;
        while (j < expression.length()) {
            if (expression.charAt(j) == ' ') {
                //分割字符串
                res.add(expression.substring(i, j));
                i = j + 1;
            } else if (expression.charAt(j) == '(') {
                //统计括号数量 也就是当前括号内的进行递归处理
                //需要找到和此左括号匹配的右括号作为子表达式的结束
                int count = 0;
                while (j < expression.length()) {
                    //进行括号匹配
                    if (expression.charAt(j) == '(') {
                        count++;
                    } else if (expression.charAt(j) == ')') {
                        count--;
                    }
                    j++;
                    if (count == 0) {
                        break;
                    }
                }
                res.add(expression.substring(i, j));
                i = j + 1;
            }
            j++;
        }
        if (i < expression.length()) {
            res.add(expression.substring(i));
        }
        return res.toArray(new String[0]);
    }
}
```