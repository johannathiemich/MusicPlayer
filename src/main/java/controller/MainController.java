package controller;

import javazoom.jlgui.basicplayer.BasicPlayer;
import model.Playlist;
import model.PlaylistLibrary;
import model.Song;
import model.SongLibrary;
import view.ColorTheme;
import view.MusicPlayerGUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller is a Supervising Controller or messenger
 * between Model(data,entity) and View(interface,gui)
 * based on the MVC design pattern.
 * It makes data and interfaces are independent from each other.
 * ActionListeners are here.
 */
@SuppressWarnings("ALL")
public class MainController {

    private String appName;

    //View
    private MusicPlayerGUI playerView;

    //Models
    private SongLibrary library;
    private PlaylistLibrary playlistLibrary;

    //Other Controllers
    private PlayerController playerControl;

    //...
    private Song selectedSong;  //different from currentSong
    private String selectedPlaylistName;

    //Playlist Windows
    private ArrayList<MusicPlayerGUI> playlistWindowArray;
    private String focusedWindowName = "main";
    private MusicPlayerGUI focusedWindow;


    /**
     * Construct a main controller and initialize all modules
     */
    public MainController(String appName) {
        this.appName = appName;

        //assign modules
        playerView = new MusicPlayerGUI(appName, 800, 600, "main", "library");
        library = new SongLibrary(); //should always be up-to-date with db
        playlistLibrary = new PlaylistLibrary(); //should always be up-to-date with db

        playerControl = new PlayerController(library, playerView);
        selectedSong = null;

        playlistWindowArray = new ArrayList<MusicPlayerGUI>();
        selectedPlaylistName = null;

        //setup presentation
        playerView.updateTableView(library);
        playerView.getSideView().updatePlaylistTree(playlistLibrary.getAllPlaylistNames());
        playerView.setVisible(true);

        //create menu items under [Add To Playlist] popup menu
        playerView.setAddToPlaylistPopupMenuItem(playlistLibrary.getAllPlaylistNames());

        //Add listeners to buttons and slider
        playerView.addPlayerControlButtonListener(new PlayerControlButtonListener());
        playerView.addVolumeSliderListener(new VolumeSliderListener());

        //Add listeners to standard menu bar / popup menu items
        playerView.addMenuItemListener(new MenuItemListener());
        playerView.addControlsMenuItemListener(new ControlsMenuItemListener());

        //Add listener to table
        playerView.addSelectionListenerForTable(new SelectionListenerForTable());
        playerView.addMouseListenerForTable(new MouseListenerForTable());

        //Add WindowFocusListener
        playerView.addFocusListener(new FocusListenerForWindow());

        //Extra feature, add listener to extra menus on menu bar
        playerView.addViewMenuListener(new ViewMenuListener());

        //Add listener to the trees in the side panel
        playerView.getSideView().addMouseListener(new MouseListenerForSideView());
        playerView.getSideView().addMenuListener(new PopupMenuListenerForPlaylist());

        //Add drop target to scroll pane
        playerView.addDragDropToScrollPane(new DragDropToScrollPane());

        //initializing only in the beginning because otherwise I got NullPointerExceptions in the beginning
        focusedWindow = playerView;
        focusedWindowName = "main";

        playerView.getSongTable().getTableHeader().addMouseListener(new TableHeaderListener());
        playerView.getSongListView().addItemListenerTableHeader(new TableColumnCheckBoxListener());

    }

    /**
     * Action of "Play" that occurs by button, double-click, menu, hotkey...
     */
    private void playAction() {
        //play song
        playerControl.playSong(selectedSong);
        //change the play button text
        updatePlayBtnTextInAllWindow(MusicPlayerGUI.BTNTEXT_PAUSE);

    }


    //Listeners

    /**
     * PlayerControlButtonListener class implements
     * the actions of Play/Stop/Prev/Next buttons
     * by the name of the components.
     */
    class PlayerControlButtonListener implements ActionListener {
        String btnName;
        int playerStatus;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the name of event source component
            btnName = ((JButton) e.getSource()).getName();
            playerStatus = playerControl.getPlayerStatus();

            if (btnName.equals("play")) {
                //PLAY button actions
                System.out.println("[BUTTON] Play/Pause/Resume button is pressed.");

                switch (playerStatus) {
                    //Pause Action
                    case BasicPlayer.PLAYING:
                        playerControl.pauseSong();
                        updatePlayBtnTextInAllWindow(MusicPlayerGUI.BTNTEXT_PLAY);
                        break;
                    //Resume Action
                    case BasicPlayer.PAUSED:
                        playerControl.resumeSong();
                        updatePlayBtnTextInAllWindow(MusicPlayerGUI.BTNTEXT_PAUSE);
                        break;
                    //Play Action
                    case BasicPlayer.STOPPED:
                    default:
                        playAction();
                        break;
                }
            } else if (btnName.equals("stop")) {
                //STOP button action
                System.out.println("[BUTTON] STOP button is pressed.");
                playerControl.stopSong();
                updatePlayBtnTextInAllWindow(MusicPlayerGUI.BTNTEXT_PLAY);
            } else if (btnName.equals("prev")) {
                //PREV button action
                System.out.println("[BUTTON] PREV button is pressed.");
                //reflect to the view: change the row selection of the window
                playerControl.setSongTable(focusedWindow.getSongTable());

                playerControl.playPrevSong();
                //reflect to the view: update all playBtnText
                updatePlayBtnTextInAllWindow(MusicPlayerGUI.BTNTEXT_PAUSE);

                focusedWindow.changeTableRowSelection(playerControl.getCurrSongIndex());
                //focusedWindow.changeTableRowSelection(playerControl.getSongList().indexOf(playerControl.getCurrentSong()));
            } else if (btnName.equals("next")) {
                //NEXT button action
                System.out.println("[BUTTON] NEXT button is pressed.");
                //reflect to the view: change the row selection of the window
                playerControl.setSongTable(focusedWindow.getSongTable());

                playerControl.playNextSong();
                //reflect to the view: update all playBtnText
                updatePlayBtnTextInAllWindow(MusicPlayerGUI.BTNTEXT_PAUSE);

                System.out.println("current song index is: " + playerControl.getCurrSongIndex());
                focusedWindow.changeTableRowSelection(playerControl.getCurrSongIndex());
            } else {
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
            double sliderVal = ((JSlider) e.getSource()).getValue();
            playerControl.setVolume(sliderVal);
        }
    }


    /**
     * MenuItemListener class implements
     * the actions of menu items in menu bar and popup menu
     * by the name of the components.
     * "openSong"       Open Song (not in the library and play)
     * "lib-addSong"    Add Song To Library
     * "lib-deleteSong" Delete Song From Library
     * "newPlaylist"    New Playlist
     * "about"          About
     * "exit"           Exit
     * "addToPlaylist"  Add To Playlist
     */
    class MenuItemListener implements ActionListener {
        String menuName;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the name of event source component
            menuName = ((JMenuItem) e.getSource()).getName();
            System.out.println("menuName: " + menuName);

            if (menuName == null) {
                System.out.println("[Menu_Error] menuName: null");
                return;
            }
            if (menuName.equals("openSong")) {
                //[Open Song] menu actions
                System.out.println("[Menu] Open/Play Song not in library is pressed.");
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(playerView) == JFileChooser.APPROVE_OPTION) {
                    String selectedPath = chooser.getSelectedFile().getAbsolutePath();
                    Song newSong = new Song(selectedPath);
                    if (newSong.getPath() == null) {
                        System.out.println("[FileChooser] Not a valid file.\n");
                        JOptionPane.showMessageDialog(null, "This file is not a valid " +
                                "mp3 file.");
                    } else {
                        playerControl.playSong(newSong);
                    }
                }

            } else if (menuName.equals("lib-addSong")) {
                //[Add A Song To Library] menu actions
                System.out.println("[Menu] Add Song To Library is pressed.");
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //save windowname in case loosing focus when choosing song
                String windowName = focusedWindowName;
                if (chooser.showOpenDialog(playerView) == JFileChooser.APPROVE_OPTION) {
                    String selectedPath = chooser.getSelectedFile().getAbsolutePath();
                    Song newSong = new Song(selectedPath);
                    //Check if the chosen file is valid input
                    if (newSong.getPath() == null) {
                        System.out.println("[FileChooser] Not a valid file.\n");
                        JOptionPane.showMessageDialog(null, "This file is not a valid " +
                                "mp3 file.");
                    } else {
                        if (!windowName.equals("main")) {
                            //TODO suspicious a bit...
                            playlistLibrary.getPlaylistByName(windowName).addSong(newSong);
                            getPlaylistWindow(windowName).
                                    updateTableView(playlistLibrary.getPlaylistByName(windowName));
                        }
                        library.addSong(newSong);
                        playerView.updateTableView(library);
                        playerControl.updateSongList(library);
                    }

                }

            } else if (menuName.equals("lib-deleteSong")) {
                //[Delete Song From Library] menu actions
                if (focusedWindowName.equals("main")) {
                    if (playerView.getDisplayingListName().equals("library")) {
                    //library is displaying on the main window
                        System.out.println("[Menu] Delete Song is pressed.");
                        int counterDeletedSongs = 0;
                        int[] selectedRows = playerView.getSongTable().getSelectedRows();
                        for (int i = 0; i < selectedRows.length; i++) {
                            selectedRows[i] = selectedRows[i] - counterDeletedSongs;
                            boolean isRowInbound = (selectedRows[i] >= 0) && (selectedRows[i] < library.size());

                            if (isRowInbound) {
                                counterDeletedSongs++;
                                Song selectedSong = library.get(selectedRows[i]);
                                System.out.println("row:" + selectedRows[i] + " is selected to delete.");

                                //delete song from the library
                                library.deleteSong(selectedSong);
                                //delete song from all playlists
                                playlistLibrary.deleteSongFromAllPlaylists(selectedSong);
                                //update the view
                                playerView.updateTableView(library);
                                playerControl.updateSongList(library);
                                //reflect deleted song to all opened playlist window
                                for (String plistName : playlistLibrary.getAllPlaylistNames()) {
                                    if (getPlaylistWindow(plistName) != null) {
                                        getPlaylistWindow(plistName).
                                                updateTableView(playlistLibrary.getPlaylistByName(plistName));
                                    }
                                }

                            } else {
                                System.out.println("row:" + selectedRows[i] + ", nothing selected to delete.");
                            }
                        }
                    } else {
                    //playlist is displaying on the main window
                        int[] selectedRows = playerView.getSongTable().getSelectedRows();
                        String playlistName = playerView.getDisplayingListName();
                        int counterDeletedSongs = 0;
                        for (int i = 0; i < selectedRows.length; i++) {
                            selectedRows[i] = selectedRows[i]-counterDeletedSongs;
                            boolean isRowInbound = (selectedRows[i] >= 0) &&
                                    (selectedRows[i] < playlistLibrary.getPlaylistByName(playlistName).
                                            getSongList().size());
                            if (isRowInbound) {
                                counterDeletedSongs++;
                                Song selectedSong = playlistLibrary.getPlaylistByName(playlistName).
                                        getSongList().get(selectedRows[i]);
                                playlistLibrary.getPlaylistByName(playlistName).deleteSong(selectedSong);

                            }
                        }
                        //TODO refactoring point
                        playerView.updateTableView(playlistLibrary.getPlaylistByName(playlistName));
                        getPlaylistWindow(playlistName).updateTableView(
                                playlistLibrary.getPlaylistByName(playlistName));
                    }
                } else {
                //[Delete Song] menu actions on the playlist window
                    //TODO Delete Song from Playlist should be separated (from delete song from library) for playlist in main window or playlist window
                    System.out.println("[PlaylistWindow] Delete Song is pressed");
                    int counterDeletedSongs = 0;
                    int[] selectedRows = getPlaylistWindow(focusedWindowName).getSongTable().getSelectedRows();
                    for (int i = 0; i < selectedRows.length; i++) {
                        selectedRows[i] = selectedRows[i] - counterDeletedSongs;
                        boolean isRowInbound = (selectedRows[i] >= 0) &&
                                (selectedRows[i] < playlistLibrary.getPlaylistByName(focusedWindowName).getSongList().size());
                        if (isRowInbound) {
                            counterDeletedSongs++;
                            Song selectedSong =
                                    playlistLibrary.getPlaylistByName(focusedWindowName).getSongList().get(selectedRows[i]);
                            System.out.println("[PlaylistWindow] row: " + selectedRows[i] + " is selected to delete.");

                            //delete song from playlist
                            playlistLibrary.getPlaylistByName(focusedWindowName).deleteSong(selectedSong);

                        } else {
                            System.out.println("[PlaylistWindow] row: " + selectedRows[i] + " nothing selected to delete.");
                        }
                    }
                    //TODO refactoring point
                    getPlaylistWindow(focusedWindowName).updateTableView(
                            playlistLibrary.getPlaylistByName(focusedWindowName));
                    playerView.updateTableView(playlistLibrary.getPlaylistByName(focusedWindowName));
                }

            } else if (menuName.equals("about")) {
                //[About] menu actions
                System.out.println("[Menu] About is pressed.");
                String title = "About";
                String teamInfo = "[CECS543 Team6]\nSella Bae\nBrett Rexius\nJohanna Thiemich";
                String year = "2019";
                String msg = appName + "\n" + year + "\n\n" + teamInfo;
                JOptionPane.showMessageDialog(playerView, msg, title, JOptionPane.PLAIN_MESSAGE);

            } else if (menuName.equals("newPlaylist")) {
                //[New Playlist] menu actions
                System.out.println("[Menu] New Playlist is pressed.");
                //ask user to name a new playlist
                String title = "Create Playlist";
                String msg = "Name the Playlist";
                String playlistName = JOptionPane.showInputDialog(playerView, msg, title, JOptionPane.PLAIN_MESSAGE);
                //ask again if the name exists
                while (playlistLibrary.exists(playlistName) || playlistName.equalsIgnoreCase("library")) {
                    msg = "Playlist \"" + playlistName + "\" already exists.\nName the Playlist";
                    playlistName = JOptionPane.showInputDialog(playerView, msg, title, JOptionPane.PLAIN_MESSAGE);
                }
                //name
                playlistLibrary.addPlaylist(playlistName);
                //update side panel
                playerView.getSideView().updatePlaylistTree(playlistLibrary.getAllPlaylistNames());
                //update playlists in the popup menu
                playerView.setAddToPlaylistPopupMenuItem(playlistLibrary.getAllPlaylistNames(), this);
                //select the playlist in the side panel tree
                playerView.getSideView().getLibraryTree().clearSelection();
                int lastRow = playerView.getSideView().getPlaylistTree().getRowCount() - 1;
                playerView.getSideView().getPlaylistTree().setSelectionRow(lastRow);
                //open it on the main window
                Playlist playlist = playlistLibrary.getPlaylistByName(playlistName);
                playerView.updateTableView(playlist);
                playerControl.updateSongList(playlist.getSongList());

            } else if (menuName.equals("exit")) {
                //[Exit] menu actions
                System.exit(0);

            } else if (menuName.equals("addToPlaylist")) {
                //[Add To Playlist] menu actions
                String playlistName = ((JMenuItem) e.getSource()).getText();
                System.out.println("[PopupMenu] Add To Playlist \"" + playlistName + "\" is clicked.");
                Playlist playlist = playlistLibrary.getPlaylistByName(playlistName);
                if (playlist != null) {
                    int[] selectedRow = playerView.getSongTable().getSelectedRows();

                    for (int i = 0; i < selectedRow.length; i++) {
                        boolean isRowInbound = (selectedRow[i] >= 0) && (selectedRow[i] < library.size());

                        if (isRowInbound) {
                            Song selectedSong = library.get(selectedRow[i]);
                            System.out.println("row:" + selectedRow[i] + " is selected to be added.");
                            playlist.addSong(selectedSong);
                            //update the opened playlist window view where the song was added
                            if (getPlaylistWindow(playlistName) != null) {
                                getPlaylistWindow(playlistName).
                                        updateTableView(playlistLibrary.getPlaylistByName(playlistName));
                            }
                        } else {
                            System.out.println("row:" + selectedRow[i] + ", nothing selected to add.");
                        }
                    }
                }

            } else {
                System.out.println("none of the menu item action performed.");
            }

        }
    }

    //Class for controls menu actions
    class ControlsMenuItemListener implements ActionListener {
        JMenuItem menuItem;
        String menuName;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the name of event source component
            menuItem = (JMenuItem) e.getSource();
            menuName = menuItem.getName();

            System.out.println("menuName: " + menuName);

            if (menuName == null) {
                System.out.println("[Menu_Error] menuName: null");
                return;
            }

            if(menuName.equals("Play")){
                System.out.println("[Controls Menu] Play is pressed.");
                playAction();
            }
            else if(menuName.equals("Next")){
                System.out.println("[Controls Menu] Next is pressed.");
                //TODO connect to next button
            }
            else if(menuName.equals("Previous")){
                System.out.println("[Controls Menu] Previous is pressed.");
                //TODO connect to previous button
            }
            else if(menuName.equals("recent")){
                String text = menuItem.getText();
                System.out.println("[Controls Menu] Play Recent is pressed. "+text);

                //get the index of the menu item and play that in recentlyPlayedSongs
                int index = playerView.getPlayRecentMenuIndex(menuItem);
                System.out.println("index = "+ index);
                selectedSong = playerControl.getRecentlyPlayedSongs().get(index);
                playAction();
            }
            else if(menuName.equals("Current")){
                System.out.println("[Controls Menu] Current is pressed.");
                //TODO go to and highlight current playing song
            }
            else if(menuName.equals("Increase")){
                System.out.println("[Controls Menu] Increase is pressed.");
                //TODO increase volume by 5%
            }
            else if(menuName.equals("Decrease")){
                System.out.println("[Controls Menu] Decrease is pressed.");
                //TODO decrease volume by 5%
            }
            else if(menuName.equals("Shuffle")){
                System.out.println("[Controls Menu] Shuffle is pressed.");
                //TODO when this is clicked we need to have a random song play next
            }
            else if(menuName.equals("Repeat")){
                System.out.println("[Controls Menu] Repeat is pressed.");
                //TODO when this is clicked the song will play repeatedly
            }

        }
    }

    /**
     * ViewMenuListener class implements
     * the action of [View] menu items(JCheckBoxMenuItem) in menu bar
     * by the name and state of the components.
     * "darkTheme"  Set or unset the dark theme to the app
     * "songInfo"   Show or hide the info panel of the currently playing song at the bottom
     */
    class ViewMenuListener implements ActionListener {
        JCheckBoxMenuItem checkMenu;
        String menuName;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the name of event source component
            checkMenu = (JCheckBoxMenuItem) e.getSource();
            menuName = checkMenu.getName();

            if (menuName.equals("darkTheme")) {
                //[Dark Theme] menu actions
                if (checkMenu.getState()) {
                    System.out.println("[ViewMenu] Set Dark Theme.");
                    playerView.setColorTheme(ColorTheme.dark);
                } else {
                    System.out.println("[ViewMenu] Unset Dark Theme.");
                    playerView.setColorTheme(ColorTheme.white);
                }

            } else if (menuName.equals("songInfo")) {
                //[Current Song Info] menu actions
                if (checkMenu.getState()) {
                    System.out.println("[ViewMenu] Show Playing Song Info.");
                    playerView.getControlView().showSongInfoPanel(true);
                } else {
                    System.out.println("[ViewMenu] Hide Playing Song Info.");
                    playerView.getControlView().showSongInfoPanel(false);
                }
            }
        }
    }

    /**
     * SelectionListenerForTable detects
     * any row selection change of the table
     * either by mouse or keyboard arrows
     */
    class SelectionListenerForTable implements ListSelectionListener {
        private JTable table;
        private int row;
        private boolean isRowInbound;

        //Table row selected
        @Override
        public void valueChanged(ListSelectionEvent e) {
            //e.getSource()
            String displaying = focusedWindow.getDisplayingListName();
            if (!e.getValueIsAdjusting()) {
                //get the table in the focused window
                if (focusedWindowName.equals("main")) {
                    table = playerView.getSongTable();
                } else {
                    table = getPlaylistWindow(focusedWindowName).getSongTable();
                }
                //check table row limit
                row = table.getSelectedRow();
                isRowInbound = row >= 0 && row < table.getRowCount();
                if (isRowInbound) {
                    //get the song from library or playlist
                    if(displaying.equals("library")){
                        selectedSong = library.get(row);
                    } else {
                        Playlist playlist = playlistLibrary.getPlaylistByName(displaying);
                        selectedSong = playlist.getSongList().get(row);
                    }
                    System.out.print("[Table] selectedRow:" + row);
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
            source = (JTable) e.getSource();
            rowCount = source.getRowCount();
            row = source.rowAtPoint(e.getPoint());
            col = source.columnAtPoint(e.getPoint());
            isRowInbound = (row >= 0) && (row < rowCount);
            // Right-click Popup Trigger (for MacOS)
            if (e.isPopupTrigger() && library.size() > 0) {
                if (isRowInbound) {   //right click in table
                    System.out.println("right clicked in table. row:" + row);
                    //source.changeSelection(row, col, false, false);
                    playerView.getPopUpMenu().show(e.getComponent(), e.getX(), e.getY());
                } else {                //right click out of table
                    System.out.println("right clicked outside of the table");
                    playerView.getPopUpMenuInBlankspace().show(e.getComponent(), e.getX(), e.getY());
                }
            }
            // Left-click outside of table to clear selection
            if (!isRowInbound && !e.isPopupTrigger()) {
                playerView.getSongTable().clearSelection();
                System.out.println("Cleared row selections.");
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Right-click Popup Trigger (for Windows)
            if (e.isPopupTrigger()) {
                if (isRowInbound) {   //right click in table
                    System.out.println("right clicked inside of the table");
                    //source.changeSelection(row, col, false, false);
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
            if (isRowInbound) {
                if ((e.getClickCount() == 2) && !e.isConsumed() && !e.isPopupTrigger()) {
                    System.out.println("[Table] double clicked");
                    selectedSong = playerControl.getSongList().get(row);
                    playAction();
                }
            }
        }
    }

    class TableHeaderListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("mouse Clicked on header");
            if (e.isPopupTrigger()) {
                playerView.getSongListView().getTableHeaderPopup().show(e.getComponent(), e.getX(), e.getY());
                System.out.println("is popup");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("mouse pressed on header");
            if (e.isPopupTrigger()) {
                playerView.getSongListView().getTableHeaderPopup().show(e.getComponent(), e.getX(), e.getY());
                System.out.println("is popup");
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("mouse released on header");
            if (e.isPopupTrigger()) {
                playerView.getSongListView().getTableHeaderPopup().show(e.getComponent(), e.getX(), e.getY());
                System.out.println("is popup");
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    class TableColumnCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            for (int i = 0; i < playerView.getSongListView().getTableHeaderPopup().getComponentCount(); i++) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) playerView.getSongListView().
                        getTableHeaderPopup().getComponent(i);
                System.out.println(item.getText());
                if (item.isSelected()) {
                    playerView.getSongListView().showColumn(playerView.getSongTable().
                            getColumnModel().getColumn(i));
                    for (int j = 0; j < playlistWindowArray.size(); j++) {
                        playlistWindowArray.get(j).getSongListView().showColumn(playlistWindowArray.
                                get(j).getSongTable().getColumnModel().getColumn(i));
                    }

                } else {
                    playerView.getSongListView().hideColumn(playerView.getSongTable().
                            getColumnModel().getColumn(i));
                    for (int j = 0; j < playlistWindowArray.size(); j++) {
                        playlistWindowArray.get(j).getSongListView().hideColumn(playlistWindowArray.
                                get(j).getSongTable().getColumnModel().getColumn(i));
                    }

                }
            }
        }


    }

    /**
     * DragDropToScrollPane class implements drag-and-drop
     * 1. mp3 files to library on the main window
     * 2. mp3 files to playlist on the main window
     * 3. mp3 files to playlist on a playlist window
     * 4. songs in library to playlist window
     * 5. songs in playlist to playlist
     */
    class DragDropToScrollPane
            extends DropTarget implements DragGestureListener {

        //code from class DragDropToScrollPanePlWindow
//        private DataFlavor[] supportedFlavors = new DataFlavor[1];
//
//        public DragDropToScrollPane() {
//            supportedFlavors[0] = DataFlavor.stringFlavor;
//        }

        public synchronized void drop(DropTargetDropEvent evt) {
            //get the target window of drop event
            MusicPlayerGUI targetWindow = (MusicPlayerGUI)((DropTarget)evt.getSource()).getComponent();
            String displaying = targetWindow.getDisplayingListName();
            System.out.println("[DragDrop] drop targetWindow: "
                    +targetWindow.getWindowName()+" displaying: "+displaying
            );
            //bring the target window to the front
            targetWindow.toFront();

            boolean invalidFilesFound = false;
            String filePath;
            int successCount = 0;
            int draggedCount = 0;
            int droppedSongsCount = 0;

            evt.acceptDrop(DnDConstants.ACTION_COPY);
            List<File> droppedFiles = null;
            String droppedSongs = null;
            try {
                if (evt.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                //Drag-and-Drop mp3 files to the app
                    droppedFiles = (List<File>) evt.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    draggedCount = droppedFiles.size();
                    for (File file : droppedFiles) {
                        filePath = file.getAbsolutePath();
                        Song newSong = new Song(filePath);
                        if (newSong.getPath() == null) {
                            //not a valid song file
                            System.out.println("[DragDrop] Not a valid file. '" + filePath + "'\n");
                            invalidFilesFound = true;
                        } else {
                            //add the song to the library
                            //if library successfully adds the song
                            //which is valid mp3 and not present in library..
                            if(library.addSong(newSong) != SongLibrary.ADDSONG_FILEPATH_NULL) {
                                successCount++;

                                if (!displaying.equals("library")) {
                                //if displaying a playlist on the target window(either main or new)
                                    //add the song also to the playlist
                                    Playlist playlist = playlistLibrary.getPlaylistByName(displaying);
                                    playlist.addSong(newSong);
                                    //update the view and the player control
                                    targetWindow.updateTableView(playlist);
                                    playerControl.updateSongList(playlist.getSongList());

                                    //if library is on the main window
                                    if(playerView.getDisplayingListName().equals("library")) {
                                        //also update the view of the main window
                                        playerView.updateTableView(library);
                                    } else {
                                    //if playlist is opened in main window
                                        playerView.updateTableView(playlist);
                                    }

                                    //update a playlist window if the playlist is also opened in a window
                                    getPlaylistWindow(displaying).updateTableView(playlist);

                                } else {
                                //if displaying library on the targetWindow
                                    //update the view and the player control
                                    targetWindow.updateTableView(library);
                                    playerControl.updateSongList(library);
                                }
                            }
                        }
                    }

                } else if (evt.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)) {
                //Drag-and-Drop inter windows
                    droppedSongs = (String) evt.getTransferable().
                            getTransferData(DataFlavor.stringFlavor);

                    System.out.println("[DragDrop] string transferable" + droppedSongs);
                    draggedCount = droppedSongs.split("\n").length;

                    for (String song : droppedSongs.split("\n")) {
                        String songPath = song.split("\t")[0];
                        System.out.println("[DragDrop] song name is " + songPath);
                        Song addedSong = new Song(songPath);
                        if (addedSong == null) {
                            invalidFilesFound = true;
                        } else {
                            if (displaying.equals("library")) {
                            //to library
                                library.addSong(addedSong);
                                System.out.println("Song was added to the library");
                                targetWindow.updateTableView(library);
                                playerControl.updateSongList(library);
                            } else {
                            //to playlist
                                Playlist playlist = playlistLibrary.getPlaylistByName(displaying);
                                playlist.addSong(addedSong);
                                System.out.println("Song was added to the playlist" + displaying);
                                //update the view
                                targetWindow.updateTableView(playlist);
                                //TODO refactoring point for the update
                                if(playerView.getDisplayingListName().equals(displaying)) {
                                    //if the main window is showing the same playlist
                                    playerView.updateTableView(playlist);
                                }
                                playerControl.updateSongList(playlist.getSongList());
                            }
                        }
                    }
                }
                if (invalidFilesFound) {
                    System.out.println("[DragDrop] Added " + successCount + " songs out of " + draggedCount + " files.\n");
                    String msg = "Some files have not been added\nsince they are not valid mp3 files.";
                    JOptionPane.showMessageDialog(playerView, msg, "Notice", JOptionPane.PLAIN_MESSAGE);
                }

            } catch (UnsupportedFlavorException e) {
                System.out.println("File flavor not supported");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void dragGestureRecognized(DragGestureEvent dge) {
            //TODO dragGestureRecognized() is never executed.
            //get the source window of the DragGestureEvent
            MusicPlayerGUI sourceWindow = (MusicPlayerGUI)SwingUtilities.getWindowAncestor(dge.getComponent());
            String displaying = sourceWindow.getDisplayingListName();
            System.out.println("[DragDrop] drag sourceWindow: "
                    +sourceWindow.getWindowName()+" displaying: "+displaying
            );

            Cursor cursor = Cursor.getDefaultCursor();
            int[] songIndices = sourceWindow.getSongTable().getSelectedRows();
            String songNames = "";
            System.out.println("[DragDrop] selected row count: "+songIndices.length);

            if(displaying.equals("library")) {
            //from library
                for (int i = 0; i < songIndices.length; i++) {
                    songNames = songNames + ";" + library.get(songIndices[i]).getPath();
                }
            } else {
            //from playlist
                Playlist playlist = playlistLibrary.getPlaylistByName(displaying);
                for (int i = 0; i < songIndices.length; i++) {
                    songNames = songNames + ";"
                            + playlist.getSongList().get(songIndices[i]).getPath();
                }
            }
            System.out.println("Song name dragged: " + songNames);
            Transferable t = new StringSelection(songNames);
            if (dge.getDragAction() == DnDConstants.ACTION_COPY) {
                cursor = DragSource.DefaultCopyDrop;
            }
            dge.startDrag(cursor, t);
        }
    }

    /**
     * MouseListenerForTree covers:
     * [1] left-click on "Library" to show it on the main window
     * [2] left-click on a playlist node to show it on the main window
     * [3] popup trigger for right-click on a playlist node
     * [4] clear selections for left-click outside of trees
     */
    class MouseListenerForSideView extends MouseAdapter {
        private JTree tree;
        private TreePath treePath;
        private boolean isPlaylistSelected;

        @Override
        public void mousePressed(MouseEvent e) {
            tree = (JTree) e.getSource();
            // Get treePath of the selected tree node
            treePath = tree.getPathForLocation(e.getX(), e.getY());
            // initialize state variables
            isPlaylistSelected = false;

            // Check if the event is within trees
            if (treePath != null) {
                //clear highlight on the not selected tree.
                if (tree.getName().equals("libraryTree")) {
                    playerView.getSideView().getPlaylistTree().clearSelection();
                    selectedPlaylistName = null;
                    isPlaylistSelected = false;

                } else if (tree.getName().equals("playlistTree")) {
                    //when Playlist tree is selected including the root node
                    playerView.getSideView().getLibraryTree().clearSelection();
                }

                // Get the playlist name if a playlist is selected
                if ((tree.getName().equals("playlistTree")) && (treePath.getParentPath() != null)) {
                    isPlaylistSelected = true;
                    String text = treePath.getLastPathComponent().toString();
                    selectedPlaylistName = extractPlaylistNameFromTreeNodeText(text);
                }

                // [3] Right-click Popup Trigger (for MacOS)
                if (e.isPopupTrigger() && isPlaylistSelected) {
                    System.out.println("[Playlist] right clicked: " + selectedPlaylistName);
                    tree.setSelectionPath(treePath);
                    //show playlist popup menu
                    JPopupMenu popupMenu = playerView.getSideView().getPlaylistPopupMenu();
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            } else {
                // [4] Clear any tree selections when left-clicking outside of tree
                if (!e.isPopupTrigger()) {
                    playerView.getSideView().getLibraryTree().clearSelection();
                    playerView.getSideView().getPlaylistTree().clearSelection();
                    System.out.println("cleared tree selections.");
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // [3] Right-click Popup Trigger (for Windows)
            if (e.isPopupTrigger() && isPlaylistSelected) {
                System.out.println("[Playlist] right clicked: " + selectedPlaylistName);
                tree.setSelectionPath(treePath);
                //show popup menu
                JPopupMenu popupMenu = playerView.getSideView().getPlaylistPopupMenu();
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Detect left-click event
            if (!e.isPopupTrigger()) {

                // [1] Left-click on "Library"
                if (tree.getName().equals("libraryTree")) {
                    System.out.println("[SideView] Library clicked\n");
                    //show library on the main window
                    selectedPlaylistName = null;
                    playerView.updateTableView(library);
                    playerControl.updateSongList(library);
                }

                // [2] Left-click on a playlist name under "Playlist"
                if (isPlaylistSelected) {
                    System.out.println("[SideView] Playlist \"" + selectedPlaylistName + "\" clicked");
                    Playlist playlist = playlistLibrary.getPlaylistByName(selectedPlaylistName);
                    System.out.println("[Playlist:" + selectedPlaylistName + "] " + playlist.getSongList().size() + " songs\n");
                    //show the selected playlist on the main window
                    playerView.updateTableView(playlist);
                    playerControl.updateSongList(playlist.getSongList());
                }
            }
        }

        /**
         * Extract only the playlist name from text in a tree node
         * e.g. "favorite (3)" to "favorite"
         *
         * @param text the text in treeNode with song counts
         * @return the string of playlistName with counts removed
         */
        private String extractPlaylistNameFromTreeNodeText(String text) {
            if (text == null) {
                return null;
            }
            if (text.endsWith(")")) {
                int pos = text.lastIndexOf(" (");
                return text.substring(0, pos);
            } else {
                return text;
            }
        }
    }

    /**
     * PopupMenuItemListenerForPlaylist class implements
     * the actions of popup menu items about playlist
     * by the name of the components.
     * "playlist-openNewWindow"  Open in New Window
     * "playlist-delete"         Delete Playlist
     */
    private class PopupMenuListenerForPlaylist implements ActionListener {
        String menuName;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the name of event source component
            menuName = ((JMenuItem) e.getSource()).getName();

            if (menuName.equals("playlist-newWindow")) {
                //[Open in New Window] menu action
                System.out.println("[PopupMenu] Open in New Window is pressed.");

                Boolean isOpen = false;
                //If the playlist is already opened in a new window
                for (MusicPlayerGUI playlistWindow : playlistWindowArray) {
                    if (playlistWindow.getWindowName().equalsIgnoreCase(selectedPlaylistName)) {
                        //show the opened window to the front
                        playlistWindow.toFront();
                        isOpen = true;
                    }
                }
                //If not opened
                if (!isOpen) {
                    //open a new window for the selected playlist
                    MusicPlayerGUI newPlaylistWindow = createNewPlaylistWindow(selectedPlaylistName, playerView);
                    newPlaylistWindow.getSongTable().getTableHeader().addMouseListener(new TableHeaderListener());
                    newPlaylistWindow.getSongListView().addItemListenerTableHeader(new TableColumnCheckBoxListener());
                    playlistWindowArray.add(newPlaylistWindow);
                    //main window shows library
                    playerView.updateTableView(library);
                    System.out.println("[NewWindow] playlist \""+selectedPlaylistName+"\" is opened in a new window. "
                            + "("+playlistWindowArray.size()+" playlist windows in total)"
                    );
                }

            } else if (menuName.equals("playlist-delete")) {
                //[Delete Playlist] menu action
                System.out.println("[PopupMenu] Delete Playlist is pressed.");

                String title = "Delete Playlist";
                String msg = "Are you sure you want to delete\nplaylist \""+selectedPlaylistName+"\"?";
                //Ask user if they surely want to delete playlist via dialog
                int flag = JOptionPane.showConfirmDialog(playerView, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (flag == JOptionPane.YES_OPTION) {
                    System.out.println("[DeletePlaylist] Yes is pressed on deleting "+selectedPlaylistName+".");
                    //delete the selected playlist
                    playlistLibrary.deletePlaylist(selectedPlaylistName);
                    //update the playlist tree view on the side
                    playerView.getSideView().updatePlaylistTree(playlistLibrary.getAllPlaylistNames());
                    //show library on the main window
                    playerView.updateTableView(library);
                    //if the playlist window was opened, close the window
                    MusicPlayerGUI playlistWindow = getPlaylistWindow(selectedPlaylistName);
                    if(playlistWindow!=null) {
                        playlistWindow.dispose();
                        playlistWindowArray.remove(playlistWindow);
                    }
                } else {
                    System.out.println("[DeletePlaylist] Canceled.");
                }

            }
        }
    }

    /**
     * FocusListenerForWindow class implements the actions when a window is focused.
     * This changes the song lists loaded on the BasicPlayer
     */
    public class FocusListenerForWindow implements WindowFocusListener {
        @Override
        public void windowGainedFocus(WindowEvent e) {
            focusedWindow = (MusicPlayerGUI) e.getWindow();
            focusedWindowName = focusedWindow.getWindowName();

            //if the main window has the focus
            if (focusedWindowName.equals("main")) {
                System.out.println("\nFocus on the main window.");
                //TODO might need to check if playlist is on the main window table... or it can work fine without it.
                playerControl.updateSongList(library);
            } else {
                    System.out.println("\nFocus on playlist window \"" + focusedWindowName + "\".");
                    playerControl.updateSongList(playlistLibrary.getPlaylistByName(focusedWindowName).getSongList());
                    selectedPlaylistName = focusedWindowName;
                }
            }

        @Override
        public void windowLostFocus(WindowEvent e) {
            //clear any row selection on the table when losing focus
            ((MusicPlayerGUI) e.getWindow()).getSongTable().clearSelection();
            //remember the last focused window in case no window is focused.
            focusedWindow = (MusicPlayerGUI) e.getWindow();
            focusedWindowName = focusedWindow.getWindowName();
            System.out.println(focusedWindowName + " window lost the focus.");
        }
    }

    /**
     * Creates a new window for a playlist.
     * This new window reuses the same class MusicPlayerGUI that the main window used.
     *
     * @param playlistName the name of the playlist to be represented on a new window
     * @param parentView   the MusicPlayerGUI instance of main window
     */
    private MusicPlayerGUI createNewPlaylistWindow(String playlistName, MusicPlayerGUI parentView) {
        //Create a new window for a playlist

        MusicPlayerGUI playlistWindow = new MusicPlayerGUI("Playlist: " + playlistName, 500, 300, playlistName, playlistName);
        //Closing action of playlist window
        playlistWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        playlistWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.out.println("Playlist window \"" + playlistName + "\" is closing.");
                playlistWindowArray.remove(playlistWindow);
            }
        });

        //Set songs on the view
        playlistWindow.getSongListView().updateTableView(playlistLibrary.getPlaylistByName(playlistName).getSongList());

        //copy the play btn text
        playlistWindow.setPlayBtnText(parentView.getControlView().getPlayBtn().getText());

        //Disable the unnecessary componets
        playlistWindow.getSideView().setVisible(false);
        playlistWindow.getJMenuBar().getMenu(1).setVisible(false);  //hide view menu

        //Add listeners for the new window
        //menu
        playlistWindow.addMenuItemListener(new MenuItemListener());
        //controls
        playlistWindow.addPlayerControlButtonListener(new PlayerControlButtonListener());
        playlistWindow.addVolumeSliderListener(new VolumeSliderListener());
        //table
        playlistWindow.addSelectionListenerForTable(new SelectionListenerForTable());
        playlistWindow.addMouseListenerForTable(new MouseListenerForTable());
        //window focus
        playlistWindow.addFocusListener(new FocusListenerForWindow());
        //drop target to scroll pane
        playlistWindow.addDragDropToScrollPane(new DragDropToScrollPane());
        //transfer handler
        //playlistWindow.getSongListView().getSongTable().setTransferHandler(
        //        new TableRowTransferHandler(library, playlistLibrary)
        //);

        playlistWindow.setVisible(true);
        System.out.println("Playlist \"" + playlistName + "\" is opened in a new window.\n");

        return playlistWindow;
    }


    /**
     * Updates play buttons in all window
     *
     * @param btnText
     */
    public void updatePlayBtnTextInAllWindow(String btnText) {
        playerView.setPlayBtnText(btnText);
        for (MusicPlayerGUI playlistWindow : playlistWindowArray) {
            playlistWindow.setPlayBtnText(btnText);
        }
    }

    /**
     * Gets playlistWindow by playlist name, if opened.
     *
     * @param playlistName the name of the playlist to check if a new window is opened.
     * @return MusicPlayerGUI window, null if no playlist window is opened with the name
     */
    public MusicPlayerGUI getPlaylistWindow(String playlistName) {
        for (MusicPlayerGUI playlistWindow : playlistWindowArray) {
            if (playlistWindow.getWindowName().equalsIgnoreCase(playlistName)) {
                return playlistWindow;
            }
        }
        return null;
    }

}
