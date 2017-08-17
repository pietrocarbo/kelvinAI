package games.connectfour;

import java.math.BigInteger;

public class Agent {

//    private final int SEED_UNIT = 1;   // has to be unsigned
//    private final int SEED_AI = SEED_UNIT, SEED_HUMAN = - SEED_UNIT;
    private static String seedStarter;
    private long nodeVisited = 0;

    public static String getSeedStarter() {
        return seedStarter;
    }

    public Agent(int starter) {
        if (starter == 0)   seedStarter = "O";
        else                seedStarter = "X";
    }

    public static String seedToMarker (int seed) {
        String marker;

        if (seed == 0)          marker = "O";
        else if (seed == 1)     marker = "X";
        else                    marker = getSeedStarter();

        return marker;
    }

    public int[] ply (String[][] board) {
        nodeVisited++;

        Game game = new Game(seedStarter);

        Action bestMove = null;
        // int[][] board = convertBoardFormat(stringBoard, SEED_UNIT);

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity
        String player = Game.getPlayer(board);  // 0 is AI, 1 is Human, -1 is starter

        for (Action action : Game.getActions(board)) {  // list of Action for empty squares filling moves

            double value = minValue(Game.getResult(board, action), player); // recursion called with new board

//            System.out.println("value -> " + value);

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }
        }
        System.out.println("[debug-ai] ply value " + resultValue + " nodes expanded " + nodeVisited);
        return new int[]{bestMove.getRow(), bestMove.getColumn()};
    }

    public double minValue(String[][] board, String player) {  // returns an utility value
        nodeVisited++;

        if (Game.isTerminal(board))
            return Game.getUtility(board);

        double value = Double.POSITIVE_INFINITY;

        for (Action action : Game.getActions(board)) {
            value = Math.min(value, maxValue(Game.getResult(board, action), player));
        }

        return value;
    }

    public double maxValue(String[][] board, String player) { // returns an utility value
        nodeVisited++;

        if (Game.isTerminal(board))
            return Game.getUtility(board);

        double value = Double.NEGATIVE_INFINITY;

        for (Action action : Game.getActions(board))
            value = Math.max(value, minValue(Game.getResult(board, action), player));

        return value;
    }


}
