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

来源：力扣（LeetCode）

链接：https://leetcode.cn/problems/parse-lisp-expression

解决方案：https://leetcode.cn/problems/parse-lisp-expression/solution/lisp-by-jiang-hui-4-hhzv/

代码: 
```
import java.util.*;

public class Problem20 {
    private static final String LET = "let";
    private static final String ADD = "add";
    private static final String MUlT = "mult";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(evaluate(s));
        sc.close();
    }

    public static int evaluate(String expressionStr) {
        return cal(expressionStr, new HashMap<>());
    }

    private static int cal(String expressionStr, Map<String, Integer> preVariableScope) {
        // 一、解析 命令表达式字符串
        String[] instructions = splitExpression(expressionStr);
        // 二、计算命令表达式
        String command = instructions[0]; // 命令 let add mult
        int commandLength = instructions.length;
        // 二、计算命令表达式: 1、LET命令 let表达式格式: let v1 e1 ..... vn en expr
        if (LET.equals(command)) {
            // 1)、参数解析: 先处理变量对的赋值: i是变量,i+1是变量i的值
            Map<String, Integer> curVariableScope = new HashMap<>(preVariableScope);
            for (int i = 1; i < commandLength - 2; i += 2) {
                String vi = instructions[i]; // vi
                String eiStr = instructions[i + 1]; // ei
                int eiValue;
                // 遇到括号证明 ei仍然是一个表达式
                if (eiStr.charAt(0) == '(') {
                    // 递归 计算表达式
                    eiValue = cal(eiStr, curVariableScope);
                } else {
                    //获取eiStr的值，是数字还是变量，变量的话需要从map中获取
                    if (curVariableScope.containsKey(eiStr)) {
                        eiValue = curVariableScope.get(eiStr);
                    } else {
                        eiValue = Integer.parseInt(eiStr);
                    }
                }
                curVariableScope.put(vi, eiValue);
            }
            // 2)、计算最终值: 处理let的最后一个表达式expr
            String endExpression = instructions[commandLength - 1];
            if (endExpression.charAt(0) == '(') { // 表达式
                // 递归 计算表达式
                return cal(endExpression, curVariableScope);
            } else if (curVariableScope.containsKey(endExpression)) { // 变量
                return curVariableScope.get(endExpression);
            } else { // 数字
                return Integer.parseInt(endExpression);
            }
        // 二、计算命令表达式: 2、ADD或MULT命令 ADD命令 add表达式: add e1 e2; MULT命令: mult表达式: mult e1 e2
        } else {
            int[] variables = new int[2];
            // 1)、参数解析: 计算 e1 和 e2
            for (int i = 1; i < commandLength; i++) {
                String ei = instructions[i];
                if (ei.charAt(0) == '(') { // 判断ei是否为表达式
                    // 递归 计算表达式
                    variables[i - 1] = cal(ei, preVariableScope);
                } else if (preVariableScope.containsKey(ei)) { // 变量赋值从上一个变量作用域获取
                    variables[i - 1] = preVariableScope.get(ei);
                } else { // 数字直接赋值
                    variables[i - 1] = Integer.parseInt(ei);
                }
            }
            // 2)、计算最终值
            if (ADD.equals(command)) { // 加法
                return variables[0] + variables[1];
            } else if (MUlT.equals(command)) { // 乘法
                return variables[0] * variables[1];
            } else {
                return -1;
            }
        }
    }

    /**
     * 解析表达式字符串
     * @param expression 表达式字符串
     * @return 表达式数组
     */
    private static String[] splitExpression(String expression) {
        List<String> expressions = new ArrayList<>();
        // 1、去掉两个括号
        expression = expression.substring(1, expression.length() - 1);
        int left = 0, right = 0;
        while (right < expression.length()) {
            char ch = expression.charAt(right);
            // 2、空格 则分割表达式的命令格式
            if (ch == ' ') {
                expressions.add(expression.substring(left, right));
                left = right + 1;
            // 3、括号 则表示表达式,需要在下一阶段计算时候解析 即递归处理
            } else if (ch == '(') {
                int count = 0;
                while (right < expression.length()) {
                    // 进行括号匹配
                    ch = expression.charAt(right);
                    if (ch == '(') { // 左括号表示表达式开始
                        count++;
                    } else if (ch == ')') { // 右括号表示表达式结束
                        count--;
                    }
                    right++;
                    if (count == 0) { // 当表达式都匹配完毕，就跳出循环
                        break;
                    }
                }
                expressions.add(expression.substring(left, right)); // 将表达式放入返回list中
                left = right + 1;
            }
            right++;
        }
        // 4、如果表达式expression不是以括号结尾
        if (left < expression.length()) {
            expressions.add(expression.substring(left));
        }
        // list转数组
        return expressions.toArray(new String[0]);
    }
}
```