package main;

import games.briscola.BriscolaEngine;
import games.connectfour.Engine;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        decks.fiftytwo.Deck americanDeck = new decks.fiftytwo.Deck();
//        decks.fiftytwo.Hand hand = new decks.fiftytwo.Hand(americanDeck.shuffle().deal(2));
//        System.out.println(americanDeck + "-----\n" + hand);

//        BriscolaEngine briscolaEngine = new BriscolaEngine();
//        briscolaEngine.play1vsAI();

        //Engine tictactoe = new Engine(0);  // 0 for AI to start, (1 or) other int for Human to start
        //tictactoe.play();


        games.connectfour.Engine connect4 = new games.connectfour.Engine(1);
        connect4.play1vsAI();
    }

}
