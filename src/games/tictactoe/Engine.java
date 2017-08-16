package games.tictactoe;

import java.util.Scanner;

public class Engine {

    private Scanner scanner;

    private String[][] grid = new String[3][3];

    private int turns;

    private int player;

    private String winningMessage;

    public Engine(int starter) {
        this.scanner = new Scanner(System.in);
        this.player = starter == 0 ? 0 : 1;  // AI is 0, Human is 1
        this.turns = 0;
        this.winningMessage = "It's draw, there is no winner";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.grid[i][j] = " ";
            }
        }
    }

    public void play() {
        System.out.println("Input your next move (eg. 0,0) after each board");

        Agent ai = new Agent(player);

        while (!checkAndDeclareWinner(grid)) {

            System.out.println(this);
            Integer row, column;

            if (player == 0) {
                int[] aiMove = ai.ply(grid);
                row = aiMove[0];
                column = aiMove[1];
                System.out.println("[ai-metrics] expanded " + aiMove[2] + " nodes for current move");

                if (isLegalMove(grid, row, column)) {
                    grid[row][column] = "O";
                    player = 1 - player;
                    turns++;
                } else {
                    System.err.println("Wrong AI input value '" + aiMove + "' repeat please");
                    continue;
                }

            } else {
                String input = scanner.next();
                if (!input.matches("^\\d,\\d$")) {
                    System.err.println("Wrong input format '" + input + "' repeat please");
                    continue;
                } else {
                    row = Integer.parseInt(input.substring(0, 1));
                    column = Integer.parseInt(input.substring(2));
                }

                if (isLegalMove(grid, row, column)) {
                    grid[row][column] = "X";
                    player = 1 - player;
                    turns++;
                } else {
                    System.err.println("Wrong input value '" + input + "' repeat please");
                    continue;
                }

            }

        }

        System.out.println(this);
        System.out.println(winningMessage);
    }

    public boolean isLegalMove (String[][] grid, int row, int column) {
        return row >= 0 && row <= 2 && column >= 0 && column <= 2 && grid[row][column].equals(" ");
    }

    public boolean checkAndDeclareWinner (String[][] grid) {

        if (declareThree(grid, 0,0, 0, 1, 0,2))   return true;
        if (declareThree(grid, 1,0, 1, 1, 1,2))   return true;
        if (declareThree(grid, 2,0, 2, 1, 2,2))   return true;

        if (declareThree(grid, 0,0, 1, 0, 2,0))   return true;
        if (declareThree(grid, 0,1, 1, 1, 2,1))   return true;
        if (declareThree(grid, 0,2, 1, 2, 2,2))   return true;

        if (declareThree(grid, 0,0, 1, 1, 2,2))   return true;
        if (declareThree(grid, 2,0, 1, 1, 0,2))   return true;

        if (turns == 9)             return true;  // se nessuno ha vinto ma il campo è pieno

        return false;
    }

    public boolean declareThree (String[][] grid, int r1, int c1, int r2, int c2, int r3, int c3) {
        String s1 = grid[r1][c1];
        String s2 = grid[r2][c2];
        String s3 = grid[r3][c3];
        int check = checkThree(s1, s2, s3);
        if (check == 0) {
            this.winningMessage = "Player O won in " + r1 + "," + c1 + " - " + r2 + "," + c2 + " - " + r3 + "," + c3;
            return true;
        } else if (check == 1) {
            this.winningMessage = "Player X won in " + r1 + "," + c1 + " - " + r2 + "," + c2 + " - " + r3 + "," + c3;
            return true;
        }
        return false;
    }

    public int checkThree (String e1, String e2, String e3) {
        String test = e1 + e2 + e3;
        if (test.equals("OOO")) return 0;
        else if (test.equals("XXX")) return 1;
        else return -1;
    }

    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n---------- turn " + this.turns + " ---------\n");
        for (String[] row : grid) {
            stringBuilder.append("|");
            for (String element : row) {
                stringBuilder.append(element + "|");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
