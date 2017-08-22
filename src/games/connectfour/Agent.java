package games.connectfour;

public class Agent {

    private static char seedStarter;
    private long nodesVisited = 0;
    private long depth = 0;
    private final int SEARCH_DEPTH = 1;
    private Game game;

    public Agent(int starter) {
        if (starter == 0)   seedStarter = 'O';
        else                seedStarter = 'X';
        game = new Game(seedStarter);
    }

    public int[] ply (char[][] board) {
        int movesCounter = 0;
        Action bestMove = null;

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity
        char player = game.getPlayer(board);  // 'O' is AI, 'X' is Human

        for (Action action : game.getActions(board)) {  // list of Action for bottom empty squares

            System.out.println("[search] analyzing " + movesCounter + "º move " + action.getRow() + ", " + action.getColumn());

            double value = minValue(game.getResult(board, action), player); // recursion called with new board
            depth = 0;
            movesCounter++;

            System.out.println("[search] move analyzed. (found:" + value + ", best: " + resultValue + ")");

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }
        }

        System.out.println("[final] ply (" + bestMove.getRow() + ", " + bestMove.getColumn() + ") value " + resultValue + ", nodes expanded " + nodesVisited + ", moves analyzed " + movesCounter);
        return new int[]{bestMove.getRow(), bestMove.getColumn()};
    }

    public double minValue(char[][] board, char player) {  // returns an utility value
        double value = Double.POSITIVE_INFINITY;
        nodesVisited++;
        depth++;

        if (game.isTerminal(board) || depth > SEARCH_DEPTH) {
            return game.getUtilityHeuristic(board);
        }

        for (Action action : game.getActions(board)) {
            value = Math.min(value, maxValue(game.getResult(board, action), player));
        }

        System.out.println("[min] depth " + depth +", nodes visited " + nodesVisited);
        return value;
    }

    public double maxValue(char[][] board, char player) { // returns an utility value
        double value = Double.NEGATIVE_INFINITY;
        nodesVisited++;
        depth++;


        if (game.isTerminal(board) || depth > SEARCH_DEPTH) {
            return game.getUtilityHeuristic(board);
        }

        for (Action action : game.getActions(board)) {
            value = Math.max(value, minValue(game.getResult(board, action), player));
        }

        System.out.println("[max] depth " + depth +", nodes visited " + nodesVisited);
        return value;
    }
}
