package games.connectfour;

import main.MovesOrdering;

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

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public char getSeed() {
        return seed;
    }

    public static List<Action> getActions(char[][] board, char seed, MovesOrdering order) {
        List<Action> result = new ArrayList<Action>();

        switch (order) {

            case STANDARD:
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if (board[i][j] == '_') {
                            result.add(new Action(i, j, seed));
                            break;
                        }
                    }
                }
                break;

            case RANDOM:
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if (board[i][j] == '_') {
                            result.add(new Action(i, j, seed));
                            break;
                        }
                    }
                }
                Collections.shuffle(result);
                break;

            case MIDDLE_FIRST:
                int[] middleColumnsFirst = new int[]{3, 2, 4, 1, 5, 0, 6};
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if (board[i][middleColumnsFirst[j]] == '_') {
                            result.add(new Action(i, middleColumnsFirst[j], seed));
                            break;
                        }
                    }
                }
                break;
        }
        return result;
    }
}
