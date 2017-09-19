package games.connectfour;

import main.MovesOrdering;

import java.util.Scanner;
import java.util.logging.Logger;


abstract class Player {

    char mySeed;
    char starterSeed;

    public Player(char mySeed, char starterSeed) {
        this.mySeed = mySeed;
        this.starterSeed = starterSeed;
    }

    public char getMySeed() {
        return mySeed;
    }

    abstract Action play(char grid[][]);
}

class AI extends Player {

    private static final Logger LOGGER = Logger.getLogger(AI.class.getName());

    private boolean maximize;
    private int depth;
    private MovesOrdering movesOrdering;

    public AI(char mySeed, char starterSeed, boolean maximize, int depth, MovesOrdering movesOrdering) {
        super(mySeed, starterSeed);
        this.maximize = maximize;
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

    public AIMCTS(char mySeed, char starterSeed, int iterations, MovesOrdering movesOrdering) {
        super(mySeed, starterSeed);
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

    public Human(char mySeed, char starterSeed) {
        super(mySeed, starterSeed);
    }

    @Override
    public Action play(char[][] grid) {
        Scanner scanner = new Scanner(System.in);

        Action nextMove;

        while (true) {
            String input = scanner.next();

            if (input.matches("^\\d$")) {
                int column = Integer.parseInt(input);
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


