package games.briscola;

import decks.forty.*;

import java.util.ArrayList;
import java.util.List;

public class BriscolaGame {



    public BriscolaGame() {
    }

    public Integer getPlayer() {
        return null;
    }

    public boolean isTerminal(BriscolaState state) {
        return state.gameOver();
    }

    public BriscolaState getInitialState() {
        return null;
    }

    public BriscolaPlayer[] getPlayers() {
        return null;
    }

    public List<BriscolaAction> getActions(BriscolaState state) {
        return null;
    }

    public BriscolaState getResult(BriscolaState state, BriscolaAgent action) {
        return null;
    }

    public double getUtility(BriscolaState state, BriscolaPlayer player) {
        return 0;
    }

}
