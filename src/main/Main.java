package main;



public class Main {

    public static void main(String[] args) {

        games.connectfour.Engine connect4;
        games.tictactoe.Engine tictactoe;

        int gameType = 3;
        switch (gameType) {
            case 1:
                tictactoe = new games.tictactoe.Engine(0);  // 0 for AI to start, (1 or) other int for Human to start
                tictactoe.playHumanVsAI();
                break;
            case 2:
                connect4 = new games.connectfour.Engine(1);
                connect4.playHumanVsAI();
                break;
            case 3:
                int nOfGames = 30, winAI1 = 0, winAI2 = 0, draws = 0, depthAI1 = 2, depthAI2 = 2;

                for(int i = 0; i < nOfGames; i++) {
                    connect4 = new games.connectfour.Engine(1);
                    switch (connect4.playAIVsAI(depthAI1,depthAI2)){
                        case 0: winAI1++;   break;
                        case 1: winAI2++;   break;
                        case 2: draws++;     break;
                    }
                }
                System.out.println("AI 1 (depth " + depthAI1 + ") victories: " + winAI1 + "\nAI 2 (depth " + depthAI2 + ") victories: " + winAI2 + "\nDraws: " + draws);
                break;
        }
//        BriscolaEngine briscolaEngine = new BriscolaEngine();
//        briscolaEngine.play1vsAI();
    }

}
