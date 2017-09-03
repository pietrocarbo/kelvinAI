package games.briscola;

import decks.forty.*;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Card briscola;
    private Deck mazzo;
    private int turns;
    private List<Player> players = new ArrayList<Player>();
    private Integer nextPlayer;
    private Hand tavolo;

    public Game(int starter, int mod) {

        mazzo = new Deck();
        briscola = mazzo.shuffle().peekLast();
        turns = 0;
        nextPlayer = starter;

        switch (mod){
            case 1:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3)), 0));
                players.add(new Human("Pietro", new Hand(mazzo.deal(3)), 1));
                break;
            case 2:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3)), 0));
                players.add(new AI("John", new Hand(mazzo.deal(3)), 1));
                break;
            case 3:
                players.add(new AI("Smith", new Hand(mazzo.deal(3)), 0));
                players.add(new AI("John", new Hand(mazzo.deal(3)), 1));
                break;
            default:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3)), 0));
                players.add(new Human("Pietro", new Hand(mazzo.deal(3)), 1));
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

        System.out.println("Next to play is Player " + nextPlayer + " - " + players.get(nextPlayer).getName() + ": ");

        if(players.get(nextPlayer) instanceof AI){
            nextCard = players.get(nextPlayer).play(tavolo, briscola.getSuit());
        }else{
            nextCard = players.get(nextPlayer).play();
        }

        players.get(nextPlayer).removeCard(nextCard);

        tavolo.addOne(nextCard);

        collectAndDeal();
    }

    public void collectAndDeal() {

        if (tavolo.getHand().size() == 2) {
            System.out.println("***\nCollecting from the (" + tavolo.getHand().size() + " elements) the board " + tavolo.getHand());

            int handWinner = Util.getHandWinner(tavolo, briscola.getSuit());

            //nextPlayer = Util.getHandWinner(tavolo, briscola.getSuit());

            if(handWinner == 0){
                nextPlayer = nextPlayer == 0 ? 1 : 0;
            }

            System.out.println("Player " + nextPlayer + " won this ply\n***\n");

            players.get(nextPlayer).collectCards(tavolo);

            tavolo.getHand().clear();

            if((20 - turns) > 3){
                players.get(nextPlayer).newCardFromDeck(mazzo.deal(1).get(0));
                if(nextPlayer == 0){
                    players.get(1).newCardFromDeck(mazzo.deal(1).get(0));
                }else{
                    players.get(0).newCardFromDeck(mazzo.deal(1).get(0));
                }
            }

            turns++;
        }else{
            nextPlayer = nextPlayer == 0 ? 1 : 0;
        }
    }

    public List<Player> getPlayers(){ return players;}

    @Override
    public String toString() {
        return players.get(0).getName() + " (" + Util.calculatePoints(players.get(0).getCardsCollected()) + " pts)" + players.get(0).getCards() + "\n" +
                "\n" +
                "board " + tavolo.getHand() + "   " +
                "(plyes left " + (20 - turns) + " , nextPlayer " + nextPlayer + ", briscola " + briscola + ")\t\t\t" +
                "\n\n" +
                players.get(1).getName() + " (" + Util.calculatePoints(players.get(1).getCardsCollected()) + " pts)" + players.get(1).getCards() +
                "\n-------------------------------------------------------------------------------------------------------------------------------------------------\n";
    }
}
