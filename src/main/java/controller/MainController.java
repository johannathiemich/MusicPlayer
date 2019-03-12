package controller;

import model.Song;
import model.SongLibrary;
import database.DatabaseHandler;
import view.MusicPlayerGUI;
import javazoom.jlgui.basicplayer.BasicPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This controller is a Supervising Controller or messenger
 * between Model(data,entity) and View(interface,gui)
 * based on the MVC design pattern.
 * It makes data and interfaces are independent from each other.
 * ActionListeners are here.
 */
public class MainController {

    //View
    private MusicPlayerGUI playerView;
    //Models
    private SongLibrary library;
    private DatabaseHandler db;
    //Other Controllers
    private PlayerController playerControl;

    private Song selectedSong;  //different from currentSong

    /**
     * Construct a main controller and initialize all modules
     */
    public MainController() {
        //assign modules
        playerView = new MusicPlayerGUI("controller.MainController Testing");
        db = new DatabaseHandler();
        library = new SongLibrary(db.getSongLibrary()); //should always be up to date with db
        playerControl = new PlayerController();
        selectedSong = new Song();

        //setup presentation
        playerView.updateTableView(library);
        playerView.setVisible(true);

        //add listeners
        playerView.addPlayBtnListener(new PlayBtnListener());
        playerView.addStopBtnListener(new StopBtnListener());
        playerView.addPrevBtnListener(new PrevBtnListener());
        playerView.addNextBtnListener(new NextBtnListener());
        playerView.addVolumeSliderListener(new VolumeSliderListener());
        playerView.addTableListener(new TableListener());

        //test();
    }

    //THIS IS FOR TESTING ------------------------- PLAYER WORKS GREAT!
    //PUT MP3 FILES IN YOUR LOCAL DIRECTORY TO TEST
    SongLibrary testLibrary = new SongLibrary();
    Song testSong = new Song();
    public void test(){
        System.out.println("========= TESTING! MP3files in local directory");
        testLibrary.addSong(new Song("/Users/sella/downloads/mp3/cinemaparadiso.mp3"));
        testLibrary.addSong(new Song("/Users/sella/downloads/mp3/Jamaica Farewell by Harry Belafonte.mp3"));
        testLibrary.addSong(new Song("invalid file Path"));
        testLibrary.addSong(new Song("/Users/sella/downloads/mp3/HONOLULU CITY LIGHTS KAPONO.mp3"));
        testLibrary.addSong(new Song("/Users/sella/downloads/mp3/03 Cotton Fields.mp3"));
        playerControl = new PlayerController(testLibrary);
        playerView.updateTableView(testLibrary);
        library = testLibrary;
    }

    //Listeners
    class PlayBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int playerStatus = playerControl.getPlayerStatus();
            String btnText = playerView.getPlayBtnText();
            System.out.println(btnText+" button is pressed.");

            switch (playerStatus) {
                //Pause Action
                case BasicPlayer.PLAYING :
                    playerControl.pauseSong();
                    btnText = "Resume";
                    break;
                //Resume Action
                case BasicPlayer.PAUSED :
                    playerControl.resumeSong();
                    btnText = "Pause";
                    break;
                //Play Action
                case BasicPlayer.STOPPED :
                default:
                    playerControl.setCurrentSong(selectedSong);
                    playerControl.playSong();
                    btnText = "Pause";
                    System.out.println("playerStatus: "+playerStatus);
                    break;
            }
            playerView.setPlayBtnText(btnText);
        }
    }

    class StopBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("STOP button is pressed.");
            playerView.setPlayBtnText("Play");
            playerControl.stopSong();
        }
    }

    class PrevBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("PREV button is pressed.");
            //TODO Play the previous song in the library
        }
    }

    class NextBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("NEXT button is pressed.");
            //TODO Play the next song in the library
        }
    }

    class VolumeSliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            int volume = source.getValue();
            System.out.println("Slider tick: " + volume);
            //TODO Adjust the volume of the player

            /*
            if (!source.getValueIsAdjusting()) {
                System.out.println("Volume: " + volume);
            }
            */

        }
    }

    class TableListener implements ListSelectionListener {
        final JTable table = playerView.getSongTable();

        //Table row selected
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                System.out.print("Row "+selectedRow+" is selected. ");
                try {
                    String title = table.getValueAt(selectedRow, 1).toString();
                    String artist = table.getValueAt(selectedRow, 2).toString();
                    System.out.print(title + " - " + artist);
                } finally {
                    System.out.println();
                }

                selectedSong = library.get(selectedRow);
            }
        }
    }
}
