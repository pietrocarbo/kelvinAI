package games.connectfour;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "WinningCheckConnect4Servlet", urlPatterns = {"/winC4"})
public class WinningCheckConnect4Servlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(WinningCheckConnect4Servlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        int checkResult = Util.isGameOver(boardElements, Util.getTurn(boardElements), 'O', 'X');

        String strToReturn = "N";
        if (checkResult == 0)                 strToReturn = "WKELVIN ha VINTO la partita";
        else if (checkResult == 1)            strToReturn = "WHAI VINTO la partita";
        else if (checkResult == 2)            strToReturn = "DPartita conclusa in PAREGGIO";

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(strToReturn);

        LOGGER.info("Game over control connect4 servlet sent: " + strToReturn);
    }
}
