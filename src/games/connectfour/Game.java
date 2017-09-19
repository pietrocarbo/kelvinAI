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

        this.nextToPlay = starter;
        this.turns = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.grid[i][j] = '_';
            }
        }

        char starterSeed = (starter == 0 ? 'O' : 'X');

        switch (mod) {
            default:
            case HUMAN_VS_HUMAN:
                players.add(new Human('O', starterSeed));
                players.add(new Human('X', starterSeed));
                break;

                case HUMAN_VS_AI:
                    players.add(new AI('O', starterSeed, true, depthsOfSearch.get(0), movesOrdering.get(0)));
                    players.add(new Human('O', starterSeed));
                break;

            case AI_VS_AI:
                players.add(new AI('O', starterSeed, true, depthsOfSearch.get(0), movesOrdering.get(0)));
                players.add(new AI('X', starterSeed, false, depthsOfSearch.get(1), movesOrdering.get(1)));
                break;

            case MCTS_VS_AI:
                players.add(new AIMCTS('O', starterSeed, depthsOfSearch.get(0), movesOrdering.get(0)));
                players.add(new AI('X', starterSeed, false, depthsOfSearch.get(1), movesOrdering.get(1)));
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

