package games.briscola;

import decks.forty.*;
import main.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game {

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private int turns;
    private Deck mazzo;
    private Hand tavolo;
    private Card briscola;
    private Integer nextPlayer;
    private List<Player> players = new ArrayList<Player>();

    public Game(int starter, GameType mod, List<Integer> searchParameters) {

        turns = 0;
        mazzo = new Deck();
        nextPlayer = starter;
        briscola = mazzo.shuffle().peekLast();
        tavolo = new Hand(new ArrayList<>());

        switch (mod) {
            default:
            case HUMAN__VS__HUMAN:
                players.add(new Human(0, "Mirco", new Hand(mazzo.deal(3))));
                players.add(new Human(1, "Pietro", new Hand(mazzo.deal(3))));
                break;

            case HUMAN__VS__AI_RULE:
                players.add(new AI(0, "Kelvin", new Hand(mazzo.deal(3)), -1, false, -1));
                players.add(new Human(1, "Pietro", new Hand(mazzo.deal(3))));
                break;

            case HUMAN__VS__AI_MINMAX:
                players.add(new AI(0, "Kelvin", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.add(new Human(1, "Mirco", new Hand(mazzo.deal(3))));
                break;

            case AI_MINMAX__VS__AI_RULE:
                players.add(new AI(0, "Kelvin-MM", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.add(new AI(1, "Kelvin-RULE", new Hand(mazzo.deal(3)), -1, false, -1));
                break;

            case AI_MINMAX__VS__AI_RANDOM:
                players.add(new AI(0, "Kelvin-MM", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.add(new AI(1, "Kelvin-RANDOM", new Hand(mazzo.deal(3)), -1, true, -1));
                break;

            case AI_HYBRID__VS__AI_MINMAX:
                players.add(new AI(0, "Kelvin-Hybrid", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.get(0).setHybridStrategy(true);
                players.add(new AI(1, "Kelvin-MM", new Hand(mazzo.deal(3)), searchParameters.get(2), false, searchParameters.get(3)));
                break;

            case AI_HYBRID__VS__AI_RULE:
                players.add(new AI(0, "Kelvin-Hybrid", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.get(0).setHybridStrategy(true);
                players.add(new AI(1, "Kelvin-RULE", new Hand(mazzo.deal(3)), -1, false, -1));
                break;

            case AI_HYBRID__VS__AI_RANDOM:
                players.add(new AI(0, "Kelvin-Hybrid", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.get(0).setHybridStrategy(true);
                players.add(new AI(1, "Kelvin-RANDOM", new Hand(mazzo.deal(3)), -1, true, -1));
                break;

            case AI_HYBRID__VS__AI_HYBRID:
                players.add(new AI(0, "Kelvin-Hybrid (depth maggiore)", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.get(0).setHybridStrategy(true);
                players.add(new AI(1, "Kelvin-Hybrid (depth minore)", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.get(0).setHybridStrategy(true);
                break;

            case HUMAN__VS__AI_HYBRID:
                players.add(new AI(0, "Kelvin-Hybrid", new Hand(mazzo.deal(3)), searchParameters.get(0), false, searchParameters.get(1)));
                players.get(0).setHybridStrategy(true);
                players.add(new Human(1, "Mirco", new Hand(mazzo.deal(3))));
                break;
        }

    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getNextPlayer() {
        if (turns < 20) return nextPlayer;
        else return -1;
    }

    public void doNextTurn() {

        Card nextCard;

        LOGGER.fine("Next player is " + nextPlayer + " - " + players.get(nextPlayer).getName() + ": ");

        if (players.get(nextPlayer) instanceof AI) {

            Hand unseenCards = new Hand(new ArrayList<>(mazzo.getCards()));
            unseenCards.addAll(players.get(Util.toggle(nextPlayer)).getCards().getHand());
            unseenCards.removeOne(briscola);

            nextCard = players.get(nextPlayer).play(
                    tavolo,
                    briscola,
                    unseenCards,
                    turns,
                    players.get(Util.toggle(nextPlayer)).getCardsCollected(),
                    players.get(Util.toggle(nextPlayer)).getCards().getHand().size());

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
            LOGGER.info("Collecting from " + tavolo.getHand().size() + " elements on the board " + tavolo.getHand());

            int handWinner = Util.getHandWinner(tavolo, briscola.getSuit());

            if (handWinner == 0) {  // first hand player win
                nextPlayer = Util.toggle(nextPlayer);
            }

            LOGGER.info("Player " + nextPlayer + " - " + players.get(nextPlayer).getName() + " won this turn");

            players.get(nextPlayer).collectCards(tavolo);

            tavolo.getHand().clear();

            if ((20 - turns) > 3) {  // i.e. turns >= 16
                players.get(nextPlayer).addNewCardToHand(mazzo.deal(1).get(0));
                players.get(Util.toggle(nextPlayer)).addNewCardToHand(mazzo.deal(1).get(0));
            }
            turns++;

        } else {
            nextPlayer = Util.toggle(nextPlayer);
        }
    }

    @Override
    public String toString() {
        return players.get(0).getName() + " (" + Util.calculatePoints(players.get(0).getCardsCollected()) + " pts)" + players.get(0).getCards() + "\n" +
                "\n" +
                "board " + tavolo.getHand() + "   " +
                "(turns left " + (20 - turns) + " , nextPlayerIndex " + nextPlayer + ", briscola " + briscola + ")\t\t\t" +
                "\n\n" +
                players.get(1).getName() + " (" + Util.calculatePoints(players.get(1).getCardsCollected()) + " pts) " + players.get(1).getCards() +
                "\n-------------------------------------------------------------------------------------------------------------------------------------------------\n";
    }
}
