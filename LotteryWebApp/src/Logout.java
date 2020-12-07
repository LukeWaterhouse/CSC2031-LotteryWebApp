import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

@WebServlet("/Logout")
public class Logout extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        System.out.println("Logging Out...");
        HttpSession session = request.getSession();

        //Creates an Enumeration of the sessions attributes
        Enumeration<String> attributes = session.getAttributeNames();
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        ArrayList<String> attributeArray = new ArrayList<>();
        System.out.println("BEFORE");


        //adds the appropriate session attributes to an array
        while (attributes.hasMoreElements())
        {
            String key = attributes.nextElement();
            System.out.println((key + ": " + session.getValue(key) + "<br>"));
            if (!key.equals("Cipher")&&!key.equals("pair")&& !key.toLowerCase().contains("FirstCheck".toLowerCase())){
                attributeArray.add(key);
            }
        }

        //removes the attributes added
        for (int i=0;i<attributeArray.size();i++){

            System.out.println("Clearing "+attributeArray.get(i)+"...");
            session.removeAttribute(attributeArray.get(i));
        }
        System.out.println("AFTER:");
        session.setAttribute("firstTime",true);

        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }
}
