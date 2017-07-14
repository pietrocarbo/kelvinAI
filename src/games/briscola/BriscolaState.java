package games.briscola;

import decks.forty.Card;
import decks.forty.Deck;
import decks.forty.Hand;

public class BriscolaState {

    private Hand board;
    private Card briscola;
    private Deck deck;
    private BriscolaPlayer p1;
    private BriscolaPlayer p2;

    public boolean gameOver () {
        return deck.getCardsLeft() == 0 && p1.getCards().getHand().size() == 0 && p2.getCards().getHand().size() == 0;
    }

}
