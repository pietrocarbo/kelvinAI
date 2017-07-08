package main;

import decks.forty.*;


import java.util.List;

public class MainFortyDeck {

    public static void main(String[] args) {

        Deck italianDeck = new Deck();

        Hand hand = new Hand(italianDeck.shuffle().deal(3));


    }
}
