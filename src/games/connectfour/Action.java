package games.connectfour;

public class Action {

    private int column;
    private int row;
    private String seed;

    public Action(int column, int row, String seed) {
        this.column = column;
        this.row = row;
        this.seed = seed;
    }

    public Action(int column, String seed) {
        this.column = column;
        this.seed = seed;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public String getSeed() {
        return seed;
    }
}
