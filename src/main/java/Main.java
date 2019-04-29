import controller.MainController;
import database.DatabaseHandler;
import view.SongListView;

public class Main {

    public static String appName = "MyTunes 3.0";

    public static void main(String[] args) {

       // DatabaseHandler handler = DatabaseHandler.getInstance();
        //boolean[] array = new boolean[]{true, false, true, false, true};
        //boolean[] returnArray = new boolean[5];
        //handler.saveShowHideColumns(array);
        //returnArray = handler.getShowHideColumns();


        MainController controller = new MainController(appName);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                DatabaseHandler.getInstance().saveShowHideColumns(SongListView.getColumnVisibility());
            }
        }));


    }
}
