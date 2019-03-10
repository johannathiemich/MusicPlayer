import javazoom.jlgui.basicplayer.BasicPlayer;

/**
 * This controller is a Supervising Controller or messenger
 * between Model(data,entity) and View(interface,gui)
 * based on the MVC design pattern.
 * It prevents direct interactions between data and interfaces.
 */
public class MainController {

    //View
    private MusicPlayerGUI playerView;

    //Models
    private SongLibrary library;
    private DatabaseHandler db;

    //Other Controllers
    private BasicPlayer player;

    /**
     * Construct a main controller and initialize all modules
     */
    public MainController(){
        playerView = new MusicPlayerGUI("MyTunes 0.0");
        library = new SongLibrary();
        db = new DatabaseHandler();
        player = new BasicPlayer();
    }


    //TODO add inter classes of ActionListener for Table, Buttons, Slider
}
