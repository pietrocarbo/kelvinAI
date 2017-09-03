package games.connectfour;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "WinningCheckConnect4Servlet", urlPatterns = {"/winC4"})
public class WinningCheckConnect4Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        char[][] boardElements = new char[6][7];
        int counter = 0;

//        for (String key : request.getParameterMap().keySet()) {
//            System.out.print("key " + key + " (lenght " + request.getParameterMap().get(key).length + ")\t");
//            for (String ar : request.getParameterMap().get(key)) {
//                System.out.print("*"  + ar + "*,");
//            }
//            System.out.println();
//        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                boardElements[i][j] = request.getParameterMap().get("gridValues[]")[counter++].charAt(0);
                boardElements[i][j] = (boardElements[i][j] == '-' ? '_' : boardElements[i][j]);
                // System.out.print(boardElements[i][j] + "\t");
            }
            // System.out.println();
        }


        PrintWriter out = response.getWriter();

        int checkResult = Game.gameOverChecks(boardElements, Game.calculateTurn(boardElements));

        String strToReturn = "N";
        if (checkResult == 0) {
            strToReturn = "WKELVIN ha VINTO la partita";

        } else if (checkResult == 1) {
            strToReturn = "WHAI VINTO la partita";

        } else if (checkResult == 2) {
            strToReturn = "DPartita conclusa in PAREGGIO";
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        out.write(strToReturn);
        System.out.println("winC4 servlet sent " + strToReturn);
    }
}
