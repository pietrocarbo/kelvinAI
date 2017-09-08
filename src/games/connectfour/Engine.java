package games.connectfour;

import java.util.Scanner;
import java.util.logging.Logger;

public class Engine {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private int nextToPlay;
    private String winningMessage = "It's draw, there is no winner";
    private int gameOverChecks = -1;
    private int depth1, depth2;


    public Engine() {
    }

    public int getGameOverChecks() {
        return gameOverChecks;
    }

    public void setDepth1(int depth1) {
        this.depth1 = depth1;
    }

    public void setDepth2(int depth2) {
        this.depth2 = depth2;
    }

    public void playNewGame(int starter, int mod) {

        nextToPlay = starter;

        Game game = new Game(starter, mod);

        game.setDepths(depth1, depth2);

        while (gameOverChecks < 0) {

            game.doNextTurn();

            gameOverChecks = Util.gameOverChecks(game.getGrid(), game.getTurns(), game.getPlayers().get(0).getMySeed(), game.getPlayers().get(1).getMySeed());
        }

        LOGGER.info(Util.boardToString(game.getGrid(), game.getTurns(), "*** GAME OVER ***"));

        if (gameOverChecks == 2) LOGGER.info(winningMessage);
        else LOGGER.info("Player " + game.getPlayers().get(gameOverChecks).getMySeed() + " won the game");
    }
}