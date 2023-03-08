package com.luke;

import java.util.LinkedList;
import java.util.Queue;

public class Problem9 {
    public static void main(String[] args) {
        char[][] grid1 = generateGrid1();
        char[][] grid2 = generateGrid2();
        // int num1 = numIslandsBfs(grid1);
        int num1 = numIslandsDfs(grid1);
        System.out.println(num1);
        // int num2 = numIslandsBfs(grid2);
        int num2 = numIslandsBfs(grid2);
        System.out.println(num2);
    }

    public static char[][] generateGrid1() {
        return new char[][]{
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };
    }

    public static char[][] generateGrid2() {
        return new char[][]{
                {'1','1','0','0','0'},
                {'1','1','0','0','0'},
                {'0','0','1','0','0'},
                {'0','0','0','1','1'}
        };
    }

    /**
     * 深度优先
     * @param grid 矩阵
     * @return int
     */
    public static int numIslandsDfs(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int totalRow = grid.length;
        int totalCol = grid[0].length;
        int numIslands = 0;
        for (int row = 0; row < totalRow; row++) {
            for (int col = 0; col < totalCol; col++) {
                if (grid[row][col] == '1') {
                    numIslands++;
                    dfs(grid,row,col);
                }
            }
        }
        return numIslands;
    }

    /**
     * 广度优先
     * @param grid 矩阵
     * @return int
     */
    public static int numIslandsBfs(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int totalRow = grid.length;
        int totalCol = grid[0].length;
        int numIslands = 0;
        for (int row = 0; row < totalRow; row++) {
            for (int col = 0; col < totalCol; col++) {
                if (grid[row][col] == '1') {
                    numIslands++;
                    bfs(grid,row,col);
                }
            }
        }
        return numIslands;
    }

    public static void dfs(char[][] grid, int row, int col) {
        int totalRow = grid.length;
        int totalCol = grid[0].length;
        boolean inRow = row < 0  || row >= totalRow;
        boolean inCol = col < 0 || col >= totalCol;
        // 校验 坐标是否有效，且 grid[row][col]是否遇到水，遇到水则返回
        if (inRow || inCol || grid[row][col] == '0') {
            return;
        }
        // 访问岛屿的节点，已经访问的岛屿节点标识为0
        grid[row][col] = '0';
        dfs(grid,row + 1, col); // 上
        dfs(grid,row - 1, col); // 下
        dfs(grid,row, col - 1); // 左
        dfs(grid,row, col + 1); // 右
    }

    public static void bfs(char[][] grid, int row, int col) {
        int totalRow = grid.length;
        int totalCol = grid[0].length;
        boolean inRow = row < 0  || row >= totalRow;
        boolean inCol = col < 0 || col >= totalCol;
        // 校验 坐标是否有效，且 grid[row][col]是否遇到水，遇到水则返回
        if (inRow || inCol || grid[row][col] == '0') {
            return;
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] {row, col});
        while (!queue.isEmpty()) {
            int[] ele = queue.poll();
            int curRow = ele[0];
            int curCol = ele[1];
            grid[curRow][curCol] = '0';
            if (curRow - 1 >= 0 && grid[curRow - 1][curCol] == '1') { // 上
                queue.add(new int[]{curRow - 1, curCol});
            }
            if (curRow + 1 < totalRow && grid[curRow + 1][curCol] == '1') { // 下
                queue.add(new int[]{curRow + 1, curCol});
            }
            if (curCol - 1 >= 0 && grid[curRow][curCol - 1] == '1') { // 左
                queue.add(new int[]{curRow, curCol - 1});
            }
            if (curCol + 1 < totalCol && grid[curRow][curCol + 1] == '1') { // 右
                queue.add(new int[]{curRow, curCol + 1});
            }
        }
    }
}
