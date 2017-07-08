package decks.forty;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private Integer cardsLeft;
    private List<Card> hand = new ArrayList<>();

    public Hand (List<Card> cards) {
        for (Card card : cards)
            hand.add(card);
        cardsLeft = hand.size();
    }

    public List<Card> getHand() {
        return hand;
    }

    @Override
    public String toString () {
        StringBuilder strBuilder = new StringBuilder();
        int i = 1;
        for (Card card : hand) {
            strBuilder.append(i++ + ". " + card + "\n");
        }
        return strBuilder.toString();
    }
}
