package com.luke;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Problem39 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String[] strArray = s.split(",");
        int m = Integer.parseInt(strArray[0]); // 棋盘的总行数
        int n = Integer.parseInt(strArray[1]); // 棋盘的总列数
        int[][] chessBoard = generateChessBoard(strArray, m, n);
        int sRow = Integer.parseInt(strArray[strArray.length - 4]); // 源节点的行索引
        int sCol = Integer.parseInt(strArray[strArray.length - 3]); // 源节点的列索引
        int tRow = Integer.parseInt(strArray[strArray.length - 2]); // 目标节点的行索引
        int tCol = Integer.parseInt(strArray[strArray.length - 1]); // 目标节点的列索引
        sc.close();
        System.out.println(checkConnectivity(chessBoard,sRow,sCol,tRow,tCol));
    }

    /**
     *
     * @param chessBoard 棋盘
     * @param sRow 源节点的行索引
     * @param sCol 源节点的列索引
     * @param tRow 目标节点的行索引
     * @param tCol 目标节点的列索引
     * @return int 消除路线上图案的个数
     */
    public static int checkConnectivity(int[][] chessBoard,
                                        int sRow, int sCol,
                                        int tRow, int tCol) {
        int rowLen = chessBoard.length;
        int colLen = chessBoard[0].length;
        // 校验坐标的有效性
        if (sRow < 0 || sRow >= rowLen
            || sCol <0 || sCol >= colLen
            || tRow < 0 || tRow >= rowLen
            || tCol <0 || tCol >= colLen) {
            return 0;
        }
        // 1、校验图案是否相同
        int sDiagram = chessBoard[sRow][sCol];
        int tDiagram = chessBoard[tRow][tCol];
        if (sDiagram != tDiagram) {
            return 0;
        }
        // 获取源节点和目标节点附近的通路(即水平方向、垂直方向)
        // 源节点的通路(水平方向)
        List<int[]> sHorizontalPath = findPathPoint(chessBoard, sRow, sCol, "H");
        // 源节点的通路(垂直方向)
        List<int[]> sVerticalPath = findPathPoint(chessBoard, sRow, sCol, "V");
        // 目标节点的通路(水平方向)
        List<int[]> tHorizontalPath = findPathPoint(chessBoard, tRow, tCol, "H");
        // 目标节点的通路(垂直方向)
        List<int[]> tVerticalPath = findPathPoint(chessBoard, tRow, tCol, "V");

        // ======================情况1：相邻：即源节点和目标节点纵坐标或者横坐标相差1个单位======================
        if ((Math.abs(sRow - tRow) == 1 && sCol == tCol)
            || (sRow == tRow && Math.abs(sCol - tCol) == 1)) {
            return 2;
        }

        // ======================情况2：0或1个拐点======================
        // 源节点水平方向、目标节点垂直方向 相交的拐点
        if(sHorizontalPath.size() > 0 && tVerticalPath.size() > 0) {
            int[] gPointPixes = {sRow, tCol};
            boolean sConnected = false;
            for (int[] s : sHorizontalPath) {
                if (s[0] == gPointPixes[0] && s[1] == gPointPixes[1]) {
                    sConnected = true;
                    break;
                }
            }
            boolean tConnected = false;
            for (int[] s : tVerticalPath) {
                if (s[0] == gPointPixes[0] && s[1] == gPointPixes[1]) {
                    tConnected = true;
                    break;
                }
            }
            if (sConnected && tConnected) {
                return Math.abs(sCol - tCol) + Math.abs(sRow - tRow) + 1;
            }
        }
        // 源节点垂直方向、目标节点水平方向 相交的拐点
        if(sVerticalPath.size() > 0 && tHorizontalPath.size() > 0) {
            int[] gPointPixes = {tRow, sCol};
            boolean sConnected = false;
            for (int[] s : sVerticalPath) {
                if (s[0] == gPointPixes[0] && s[1] == gPointPixes[1]) {
                    sConnected = true;
                    break;
                }
            }
            boolean tConnected = false;
            for (int[] s : tHorizontalPath) {
                if (s[0] == gPointPixes[0] && s[1] == gPointPixes[1]) {
                    tConnected = true;
                    break;
                }
            }
            if (sConnected && tConnected) {
                return Math.abs(sRow - tRow) + Math.abs(sCol - tCol) + 1;
            }
        }

        // ======================情况3：2个拐点========================
        int minNumDiagram = Integer.MAX_VALUE;
        // 水平方向上找拐点
        if (sHorizontalPath.size() > 0 && tHorizontalPath.size() > 0) {
            // 靠近源节点的拐点
            for (int[] s : sHorizontalPath) {
                boolean isConnected = true;
                int[] target = null; // 靠近目标节点的拐点
                for (int[] t : tHorizontalPath) {
                    if (t[1] != s[1]) { // 列坐标要相同
                        continue;
                    }
                    // 判断s点和t点是否有通路
                    for (int k = Math.min(s[0], t[0]); k < Math.max(s[0], t[0]); k++) {
                        if (chessBoard[k][s[1]] != 0) {
                            isConnected = false;
                            break;
                        }
                    }
                    target = t;
                }
                if (isConnected && target != null) {
                    minNumDiagram = Math.min(minNumDiagram,
                            // (源节点横坐标 - 靠近源节点拐点的横坐标)
                            // + (靠近源节点拐点的纵坐标 - 靠近目标节点拐点的纵坐标)
                            // + (目标节点的横坐标 - 靠近目标节点拐点的横坐标)
                            // + 1
                            (Math.abs(sCol - s[1]) + Math.abs(s[0] - target[0]) + Math.abs(target[1] - tCol) + 1));
                }
            }
        }
        // 垂直方向上找拐点
        if (sVerticalPath.size() > 0 && tVerticalPath.size() > 0) {
            // 靠近源节点的拐点
            for (int[] s : sVerticalPath) {
                boolean isConnected = true;
                int[] target = null; // 靠近目标节点的拐点
                for (int[] t : tVerticalPath) {
                    if (t[0] != s[0]) { // 行坐标要相同
                        continue;
                    }
                    // 判断s点和t点是否有通路
                    for (int k = Math.min(s[1], t[1]); k < Math.max(s[1], t[1]); k++) {
                        if (chessBoard[s[0]][k] != 0) {
                            isConnected = false;
                            break;
                        }
                    }
                    target = t;
                }
                if (isConnected && target != null) {
                    minNumDiagram = Math.min(minNumDiagram,
                            // (源节点横坐标 - 靠近源节点拐点的横坐标)
                            // + (靠近源节点拐点的纵坐标 - 靠近目标节点拐点的纵坐标)
                            // + (目标节点的横坐标 - 靠近目标节点拐点的横坐标)
                            // + 1
                            (Math.abs(sRow - s[0]) + Math.abs(s[1] - target[1]) + Math.abs(target[0] - tRow) + 1));
                }
            }
        }

        if (minNumDiagram == Integer.MAX_VALUE) {
            return 0;
        }
        return minNumDiagram;
    }

    /**
     *
     * @param chessBoard 矩阵
     * @param row 行坐标
     * @param col 列坐标
     * @param pathType 通路类型: H水平方向通路，V垂直方向通路，HV或者VH水平和垂直方向
     * @return 通路上的节点坐标
     */
    public static List<int[]> findPathPoint(int[][] chessBoard, int row, int col, String pathType) {
        int rowLen = chessBoard.length;
        int colLen = chessBoard[0].length;
        List<int[]> result = new ArrayList<>();
        if (row < 0 || row >= rowLen || col < 0 || col >= colLen) {
            return result;
        }
        // 找水平通路
        if (pathType.contains("H")) {
            List<int[]> hList= new ArrayList<>();
            // 0~x之间的点
            for (int i = col - 1; i >= 0; i--) {
                if (chessBoard[row][i] != 0) {
                    break;
                }
                hList.add(new int[]{row, i});
            }
            // x~xLen之间的点
            for (int i = col + 1; i < colLen; i++) {
                if (chessBoard[row][i] != 0) {
                    break;
                }
                hList.add(new int[]{row, i});
            }
            if (hList.size() > 0) {
                result.addAll(hList);
            }
        }
        // 找垂直通路
        if (pathType.contains("V")) {
            List<int[]> vList= new ArrayList<>();
            // 0~y之间的点
            for (int i = row - 1; i >= 0; i--) {
                if (chessBoard[i][col] != 0) {
                    break;
                }
                vList.add(new int[] {i, col});
            }
            // y~yLen之间的点
            for (int i = row + 1; i < rowLen; i++) {
                if (chessBoard[i][col] != 0) {
                    break;
                }
                vList.add(new int[] {i, col});
            }
            if (vList.size() > 0) {
                result.addAll(vList);
            }
        }
        return result;
    }

    public static int[][] generateChessBoard(String[] strArray, int m, int n) {
        int[][] chessBoard = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                chessBoard[i][j] = Integer.parseInt(strArray[ i * n + j + 2]);
            }
        }
        return chessBoard;
    }
}
