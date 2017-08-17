package games.connectfour;

public class Agent {

    private final int SEED_UNIT = 1;   // has to be unsigned
    private final int SEED_AI = SEED_UNIT, SEED_HUMAN = - SEED_UNIT;
    private int SEED_STARTER;
    private int nodeVisited = 0;

    public Agent(int starter){
        if (starter == 0)   SEED_STARTER = SEED_AI;
        else                SEED_STARTER = SEED_HUMAN;
    }
}
