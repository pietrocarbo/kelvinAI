package main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
//    Levels of logging importance in descending order
//    SEVERE
//    WARNING
//    INFO
//    CONFIG
//    FINE
//    FINER
//    FINEST  (ALL)

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

        games.tictactoe.Engine tictactoe = new games.tictactoe.Engine();
        games.connectfour.Engine connect4 = new games.connectfour.Engine();
        games.briscola.Engine briscola = new games.briscola.Engine();


        List<MovesOrdering> movesOrderings = new ArrayList<>();
        List<Integer> depthsOfSearch = new ArrayList<>();

        globalLoggingConfig(Level.FINE);

        int gameToPlay = 6;
        switch (gameToPlay) {

            /*  ---------- Tic Tac Toe ---------- */
            case 0:
                tictactoe.playNewGame(0, GameType.HUMAN__VS__HUMAN);
                break;

            case 1:
                tictactoe.playNewGame(1, GameType.HUMAN__VS__AI_MINMAX); // if(starter == 0) then AI start else Human start //
                break;

            case 2:
                tictactoe.playNewGame(0, GameType.AI_MINMAX__VS__AI_MINMAX);
                break;


            /*  ---------- Connect 4 ---------- */
            case 3:
                connect4.playNewGame(0, GameType.HUMAN__VS__HUMAN, depthsOfSearch, movesOrderings);
                break;

            case 4:
                movesOrderings.add(MovesOrdering.MIDDLE_FIRST);
                depthsOfSearch.add(5);

                connect4.playNewGame(0, GameType.HUMAN__VS__AI_MINMAX, depthsOfSearch, movesOrderings);
                break;

            case 5:
                movesOrderings.add(MovesOrdering.RANDOM);
                depthsOfSearch.add(1000);  // Monte Carlo iterations

                connect4.playNewGame(0, GameType.HUMAN__VS__AI_MCTS, depthsOfSearch, movesOrderings);
                break;

            case 6:
                movesOrderings.add(MovesOrdering.STANDARD);
                depthsOfSearch.add(3);
                movesOrderings.add(MovesOrdering.MIDDLE_FIRST);
                depthsOfSearch.add(5);

                connect4.playNewGame(1, GameType.AI_MINMAX__VS__AI_MINMAX, depthsOfSearch, movesOrderings);
                break;

            case 7:
                movesOrderings.add(MovesOrdering.RANDOM);
                depthsOfSearch.add(1000);

                movesOrderings.add(MovesOrdering.STANDARD);
                depthsOfSearch.add(0);

                connect4.playNewGame(0, GameType.AI_MCTS__VS__AI_MINMAX, depthsOfSearch, movesOrderings);
                break;

                /*  ---------- Briscola ---------- */
//            case 7:
//                int nOfGames = 50, winAI0 = 0, winAI1 = 0, draws = 0, depthAI0 = 500, depthAI1 = 3;
//                TimeWatch timer = TimeWatch.start();
//
//                for(int i = 0; i < nOfGames; i++) {
//                    connect4.playNewGame(0, 4, Arrays.asList(depthAI0, depthAI1), MovesOrdering.MIDDLE_FIRST);
//
//                    switch (connect4.getGameOverChecks()) {
//                        case 0: winAI0++;   break;
//                        case 1: winAI1++;   break;
//                        case 2: draws++;    break;
//                    }
//                }
//                LOGGER.severe("\n" + ANSI_BLUE + "AI 0 (depth " + depthAI0 + ") won " + winAI0 + " games, ratio:  " + (winAI0*100.0)/nOfGames +
//                                            "%\nAI 1 (depth " + depthAI1 + ") won " + winAI1 + " games, ratio:  " + (winAI1*100.0)/nOfGames +
//                                            "%\nDraws: " + draws + ", ratio: " +  (draws*100.0)/nOfGames + "%" +
//                                            "\nTime elapsed: " + timer.time(TimeUnit.SECONDS) + " seconds" + ANSI_NO_COLOR);
//                break;
//
//            case 8:
//                briscola.playNewGame(3, 50);
//                break;
        }
    }

}
