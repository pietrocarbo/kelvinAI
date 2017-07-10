package main;

import decks.forty.*;
import games.BriscolaEngine;


import java.util.List;
import java.util.Scanner;

public class MainFortyDeck {

    public static void main(String[] args) {

        playBriscola();

    }


    public static void playBriscola () {

        BriscolaEngine briscolaEngine = new BriscolaEngine();
        Scanner sc = new Scanner(System.in);

        Card cardSelected = new Card(Rank.ASSO, Suit.DENARI);
        boolean isGameOver = false;

        System.out.println("Game started. Enter the number of the card you want to play\n");

        do {
            System.out.println(briscolaEngine);

            int inputIDX = sc.nextInt();

            if (inputIDX == 4) {
                Hand puntiP1 = briscolaEngine.getPuntiP1();
                System.out.println("\npoints P1 (" + briscolaEngine.pointScored(puntiP1) + ") " + puntiP1);
                continue;

            } else if (inputIDX == 5) {
                Hand puntiP2 = briscolaEngine.getPuntiP2();
                System.out.println("\npoints P2 (" + briscolaEngine.pointScored(puntiP2) + ") " + puntiP2);
                continue;

            } else if (inputIDX < 1 || inputIDX > 5
                       || (briscolaEngine.getTurnsLeft() == 2 && inputIDX > 2)
                       || (briscolaEngine.getTurnsLeft() == 1 && inputIDX > 1) ) {
                System.err.println("Entered out of range card number ('4'-'5' to see current points)");
                continue;

            } else {
                if (briscolaEngine.whoseTurn() == 1)
                    cardSelected = briscolaEngine.getManoP1().getHand().get(inputIDX - 1);
                else if (briscolaEngine.whoseTurn() == 2)
                    cardSelected = briscolaEngine.getManoP2().getHand().get(inputIDX - 1);
            }

            isGameOver = briscolaEngine.play(cardSelected);

        } while (isGameOver == false);

    }

}
