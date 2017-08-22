package games.connectfour;

public class Row {

    private boolean givesPoint;
    private char seed;
    private boolean[] occurrences;
    private int totalOccurrences;

    public Row(boolean givesPoint) {
        this.givesPoint = givesPoint;
    }

    public Row(boolean givesPoint, char seed, boolean[] occurences, int totalOccurences) {
        this.givesPoint = givesPoint;
        this.seed = seed;
        this.occurrences = occurences;
        this.totalOccurrences = totalOccurences;
    }

    public char getSeed() {
        return seed;
    }

    public boolean[] getOccurrences() {
        return occurrences;
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public boolean getGivesPoint() {
        return givesPoint;
    }
}
