package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;
import decks.forty.Rank;
import decks.forty.Suit;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Player {

    String name;
    Hand cards;
    Hand cardsCollected;
    int ID;

    public Player(String name, Hand initialHand, int ID) {
    }

    public Player(){}

    public Card play(){
        return null;
    }

    public Card play(Hand tavolo, Suit briscola){
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

    public AI(String name, Hand initialHand, int ID){
        this.name = name;
        this.ID = ID;
        cards = initialHand;
        cardsCollected = new Hand(new ArrayList<Card>());
    }

    @Override
    public Card play(){
        return firstCardPly();
    }

    @Override
    public Card play(Hand tavolo, Suit briscola){
        if(tavolo.getHand().size() == 0 || tavolo.getHand().get(0).getRank().getBriscolaValue() < 8){
            //Se sono il primo a giocare (tavolo vuoto) gioco la carta che ha il minor valore nella mia mano
            return cards.getHand().get(Util.getIDOfLessValueCard(cards, briscola));

        }else{
            //Se l'avversario ha già giocato gioco la prima carta più forte della carta giocata dall'avversario
            return  cards.getHand().get(Util.getIDOfFirstBestCard(cards, briscola, tavolo.getHand().get(0)));
        }
    }

    @Override
    public Card play(Hand tavolo, int turns) {
        return firstCardPly();
    }

    private Card firstCardPly() {
        return cards.getHand().get(0);
    }

    private Card randomCardPly() {
        Random random = new Random();
        return null;
    }

    private Card minmaxPly() {
        // TODO (using 'game' object)
        return null;
    }
}

class Human extends Player {

    public Human(String name, Hand initialHand, int ID){
        this.name = name;
        this.ID = ID;
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
