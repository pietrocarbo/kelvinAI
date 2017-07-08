package decks.italian;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private Integer nOfCards = 40;
    private List<Card> deck = new ArrayList<Card>();

    public Deck () {
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                deck.add(new Card(rank, suit));
    }

    public List<Card> shuffle () {
        Collections.shuffle(deck);
        return deck;
    }

    @Override
    public String toString () {
        StringBuilder strBuilder = new StringBuilder();
        for (Card card : deck ) {
            strBuilder.append(card.toString() + "\n");
        }
        return strBuilder.toString();
    }
}