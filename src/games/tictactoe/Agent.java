package games.tictactoe;

import java.util.ArrayList;
import java.util.List;

class Action {
    private int row;
    private int column;
    private int marker;

    public Action(int row, int column, int playerMarker) {
        this.row = row;
        this.column = column;
        this.marker = playerMarker;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getMarker() {
        return marker;
    }
}

public class Agent {
    private int[][] board;
    private final int CONVERT_UNIT = 1;   // has to be unsigned
    private int nodeVisited = 0;

    // getPlayer(board) -> current player
    public int getPlayer (int[][] board) {

        int aiPly = 0, humanPly = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] > 0)        aiPly++;
                else if (board[i][j] < 0)   humanPly++;
            }
        }

        if     (aiPly <= humanPly)   return 1;  // O turn (AI index +1)
        else /*(aiPly > humanPly)*/  return -1; // X turn (Human idx -1)
    }


    // getActions(board) -> list of moves available
    public List<Action> getActions (int[][] board) {
        ArrayList<Action> movesAvailible = new ArrayList<>();

        int markerValue = (getPlayer(board) == 1 ) ? CONVERT_UNIT : -CONVERT_UNIT;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0)   movesAvailible.add(new Action(i, j, markerValue));
            }
        }

        return movesAvailible;
    }

    // getResult(board, move) -> new board
    public int[][] getResult (int[][] board, Action move) {
        int[][] newBoard = new int[3][3];
        if (board[move.getRow()][move.getColumn()] != 0) {
            System.err.println("getResult(): it was generated a move for a non-empty square");
        } else {
            copyBoard(board, newBoard);
            newBoard[move.getRow()][move.getColumn()] = move.getMarker();
        }
        return newBoard;
    }

    private int[][] copyBoard(int[][] originalBoard, int[][] copiedBoard) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copiedBoard[i][j] = originalBoard[i][j];
            }
        }
        return copiedBoard;
    }

    private int[][] convertBoardFormat(String[][] stringBoard, int convertUnit) {
        int[][] intBoard = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (stringBoard[i][j]) {
                    case " ":   // empty square
                        intBoard[i][j] = 0;
                        break;
                    case "O":  // AI marker
                        intBoard[i][j] = convertUnit;
                        break;
                    case "X":  // oppenent marker
                        intBoard[i][j] = -convertUnit;
                        break;
                }
            }
        }
        return intBoard;
    }

    public int[] ply (String[][] stringBoard) {
        nodeVisited = 0;
        Action bestMove = null;
        int[][] board = convertBoardFormat(stringBoard, this.CONVERT_UNIT);

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity
        int player = getPlayer(board);  // 0 is AI, 1 is Human

        for (Action action : getActions(board)) {  // list of Action for empty squares filling moves

            double value = minValue(getResult(board, action), player); // recursion called with new board

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }
        }

        return new int[]{bestMove.getRow(), bestMove.getColumn(), nodeVisited};
    }

    public double minValue(int[][] board, int player) {  // returns an utility value
        nodeVisited++;

        if (isTerminal(board))
            return getUtility(board, player);

        double value = Double.POSITIVE_INFINITY;

        for (Action action : getActions(board))
            value = Math.min(value, maxValue(getResult(board, action), player));

        return value;
    }

    public double maxValue(int[][] board, int player) { // returns an utility value
        nodeVisited++;

        if (isTerminal(board))
            return getUtility(board, player);

        double value = Double.NEGATIVE_INFINITY;

        for (Action action : getActions(board))
            value = Math.max(value, minValue(getResult(board, action), player));

        return value;
    }

    public double getUtility(int[][] board, int player) {
        int counter = 0, aiPly = 0, aiWin = CONVERT_UNIT * 3, humanPly = 0, humanWin = CONVERT_UNIT * -3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] > 0)        aiPly++;
                else if (board[i][j] < 0)   humanPly++;
            }
        }
        if ((aiPly + humanPly) == 9)   return 0.0;

        counter = board[0][0] + board[0][1] + board[0][2];
        if (counter == aiWin )          return 1.0;
        else if (counter == humanWin)   return -1.0;

        counter = board[1][0] + board[1][1] + board[1][2];
        if (counter == aiWin)           return 1.0;
        else if (counter == humanWin)   return -1.0;

        counter = board[2][0] + board[2][1] + board[2][2];
        if (counter == aiWin)           return 1.0;
        else if (counter == humanWin)   return -1.0;

        counter = board[0][0] + board[1][0] + board[2][0];
        if (counter == aiWin)           return 1.0;
        else if (counter == humanWin)   return -1.0;

        counter = board[0][1] + board[1][1] + board[2][1];
        if (counter == aiWin)           return 1.0;
        else if (counter == humanWin)   return -1.0;

        counter = board[0][2] + board[1][2] + board[2][2];
        if (counter == aiWin)           return 1.0;
        else if (counter == humanWin)   return -1.0;

        counter = board[0][0] + board[1][1] + board[2][2];
        if (counter == aiWin)           return 1.0;
        else if (counter == humanWin)   return -1.0;

        counter = board[2][0] + board[1][1] + board[0][2];
        if (counter == aiWin)           return 1.0;
        else if (counter == humanWin)   return -1.0;

        System.err.println("getUtility(): called on a non-terminal board");
        System.exit(-1);
        return 0.0;
    }

    public boolean isTerminal (int[][] board) {
        int counter = 0, aiPly = 0, aiWin = CONVERT_UNIT*3, humanPly = 0, humanWin = CONVERT_UNIT*-3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] > 0)        aiPly++;
                else if (board[i][j] < 0)   humanPly++;
            }
        }
        if ((aiPly + humanPly) == 9)   return true;

        counter = board[0][0] + board[0][1] + board[0][2];
        if (counter == aiWin || counter == humanWin)  return true;

        counter = board[1][0] + board[1][1] + board[1][2];
        if (counter == aiWin || counter == humanWin)  return true;

        counter = board[2][0] + board[2][1] + board[2][2];
        if (counter == aiWin || counter == humanWin)  return true;

        counter = board[0][0] + board[1][0] + board[2][0];
        if (counter == aiWin || counter == humanWin)  return true;

        counter = board[0][1] + board[1][1] + board[2][1];
        if (counter == aiWin || counter == humanWin)  return true;

        counter = board[0][2] + board[1][2] + board[2][2];
        if (counter == aiWin || counter == humanWin)  return true;

        counter = board[0][0] + board[1][1] + board[2][2];
        if (counter == aiWin || counter == humanWin)  return true;

        counter = board[2][0] + board[1][1] + board[0][2];
        if (counter == aiWin || counter == humanWin)  return true;

        return false;
    }

}
