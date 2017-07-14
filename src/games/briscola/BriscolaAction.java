package games.briscola;

import decks.forty.Card;

public class BriscolaAction {

    private int idxCardToPlay;
    private Card cardToPlay;

    public BriscolaAction(int idxCardToPlay, Card cardToPlay) {
        this.idxCardToPlay = idxCardToPlay;
        this.cardToPlay = cardToPlay;
    }

    public BriscolaAction(int idxCardToPlay) {
        this.idxCardToPlay = idxCardToPlay;
    }
}
