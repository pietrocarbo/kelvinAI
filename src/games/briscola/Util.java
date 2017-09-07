package games.briscola;

import decks.forty.*;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {

    public static Integer calculatePoints(Hand cards) {
        Integer counter = 0;
        for (Card card : cards.getHand()) {
            switch (card.getRank()) {
                case ASSO:
                    counter += 11;
                    break;
                case TRE:
                    counter += 10;
                    break;
                case RE:
                    counter += 4;
                    break;
                case CAVALLO:
                    counter += 3;
                    break;
                case FANTE:
                    counter += 2;
                    break;
                default:
                    break;
            }
        }
        return counter;
    }

    public static Integer getHandWinner(Hand tavolo, Suit semeBriscola) {
        Card c1 = tavolo.getHand().get(0);
        Suit s1 = c1.getSuit();
        Rank r1 = c1.getRank();

        Card c2 = tavolo.getHand().get(1);
        Suit s2 = c2.getSuit();
        Rank r2 = c2.getRank();

        //System.out.println("Choosing winner between cards " + c1 + "(" + r1.getBriscolaValue() + ") and " + c2 + "(" + r2.getBriscolaValue() + ")");

        if (s1 == semeBriscola && s1 != s2) return 0;
        if (s2 == semeBriscola && s1 != s2) return 1;

        if (s1 == s2) return r1.getBriscolaValue() > r2.getBriscolaValue() ? 0 : 1;

        return 0;  // non c'è briscola e le due carte sono di semi diversi: vince la prima carta
    }

    public static Integer getHandWinner(Hand tavolo, Suit semeBriscola, int lastPlayer) {
        Card c1 = tavolo.getHand().get(0);
        Suit s1 = c1.getSuit();
        Rank r1 = c1.getRank();

        Card c2 = tavolo.getHand().get(1);
        Suit s2 = c2.getSuit();
        Rank r2 = c2.getRank();

        //System.out.println("Choosing winner between cards " + c1 + "(" + r1.getBriscolaValue() + ") and " + c2 + "(" + r2.getBriscolaValue() + ")");

        if (s1 == semeBriscola && s1 != s2) return 0;
        if (s2 == semeBriscola && s1 != s2) return 1;

        if (s1 == s2) return r1.getBriscolaValue() > r2.getBriscolaValue() ? (lastPlayer == 0 ? 1 : 0) : lastPlayer;

        return lastPlayer == 0 ? 1 : 0;  // non c'è briscola e le due carte sono di semi diversi: vince la prima carta
    }

    public static int getGameWinner(List<Player> players){
        int winner = 0, ptsMax = 0;

        for(int i = 0; i < players.size(); i++) {
            if(Util.calculatePoints(players.get(i).getCardsCollected()) > ptsMax){
                winner = i;
                ptsMax = Util.calculatePoints(players.get(i).getCardsCollected());
            }else if(Util.calculatePoints(players.get(i).getCardsCollected()) == ptsMax){
                winner = -1;
            }
        }

        return winner;
    }

    public static int getIDOfLessValueCard(Hand cards, Suit briscola){
        int lessVal = 9, ID = -1;
        for(int i = 0; i < cards.getHand().size(); i++){
            if(lessVal > cards.getHand().get(i).getRank().getBriscolaValue() && cards.getHand().get(i).getSuit() != briscola){
                lessVal = cards.getHand().get(i).getRank().getBriscolaValue();
                ID = i;
            }
        }

        if(ID == -1){
            for(int i = 0; i < cards.getHand().size(); i++){
                if(lessVal > cards.getHand().get(i).getRank().getBriscolaValue()){
                    lessVal = cards.getHand().get(i).getRank().getBriscolaValue();
                    ID = i;
                }
            }
        }

        return ID == -1 ? 0 : ID;
    }

    public static int getIDOfFirstBestCard(Hand cards, Suit briscola, Card tableCard){
        List<Card> winnerCards = new ArrayList<>();
        List<Integer> winnerCardsID = new ArrayList<>();

        for(int i = 0; i < cards.getHand().size(); i++){
            Hand newTable = new Hand(new ArrayList<Card>());
            newTable.addOne(tableCard);
            newTable.addOne(cards.getHand().get(i));
            if(getHandWinner(newTable, briscola) == 1){
                winnerCards.add(cards.getHand().get(i));
                winnerCardsID.add(i);
            }
        }

        if(winnerCards.size() > 0) {
            return winnerCardsID.get(getIDOfLessValueCard(new Hand(winnerCards), briscola));
        }else{
            return getIDOfLessValueCard(cards, briscola);
        }

    }

    public static Card monteCarloMethod(int whoIam, int depth, boolean pruning, int nmazzi,Hand cards, Card briscola ,Hand tavolo, Hand otherCards, int p1Point, int p2Point, int oppositeNoOfCard,int nextPlayer){

        System.out.println("I'm thinking...");

        int[] choice = new int[cards.getHand().size()];

        int k = 0;
        long nodes = 0;

        List<int[]> mazzi = new ArrayList<>();

        while(k < nmazzi) {
            //Se ho finito le combinazioni esco dal ciclo dei mazzi
            if(otherCards.getHand().size() < 5){
                if(otherCards.getHand().size() == 0 || mazzi.size() == factorial(otherCards.getHand().size())) {
                    break;
                }
            }

            Deck mixedMazzo;

            do {
                mixedMazzo = new Deck(otherCards.shuffle());
            }while (alreadyExist(mazzi, getSerial(mixedMazzo)));

            mazzi.add(getSerial(mixedMazzo));

            mixedMazzo.getDeck().add(briscola);

            for (int i = 0; i < cards.getHand().size(); i++) {

                Hand myCards = new Hand(new ArrayList<Card>(cards.getHand()));

                //Aggiorno il tavolo con la corrente carta giocata sul tavolo
                tavolo.addOne(cards.getHand().get(i));

                myCards.removeOne(cards.getHand().get(i));

                //Creo la mano dell'avversario con 3 carte ipotetiche
                Hand oppositeHandCard = new Hand(new ArrayList<Card>(mixedMazzo.deal(oppositeNoOfCard)));

                //Creo il gioco
                MonteCarloGame game = new MonteCarloGame(new Deck(new Hand(new ArrayList<>(mixedMazzo.getDeck()))), briscola, myCards, oppositeHandCard, p1Point, p2Point, new Hand(new ArrayList<Card>(tavolo.getHand())), nextPlayer);
                game.setWhoIam(whoIam);

                mixedMazzo.getDeck().addAll(oppositeHandCard.getHand());

                tavolo.removeOne(cards.getHand().get(i));

                //Lo faccio giocare con il metodo minmax fino alla fine
                game.playUntilEnd(depth, pruning);

                //Prendo i punti finali del mio giocatore (i player hanno giocato con minmax algorithm)
                choice[i] += game.getGameVal();
                nodes += MonteCarloGame.nodes;
            }
            k++;
        }

        int maxIndex = 0;

        for(int i = 0; i < choice.length; i++){
            if(choice[i]/nmazzi > choice[maxIndex]/nmazzi){
                maxIndex = i;
            }
            //System.out.println("RISULTATO CARTA No " + i + " : " + choice[i]/nmazzi);
        }
        //System.out.println("TOTALE NODI CREATI --> " + nodes);

        MonteCarloGame.nodes = 0;

        return cards.getHand().get(maxIndex);
    }

    public static int[] getSerial(Deck mazzo){
        int[] cardCode = new int[mazzo.getDeck().size()];

        for(int i = 0; i < mazzo.getDeck().size(); i++){
            cardCode[i] = mazzo.getDeck().get(i).getRank().getBriscolaValue();
            switch (mazzo.getDeck().get(i).getSuit()){
                case COPPE:
                    cardCode[i] += 10;
                    break;
                case SPADE:
                    cardCode[i] += 20;
                    break;
                case DENARI:
                    cardCode[i] += 30;
            }
        }

        return cardCode;
    }

    public static boolean alreadyExist(List<int[]> mazzi, int[] newmazzo){
        for(int i = 0; i < mazzi.size(); i++){
            boolean sameCard = true;
            for(int j = 0; j < mazzi.get(i).length; j++){
                if(mazzi.get(i)[j] !=  newmazzo[j]){
                    sameCard = false;
                }
            }
            if(sameCard == true){
                return true;
            }
        }

        return false;
    }

    public static long factorial(int n){
        if(n == 1){
            return 1;
        }else {
            return factorial(n - 1) * n;
        }
    }
}
