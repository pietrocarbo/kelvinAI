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

        do {
            System.out.println(briscolaEngine);
            int inputIDX = sc.nextInt();
            if (briscolaEngine.whoseTurn() == 1)
                cardSelected = briscolaEngine.getManoP1().getHand().get(inputIDX);
            else if (briscolaEngine.whoseTurn() == 2)
                cardSelected = briscolaEngine.getManoP2().getHand().get(inputIDX);
            else {
                System.err.println("ERROR");
                System.exit(-1);
            }

        } while (briscolaEngine.play(cardSelected));

    }

}
