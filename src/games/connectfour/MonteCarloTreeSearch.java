package games.connectfour;

import main.MovesOrdering;

import java.util.*;
import java.util.logging.Logger;

class Node {

    private final Logger LOGGER = Logger.getLogger(Node.class.getName());

    private int numberOfVisit;
    private double totalReward;

    Node parent;
    List<Node> childrens = null;
    private boolean expanded = false;
    private boolean terminalNode;

    private int row;
    private int column;
    char[][] gameState;
    private char nextPlayerSeed;

    public int getNumberOfVisit() {
        return numberOfVisit;
    }

    public double getTotalReward() {
        return totalReward;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isTerminalNode() {
        return terminalNode;
    }

    public char getNextPlayerSeed() {
        return nextPlayerSeed;
    }

    public char[][] getGameState() {
        return gameState;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildrens() {
        return childrens;
    }

    public void setNumberOfVisit(int numberOfVisit) {
        this.numberOfVisit = numberOfVisit;
    }

    public void setTotalReward(double totalReward) {
        this.totalReward = totalReward;
    }

    public Node(int numberOfVisit, int totalReward, Node parent, char[][] gameState, boolean terminalNode, char nextPlayerSeed, int row, int column) {
        this.numberOfVisit = numberOfVisit;
        this.totalReward = totalReward;
        this.parent = parent;
        this.gameState = gameState;
        this.terminalNode = terminalNode;
        this.nextPlayerSeed = nextPlayerSeed;
        this.row = row;
        this.column = column;
    }

    public List<Node> produceChildrens(MovesOrdering ordering) {
        if (expanded) {
            LOGGER.severe("expansion called on a non leaf node, i.e. an already EXPANDED node");
        }

        expanded = true;
        childrens = new ArrayList<>();

        List<Action> possibleAction = Action.getActions(gameState, nextPlayerSeed, ordering);

        for (Action action : possibleAction) {
            char[][] resultingBoard = Util.getResult(gameState, action);

            childrens.add(
                    new Node(0, 0, this, resultingBoard,
                    Util.isGameOver(resultingBoard, Util.getTurn(resultingBoard), 'O', 'X') != -1,
                    Util.toggle(nextPlayerSeed), action.getRow(), action.getColumn()));
        }

        if (childrens.size() == 0) {
            this.terminalNode = true;
        }

        return childrens;
    }
}

public class MonteCarloTreeSearch {

    private static final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());

    public static Action mcts (char[][] initialBoard, char aiSeed, int iterations, MovesOrdering expansionMovesOrdering) {

        LOGGER.info("MCTS with " + iterations + " iterations for player " + aiSeed + " started on board \n" + Util.boardToString(initialBoard));

        // Create root node
        Node root = new Node(0, 0, null, initialBoard, false, aiSeed, -1, -1);
        root.produceChildrens(expansionMovesOrdering);

        for (int i = 0; i < iterations; i++) {

            Node current = root;
            LOGGER.info("iteration: " + i + " root (score " + current.getTotalReward() + ", visits " + current.getNumberOfVisit() + ")");

            // Select
            while (current.isExpanded() && !current.isTerminalNode()) {
                double maxUCT = -1;
                int currentVisits = current.getNumberOfVisit();
                LOGGER.finer("selection: parent visits " + current.getNumberOfVisit());

                for (Node child : current.getChildrens()) {
                    double childUCT = UCT(currentVisits, child.getTotalReward(), child.getNumberOfVisit());
                    LOGGER.finer("selection: current child UCT " + childUCT + " (actual best " + maxUCT + ")");

                    if (childUCT >= maxUCT) {
                        LOGGER.finer("selection: found better UCT (old " + maxUCT + ", new " + childUCT + ")");
                        maxUCT = childUCT;
                        current = child;
                    }
                }
            }

            LOGGER.finer("selected node with visits: " + current.getNumberOfVisit() + " and score: " + current.getTotalReward());

            // Expand
            if (current.getNumberOfVisit() > 0 && !current.isExpanded() && !current.isTerminalNode()) {
                List<Node> currentChildrens = current.produceChildrens(expansionMovesOrdering);
                if (currentChildrens.size() == 0) {
                    LOGGER.severe("ERROR attempt to expand terminal node (isTerminal = " + current.isTerminalNode() + ") with board\n" + Util.boardToString(current.getGameState()));
                    System.exit(-1);
                }
                current = currentChildrens.get(0);
            }

            LOGGER.finer("expansion completed, current node -> visits: " + current.getNumberOfVisit() + ", score: " + current.getTotalReward());

            // Simulate
            double reward = simulatePlayout(current.getGameState(), current.getNextPlayerSeed(), aiSeed);

            LOGGER.finer("simulation completed, current node -> reward: " + reward);

            // Backpropagate
            while(current.getParent() != null) {
                current.setNumberOfVisit(current.getNumberOfVisit() + 1);
                current.setTotalReward(current.getTotalReward() + reward);
                current = current.getParent();
            }
            current.setNumberOfVisit(current.getNumberOfVisit() + 1);
            current.setTotalReward(current.getTotalReward() + reward);

            LOGGER.finer("backpropagation and iteration " + i + " completed");

        }

        Node bestChildNode = new Node(-1, 0, null, null, false,  '_', -1, -1);
        for (Node rootChild: root.childrens) {
            if (rootChild.getNumberOfVisit() > bestChildNode.getNumberOfVisit()) {
                bestChildNode = rootChild;
            }
        }

        LOGGER.info("MCTS choosed move " + bestChildNode.getRow() + "," + bestChildNode.getColumn() + " visited " + bestChildNode.getNumberOfVisit() + " times");

        return new Action(bestChildNode.getRow(), bestChildNode.getColumn(), aiSeed);
    }

    public static double UCT(int parentVisits, double nodeScore, int nodeVisits) {
        if (nodeVisits == 0) {
            return Double.MAX_VALUE;
        }
        return (nodeScore / (double) nodeVisits) + 1.41 * Math.sqrt(Math.log(parentVisits) / (double) nodeVisits);
    }

    public static double simulatePlayout(char[][] board, char nextPlayerSeed, char aiSeed) {

        int endgame = Util.isGameOver(board, Util.getTurn(board), aiSeed, Util.toggle(aiSeed));

        while (endgame == -1) {

            List<Action> actions = Action.getActions(board, nextPlayerSeed, MovesOrdering.RANDOM);
            if (actions.size() == 0) {
                LOGGER.severe("ERROR during simulation: no action is possible from terminal board\n" + Util.boardToString(board));
                System.exit(-1);
            }

            Action randomAction = actions.get(0);

            board = Util.getResult(board, randomAction);

            endgame = Util.isGameOver(board, Util.getTurn(board), aiSeed, Util.toggle(aiSeed));

            nextPlayerSeed = Util.toggle(nextPlayerSeed);
        }

        if      (endgame == 0)      return 1;      //seedP1 win, return ai win
        else if (endgame == 1)      return -1;     //seedP2 win, return ai loss
        else                        return 0;      //draw
    }

}
