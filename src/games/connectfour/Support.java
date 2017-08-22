package games.connectfour;

public class Support {

    private boolean isSupported;
    private int totalSquareMissing;

    public Support(boolean isSupported, int totalSquareMissing) {
        this.isSupported = isSupported;
        this.totalSquareMissing = totalSquareMissing;
    }

    public boolean isSupported() {
        return isSupported;
    }

    public int getTotalSquareMissing() {
        return totalSquareMissing;
    }
}
