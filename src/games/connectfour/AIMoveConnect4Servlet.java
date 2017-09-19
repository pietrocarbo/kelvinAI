package games.connectfour;

import main.MovesOrdering;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet(name = "AIMoveConnect4Servlet", urlPatterns = {"/aimC4"})
public class AIMoveConnect4Servlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AIMoveConnect4Servlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int depth = Integer.parseInt(request.getParameterMap().get("depth")[0]);
        LOGGER.finest("request parameters: depth " + depth);

        int counter = 0;
        char[][] boardElements = new char[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                boardElements[i][j] = request.getParameterMap().get("gridValues[]")[counter++].charAt(0);
                boardElements[i][j] = (boardElements[i][j] == '-' ? '_' : boardElements[i][j]);
                LOGGER.finest("board[" + i + "][" + j +"] = " + boardElements[i][j] + "\t");
            }
            LOGGER.finest("\n");
        }

        Action move = Util.heuristicMinMaxAlgorithm(boardElements, 'O',  depth, MovesOrdering.MIDDLE_FIRST);

        String strToReturn = move.getRow() + "" + move.getColumn();
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(strToReturn);
        LOGGER.finest("AI move connect4 servlet sent " + strToReturn);
    }
}
