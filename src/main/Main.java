package main;

import games.briscola.BriscolaEngine;
import games.tictactoe.Engine;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        decks.fiftytwo.Deck americanDeck = new decks.fiftytwo.Deck();
//        decks.fiftytwo.Hand hand = new decks.fiftytwo.Hand(americanDeck.shuffle().deal(2));
//        System.out.println(americanDeck + "-----\n" + hand);

//        BriscolaEngine briscolaEngine = new BriscolaEngine();
//        briscolaEngine.play1vsAI();

        Engine tictactoe = new Engine(0);
        tictactoe.play();

    }

}
