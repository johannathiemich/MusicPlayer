package controller;

import javazoom.jlgui.basicplayer.BasicPlayer;
import model.DatabaseHandler;
import model.Song;
import model.SongLibrary;
import view.MusicPlayerGUI;

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
 */
public class MainController {

    //View
    private MusicPlayerGUI playerView;
    //Models
    private SongLibrary library;
    private DatabaseHandler db;
    //Other Controllers
    private BasicPlayer player;
    //private PlayerController playerController;

    private Song currentSong;

    /**
     * Construct a main controller and initialize all modules
     */
    public MainController(){
        //assign modules
        playerView = new MusicPlayerGUI("controller.MainController Testing");
        db = new DatabaseHandler();
        library = new SongLibrary(db.getSongLibrary());
        player = new BasicPlayer();

        //setup presentation
        playerView.updateTableView(library);
        playerView.setVisible(true);

        //add listeners
        addListeners();
        addListenersToTable();
    }

    /**
     * Add Listeners to buttons, volume slider
     */
    public void addListeners(){
        //Buttons pressed
        playerView.startSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PLAY button is pressed.");
                //TODO Play the selected song
            }
        });
        playerView.stopSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("STOP button is pressed.");
                //TODO Stop the playing song
            }
        });
        playerView.prevSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PREVIOUS button is pressed.");
                //TODO Play the previous song of the current song
            }
        });
        playerView.nextSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("NEXT button is pressed.");
                //TODO Play the next song of the current song
            }
        });

        //Slider changed
        playerView.scrollVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                System.out.println("Slider tick: " + source.getValue());
                //TODO Adjust the volume

                if (!source.getValueIsAdjusting()) {
                    int volume = source.getValue();
                    System.out.println("Volume: " + volume);
                }

            }
        });
    }

    /**
     * Add Listeners to table
     */
    public void addListenersToTable(){
        final JTable table = playerView.songTable;

        //Table row selected
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    String title = table.getValueAt(selectedRow, 1).toString();
                    String artist = table.getValueAt(selectedRow, 2).toString();

                    System.out.println("ROW " + selectedRow + " '" + title + " - " + artist + "' is selected.");

                    //TODO currentSong = ...
                }
            }
        });
    }
}
