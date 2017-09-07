package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;
import decks.forty.Rank;
import decks.forty.Suit;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Player {

    int ID;
    String name;
    Hand cards;
    Hand cardsCollected;
    Hand briscole = new Hand(new ArrayList<Card>());

    public Player(String name, Hand initialHand, int ID) {
    }

    public Player(){}

    public Card play(){
        return null;
    }

    public Card play(Hand tavolo, Card briscola, Hand unKnownCards, int p2Point ,int turn, int oppositeNoOfCard, int nextPlayer){

        return null;
    }

    public Card play(Hand tavolo, int turns){
        return null;
    }

    public String getName() {
        return name;
    }

    public Hand getCards() {
        return cards;
    }

    public void setMinMaxParameter(int depth, boolean pruning, int mazzi){}

    public void addBriscole(Card briscole){ this.briscole.addOne(briscole);}

    public Hand getBriscole(){ return briscole;}

    public void newCardFromDeck(Card newCard){
        cards.addOne(newCard);
    }

    public void collectCards(Hand newCardsToCollect){
        cardsCollected.addAll(newCardsToCollect.getHand());
    }

    public Hand getCardsCollected(){ return cardsCollected;}

    public void removeCard(Card toRemove){
        cards.removeOne(toRemove);
    }
}

class AI extends Player {

    int depth;
    boolean pruning;
    int mazzi;

    public AI(String name, Hand initialHand, int ID){
        this.ID = ID;
        this.name = name;
        cards = initialHand;
        cardsCollected = new Hand(new ArrayList<Card>());
    }

    @Override
    public void setMinMaxParameter(int depth, boolean pruning, int mazzi){
        this.depth = depth;
        this.pruning = pruning;
        this.mazzi = mazzi;
    }

    @Override
    public Card play(){
        return cards.getHand().get(0);
    }

    @Override
    public Card play(Hand tavolo, Card briscola, Hand unKnownCards, int p2Point, int turn, int oppositeNoOfCard, int nextPlayer){



        if(tavolo.getHand().size() == 0){
            //Se sono il primo a giocare (tavolo vuoto) gioco la carta che ha il minor valore nella mia mano
            return cards.getHand().get(Util.getIDOfLessValueCard(cards, briscola.getSuit()));

        }else{
            //Se l'avversario ha già giocato gioco con MonteCarlo
            System.out.print(name + ": ");
            return Util.monteCarloMethod(ID , depth, pruning, mazzi, new Hand(new ArrayList<Card>(cards.getHand())), briscola, tavolo, unKnownCards, Util.calculatePoints(cardsCollected), p2Point, oppositeNoOfCard, nextPlayer);
        }



/*
        if(tavolo.getHand().size() == 0 || tavolo.getHand().get(0).getRank().getBriscolaValue() < 8){
            //Se sono il primo a giocare (tavolo vuoto) gioco la carta che ha il minor valore nella mia mano
            return cards.getHand().get(Util.getIDOfLessValueCard(cards, briscola.getSuit()));

        }else{
            //Se l'avversario ha già giocato gioco la prima carta più forte della carta giocata dall'avversario
            return  cards.getHand().get(Util.getIDOfFirstBestCard(cards, briscola.getSuit(), tavolo.getHand().get(0)));
        }
        */
    }

    @Override
    public Card play(Hand tavolo, int turns) {
        return null;
    }

}

class Human extends Player {

    public Human(String name, Hand initialHand){
        this.name = name;
        cards = initialHand;
        cardsCollected = new Hand(new ArrayList<Card>());
    }

    @Override
    public Card play() {

        Scanner sc = new Scanner(System.in);

        Card cardSelected = new Card();

        while (true) {
            int inputIDX = sc.nextInt();

            if (inputIDX < 1 || inputIDX > cards.getHand().size()) {
                System.err.println("Entered out of range card number - please try again: ");
                continue;
            } else {
                cardSelected = cards.getHand().get(inputIDX - 1);
                break;
            }
        }

        return cardSelected;
    }
}
