package games.connectfour;

import java.util.Scanner;

public class Engine {

    private Scanner scanner;

    private String[][] grid = new String[6][7];

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
                this.grid[i][j] = "_";
            }
        }
    }

    public void play(){
        System.out.println("Input your next move (eg. 0-6) after each board");

        Agent ai = new Agent(player);

        while (!checkAndDeclareWinner(grid)) {

            System.out.println(this);
            String input =  scanner.next();
            int column;

            if (!input.matches("^\\d$")) {
                System.err.println("Wrong input format '" + input + "' repeat please");
                continue;
            }else {
                column = Integer.parseInt(input);

                if (isLegalMove(grid, column)) {
                    for(int i = 0;i < 6;i++) {
                        if(grid[i][column].equals("_")) {
                            grid[i][column] = player == 0 ? "O" : "X";
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
        }

        System.out.println(this);
        System.out.println(winningMessage);
    }

    public boolean checkAndDeclareWinner(String[][] grid){
        /*
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
*/

        //Controllo le diagonali
        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int[] d : directions) {
            int dx = d[0];
            int dy = d[1];
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    int lastx = x + 3*dx;
                    int lasty = y + 3*dy;
                    if (0 <= lastx && lastx < 6 && 0 <= lasty && lasty < 7) {
                        String w = grid[x][y];
                        if (!w.equals("_") && w.equals(grid[x+dx][y+dy]) && w.equals(grid[x+2*dx][y+2*dy]) && w.equals(grid[lastx][lasty])) {
                            winningMessage = "Player " + w + " won the game";
                            return true;
                        }
                    }
                }
            }
        }

        //Controllo che la grid sia full e senza vincitori(parità)
        if (turns == (7*6))             return true;
        return false;
    }

    public boolean isLegalMove(String[][] grid,int column){
        return column >= 0 && column <= 6 && grid[5][column].equals("_");
    }

    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n---------- turn " + this.turns + " (next to play " + (player == 0 ? "0" : "X") + ")\n");

        for(int i = 5; i >= 0; i--){
            stringBuilder.append("|");
            for(int j = 0; j <= 6;j++){
                stringBuilder.append(grid[i][j] + "|");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("-");
        for(int i = 0; i <= 6; i++){
            stringBuilder.append(i + "-");
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}
