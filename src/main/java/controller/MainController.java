package controller;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import model.Song;
import model.SongLibrary;
import view.ListDialog;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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
        playerView = new MusicPlayerGUI("MyTunes1.0");
        library = new SongLibrary(); //should always be up-to-date with db

        playerControl = new PlayerController(library, playerView);
        selectedSong = new Song();

        //setup presentation
        playerView.updateTableView(library);
        playerView.setVisible(true);

        //add listeners for buttons and slider
        playerView.addPlayerControlButtonListener(new PlayerControlButtonListener());
        playerView.addVolumeSliderListener(new VolumeSliderListener());

        //Add listeners for standard menu
        playerView.addOpenSongMenuItemListener(new OpenSongMenuItemListener());
        playerView.addAddSongMenuItemListener(new AddSongMenuItemListener());
        playerView.addDeleteSongMenuListener(new DeleteSongMenuItemListener());
        playerView.addAboutMenuItemListener(new AboutMenuItemListener());
        playerView.addExitMenuItemListener(new ExitMenuItemListener());

        //Add listeners for popup menu
        playerView.addDeleteSongPopupListener(new DeleteSongPopupItemListener());
        playerView.addAddSongPopupListener(new AddSongMenuItemListener());

        //Add listener for table
        playerView.addSelectionListenerForTable(new SelectionListenerForTable());
        playerView.addMouseListenerForTable(new MouseListenerForTable());

        //Add listener for drag and drop area
        addDragDropToScrollPane();

    }

    //Listeners

    /**
     * PlayerControlButtonListener class implements
     * the actions of Play/Stop/Prev/Next buttons
     */
    class PlayerControlButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the name of event source component
            String btnName = ((JButton)e.getSource()).getName();

            int playerStatus = playerControl.getPlayerStatus();

            if (btnName.equals("play")) {
                //PLAY button actions
                System.out.println("[BUTTON] Play/Pause/Resume button is pressed.");

                switch (playerStatus) {
                    //Pause Action
                    case BasicPlayer.PLAYING :
                        playerControl.pauseSong();
                        break;
                    //Resume Action
                    case BasicPlayer.PAUSED :
                        playerControl.resumeSong();
                        break;
                    //Play Action
                    case BasicPlayer.STOPPED :
                    default:
                        if(playerView.isAnyRowSelected()) {
                            playerControl.setCurrentSong(selectedSong);
                            playerControl.playSong();
                        }else{
                            System.out.println("nothing selected, playing the first song in the library..");
                            playerControl.playSong(library.get(0));
                        }
                        break;
                }
            } else if (btnName.equals("stop")) {
                //STOP button action
                System.out.println("[BUTTON] STOP button is pressed.");
                playerControl.stopSong();
            }
            else if (btnName.equals("prev")) {
                //PREV button action
                System.out.println("[BUTTON] PREV button is pressed.");
                playerControl.playPrevSong();
            }
            else if (btnName.equals("next")) {
                //NEXT button action
                System.out.println("[BUTTON] NEXT button is pressed.");
                playerControl.playNextSong();
            }
            else {
                System.out.println("none of play/stop/prev/next buttons");
            }
        }
    }

    /**
     * Slider value increases when slid to the right, decreases when slid to the left.
     */
    class VolumeSliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            double sliderVal = source.getValue();
            playerControl.setVolume(sliderVal);
        }
    }

    /**
     * User chooses in the menu item to add a song.
     * Directory pops up and user can find a song to add to the library.
     * If the file is not a valid mp3 file, an error message will appear.
     */
    class AddSongMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Add song is pressed.");
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            String filePath = "";
            if (chooser.showOpenDialog(playerView) == JFileChooser.APPROVE_OPTION) {
                filePath = chooser.getSelectedFile().getAbsolutePath();
                try {
                    //TODO Make Song Constructor handle invalid mp3 files (1)
                    Mp3File mp3file = new Mp3File(filePath);
                    library.addSong(new Song(filePath));
                    playerView.updateTableView(library);
                    playerControl.updateLibrary(library);
                    System.out.println("[Add Song by File Chooser] SUCCESS! '"+filePath+"'");
                } catch (UnsupportedTagException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InvalidDataException e1) {
                    System.out.println("[Add Song by File Chooser] ERROR: invalid file '"+filePath+"'");
                    JOptionPane.showMessageDialog(null, "This file is not a valid mp3 file.");
                }
            }
        }
    }

    /**
     * User chooses in the menu item to play a song that is not in the library.
     * Directory pops up and user can find a song to play.
     * If the file is not a valid mp3 file, an error message will appear.
     */
    class OpenSongMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Play song not in library is pressed.");
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            String selectedPath = "";
            if (chooser.showOpenDialog(playerView) == JFileChooser.APPROVE_OPTION) {
                selectedPath = chooser.getSelectedFile().getAbsolutePath();
                try {
                    //TODO Make Song Constructor handle invalid mp3 files (2)
                    Mp3File mp3file = new Mp3File(selectedPath);
                    playerControl.playSong(new Song(selectedPath));
                } catch (UnsupportedTagException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InvalidDataException e1) {
                    JOptionPane.showMessageDialog(null, "This file is not a valid mp3 file.");
                }
            }
        }
    }

    /**
     * User chooses the menu item to exit the program.
     */
    class ExitMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * User right clicks on a song and has the option to delete the song they clicked on.
     */
    class DeleteSongPopupItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //What is this part?
            int selectedRow = playerView.getSongTable().getSelectedRow();
            boolean isRowInbound = (selectedRow >= 0) && (selectedRow < library.size());

            if ( isRowInbound ) {
                Song selectedSong = library.get(selectedRow);
                System.out.println("row:"+selectedRow+" is selected to delete.");
                library.deleteSong(selectedSong);
                playerView.updateTableView(library);
                playerControl.updateLibrary(library);
            } else {
                System.out.println("row:"+selectedRow+", nothing selected to delete.");
            }
        }
    }

    /**
     * User chooses the menu item to delete a song.
     * The library list will appear and the user will choose which song they would
     * like to delete from the library.
     */
    class DeleteSongMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (library.size() > 0 ) {
                ListDialog.showDialog(playerView, playerView, "Choose the song to be deleted.",
                        "Delete Song from Library", library.convertToString(), null,
                        library.convertToString()[0]);
                String selectedSong = "";
                if (ListDialog.getSelectedValue() != null) {
                    selectedSong = ListDialog.getSelectedValue().split("\\[")[0].trim();
                    if (library.getSongByPath(selectedSong) != null) {
                        library.deleteSong(library.getSongByPath(selectedSong));
                        playerView.updateTableView(library);
                        playerControl.updateLibrary(library);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "There is no song in the " +
                        "library to be deleted.");
            }
          }
    }

    /**
     * SelectionListenerForTable detects
     * any row selection change of the table
     * either by mouse or keyboard arrows
     */
    class SelectionListenerForTable implements ListSelectionListener {
        private JTable table = playerView.getSongTable();
        private int row;
        private boolean isRowInbound;

        //Table row selected
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                row = table.getSelectedRow();
                isRowInbound = row >= 0 && row < table.getRowCount();

                if (isRowInbound) {
                    selectedSong = library.get(row);
                    System.out.print("[Table] selectedRow:"+row);
                    System.out.println(", [" + selectedSong.getTitleAndArtist() + "]");
                }
            }
        }
    }

    /**
     * MouseListenerForTable covers:
     * 1. popup trigger for right-click inside of table
     * 2. popup trigger for right-click outside of table
     * 3. clear selections for left-click outside of table
     * 4. double-click to play the song
     */
    class MouseListenerForTable extends MouseAdapter {
        private JTable source;
        private int row = 0, col = 0, rowCount = 0;
        private boolean isRowInbound;

        @Override
        public void mousePressed(MouseEvent e) {
            // Get the mouse position in the table
            source = (JTable)e.getSource();
            rowCount = source.getRowCount();
            row = source.rowAtPoint( e.getPoint() );
            col = source.columnAtPoint( e.getPoint() );
            isRowInbound = (row >= 0) && (row < rowCount);

            // Right-click Popup Trigger (for MacOS)
            if (e.isPopupTrigger() && library.size() > 0)
            {
                if ( isRowInbound ) {   //right click in table
                    System.out.println("right clicked in table. row:"+row);
                    source.changeSelection(row, col, false, false);
                    playerView.getPopUpMenu().show(e.getComponent(), e.getX(), e.getY());
                } else {                //right click out of table
                    System.out.println("right clicked outside of the table");
                    playerView.getPopUpMenuInBlankspace().show(e.getComponent(), e.getX(), e.getY());
                }
            }

            // Left-click outside of table to clear selection
            if ( ! isRowInbound && ! e.isPopupTrigger()) {
                playerView.getSongTable().clearSelection();
                System.out.println("Cleared row selections.");
            }
        }


        @Override
        public void mouseReleased(MouseEvent e)
        {
            // Right-click Popup Trigger (for Windows)
            if (e.isPopupTrigger())
            {
                if ( isRowInbound ) {   //right click in table
                    System.out.println("right clicked inside of the table");
                    source.changeSelection(row, col, false, false);
                    playerView.getPopUpMenu().show(e.getComponent(), e.getX(), e.getY());
                } else {                //right click out of table
                    System.out.println("right clicked outside of the table");
                    playerView.getPopUpMenuInBlankspace().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Double-click on a song to play
            if ( isRowInbound ) {
                if (e.getClickCount() == 2 && !e.isConsumed() && !e.isPopupTrigger()) {
                    System.out.println("double clicked");
                    Song selectedSong = library.get(row);
                    playerControl.playSong(selectedSong);
                }
            }
        }
    }

    /**
     * User can drag and drop mp3 files from their directory into the library.
     */
    public void addDragDropToScrollPane() {
        playerView.getScrollPane().setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                boolean invalidFilesFound = false;
                try {
                    String filePath = "";
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        filePath = file.getAbsolutePath();
                        try {
                            //TODO Make Song Constructor handle invalid mp3 files (3)
                            Mp3File mp3file = new Mp3File(filePath);
                            library.addSong(new Song(filePath));
                            playerView.updateTableView(library);
                            playerControl.updateLibrary(library);
                        } catch (UnsupportedTagException e1) {
                            invalidFilesFound = true;
                        } catch (IOException e1) {
                            invalidFilesFound = true;
                        } catch (InvalidDataException e1) {
                            invalidFilesFound = true;
                        }
                    }
                    if (invalidFilesFound) {
                        JOptionPane.showMessageDialog(null, "Some files have not been added " +
                                "since they are not valid mp3 files.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    /**
     * Listener for 'About' menu item
     */
    public class AboutMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = "About";
            String appName = "MyTunes1.0";
            String teamInfo = "[CECS543 Team6]\nSella Bae\nBrett Rexius\nJohanna Thiemich";
            String date = "3/14/2019";
            String msg = appName+"\n"+date+"\n\n"+teamInfo;
            JOptionPane.showMessageDialog(playerView, msg, title, JOptionPane.PLAIN_MESSAGE);
        }
    }
}
