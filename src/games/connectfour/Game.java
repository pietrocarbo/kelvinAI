package games.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Game {
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    public static char getPlayer (char[][] board, char startingPlayer) {
        int xVSoPlyCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if(board[i][j] == 'X') {
                    xVSoPlyCounter++;
                } else if (board[i][j] == 'O') {
                    xVSoPlyCounter--;
                }
            }
        }

        return (xVSoPlyCounter == 0 ? startingPlayer :
                xVSoPlyCounter > 0 ? 'O' : 'X');
    }


    public static List<Action> getActions (char[][] board, char startingPlayer, int order) {
        List<Action> result = new ArrayList<Action>();
        char nextPlayer = Game.getPlayer(board, startingPlayer);

        switch (order) {
            case 0:         // left to right
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if(board[i][j] == '_') {
                            result.add(new Action(i, j, nextPlayer));
                            break;
                        }
                    }
                }
                break;
            case 1:         // random ordering
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if(board[i][j] == '_') {
                            result.add(new Action(i, j, nextPlayer));
                            break;
                        }
                    }
                }
                 Collections.shuffle(result);
                break;
            case 2:        // first middle columns
                int[] middleColumnsFirst = new int[]{3, 2, 4, 1, 5, 0, 6};
                for (int j = 0; j < 7; j++) {
                    for (int i = 0; i < 6; i++) {
                        if(board[i][middleColumnsFirst[j]] == '_') {
                            result.add(new Action(i, middleColumnsFirst[j], nextPlayer));
                            break;
                        }
                    }
                }
                break;
        }
        return result;
    }

    public static char[][] getResult (char[][] board, Action move) {
        char[][] newBoard = new char[6][7];
        if (move.getColumn() >= 0 && move.getColumn() <= 6 && board[move.getRow()][move.getColumn()] != '_') {
            LOGGER.severe("getResult(): it was generated a move for a non-empty square");
            System.exit(-1);
        } else {
            Game.copyBoard(board, newBoard);
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

        if (turns == (7*6))             return 2;
        return -1; // game not over
    }

    public static boolean isTerminal (char[][] board) {

        int winner = Game.gameOverChecks(board, Game.calculateTurn(board));
        if (winner == 0 || winner == 1 || winner == 2)  return true;

        return false;
    }


    public static int calculateTurn (char[][] board) {
        int turn = 0;
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 7; y++) {
                if (board[x][y] != '_')     turn++;
            }
        }
        return turn;
    }

    public static double getUtilityHeuristic (char[][] board, char askingPlayer) {
        LOGGER.finer(Game.boardToString(board, Game.calculateTurn(board), "H.E. next to play " + askingPlayer));
        int boardScore = 0, two = 100, three = 1000, matchpoint = 10000, four = 10000000;

        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int i = 0; i < 4; i++) {  // for each direction
            int dx = directions[i][0];
            int dy = directions[i][1];

            for (int x = 0; x < 6; x++) {  // for each square
                for (int y = 0; y < 7; y++) { //

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
                            if (totalOccurrences != 4 && i != 0) {
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
                                    LOGGER.finest("4 in a row found for player " + row.getSeed() + " at (" + x + "," + y + " ->" + idxX4 + "," + idxY4 + ")");
                                    rowScore = four;
                                    break;
                                default:
                                    LOGGER.severe("Error in score calculation!");
                                    System.exit(-1);
                                    break;
                            }
                            if (row.getSeed() != askingPlayer) {
                                rowScore = -rowScore;
                            }
                            boardScore += rowScore;
                        }
                    }

                }
            }
        }

        LOGGER.finer("board score " + boardScore);
        return boardScore;
    }

    /*
    private int simpleHeuristic(char[][] grid, int rows, int columns, char seed){
        int value = 0;
        int[][] directions = {{1,0}, {1,-1}, {1,1}, {0,1}};
        for (int[] d : directions) {
            int dx = d[0];
            int dy = d[1];
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < columns; y++) {
                    int lastx = x + 3*dx;
                    int lasty = y + 3*dy;
                    if (0 <= lastx && lastx < rows && 0 <= lasty && lasty < columns) {
                        char w = grid[x][y];
                        if (w != '_' && w == grid[x+dx][y+dy] && w == grid[x+2*dx][y+2*dy] && w == grid[lastx][lasty]) {
                            if(w == seed) {
                                value = 1000;
                            }else{
                                System.out.println("PERDO CAZZO - " + Game.stampGrid(grid, rows,columns));
                                value = 0;
                            }
                            return value;
                        }
                    }
                }
            }
        }
        return -1000;
    }
    */

    private static Support checkBottomSupport (char[][] board, boolean[] occurrences, int[] indexes, boolean horizontalScan) {
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

    private static Row areThereMoreThenTwoInARowAndNotOpposingSeed (char first, char second, char third, char fourth) {
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

    public static String boardToString (char[][] grid, int turns, String player) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n---- turn " + turns + " " + player + "\n");

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

        return stringBuilder.toString();
    }
}

