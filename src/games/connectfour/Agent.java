package games.connectfour;

import java.util.List;
import java.util.logging.Logger;

public class Agent {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private static char seedStarter;
    private long nodesVisited;
    private int depth;
    private int movesCounter;
    private final int SEARCH_DEPTH;
    private Game game;

    public Agent(int starter, int search_depth) {
        if (starter == 0)   seedStarter = 'O';
        else                seedStarter = 'X';
        SEARCH_DEPTH = search_depth;
        game = new Game(seedStarter);
    }

    public int[] ply (char[][] board) {
        Action bestMove = null;
        nodesVisited = 0;
        movesCounter = 0;

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity
        char player = game.getPlayer(board);  // 'O' is AI, 'X' is Human
        List<Action> legalActions = game.getActions(board);

        for (Action action : legalActions) {  // list of Action for bottom empty squares
            depth = 0;

            movesCounter++;
            LOGGER.fine("[search] analyzing " + movesCounter + "/" + legalActions.size() + " move " + action.getRow() + ", " + action.getColumn());

            double value = minValue(game.getResult(board, action), player == 'O' ? 'X' : 'O', Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

            LOGGER.fine("[search] move analyzed. (found:" + value + ", best: " + resultValue + ")");

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }
        }
        if (bestMove == null)   bestMove = legalActions.get(0);  // if all actions score -Infinity we must anyway choose one

        LOGGER.info("[final] ply (" + bestMove.getRow() + ", " + bestMove.getColumn() + ") value " + resultValue + ", nodes expanded " + nodesVisited + ", moves analyzed " + movesCounter);
        return new int[]{bestMove.getRow(), bestMove.getColumn()};
    }

    public double minValue(char[][] board, char player, double alpha, double beta) {  // returns an utility value
        double value = Double.POSITIVE_INFINITY;
        nodesVisited++;
        depth++;

        if (game.isTerminal(board) || depth > SEARCH_DEPTH) {
            return game.getUtilityHeuristic(board, player);
        }

        for (Action action : game.getActions(board)) {
            value = Math.min(value, maxValue(game.getResult(board, action), player == 'O' ? 'X' : 'O', alpha, beta));
            if (value <= alpha) {
                LOGGER.info("Search subtree pruned by alpha (value: " + value + " <= " + alpha + " -> beta)");
                return value;
            }
            beta = Math.min(beta, value);
        }

        LOGGER.fine("depth " + depth +", nodes visited " + nodesVisited);
        return value;
    }

    public double maxValue(char[][] board, char player, double alpha, double beta) { // returns an utility value
        double value = Double.NEGATIVE_INFINITY;
        nodesVisited++;
        depth++;


        if (game.isTerminal(board) || depth > SEARCH_DEPTH) {
            return game.getUtilityHeuristic(board, player);
        }

        for (Action action : game.getActions(board)) {
            value = Math.max(value, minValue(game.getResult(board, action), player == 'O' ? 'X' : 'O', alpha, beta));
            if (value >= beta) {
                LOGGER.info("Search subtree pruned by beta (value: " + value + " >= " + beta + " -> beta)");
                return value;
            }
            alpha = Math.max(alpha, value);
        }

        LOGGER.fine("depth " + depth +", nodes visited " + nodesVisited);
        return value;
    }
}
