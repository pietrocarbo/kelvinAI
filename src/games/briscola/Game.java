package games.briscola;

import decks.forty.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private Card briscola;
    private Deck mazzo;
    private int turns;
    private List<Player> players = new ArrayList<Player>();
    private Integer nextPlayer;
    private Hand tavolo;

    public Game(int starter, int gameType) {

        mazzo = new Deck();
        briscola = mazzo.shuffle().peekLast();
        turns = 0;
        nextPlayer = starter;

        switch (gameType){
            default:
            case 1:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3))));
                players.add(new Human("Pietro", new Hand(mazzo.deal(3))));
                break;
            case 2:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3))));
                players.add(new AI("Kelvin", new Hand(mazzo.deal(3)), 1));
                players.get(1).setMinMaxParameter( 8, true , 50);

                break;
            case 3:
                players.add(new AI("Smith", new Hand(mazzo.deal(3)), 0));
                players.get(0).setMinMaxParameter( 3, true , 50);

                players.add(new AI("Kelvin", new Hand(mazzo.deal(3)), 1));
                players.get(1).setMinMaxParameter( 3, true , 50);
                break;
        }

        tavolo = new Hand(new ArrayList<Card>());
    }

    public int getNextPlayer(){
        if(turns < 20)
            return nextPlayer;
        else
            return -1;
    }

    public void doNextTurn() {

        Card nextCard;

        LOGGER.fine("Next player is " + nextPlayer + " - " + players.get(nextPlayer).getName() + ": ");

        if (players.get(nextPlayer) instanceof AI) {

            Hand unseenCards = new Hand(new ArrayList<Card>(mazzo.getCards()));
            unseenCards.addAll(players.get(nextPlayer == 0 ? 1 : 0).getCards().getHand());
            unseenCards.removeOne(briscola);
            nextCard = players.get(nextPlayer).play(
                    new Hand(new ArrayList<Card>(tavolo.getHand())),
                    briscola,
                    unseenCards,
                    Util.calculatePoints(players.get(nextPlayer == 0 ? 1 : 0).getCardsCollected()),
                    turns,
                    players.get(nextPlayer == 0 ? 1 : 0).getCards().getHand().size(),
                    nextPlayer);
        } else {
            nextCard = players.get(nextPlayer).play();
        }

        if (nextCard.getSuit() == briscola.getSuit()) {
            players.get(nextPlayer).addBriscola(nextCard);
        }

        players.get(nextPlayer).removeCardFromHand(nextCard);

        tavolo.addOne(nextCard);

        collectAndDeal();
    }

    public void collectAndDeal() {

        if (tavolo.getHand().size() == 2) {
            LOGGER.finest("Collecting from " + tavolo.getHand().size() + " elements on the board " + tavolo.getHand());

            int handWinner = Util.getHandWinner(tavolo, briscola.getSuit());

            if (handWinner == 0) {
                nextPlayer = nextPlayer == 0 ? 1 : 0;
            }

            LOGGER.finest("Player " + nextPlayer + " won this turn");

            players.get(nextPlayer).collectCards(tavolo);

            tavolo.getHand().clear();

            if ((20 - turns) > 3) {
                players.get(nextPlayer).addNewCardToHand(mazzo.deal(1).get(0));
                players.get(1 - nextPlayer).addNewCardToHand(mazzo.deal(1).get(0));
            }
            turns++;

        } else {
            nextPlayer = nextPlayer == 0 ? 1 : 0;
        }
    }

    public List<Player> getPlayers() { return players; }

    @Override
    public String toString() {
        return players.get(0).getName() + " (" + Util.calculatePoints(players.get(0).getCardsCollected()) + " pts)" + players.get(0).getCards() + "\n" +
                "\n" +
                "board " + tavolo.getHand() + "   " +
                "(turns left " + (20 - turns) + " , nextPlayer " + nextPlayer + ", briscola " + briscola + ")\t\t\t" +
                "\n\n" +
                players.get(1).getName() + " (" + Util.calculatePoints(players.get(1).getCardsCollected()) + " pts) " + players.get(1).getCards() +
                "\n-------------------------------------------------------------------------------------------------------------------------------------------------\n";
    }
}
