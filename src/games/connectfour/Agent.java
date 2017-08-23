package games.connectfour;

import java.util.List;
import java.util.logging.Logger;

public class Agent {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private static char seedStarter;
    private long nodesVisited = 0;
    private long depth = 0;
    private int SEARCH_DEPTH = 0;
    private Game game;

    public Agent(int starter, int depth) {
        if (starter == 0)   seedStarter = 'O';
        else                seedStarter = 'X';
        SEARCH_DEPTH = depth;
        game = new Game(seedStarter);
    }

    public int[] ply (char[][] board) {
        int movesCounter = 0;
        Action bestMove = null;

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity
        char player = game.getPlayer(board);  // 'O' is AI, 'X' is Human
        List<Action> legalActions = game.getActions(board);

        for (Action action : legalActions) {  // list of Action for bottom empty squares

            LOGGER.fine("[search] analyzing " + movesCounter + "/" + legalActions.size() + " move " + action.getRow() + ", " + action.getColumn());

            double value = minValue(game.getResult(board, action), player,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); // recursion called with new board
            depth = 0;
            movesCounter++;

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
            return game.getUtilityHeuristic(board);
        }

        for (Action action : game.getActions(board)) {
            value = Math.min(value, maxValue(game.getResult(board, action), player, alpha, beta));
            if (value <= alpha)
                return value;
            beta = Math.min(beta, value);
        }

        LOGGER.fine("[min] depth " + depth +", nodes visited " + nodesVisited);
        return value;
    }

    public double maxValue(char[][] board, char player, double alpha, double beta) { // returns an utility value
        double value = Double.NEGATIVE_INFINITY;
        nodesVisited++;
        depth++;


        if (game.isTerminal(board) || depth > SEARCH_DEPTH) {
            return game.getUtilityHeuristic(board);
        }

        for (Action action : game.getActions(board)) {
            value = Math.max(value, minValue(game.getResult(board, action), player, alpha, beta));
            if (value >= beta)
                return value;
            alpha = Math.max(alpha, value);
        }

        LOGGER.fine("[max] depth " + depth +", nodes visited " + nodesVisited);
        return value;
    }
}
