package games.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private int row;
    private int column;
    private char seed;

    public Action(int row, int column, char playerSeed) {
        this.row = row;
        this.column = column;
        this.seed = playerSeed;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public char getSeed() {
        return seed;
    }

    public static List<Action> getActions(char[][] board, char seed) {
        ArrayList<Action> movesAvailible = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') movesAvailible.add(new Action(i, j, seed));
            }
        }

        return movesAvailible;
    }


}
