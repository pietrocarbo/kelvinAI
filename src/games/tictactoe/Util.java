package games.tictactoe;

public class Util {
    static int nodes = 0;
    static char seedP1, seedP2;

    public static void setSeeds(char seedP1, char seedP2) {
        Util.seedP1 = seedP1;
        Util.seedP2 = seedP2;
    }

    public static char[][] getResult(char[][] board, Action move) {
        char[][] newBoard = new char[3][3];

        if (board[move.getRow()][move.getColumn()] != ' ') {
            System.err.println("getResult(): it was generated a move for a non-empty square");
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

    public static int isEnded(char[][] grid) {
        int result = -2;

        //Controllo per righe
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] == grid[i][1] && grid[i][0] == grid[i][2] && grid[i][0] != ' ') {
                if (grid[i][0] == seedP1) {
                    return 0;
                } else {
                    return 1;
                }
            }
            if (grid[0][i] == grid[1][i] && grid[0][i] == grid[2][i] && grid[0][i] != ' ') {
                if (grid[0][i] == seedP1) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

        if (((grid[1][1] == grid[0][0] && grid[1][1] == grid[2][2]) || (grid[1][1] == grid[0][2] && grid[1][1] == grid[2][0])) && grid[1][1] != ' ') {
            if (grid[1][1] == seedP1) {
                return 0;
            } else {
                return 1;
            }
        }

        result = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == ' ') {
                    result = -2;
                }
            }
        }

        return result;
    }

    public static char getNextPlayer(char[][] board, char starter, char seedP1, char seedP2) {
        int seedP1counter = 0, seedP2counter = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == seedP1) seedP1counter++;
                else if (board[i][j] == seedP2) seedP2counter++;
            }
        }

        if (seedP1counter < seedP2counter) return seedP1;
        else if (seedP1counter == seedP2counter) return starter;
        else {
            return seedP2;
//            System.err.println("Error in turn calculation");
//            System.exit(0);
//            return 0;
        }
    }

    public static Action minMaxAlg(char[][] grid, char mySeed, char oppositeSeed, char starter) {
        Action bestMove = null;

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity

        char nextPlayer = Util.getNextPlayer(grid, starter, mySeed, oppositeSeed);

        for (Action action : Action.getActions(grid, nextPlayer)) {  // list of Action for empty squares filling moves

            double value = minMaxFunc(getResult(grid, action), nextPlayer == seedP1 ? seedP2 : seedP1, false); // recursion called with new board

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }
        }
        System.out.println("[debug-ai] ply " + bestMove.getRow() + "," + bestMove.getColumn() + " value " + resultValue);
        return new Action(bestMove.getRow(), bestMove.getColumn(), mySeed);
    }

    public static double minMaxFunc(char[][] board, char player, boolean maximize) {  // returns an utility value
        nodes++;

        if (Util.isEnded(board) != -2) {
            return Util.getUtility(convertBoardFormat(board));
        }

        double value;

        if (maximize) {
            value = Double.NEGATIVE_INFINITY;
            for (Action action : Action.getActions(board, player)) {
                value = Math.max(value, minMaxFunc(getResult(board, action), player == seedP1 ? seedP2 : seedP1, false));
            }
        } else {
            value = Double.POSITIVE_INFINITY;
            for (Action action : Action.getActions(board, player)) {
                value = Math.min(value, minMaxFunc(getResult(board, action), player == seedP1 ? seedP2 : seedP1, true));
            }
        }

        return value;
    }

    public static int getTurn(int[][] board) {
        int tot = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != 0) {
                    tot++;
                }
            }
        }

        return Math.abs(tot / 2);
    }

    public static double getUtility(int[][] board) {

        int turns = 9 - Util.getTurn(board);

        int counter = 0, aiPly = 0, aiWin = 3, humanPly = 0, humanWin = -3;
        double draw = 0.0, win = 10.0 * turns, loss = -10.0 * turns;


        counter = board[0][0] + board[0][1] + board[0][2];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        counter = board[1][0] + board[1][1] + board[1][2];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        counter = board[2][0] + board[2][1] + board[2][2];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        counter = board[0][0] + board[1][0] + board[2][0];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        counter = board[0][1] + board[1][1] + board[2][1];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        counter = board[0][2] + board[1][2] + board[2][2];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        counter = board[0][0] + board[1][1] + board[2][2];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        counter = board[2][0] + board[1][1] + board[0][2];
        if (counter == aiWin) return win;
        else if (counter == humanWin) return loss;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] > 0) aiPly++;
                else if (board[i][j] < 0) humanPly++;
            }
        }
        if ((aiPly + humanPly) == 9) return draw;

        System.err.println("getUtility(): called on a non-terminal board");
        System.exit(-1);
        return 0.0;
    }

    private static int[][] convertBoardFormat(char[][] stringBoard) {
        int[][] intBoard = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (stringBoard[i][j]) {
                    case ' ':   // empty square
                        intBoard[i][j] = 0;
                        break;
                    case 'O':  // AI marker
                        intBoard[i][j] = 1;
                        break;
                    case 'X':  // oppenent marker
                        intBoard[i][j] = -1;
                        break;
                }
            }
        }
        return intBoard;
    }

    private static void StampBoard(char[][] board) {
        System.out.println("--- BOARD ---");
        for (int i = 0; i < board.length; i++) {
            for (int y = 0; y < board.length; y++) {
                System.out.print("|" + board[i][y]);
            }
            System.out.println("|");
        }
    }
}
