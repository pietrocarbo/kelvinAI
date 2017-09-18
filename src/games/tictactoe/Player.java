package games.tictactoe;

import java.util.Scanner;

abstract class Player {

    int ID;
    char seed;

    public Player(int ID, char seed) {
        this.ID = ID;
        this.seed = seed;
    }

    public Action play(char[][] grid) {
        return null;
    }

    public Action play(char[][] grid, char oppositeSeed) {
        return null;
    }

    public int getID() {
        return ID;
    }

    public char getSeed() {
        return seed;
    }
}

class AI extends Player {

    private char starter;

    public AI(int ID, char seed, char starter) {
        super(ID, seed);
        this.starter = starter;
    }

    public Action play(char[][] grid, char oppositeSeed) {

        return Util.minMaxAlg(grid, seed, oppositeSeed, starter);
    }
}

class Human extends Player {

    public Human(int ID, char seed) {
        super(ID, seed);
    }

    @Override
    public Action play(char[][] grid) {
        Scanner scanner = new Scanner(System.in);

        int row, column;

        while (true) {
            String input = scanner.next();
            if (!input.matches("^\\d,\\d$")) {
                System.err.println("Wrong input format '" + input + "' repeat please");
                continue;
            } else {
                row = Integer.parseInt(input.substring(0, 1));
                column = Integer.parseInt(input.substring(2));
            }

            if (Util.isLegalMove(grid, row, column)) {
                break;
            } else {
                System.err.println("Wrong input value '" + input + "' repeat please");
                continue;
            }
        }

        return new Action(row, column, seed);
    }
}