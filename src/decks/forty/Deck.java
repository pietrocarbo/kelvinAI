package decks.forty;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Deck {

    private Integer cardsLeft;
    private List<Card> deck = new ArrayList<>();

    public Deck () {
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                deck.add(new Card(rank, suit));
        cardsLeft = deck.size();
    }

    public List<Card> getDeck() {
        return deck;
    }

    public Integer getCardsLeft() {
        return cardsLeft;
    }

    public Deck shuffle () {
        Collections.shuffle(deck);
        return this;
    }

    public Card peekLast () {
        return new Card(deck.get(deck.size()-1).getRank(), deck.get(deck.size()-1).getSuit());
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
