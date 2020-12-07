import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import java.security.*;


@WebServlet("/AddUserNumbers")
public class AddUserNumbers extends HttpServlet {


    Cipher cipher;
    KeyPair pair;
    Methods method = new Methods();

    public AddUserNumbers(){
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //Stars session object
        HttpSession session = request.getSession();

        //sets the keypair for the session
        if (session.getAttribute("firstCheck")==null){
            try {
                KeyPairGenerator keypair = KeyPairGenerator.getInstance("RSA");
                pair = keypair.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            session.setAttribute("firstCheck","true");
            session.setAttribute("pair",pair);
        }


        System.out.println("STARTING AddUserNumbers....");
        System.out.println("------------------------------------------");

        //Sets the users first time for generating name
        String userFirstCheck = "FirstCheck"+session.getAttribute("username");
        System.out.println("First time generating user reference: " +userFirstCheck);

        //gets the randomNum generated from the session and prints it to console
        String data = request.getParameter("LotteryText");
        System.out.println("Inputted lottery number: "+data);

        //get the hashed password for the filename from the session
        String hashedPassword = (String) session.getAttribute("password");

        //builds the correct filename using the hashed password
        StringBuilder filenameBuilder = new StringBuilder();
        String filename;

        for (int i=0;i<20;i++){
            filenameBuilder.append(hashedPassword.charAt(i));
        }

        //sets the final form of the filename
        filename = filenameBuilder.toString();
        filename= filename+".txt";
        //sets the filename as a session attribute
        session.setAttribute("filename",filename);

        // runs if the user hasn't generated their first number to add to draws..
        if(session.getAttribute(userFirstCheck)==null||session.getAttribute(userFirstCheck)=="true"){

            System.out.println("FIRST TIME IF STATEMENT...");

            //once one number added sets check to allow user to press getDraws button
            session.setAttribute("showDraws","true");

            //sets check for first draw added to false
            session.setAttribute(userFirstCheck,"false");

            //gets the keypair from the session
            pair = (KeyPair) session.getAttribute("pair");

            //encrypts the data and writes it to a file
            method.bytesFileWriter(filename,method.encryptData(data,pair));

            //sets the keypair and cipher to session objects
            session.setAttribute("pair",pair);
            session.setAttribute("Cipher",cipher);

            //sends first draw added message to the accounts page
            request.setAttribute("message", "First Draw Added!");
            session.setAttribute("winning message", null);


            //refreshes the account page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            dispatcher.forward(request, response);

        }else {

            //Runs if user has already added one file to their draws...
            System.out.println("NOT FIRST TIME IF STATEMENT...");

            //gets the filename from the session
            filename = (String) session.getAttribute("filename");

            //get the cipher instance
            try {
                cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            }catch (NoSuchAlgorithmException | NoSuchPaddingException ex){
                ex.printStackTrace();
            }

            //gets the keypair from the session
            pair = (KeyPair) session.getAttribute("pair");

            //decrypts the data to be added to the new inputted draw
            String decrypted = method.decryptData(method.bytesFileReader(filename),pair);
            String newmessage = decrypted + ", "+ data;

            System.out.println("Old number plus new number: "+newmessage);

            //encrypts the new information to the file
            method.bytesFileWriter(filename,method.encryptData(newmessage,pair));

            //sets the keypair and cipher to session objects
            session.setAttribute("pair",pair);
            session.setAttribute("Cipher",cipher);

            //sets message to be shown on accounts.jsp
            request.setAttribute("message", "New Draw Added!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            dispatcher.forward(request, response);
            
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
