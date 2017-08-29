package games.connectfourP;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AIMoveConnect4Servlet", urlPatterns = {"/aimC4"})
public class AIMoveConnect4Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        char[][] boardElements = new char[6][7];
        int counter = 0;

        for (String key : request.getParameterMap().keySet()) {
            System.out.print("key " + key + " (lenght " + request.getParameterMap().get(key).length + ")\t");
            for (String ar : request.getParameterMap().get(key)) {
                System.out.print("*"  + ar + "*,");
            }
            System.out.println();
        }

        int depth = Integer.parseInt(request.getParameterMap().get("depth")[0]);
        char starter = request.getParameterMap().get("starter")[0].charAt(0);

        System.out.println("aim servlet: depth " + depth + " starter " + starter + " board...");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                boardElements[i][j] = request.getParameterMap().get("gridValues[]")[counter++].charAt(0);
                boardElements[i][j] = (boardElements[i][j] == '-' ? '_' : boardElements[i][j]);
                System.out.print(boardElements[i][j] + "\t");
            }
            System.out.println();
        }


        int[] aiMove = new Agent('O', starter).heuristicMinMax(boardElements, depth, 2);

        String strToReturn = aiMove[0] + "" + aiMove[1];
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.write(strToReturn);
        System.out.println("aimTTT servlet sent " + strToReturn);
    }
}
