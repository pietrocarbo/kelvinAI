package games.connectfour;

import java.util.Scanner;
import java.util.logging.Logger;


class Player {
    char mySeed;
    char starterSeed;
    final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public Player() {
    }

    public char getMySeed() {
        return mySeed;
    }

    public void setStarterSeed(char starterSeed) {
        this.starterSeed = starterSeed;
    }

    public Action play(char grid[][]) {
        return null;
    }
}

class AI extends Player {
    private int depth;
    private int movesOrdering;

    public AI(char mySeed, int depth, int movesOrdering) {
        this.mySeed = mySeed;
        this.depth = depth;
        this.movesOrdering = movesOrdering;
    }

    @Override
    public Action play(char[][] grid) {
        Action nextMove;

        while (true) {
            int[] aiMove = Util.heuristicMinMax(grid, depth, starterSeed, mySeed, movesOrdering);
            int row = aiMove[0];
            int column = aiMove[1];

            if (Util.isLegalMove(grid, column, row)) {
                nextMove = new Action(row, column, mySeed);
                break;
            }

            LOGGER.warning("Wrong AI input value '" + aiMove + "' repeat please");
        }

        return nextMove;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}

class Human extends Player {

    public Human(char mySeed) {
        this.mySeed = mySeed;
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
                }
            }

            LOGGER.warning("Wrong player X input value '" + input + "' repeat please");
        }

        return nextMove;
    }
}


