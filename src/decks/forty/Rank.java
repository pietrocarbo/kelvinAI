package decks.forty;

public enum Rank {
    ASSO(10),
    DUE(1),
    TRE(9),
    QUATTRO(2),
    CINQUE(3),
    SEI(4),
    SETTE(5),
    FANTE(6),
    CAVALLO(7),
    RE(8);

    private final int briscolaValue;

    Rank(int briscolaValue) {
        this.briscolaValue = briscolaValue;
    }

    public int getBriscolaValue() {
        return briscolaValue;
    }
}