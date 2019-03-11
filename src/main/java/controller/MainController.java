package controller;

import model.DatabaseHandler;
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

    /**
     * Construct a main controller and initialize all modules
     */
    public MainController() {
        //assign modules
        playerView = new MusicPlayerGUI("controller.MainController Testing");
        db = new DatabaseHandler();
        library = new SongLibrary(db.getSongLibrary());
        playerControl = new PlayerController();

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

    }

    //Listeners
    class PlayBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("PLAY button is pressed.");
//            playerControl.setCurrentSong(testSong);
//            playerControl.playSong();
        }
    }

    class StopBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("STOP button is pressed.");
            //TODO Stop the selected song
        }
    }

    class PrevBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("PREV button is pressed.");
            //TODO Play the previous song of the current song
        }
    }

    class NextBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("NEXT button is pressed.");
            //TODO Play the next song of the current song
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
                String title = table.getValueAt(selectedRow, 1).toString();
                String artist = table.getValueAt(selectedRow, 2).toString();

                System.out.println("ROW " + selectedRow + " '" + title + " - " + artist + "' is selected.");

                //TODO set currentSong to be the Song at selectedRow
            }
        }
    }
}
