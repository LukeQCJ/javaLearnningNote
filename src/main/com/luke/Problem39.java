package com.luke;

import java.util.ArrayList;
import java.util.List;

public class Problem39 {
    public static void main(String[] args) {
        int[][] chessBoard = generateChessBoard();
        System.out.println(checkConnectivity(chessBoard,0,1,0,2));
        System.out.println(checkConnectivity(chessBoard,1,1,3,0));
        System.out.println(checkConnectivity(chessBoard,2,0,3,2));
        System.out.println(checkConnectivity(chessBoard,0,0,2,3));
    }

    public static int checkConnectivity(int[][] chessBoard,
                                        int sPointX, int sPointY,
                                        int tPointX, int tPointY) {
        int xLen = chessBoard.length;
        int yLen = chessBoard[0].length;
        // 校验坐标的有效性
        if (sPointX < 0 || sPointX >= xLen
            || sPointY <0 || sPointY >= yLen
            || tPointX < 0 || tPointX >= xLen
            || tPointY <0 || tPointY >= yLen) {
            return 0;
        }
        // 校验图案是否相同
        int sDiagram = chessBoard[sPointX][sPointY];
        int tDiagram = chessBoard[tPointX][tPointY];
        if (sDiagram != tDiagram) {
            return 0;
        }
        // 获取源节点和目标节点附近的通路(即水平方向、垂直方向)
        // 源节点的通路(水平方向)
        List<int[]> sHorizontalPath = findPathPoint(chessBoard, sPointX, sPointY, "H");
        // 源节点的通路(垂直方向)
        List<int[]> sVerticalPath = findPathPoint(chessBoard, sPointX, sPointY, "V");
        // 目标节点的通路(水平方向)
        List<int[]> tHorizontalPath = findPathPoint(chessBoard, tPointX, tPointY, "H");
        // 目标节点的通路(垂直方向)
        List<int[]> tVerticalPath = findPathPoint(chessBoard, tPointX, tPointY, "V");
        // ======================情况1：相邻：即源节点和目标节点纵坐标或者横坐标相差1个单位======================
        if ((Math.abs(sPointX - tPointX) == 1 && sPointY == tPointY)
            || (sPointX == tPointX && Math.abs(sPointY - tPointY) == 1)) {
            return 2;
        }
        // ======================情况2：0或1个拐点======================
        // 源节点水平方向、目标节点垂直方向 相交的拐点
        if(sHorizontalPath.size() > 0 && tVerticalPath.size() > 0) {
            int[] gPointPixes = {tPointX, sPointY};
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
                return Math.abs(sPointX - tPointX) + Math.abs(sPointY - tPointY) + 1;
            }
        }
        // 源节点垂直方向、目标节点水平方向 相交的拐点
        if(sVerticalPath.size() > 0 && tHorizontalPath.size() > 0) {
            int[] gPointPixes = {sPointX, tPointY};
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
                return Math.abs(sPointX - tPointX) + Math.abs(sPointY - tPointY) + 1;
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
                    if (t[0] != s[0]) { // 横坐标要相同
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
                            (Math.abs(sPointX - s[0]) + Math.abs(s[1] - target[1]) + Math.abs(target[0] - tPointX) + 1));
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
                    if (t[1] != s[1]) { // 纵坐标要相同
                        continue;
                    }
                    // 判断s点和t点是否有通路
                    for (int k = Math.min(s[0], t[0]); k < Math.max(s[0], t[0]); k++) {
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
                            (Math.abs(sPointY - s[1]) + Math.abs(s[0] - target[0]) + Math.abs(target[1] - tPointY) + 1));
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
     * @param x 横坐标
     * @param y 纵坐标
     * @param pathType 通路类型: H水平方向通路，V垂直方向通路，HV或者VH水平和垂直方向
     * @return 通路上的节点坐标
     */
    public static List<int[]> findPathPoint(int[][] chessBoard, int x, int y, String pathType) {
        int xLen = chessBoard.length;
        int yLen = chessBoard[0].length;
        List<int[]> result = new ArrayList<>();
        if (x < 0 || x >= xLen || y < 0 || y >= yLen) {
            return result;
        }
        // 找水平通路
        if (pathType.contains("H")) {
            List<int[]> xList= new ArrayList<>();
            // 0~x之间的点
            for (int i = x - 1; i >= 0; i--) {
                if (chessBoard[x][i] != 0) {
                    break;
                }
                xList.add(new int[]{x, i});
            }
            // x~xLen之间的点
            for (int i = x + 1; i < xLen; i++) {
                if (chessBoard[x][i] != 0) {
                    break;
                }
                xList.add(new int[]{x, i});
            }
            if (xList.size() > 0) {
                result.addAll(xList);
            }
        }
        // 找垂直通路
        if (pathType.contains("V")) {
            List<int[]> yList= new ArrayList<>();
            // 0~y之间的点
            for (int i = x - 1; i >= 0; i--) {
                if (chessBoard[i][y] != 0) {
                    break;
                }
                yList.add(new int[] {i, y});
            }
            // y~yLen之间的点
            for (int i = y + 1; i < yLen; i++) {
                if (chessBoard[i][y] != 0) {
                    break;
                }
                yList.add(new int[] {i, y});
            }
            if (yList.size() > 0) {
                result.addAll(yList);
            }
        }
        return result;
    }
    public static int[][] generateChessBoard() {
        return new int[][]{
                {1, 3, 3, 4},
                {0, 6, 0, 0},
                {4, 0, 2, 1},
                {6, 0, 4, 2}
        };
    }
}
