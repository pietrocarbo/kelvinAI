package games.connectfour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonteCarloTreeSearch {

    class Node {

        private int numberOfVisit;
        private double totalReward;
        private boolean expanded = false;

        Node parent;
        List<Node> childrens;
        private boolean terminalNode = false;

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

        public Node(int numberOfVisit, int totalReward, Node parent, char[][] gameState, char nextSeed, int row, int column) {
            this.numberOfVisit = numberOfVisit;
            this.totalReward = totalReward;
            this.parent = parent;
            this.childrens = null;
            this.gameState = gameState;
            this.nextSeed = nextSeed;
            this.row = row;
            this.column = column;
        }

        public List<Node> produceChildrens() {
            expanded = true;
            childrens = new ArrayList<Node>();

            for (int column = 0; column < 7; column++) {
                for (int row = 0; row < 6; row++) {
                    if (gameState[row][column] == '_') {
                        childrens.add(new Node(0, 0, this, boardAfterMove(gameState, row, column, nextSeed),
                                            nextSeed == 'O' ? 'X' : 'O', row, column));
                        break;
                    }
                }
            }
            if (childrens.size() == 0) {
                this.terminalNode = true;
            }

            return childrens;
        }
    }


    public int[] mcts (char[][] initialBoard, char aiSeed, int iterations) {

        // Create root node
        Node root = new Node(0, 0, null, initialBoard, 'O', -1, -1);

        for (int i = 0; i < iterations; i++) {

            Node current = root;

            // Select
            while (current.expanded && !current.terminalNode) {
                double maxUCB = -1;

                for (Node child : current.getChildrens()) {
                    double childUCB = (child.getTotalReward() / child.getNumberOfVisit()) * Math.sqrt(2) * Math.sqrt(current.getNumberOfVisit() / child.getNumberOfVisit());
                    if (childUCB > maxUCB) {
                        maxUCB = childUCB;
                        current = child;
                    }
                }
            }

            // Expand (middle move first)
            if (!current.terminalNode) {
                List<Node> currentChildrens = current.produceChildrens();
                current = currentChildrens.get(0);
            }

            // Simulate (randomly)
            double reward = simulatePlayout(current.getGameState(), current.getNextSeed());

            // Backpropagate
            while(current.getParent() != null) {
                current.setNumberOfVisit(current.getNumberOfVisit() + 1);
                current.setTotalReward(current.getTotalReward() + reward);
                current = current.getParent();
            }

        }

        Node bestAction = new Node(-1, 0, null, null, '_', -1, -1);
        for (Node rootChild: root.childrens) {
            if (rootChild.getNumberOfVisit() > bestAction.getNumberOfVisit()) {
                bestAction = rootChild;
            }
        }

        return new int[] {bestAction.getRow(), bestAction.getColumn()};
    }

    public static double simulatePlayout(char[][] board, char nextSeed) {
        Random random = new Random();
        List<Integer> columnAvailable = new ArrayList<>();
        columnAvailable.add(0);
        columnAvailable.add(1);
        columnAvailable.add(2);
        columnAvailable.add(3);
        columnAvailable.add(4);
        columnAvailable.add(5);
        columnAvailable.add(6);

        int endgame = Util.gameOverChecks(board, Util.calculateTurn(board), 'O', 'X');
        while (endgame != -1) {

            int[] move = randomMove(board, random, columnAvailable);
            board = boardAfterMove(board, move[0], move[1], nextSeed);
            endgame = Util.gameOverChecks(board, Util.calculateTurn(board), 'O', 'X');
            nextSeed = nextSeed == 'O' ? 'X' : 'O';
        }

        if      (endgame == 0)      return 1;      //seedP1 win, ai win
        else if (endgame == 1)      return 0;      //seedP2 win, ai loss
        else /*if(endgame == 2)*/   return 0.5;    //draw
    }

    public static int[] randomMove(char[][] board, Random random, List columnAvailable) {
        while (true) {
            int column = random.nextInt(columnAvailable.size());
            for (int row = 0; row < 6; row++) {
                if (board[row][column] == '_') {
                    return new int[]{row, column};
                }
            }
            columnAvailable.remove(column);
        }
    }

    public static char[][] boardAfterMove (char[][] oldBoard, int row, int column, char seed) {
        char[][] newBoard = new char[6][7];
        for (int i = 0; i < 7; i++) { // columns
            for (int j = 0; j < 6; j++) { // rows
                    newBoard[j][i] = oldBoard[j][i];
            }
        }
        newBoard[row][column] = seed;
        return newBoard;
    }
}
