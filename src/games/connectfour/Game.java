package games.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Game {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private char starter;

    public Game(char seedStarter) {
        this.starter = seedStarter;
    }

    public char getPlayer (char[][] board) {
        int xPlyCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(board[i][j] == 'X') { // (player 1)
                    xPlyCounter++;
                } else if (board[i][j] == 'O') { // (player 0)
                    xPlyCounter--;
                }
            }
        }

        return (xPlyCounter == 0 ? starter :
                xPlyCounter > 0 ? 'O' : 'X');  // return 0 if it is AI turn, 1 if it is human turn, else the starter
    }

    public List<Action> getActions (char[][] board) {
        List<Action> result = new ArrayList<Action>();
        char nextPlayer = getPlayer(board);

        for (int j = 0; j < 7; j++) { // for each column
            for (int i = 0; i < 6; i++) { // and then for each row upwards
                if(board[i][j] == '_') { // blank
                    result.add(new Action(i, j, nextPlayer));
                    break;
                }
            }
        }

        // Collections.shuffle(result);  // random moves ordering
        return result;
    }

    public char[][] getResult (char[][] board, Action move) {
        char[][] newBoard = new char[6][7];
        if (move.getColumn() >= 0 && move.getColumn() <= 6 && board[move.getRow()][move.getColumn()] != '_') {
            LOGGER.severe("getResult(): it was generated a move for a non-empty square");
            System.exit(-1);
        } else {
            copyBoard(board, newBoard);
            newBoard[move.getRow()][move.getColumn()] = move.getSeed();
        }
        return newBoard;
    }

    private char[][] copyBoard(char[][] originalBoard, char[][] copiedBoard) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                copiedBoard[i][j] = originalBoard[i][j];
            }
        }
        return copiedBoard;
    }

    public static int gameOverChecks (char[][] board, int turns) {

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
                            if (w == 'O')        return 0;
                            else if (w == 'X')   return 1;
                        }
                    }
                }
            }
        }

        if (turns == (7*6))             return 2;  // draw check
        return -1; // game is not over
    }

    public boolean isTerminal (char[][] board) {

        int winner = gameOverChecks (board, calculateTurn(board));
        if (winner == 0 || winner == 1 || winner == 2)  return true;

        return false;
    }


    public int calculateTurn (char[][] board) {
        int turn = 0;
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 7; y++) {
                if (board[x][y] != '_')     turn++;
            }
        }
        return turn;
    }

    public double getUtilityHeuristic (char[][] board, char player) {

        LOGGER.finer(Engine.boardToString(board, calculateTurn(board), player == 'O' ? 2 : 3));
        int boardScore = 0, two = 5, three = 50, matchpoint = 1000, four = Integer.MAX_VALUE;

        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int i = 0; i < 4; i++) {  // for each direction
            int dx = directions[i][0];
            int dy = directions[i][1];

            for (int x = 0; x < 6; x++) {  // for each square
                for (int y = 0; y < 7; y++) {

                    int idxX2 = x + dx;
                    int idxY2 = y + dy;

                    int idxX3 = x + 2 * dx;
                    int idxY3 = y + 2 * dy;

                    int idxX4 = x + 3 * dx;
                    int idxY4 = y + 3 * dy;

                    int[] indexes = new int[]{x, y, idxX2, idxY2, idxX3, idxY3, idxX4, idxY4};

                    if (0 <= idxX4 && idxX4 < 6 && 0 <= idxY4 && idxY4 < 7) {

                        char first = board[x][y];
                        char second = board[idxX2][idxY2];
                        char third = board[idxX3][idxY3];
                        char fourth = board[idxX4][idxY4];
                        Row row = areThereMoreThenTwoInARowAndNotOpposingSeed(first, second, third, fourth);

                        if (row.getGivesPoint()) {

                            int totalOccurrences = row.getTotalOccurrences();
                            Support supportInfo;
                            if (totalOccurrences != 4 && i != 0) { // check support if it is not a victory and direction is not vertical
                                supportInfo = checkBottomSupport(board, row.getOccurrences(), indexes, i == 3 ? true : false);
                            } else {
                                supportInfo = new Support(true, 0);
                            }

                            int rowScore = 0, missingSupports = supportInfo.getTotalSquareMissing();

                            switch (totalOccurrences) {
                                case 2:
                                    rowScore = two - missingSupports;
                                    break;
                                case 3:
                                    if (missingSupports == 0) rowScore = matchpoint;
                                    else {
                                        rowScore = three - 5 * missingSupports;
                                    }
                                    break;
                                case 4:
                                    rowScore = four;
                                    break;
                                default:
                                    LOGGER.severe("Error in score calculation!");
                                    System.exit(-1);
                                    break;
                            }
                            if (rowScore < 0) rowScore = 0;   // too many supports missing
                            if (row.getSeed() != player) rowScore *= -1;  // if it is human streak

                            boardScore += rowScore;
                        }
                    }

                }
            }
        }

        LOGGER.finer("board score " + boardScore);
        return boardScore;
    }

    private Support checkBottomSupport (char[][] board, boolean[] occurrences, int[] indexes, boolean horizontalScan) {
        int missingSupports = 0;

        for (int i = 0; i < 4; i++) {
            if (!occurrences[i]) {

                if (horizontalScan && indexes[0] == 0)
                {
                    return new Support(true, missingSupports);
                }
                else if (horizontalScan && indexes[0] > 0)
                {
                    for (int j = indexes[2*i]-1; j >= 0; j--) {
                        if (board[j][indexes[2*i+1]] == '_') {
                            missingSupports++;
                        } else
                            continue;
                    }

                }
                else if (!horizontalScan && indexes[2*i] > 0)
                {
                    for (int j = indexes[2*i]-1; j >= 0; j--) {
                        if (board[j][indexes[2*i+1]] == '_') {
                            missingSupports++;
                        } else
                            continue;
                    }
                }
                else if (!horizontalScan && indexes[2*i] == 0)
                {
                    continue;
                }
                else {
                    LOGGER.severe("Error in support calculation!");
                    System.exit(-1);
                }

            }
        }
        return new Support(missingSupports == 0 ? true : false, missingSupports);
    }

    private Row areThereMoreThenTwoInARowAndNotOpposingSeed (char first, char second, char third, char fourth) {
        boolean[] occurrences = new boolean[]{false, false, false, false};
        char[] row = {first, second, third, fourth};
        int os = 0, xs = 0;
        char dominantSeed;

        for (int i = 0; i < 4; i++) {
            if (row[i] != '_') {
                if (row[i] == 'O') {
                    occurrences[i] = true;
                    os++;
                }
                else if (row[i] == 'X') {
                    occurrences[i] = true;
                    xs++;
                }
                else {
                    LOGGER.severe("Board malformed!");
                    System.exit(-1);
                }
            }
        }
        if ((xs > 0 && os > 0) || (xs + os == 0) || ((xs > os && xs < 2) || (os > xs && os < 2)))
            return new Row(false);


        if (xs > 0) dominantSeed = 'X';
        else        dominantSeed = 'O';

        return new Row(true, dominantSeed, occurrences, xs > 0 ? xs : os);
    }

    private double getUtility(char[][] board) {

        double draw = 0.0, AIwin = 10.0, AIloss = -10.0;

        int winner = gameOverChecks (board, 0);

        if (winner == 0)        return AIwin;
        else if (winner == 1)   return AIloss;
        else if (winner == 2)   return draw;

        else {
            LOGGER.severe("getUtility(): called on a non-terminal board");
            System.exit(-1);
            return 0.0;
        }
    }

    public double oldGetUtilityHeuristic (char[][] board) {

        int matchpoint = 3000, two = 3, three = 30, four = 30000;
        int nextPlayer = (getPlayer(board) == 'O' ? 0 : 1) + 2;
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


        LOGGER.finer(Engine.boardToString(board, calculateTurn(board), nextPlayer));

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
        LOGGER.finer("score of this table " + score);
        return score;
    }

}
