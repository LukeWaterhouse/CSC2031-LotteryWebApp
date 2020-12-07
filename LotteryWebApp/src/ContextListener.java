import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class ContextListener implements ServletContextListener {

    //deletes user files when tomcat server stops

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        File dir = new File("UserFiles");
        if (dir.exists()) {
            for (File file : dir.listFiles())
                if (!file.isDirectory())
                    file.delete();

            dir.delete();

        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        File dir = new File("UserFiles");
        if (dir.exists()) {
            for (File file : dir.listFiles())
                if (!file.isDirectory())
                    file.delete();

            dir.delete();

        }
    }
}