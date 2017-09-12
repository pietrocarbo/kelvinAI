package main;

import games.briscola.Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
//    Levels of logging importance (descending order)
//    SEVERE
//    WARNING
//    INFO
//    CONFIG
//    FINE
//    FINER
//    FINEST

    private static void globalLoggingConfig (Level logLevel) {
        Logger rootLogger = LogManager.getLogManager().getLogger("");

        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }

        StreamHandler sh = new StreamHandler(System.out, new MyLogFormatter()) {
            @Override
            public synchronized void publish(final LogRecord record) {
                super.publish(record);
                flush();
            }
        };

        rootLogger.addHandler(sh);
        rootLogger.setLevel(logLevel);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(logLevel);
        }
    }

    public static void main(String[] args) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_NO_COLOR = "\u001B[0m";

        games.connectfour.Engine connect4;
        games.tictactoe.Engine tictactoe;
        Engine briscola;

        globalLoggingConfig(Level.SEVERE);

        int gameToPlay = 3;
        switch (gameToPlay) {
            case 1:
                tictactoe = new games.tictactoe.Engine(1);  // 0 for AI to start, 1 for Human to start
                tictactoe.playHumanVsAI();
                break;
            case 2:
                connect4 = new games.connectfour.Engine();
                connect4.playNewGame(0, 2, Arrays.asList(5));
                break;
            case 3:
                int nOfGames = 10, winAI0 = 0, winAI1 = 0, draws = 0, depthAI0 = 350, depthAI1 = 3;
                TimeWatch timer = TimeWatch.start();
                for(int i = 0; i < nOfGames; i++) {
                    connect4 = new games.connectfour.Engine();

                    connect4.playNewGame(0, 4, Arrays.asList(depthAI0, depthAI1));

                    switch (connect4.getGameOverChecks()) {
                        case 0: winAI0++;   break;
                        case 1: winAI1++;   break;
                        case 2: draws++;    break;
                    }
                }
                LOGGER.severe("\n" + ANSI_BLUE + "AI 0 (depth " + depthAI0 + ") won " + winAI0 + " games, ratio:  " + (winAI0*100.0)/nOfGames +
                                            "%\nAI 1 (depth " + depthAI1 + ") won " + winAI1 + " games, ratio:  " + (winAI1*100.0)/nOfGames +
                                            "%\nDraws: " + draws + ", ratio: " +  (draws*100.0)/nOfGames + "%" +
                                            "\nTime elapsed: " + timer.time(TimeUnit.SECONDS) + " seconds");
                break;
            case 10:
                briscola = new Engine();
                briscola.playNewGame(3, 50);
                break;
        }
    }

}
