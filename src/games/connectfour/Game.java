package games.connectfour;

import main.GameType;
import main.MovesOrdering;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game {

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private List<Player> players = new ArrayList<>();
    private char[][] grid = new char[6][7];
    private int turns;
    private int nextToPlay;

    public char[][] getGrid() {
        return grid;
    }

    public int getTurns() {
        return turns;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Game(int starter, GameType mod, List<Integer> depthsOfSearch, List<MovesOrdering> movesOrdering) {

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.grid[i][j] = '_';
            }
        }

        this.turns = 0;
        this.nextToPlay = (starter == 0 ? 0 : 1);

        switch (mod) {
            default:
            case HUMAN__VS__HUMAN:
                players.add(new Human('O'));
                players.add(new Human('X'));
                break;

            case HUMAN__VS__AI_MINMAX:
                players.add(new AI('O', depthsOfSearch.get(0), movesOrdering.get(0)));
                players.add(new Human('X'));
                break;

            case HUMAN__VS__AI_MCTS:
                players.add(new AIMCTS('O', depthsOfSearch.get(0), movesOrdering.get(0)));
                players.add(new Human('X'));
                break;

            case AI_MINMAX__VS__AI_MINMAX:
                players.add(new AI('O', depthsOfSearch.get(0), movesOrdering.get(0)));
                players.add(new AI('X', depthsOfSearch.get(1), movesOrdering.get(1)));
                break;

            case AI_MCTS__VS__AI_MINMAX:
                players.add(new AIMCTS('O', depthsOfSearch.get(0), movesOrdering.get(0)));
                players.add(new AI('X', depthsOfSearch.get(1), movesOrdering.get(1)));
                break;
        }
    }

    public void doNextTurn() {
        LOGGER.info(Util.boardToString(grid, turns, "-> next to play is " + players.get(nextToPlay).getMySeed()));

        Action nextMove = players.get(nextToPlay).play(grid);

        grid[nextMove.getRow()][nextMove.getColumn()] = nextMove.getSeed();

        nextToPlay = 1 - nextToPlay;

        turns++;
    }
}

