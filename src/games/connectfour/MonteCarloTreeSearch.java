package games.connectfour;

import java.util.*;
import java.util.logging.Logger;

class Node {

    private final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());

    private int numberOfVisit;
    private double totalReward;

    Node parent;
    List<Node> childrens = null;
    private boolean expanded = false;
    private boolean terminalNode;

    private int row;
    private int column;
    char[][] gameState;
    private char nextSeed;

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

    public char getNextSeed() {
        return nextSeed;
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

    public Node(int numberOfVisit, int totalReward, Node parent, char[][] gameState, boolean terminalNode, char nextSeed, int row, int column) {
        this.numberOfVisit = numberOfVisit;
        this.totalReward = totalReward;
        this.parent = parent;
        this.gameState = gameState;
        this.terminalNode = terminalNode;
        this.nextSeed = nextSeed;
        this.row = row;
        this.column = column;
    }

    public List<Node> produceChildrens() {
        if (expanded) {
            LOGGER.severe("expansion called on a non leaf node, i.e. an already EXPANDED node");
        }

        expanded = true;
        childrens = new ArrayList<Node>();

        List<Action> possibleAction = Action.getActions(gameState, 'O', 1);
        for (Action action : possibleAction) {
            char[][] resultingBoard = Util.getResult(gameState, action);
            childrens.add(new Node(0, 0, this, resultingBoard,
                    Util.gameOverChecks(resultingBoard, Util.calculateTurn(resultingBoard), 'O', 'X') != -1 ? true : false,
                    nextSeed == 'O' ? 'X' : 'O', action.getRow(), action.getColumn()));
        }

        if (childrens.size() == 0) {
            this.terminalNode = true;
        }

        return childrens;
    }
}

public class MonteCarloTreeSearch {

    private static final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());

    public static int[] mcts (char[][] initialBoard, char aiSeed, int iterations) {

        LOGGER.info("MCTS (" + iterations + " iteration) started on board \n" + Util.boardToString(initialBoard));

        // Create root node
        Node root = new Node(0, 0, null, initialBoard, false, aiSeed, -1, -1);
        root.produceChildrens();

        for (int i = 0; i < iterations; i++) {

            Node current = root;
            LOGGER.info("iteration: " + i + " root score " + current.getTotalReward() + ", visits " + current.getNumberOfVisit());

            // Select
            while (current.isExpanded() && !current.isTerminalNode()) {
                double maxUCT = -1;
                int currentVisits = current.getNumberOfVisit();
                // LOGGER.info("inside selection: current visits " + current.getNumberOfVisit());

                for (Node child : current.getChildrens()) {
                    double childUCT = UCT(currentVisits, child.getTotalReward(), child.getNumberOfVisit());
                    // LOGGER.info("inside selection: current child UCT " + childUCT + " (best " + maxUCT + ")");

                    if (childUCT >= maxUCT) {
                        // LOGGER.info("inside selection: found better UCT (old " + maxUCT + ", new " + childUCT + ")");
                        maxUCT = childUCT;
                        current = child;
                    }
                }

            }

            LOGGER.info("selection completed, current -> visit " + current.getNumberOfVisit() + " score " + current.getTotalReward());

            // Expand
            if (current.getNumberOfVisit() > 0 && !current.isExpanded() && !current.isTerminalNode()) {
                List<Node> currentChildrens = current.produceChildrens();
                if (currentChildrens.size() == 0) {
                    LOGGER.severe("attempt to expand terminal node (flag " + current.isTerminalNode() + ") w/ board\n" + Util.boardToString(current.getGameState()));
                    System.exit(-1);
                }
                current = currentChildrens.get(0);
            }

            LOGGER.info("expansion completed, current -> visit " + current.getNumberOfVisit() + " score " + current.getTotalReward());

            // Simulate (randomly)
            double reward = simulatePlayout(current.getGameState(), current.getNextSeed());

            LOGGER.info("simulation completed, current -> reward " + reward);

            // Backpropagate
            while(current.getParent() != null) {
                current.setNumberOfVisit(current.getNumberOfVisit() + 1);
                current.setTotalReward(current.getTotalReward() + reward);
                current = current.getParent();
            }
            current.setNumberOfVisit(current.getNumberOfVisit() + 1);
            current.setTotalReward(current.getTotalReward() + reward);

            LOGGER.info("backpropagation completed");

        }

        Node bestAction = new Node(-1, 0, null, null, false,  '_', -1, -1);
        for (Node rootChild: root.childrens) {
            if (rootChild.getNumberOfVisit() > bestAction.getNumberOfVisit()) {
                bestAction = rootChild;
            }
        }

        LOGGER.info("MCTS choosed move " + bestAction.getRow() + "," + bestAction.getColumn());
        return new int[] {bestAction.getRow(), bestAction.getColumn()};
    }

    public static double UCT(int parentVisits, double nodeScore, int nodeVisits) {
        if (nodeVisits == 0) {
            return Double.MAX_VALUE;
        }
        return ((double) nodeScore / (double) nodeVisits) + 1.41 * Math.sqrt(Math.log(parentVisits) / (double) nodeVisits);
    }

    public static double simulatePlayout(char[][] board, char nextSeed) {

        int endgame = Util.gameOverChecks(board, Util.calculateTurn(board), 'O', 'X');
        while (endgame == -1) {

            Action randomAction = randomMove(board);
            board = Util.getResult(board, randomAction);
            endgame = Util.gameOverChecks(board, Util.calculateTurn(board), 'O', 'X');
            nextSeed = nextSeed == 'O' ? 'X' : 'O';
        }

        if      (endgame == 0)      return 1;      //seedP1 win, ai win
        else if (endgame == 1)      return -1;      //seedP2 win, ai loss
        else /*if(endgame == 2)*/   return 0;    //draw
    }

    public static Action randomMove(char[][] board) {

        List<Action> actions = Action.getActions(board, 'O',1);
        if (actions.size() == 0) {
            LOGGER.severe("no action is possible from terminal board\n" + Util.boardToString(board));
        }
        return actions.get(0);
    }

}
