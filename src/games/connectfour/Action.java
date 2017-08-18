package games.connectfour;

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

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public char getSeed() {
        return seed;
    }
}
