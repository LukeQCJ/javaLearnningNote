package com.luke;

import java.util.LinkedList;
import java.util.Queue;

public class Problem15 {
    public static void main(String[] args) {
        int[][] grid = generateGrid();
        int maxArea = maxAreaOfIsland(grid);
        System.out.println(maxArea);
    }

    public static int maxAreaOfIsland(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int rowTotal = grid.length;
        int colTotal = grid[0].length;
        int maxArea = 0;
        for (int i = 0; i < rowTotal; i++) {
            for (int j = 0; j < colTotal; j++) {
                if (grid[i][j] == 1) {
                    int curArea = getAreaOfIslandBfs(grid, i, j);
                    maxArea = Math.max(maxArea,curArea);
                }
            }
        }
        return maxArea;
    }

    public static int[][] generateGrid() {
        return new int[][]{
                {1,1,0,0,0},
                {1,1,0,0,0},
                {0,0,0,1,1},
                {0,0,0,1,1}
        };
    }

    /**
     * 广度优先
     * @param grid 表格
     * @param row 行
     * @param col 列
     * @return int 面积
     */
    public static int getAreaOfIslandBfs(int[][] grid, int row, int col) {
        int rowTotal = grid.length;
        int colTotal = grid[0].length;
        boolean rowNotValid = row >= rowTotal || row < 0;
        boolean colNotValid = col >= colTotal || col < 0;
        if (rowNotValid || colNotValid) {
            return 0;
        }
        int area = 0;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] {row, col});
        while (!queue.isEmpty()) {
            int[] gridCoordinate = queue.poll();
            int r = gridCoordinate[0];
            int c = gridCoordinate[1];
            if (grid[r][c] == 1) {
                grid[r][c] = 0;
                area++;
            }
            if (r - 1 >= 0 && grid[r - 1][c] == 1) { // 上
                queue.add(new int[]{r - 1, c});
                grid[r - 1][c] = 0;
                area++;
            }
            if (r + 1 < rowTotal && grid[r + 1][c] == 1) { // 下
                queue.add(new int[]{r + 1, c});
                grid[r + 1][c] = 0;
                area++;
            }
            if (c - 1 >= 0 && grid[r][c - 1] == 1) { // 左
                queue.add(new int[]{r, c - 1});
                grid[r][c - 1] = 0;
                area++;
            }
            if (c + 1 < colTotal && grid[r][c + 1] == 1) { // 右
                queue.add(new int[]{r, c + 1});
                grid[r][c + 1] = 0;
                area++;
            }
        }
        return area;
    }
}
