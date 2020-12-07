import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.KeyPair;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

@WebServlet("/checkWin")
public class checkWin extends HttpServlet {

    private Connection conn;
    private Statement stmt;


    KeyPair pair;
    Methods methods = new Methods();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        System.out.println("STARTING checkWin....");
        System.out.println("--------------------------------------------------");
        String winningDraw = "";

        HttpSession session = request.getSession();

        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";


        //sets the first time getting draws check to true as you have entered the lottery draw
        String userFirstCheck = "FirstCheck"+session.getAttribute("username");
        session.setAttribute(userFirstCheck,"true");


        // 1. use this when running everything in Docker using docker-compose
        String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        //String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        // 3. use this when running tomcat and mysql database servers on your machine
        //String DB_URL = "jdbc:mysql://localhost:3306/lottery";

        //gets the winning draw
        try{

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            PreparedStatement stmt = conn.prepareStatement("SELECT * from winningDraws");
            ResultSet results = stmt.executeQuery();

            while (results.next()){

                winningDraw = results.getString("Draw");
                System.out.println("Winning Draw: "+winningDraw);

            }
            }catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if unsuccessful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Database Error, Please try again");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        //get the keyPair from the session
        pair = (KeyPair) session.getAttribute("pair");


        //gets the users password and gets file name from it
        String hashedPassword = (String) session.getAttribute("password");
        StringBuilder filenameBuilder = new StringBuilder();
        String filename;
        for (int i=0;i<20;i++){
            filenameBuilder.append(hashedPassword.charAt(i));
        }
        filename = filenameBuilder.toString();
        filename= filename+".txt";
        System.out.println("first 20 of Hashed:"+filename);


        //decrypts the users draws
        String drawsUnsorted = methods.decryptData(methods.bytesFileReader(filename),pair);
        System.out.println("Message to change: "+drawsUnsorted);


        //turns the draws string to an array of the draws and sets it as session attribute, outputs correct message
        //depending if you won or not
        List<String> items = Arrays.asList(drawsUnsorted.split("\\s*, \\s*"));
        String finalMessage;
        for (int i=0;i<items.size();i++){
            System.out.println(items.get(i));
            if (items.get(i).equals(winningDraw)){
                finalMessage = "YOU WIN! your draw "+winningDraw+" is the winning number!";
                session.setAttribute("winning message",finalMessage);
                System.out.println(finalMessage);
            }else {
                session.setAttribute("winning message", "Unlucky! You did not have the winning draw...");
            }

        }
        session.setAttribute("draws",items);
        session.setAttribute("showDraws",null);

        //clears the file of draws
        methods.clearFile(filename);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        dispatcher.forward(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);

    }
}
