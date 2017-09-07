package decks.forty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {

    private Integer cardsLeft;
    private List<Card> hand = new ArrayList<>();

    public Hand (List<Card> cards) {
        for (Card card : cards)
            hand.add(card);
        cardsLeft = hand.size();
    }

    public Hand addOne (Card toAdd) {
        hand.add(toAdd);
        return this;
    }

    public Hand shuffle(){
        Collections.shuffle(hand);

        return this;
    }

    public Hand addAll (List<Card> toAdd) {
        hand.addAll(toAdd);
        return this;
    }

    public void removeOne (Card toRemove) {
        if (!hand.remove(toRemove)) {
            try{
                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).getRank() == toRemove.getRank() && hand.get(i).getSuit() == toRemove.getSuit()) {
                        hand.remove(i);
                    }
                }
            }catch (Exception e) {
                System.err.println("Request to remove card '" + toRemove + "' not in the hand");
                System.exit(-1);
            }
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    @Override
    public String toString () {
        StringBuilder strBuilder = new StringBuilder("");
        int i = 1;
        for (Card card : hand) {
            strBuilder.append("\n" + i++ + ". " + card + ";");
        }
        int idxOfLastComma = strBuilder.lastIndexOf(",");
        return (idxOfLastComma != -1) ?
                strBuilder.replace(idxOfLastComma, idxOfLastComma+1, "]").toString()
                :
                strBuilder.append("").toString();
    }

}
