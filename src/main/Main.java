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


        //games.connectfour.Engine connect4 = new games.connectfour.Engine(1);
        //connect4.playHumanVsAI();


        //Creo una piccola statistica su tot partite
        int AI1 = 0, AI2 = 0, draw = 0, depthAI1 = 5, depthAI2 = 5;

        games.connectfour.Engine connect4;
        //Provo con 30 partite
        for(int i = 0; i< 30; i++) {
            connect4 = new games.connectfour.Engine(1);
            switch (connect4.playAIVsAI(depthAI1,depthAI2)){
                case 0: AI1++; break;
                case 1: AI2++; break;
                case 2: draw++; break;
            }
        }

        System.out.println("AI 1 (depth " + depthAI1 + ") win: " + AI1 + "\nAI 2 (depth " + depthAI2 + ") win: " + AI2 + "\nDraw: " + draw);
    }

}
