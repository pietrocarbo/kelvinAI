package games.connectfour;

import main.MovesOrdering;

import java.util.Scanner;
import java.util.logging.Logger;


abstract class Player {

    char mySeed;

    public Player(char mySeed) {
        this.mySeed = mySeed;
    }

    public char getMySeed() {
        return mySeed;
    }

    abstract Action play(char grid[][]);
}

class AI extends Player {

    private static final Logger LOGGER = Logger.getLogger(AI.class.getName());

    private int depth;
    private MovesOrdering movesOrdering;

    public AI(char mySeed, int depth, MovesOrdering movesOrdering) {
        super(mySeed);
        this.depth = depth;
        this.movesOrdering = movesOrdering;
    }

    @Override
    public Action play(char[][] grid) {
        Action chosenAction = Util.heuristicMinMaxAlgorithm(grid, mySeed, depth, movesOrdering);

        if (!Util.isLegalMove(grid, chosenAction.getColumn(), chosenAction.getRow())) {
            LOGGER.severe("Wrong AI input value '" + chosenAction.getRow() + "," + chosenAction.getColumn() + "' repeat please");
        }

        return chosenAction;
    }
}

class AIMCTS extends Player {

    private static final Logger LOGGER = Logger.getLogger(AIMCTS.class.getName());

    private int iterations;
    private MovesOrdering movesOrdering;

    public AIMCTS(char mySeed, int iterations, MovesOrdering movesOrdering) {
        super(mySeed);
        this.iterations = iterations;
        this.movesOrdering = movesOrdering;
    }

    @Override
    public Action play(char[][] grid) {
        Action chosenAction = MonteCarloTreeSearch.mcts(grid, mySeed, iterations, movesOrdering);

        if (!Util.isLegalMove(grid, chosenAction.getColumn(), chosenAction.getRow())) {
            LOGGER.severe("Wrong AIMCTS input value '" + chosenAction.getRow() + "," + chosenAction.getColumn() + "' repeat please");
        }

        return chosenAction;
    }
}

class Human extends Player {

    private static final Logger LOGGER = Logger.getLogger(Human.class.getName());

    public Human(char mySeed) {
        super(mySeed);
    }

    @Override
    public Action play(char[][] grid) {
        Scanner scanner = new Scanner(System.in);

        Action nextMove;

        while (true) {

            LOGGER.info("Enter the number of column you want to play");
            String input = scanner.next();

            if (input.matches("^\\d$")) {
                int column = Integer.parseInt(input);
                if (column < 0 || column > 6) continue;
                int row = -1;
                for (int i = 0; i < 6; i++) {
                    if (grid[i][column] == '_') {
                        row = i;
                        break;
                    }
                }

                if (Util.isLegalMove(grid, column, row)) {
                    nextMove = new Action(row, column, mySeed);
                    break;
                } else {
                    LOGGER.warning("Wrong player X input value '" + input + "' repeat please");
                }
            }
            LOGGER.warning("Wrong player X input form '" + input + "' repeat please");
        }

        return nextMove;
    }
}


