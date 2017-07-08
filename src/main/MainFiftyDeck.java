package main;

import decks.fiftytwo.*;

public class MainFiftyDeck {

    public static void main (String[] args) {

        Deck americanDeck = new Deck();

        Hand hand = new Hand(americanDeck.shuffle().deal(2));

        System.out.println(americanDeck + "-----\n" + hand);

    }

}
