package games.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private static char seedStarter;

    public Game(char seedStarter) {
        this.seedStarter = seedStarter;
    }

    public static char getPlayer (char[][] board) {

        int plyCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(board[i][j] == 'X') { // Human
                    plyCounter++;
                } else if (board[i][j] == 'O') { // AI
                    plyCounter--;
                }
            }
        }

        return (plyCounter == 0 ? seedStarter :
                    plyCounter > 0 ? 'O' : 'X');  // "O" (return 0) is AI turn, "X" (return 1) is human turn, starter return (-1)
    }

    public static List<Action> getActions (char[][] board) {

        //TODO: Controllare che vengano esplorate tutte le mosse dispobili in modo corretto

        List<Action> result = new ArrayList<Action>();

        for (int j = 0; j < 7; j++) { // for each column
            for (int i = 0; i < 6; i++) { // and then for each row upwards

                if(board[i][j] == '_') { // blank
                    result.add(new Action(i, j, getPlayer(board)));
                    break;
                }
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public static char[][] getResult (char[][] board, Action move) {
        char[][] newBoard = new char[6][7];
        if (move.getColumn() >= 0 && move.getColumn() <= 6 && board[5][move.getColumn()] != '_') {
            System.err.println("getResult(): it was generated a move for a non-empty square");
            System.exit(-1);
        } else {
            copyBoard(board, newBoard);
            newBoard[move.getRow()][move.getColumn()] = move.getSeed();
        }

        return newBoard;
    }

    private static char[][] copyBoard(char[][] originalBoard, char[][] copiedBoard) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                copiedBoard[i][j] = originalBoard[i][j];
            }
        }
        return copiedBoard;
    }

    public static int declareWinner (char[][] board) {

        // check every directions from each square
        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int[] d : directions) {
            int dx = d[0];
            int dy = d[1];
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    int lastx = x + 3*dx;
                    int lasty = y + 3*dy;
                    if (0 <= lastx && lastx < 6 && 0 <= lasty && lasty < 7) {
                        char w = board[x][y];
                        if (w != '_'  && w == board[x+dx][y+dy] && w == board[x+2*dx][y+2*dy] && w == board[lastx][lasty]) {
                            if (w == 'O')   return 0;
                            else if (w == 'X')   return 1;
                        }
                    }
                }
            }
        }

        int turns = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(board[i][j] != '_') { // not blank
                    turns++;
                }
            }
        }

        // draw check
        if (turns == (7*6))             return -1;
        return -2;
    }

    public static boolean isTerminal (char[][] board) {

        int winner = declareWinner(board);
        if (winner == 0 || winner == 1 || winner == 2)  return true;

        return false;
    }

    public static double getUtility(char[][] board) {

        double draw = 0.0, AIwin = 10.0, AIloss = -10.0;

        int winner = declareWinner(board);

        if (winner == 0)        return AIwin;
        else if (winner == 1)   return AIloss;
        else if (winner == 2)   return draw;

        else {
            System.err.println("getUtility(): called on a non-terminal board");
            System.exit(-1);
            return 0.0;
        }
    }

    public static double evaluateHeuristic (char[][] board) {

        int matchpoint = 30000, two = 3, three = 30, four = 3000;
        int score = 0;

        int[][] horizontalSB = new int[6][7];
        int[][] verticalSB = new int[6][7];
        int[][] diagonalSB = new int[6][7];
        int[][] antidiagonalSB = new int[6][7];

        List<int[][]> scoreBoards = new ArrayList<>();
        scoreBoards.add(horizontalSB);
        scoreBoards.add(antidiagonalSB);
        scoreBoards.add(diagonalSB);
        scoreBoards.add(verticalSB);


        System.out.println(Engine.printBoard(board, -1, 0));

        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int i = 0; i < 4; i++) {
            int dx = directions[i][0];
            int dy = directions[i][1];

            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    char w = board[x][y];

                    int lastx4 = x + 3 * dx;
                    int lasty4 = y + 3 * dy;

                    int lastx3 = x + 2 * dx;
                    int lasty3 = y + 2 * dy;

                    int lastx2 = x + dx;
                    int lasty2 = y + dy;


                    //Dare punti solo se possibile soluzione di 4
                    if (w != '_' && scoreBoards.get(i)[x][y] == 0 && (0 <= lastx4 && lastx4 < 6 && 0 <= lasty4 && lasty4 < 7)) {

                        char[] next3 = {board[lastx2][lasty2], board[lastx3][lasty3], board[lastx4][lasty4]};

                        if ((next3[0] == '_' || next3[0] == w) && (next3[1] == '_' || next3[1] == w) && (next3[2] == '_' || next3[2] == w)) {
                            //Conto quante occorrenze di w ci sono
                            int counterw = 1;
                            for (char next : next3) {
                                if (next == w) counterw++;
                            }
                            int points = 0;
                            switch (counterw) {
                                case 1:
                                    break;
                                case 2:
                                    points = two;
                                    break;
                                case 3:
                                    points = three;
                                    if(lastx4 == 0){
                                        points += matchpoint;
                                    }else if(next3[0] == '_'){
                                        if(board[lastx2-1][lasty2] != '_'){
                                            points += matchpoint;
                                        }
                                    }else if(next3[1] == '_'){
                                        if(board[lastx3-1][lasty3] != '_'){
                                            points += matchpoint;
                                        }
                                    }else {
                                        if(board[lastx4-1][lasty4] != '_'){
                                            points += matchpoint;
                                        }
                                    }
                                    break;
                                case 4:
                                    points = four;
                                    break;
                            }
                            if (w == 'X') points = 0 - points;
                            scoreBoards.get(i)[x][y] += points;
                            scoreBoards.get(i)[lastx2][lasty2] += points;
                            scoreBoards.get(i)[lastx3][lasty3] += points;
                            scoreBoards.get(i)[lastx4][lasty4] += points;

                            //TODO: Controllare il caso in cui il 'buco' sia il primo elemento(sotto)


/*

                            if ((0 <= lastx4 && lastx4 < 6 && 0 <= lasty4 && lasty4 < 7) && (w == board[x + dx][y + dy] && w == board[x + 2 * dx][y + 2 * dy] && w == board[lastx4][lasty4])) {
                             System.out.println("four in a row");
                                if (w == 'O') {
                                    scoreBoards.get(i)[x][y] += four;
                                    scoreBoards.get(i)[x+dx][y+dy] += four;
                                    scoreBoards.get(i)[x+2*dx][y+2*dy] += four;
                                    scoreBoards.get(i)[lastx4][lasty4] += four;
                                }
                                else if (w == 'X') {
                                    scoreBoards.get(i)[x][y] -= four;
                                    scoreBoards.get(i)[x+dx][y+dy] -= four;
                                    scoreBoards.get(i)[x+2*dx][y+2*dy] -= four;
                                    scoreBoards.get(i)[lastx4][lasty4] -= four;
                                }
                        } else if ((0 <= lastx3 && lastx3 < 6 && 0 <= lasty3 && lasty3 < 7) && (w == board[x + dx][y + dy] && w == board[lastx3][lasty3])) {
                             System.out.println("three in a row");
                                if (w == 'O') {
                                    scoreBoards.get(i)[x][y] += three;
                                    scoreBoards.get(i)[x+dx][y+dy] += three;
                                    scoreBoards.get(i)[lastx3][lasty3] += three;
                                } else if (w == 'X') {
                                    scoreBoards.get(i)[x][y] -= three;
                                    scoreBoards.get(i)[x+dx][y+dy] -= three;
                                    scoreBoards.get(i)[lastx3][lasty3] -= three;
                                }
                        } else if ((0 <= lastx2 && lastx2 < 6 && 0 <= lasty2 && lasty2 < 7) && (w == board[lastx2][lasty2])) {
                                System.out.println("two in a row");
                                if (w == 'O') {
                                    scoreBoards.get(i)[x][y] += two;
                                    scoreBoards.get(i)[lastx2][lasty2] += two;
                                } else if (w == 'X')  {
                                    scoreBoards.get(i)[x][y] -= two;
                                    scoreBoards.get(i)[lastx2][lasty2] -= two;
                                }
                        } else {
                            //System.out.println("one in a row");
                            //if (w == '0')           scoreBoards.get(i)[x][y] += one;
                            //else if (w == 'X')      scoreBoards.get(i)[x][y] -= one;

                        }
*/
                        }
                    }
                }
            }
        }


        for (int[][] scoreTable : scoreBoards) {
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    score += scoreTable[x][y];
                }
            }
        }
        System.out.println("score of previous table " + score);
        return score;
    }

}
