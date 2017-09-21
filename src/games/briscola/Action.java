package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;

import java.util.ArrayList;
import java.util.List;

public class Action {

    /*
    ** E' l'insieme di tutte le combinazioni possibili di carte per completare un turno
     *  (contiene due carte e l'ID del primo di mano)
     */

    private Hand cardsPlayed;
    private int firstPlayer;

    public Action(int firstPlayer) {
        this.firstPlayer = firstPlayer;
        this.cardsPlayed = new Hand(new ArrayList<>());
    }

    public Hand getCardsPlayed() {
        return cardsPlayed;
    }

    public int getFirstPlayer() {
        return firstPlayer;
    }

    public Action addOne(Card card) {
        cardsPlayed.getHand().add(card);
        return this;
    }

    public static List<Action> getActions(Hand p1Hand, Hand p2Hand, int nextToPlay) {
        List<Action> result = new ArrayList<>();

        for (int i = 0; i < p1Hand.getHand().size(); i++) {
            for (int j = 0; j < p2Hand.getHand().size(); j++) {
                Action oneAction = new Action(nextToPlay);

                if (nextToPlay == 0) {
                    oneAction.addOne(p1Hand.getHand().get(i));
                    oneAction.addOne(p2Hand.getHand().get(j));
                } else {
                    oneAction.addOne(p2Hand.getHand().get(j));
                    oneAction.addOne(p1Hand.getHand().get(i));
                }
                result.add(oneAction);
            }
        }

        return result;
    }
}
