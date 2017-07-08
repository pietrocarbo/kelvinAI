package decks.fiftytwo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private Integer cardsLeft;
    private List<Card> deck = new ArrayList<>();

    public Deck () {
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                deck.add(new Card(rank, suit));
        cardsLeft = deck.size();
    }

    public Deck shuffle () {
        Collections.shuffle(deck);
        return this;
    }

    public List<Card> deal (Integer nOfCards)  {
        if (cardsLeft < nOfCards) {
            System.err.println("Request to deal more cards then left in the deck");
            System.exit(-1);
            return deck;
        }
        else {
            List<Card> headSublist = deck.subList(0, nOfCards);
            List<Card> ret = new ArrayList<Card>(headSublist);
            headSublist.clear();
            cardsLeft = deck.size();
            return ret;
        }
    }

    @Override
    public String toString () {
        StringBuilder strBuilder = new StringBuilder();
        int i = 1;
        for (Card card : deck) {
            strBuilder.append(i++ + ". " + card + "\n");
        }
        return strBuilder.toString();
    }
}
