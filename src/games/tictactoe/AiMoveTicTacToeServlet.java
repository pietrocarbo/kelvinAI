package games.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "AiMoveTicTacToeServlet", urlPatterns = {"/aimTTT"})
public class AiMoveTicTacToeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> requestData = request.getParameterMap();

        String[] board = new String[9];
        int i = 0;
        for (String s : requestData.get("gridValues[]")) {
//            System.out.print("*" + s + "*\t");
             board[i++] = s;
        }

        int starter = Integer.parseInt(requestData.get("starter")[0]);
        char[][] grid = {{board[0].replace('-', ' ').toCharArray()[0], board[1].replace('-', ' ').toCharArray()[0], board[2].replace('-', ' ').toCharArray()[0]},
                {board[3].replace('-', ' ').toCharArray()[0], board[4].replace('-', ' ').toCharArray()[0], board[5].replace('-', ' ').toCharArray()[0]},
                {board[6].replace('-', ' ').toCharArray()[0], board[7].replace('-', ' ').toCharArray()[0], board[8].replace('-', ' ').toCharArray()[0]}};

        Util.setSeeds('O', 'X');

        Action nextMove = Util.minMaxAlg(grid, 'O', 'X', starter == 0 ? 'O' : 'X');

        int[] aiMove = {nextMove.getRow(), nextMove.getColumn()};

        String strToReturn = aiMove[0] + "" + aiMove[1];
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.write(strToReturn);
        System.out.println("aimTTT servlet sent " + strToReturn);
    }
}
