package games.briscola;

import decks.forty.*;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Card briscola;
    private Deck mazzo;
    private int turns;
    private List<Player> players = new ArrayList<Player>();
    private Integer toPlay;
    private Hand tavolo;

    public Game(int starter, int mod) {

        mazzo = new Deck();
        briscola = mazzo.shuffle().peekLast();
        turns = 0;
        toPlay = starter;

        switch (mod){
            case 1:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3)), 1));
                players.add(new Human("Pietro", new Hand(mazzo.deal(3)), 2));
                break;
            case 2:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3)), 1));
                players.add(new AI("John", new Hand(mazzo.deal(3)), 2));
                break;
            case 3:
                players.add(new AI("Smith", new Hand(mazzo.deal(3)), 1));
                players.add(new AI("John", new Hand(mazzo.deal(3)), 2));
                break;
            default:
                players.add(new Human("Mirco", new Hand(mazzo.deal(3)), 1));
                players.add(new Human("Pietro", new Hand(mazzo.deal(3)), 2));
                break;
        }

        tavolo = new Hand(new ArrayList<Card>());
    }

    public int getNextPlayer(){
        if(turns < 20)
            return toPlay;
        else
            return 0;
    }

    public void doNextTurn() {

        Card nextCard;

        System.out.println("Next to play is Player " + toPlay + " - " + players.get(toPlay - 1).getName() + ": ");

        if(players.get(toPlay - 1) instanceof AI){
            nextCard = players.get(toPlay - 1).play(tavolo, briscola.getSuit());
        }else{
            nextCard = players.get(toPlay - 1).play();
        }

        players.get(toPlay - 1).removeCard(nextCard);

        tavolo.addOne(nextCard);

        collectAndDeal();
    }

    public void collectAndDeal() {

        if (tavolo.getHand().size() == 2) {
            System.out.println("***\nCollecting from the (" + tavolo.getHand().size() + " elements) the board " + tavolo.getHand());

            toPlay = Util.getHandWinner(tavolo, briscola.getSuit());

            System.out.println("Player " + toPlay + " won this ply\n***\n");

            players.get(toPlay - 1).collectCards(tavolo);

            tavolo.getHand().clear();

            if((20 - turns) > 3){
                players.get(toPlay - 1).newCardFromDeck(mazzo.deal(1).get(0));
                if(toPlay == 1){
                    players.get(1).newCardFromDeck(mazzo.deal(1).get(0));
                }else{
                    players.get(0).newCardFromDeck(mazzo.deal(1).get(0));
                }
            }

            turns++;
        }else{
            toPlay = toPlay == 1 ? 2 : 1;
        }
    }

    public List<Player> getPlayers(){ return players;}

    @Override
    public String toString() {
        return players.get(0).getName() + " (" + Util.calculatePoints(players.get(0).getCardsCollected()) + " pts)\nHand: " + players.get(0).getCards() + "\n" +
                "\n" +
                "(plyes left " + (20 - turns) + " , toPlay " + toPlay + ", briscola " + briscola + ")\t\t\t" +
                "board " + tavolo.getHand() + "\n" +
                "\n" +
                players.get(1).getName() + " (" + Util.calculatePoints(players.get(1).getCardsCollected()) + " pts)\nHand: " + players.get(1).getCards() +
                "\n-------------------------------------------------------------------------------------------------------------------------------------------------\n";
    }
}
