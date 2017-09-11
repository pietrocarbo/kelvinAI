package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

abstract class Player {

    int ID;
    String name;
    Hand cards;
    Hand cardsCollected;
    Hand briscole = new Hand(new ArrayList<Card>());

    public Player (int ID, String name, Hand initialHand) {
        this.ID = ID;
        this.name = name;
        this.cards = initialHand;
        this.cardsCollected = new Hand(new ArrayList<Card>());
    }

    public Player () {}

    abstract public Card play();

    abstract public Card play (Hand tavolo, int turns);

    abstract public Card play (Hand tavolo, Card briscola, Hand unKnownCards, int p2Point ,int turn, int oppositeNoOfCard, int nextPlayer);

    abstract public void setMinMaxParameter (int depth, boolean pruning, int mazzi);

    public String getName() {
        return name;
    }

    public Hand getCards() {
        return cards;
    }

    public Hand getBriscole() { return briscole;}

    public Hand getCardsCollected() { return cardsCollected; }

    public void addBriscola (Card briscole) { this.briscole.addOne(briscole);}

    public void collectCards (Hand newCardsToCollect){
        cardsCollected.addAll(newCardsToCollect.getHand());
    }

    public void addNewCardToHand (Card newCard){
        cards.addOne(newCard);
    }

    public void removeCardFromHand(Card toRemove){
        cards.removeOne(toRemove);
    }
}

class AI extends Player {

    int depth;
    boolean pruning;
    int numberOfRandomDeals;

    public AI(String name, Hand initialHand, int ID) {
        super(ID, name, initialHand);
    }

    @Override
    public void setMinMaxParameter(int depth, boolean pruning, int mazzi){
        this.depth = depth;
        this.pruning = pruning;
        this.numberOfRandomDeals = mazzi;
    }

    @Override
    public Card play(){
        return cards.getHand().get(0);
    }

    @Override
    public Card play(Hand tavolo, int turns) {
        return null;
    }

    @Override
    public Card play (Hand tavolo, Card briscola, Hand unKnownCards, int p2Point, int turn, int oppositeNoOfCard, int nextPlayer) {



        if (tavolo.getHand().size() == 0) {
            //Se sono il primo a giocare (tavolo vuoto) gioco la carta che ha il minor valore nella mia mano
            return cards.getHand().get(Util.getIndexOfLessValueCard(cards, briscola.getSuit()));

        } else {
            //Se l'avversario ha già giocato, gioco con MonteCarlo
            System.out.print(name + ": ");
            return Util.monteCarloMethod(ID , depth, pruning, numberOfRandomDeals,
                    new Hand(new ArrayList<Card>(cards.getHand())), briscola, tavolo,
                    unKnownCards, Util.calculatePoints(cardsCollected), p2Point, oppositeNoOfCard, nextPlayer);
        }

/*
        if (tavolo.getHand().size() == 0 || tavolo.getHand().get(0).getRank().getBriscolaValue() < 8) {
            //Se sono il primo a giocare (tavolo vuoto) gioco la carta che ha il minor valore nella mia mano
            return cards.getHand().get(Util.getIndexOfLessValueCard(cards, briscola.getSuit()));

        } else {
            //Se l'avversario ha già giocato gioco la prima carta più forte della carta giocata dall'avversario
            return  cards.getHand().get(Util.getIndexOfLessValueWinningCardOtherwiseLessValue(cards, briscola.getSuit(), tavolo.getHand().get(0)));
        }
        */
    }

}

class Human extends Player {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public Human(String name, Hand initialHand){
        this.name = name;
        this.cards = initialHand;
        this.cardsCollected = new Hand(new ArrayList<Card>());
    }

    @Override
    public Card play() {
        Scanner sc = new Scanner(System.in);
        Card cardSelected;

        while (true) {
            int inputIDX = sc.nextInt();

            if (inputIDX < 1 || inputIDX > cards.getHand().size()) {
                LOGGER.severe("Entered out of range card number, please try again");
                continue;
            } else {
                cardSelected = cards.getHand().get(inputIDX - 1);
                break;
            }
        }

        return cardSelected;
    }

    @Override
    public Card play(Hand tavolo, int turns) {
        return null;
    }

    @Override
    public Card play(Hand tavolo, Card briscola, Hand unKnownCards, int p2Point, int turn, int oppositeNoOfCard, int nextPlayer) {
        return null;
    }

    @Override
    public void setMinMaxParameter(int depth, boolean pruning, int mazzi) {

    }
}
