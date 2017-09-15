package decks.forty;

public enum Suit {
    BASTONI, COPPE, DENARI, SPADE;

    public static Suit stringToSuit(String strSuit) {
        switch (strSuit) {
            case "BASTONI":
                return Suit.BASTONI;
            case "DENARI":
                return Suit.DENARI;
            case "SPADE":
                return Suit.SPADE;
            case "COPPE":
                return Suit.COPPE;
            default:
                System.err.println("ERRORE: ricevuto suit sconosciuto");
                System.exit(-1);
                return Suit.COPPE;
        }
    }
}
