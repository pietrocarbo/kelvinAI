package games.tictactoe;

import main.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game {

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private List<Player> players = new ArrayList<>();
    private char[][] grid = new char[3][3];
    private int turns = 0;
    private int nextToPlay;

    public char[][] getGrid() {
        return grid;
    }

    public Game(int starter, GameType mod) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.grid[i][j] = ' ';
            }
        }

        if (starter != 0 && starter != 1) {
            LOGGER.severe("ERROR unexpected starter player identifier");
        }

        this.nextToPlay = (starter == 0 ? 0 : 1);

        switch (mod) {
            default:
            case HUMAN__VS__HUMAN:
                this.players.add(new Human('O'));
                this.players.add(new Human('X'));
                break;
            case HUMAN__VS__AI_MINMAX:
                this.players.add(new AI('O'));
                this.players.add(new Human('X'));
                break;
            case AI_MINMAX__VS__AI_MINMAX:
                this.players.add(new AI('O'));
                this.players.add(new AI('X'));
                break;
        }

    }

    public void doNextTurn() {

        LOGGER.info("Next to play is player " + nextToPlay);

        Action nextMove = players.get(nextToPlay).play(grid);

        grid[nextMove.getRow()][nextMove.getColumn()] = nextMove.getSeed();

        nextToPlay = 1 - nextToPlay;

        turns++;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n---------- turn " + this.turns + " ---------\n");
        for (char[] row : grid) {
            stringBuilder.append("|");
            for (char element : row) {
                stringBuilder.append(element + "|");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
