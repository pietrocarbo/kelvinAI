package games.briscola;

import decks.forty.Card;
import decks.forty.Hand;
import decks.forty.Rank;
import decks.forty.Suit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "AIMoveBriscolaServlet", urlPatterns = {"/aimBRI"})
public class AIMoveBriscolaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  // TODO
//
////        for (String key : request.getParameterMap().keySet()) {
////            System.out.print("key " + key + " (lenght " + request.getParameterMap().get(key).length + ")\t");
////            for (String ar : request.getParameterMap().get(key)) {
////                System.out.print("*"  + ar + "*,");
////            }
////            System.out.println();
////        }
//
//        Card briscola = new Card(Rank.stringToRank(request.getParameter("briscola[name]")), Suit.stringToSuit(request.getParameter("briscola[suit]")));
//
//        Hand myHand = new Hand(new ArrayList<Card>());
//        for (int i = 0; i < Integer.parseInt(request.getParameter("nMyCards")); i++) {
//            myHand.addOne(new Card(Rank.stringToRank(request.getParameter("myCards[" + i + "][name]")), Suit.stringToSuit(request.getParameter("myCards[" + i + "][suit]"))));
//        }
//
//        Hand board = new Hand(new ArrayList<Card>());
//        for (int i = 0; i < Integer.parseInt(request.getParameter("nBoardCards")); i++) {
//            board.addOne(new Card(Rank.stringToRank(request.getParameter("board[" + i + "][name]")), Suit.stringToSuit(request.getParameter("board[" + i + "][suit]"))));
//        }
//
//        Hand unknownCards = new Hand(new ArrayList<Card>());
//        for (int i = 0; i < Integer.parseInt(request.getParameter("nUnknownCards")); i++) {
//            unknownCards.addOne(new Card(Rank.stringToRank(request.getParameter("unknownCards[" + i + "][name]")), Suit.stringToSuit(request.getParameter("unknownCards[" + i + "][suit]"))));
//        }
//
//        Player kelvin = new AI("Kelvin", myHand, 0);
////        kelvin.setMinMaxParameter(4, true, 50);
////        Card cardToPlay = kelvin.play(board, briscola, unknownCards, 0, Integer.parseInt(request.getParameter("turns")), Integer.parseInt(request.getParameter("nOppCards")), 0);
//
////        Util.monteCarloMethod(
////                0,
////                3,
////                true,
////                50,
////                myHand,
////                briscola,
////                board,
////                unknownCards,
////                0,
////                0,
////                Integer.parseInt(request.getParameter("nOppCards")),
////                0);
//
//        // System.out.println("aiBRI chose to play " + cardToPlay);
//
////        String strToReturn = cardToPlay.getRank() + "di" + cardToPlay.getSuit();
//        response.setContentType("text/plain");
//        response.setCharacterEncoding("UTF-8");
//
//        response.getWriter().write(strToReturn);
//        System.out.println("aimBRI servlet sent: " + strToReturn);
    }
}
