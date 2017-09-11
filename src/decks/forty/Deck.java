package decks.forty;


import java.util.*;

public class Deck {

    private Integer cardsLeft;
    private List<Card> cards = new ArrayList<Card>();

    public Deck () {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        cardsLeft = cards.size();
    }

    public Deck (Hand oldDeck) {
        for (int i = 0; i < oldDeck.getHand().size(); i++) {
            cards.add(oldDeck.getHand().get(i));
        }
        cardsLeft = cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }

    public Integer getCardsLeft() {
        return cardsLeft;
    }

    public Deck shuffle () {
        Collections.shuffle(cards);
        return this;
    }

    public Card peekLast () {
        return new Card(cards.get(cards.size()-1).getRank(), cards.get(cards.size()-1).getSuit());
    }

    public List<Card> deal (Integer nOfCards)  {
        if (cards.size() < nOfCards) {
            System.err.println("Request to deal more cards then left in the cards");
            System.exit(-1);
            return cards;
        }
        else {
            List<Card> headSublist = cards.subList(0, nOfCards);
            List<Card> ret = new ArrayList<Card>(headSublist);
            headSublist.clear();
            cardsLeft = cards.size();
            return ret;
        }
    }

    @Override
    public String toString () {
        StringBuilder strBuilder = new StringBuilder();
        int i = 1;
        for (Card card : cards) {
            strBuilder.append(i++ + ". " + card + "\n");
        }
        return strBuilder.toString();
    }
}
