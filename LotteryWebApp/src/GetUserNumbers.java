import javax.crypto.Cipher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;

@WebServlet("/GetUserNumbers")
public class GetUserNumbers extends HttpServlet {

    KeyPair pair;
    public Cipher cipher;
    Methods method = new Methods();



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("STARTING GetUserNumbers...");
        System.out.println("------------------------------------------");

        //get the session
        HttpSession session = request.getSession();
        //get the keyPair and cipher from the session
        pair = (KeyPair) session.getAttribute("pair");
        cipher = (Cipher) session.getAttribute("Cipher");
        System.out.println("Cipher: "+cipher);

        //get the filename from the session
        String hashedPassword = (String) session.getAttribute("password");
        System.out.println("Hashed password:" +hashedPassword);
        StringBuilder filenameBuilder = new StringBuilder();
        String filename;

        for (int i=0;i<20;i++){
            filenameBuilder.append(hashedPassword.charAt(i));
        }
        filename = filenameBuilder.toString();
        filename= filename+".txt";
        System.out.println("filename: " +filename);


        //decrypt the users draws and add to session as array
        String drawsUnsorted = method.decryptData(method.bytesFileReader(filename),pair);
        List<String> items = Arrays.asList(drawsUnsorted.split("\\s*, \\s*"));

        System.out.println("List of users numbers: ");
        for (int i=0;i<items.size();i++){
            System.out.println(items.get(i));
        }
        request.setAttribute("draws",items);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        dispatcher.forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
