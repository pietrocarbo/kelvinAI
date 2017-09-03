package games.briscola;

import decks.forty.*;

import java.util.Scanner;

public class Engine {



    public Engine() {

    }

    public void playNewGame(int mod) {

        Game game = new Game(1, mod);

        boolean isGameOver = false;

        System.out.println("Game started.\n");

        do {
            System.out.println(game);

            int nextPlayer = game.getNextPlayer();

            if(nextPlayer == 0){
                isGameOver = true;
            }else {
                game.doNextTurn();
            }
        } while (!isGameOver);

        System.out.println("End game. ");

        int winner = Util.getGameWinner(game.getPlayers());

        if(winner == -1){
            System.out.println("Game ends in draw.");
        }else{
            System.out.println(game.getPlayers().get(winner).getName() + " won the game.");
        }

    }

    /*
    public void play1vs1 () {

        Scanner sc = new Scanner(System.in);

        Card cardSelected = new Card(Rank.ASSO, Suit.DENARI);
        boolean isGameOver = false;

        System.out.println("Game started. Enter the number of the card you want to play\n");

        do {
            System.out.println(this);

            int inputIDX = sc.nextInt();

            if (inputIDX == 4) {
                Hand puntiP1 = getPuntiP1();
                System.out.println("\npoints P1 (" + calculatePoints(puntiP1) + ") " + puntiP1);
                continue;

            } else if (inputIDX == 5) {
                Hand puntiP2 = getPuntiP2();
                System.out.println("\npoints P2 (" + calculatePoints(puntiP2) + ") " + puntiP2);
                continue;

            } else if (inputIDX < 1 || inputIDX > 5
                    || (getTurnsLeft() == 2 && inputIDX > 2)
                    || (getTurnsLeft() == 1 && inputIDX > 1) ) {
                System.err.println("Entered out of range card number ('4'-'5' to see current points)");
                continue;

            } else {
                if (toPlay == 1)
                    cardSelected = getManoP1().getHand().get(inputIDX - 1);
                else if (toPlay == 2)
                    cardSelected = getManoP2().getHand().get(inputIDX - 1);
            }

            isGameOver = ply(cardSelected);

        } while (isGameOver == false);

    }
*/
}
