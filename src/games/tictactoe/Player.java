package games.tictactoe;

import java.util.Scanner;
import java.util.logging.Logger;

abstract class Player {
    char seed;

    public Player(char seed) {
        this.seed = seed;
    }

    abstract public Action play(char[][] grid);
}

class AI extends Player {

    private static final Logger LOGGER = Logger.getLogger(AI.class.getName());
    private boolean maximize;

    public AI(char seed, boolean maximize) {
        super(seed);
        this.maximize = maximize;
    }

    public Action play(char[][] grid) {
        return Util.minMaxAlgorithm(grid, seed, seed == 'O' ? 'X' : 'O', maximize);
    }
}

class Human extends Player {

    private static final Logger LOGGER = Logger.getLogger(Human.class.getName());

    public Human(char seed) {
        super(seed);
    }

    @Override
    public Action play(char[][] grid) {
        Scanner scanner = new Scanner(System.in);

        int row, column;

        while (true) {
            String input = scanner.next();

            if (!input.matches("^\\d,\\d$")) {
                LOGGER.severe("Wrong input format '" + input + "' repeat please");
                continue;
            } else {
                row = Integer.parseInt(input.substring(0, 1));
                column = Integer.parseInt(input.substring(2));
            }

            if (Util.isLegalMove(grid, row, column)) {
                break;
            } else {
                LOGGER.severe("Wrong input value '" + input + "' repeat please");
                continue;
            }
        }

        return new Action(row, column, seed);
    }
}