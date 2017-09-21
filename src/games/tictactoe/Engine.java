package games.tictactoe;

import main.GameType;

import java.util.logging.Logger;

public class Engine {

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());

    public int playNewGame(int starter, GameType mod) {

        LOGGER.info("Starting new game of tictactoe");

        Game game = new Game(starter, mod);

        int result = -2;

        while (result == -2) {
            LOGGER.info(game.toString());

            game.doNextTurn();

            result = Util.isGameOver(game.getGrid());
        }
        LOGGER.info("Game ended with board " + game.toString());

        if (result == -1) {
            System.out.println("Game ended in a DRAW.");
        } else {
            System.out.println("Player " + result + " won the game.");
        }

        return result;
    }
}
