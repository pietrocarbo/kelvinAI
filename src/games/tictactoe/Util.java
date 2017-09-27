package games.tictactoe;

import java.util.logging.Logger;

public class Util {

    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    static int nodes = 0;

    public static char[][] getResult(char[][] board, Action move) {
        char[][] newBoard = new char[3][3];
        if (board[move.getRow()][move.getColumn()] != ' ') {
            LOGGER.severe("getResult(): it was generated a move for a non-empty square");
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newBoard[i][j] = board[i][j];
                }
            }
            newBoard[move.getRow()][move.getColumn()] = move.getSeed();
        }
        return newBoard;
    }

    public static boolean isLegalMove(char[][] grid, int row, int column) {
        return row >= 0 && row <= 2 && column >= 0 && column <= 2 && grid[row][column] == ' ';
    }

    public static int isGameOver(char[][] grid) {

        for (int i = 0; i < 3; i++) {

            if (grid[i][0] == grid[i][1] && grid[i][0] == grid[i][2] && grid[i][0] != ' ') {
                if (grid[i][0] == 'O')      return 0;
                else                        return 1;
            }
            if (grid[0][i] == grid[1][i] && grid[0][i] == grid[2][i] && grid[0][i] != ' ') {
                if (grid[0][i] == 'O')      return 0;
                else                        return 1;
            }
        }

        if (grid[1][1] != ' ' && ((grid[1][1] == grid[0][0] && grid[1][1] == grid[2][2]) || (grid[1][1] == grid[0][2] && grid[1][1] == grid[2][0]))) {
            if (grid[1][1] == 'O')         return 0;
            else                           return 1;
        }

        if (getTurn(grid) == 9)            return -1;

        return -2;
    }

    public static Action minMaxAlgorithm(char[][] grid, char mySeed) {

        // if (Util.getTurn(grid) == 0)    return new Action(1, 1, mySeed);

        nodes = 1;

        Action bestMove = null;

        double resultValue = Double.NEGATIVE_INFINITY;

        for (Action action : Action.getActions(grid, mySeed)) {

            double value = minMaxRecursiveFunction(getResult(grid, action), mySeed, toggle(mySeed), false);

            LOGGER.fine("action " + action.getRow() + "," + action.getColumn() + " scored " + value);

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }

        }
        LOGGER.fine("move selected is " + bestMove.getRow() + "," + bestMove.getColumn() + " with highest score of " + resultValue + " (nodes visited " + nodes + ")");
        return new Action(bestMove.getRow(), bestMove.getColumn(), mySeed);
    }

    public static double minMaxRecursiveFunction(char[][] board, char player, char playing, boolean maximize) {
        nodes++;

        if (isGameOver(board) != -2) {
            return getUtility(board, player);
        }

        double value;

        if (maximize) {
            value = Double.NEGATIVE_INFINITY;
            for (Action action : Action.getActions(board, playing)) {
                value = Math.max(value, minMaxRecursiveFunction(getResult(board, action), player, toggle(playing), false));
            }
        } else {
            value = Double.POSITIVE_INFINITY;
            for (Action action : Action.getActions(board, playing)) {
                value = Math.min(value, minMaxRecursiveFunction(getResult(board, action), player, toggle(playing), true));
            }
        }

        return value;
    }

    public static char toggle(char actualPlayer) {
        return (actualPlayer ==  'O' ? 'X' : 'O');
    }

    public static int getTurn(char[][] board) {
        int turnsPlayed = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != ' ') {
                    turnsPlayed++;
                }
            }
        }
        return turnsPlayed;
    }

    public static double getUtility(char[][] board, char mySeed) {
        LOGGER.finer("utility evaluation (" + mySeed + " POV) of terminal board");
        LOGGER.finer(stringifyBoard(board));

        char opponentSeed = toggle(mySeed);
        int turnsPlayed = getTurn(board);

        double draw = 0.0, win = 10.0 - turnsPlayed, loss = -10.0 + turnsPlayed;

        if      (mySeed == board[0][0] && mySeed == board[0][1] && mySeed == board[0][2])                       return win;
        else if (opponentSeed == board[0][0] && opponentSeed == board[0][1] && opponentSeed == board[0][2])     return loss;

        if      (mySeed == board[1][0] && mySeed == board[1][1] && mySeed == board[1][2])                       return win;
        else if (opponentSeed == board[1][0] && opponentSeed == board[1][1] && opponentSeed == board[1][2])     return loss;

        if      (mySeed == board[2][0] && mySeed == board[2][1] && mySeed == board[2][2])                       return win;
        else if (opponentSeed == board[2][0] && opponentSeed == board[2][1] && opponentSeed == board[2][2])     return loss;

        if      (mySeed == board[0][0] && mySeed == board[1][0] && mySeed == board[2][0])                       return win;
        else if (opponentSeed == board[0][0] && opponentSeed == board[1][0] && opponentSeed == board[2][0])     return loss;

        if      (mySeed == board[0][1] && mySeed == board[1][1] && mySeed == board[2][1])                       return win;
        else if (opponentSeed == board[0][1] && opponentSeed == board[1][1] && opponentSeed == board[2][1])     return loss;

        if      (mySeed == board[0][2] && mySeed == board[1][2] && mySeed == board[2][2])                       return win;
        else if (opponentSeed == board[0][2] && opponentSeed == board[1][2] && opponentSeed == board[2][2])     return loss;

        if      (mySeed == board[0][0] && mySeed == board[1][1] && mySeed == board[2][2])                       return win;
        else if (opponentSeed == board[0][0] && opponentSeed == board[1][1] && opponentSeed == board[2][2])     return loss;

        if      (mySeed == board[2][0] && mySeed == board[1][1] && mySeed == board[0][2])                       return win;
        else if (opponentSeed == board[2][0] && opponentSeed == board[1][1] && opponentSeed == board[0][2])     return loss;

        if (turnsPlayed == 9) return draw;

        LOGGER.severe("getUtility(): called on a non-terminal board");
        System.exit(-1);
        return 0.0;
    }

    private static String stringifyBoard(char[][] board) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < 3; i++) {
            sb.append("|");
            for (int y = 0; y < 3; y++) {
                sb.append(board[i][y] + "|");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
