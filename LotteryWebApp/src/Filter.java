import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


@WebFilter(filterName = "Filter")

public class Filter implements javax.servlet.Filter {

    //Server Filter..

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getServletContext().log("Filter Started");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        boolean invalid = false;
        Map params = servletRequest.getParameterMap();
        System.out.println(params.values());

        if (params != null){
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext()){

                String key = (String) iter.next();
                String[] values = (String[]) params.get(key);

                for (int i=0; i<values.length;i++){
                    if (checkChars(values[i])){
                        invalid=true;
                        break;
                    }
                }
                if (invalid){
                    break;
                }
            }
            if (invalid){
                try{
                    servletRequest.getRequestDispatcher("/error.jsp").forward(servletRequest, servletResponse);
                    servletRequest.setAttribute("message", "use of bad chars!");
                    System.out.println("Filter Error!");

                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }



    }
    public static boolean checkChars(String value) {
        boolean invalid = false;
        String[] badChars = { "<", ">", "script", "alert", "truncate", "delete",
                "insert", "drop", "null", "xp_", "<>", "!", "{", "}", "`",
                "input" };

        for(int i = 0; i < badChars.length; i++){
            if(value.contains(badChars[i])){
                invalid = true;
                break;
            }
        }
        return invalid;
    }

    @Override
    public void destroy() {

    }
}
