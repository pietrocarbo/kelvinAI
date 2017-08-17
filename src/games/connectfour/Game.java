package games.connectfour;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private static String seedStarter;

    public Game(String seedStarter) {
        this.seedStarter = seedStarter;
    }

    public static String getPlayer (String[][] board) {

        int plyCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(board[i][j].equals("X")) { // Human
                    plyCounter++;
                } else if (board[i][j].equals("O")) { // AI
                    plyCounter--;
                }
            }
        }

        return (plyCounter == 0 ? seedStarter :
                    plyCounter > 0 ? "O" : "X");  // "O" (return 0) is AI turn, "X" (return 1) is human turn, starter return (-1)
    }

    public static List<Action> getActions (String[][] board) {

        List<Action> result = new ArrayList<Action>();

        for (int j = 0; j < 7; j++) { // for each column
            for (int i = 0; i < 6; i++) { // and then for each row upwards

                if(board[i][j].equals("_")) { // blank
                    result.add(new Action(i, j, getPlayer(board)));
                    break;
                }
            }
        }
        return result;
    }

    public static String[][] getResult (String[][] board, Action move) {
        String[][] newBoard = new String[6][7];
//        if (move.getColumn() >= 0 && move.getColumn() <= 6 && !board[5][move.getColumn()].equals("_") ) {
//            System.err.println("getResult(): it was generated a move for a non-empty square");
//            System.exit(-1);
//        } else {
            copyBoard(board, newBoard);
            newBoard[move.getRow()][move.getColumn()] = move.getSeed();
        //}

        return newBoard;
    }

    private static String[][] copyBoard(String[][] originalBoard, String[][] copiedBoard) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                copiedBoard[i][j] = originalBoard[i][j];
            }
        }
        return copiedBoard;
    }

    public static int declareWinner (String[][] board) {

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
                        String w = board[x][y];
                        if (!w.equals("_") && w.equals(board[x+dx][y+dy]) && w.equals(board[x+2*dx][y+2*dy]) && w.equals(board[lastx][lasty])) {
                            if (w.equals("0"))   return 0;
                            else if (w.equals("X"))   return 1;
                        }
                    }
                }
            }
        }

        int turns = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(!board[i][j].equals("_")) { // not blank
                    turns++;
                }
            }
        }

        // draw check
        if (turns == (7*6))             return -1;
        return -2;
    }

    public static boolean isTerminal (String[][] board) {

        int winner = declareWinner(board);
        if (winner == 0 || winner == 1 || winner == 2)  return true;

        return false;
    }

    public static double getUtility(String[][] board) {

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

}
