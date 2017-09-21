package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
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
        List<Integer> searchParameters = new ArrayList<>();

        globalLoggingConfig(Level.INFO);

        int numberOfGames = 100, gameToPlay = 17;

//        Scanner scanner = new Scanner(System.in);
//        do {
//            System.out.println("Enter the number of the game modality you want to play from this list: \n" +
//                    "0. Human vs Human (Tic Tac Toe) \n" +
//                    "1. Human vs AI minmax (Tic Tac Toe) \n" +
//                    "2. AI minmax vs AI minmax (Tic Tac Toe) \n" +
//                    "3. Human vs Human (Connect 4) \n" +
//                    "4. Human vs AI minmax (Connect 4) \n" +
//                    "5. Human vs AI Monte Carlo tree search (Connect 4) \n" +
//                    "6. AI minmax vs AI minmax with different depth and move ordering (Connect 4) \n" +
//                    "7. AI Monte Carlo tree search vs AI minmax (Connect 4) \n" +
//                    "8. Human vs Human (Briscola) \n" +
//                    "9. Human vs AI rule-based (Briscola) \n" +
//                    "10. Human vs AI minmax (Briscola) \n" +
//                    "11. AI minmax vs AI rule-based (Briscola) \n" +
//                    "12. AI minmax vs AI random choice (Briscola) \n" +
//                    "13. AI hybrid vs AI minmax (Briscola) \n" +
//                    "14. AI hybrid vs AI rule-based (Briscola) \n" +
//                    "15. AI hybrid vs AI random choice (Briscola) \n" +
//                    "16. AI hybrid vs AI hybrid with different depth and determinated deals (Briscola) \n" +
//                    "17. AI hybrid vs Human (Briscola)");
//
//            gameToPlay = scanner.nextInt();
//        } while (gameToPlay < 0 || gameToPlay > 17);
//
//        System.out.println("Enter the number of matches to play: ");
//        numberOfGames = scanner.nextInt();

        int player0Victories = 0, player1Victories = 0, draws = 0, winner = -2;
        TimeWatch timer = TimeWatch.start();

        for (int i = 0; i < numberOfGames; i++) {
            switch (gameToPlay) {

                /*  ---------- Tic Tac Toe ---------- */
                case 0:
                    winner = tictactoe.playNewGame(0, GameType.HUMAN__VS__HUMAN);
                    break;

                case 1:
                    winner = tictactoe.playNewGame(1, GameType.HUMAN__VS__AI_MINMAX); // if(starter == 0) then AI start else Human start //
                    break;

                case 2:
                    winner = tictactoe.playNewGame(0, GameType.AI_MINMAX__VS__AI_MINMAX);
                    break;


                /*  ---------- Connect 4 ---------- */
                case 3:
                    winner = connect4.playNewGame(0, GameType.HUMAN__VS__HUMAN, searchParameters, movesOrderings);
                    break;

                case 4:
                    movesOrderings.add(MovesOrdering.MIDDLE_FIRST);
                    searchParameters.add(7);

                    winner = connect4.playNewGame(0, GameType.HUMAN__VS__AI_MINMAX, searchParameters, movesOrderings);
                    break;

                case 5:
                    movesOrderings.add(MovesOrdering.RANDOM);
                    searchParameters.add(1000);  // Monte Carlo iterations

                    winner = connect4.playNewGame(0, GameType.HUMAN__VS__AI_MCTS, searchParameters, movesOrderings);
                    break;

                case 6:
                    movesOrderings.add(MovesOrdering.STANDARD);
                    searchParameters.add(3);
                    movesOrderings.add(MovesOrdering.MIDDLE_FIRST);
                    searchParameters.add(5);

                    winner = connect4.playNewGame(1, GameType.AI_MINMAX__VS__AI_MINMAX, searchParameters, movesOrderings);
                    break;

                case 7:
                    movesOrderings.add(MovesOrdering.RANDOM);
                    searchParameters.add(1000);  // Monte Carlo iterations

                    movesOrderings.add(MovesOrdering.STANDARD);
                    searchParameters.add(0);

                    winner = connect4.playNewGame(0, GameType.AI_MCTS__VS__AI_MINMAX, searchParameters, movesOrderings);
                    break;

                    /*  ---------- Briscola ---------- */
                case 8:
                    winner = briscola.playNewGame(0, GameType.HUMAN__VS__HUMAN, searchParameters);
                    break;

                case 9:
                    winner = briscola.playNewGame(0, GameType.HUMAN__VS__AI_RULE, searchParameters);
                    break;

                case 10:
                    searchParameters.add(7);  // depth
                    searchParameters.add(200);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.HUMAN__VS__AI_MINMAX, searchParameters);
                    break;

                case 11:
                    searchParameters.add(5);  // depth
                    searchParameters.add(100);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.AI_MINMAX__VS__AI_RULE, searchParameters);
                    break;

                case 12:
                    searchParameters.add(5);  // depth
                    searchParameters.add(100);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.AI_MINMAX__VS__AI_RANDOM, searchParameters);
                    break;

                case 13:
                    searchParameters.add(5);  // depth
                    searchParameters.add(100);  // random deals to search
                    searchParameters.add(5);  // depth
                    searchParameters.add(100);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.AI_HYBRID__VS__AI_MINMAX, searchParameters);
                    break;

                case 14:
                    searchParameters.add(5);  // depth
                    searchParameters.add(100);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.AI_HYBRID__VS__AI_RULE, searchParameters);
                    break;

                case 15:
                    searchParameters.add(5);  // depth
                    searchParameters.add(100);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.AI_HYBRID__VS__AI_RANDOM, searchParameters);
                    break;

                case 16:
                    searchParameters.add(8);  // depth
                    searchParameters.add(200);  // random deals to search
                    searchParameters.add(3);  // depth
                    searchParameters.add(100);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.AI_HYBRID__VS__AI_HYBRID, searchParameters);
                    break;

                case 17:
                    searchParameters.add(10);  // depth
                    searchParameters.add(250);  // random deals to search
                    winner = briscola.playNewGame(0, GameType.HUMAN__VS__AI_HYBRID, searchParameters);
                    break;
            }

            LOGGER.warning("Game n." + i + " ended with winner " + winner);
            switch (winner) {
                case 0:
                    player0Victories++;
                    break;
                case 1:
                    player1Victories++;
                    break;
                default:
                    draws++;
                    break;
            }

        }

        System.out.println("\n" + ANSI_BLUE + "Player 0 won: " + player0Victories + " games (ratio  " + (player0Victories * 100.0) / numberOfGames + "%)" +
                "\nPlayer 1 won: " + player1Victories + " games (ratio " + (player1Victories * 100.0) / numberOfGames + "%)" +
                "\nDraws: " + draws + " (ratio " + draws + "%)" +
                "\nTime elapsed: " + timer.time(TimeUnit.SECONDS) + " seconds" + ANSI_NO_COLOR);
    }
}