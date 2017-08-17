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

    private final int SEED_UNIT = 1;   // has to be unsigned
    private final int SEED_AI = SEED_UNIT, SEED_HUMAN = - SEED_UNIT;
    private int SEED_STARTER;
    private int nodeVisited = 0;

    public Agent (int starter) {
        if (starter == 0)   SEED_STARTER = SEED_AI;
        else                SEED_STARTER = SEED_HUMAN;
    }

    // getPlayer(board) -> current player
    public int getPlayer (int[][] board) {
        int aiPly = 0, humanPly = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == SEED_AI)          aiPly++;
                else if (board[i][j] == SEED_HUMAN)   humanPly++;
            }
        }

        if     (aiPly < humanPly)   return SEED_AI;
        else if (aiPly == humanPly) return SEED_STARTER;
        else {                      return SEED_HUMAN;
//            System.err.println("Error in turn calculation");
//            System.exit(0);
//            return 0;
        }
    }


    // getActions(board) -> list of moves available
    public List<Action> getActions (int[][] board) {
        ArrayList<Action> movesAvailible = new ArrayList<>();

        int markerValue = (getPlayer(board) == SEED_AI ) ? SEED_UNIT : -SEED_UNIT;

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

    private void StampBoard(int[][] board) {
        System.out.println("--- BOARD ---");
        for(int i = 0; i < board.length;i++){
            for(int y = 0; y < board.length;y++){
                System.out.print("|" + board[i][y]);
            }
            System.out.println("|");
        }
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
        int[][] board = convertBoardFormat(stringBoard, SEED_UNIT);

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity
        int player = getPlayer(board);  // 0 is AI, 1 is Human

        for (Action action : getActions(board)) {  // list of Action for empty squares filling moves

            double value = minValue(getResult(board, action), player); // recursion called with new board

//            System.out.println("value -> " + value);

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }
        }
        System.out.println("[debug-ai] ply value " + resultValue);
        return new int[]{bestMove.getRow(), bestMove.getColumn(), nodeVisited};
    }

    public double minValue(int[][] board, int player) {  // returns an utility value
        nodeVisited++;

        if (isTerminal(board))
            return getUtility(board);

        double value = Double.POSITIVE_INFINITY;

        for (Action action : getActions(board)) {
            value = Math.min(value, maxValue(getResult(board, action), player));
        }

        return value;
    }

    public double maxValue(int[][] board, int player) { // returns an utility value
        nodeVisited++;

        if (isTerminal(board))
            return getUtility(board);

        double value = Double.NEGATIVE_INFINITY;

        for (Action action : getActions(board))
            value = Math.max(value, minValue(getResult(board, action), player));

        return value;
    }

//    public double getUtility (int[][] board) {
//        double utility = 0.0;
//
//        utility += scoreLine(board,0, 0, 0, 1, 0, 2);  // row 0
//        utility += scoreLine(board,1, 0, 1, 1, 1, 2);  // row 1
//        utility += scoreLine(board,2, 0, 2, 1, 2, 2);  // row 2
//        utility += scoreLine(board,0, 0, 1, 0, 2, 0);  // col 0
//        utility += scoreLine(board,0, 1, 1, 1, 2, 1);  // col 1
//        utility += scoreLine(board,0, 2, 1, 2, 2, 2);  // col 2
//        utility += scoreLine(board,0, 0, 1, 1, 2, 2);  // diagonal
//        utility += scoreLine(board,0, 2, 1, 1, 2, 0);  // antidiagonal
//
//        return utility;
//    }

//    public double scoreLine (int[][] board, int row1, int col1, int row2, int col2, int row3, int col3) {
//
//        int score = 0;
//
//        if (board[row1][col1] == SEED_UNIT) {
//            score = 1;
//        } else if (board[row1][col1] == -SEED_UNIT) {
//            score = -1;
//        }
//
//        if (board[row2][col2] == SEED_UNIT) {
//            if (score == 1) {
//                score = 10;
//            } else if (score == -1) { // mixed seeds line
//                return 0;
//            } else {  // cell1 was empty
//                score = 1;
//            }
//        } else if (board[row2][col2] == -SEED_UNIT) {
//            if (score == -1) {
//                score = -10;
//            } else if (score == 1) { // mixed seeds line
//                return 0;
//            } else {  // cell1 was empty
//                score = -1;
//            }
//        }
//
//        if (board[row3][col3] == SEED_UNIT) {
//            if (score > 0) {  // cell1 and/or cell2 are AI seed
//                score *= 10;
//            } else if (score < 0) {  // mixed seeds line
//                return 0;
//            } else {  // cell1 and cell2 are empty
//                score = 1;
//            }
//        } else if (board[row3][col3] == -SEED_UNIT) {
//            if (score < 0) {  // cell1 and/or cell2 are human seed
//                score *= 10;
//            } else if (score > 1) {  // mixed line seeds
//                return 0;
//            } else {  // cell1 and cell2 are empty
//                score = -1;
//            }
//        }
//
//        return score;
//    }

    public double getUtility(int[][] board) {

        int counter = 0, aiPly = 0, aiWin = SEED_AI * 3, humanPly = 0, humanWin = SEED_HUMAN * 3;
        double draw = 0.0, win = 10.0, loss = -10.0;

        counter = board[0][0] + board[0][1] + board[0][2];
        if (counter == aiWin )          return win;
        else if (counter == humanWin)   return loss;

        counter = board[1][0] + board[1][1] + board[1][2];
        if (counter == aiWin)           return win;
        else if (counter == humanWin)   return loss;

        counter = board[2][0] + board[2][1] + board[2][2];
        if (counter == aiWin)           return win;
        else if (counter == humanWin)   return loss;

        counter = board[0][0] + board[1][0] + board[2][0];
        if (counter == aiWin)           return win;
        else if (counter == humanWin)   return loss;

        counter = board[0][1] + board[1][1] + board[2][1];
        if (counter == aiWin)           return win;
        else if (counter == humanWin)   return loss;

        counter = board[0][2] + board[1][2] + board[2][2];
        if (counter == aiWin)           return win;
        else if (counter == humanWin)   return loss;

        counter = board[0][0] + board[1][1] + board[2][2];
        if (counter == aiWin)           return win;
        else if (counter == humanWin)   return loss;

        counter = board[2][0] + board[1][1] + board[0][2];
        if (counter == aiWin)           return win;
        else if (counter == humanWin)   return loss;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] > 0)        aiPly++;
                else if (board[i][j] < 0)   humanPly++;
            }
        }
        if ((aiPly + humanPly) == 9)   return draw;

        System.err.println("getUtility(): called on a non-terminal board");
        System.exit(-1);
        return 0.0;
    }


    public boolean isTerminal (int[][] board) {
        int counter = 0, aiPly = 0, aiWin = SEED_AI * 3, humanPly = 0, humanWin = SEED_HUMAN * 3;
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
