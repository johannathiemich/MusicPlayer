package controller;

import javazoom.jlgui.basicplayer.BasicPlayer;
import model.Song;
import model.SongLibrary;
import view.MusicPlayerGUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

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
    //Other Controllers
    private PlayerController playerControl;

    private Song selectedSong;  //different from currentSong

    /**
     * Construct a main controller and initialize all modules
     */
    public MainController() {
        //assign modules
        playerView = new MusicPlayerGUI("controller.MainController Testing");
        library = new SongLibrary(); //should always be up to date with db

        // [TEST1]
        //TESTAddSongToLibrary();

        playerControl = new PlayerController(library);
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
        playerView.addSongItemListener(new AddSongListener());
        playerView.openSongItemListener(new OpenSongListener());
        addDragDropListener();

    }

    // [TEST1] THIS IS TO TEST PLAYER CONTROL ACTIONS --------------------
    // PUT MP3 FILES IN YOUR LOCAL DIRECTORY TO TEST
    public void TESTAddSongToLibrary(){
        SongLibrary testLibrary = new SongLibrary();
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
            //TODO Better call playerControl.playPrevSong() and let it do all the jobs below.

            int prevRow;
            int selectedRow = playerView.getSongTable().getSelectedRow();
            int lastRow = playerView.getSongTable().getRowCount() - 1;

            if(selectedRow == 0) {
                prevRow = lastRow;
            } else {
                prevRow = selectedRow - 1;
            }

            // Update row selection on the view
            playerView.changeTableRowSelection(prevRow);
            // Get the previous song from the library
            Song prevSong = library.get(prevRow);
            selectedSong = prevSong;

            // Set prevSong as a current one and play it
            playerControl.setCurrentSong(prevSong);
            playerControl.playSong();
            // Change the button text
            playerView.setPlayBtnText("Pause");
        }
    }

    class NextBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("NEXT button is pressed.");
            //TODO Better call playerControl.playNextSong() and let it do all the jobs below.

            int nextRow;
            int selectedRow = playerView.getSongTable().getSelectedRow();
            int lastRow = playerView.getSongTable().getRowCount() - 1;

            if(selectedRow == lastRow) {
                nextRow = 0;    //nextRow goes to the top
            } else {
                nextRow = selectedRow + 1;
            }

            // Update row selection on the view
            playerView.changeTableRowSelection(nextRow);
            // Get the previous song from the library
            Song nextSong = library.get(nextRow);
            selectedSong = nextSong;

            // Set prevSong as a current one and play it
            playerControl.setCurrentSong(nextSong);
            playerControl.playSong();
            // Change the button text
            playerView.setPlayBtnText("Pause");
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

    class AddSongListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Add song is pressed.");
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            String selectedPath = "";
            if (chooser.showOpenDialog(playerView) == JFileChooser.APPROVE_OPTION) {
                selectedPath = chooser.getSelectedFile().getAbsolutePath();
                library.addSong(new Song(selectedPath));
                playerView.updateTableView(library);
            }
        }
    }

    class OpenSongListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Play song not in library is pressed.");
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            String selectedPath = "";
            if (chooser.showOpenDialog(playerView) == JFileChooser.APPROVE_OPTION) {
                selectedPath = chooser.getSelectedFile().getAbsolutePath();
                playerControl.playSong(new Song(selectedPath));
            }
        }
    }

    public void addDragDropListener() {
        playerView.getScrollPane().setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        library.addSong(new Song(file.getAbsolutePath()));
                        playerView.updateTableView(library);
                        System.out.println("Added songs via drop");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
}
