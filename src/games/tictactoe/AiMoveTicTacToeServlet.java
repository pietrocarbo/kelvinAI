package games.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "AiMoveTicTacToeServlet", urlPatterns = {"/aimTTT"})
public class AiMoveTicTacToeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AiMoveTicTacToeServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int i = 0;
        String[] board = new String[9];
        for (String s : request.getParameterMap().get("gridValues[]")) {
             board[i++] = s;
        }

        char[][] grid =
            {
                {board[0].replace('-', ' ').toCharArray()[0], board[1].replace('-', ' ').toCharArray()[0], board[2].replace('-', ' ').toCharArray()[0]},
                {board[3].replace('-', ' ').toCharArray()[0], board[4].replace('-', ' ').toCharArray()[0], board[5].replace('-', ' ').toCharArray()[0]},
                {board[6].replace('-', ' ').toCharArray()[0], board[7].replace('-', ' ').toCharArray()[0], board[8].replace('-', ' ').toCharArray()[0]}
            };

        Action nextMove = Util.minMaxAlgorithm(grid, 'O', 'X', true);

        String strToReturn = nextMove.getRow() + "" + nextMove.getColumn();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(strToReturn);
        LOGGER.info("AI move tictactoe servlet sent " + strToReturn);
    }
}
