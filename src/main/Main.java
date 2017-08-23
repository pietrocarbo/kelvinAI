package main;

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

        games.connectfour.Engine connect4;
        games.tictactoe.Engine tictactoe;
        games.briscola.BriscolaEngine briscola;

        globalLoggingConfig(Level.WARNING);

        int gameType = 3;
        switch (gameType) {
            case 1:
                tictactoe = new games.tictactoe.Engine(1);  // 0 for AI to start, (1 or) other int for Human to start
                tictactoe.playHumanVsAI();
                break;
            case 2:
                connect4 = new games.connectfour.Engine(1);
                connect4.playHumanVsAI();
                break;
            case 3:
                int nOfGames = 10, winAI1 = 0, winAI2 = 0, draws = 0, depthAI1 = 1, depthAI2 = 10;
                TimeWatch timer = TimeWatch.start();
                for(int i = 0; i < nOfGames; i++) {
                    connect4 = new games.connectfour.Engine(1);
                    switch (connect4.playAIVsAI(depthAI1,depthAI2)){
                        case 0: winAI1++;   break;
                        case 1: winAI2++;   break;
                        case 2: draws++;     break;
                    }
                }
                LOGGER.warning("\n" + ANSI_BLUE + "AI 1 (depth " + depthAI1 + ") won " + winAI1 + " games, ratio:  " + (winAI1*100.0)/nOfGames +
                                            "%\nAI 2 (depth " + depthAI2 + ") won " + winAI2 + " games, ratio:  " + (winAI2*100.0)/nOfGames +
                                            "%\nDraws: " + draws + ", ratio: " +  (draws*100.0)/nOfGames + "%" +
                                            "\nTime elapsed: " + timer.time(TimeUnit.SECONDS) + " seconds");
                break;
            case 10:
                briscola = new games.briscola.BriscolaEngine();
                briscola.play1vsAI();
                break;
        }
    }

}
