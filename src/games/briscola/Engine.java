package games.briscola;

import main.GameType;

import java.util.List;
import java.util.logging.Logger;

public class Engine {

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());

    public int playNewGame(int starter, GameType gameType, List<Integer> depthsOfSearch) {

        LOGGER.info("Starting new game of briscola");

        boolean isGameOver = false;

        Game game = new Game(starter, gameType, depthsOfSearch);

        do {
            LOGGER.info(game.toString());

            int nextPlayer = game.getNextPlayer();

            if (nextPlayer == -1) isGameOver = true;
            else game.doNextTurn();

        } while (!isGameOver);

        int winner = Util.getGameWinner(game.getPlayers());

        if (winner == -1) {
            LOGGER.info("Game ended in a DRAW.");
        } else {
            LOGGER.info("Player " + game.getPlayers().get(winner).getName() + " won the game (briscole picked:\n " + game.getPlayers().get(winner).getBriscole());

        }

        return winner;
    }

}
