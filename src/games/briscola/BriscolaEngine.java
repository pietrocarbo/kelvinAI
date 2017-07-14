package games.briscola;

import decks.forty.*;

import java.util.ArrayList;
import java.util.Scanner;

public class BriscolaEngine {

    private Card briscola;
    private Deck mazzo;
    private Integer turnsLeft;
    private Integer toPlay;
    private Hand manoP1;
    private Hand manoP2;
    private Hand puntiP1;
    private Hand puntiP2;
    private Hand tavolo;

    public BriscolaEngine() {
        mazzo = new Deck();
        briscola = mazzo.shuffle().peekLast();
        turnsLeft = 20;
        toPlay = 1;

        manoP1 = new Hand(mazzo.deal(3));
        manoP2 = new Hand(mazzo.deal(3));

        puntiP1 = new Hand(new ArrayList<Card>());
        puntiP2 = new Hand(new ArrayList<Card>());

        tavolo = new Hand(new ArrayList<Card>());
    }

    public Integer changeTurn() {
        return toPlay = (toPlay == 2 ? 1 : 2);
    }

    public boolean ply(Card card) {

        if (toPlay == 1) {
            manoP1.removeOne(card);
            tavolo.addOne(card);
            collectAndDeal();

        } else if (toPlay == 2) {
            manoP2.removeOne(card);
            tavolo.addOne(card);
            collectAndDeal();
        }

        if (turnsLeft == 0) {
            System.out.println("Game over (" + mazzo.getDeck().size() + " cards in deck). Scores: P1 (" + puntiP1.getHand().size() + " cards, " + pointScored(puntiP1) + " points), P2(" + puntiP2.getHand().size() + " cards, " + pointScored(puntiP2) + " points)");
            return true;
        }

        return false;
    }

    public void collectAndDeal() {

        if (tavolo.getHand().size() != 2) {
            changeTurn();

        } else {
            System.out.println("***\nCollecting from the (" + tavolo.getHand().size() + " elements) the board " + tavolo.getHand());
            toPlay = chooseWinner();
            System.out.println("Player " + toPlay + " won this ply\n***\n");

            if (toPlay == 1) {
                puntiP1.addAll(tavolo.getHand());
                tavolo.getHand().clear();
                if (turnsLeft > 3) {
                    manoP1.addOne(mazzo.deal(1).get(0));
                    manoP2.addOne(mazzo.deal(1).get(0));
                }
            } else {
                puntiP2.addAll(tavolo.getHand());
                tavolo.getHand().clear();
                if (turnsLeft > 3) {
                    manoP2.addOne(mazzo.deal(1).get(0));
                    manoP1.addOne(mazzo.deal(1).get(0));
                }
            }
            turnsLeft--;
        }
    }

    public Integer chooseWinner() {
        Card c1 = tavolo.getHand().get(0);
        Suit s1 = c1.getSuit();
        Rank r1 = c1.getRank();

        Card c2 = tavolo.getHand().get(1);
        Suit s2 = c2.getSuit();
        Rank r2 = c2.getRank();

        Suit semeBriscola = briscola.getSuit();

        System.out.println("Choosing winner between cards " + c1 + " and " + c2);

        if (s1 == semeBriscola && s1 != s2) return 1;
        if (s2 == semeBriscola && s1 != s2) return 2;

        if (s1 == s2) return r1.getBriscolaValue() > r2.getBriscolaValue() ? 1 : 2;

        return 1;  // non c'è briscola e le due carte sono di semi diversi: vince la prima carta
    }

    public Integer pointScored(Hand cards) {
        Integer counter = 0;
        for (Card card : cards.getHand()) {
            switch (card.getRank()) {
                case ASSO:
                    counter += 11;
                    break;
                case TRE:
                    counter += 10;
                    break;
                case RE:
                    counter += 4;
                    break;
                case CAVALLO:
                    counter += 3;
                    break;
                case FANTE:
                    counter += 2;
                    break;
                default:
                    break;
            }
        }
        return counter;
    }

    public Hand getManoP1() {
        return manoP1;
    }

    public Hand getManoP2() {
        return manoP2;
    }

    public Hand getPuntiP1() {
        return puntiP1;
    }

    public Hand getPuntiP2() {
        return puntiP2;
    }

    public Integer getTurnsLeft() {
        return turnsLeft;
    }


    @Override
    public String toString() {
        return "P1\t\t\t\t\t\t\t\t\t\t\t\t" + manoP1 + "\n" +
                "\n" +
                "(plyes left " + turnsLeft + " , toPlay " + toPlay + ", briscola " + briscola + ")\t\t\t" +
                "board " + tavolo.getHand() + "\n" +
                "\n" +
                "P2\t\t\t\t\t\t\t\t\t\t\t\t" + manoP2 +
                "\n-------------------------------------------------------------------------------------------------------------------------------------------------\n";
    }


    public void play1vsAI() {

        BriscolaGame game = new BriscolaGame();

        BriscolaAgent briscolaAgent = new BriscolaAgent(game);

        Scanner sc = new Scanner(System.in);

        Card cardSelected = new Card(Rank.ASSO, Suit.DENARI);
        boolean isGameOver = false;

        System.out.println("Game started. Enter the number of the card you want to play\n");

        do {
            System.out.println(game);

            if (game.getPlayer() == 1) {
                cardSelected = briscolaAgent.ply();

            } else if (game.getPlayer() == 2) {
                int inputIDX = sc.nextInt();

                if (inputIDX == 4) {
                    Hand puntiP1 = getPuntiP1();
                    System.out.println("\npoints P1 (" + pointScored(puntiP1) + ") " + puntiP1);
                    continue;

                } else if (inputIDX == 5) {
                    Hand puntiP2 = getPuntiP2();
                    System.out.println("\npoints P2 (" + pointScored(puntiP2) + ") " + puntiP2);
                    continue;

                } else if (inputIDX < 1 || inputIDX > 5
                        || (getTurnsLeft() == 2 && inputIDX > 2)
                        || (getTurnsLeft() == 1 && inputIDX > 1) ) {
                    System.err.println("Entered out of range card number ('4'-'5' to see current points)");
                    continue;

                }
                cardSelected = getManoP2().getHand().get(inputIDX - 1);

            }

            isGameOver = ply(cardSelected);

        } while (isGameOver == false);

    }

    public void play1vs1 () {

        Scanner sc = new Scanner(System.in);

        Card cardSelected = new Card(Rank.ASSO, Suit.DENARI);
        boolean isGameOver = false;

        System.out.println("Game started. Enter the number of the card you want to play\n");

        do {
            System.out.println(this);

            int inputIDX = sc.nextInt();

            if (inputIDX == 4) {
                Hand puntiP1 = getPuntiP1();
                System.out.println("\npoints P1 (" + pointScored(puntiP1) + ") " + puntiP1);
                continue;

            } else if (inputIDX == 5) {
                Hand puntiP2 = getPuntiP2();
                System.out.println("\npoints P2 (" + pointScored(puntiP2) + ") " + puntiP2);
                continue;

            } else if (inputIDX < 1 || inputIDX > 5
                    || (getTurnsLeft() == 2 && inputIDX > 2)
                    || (getTurnsLeft() == 1 && inputIDX > 1) ) {
                System.err.println("Entered out of range card number ('4'-'5' to see current points)");
                continue;

            } else {
                if (toPlay == 1)
                    cardSelected = getManoP1().getHand().get(inputIDX - 1);
                else if (toPlay == 2)
                    cardSelected = getManoP2().getHand().get(inputIDX - 1);
            }

            isGameOver = ply(cardSelected);

        } while (isGameOver == false);

    }

}
