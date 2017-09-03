package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;
import decks.forty.Rank;
import decks.forty.Suit;

import java.util.ArrayList;
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

        System.out.println("Choosing winner between cards " + c1 + " and " + c2);

        if (s1 == semeBriscola && s1 != s2) return 1;
        if (s2 == semeBriscola && s1 != s2) return 2;

        if (s1 == s2) return r1.getBriscolaValue() > r2.getBriscolaValue() ? 1 : 2;

        return 1;  // non c'è briscola e le due carte sono di semi diversi: vince la prima carta
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
        int lessVal = 100, ID = -1;
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

        return ID;
    }

    public static int getIDOfFirstBestCard(Hand cards, Suit briscola, Card tableCard){
        List<Card> winnerCards = new ArrayList<>();
        List<Integer> winnerCardsID = new ArrayList<>();

        for(int i = 0; i < cards.getHand().size(); i++){
            Hand newTable = new Hand(new ArrayList<Card>());
            newTable.addOne(tableCard);
            newTable.addOne(cards.getHand().get(i));
            if(getHandWinner(newTable, briscola) == 2){
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

    public static List<Action> getAction(Hand myCards, int ID){

        return new ArrayList<>();
    }
}
