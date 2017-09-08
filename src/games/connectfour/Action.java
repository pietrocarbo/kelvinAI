package games.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Action {

    private int column;
    private int row;
    private char seed;

    public Action(int row, int column, char seed) {
        this.column = column;
        this.row = row;
        this.seed = seed;
    }

    public Action(int column, char seed) {
        this.column = column;
        this.seed = seed;
    }

    public Action(char seed) {
        this.seed = seed;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public char getSeed() {
        return seed;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setSeed(char seed) {
        this.seed = seed;
    }

    public static List<Action> getActions(char[][] board, char startingPlayer, int order) {
        List<Action> result = new ArrayList<Action>();
        char nextPlayer = Util.getNextPlayer(board, startingPlayer);

        switch (order) {
            case 0:         // left to right
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if (board[i][j] == '_') {
                            result.add(new Action(i, j, nextPlayer));
                            break;
                        }
                    }
                }
                break;
            case 1:         // random ordering
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if (board[i][j] == '_') {
                            result.add(new Action(i, j, nextPlayer));
                            break;
                        }
                    }
                }
                Collections.shuffle(result);
                break;
            case 2:        // first middle columns
                int[] middleColumnsFirst = new int[]{3, 2, 4, 1, 5, 0, 6};
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if (board[i][middleColumnsFirst[j]] == '_') {
                            result.add(new Action(i, middleColumnsFirst[j], nextPlayer));
                            break;
                        }
                    }
                }
                break;
        }
        return result;
    }
}
