package games.connectfour;

import java.util.Scanner;

public class Engine {

    private Scanner scanner;

    private char[][] grid = new char[6][7];
    private int turns;
    private int player;
    private String winningMessage;

    public Engine(int starter) {
        this.scanner = new Scanner(System.in);
        this.player = starter == 0 ? 0 : 1;  // AI is 0, Human is 1
        this.turns = 0;
        this.winningMessage = "It's draw, there is no winner";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.grid[i][j] = '_';
            }
        }
    }

    public void playHumanVsHuman() {
        System.out.println("Input your next move (eg. 0-6) after each board");
        int gameOverChecks = -1;

        while (gameOverChecks < 0) {

            Engine.printBoard(grid, turns, (player == 0 ? "0" : "X"));
            String input = scanner.next();
            int column;

            if (!input.matches("^\\d$")) {
                System.err.println("Wrong input format '" + input + "' repeat please");
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
                    System.err.println("Wrong input value '" + input + "' repeat please");
                    continue;
                }
            }
            gameOverChecks = Game.gameOverChecks(grid, turns);
        }


        Engine.printBoard(grid, turns, "END BOARD");
        if (gameOverChecks == 2)
            System.out.println(winningMessage);
        else if (gameOverChecks == 0)
            System.out.println("Player O won the game");
        else if (gameOverChecks == 1)
            System.out.println("Player X won the game");
    }

    public void playHumanVsAI() {

        System.out.println("Input your next move (eg. 0-6) after each board");
        Agent ai = new Agent(player,1);
        int row, column, gameOverChecks = -1;

        while (gameOverChecks < 0) {
            Engine.printBoard(grid, turns, (player == 0 ? "0" : "X"));

            if (player == 0) {
                int[] aiMove = ai.ply(grid);
                row = aiMove[0];
                column = aiMove[1];

                if (isLegalMove(grid, column)) {
                    grid[row][column] = 'O';
                    player = 1 - player;
                    turns++;
                } else {
                    System.err.println("Wrong AI input value '" + aiMove + "' repeat please");
                    continue;
                }

            } else {
                String input = scanner.next();

                if (!input.matches("^\\d$")) {
                    System.err.println("Wrong input format '" + input + "' repeat please");
                    continue;
                } else {
                    column = Integer.parseInt(input);

                    if (isLegalMove(grid, column)) {
                        for (int i = 0; i < 6; i++) {
                            if (grid[i][column] == '_') {
                                grid[i][column] = 'X';
                                player = 1 - player;
                                turns++;
                                break;
                            }
                        }
                    } else {
                        System.err.println("Wrong human input value '" + input + "' repeat please");
                        continue;
                    }
                }
            }
            gameOverChecks = Game.gameOverChecks(grid, turns);
        }


        Engine.printBoard(grid, turns, "END BOARD");
        if (gameOverChecks == 2)
            System.out.println(winningMessage);
        else if (gameOverChecks == 0)
            System.out.println("Player O won the game");
        else if (gameOverChecks == 1)
            System.out.println("Player X won the game");
    }

    public int playAIVsAI(int depthAI1, int depthAI2) {

        //System.out.println("Input your next move (eg. 0-6) after each board");
        Agent ai1 = new Agent(0, depthAI1);
        Agent ai2 = new Agent(0, depthAI2);
        int row, column, gameOverChecks = -1;

        while (gameOverChecks < 0) {
            Engine.printBoard(grid, turns, (player == 0 ? "0" : "X"));

            if (player == 0) {
                int[] ai1Move = ai1.ply(grid);
                row = ai1Move[0];
                column = ai1Move[1];

                if (isLegalMove(grid, column)) {
                    grid[row][column] = 'O';
                    player = 1 - player;
                    turns++;
                } else {
                    System.err.println("Wrong AI(1) input value '" + ai1Move + "' repeat please");
                    continue;
                }

            } else {
                int[] ai2Move = ai2.ply(grid);
                row = ai2Move[0];
                column = ai2Move[1];

                if (isLegalMove(grid, column)) {
                    grid[row][column] = 'X';
                    player = 1 - player;
                    turns++;
                } else {
                    System.err.println("Wrong AI(2) input value '" + ai2Move + "' repeat please");
                    continue;
                }
            }
            gameOverChecks = Game.gameOverChecks(grid, turns);
        }


        Engine.printBoard(grid, turns, "END BOARD");
        if (gameOverChecks == 2)
            System.out.println(winningMessage);
        else if (gameOverChecks == 0)
            System.out.println("Player O(AI 1) won the game");
        else if (gameOverChecks == 1)
            System.out.println("Player X(AI 2) won the game");

        return gameOverChecks;
    }


    public boolean isLegalMove(char[][] grid, int column){
        return column >= 0 && column <= 6 && grid[5][column] == '_';
    }


    public static void printBoard (char[][] grid, int turns, String player) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n---- turn " + turns + " (next to play " + player + ") ----\n");

        for(int i = 5; i >= 0; i--){
            stringBuilder.append("|");
            for(int j = 0; j <= 6;j++){
                stringBuilder.append(grid[i][j] + "|");
            }
            stringBuilder.append("\t\t " + i + ".\n");
        }

        stringBuilder.append("\n.");
        for (int i = 0; i <= 6; i++){
            stringBuilder.append(i + ".");
        }
        stringBuilder.append("\n");

        System.out.println(stringBuilder.toString());
    }
}

    /* public boolean oldCheckAndDeclareWinner (char[][] grid) {
        int occurrences = 0;
        String lastSeed = "_";

        //Controllo per righe
        for(int i = 0; i <= 5; i++){
            for(int j = 0; j <= 6 || ((7 - j) >= (4 - occurrences)); j++){
                if(grid[i][j].equals(lastSeed) && !lastSeed.equals("_")){
                    occurrences++;
                    if(occurrences == 4){
                        winningMessage = "Player " + lastSeed + " won(by row) the game";
                        return true;
                    }
                }else{
                    if(grid[i][j].equals("_")){
                        occurrences = 0;
                    }else {
                        occurrences = 1;
                    }
                    lastSeed = grid[i][j];
                }
            }
            occurrences = 0;
            lastSeed = "_";
        }

        occurrences = 0;
        lastSeed = "_";

        //Controllo per colonne
        for(int j = 0; j <= 6; j++){
            for(int i = 0; i <= 5 || ((6 - i) >= (4 - occurrences)); i++){
                if(grid[i][j].equals(lastSeed) && !lastSeed.equals("_")){
                    occurrences++;
                    if(occurrences == 4){
                        winningMessage = "Player " + lastSeed + " won(by column) the game";
                        return true;
                    }
                }else{
                    if(grid[i][j].equals("_")){
                        occurrences = 0;
                        break;
                    }else {
                        occurrences = 1;
                    }
                    lastSeed = grid[i][j];
                }
            }
            occurrences = 0;
            lastSeed = "_";
        }
}*/