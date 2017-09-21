package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

abstract class Player {

    int ID;
    String name;
    Hand cards;
    Hand cardsCollected;
    Hand briscole = new Hand(new ArrayList<>());

    public Player (int ID, String name, Hand initialHand) {
        this.ID = ID;
        this.name = name;
        this.cards = initialHand;
        this.cardsCollected = new Hand(new ArrayList<>());
    }

    abstract public Card play();

    abstract public Card play(Hand tavolo, Card briscola, Hand unknownCards, int turn, Hand opponentCardsCollected, int opponentNOfCard);

    public void setHybridStrategy(boolean hybridStrategy) {
        return;
    }

    public String getName() {
        return name;
    }

    public Hand getCards() {
        return cards;
    }

    public Hand getBriscole() {
        return briscole;
    }

    public void addBriscola(Card briscole) {
        this.briscole.addOne(briscole);
    }

    public Hand getCardsCollected() {
        return cardsCollected;
    }

    public void collectCards(Hand newCardsToCollect) {
        cardsCollected.addAll(new ArrayList<>(newCardsToCollect.getHand()));
    }

    public void addNewCardToHand(Card newCard) {
        cards.addOne(newCard);
    }

    public void removeCardFromHand(Card toRemove){
        cards.removeOne(toRemove);
    }
}

class AI extends Player {

    int depth;
    boolean randomPlay;
    int numberOfRandomDeals;
    boolean hybridStrategy = false;


    public AI(int ID, String name, Hand initialHand, int depth, boolean randomPlay, int mazzi) {
        super(ID, name, initialHand);
        this.depth = depth;
        this.randomPlay = randomPlay;
        this.numberOfRandomDeals = mazzi;
    }

    @Override
    public void setHybridStrategy(boolean hybridStrategy) {
        this.hybridStrategy = hybridStrategy;
    }

    public Card chooseRandom() {
        return cards.getHand().get(new Random().nextInt(cards.getHand().size()));
    }

    @Override
    public Card play(Hand tavolo, Card briscola, Hand unknownCards, int turn, Hand opponentCardsCollected, int opponentNOfCard) {

        if (turn == 19) return cards.getHand().get(0);

        if (randomPlay) return chooseRandom();

        // rule-based decision
        if (depth == -1) {

            if (tavolo.getHand().size() == 0) {  // ai is first hand player
                return cards.getHand().get(Util.getIndexOfLessValueCard(cards, briscola.getSuit()));
            } else {  // ai is second hand player
                return cards.getHand().get(Util.getIndexOfLessValueWinningCardOtherwiseLessValue(cards, briscola.getSuit(), tavolo.getHand().get(0)));
            }


        } else {  // search-based decision

            if (hybridStrategy && tavolo.getHand().size() == 0) {
                return cards.getHand().get(Util.getIndexOfLessValueCard(cards, briscola.getSuit()));
            } else {
                return Util.monteCarloApproximation(
                        name,
                        depth,
                        numberOfRandomDeals,
                        cards,
                        briscola,
                        tavolo,
                        unknownCards,
                        cardsCollected,
                        opponentCardsCollected,
                        turn,
                        opponentNOfCard);
            }
        }

    }

    @Override
    public Card play() {
        return null;
    }
}

class Human extends Player {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public Human(int ID, String name, Hand initialHand) {
        super(ID, name, initialHand);
    }

    @Override
    public Card play() {
        Scanner sc = new Scanner(System.in);

        Card cardSelected;

        while (true) {
            int inputIDX = sc.nextInt();

            if (inputIDX < 1 || inputIDX > cards.getHand().size()) {
                LOGGER.severe("Wrong card number entered, please try again (enter 1,2,3)");
                continue;
            } else {
                cardSelected = cards.getHand().get(inputIDX - 1);
                break;
            }
        }

        return cardSelected;
    }

    @Override
    public Card play(Hand tavolo, Card briscola, Hand unknownCards, int turn, Hand opponentCardsCollected, int opponentNOfCard) {
        return null;
    }
}
