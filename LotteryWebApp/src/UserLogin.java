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


@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {

    private Connection conn;
    private Statement stmt;

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    String algorithm = "SHA-256";
    int loginAttempts = 0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("STARTING userLogin...");
        System.out.println("-------------------------------------------");

        HttpSession session = request.getSession();
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        // sets the user and pass for the database
        String USER = "user";
        String PASS = "password";
        // gets the username and password
        String userLogin = request.getParameter("username");
        String userPassword = request.getParameter("password");

        System.out.println("Users Username: "+userLogin);
        System.out.println("Users Password:"+userPassword);
        System.out.println("draws at login: "+session.getAttribute("draws"));

        try {
            //sets the variable userPassword as the hashed password
            userPassword = generateHash(userPassword, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //initialise the account detail strings
        String email = "";
        String firstName = "";
        String lastName = "";
        String username = "";
        String password = "";
        String role = "";


        // URLs to connect to database depending on your development approach
        // (NOTE: please change to option 1 when submitting)

        // 1. use this when running everything in Docker using docker-compose
        String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        //String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        // 3. use this when running tomcat and mysql database servers on your machine
        //String DB_URL = "jdbc:mysql://localhost:3306/lottery";

        System.out.println("The URL: "+DB_URL);


        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            //check if user and pass exist
            PreparedStatement stmt = conn.prepareStatement("SELECT * from userAccounts WHERE Username = ? AND Pwd = ?");
            stmt.setString(1, userLogin);
            stmt.setString(2,userPassword);
            ResultSet results = stmt.executeQuery();
            String output = "";


            while (results.next()){
                //takes data from the database while there is something present and sets the data to the appropriate variables
                output = results.getString("Username");
                email = results.getString("Email");
                username = results.getString("Username");
                firstName = results.getString("Firstname");
                lastName = results.getString("Lastname");
                password = results.getString("Pwd");
                role = results.getString("Userrole");
            }

            //sets all the users info as session attributes
            session.setAttribute("email",email);
            session.setAttribute("username",username);
            session.setAttribute("firstname",firstName);
            session.setAttribute("lastname",lastName);
            session.setAttribute("password",password);
            session.setAttribute("role",role);

            System.out.println("Users Role:" +role);


            // close connection
            conn.close();


            // initialise dispatcher
            RequestDispatcher dispatcher;

            String userFirstCheck = "FirstCheck"+session.getAttribute("username");

            System.out.println("Login userFirstCheck: "+session.getAttribute(userFirstCheck));

            session.setAttribute("showDraws","false");

            if(session.getAttribute(userFirstCheck)==null||session.getAttribute(userFirstCheck)=="true"){

                session.setAttribute("showDraws",null);

            }



            //if there is a corresponding account print login successful in account.jsp, if not print login unsuccessful in error.jsp
            if (!output.equals("")&& role.equals("admin")){

                System.out.println("User is recognised as admin...");
                session.setAttribute("role","admin");
                dispatcher = request.getRequestDispatcher("/admin/adminHome.jsp");
            }else{



                if(!output.equals("") && role.equals("user")){
                    dispatcher = request.getRequestDispatcher("/account.jsp");
                    request.setAttribute("message", "Login successful!");
                    System.out.println("User is recognised as user...");
                    session.setAttribute("role", "user");

            }else {

                    System.out.println("user not recognised as either role!");

                    dispatcher = request.getRequestDispatcher("/error.jsp");


                    if (session.getAttribute("loginAttempts")==null){
                        session.setAttribute("loginAttempts",1);
                        loginAttempts = 1;
                        request.setAttribute("message", "Login unsuccessful! You have 2 attempts remaining");


                    }else {
                        if ((int) session.getAttribute("loginAttempts")==1){
                            session.setAttribute("loginAttempts", 2);
                            request.setAttribute("message","Login unsuccessful! You have 1 attempt remaining");
                        }else {
                            if ((int) session.getAttribute("loginAttempts")==2){
                                session.setAttribute("loginAttempts", 3);
                                dispatcher = request.getRequestDispatcher("/index.jsp");
                                request.setAttribute("homeMessage","Login unsuccessful! You have failed to login too many Times!");
                                request.setAttribute("tooManyTimes","true");

                            }
                        }
                    }





                }




            }
            dispatcher.forward(request, response);



        } catch (Exception se) {
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
    }


    //generate hash (in this class as needed for finding users password hashed in SQL database)
    private static String generateHash(String data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        byte[] hash = digest.digest(data.getBytes());
        //convert to
        return bytesToStringHex(hash);
    }

    private static String bytesToStringHex(byte[] bytes){
        char [] hexChars = new char[bytes.length*2];
        for (int j=0;j<bytes.length;j++){
            int v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v >>> 4];
            hexChars[j*2+1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
