package games.briscola;

import decks.forty.Card;

public class Action {

    private int player;
    private Card cardToPlay;

    public Action(int player, Card cardToPlay) {
        this.player = player;
        this.cardToPlay = cardToPlay;
    }

    public Action(int idxCardToPlay) {
        this.player = idxCardToPlay;
    }
}
