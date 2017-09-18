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

@WebServlet(name = "WinningCheckTicTacToeServlet", urlPatterns = {"/winTTT"})
public class WinningCheckTicTacToeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(WinningCheckTicTacToeServlet.class.getName());
    private String winningMessage;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int i = 0;
        String[] boardElements = new String[9];
        for (String key : request.getParameterMap().keySet()) {
            for (String s : request.getParameterMap().get(key)) {
                boardElements[i++] = s;
            }
        }

        String[] checkResult = checkAndDeclare(boardElements);
        String strToReturn = checkResult[0] + "" + checkResult[1];

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(strToReturn);

        LOGGER.info("Game over controls tictactoe servlet sent " + strToReturn);
    }

    public String[] checkAndDeclare (String[] board) {
        String[][] grid = {     {board[0], board[1], board[2]},
                                {board[3], board[4], board[5]},
                                {board[6], board[7], board[8]} };

        if (declareThree(grid, 0,0, 0, 1, 0,2))   return new String[]{"W", this.winningMessage};
        if (declareThree(grid, 1,0, 1, 1, 1,2))   return new String[]{"W", this.winningMessage};
        if (declareThree(grid, 2,0, 2, 1, 2,2))   return new String[]{"W", this.winningMessage};

        if (declareThree(grid, 0,0, 1, 0, 2,0))   return new String[]{"W", this.winningMessage};
        if (declareThree(grid, 0,1, 1, 1, 2,1))   return new String[]{"W", this.winningMessage};
        if (declareThree(grid, 0,2, 1, 2, 2,2))   return new String[]{"W", this.winningMessage};

        if (declareThree(grid, 0,0, 1, 1, 2,2))   return new String[]{"W", this.winningMessage};
        if (declareThree(grid, 2,0, 1, 1, 0,2))   return new String[]{"W", this.winningMessage};

        if (calculateTurns(grid) == 9)             return new String[]{"D", "Partita conclusa con pareggio"};

        return new String[]{"N", ""};
    }

    private int calculateTurns (String[][] grid) {
        int turns = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0;  j < 3; j++) {
                if (!grid[i][j].equals("-"))    turns++;
            }
        }
        return turns;
    }

    private boolean declareThree (String[][] grid, int r1, int c1, int r2, int c2, int r3, int c3) {
        String s1 = grid[r1][c1];
        String s2 = grid[r2][c2];
        String s3 = grid[r3][c3];
        int check = checkThree(s1, s2, s3);
        if (check == 0) {
            this.winningMessage = "KELVIN VINCE in " + r1 + "," + c1 + " - " + r2 + "," + c2 + " - " + r3 + "," + c3;
            return true;
        } else if (check == 1) {
            this.winningMessage = "HAI VINTO in " + r1 + "," + c1 + " - " + r2 + "," + c2 + " - " + r3 + "," + c3;
            return true;
        }
        return false;
    }

    public int checkThree (String e1, String e2, String e3) {
        String test = e1 + e2 + e3;
        if (test.equals("OOO")) return 0;
        else if (test.equals("XXX")) return 1;
        else return -1;
    }
}

