package games.connectfour;

import java.util.List;
import java.util.logging.Logger;


public class Agent {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private char mySeed;
    private char starterSeed;

    private int movesOrder;

    private int movesCounter;
    private int depthCounter;
    private long nodesCounter;
    private int nodesPruned;

    public Agent(char mySeed, char starterSeed) {
        this.mySeed = mySeed;
        this.starterSeed = starterSeed;
    }

    public int[] heuristicMinMax (char[][] board, int SEARCH_DEPTH, int orderingType) {
        this.nodesCounter = 0;
        this.nodesPruned = 0;
        this.movesCounter = 0;
        this.depthCounter = 0;
        this.movesOrder = orderingType;

        Action bestMove = null;
        double resultValue = Double.NEGATIVE_INFINITY;

        List<Action> legalActions = Game.getActions(board, starterSeed, movesOrder);

        for (Action action : legalActions) {
            depthCounter = 0;
            movesCounter++;
            LOGGER.fine("analyzing " + movesCounter + "/" + legalActions.size() + " move " + action.getRow() + ", " + action.getColumn());

            double value = minValue(Game.getResult(board, action), SEARCH_DEPTH - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

            LOGGER.fine("move analyzed. max score of opponent's replies: " + value + " > best so far: " + resultValue + " ?");

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }

        }
        if (bestMove == null)   bestMove = legalActions.get(0);  // if all actions score -Infinity we must anyway choose one

        LOGGER.info("[final] ply chosen (" + bestMove.getRow() + ", " + bestMove.getColumn() + ") value " + resultValue + ", nodes expanded " + nodesCounter + ", pruned " + nodesPruned + " , moves analyzed " + movesCounter);
        return new int[]{bestMove.getRow(), bestMove.getColumn()};
    }

    public double minValue(char[][] board, int depth, double alpha, double beta) {  // returns an utility value
        double value = Double.POSITIVE_INFINITY;
        nodesCounter++;
        depthCounter++;

        if (Game.isTerminal(board) || depth <= 0) {
            return Game.getUtilityHeuristic(board, mySeed);
        }

        for (Action action : Game.getActions(board, starterSeed, movesOrder)) {
            value = Math.min(value, maxValue(Game.getResult(board, action), depth - 1, alpha, beta));

            if (value <= alpha) {
                LOGGER.fine("Search subtree pruned by alpha (value: " + value + " <= " + alpha + " -> beta)");
                nodesPruned++;
                return value;
            }
            beta = Math.min(beta, value);
        }

        LOGGER.finer("depth of this node " + depthCounter + ", total nodes visited " + nodesCounter);
        return value;
    }

    public double maxValue(char[][] board, int depth, double alpha, double beta) { // returns an utility value
        double value = Double.NEGATIVE_INFINITY;
        nodesCounter++;
        depthCounter++;

        if (Game.isTerminal(board) || depth <= 0) {
            return Game.getUtilityHeuristic(board, mySeed);
        }

        for (Action action : Game.getActions(board, starterSeed, movesOrder)) {
            value = Math.max(value, minValue(Game.getResult(board, action), depth - 1, alpha, beta));

            if (value >= beta) {
                LOGGER.fine("Search subtree pruned by beta (value: " + value + " >= " + beta + " -> beta)");
                nodesPruned++;
                return value;
            }
            alpha = Math.max(alpha, value);
        }

        LOGGER.finer("depth of this node " + depthCounter +", total nodes visited " + nodesCounter);
        return value;
    }
}
