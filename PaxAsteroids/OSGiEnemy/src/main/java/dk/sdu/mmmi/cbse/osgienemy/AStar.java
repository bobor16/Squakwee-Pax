/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgienemy;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author rasmu
 */
public class AStar {

    private static final int DIAGONAL_COST = 14;
    private static final int VERTICAL_COST = 10;
    private PriorityQueue<Node> openList;
    private Node[][] tileMap;
    private boolean[][] closedNodes;
    private int startI, startJ;
    private int endI, endJ;

    public AStar(int ROW, int COL, int startI, int startJ, int endI, int endJ, int[][] blocks) {
        tileMap = new Node[ROW][COL];
        closedNodes = new boolean[ROW][COL];

        openList = new PriorityQueue<>(new NodeComparator());

        startNode(startI, startJ);
        endNode(endI, endJ);

        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                tileMap[i][j] = new Node(i, j);
                tileMap[i][j].setH(Math.abs(i - endI) + Math.abs(j - endJ));
                tileMap[i][j].setSolution(false);
            }
        }
        tileMap[startI][startJ].setF(0);
        for (int i = 0; i < blocks.length; i++) {
            addBlockOnTileMap(blocks[i][0], blocks[i][1]);
        }
    }

    private void addBlockOnTileMap(int i, int j) {
        tileMap[i][j] = null;
    }

    private void startNode(int i, int j) {
        this.startI = i;
        this.startJ = j;
    }

    private void endNode(int i, int j) {
        this.endI = i;
        this.endJ = j;
    }

    private void updateCost(Node current, Node t, int cost) {
        if (t == null || closedNodes[t.getI()][t.getJ()]) {
            return;
        }
        int tFinalCost = t.getH() + cost;
        boolean isOpen = openList.contains(t);

        if (!isOpen || tFinalCost < t.getF()) {
            t.setF(tFinalCost);
            t.setParent(current);
        }
        if (!isOpen) {
            openList.add(t);
        }
    }

    public ArrayList<int[]> process() {
        if(tileMap[startI][startJ] == null){
            return new ArrayList<>();
        }
        openList.add(tileMap[startI][startJ]);
        Node current;
        while (true) {
            current = openList.poll();

            if (current == null) {
                break;
            }
            closedNodes[current.getI()][current.getJ()] = true;

            if (current.equals(tileMap[endI][endJ])) {
                break;
            }
            Node t;
            if (current.getI() - 1 >= 0) {
                t = tileMap[current.getI() - 1][current.getJ()];
                updateCost(current, t, (current.getF() + VERTICAL_COST));

                if (current.getJ() - 1 >= 0) {
                    t = tileMap[current.getI() - 1][current.getJ() - 1];
                    updateCost(current, t, (current.getF() + DIAGONAL_COST));
                }
                if (current.getJ() + 1 < tileMap[0].length) {
                    t = tileMap[current.getI() - 1][current.getJ() + 1];
                    updateCost(current, t, (current.getF() + DIAGONAL_COST));
                }
            }
            if (current.getJ() - 1 >= 0) {
                t = tileMap[current.getI()][current.getJ() - 1];
                updateCost(current, t, current.getF() + VERTICAL_COST);
            }
            if (current.getJ() + 1 < tileMap[0].length) {
                t = tileMap[current.getI()][current.getJ() + 1];
                updateCost(current, t, current.getF() + VERTICAL_COST);
            }
            if (current.getI() + 1 < tileMap.length) {
                t = tileMap[current.getI() + 1][current.getJ()];
                updateCost(current, t, current.getF() + VERTICAL_COST);

                if (current.getJ() - 1 >= 0) {
                    t = tileMap[current.getI() + 1][current.getJ() - 1];
                    updateCost(current, t, current.getF() + DIAGONAL_COST);
                }
                if (current.getJ() + 1 < tileMap[0].length) {
                    t = tileMap[current.getI() + 1][current.getJ() + 1];
                    updateCost(current, t, current.getF() + DIAGONAL_COST);
                }
            }
        }

        return makePath();
    }

    public void display() {
        System.out.println("Grid");

        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                if (i == startI && j == startJ) {
                    System.out.print("SO  ");
                } else if (i == endI && j == endJ) {
                    System.out.print("DE  ");
                } else if (tileMap[i][j] != null) {
                    System.out.printf("%-3d ", 0);
                } else {
                    System.out.print("BL  ");
                }

            }
            System.out.println();
        }
        System.out.println();
    }

    public void displayScores() {
        System.out.println("\n score for cells :");
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                if (tileMap[i][j] != null) {
                    System.out.printf("%-3d ", tileMap[i][j].getF());
                } else {
                    System.out.print("BL: ");
                }

            }
            System.out.println();

        }
        System.out.println();
    }

    public void displaySolution() {
        if (closedNodes[endI][endJ]) {
            System.out.println("path: ");
            Node current = tileMap[endI][endJ];
            System.out.println(current);
            tileMap[current.getI()][current.getJ()].setSolution(true);
            while (current.getParent() != null) {
                System.out.print(" -> " + current.getParent());
                tileMap[current.getParent().getI()][current.getParent().getJ()].setSolution(true);
                current = current.getParent();
            }
            System.out.println("\n");
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    if (i == startI && j == startJ) {
                        System.out.print("SO  ");
                    } else if (i == endI && j == endJ) {
                        System.out.print("DE  ");
                    } else if (tileMap[i][j] != null) {
                        System.out.printf("%-3s ", tileMap[i][j].isSolution() ? "X" : "0");
                    } else {
                        System.out.print("BL  ");
                    }
                }
                System.out.println("");
            }
            System.out.println("");

        } else {
            System.out.println("No solution:");
        }
    }

    private ArrayList<int[]> makePath() {
        Node current = tileMap[endI][endJ];
        ArrayList<int[]> path = new ArrayList<>(); 
        if (current != null) {
            tileMap[current.getI()][current.getJ()].setSolution(true);
            path.add(new int[]{current.getI(), current.getJ()});
            while (current.getParent() != null) {
                tileMap[current.getParent().getI()][current.getParent().getJ()].setSolution(true);
                current = current.getParent();
                path.add(new int[]{current.getI(), current.getJ()});
            }
        }

        return path;
    }
}
