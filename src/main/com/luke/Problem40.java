package src.main.com.luke;

import java.util.Scanner;

public class Problem40 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String MNStr = in.nextLine();
        int M = Integer.parseInt(MNStr.split(",")[0]);
        int N = Integer.parseInt(MNStr.split(",")[1]);
        String[][] matrix = new String[M][N];

        for (int i = 0; i < M; i++) {
            String nScoreStr = in.nextLine();
            // 初始化学生方阵赋值
            matrix[i] = nScoreStr.split(",");
        }
        // 获取最大的位置相连的男生数量
        System.out.println(getMaxLenOfMan(matrix));
    }

    public static int getMaxLenOfMan(String[][] matrix) {
        if (matrix == null) {
            return 0;
        }
        // 行数
        int rowTotal = matrix.length;
        // 列数
        int colTotal = matrix[0].length;
        // 访问标识矩阵
        boolean[][] matrixFlag = new boolean[rowTotal][colTotal];
        int maxLen = 0;
        String[] types = {"vertical", "horizontal", "diagonal", "back-diagonal"};
        for (String type : types) {
            for (int i = 0; i < rowTotal; i++) {
                for (int j = 0; j < colTotal; j++) {
                    if (!"M".equals(matrix[i][j]) || matrixFlag[i][j]) {
                        continue;
                    }
                    // 深度优先 获取获取最大的位置相连的男生数量
                    int curLen = deep(matrix,matrixFlag,i,j,type);
                    maxLen = Math.max(curLen, maxLen);
                    // 重置标识矩阵为初始化状态
                    matrixFlag = new boolean[rowTotal][colTotal];
                }
            }
        }
        return maxLen;
    }

    public static int deep(String[][] matrix, boolean[][] matrixFlag, int rowIndex, int colIndex, String type) {
        int rowTotal = matrix.length;
        int colTotal = matrix[0].length;
        if (rowIndex >= rowTotal || rowIndex < 0) {
            return 0;
        }
        if (colIndex >= colTotal || colIndex < 0) {
            return 0;
        }
        if (!"M".equals(matrix[rowIndex][colIndex]) || matrixFlag[rowIndex][colIndex]) {
            return 0;
        }
        matrixFlag[rowIndex][colIndex] = true;
        int MM = 0, NN = 0;
        // 垂直
        if ("vertical".equals(type)) {
            // 上
            MM = deep(matrix,matrixFlag,rowIndex - 1, colIndex, type);
            // 下
            NN = deep(matrix,matrixFlag,rowIndex + 1, colIndex, type);
        }
        // 水平
        if ("horizontal".equals(type)) {
            // 左
            MM = deep(matrix,matrixFlag,rowIndex, colIndex - 1, type);
            // 右
            NN = deep(matrix,matrixFlag,rowIndex, colIndex + 1, type);
        }
        // 对角
        if ("diagonal".equals(type)) {
            // 左上
            MM = deep(matrix,matrixFlag,rowIndex - 1, colIndex - 1, type);
            // 右下
            NN = deep(matrix,matrixFlag,rowIndex + 1, colIndex + 1, type);
        }
        // 反对角
        if ("back-diagonal".equals(type)) {
            // 左下
            MM = deep(matrix,matrixFlag,rowIndex + 1, colIndex - 1, type);
            // 右上
            NN = deep(matrix,matrixFlag,rowIndex - 1, colIndex + 1, type);
        }
        return 1 + MM + NN;
    }
}
