package games.connectfour;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Engine {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private int nextToPlay;
    private String winningMessage;
    private int gameOverChecks;

    public int getGameOverChecks() {
        return gameOverChecks;
    }

    public void playNewGame(int starter, int mod, List<Integer> depthsOfSearch) {

        gameOverChecks = -1;
        winningMessage = "It's draw, there is no winner";
        nextToPlay = starter;

        Game game = new Game(starter, mod, depthsOfSearch);

        while (gameOverChecks < 0) {

            game.doNextTurn();

            gameOverChecks = Util.gameOverChecks(game.getGrid(), game.getTurns(), game.getPlayers().get(0).getMySeed(), game.getPlayers().get(1).getMySeed());
        }

        LOGGER.warning(Util.boardToString(game.getGrid(), game.getTurns(), "*** GAME OVER ***"));

        if (gameOverChecks == 2) {
            LOGGER.info(winningMessage);
        }
        else {
            LOGGER.info("Player " + game.getPlayers().get(gameOverChecks).getMySeed() + " won the game");
        }
    }
}