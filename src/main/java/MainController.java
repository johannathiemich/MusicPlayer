import javazoom.jlgui.basicplayer.BasicPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        playerView = new MusicPlayerGUI("MainController Testing");
        library = new SongLibrary();
        db = new DatabaseHandler();
        player = new BasicPlayer();

        playerView.setVisible(true);

        addListeners();

    }

    //TODO add inner classes of ActionListener for Table, Buttons, Slider
    public void addListeners(){
        //Buttons
        playerView.startSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PLAY button is pressed.");
            }
        });
        playerView.stopSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("STOP button is pressed.");
            }
        });
        playerView.prevSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PREVIOUS button is pressed.");
            }
        });
        playerView.nextSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("NEXT button is pressed.");
            }
        });

        //Slider
        playerView.scrollVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                System.out.println("Slider tick: " + source.getValue());

                if (!source.getValueIsAdjusting()) {
                    int volume = source.getValue();
                    System.out.println("Volume: " + volume);
                }

            }
        });
    }
}
