package games.briscola;

import decks.forty.Card;
import decks.forty.Deck;
import decks.forty.Hand;

import java.util.ArrayList;
import java.util.List;

public class MonteCarloGame {

    private Card briscola;
    private Deck mazzo;
    private int p1Point, p2Point, gameVal;
    private Hand p1Hand, p2Hand;
    private Integer nextPlayer;
    private Hand tavolo;
    private int whoIam;
    public static long nodes = 0;
    boolean ENDED = false;

    public int getGameVal() {
        return gameVal;
    }

    public Card getBriscola() {
        return briscola;
    }

    public Deck getMazzo() {
        return mazzo;
    }

    public int getP1Point() {
        return p1Point;
    }

    public int getP2Point() {
        return p2Point;
    }

    public Hand getP1Hand() {
        return p1Hand;
    }

    public Hand getP2Hand() {
        return p2Hand;
    }

    public Integer getNextPlayer() {
        return nextPlayer;
    }

    public Hand getTavolo() {
        return tavolo;
    }

    public int getWhoIam() {
        return whoIam;
    }

    public void setWhoIam(int whoIam) {
        this.whoIam = whoIam;
    }

    public MonteCarloGame(Deck mazzo, Card briscola, Hand p1Hand, Hand p2Hand, int p1Point, int p2Point, Hand tavolo, int nextPlayer) {
        this.briscola = briscola;
        this.mazzo = mazzo;
        this.p1Point = p1Point;
        this.p2Point = p2Point;
        this.p1Hand = p1Hand;
        this.p2Hand = p2Hand;
        this.tavolo = tavolo;
        this.nextPlayer = nextPlayer;
    }

    public MonteCarloGame(MonteCarloGame oldGame){
        this.whoIam = oldGame.getWhoIam();
        this.mazzo = new Deck(new Hand(new ArrayList<Card>(oldGame.getMazzo().getCards())));
        this.briscola = new Card(oldGame.getBriscola());
        this.p1Point = oldGame.getP1Point();
        this.p2Point = oldGame.getP2Point();
        this.nextPlayer = oldGame.getNextPlayer();
        this.p1Hand = new Hand(new ArrayList<Card>(oldGame.getP1Hand().getHand()));
        this.p2Hand = new Hand(new ArrayList<Card>(oldGame.getP2Hand().getHand()));
        this.tavolo = new Hand(new ArrayList<Card>(oldGame.getTavolo().getHand()));
    }

    public static MonteCarloGame getResult (MonteCarloGame game, Action move) {
        MonteCarloGame newGame = new MonteCarloGame(game);
        newGame.play1Turn(move);
        return newGame;
    }

    public void play1Turn (Action move) {

        tavolo.addOne(move.getCards().getHand().get(0));
        tavolo.addOne(move.getCards().getHand().get(1));

        if (move.getFirstPlayer() == 0) {
            p1Hand.removeOne(move.getCards().getHand().get(0));
            p2Hand.removeOne(move.getCards().getHand().get(1));
        } else {
            p1Hand.removeOne(move.getCards().getHand().get(1));
            p2Hand.removeOne(move.getCards().getHand().get(0));
        }

        collectAndDeal();
    }

    public void playUntilEnd (int depth, boolean pruning) {
        boolean itsComplete = false;

        while (!itsComplete) {
            if (tavolo.getHand().size() == 2) {

                collectAndDeal();
                itsComplete = true;

            } else {
                // TODO: completamento con la giocata dell'avversario
                // (Pietro: bisogna considerare una Action come una giocata, non due, quindi cambiando play1Turn() di conseguenza e poi iniziare il minMax con maximize=false)

            }
        }

        if (nextPlayer == whoIam) {
            if (pruning) {
                gameVal = minMaxCalc(this, true, depth, -10000, 10000);
            } else {
                gameVal = minMaxCalc(this, true, depth);

            }
        } else {
            if (pruning) {
                gameVal = minMaxCalc(this, false, depth, -10000, 10000);
            } else {
                gameVal = minMaxCalc(this, false, depth);

            }
        }
    }

    private static int minMaxCalc (MonteCarloGame game, boolean maximize, int depth, int alpha, int beta) {

        nodes++;

        if (game.ENDED || depth == 0) {
            if (game.getWhoIam() == 0) {
                return game.p1Point - game.p2Point;
            } else {
                return game.p2Point - game.p1Point;
            }
        }

        int value = 0;
        List<Action> moves = Action.getAction(game.getP1Hand(), game.getP2Hand(), game.getNextPlayer());


        for (Action move : moves) {
            if (maximize) {
                value = -1000;
                value = Math.max(value, minMaxCalc(MonteCarloGame.getResult(game, move), false, depth - 1, alpha, beta));

                if(value >= beta){
                    return value;
                }
                alpha = Math.max(alpha, value);

            } else {
                value = +1000;
                value = Math.min(value, minMaxCalc(MonteCarloGame.getResult(game, move), true, depth - 1, alpha, beta));

                if(value <= alpha){
                    return value;
                }
                beta = Math.min(beta, value);
            }
        }

        return value;
    }

    private static int minMaxCalc(MonteCarloGame game, boolean maximize, int depth) {

        nodes++;

        if (game.ENDED || depth == 0) {
            return game.p1Point - game.p2Point;
        }

        int value = 0;
        List<Action> moves = Action.getAction(game.getP1Hand(), game.getP2Hand(), game.getNextPlayer());


        for(Action move : moves) {

            if (maximize) {
                value = -1000;
                value = Math.max(value, minMaxCalc(MonteCarloGame.getResult(game, move), false, depth -1));
            } else {
                value = +1000;
                value = Math.min(value, minMaxCalc(MonteCarloGame.getResult(game, move), true, depth -1));
            }
        }

        return value;
    }

    private void collectAndDeal(){
  /*      int handWinner = Util.getHandWinner(tavolo, briscola.getSuit());

        if (handWinner == 0) {
            nextPlayer = nextPlayer == 0 ? 1 : 0;
        }
*/
        nextPlayer = Util.getHandWinner(tavolo, briscola.getSuit(), nextPlayer);

        if (nextPlayer == 1) {
            p1Point += Util.calculatePoints(tavolo);
        } else {
            p2Point += Util.calculatePoints(tavolo);
        }

        tavolo.getHand().clear();

        if (mazzo.getCards().size() >= 2) {
            if (nextPlayer == 0) {
                p2Hand.addOne(mazzo.deal(1).get(0));
                p1Hand.addOne(mazzo.deal(1).get(0));
            } else {
                p1Hand.addOne(mazzo.deal(1).get(0));
                p2Hand.addOne(mazzo.deal(1).get(0));
            }
        }

        if((p1Hand.getHand().size() == 0 && p2Hand.getHand().size() == 0) || p1Point > 60 || p2Point > 60){
            ENDED = true;
        }
    }

}
