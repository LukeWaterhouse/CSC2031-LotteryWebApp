import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {

    private Connection conn;
    private PreparedStatement stmt;
    static Methods methods = new Methods();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String algorithm = "SHA-256";

        // MySql database connection info
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        //user and password to database
        String USER = "user";
        String PASS = "password";

        // URLs to connect to database depending on your development approach
        // (NOTE: please change to option 1 when submitting)

        // 1. use this when running everything in Docker using docker-compose
        String DB_URL = "jdbc:mysql://db:3306/lottery";;

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        //String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        // 3. use this when running tomcat and mysql database servers on your machine
        //String DB_URL = "jdbc:mysql://localhost:3306/lottery";

        // get parameter data that was submitted in HTML form (use form attributes 'name')
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String role = request.getParameter("txt_rolereg");
        String passwordUnhashed = request.getParameter("password");
        String password = "";

        //hashes the password inputted by user
        try {
            password = generateHash(passwordUnhashed, algorithm);
            System.out.println(password);
            System.out.println(password.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //set users created details as session attributes
        session.setAttribute("firstname",firstname);
        session.setAttribute("lastname",lastname);
        session.setAttribute("username",username);
        session.setAttribute("email",email);
        session.setAttribute("hashedPass",password);
        session.setAttribute("role",role);


        try{
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Create sql query
            String query = "INSERT INTO userAccounts (Firstname, Lastname, Email, Phone, Username, Pwd, Userrole)"
                    + " VALUES (?, ?, ?, ?, ?, ?,?)";


            // set values into SQL query statement
            stmt = conn.prepareStatement(query);
            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            stmt.setString(3,email);
            stmt.setString(4,phone);
            stmt.setString(5,username);
            stmt.setString(6,password);
            stmt.setString(7,role);

            // execute query and close connection
            stmt.execute();
            conn.close();

            // display account.jsp page with given message if successful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            request.setAttribute("homeMessage", firstname+", you have successfully created an account and can now Login!");
            dispatcher.forward(request, response);

        } catch(Exception se){
            se.printStackTrace();
            // display error.jsp page with given message if unsuccessful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", firstname+", this username/password combination already exists. Please try again");
            dispatcher.forward(request, response);
        }
        finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }
            catch(SQLException se2){}
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    //Method to generate a hash from the users password inputting the data to hash and the algorithm for it
    private static String generateHash(String data, String algorithm) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        byte[] hash = digest.digest(data.getBytes());
        //convert to
        return methods.bytesToStringHex(hash);

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
