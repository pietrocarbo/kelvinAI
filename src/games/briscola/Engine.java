package games.briscola;

import decks.forty.*;

import java.util.Scanner;

public class Engine {



    public Engine() {

    }

    public void playNewGame(int mod, int nOfGames) {
        int k = 0, p1Pts = 0, p2Pts = 0, p1Win = 0, p2Win = 0, draw = 0;

        while (k < nOfGames) {
            Game game = new Game(0, mod);

            boolean isGameOver = false;

            System.out.println("Game started.\n");

            do {
                //System.out.println(game);

                int nextPlayer = game.getNextPlayer();

                if (nextPlayer == -1) {
                    isGameOver = true;
                } else {
                    game.doNextTurn();
                }
            } while (!isGameOver);

            System.out.println("End game. ");

            int winner = Util.getGameWinner(game.getPlayers());

            if (winner == -1) {
                System.out.println("Game ends in draw.");
                draw++;
            } else {
                System.out.println(game.getPlayers().get(winner).getName() + " won the game with " + game.getPlayers().get(winner).getBriscole() + " carte di briscola.");
                if(winner == 0){
                    p1Win++;
                }else{
                    p2Win++;
                }
                p1Pts += Util.calculatePoints(game.getPlayers().get(0).getCardsCollected());
                p2Pts += Util.calculatePoints(game.getPlayers().get(1).getCardsCollected());
            }

            k++;
        }

        System.out.println("\n\n\n Smith: " + (p1Win * 100)/nOfGames + "% win with " + p1Pts/nOfGames + " AVG.");
        System.out.println("\n Kelvin: " + (p2Win * 100)/nOfGames + "% win with " + p2Pts/nOfGames + " AVG.");
        System.out.println("\n Draw: " + (draw * 100)/nOfGames + "% draw.");


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
