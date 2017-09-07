package decks.forty;


import java.util.*;

public class Deck {

    private Integer cardsLeft;
    private List<Card> deck = new ArrayList<Card>();

    public Deck () {
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                deck.add(new Card(rank, suit));
        cardsLeft = deck.size();
    }

    public Deck(Hand oldDeck){
        for(int i = 0; i < oldDeck.getHand().size(); i++) {
            deck.add(oldDeck.getHand().get(i));
        }
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
        if (deck.size() < nOfCards) {
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
