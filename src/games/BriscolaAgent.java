package games;

import decks.forty.Card;

public class BriscolaAgent {

    private BriscolaEngine engine;

    public BriscolaAgent(BriscolaEngine engine) {
        this.engine = engine;
    }

    public Card makePly() {  // TODO
        return engine.getManoP1().getHand().get(0);
    }
}
