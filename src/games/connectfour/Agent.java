package games.connectfour;

public class Agent {

    private static char seedStarter;
    private long nodeVisited = 0;
    private long depth = 0;


    public static char getSeedStarter() {
        return seedStarter;
    }

    public Agent(int starter) {
        if (starter == 0)   seedStarter = 'O';
        else                seedStarter = 'X';
    }

    public static char seedToMarker (int seed) {
        char marker;

        if (seed == 0)          marker = 'O';
        else if (seed == 1)     marker = 'X';
        else                    marker = getSeedStarter();

        return marker;
    }

    public int[] ply (char[][] board) {
        nodeVisited++;
        int moveCounter = 0;

        Game game = new Game(seedStarter);

        Action bestMove = null;
        // int[][] board = convertBoardFormat(stringBoard, SEED_UNIT);

        double resultValue = Double.NEGATIVE_INFINITY; // ai is MAX node, so initialize best move utility with -infinity
        char player = Game.getPlayer(board);  // 0 is AI, 1 is Human, -1 is starter

        for (Action action : Game.getActions(board)) {  // list of Action for empty squares filling moves

            double value = minValue(Game.getResult(board, action), player); // recursion called with new board
            depth = 0;
            moveCounter++;

            System.out.println("moveCounter -> " + moveCounter);

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }
        }
        System.out.println("[debug-ai] ply value " + resultValue + " nodes expanded " + nodeVisited);
        return new int[]{bestMove.getRow(), bestMove.getColumn()};
    }

    public double minValue(char[][] board, char player) {  // returns an utility value
        nodeVisited++;
        depth++;

        System.out.println("[debug] nodes visited " + nodeVisited + ", depth " + depth);

        if (Game.isTerminal(board))
            return Game.getUtility(board);

        double value = Double.POSITIVE_INFINITY;

        for (Action action : Game.getActions(board)) {
            // if (depth > 10000)   return value;
            value = Math.min(value, maxValue(Game.getResult(board, action), player));
        }

        return value;
    }

    public double maxValue(char[][] board, char player) { // returns an utility value
        nodeVisited++;
        depth++;

        System.out.println("[debug] nodes visited " + nodeVisited + ", depth " + depth);

        if (Game.isTerminal(board))
            return Game.getUtility(board);

        double value = Double.NEGATIVE_INFINITY;

        for (Action action : Game.getActions(board)) {
             // if (depth > 10000)   return value;
            value = Math.max(value, minValue(Game.getResult(board, action), player));
        }
        return value;
    }


}
