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

    public static Rank stringToRank(String strRank) {
        switch (strRank) {
            case "ASSO":
                return Rank.ASSO;
            case "DUE":
                return Rank.DUE;
            case "TRE":
                return Rank.TRE;
            case "QUATTRO":
                return Rank.QUATTRO;
            case "CINQUE":
                return Rank.CINQUE;
            case "SEI":
                return Rank.SEI;
            case "SETTE":
                return Rank.SETTE;
            case "FANTE":
                return Rank.FANTE;
            case "CAVALLO":
                return Rank.CAVALLO;
            case "RE":
                return Rank.RE;
            default:
                System.err.println("ERRORE: ricevuto rank sconosciuto");
                System.exit(-1);
                return Rank.RE;
        }
    }
}