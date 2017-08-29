package games.connectfourP;

import java.util.Scanner;
import java.util.logging.Logger;

public class Engine {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private Scanner scanner;
    private char[][] grid = new char[6][7];
    private int turns;
    private char playing;
    private String winningMessage;

    public Engine() {
        this.scanner = new Scanner(System.in);
        this.turns = 0;
        this.winningMessage = "It's draw, there is no winner";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.grid[i][j] = '_';
            }
        }
    }

    private void reset(char starter) {
        this.turns = 0;
        this.playing = starter;  // AI is 0, Human is 1
        this.winningMessage = "It's draw, there is no winner";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.grid[i][j] = '_';
            }
        }
    }

    private void togglePlayer() {
        this.playing = (this.playing == 'O' ? 'X' : 'O');
    }

    public void playHumanVsAI(char starterSeed, char aiSeed, int search_depth, int movesOrdering) {
        reset(starterSeed);

        LOGGER.info("Input your next move (eg. 0-6) after each board");
        Agent ai = new Agent(aiSeed, starterSeed);
        int row, column, gameOverChecks = -1;

        while (gameOverChecks < 0) {
            LOGGER.info(Game.boardToString(grid, turns,  "-> next to play " + playing));

            if (playing == 'O') {
                int[] aiMove = ai.heuristicMinMax(grid, search_depth, movesOrdering);
                row = aiMove[0];
                column = aiMove[1];

                if (isLegalMove(grid, column, row)) {
                    grid[row][column] = 'O';
                    togglePlayer();
                    turns++;
                } else {
                    LOGGER.warning("Wrong AI input value '" + aiMove + "' repeat please");
                    continue;
                }

            } else {
                String input = scanner.next();

                if (!input.matches("^\\d$")) {
                    LOGGER.warning("Wrong player X input format '" + input + "' repeat please");
                    continue;
                } else {

                    column = Integer.parseInt(input);
                    row = -1;
                    for (int i = 0; i < 6; i++) {
                        if (grid[i][column] == '_') {
                            row = i;
                            break;
                        }
                    }

                    if (isLegalMove(grid, column, row)) {
                        grid[row][column] = 'X';
                        togglePlayer();
                        turns++;
                    } else {
                        LOGGER.warning("Wrong player X input value '" + input + "' repeat please");
                        continue;
                    }

                }
            }
            gameOverChecks = Game.gameOverChecks(grid, turns);
        }

        LOGGER.info(Game.boardToString(grid, turns, "*** GAME OVER ***"));
        if (gameOverChecks == 2)
            LOGGER.info(winningMessage);
        else if (gameOverChecks == 0)
            LOGGER.info("Player O won the game");
        else if (gameOverChecks == 1)
            LOGGER.info("Player X won the game");
    }

    public int playAIVsAI(int depthAI1, int movesOrderingAI1, int depthAI2, int movesOrderingAI2) {
        reset('O');

        Agent ai1 = new Agent('O', 'O');
        Agent ai2 = new Agent('X', 'O');
        int row, column, gameOverChecks = -1;

        while (gameOverChecks < 0) {
            LOGGER.fine(Game.boardToString(grid, turns, "-> next to play " + playing));

            if (playing == 'O') {
                int[] ai1Move = ai1.heuristicMinMax(grid, depthAI1, movesOrderingAI2);
                row = ai1Move[0];
                column = ai1Move[1];

                if (isLegalMove(grid, column, row)) {
                    grid[row][column] = 'O';
                    togglePlayer();
                    turns++;
                } else {
                    LOGGER.severe("Wrong AI(1) input value '" + ai1Move + "' repeat please");
                    continue;
                }

            } else {
                int[] ai2Move = ai2.heuristicMinMax(grid, depthAI2, movesOrderingAI2);
                row = ai2Move[0];
                column = ai2Move[1];

                if (isLegalMove(grid, column, row)) {
                    grid[row][column] = 'X';
                    togglePlayer();
                    turns++;
                } else {
                    LOGGER.severe("Wrong AI(2) input value '" + ai2Move + "' repeat please");
                    continue;
                }
            }
            gameOverChecks = Game.gameOverChecks(grid, turns);
        }


        LOGGER.fine(Game.boardToString(grid, turns, "*** GAME OVER ***"));
        if (gameOverChecks == 2)
            LOGGER.fine(winningMessage);
        else if (gameOverChecks == 0)
            LOGGER.fine("Player O(AI 1) won the game");
        else if (gameOverChecks == 1)
            LOGGER.fine("Player X(AI 2) won the game");

        return gameOverChecks;
    }


    public boolean isLegalMove(char[][] grid, int column, int row) {
        return column >= 0 && column <= 6 && grid[5][column] == '_' && grid[row][column] == '_';
    }

    /*
    public void playHumanVsHuman(int starter) {
        reset(starter);

        LOGGER.info("Input your next move (eg. 0-6) after each board");
        int gameOverChecks = -1;

        while (gameOverChecks < 0) {

            LOGGER.info(Engine.boardToString(grid, turns, player));
            String input = scanner.next();
            int column;

            if (!input.matches("^\\d$")) {
                LOGGER.warning("Wrong input format '" + input + "' repeat please");
                continue;

            } else {
                column = Integer.parseInt(input);

                if (isLegalMove(grid, column)) {
                    for (int i = 0; i < 6; i++) {
                        if (grid[i][column] == '_') {
                            grid[i][column] = player == 0 ? 'O' : 'X';
                            player = 1 - player;
                            turns++;
                            break;
                        }
                    }
                } else {
                    LOGGER.warning("Wrong input value '" + input + "' repeat please");
                    continue;
                }
            }
            gameOverChecks = Game.gameOverChecks(grid, turns);
        }


        LOGGER.info(Engine.boardToString(grid, turns, -1));
        if (gameOverChecks == 2)
            LOGGER.info(winningMessage);
        else if (gameOverChecks == 0)
            LOGGER.info("Player O won the game");
        else if (gameOverChecks == 1)
            LOGGER.info("Player X won the game");
    }
*/

}