package games.connectfour;

import main.MovesOrdering;

import java.util.List;
import java.util.logging.Logger;

public class Util {
    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    static int depthCounter;
    static int movesCounter;
    static int nodesCounter;
    static int nodesPruned;

    public static void resetCounters() {
        depthCounter = 0;
        nodesCounter = 0;
        nodesPruned = 0;
        movesCounter = 0;
    }

    public static Action heuristicMinMaxAlgorithm(char[][] board, char player, int depth, MovesOrdering ordering) {
        resetCounters();

        Action bestMove = null;
        double resultValue = Double.NEGATIVE_INFINITY;

        List<Action> legalActions = Action.getActions(board, player, ordering);

        for (Action action : legalActions) {

            depthCounter = 0;
            movesCounter++;

            double value = heuristicMinMaxAlphaBeta(getResult(board, action), player, toggle(player), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, depth - 1,  ordering);

            LOGGER.fine("action " + action.getRow() + "," + action.getColumn() + " scored " + value + " (actual best " + resultValue + ")");

            if (value > resultValue) {
                bestMove = action;
                resultValue = value;
            }

        }
        if (bestMove == null)       bestMove = legalActions.get(0);  // if all actions score -Infinity we must choose one

        LOGGER.info("move selected is " + bestMove.getRow() + ", " + bestMove.getColumn() + " with highest score of " + resultValue + " (nodes visited " + nodesCounter + ", nodes pruned " + nodesPruned + ")");

        return bestMove;
    }

    public static double heuristicMinMaxAlphaBeta(char[][] board, char player, char playing, double alpha, double beta, boolean maximize, int depth, MovesOrdering ordering) {
        nodesCounter++;
        depthCounter++;

        if (isTerminal(board) || depth <= 0) {
            return getUtilityHeuristic(board, player);
        }

        double value = (maximize ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);

        for (Action action : Action.getActions(board, playing, ordering)) {

            if (maximize) {
                value = Math.max(value, heuristicMinMaxAlphaBeta(getResult(board, action), player, toggle(playing), alpha, beta, false, depth - 1, ordering));

                if (value >= beta) {
                    LOGGER.fine("subtree pruned by beta (value: " + value + " >= beta: " + beta + ")");
                    nodesPruned++;
                    return value;
                }
                alpha = Math.max(alpha, value);

            } else {

                value = Math.min(value, heuristicMinMaxAlphaBeta(getResult(board, action), player, toggle(playing), alpha, beta, true, depth - 1, ordering));

                if (value <= alpha) {
                    LOGGER.fine("subtree pruned by alpha (value: " + value + " <= alpha: " + alpha + ")");
                    nodesPruned++;
                    return value;
                }
                beta = Math.min(beta, value);
            }
        }

        LOGGER.finest("nodes visited " + nodesCounter + ", actual node depth " + depthCounter);

        return value;
    }

    public static char toggle (char actualPlayer) {
        return (actualPlayer == 'O' ? 'X' : 'O');
    }

    public static boolean isLegalMove(char[][] grid, int column, int row) {
        return column >= 0 && column <= 6 && grid[5][column] == '_' && grid[row][column] == '_';
    }

    public static char[][] getResult(char[][] board, Action move) {
        char[][] newBoard = new char[6][7];
        if (!isLegalMove(board, move.getColumn(), move.getRow())) {
            LOGGER.severe("ERROR: it was generated a unlegal move " + move.getColumn() + "," + move.getRow());
            System.exit(-1);
        } else {
            copyBoard(board, newBoard);
            newBoard[move.getRow()][move.getColumn()] = move.getSeed();
        }
        return newBoard;
    }

    public static char[][] copyBoard(char[][] originalBoard, char[][] copiedBoard) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                copiedBoard[i][j] = originalBoard[i][j];
            }
        }
        return copiedBoard;
    }

    public static int isGameOver(char[][] board, int turns, char seedPlayer1, char seedPlayer2) {
        // check every directions from each square
        int[][] directions = {{1, 0}, {1, -1}, {1, 1}, {0, 1}};
        for (int[] d : directions) {
            int dx = d[0];
            int dy = d[1];
            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < 7; y++) {
                    int lastx = x + 3 * dx;
                    int lasty = y + 3 * dy;
                    if (0 <= lastx && lastx < 6 && 0 <= lasty && lasty < 7) {
                        char w = board[x][y];
                        if (w != '_' && w == board[x + dx][y + dy] && w == board[x + 2 * dx][y + 2 * dy] && w == board[lastx][lasty]) {
                            if (w == seedPlayer1)       return 0;
                            else if (w == seedPlayer2)  return 1;
                        }
                    }
                }
            }
        }

        if (turns == (7 * 6)) return 2;

        return -1; // game not over
    }

    public static boolean isTerminal(char[][] board) {

        int winner = isGameOver(board, getTurn(board), 'O', 'X');
        if (winner == 0 || winner == 1 || winner == 2) return true;

        return false;
    }

    public static int getTurn(char[][] board) {
        int turn = 0;
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 7; y++) {
                if (board[x][y] != '_') turn++;
            }
        }
        return turn;
    }

    public static String boardToString(char[][] grid) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 5; i >= 0; i--) {
            stringBuilder.append("|");
            for (int j = 0; j <= 6; j++) {
                stringBuilder.append(grid[i][j] + "|");
            }
            stringBuilder.append("\t\t " + i + ".\n");
        }

        stringBuilder.append("\n.");
        for (int i = 0; i <= 6; i++) {
            stringBuilder.append(i + ".");
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public static String boardToString(char[][] grid, int turns, String player) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n---- turn " + turns + "\t" + player + "\n");

        for (int i = 5; i >= 0; i--) {
            stringBuilder.append("|");
            for (int j = 0; j <= 6; j++) {
                stringBuilder.append(grid[i][j] + "|");
            }
            stringBuilder.append("\t\t " + i + ".\n");
        }

        stringBuilder.append("\n.");
        for (int i = 0; i <= 6; i++) {
            stringBuilder.append(i + ".");
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public static double getUtilityHeuristic(char[][] board, char askingPlayer) {

        int turnsPlayed = getTurn(board);

        LOGGER.finer(boardToString(board, turnsPlayed, "H.E. for player " + askingPlayer));

        int boardScore = 0, two = 100 - turnsPlayed, three = 1000 - turnsPlayed, matchpoint = 10000 - turnsPlayed, four = 1000000 - turnsPlayed;

        int[][] directions = {{1, 0}, {1, -1}, {1, 1}, {0, 1}};
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
                            if (totalOccurrences != 4 && i != 0) {
                                supportInfo = checkBottomSupport(board, row.getOccurrences(), indexes, i == 3);
                            } else {
                                supportInfo = new Support(true, 0);
                            }

                            int rowScore = 0, missingSupports = supportInfo.getTotalSquareMissing();

                            switch (totalOccurrences) {
                                case 2:
                                    rowScore = two - missingSupports;
                                    break;
                                case 3:
                                    if (missingSupports == 0)   rowScore = matchpoint;
                                    else                        rowScore = three - 5 * missingSupports;

                                    break;
                                case 4:
                                    LOGGER.finest("4 in a row for player " + row.getSeed() + " (" + x + "," + y + " ->" + idxX4 + "," + idxY4 + ")");
                                    rowScore = four;
                                    break;
                                default:
                                    LOGGER.severe("ERROR: in score calculation");
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

    private static Support checkBottomSupport(char[][] board, boolean[] occurrences, int[] indexes, boolean horizontalScan) {
        int missingSupports = 0;

        for (int i = 0; i < 4; i++) {
            if (!occurrences[i]) {

                if (horizontalScan && indexes[0] == 0) {
                    return new Support(true, missingSupports);
                } else if (horizontalScan && indexes[0] > 0) {
                    for (int j = indexes[2 * i] - 1; j >= 0; j--) {
                        if (board[j][indexes[2 * i + 1]] == '_') {
                            missingSupports++;
                        } else
                            continue;
                    }

                } else if (!horizontalScan && indexes[2 * i] > 0) {
                    for (int j = indexes[2 * i] - 1; j >= 0; j--) {
                        if (board[j][indexes[2 * i + 1]] == '_') {
                            missingSupports++;
                        } else
                            continue;
                    }
                } else if (!horizontalScan && indexes[2 * i] == 0) {
                    continue;
                } else {
                    LOGGER.severe("Error in support calculation!");
                    System.exit(-1);
                }

            }
        }
        return new Support(missingSupports == 0, missingSupports);
    }

    private static Row areThereMoreThenTwoInARowAndNotOpposingSeed(char first, char second, char third, char fourth) {
        boolean[] occurrences = new boolean[]{false, false, false, false};
        char[] row = {first, second, third, fourth};
        int os = 0, xs = 0;
        char dominantSeed;

        for (int i = 0; i < 4; i++) {
            if (row[i] != '_') {
                if (row[i] == 'O') {
                    occurrences[i] = true;
                    os++;
                } else if (row[i] == 'X') {
                    occurrences[i] = true;
                    xs++;
                } else {
                    LOGGER.severe("Board malformed!");
                    System.exit(-1);
                }
            }
        }
        if ((xs > 0 && os > 0) || (xs + os == 0) || ((xs > os && xs < 2) || (os > xs && os < 2)))
            return new Row(false);


        if (xs > 0) dominantSeed = 'X';
        else dominantSeed = 'O';

        return new Row(true, dominantSeed, occurrences, xs > 0 ? xs : os);
    }

    private int notUsedVariantHeuristic(char[][] grid, int rows, int columns, char seed) {
        int value = 0;
        int[][] directions = {{1, 0}, {1, -1}, {1, 1}, {0, 1}};
        for (int[] d : directions) {
            int dx = d[0];
            int dy = d[1];
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < columns; y++) {
                    int lastx = x + 3 * dx;
                    int lasty = y + 3 * dy;
                    if (0 <= lastx && lastx < rows && 0 <= lasty && lasty < columns) {
                        char w = grid[x][y];
                        if (w != '_' && w == grid[x + dx][y + dy] && w == grid[x + 2 * dx][y + 2 * dy] && w == grid[lastx][lasty]) {
                            if (w == seed) {
                                value = 1000;
                            } else {
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
}
