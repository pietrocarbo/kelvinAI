package decks.forty;

public class Card {

    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(){
        rank = null;
        suit = null;
    }

    public Card(Card oldCard){
        rank = oldCard.getRank();
        suit = oldCard.getSuit();
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " di " + suit;
    }
}
