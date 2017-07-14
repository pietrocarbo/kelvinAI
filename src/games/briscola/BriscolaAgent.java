package games.briscola;

import decks.forty.Card;

import java.util.Random;

public class BriscolaAgent {

    private BriscolaGame game;

    public BriscolaAgent(BriscolaGame game) {
        this.game = game;
    }

    public Card ply() {
        return firstCardPly();
    }

    private Card firstCardPly () {
        return null;
    }

    private Card randomCardPly () {
        Random random = new Random();
        return null;
    }

    private Card minmaxPly () {
        // TODO (using 'game' object)
        return null;
    }
}

