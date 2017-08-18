package games.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private static char seedStarter;

    public Game(char seedStarter) {
        this.seedStarter = seedStarter;
    }

    public static char getPlayer (char[][] board) {

        int plyCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(board[i][j] == 'X') { // Human
                    plyCounter++;
                } else if (board[i][j] == 'O') { // AI
                    plyCounter--;
                }
            }
        }

        return (plyCounter == 0 ? seedStarter :
                    plyCounter > 0 ? 'O' : 'X');  // "O" (return 0) is AI turn, "X" (return 1) is human turn, starter return (-1)
    }

    public static List<Action> getActions (char[][] board) {

        List<Action> result = new ArrayList<Action>();

        for (int j = 0; j < 7; j++) { // for each column
            for (int i = 0; i < 6; i++) { // and then for each row upwards

                if(board[i][j] == '_') { // blank
                    result.add(new Action(i, j, getPlayer(board)));
                    break;
                }
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public static char[][] getResult (char[][] board, Action move) {
        char[][] newBoard = new char[6][7];
        if (move.getColumn() >= 0 && move.getColumn() <= 6 && board[5][move.getColumn()] != '_') {
            System.err.println("getResult(): it was generated a move for a non-empty square");
            System.exit(-1);
        } else {
            copyBoard(board, newBoard);
            newBoard[move.getRow()][move.getColumn()] = move.getSeed();
        }

        return newBoard;
    }

    private static char[][] copyBoard(char[][] originalBoard, char[][] copiedBoard) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                copiedBoard[i][j] = originalBoard[i][j];
            }
        }
        return copiedBoard;
    }

    public static int declareWinner (char[][] board) {

        // check every directions from each square
        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int[] d : directions) {
            int dx = d[0];
            int dy = d[1];
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    int lastx = x + 3*dx;
                    int lasty = y + 3*dy;
                    if (0 <= lastx && lastx < 6 && 0 <= lasty && lasty < 7) {
                        char w = board[x][y];
                        if (w != '_'  && w == board[x+dx][y+dy] && w == board[x+2*dx][y+2*dy] && w == board[lastx][lasty]) {
                            if (w == '0')   return 0;
                            else if (w == 'X')   return 1;
                        }
                    }
                }
            }
        }

        int turns = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(board[i][j] != '_') { // not blank
                    turns++;
                }
            }
        }

        // draw check
        if (turns == (7*6))             return -1;
        return -2;
    }

    public static boolean isTerminal (char[][] board) {

        int winner = declareWinner(board);
        if (winner == 0 || winner == 1 || winner == 2)  return true;

        return false;
    }

    public static double getUtility(char[][] board) {

        double draw = 0.0, AIwin = 10.0, AIloss = -10.0;

        int winner = declareWinner(board);

        if (winner == 0)        return AIwin;
        else if (winner == 1)   return AIloss;
        else if (winner == 2)   return draw;

        else {
            System.err.println("getUtility(): called on a non-terminal board");
            System.exit(-1);
            return 0.0;
        }
    }

    public static double evaluateHeuristic (char[][] board) {

        double one = 1.0, two = 10.0/2.0, three = 50.0/3.0, four = 1000.0/4.0;
        double score = 0.0;

        double[][] horizontalSB = new double[6][7];
        double[][] verticalSB = new double[6][7];
        double[][] diagonalSB = new double[6][7];
        double[][] antidiagonalSB = new double[6][7];

        List<double[][]> scoreBoards = new ArrayList<>();
        scoreBoards.add(horizontalSB);
        scoreBoards.add(antidiagonalSB);
        scoreBoards.add(diagonalSB);
        scoreBoards.add(verticalSB);


        System.out.println(Engine.printBoard(board, -1, 0));

        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int i = 0; i < 4; i++) {
            int dx = directions[i][0];
            int dy = directions[i][1];

            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    char w = board[x][y];

                    int lastx4 = x + 3*dx;
                    int lasty4 = y + 3*dy;

                    int lastx3 = x + 2*dx;
                    int lasty3 = y + 2*dy;

                    int lastx2 = x + dx;
                    int lasty2 = y + dy;


                    if (w != '_' && scoreBoards.get(i)[x][y] == 0.0) {

                        if (0 <= lastx4 && lastx4 < 6 && 0 <= lasty4 && lasty4 < 7) {

                            if (w == board[x + dx][y + dy] && w == board[x + 2 * dx][y + 2 * dy] && w == board[lastx4][lasty4]) {
                                System.out.println("four in a row");
                                if (w == '0') {
                                    scoreBoards.get(i)[x][y] += four;
                                    scoreBoards.get(i)[x+dx][y+dy] += four;
                                    scoreBoards.get(i)[x+2*dx][y+2*dy] += four;
                                    scoreBoards.get(i)[lastx4][lasty4] += four;
                                }
                                else if (w == 'X') {
                                    scoreBoards.get(i)[x][y] -= four;
                                    scoreBoards.get(i)[x+dx][y+dy] -= four;
                                    scoreBoards.get(i)[x+2*dx][y+2*dy] -= four;
                                    scoreBoards.get(i)[lastx4][lasty4] -= four;
                                }
                            }

                        } else if (0 <= lastx3 && lastx3 < 6 && 0 <= lasty3 && lasty3 < 7) {

                            if (w == board[x + dx][y + dy] && w == board[lastx3][lasty3]) {
                                System.out.println("three in a row");
                                if (w == '0') {
                                    scoreBoards.get(i)[x][y] += three;
                                    scoreBoards.get(i)[x+dx][y+dy] += three;
                                    scoreBoards.get(i)[lastx3][lasty3] += three;
                                } else if (w == 'X') {
                                    scoreBoards.get(i)[x][y] -= three;
                                    scoreBoards.get(i)[x+dx][y+dy] -= three;
                                    scoreBoards.get(i)[lastx3][lasty3] -= three;
                                }
                            }

                        } else if (0 <= lastx2 && lastx2 < 6 && 0 <= lasty2 && lasty2 < 7) {
                            System.out.println("sono qua");
                            if (w == board[lastx2][lasty2]) {
                                System.out.println("two in a row");
                                if (w == '0') {
                                    scoreBoards.get(i)[x][y] += two;
                                    scoreBoards.get(i)[lastx2][lasty2] += two;
                                } else if (w == 'X')  {
                                    scoreBoards.get(i)[x][y] -= two;
                                    scoreBoards.get(i)[lastx2][lasty2] -= two;
                                }
                            }

                        } else {
                            System.out.println("one in a row");
                            if (w == '0')           scoreBoards.get(i)[x][y] += one;
                            else if (w == 'X')      scoreBoards.get(i)[x][y] -= one;

                        }

                    }
                }
            }
        }


        for (double[][] scoreTable : scoreBoards) {
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    score += scoreTable[x][y];
                }
            }
        }
        System.out.println("score of previous table " + score);
        return score;
    }

}
