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
import java.util.logging.Logger;


@WebServlet(name = "AIMoveBriscolaServlet", urlPatterns = {"/aimBRI"})
public class AIMoveBriscolaServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AIMoveBriscolaServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        for (String key : request.getParameterMap().keySet()) {
//            System.out.print("key " + key + " (lenght " + request.getParameterMap().get(key).length + ")\t");
//            for (String ar : request.getParameterMap().get(key)) {
//                System.out.print("*"  + ar + "*,");
//            }
//            System.out.println();
//        }

        Card briscola = new Card(Rank.stringToRank(request.getParameter("briscola[name]")), Suit.stringToSuit(request.getParameter("briscola[suit]")));

        Hand myHand = new Hand(new ArrayList<>());
        for (int i = 0; i < Integer.parseInt(request.getParameter("nMyCards")); i++) {
            myHand.addOne(new Card(Rank.stringToRank(request.getParameter("myCards[" + i + "][name]")), Suit.stringToSuit(request.getParameter("myCards[" + i + "][suit]"))));
        }

        Hand board = new Hand(new ArrayList<>());
        for (int i = 0; i < Integer.parseInt(request.getParameter("nBoardCards")); i++) {
            board.addOne(new Card(Rank.stringToRank(request.getParameter("board[" + i + "][name]")), Suit.stringToSuit(request.getParameter("board[" + i + "][suit]"))));
        }

        Hand unknownCards = new Hand(new ArrayList<>());
        for (int i = 0; i < Integer.parseInt(request.getParameter("nUnknownCards")); i++) {
            unknownCards.addOne(new Card(Rank.stringToRank(request.getParameter("unknownCards[" + i + "][name]")), Suit.stringToSuit(request.getParameter("unknownCards[" + i + "][suit]"))));
        }

        Hand collectedHuman = new Hand(new ArrayList<>());
        for (int i = 0; i < Integer.parseInt(request.getParameter("nHumanCollected")); i++) {
            collectedHuman.addOne(new Card(Rank.stringToRank(request.getParameter("humanCollected[" + i + "][name]")), Suit.stringToSuit(request.getParameter("humanCollected[" + i + "][suit]"))));
        }

        int depth = Integer.parseInt(request.getParameterMap().get("depth")[0]);
        int numberOfRandomDeals = Integer.parseInt(request.getParameterMap().get("numberOfRandomDeals")[0]);

        Player kelvin = new AI(0, "Kelvin-Servlet", myHand, depth, false, numberOfRandomDeals);
        kelvin.setHybridStrategy(true);

        Card cardToPlay = kelvin.play(
                board,
                briscola,
                unknownCards,
                Integer.parseInt(request.getParameter("turns")),
                collectedHuman,
                Integer.parseInt(request.getParameter("nOppCards")));

        LOGGER.fine("search chose card " + cardToPlay);

        String strToReturn = cardToPlay.getRank() + "di" + cardToPlay.getSuit();
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(strToReturn);
        LOGGER.info("servlet sent: " + strToReturn);
    }
}
