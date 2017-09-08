package games.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Game {
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private List<Player> players = new ArrayList<>();
    private char[][] grid = new char[6][7];
    private int turns;
    private int nextToPlay;
    private int depth1 = 5, depth2 = 5;

    public Game(int starter, int mod) {

        this.nextToPlay = starter;
        this.turns = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.grid[i][j] = '_';
            }
        }

        switch (mod) {
            case 1:
                players.add(new Human('O'));
                players.add(new Human('X'));
                break;
            case 2:
                players.add(new Human('O'));
                players.add(new AI('X', depth1, 2));
                players.get(players.size() - 1).setStarterSeed(players.get(starter).getMySeed());
                break;
            case 3:
                players.add(new AI('O', depth1, 1));
                players.add(new AI('X', depth2, 1));

                players.get(0).setStarterSeed(players.get(starter).getMySeed());
                players.get(players.size() - 1).setStarterSeed(players.get(starter).getMySeed());
                break;
            default:
                players.add(new Human('O'));
                players.add(new Human('X'));
                break;
        }
    }

    public void setDepths(int depth1, int depth2) {
        this.depth1 = depth1;
        this.depth2 = depth2;
    }

    public char[][] getGrid() {
        return grid;
    }

    public int getTurns() {
        return turns;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void doNextTurn() {
        LOGGER.info(Util.boardToString(grid, turns, "-> next to play " + players.get(nextToPlay).getMySeed()));

        Action nextMove = players.get(nextToPlay).play(grid);

        grid[nextMove.getRow()][nextMove.getColumn()] = nextMove.getSeed();

        nextToPlay = nextToPlay == 0 ? 1 : 0;

        turns++;
    }
}

