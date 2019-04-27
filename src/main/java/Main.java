import controller.MainController;
import database.DatabaseHandler;

public class Main {

    public static String appName = "MyTunes 3.0";

    public static void main(String[] args) {

        DatabaseHandler handler = DatabaseHandler.getInstance();

        MainController controller = new MainController(appName);

    }
}
