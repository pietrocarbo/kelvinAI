package games.briscola;

import decks.forty.Hand;

public class BriscolaPlayer {

    private int score;
    private String name;
    private boolean winner;
    private Hand cards;
    private Hand points;

    public BriscolaPlayer(int score, String name, boolean winner, Hand hand, Hand points) {
        this.score = score;
        this.name = name;
        this.winner = winner;
        this.cards = hand;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean isWinner() {
        return winner;
    }

    public Hand getCards() {
        return cards;
    }

    public Hand getPoints() {
        return points;
    }
}
