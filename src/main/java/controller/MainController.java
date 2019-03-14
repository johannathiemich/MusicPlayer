package controller;

import javazoom.jlgui.basicplayer.BasicPlayer;
import model.*;
import view.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        playerView = new MusicPlayerGUI("MyTunes1.0");
        library = new SongLibrary(); //should always be up-to-date with db

        playerControl = new PlayerController(library);
        selectedSong = new Song();

        //setup presentation
        playerView.updateTableView(library);
        playerView.setVisible(true);

        //add listeners for buttons and slider
        playerView.addPlayBtnListener(new PlayBtnListener());
        playerView.addStopBtnListener(new StopBtnListener());
        playerView.addPrevBtnListener(new PrevBtnListener());
        playerView.addNextBtnListener(new NextBtnListener());
        playerView.addVolumeSliderListener(new VolumeSliderListener());

        //Add listeners for standard menu
        playerView.addAddSongMenuItemListener(new AddSongMenuItemListener());
        playerView.addOpenSongMenuItemListener(new OpenSongMenuItemListener());
        playerView.addExitMenuItemListener(new ExitMenuItemListener());
        playerView.addDeleteSongMenuListener(new DeleteSongMenuItemListener());

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
    class PlayBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int playerStatus = playerControl.getPlayerStatus();
            String btnText = playerView.getPlayBtnText();
            System.out.println("[BUTTON] "+btnText+" button is pressed.");

            switch (playerStatus) {
                //Pause Action
                case BasicPlayer.PLAYING :
                    playerControl.pauseSong();
                    btnText = ">";
                    break;
                //Resume Action
                case BasicPlayer.PAUSED :
                    playerControl.resumeSong();
                    btnText = "=";
                    break;
                //Play Action
                case BasicPlayer.STOPPED :
                default:
                    playerControl.setCurrentSong(selectedSong);
                    playerControl.playSong();
                    btnText = "=";
                    System.out.println("playerStatus: "+playerStatus);
                    break;
            }
            playerView.setPlayBtnText(btnText);
        }
    }

    class StopBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[BUTTON] STOP button is pressed.");
            playerView.setPlayBtnText(">");
            playerControl.stopSong();
        }
    }

    class PrevBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("[BUTTON] PREV button is pressed.");
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
            playerView.setPlayBtnText("=");
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
            playerView.setPlayBtnText("=");
        }
    }

    class VolumeSliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            double sliderVal = source.getValue();
            sliderVal = playerControl.convertVolume(sliderVal);
            playerControl.setVolume(sliderVal);
        }
    }

    class AddSongMenuItemListener implements ActionListener {
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

    class OpenSongMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Play song not in library is pressed.");
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            String selectedPath = "";
            if (chooser.showOpenDialog(playerView) == JFileChooser.APPROVE_OPTION) {
                selectedPath = chooser.getSelectedFile().getAbsolutePath();
                playerControl.playSong(new Song(selectedPath));
                playerView.setPlayBtnText("||");
            }
        }
    }

    class ExitMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class DeleteSongPopupItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //What is this part?
            Component c = (Component)e.getSource();
            JPopupMenu popup = (JPopupMenu)c.getParent();
            JTable table = (JTable)popup.getInvoker();
            int selectedRow = playerView.getSongTable().getSelectedRow();

            if ( (selectedRow >= 0) && (selectedRow < library.size()) ) {
                Song selectedSong = library.get(selectedRow);
                System.out.println("[DeleteSong] selectedRow: "+selectedRow+" '"+selectedSong.getPath()+"'");
                library.deleteSong(selectedSong);
                playerView.updateTableView(library);
            } else {
                System.out.println("[DeleteSong] selectedRow: "+selectedRow+", nothing selected to delete.");
            }
        }
    }

    class DeleteSongMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ListDialog.showDialog(playerView, playerView, "Choose the song to be deleted.",
                    "Delete Song from Library", library.convertToString(), library.convertToString()[0],
                    library.convertToString()[0]);
            String selectedSong = ListDialog.getSelectedValue().split("\\[")[0].trim();
            if (library.getSongByPath(selectedSong) != null) {
                library.deleteSong(library.getSongByPath(selectedSong));
                playerView.updateTableView(library);
            }
          }
    }

    /**
     * SelectionListenerForTable detects
     * any row selection change of the table
     * either by mouse or keyboard arrows
     */
    class SelectionListenerForTable implements ListSelectionListener {
        final JTable table = playerView.getSongTable();
        int row;
        boolean isRowInbound;

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
        JTable source;
        int row = 0, col = 0, rowCount = 0;
        boolean isRowInbound;

        @Override
        public void mousePressed(MouseEvent e) {
            // Get the mouse position in the table
            source = (JTable)e.getSource();
            rowCount = source.getRowCount();
            row = source.rowAtPoint( e.getPoint() );
            col = source.columnAtPoint( e.getPoint() );
            isRowInbound = (row >= 0) && (row < rowCount);

            // Right-click Popup Trigger for MacOS
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

            // Click outside of Table to clear selection
            if ( ! isRowInbound ) {
                playerView.getSongTable().clearSelection();
                System.out.println("Deselected row");
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            // Right-click Popup Trigger for Windows
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
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    System.out.println("double clicked");
                    Song selectedSong = library.get(row);
                    playerControl.playSong(selectedSong);
                    playerView.setPlayBtnText("||");
                }
            }
        }
    }


    public void addDragDropToScrollPane() {
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
