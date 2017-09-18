package games.tictactoe;

import java.util.Scanner;

public class Engine {

    public Engine() {
    }

    public void playNewGame(int starter, int mod) {
        System.out.println("Start a new game...");

        Game game = new Game(starter, mod);

        int result = -2;

        while (result == -2) {
            System.out.println(game);

            game.doNextTurn();

            result = Util.isEnded(game.getGrid());
        }
        System.out.println(game);

        if (result == -1) {
            System.out.println("Game ends in draw.");
        } else {
            System.out.println("Player " + result + " won the game.");
        }
    }
}
