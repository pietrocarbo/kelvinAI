package games.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private List<Player> players = new ArrayList<>();
    private int starter;
    private char[][] grid = new char[3][3];
    private int turns = 0;
    private int nextToPlay;

    public List<Player> getPlayers() {
        return players;
    }

    public char[][] getGrid() {
        return grid;
    }

    public Game(int starter, int mod) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.grid[i][j] = ' ';
            }
        }

        this.starter = starter;
        nextToPlay = starter;

        switch (mod) {
            case 1:
                players.add(new Human(0, 'X'));
                players.add(new Human(1, 'O'));
                break;
            case 2:
                players.add(new Human(0, 'X'));
                players.add(new AI(1, 'O', starter == 1 ? 'O' : 'X'));
                break;
            case 3:
                players.add(new AI(0, 'X', starter == 1 ? 'O' : 'X'));
                players.add(new AI(1, 'O', starter == 1 ? 'O' : 'X'));
                break;
        }

        Util.setSeeds('O', 'X');

    }

    public void doNextTurn() {

        System.out.println("Next to play is player " + nextToPlay);

        Action nextMove;
        if (players.get(nextToPlay) instanceof AI) {
            nextMove = players.get(nextToPlay).play(grid, players.get(1 - nextToPlay).getSeed());
        } else {
            nextMove = players.get(nextToPlay).play(grid);
        }

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
