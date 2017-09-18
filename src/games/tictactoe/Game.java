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

        this.nextToPlay = starter;

        switch (mod) {
            case HUMAN_VS_HUMAN:
                this.players.add(new Human('O'));
                this.players.add(new Human('X'));
                break;
            case HUMAN_VS_AI:
                this.players.add(new AI('O', true));
                this.players.add(new Human('X'));
                break;
            case AI_VS_AI:
                this.players.add(new AI('O', true));
                this.players.add(new AI('X', false));
                break;
        }

    }

    public void doNextTurn() {

        LOGGER.fine("Next to play is player " + nextToPlay);

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
