package games.connectfour;

import main.GameType;
import main.MovesOrdering;

import java.util.List;
import java.util.logging.Logger;

public class Engine {

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());

    public int playNewGame(int starter, GameType mod, List<Integer> depthsOfSearch, List<MovesOrdering> movesOrdering) {

        LOGGER.info("Starting new game of connect 4");

        int gameOverChecks = -1;

        Game game = new Game(starter, mod, depthsOfSearch, movesOrdering);

        while (gameOverChecks < 0) {

            game.doNextTurn();

            gameOverChecks = Util.isGameOver(game.getGrid(), game.getTurns(), game.getPlayers().get(0).getMySeed(), game.getPlayers().get(1).getMySeed());
        }

        LOGGER.warning(Util.boardToString(game.getGrid(), game.getTurns(), "*** GAME OVER ***"));

        if (gameOverChecks == 2) {
            LOGGER.info("Game ended in a DRAW.");
            gameOverChecks = -1;
        } else {
            LOGGER.info("Player " + game.getPlayers().get(gameOverChecks).getMySeed() + " won the game.");
        }

        return gameOverChecks;
    }
}