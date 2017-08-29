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
        String[][] grid = {     {board[0], board[1], board[2]},
                                {board[3], board[4], board[5]},
                                {board[6], board[7], board[8]} };

        int[] aiMove = new Agent(starter).ply(grid);

        String strToReturn = aiMove[0] + "" + aiMove[1];
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.write(strToReturn);
        System.out.println("aimTTT servlet sent " + strToReturn);
    }
}
