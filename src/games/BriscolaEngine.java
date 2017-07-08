package games;

import decks.forty.*;

import java.util.ArrayList;
import java.util.List;

public class BriscolaEngine implements cardGame {

    private Card briscola;
    private Deck mazzo;
    private Integer turnsLeft;

    private Integer toPlay;

    private Hand manoP1;
    private Hand manoP2;

    private Hand puntiP1;
    private Hand puntiP2;


    public BriscolaEngine () {
        mazzo = new Deck();
        briscola = mazzo.shuffle().peekLast();
        turnsLeft = 17;
        toPlay = 1;

        manoP1 = new Hand(mazzo.deal(3));
        manoP2 = new Hand(mazzo.deal(3));

        puntiP1 = new Hand(new ArrayList<Card>());
        puntiP2 = new Hand(new ArrayList<Card>());
    }

    public Integer pointScored (Hand cards) {
        Integer counter = 0;
        for (Card card : cards.getHand()) {
            switch (card.getRank()) {
                case ASSO:
                    counter += 11;
                    break;
                case TRE:
                    counter += 10;
                    break;
                case RE:
                    counter += 4;
                    break;
                case CAVALLO:
                    counter += 3;
                    break;
                case FANTE:
                    counter += 2;
                    break;
                default:
                    break;
            }
        }
        return counter;
    }

    @Override
    public Integer whoseTurn() {
        return toPlay;
    }

    @Override
    public boolean isGameOver() {
        return turnsLeft == 0;
    }
}
