package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;

import java.util.ArrayList;
import java.util.List;

public class Action {

    /*
    ** Per Action si intende l'insieme delle mosse per completare un turno, nel caso di due giocaotori una action contiene due carte e l'ID di chi sta giocando per primo
     */

    private int firstPlayer;
    private Hand cards;

    public Action(int player) {
        this.firstPlayer = player;
        cards = new Hand(new ArrayList<>());
    }

    public int getPlayer() {
        return firstPlayer;
    }

    public Action addOne(Card card){
        cards.getHand().add(card);
        return this;
    }

    public Hand getCards() {
        return cards;
    }

    public static List<Action> getAction(Hand p1Hand, Hand p2Hand, int nextToPlay){
        List<Action> actions = new ArrayList<>();

        for(int i = 0; i < p1Hand.getHand().size(); i++){
            for(int j = 0; j < p2Hand.getHand().size(); j++){
                Action oneAction = new Action(nextToPlay);
                if(nextToPlay == 0){
                    oneAction.addOne(p1Hand.getHand().get(i));
                    oneAction.addOne(p2Hand.getHand().get(j));
                }else{
                    oneAction.addOne(p2Hand.getHand().get(j));
                    oneAction.addOne(p1Hand.getHand().get(i));
                }
                actions.add(oneAction);
            }
        }

        return actions;
    }
}
