package games.briscola;

import java.util.logging.Logger;

public class Engine {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public void playNewGame(int gameType, int gamesToPlay) {
        int p1Pts = 0, p2Pts = 0, p1Win = 0, p2Win = 0, draw = 0, gamesLeft = gamesToPlay;

        while (gamesLeft > 0) {

            boolean isGameOver = false;
            Game game = new Game(0, gameType);

            LOGGER.info("New game started");

            do {
                LOGGER.finer(game.toString());
                int nextPlayer = game.getNextPlayer();

                if (nextPlayer == -1) {
                    isGameOver = true;
                } else {
                    game.doNextTurn();
                }
            } while (!isGameOver);

            LOGGER.info("Game ended");

            int winner = Util.getGameWinner(game.getPlayers());

            if (winner == -1) {
                LOGGER.info("Game ended in a draw");
                draw++;
            } else {
                if (winner == 0) {
                    p1Win++;
                } else {
                    p2Win++;
                }
                int tmpP1pts = Util.calculatePoints(game.getPlayers().get(0).getCardsCollected());
                int tmpP2pts =  Util.calculatePoints(game.getPlayers().get(1).getCardsCollected());
                LOGGER.info("P1 " + game.getPlayers().get(0).getName() + " scored " + tmpP1pts + " with "+ game.getPlayers().get(0).getBriscole() + " briscole" + "\n" +
                                 "P2 " + game.getPlayers().get(1).getName() + " scored " + tmpP2pts + " with "+ game.getPlayers().get(1).getBriscole() + " briscole");
                p1Pts += tmpP1pts;
                p2Pts += tmpP2pts;
            }
            gamesLeft--;
        }

        LOGGER.warning("P1 winnned " + (p1Win * 100)/gamesToPlay + "% of the games with " + p1Pts/gamesToPlay + " points on average");
        LOGGER.warning("P2 - Kelvin winned " + (p2Win * 100)/gamesToPlay + "% of the games with " + p2Pts/gamesToPlay + " points on average");
        LOGGER.warning("Draw rate " + (draw * 100)/gamesToPlay + "%");


    }

}
