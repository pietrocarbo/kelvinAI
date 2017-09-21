package games.briscola;

import decks.forty.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class GameStateDetermination {

    public Hand board;
    public Deck deck;
    public Card briscola;
    public Hand[] hands;
    public int[] points;
    public Hand[] collectedCards;
    public int turn;
    public int nextPlayerIndex;

    public GameStateDetermination(Hand board, Deck deck, Card briscola, Hand[] hands, int[] points, Hand[] collectedCards, int turn, int nextPlayerIndex) {
        this.board = board;
        this.deck = deck;
        this.briscola = briscola;
        this.hands = hands;
        this.points = points;
        this.collectedCards = collectedCards;
        this.turn = turn;
        this.nextPlayerIndex = nextPlayerIndex;
    }

    public GameStateDetermination(GameStateDetermination oldGameStateDetermination) {
        this.board = new Hand(new ArrayList<>(oldGameStateDetermination.board.getHand()));
        this.deck = new Deck(new Hand(new ArrayList<>(oldGameStateDetermination.deck.getCards())));
        this.briscola = oldGameStateDetermination.briscola;
        this.hands = new Hand[2];
        this.hands[0] = new Hand(new ArrayList<>(oldGameStateDetermination.hands[0].getHand()));
        this.hands[1] = new Hand(new ArrayList<>(oldGameStateDetermination.hands[1].getHand()));
        this.points = new int[2];
        this.points[0] = oldGameStateDetermination.points[0];
        this.points[1] = oldGameStateDetermination.points[1];
        this.collectedCards = new Hand[2];
        this.collectedCards[0] = new Hand(new ArrayList<>(oldGameStateDetermination.collectedCards[0].getHand()));
        this.collectedCards[1] = new Hand(new ArrayList<>(oldGameStateDetermination.collectedCards[1].getHand()));
        this.turn = oldGameStateDetermination.turn;
        this.nextPlayerIndex = oldGameStateDetermination.nextPlayerIndex;
    }

}

public class Util {

    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static int getIndexOfLessValueCard(Hand cards, Suit briscola) {
        int i, lessValueFound = 11, indexLessValueCard = -1;

        for (i = 0; i < cards.getHand().size(); i++) {
            if (cards.getHand().get(i).getSuit() != briscola
                    && cards.getHand().get(i).getRank().getBriscolaValue() <= 5 // no points card
                    && lessValueFound > cards.getHand().get(i).getRank().getBriscolaValue()) {
                lessValueFound = cards.getHand().get(i).getRank().getBriscolaValue();
                indexLessValueCard = i;
            }
        }
        if (indexLessValueCard != -1) return indexLessValueCard;


        for (i = 0; i < cards.getHand().size(); i++) {
            if (cards.getHand().get(i).getSuit() != briscola
                    && cards.getHand().get(i).getRank().getBriscolaValue() <= 8  // no 11/10 points card
                    && lessValueFound > cards.getHand().get(i).getRank().getBriscolaValue()) {
                lessValueFound = cards.getHand().get(i).getRank().getBriscolaValue();
                indexLessValueCard = i;
            }
        }
        if (indexLessValueCard != -1) return indexLessValueCard;


        for (i = 0; i < cards.getHand().size(); i++) {
            if (cards.getHand().get(i).getSuit() != briscola
                    && lessValueFound > cards.getHand().get(i).getRank().getBriscolaValue()) {
                lessValueFound = cards.getHand().get(i).getRank().getBriscolaValue();
                indexLessValueCard = i;
            }
        }
        if (indexLessValueCard != -1) return indexLessValueCard;


        for (i = 0; i < cards.getHand().size(); i++) {
            if (lessValueFound > cards.getHand().get(i).getRank().getBriscolaValue()) {
                lessValueFound = cards.getHand().get(i).getRank().getBriscolaValue();
                indexLessValueCard = i;
            }
        }
        return indexLessValueCard;
    }

    public static int getIndexOfLessValueWinningCardOtherwiseLessValue(Hand cards, Suit briscola, Card tableCard) {
        List<Card> winningCards = new ArrayList<>();
        List<Integer> winningCardsIndices = new ArrayList<>();

        for (int i = 0; i < cards.getHand().size(); i++) {

            Hand newTable = new Hand(new ArrayList<>());
            newTable.addOne(tableCard);
            newTable.addOne(cards.getHand().get(i));

            if (getHandWinner(newTable, briscola) == 1) {
                winningCards.add(cards.getHand().get(i));
                winningCardsIndices.add(i);
            }
        }

        if (winningCards.size() > 1) {
            return winningCardsIndices.get(getIndexOfLessValueCard(new Hand(winningCards), briscola));
        } else if (winningCards.size() == 1) {
            return winningCardsIndices.get(0);
        } else {
            return getIndexOfLessValueCard(cards, briscola);
        }
    }

    public static Card monteCarloApproximation(String name, int search_depth, int numberOfRandomDeals,
                                               Hand myHand, Card briscola, Hand board, Hand unseenCards,
                                               Hand myCollectedCards, Hand opponentCollectedCards,
                                               int turn, int opponentNOfCard) {

        LOGGER.fine("Monte Carlo approximation based on minmax started (player " + name + ", depth " + search_depth + ", deals " + numberOfRandomDeals + ")");

        int[] cardConfidenceValues = new int[myHand.getHand().size()];

        int dealsSearchedCounter = 0;

        List<int[]> dealsSearchedSerialization = new ArrayList<>();
        long totalUnseenCardsPermutations = factorial(unseenCards.getHand().size());

        List<Deck> randomizedDecks = new ArrayList<>();

        while (dealsSearchedCounter < numberOfRandomDeals && dealsSearchedCounter != totalUnseenCardsPermutations) {

            Deck randomDeck;
            int[] randomDeckSerialization;

            do {
                randomDeck = new Deck(unseenCards.shuffle());
                randomDeckSerialization = serializeCards(randomDeck);
            } while (alreadyExist(dealsSearchedSerialization, randomDeckSerialization));

            dealsSearchedSerialization.add(randomDeckSerialization);

            randomDeck.getCards().add(briscola);

            randomizedDecks.add(randomDeck);

            dealsSearchedCounter++;
        }

        LOGGER.fine("Randomized decks list created (length " + randomizedDecks.size() + ")");

        Hand[] hands = new Hand[2];
        hands[0] = new Hand(new ArrayList<>(myHand.getHand()));

        int[] points = new int[]{calculatePoints(myCollectedCards), calculatePoints(opponentCollectedCards)};

        Hand[] collectedCards = new Hand[2];
        collectedCards[0] = new Hand(new ArrayList<>(myCollectedCards.getHand()));
        collectedCards[1] = new Hand(new ArrayList<>(opponentCollectedCards.getHand()));

        List<Card> legalActions = getActions(myHand);

        for (int i = 0; i < randomizedDecks.size(); i++) {

            hands[1] = new Hand(new ArrayList<>(randomizedDecks.get(i).deal(opponentNOfCard)));
            GameStateDetermination initialGSD = new GameStateDetermination(new Hand(new ArrayList<>(board.getHand())), randomizedDecks.get(i), briscola, hands, points, collectedCards, turn, 0);


            int bestCardIndexCurrentGDS = 0;
            double resultValue = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < legalActions.size(); j++) {

                double value = heuristicMinMaxAlphaBeta(getResult(initialGSD, legalActions.get(j)), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, search_depth);

                LOGGER.finer("card " + legalActions.get(j) + " scored " + value + " in GSD n." + i);

                if (value > resultValue) {
                    bestCardIndexCurrentGDS = j;
                    resultValue = value;
                }

            }
            LOGGER.finer("best card index in GSD n." + i + " is " + bestCardIndexCurrentGDS);
            cardConfidenceValues[bestCardIndexCurrentGDS] += 1;
        }

        int bestCardIndex = 0;
        for (int i = 0; i < myHand.getHand().size(); i++) {
            if (cardConfidenceValues[i] > cardConfidenceValues[bestCardIndex]) {
                bestCardIndex = i;
            }
            LOGGER.info("Card n." + i + " scored " + cardConfidenceValues[i]);
        }
        LOGGER.info("Chosen card with index " + bestCardIndex + " (confidence " + cardConfidenceValues[bestCardIndex] + ")");

        return myHand.getHand().get(bestCardIndex);
    }

    public static double heuristicMinMaxAlphaBeta(GameStateDetermination gsd, double alpha, double beta, int depth) {
        double value = (gsd.nextPlayerIndex == 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);

        if (gsd.turn == 19 || depth <= 0) {
            return (gsd.points[0] - gsd.points[1]);
        }

        for (Card action : getActions(gsd.hands[gsd.nextPlayerIndex])) {

            if (gsd.nextPlayerIndex == 0) {  // maximize
                value = Math.max(value, heuristicMinMaxAlphaBeta(getResult(gsd, action), alpha, beta, depth - 1));

                if (value >= beta) {
                    LOGGER.finest("subtree pruned by beta (value: " + value + " >= beta: " + beta + ")");
                    return value;
                }
                alpha = Math.max(alpha, value);

            } else {  // minimize

                value = Math.min(value, heuristicMinMaxAlphaBeta(getResult(gsd, action), alpha, beta, depth - 1));

                if (value <= alpha) {
                    LOGGER.finest("subtree pruned by alpha (value: " + value + " <= alpha: " + alpha + ")");
                    return value;
                }
                beta = Math.min(beta, value);
            }
        }

        LOGGER.finest("value " + value + " returned from depth " + depth);

        return value;
    }

    public static GameStateDetermination getResult(GameStateDetermination gsd, Card cardPlayed) {
        GameStateDetermination resultGSD = new GameStateDetermination(gsd);

        if (!resultGSD.hands[gsd.nextPlayerIndex].removeOne(cardPlayed)) {
            LOGGER.severe("ERROR impossible to transition to new game state, card played not in hand");
            System.exit(-1);
        }

        resultGSD.board.addOne(cardPlayed);

        if (resultGSD.board.getHand().size() == 2) {

            int winner = getHandWinner(resultGSD.board, resultGSD.briscola.getSuit());
            resultGSD.nextPlayerIndex = (winner != 1 ? toggle(resultGSD.nextPlayerIndex) : resultGSD.nextPlayerIndex);

            resultGSD.collectedCards[resultGSD.nextPlayerIndex].addAll(resultGSD.board.getHand());
            resultGSD.board.getHand().clear();

            resultGSD.points[resultGSD.nextPlayerIndex] = calculatePoints(resultGSD.collectedCards[resultGSD.nextPlayerIndex]);

            resultGSD.turn++;

            if (resultGSD.turn < 17) {
                if (resultGSD.nextPlayerIndex == 0) {
                    resultGSD.hands[0].addOne(resultGSD.deck.deal(1).get(0));
                    resultGSD.hands[1].addOne(resultGSD.deck.deal(1).get(0));
                } else {
                    resultGSD.hands[1].addOne(resultGSD.deck.deal(1).get(0));
                    resultGSD.hands[0].addOne(resultGSD.deck.deal(1).get(0));
                }
            }

        } else {
            resultGSD.nextPlayerIndex = toggle(resultGSD.nextPlayerIndex);
        }

        return resultGSD;
    }

    public static List<Card> getActions(Hand cards) {
        return cards.getHand();
    }

    // returns the INDEX of the BOARD winning card
    public static Integer getHandWinner(Hand tavolo, Suit semeBriscola) {
        Card c1 = tavolo.getHand().get(0);
        Suit s1 = c1.getSuit();
        Rank r1 = c1.getRank();

        Card c2 = tavolo.getHand().get(1);
        Suit s2 = c2.getSuit();
        Rank r2 = c2.getRank();

        LOGGER.finest("Choosing winner between cards " + c1 + " (value " + r1.getBriscolaValue() + ") and " + c2 + " ( value " + r2.getBriscolaValue() + ")");

        if (s1 == semeBriscola && s1 != s2) return 0;
        if (s2 == semeBriscola && s1 != s2) return 1;

        if (s1 == s2) return r1.getBriscolaValue() > r2.getBriscolaValue() ? 0 : 1;

        return 0;  // non c'è briscola e le due carte sono di semi diversi: vince la prima carta
    }

    public static int getGameWinner(List<Player> players) {
        int winnerPlayerIndex = 0, pointsMax = 0;

        for (int i = 0; i < players.size(); i++) {
            int points = calculatePoints(players.get(i).getCardsCollected());
            if (points > pointsMax) {
                pointsMax = points;
                winnerPlayerIndex = i;
            } else if (points == pointsMax) {
                winnerPlayerIndex = -1;
            }
        }

        return winnerPlayerIndex;
    }

    public static Integer calculatePoints (Hand cards) {
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

    public static int[] serializeCards (Deck mazzo) {
        int[] cardCodes = new int[mazzo.getCards().size()];
        for (int i = 0; i < mazzo.getCards().size(); i++) {

            cardCodes[i] = mazzo.getCards().get(i).getRank().getBriscolaValue();  // [1-10]
            switch (mazzo.getCards().get(i).getSuit()) {
                case COPPE:
                    cardCodes[i] += 10;  // [11-20]
                    break;
                case SPADE:
                    cardCodes[i] += 20;  // [21-30]
                    break;
                case DENARI:
                    cardCodes[i] += 30;  // [31-40]
            }
        }

        return cardCodes;
    }

    public static boolean alreadyExist (List<int[]> oldDecks, int[] newDeck) {
        for (int i = 0; i < oldDecks.size(); i++) {

            boolean sameDecks = true;
            for (int j = 0; j < oldDecks.get(i).length; j++) {
                if (oldDecks.get(i)[j] != newDeck[j]) {
                    sameDecks = false;
                }
            }
            if (sameDecks) {
                return true;
            }
        }
        return false;
    }

    public static long factorial (int n) {
        if (n >= 19) return Long.MAX_VALUE;

        if (n == 0 || n == 1) return 1;
        else return factorial(n - 1) * n;
    }

    public static int toggle(int actualPlayerID) {
        return (actualPlayerID == 0 ? 1 : 0);
    }
}
