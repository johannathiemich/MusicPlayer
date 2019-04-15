package view;

import model.Song;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

/**
 * This class represents the main frame for our application. It holds all the GUI components.
 * In the Model - View - Controller  pattern this class represents the main view that is responsible
 * for displaying the data from the model and delegating user interactions to the controller.
 */
public class MusicPlayerGUI extends JFrame {
    //the name of what this window represents
    private String windowName;

    //font of the app
    public static Font FONT = new Font("Helvetica",Font.PLAIN,14);

    //complete SongListView panel in the center
    private SongListView songListView;
    //complete ControlView panel at the bottom
    private ControlView controlView;
    //complete SidePanelView on the left
    private SidePanelView sideView;

    //top bar containing standard menu and menu items for its entries
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openSongMenuItem;
    private JMenuItem addSongMenuItem;
    private JMenuItem deleteSongMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuItem newPlaylistMenuItem;
    private JMenuItem exitMenuItem;

    //popup menu(right-click) and menu items
    private JPopupMenu popUpMenu;
    private JMenuItem deleteSongMenuItemPopup;
    private JMenuItem addSongMenuItemPopup;
    private JMenu addToPlaylistPopupMenu;

    //menu for extra features
    private JMenu viewMenu;
    private JCheckBoxMenuItem darkThemeMenuItem;
    private JCheckBoxMenuItem songInfoMenuItem;

    /**
     * This constructor initializes all necessary components.
     * @param frameTitle the title shown on the frame
     * @param width the width of the window
     * @param height the height of the window
     * @param windowName the name of what this window represents
     */
    public MusicPlayerGUI(String frameTitle, int width, int height, String windowName) {
        super(frameTitle);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width,height));
        this.setMinimumSize(new Dimension(width,height));

        //to detect which window is focused
        //this.setFocusable(true);
        this.windowName = windowName;

        // table view of a list of songs
        songListView = new SongListView();
        // controlView with player buttons and volume slider
        controlView = new ControlView();
        // tree view of library/playlist
        sideView = new SidePanelView();

        // Set color theme of the window
        setColorTheme(ColorTheme.dark);

        // Menu bar setup
        createMenu();

        // PopUp Menu setup
        popUpMenu = new JPopupMenu();
        deleteSongMenuItemPopup = new JMenuItem("Delete Song");
        addSongMenuItemPopup = new JMenuItem("Add New Song");
        addToPlaylistPopupMenu = new JMenu("Add To Playlist");
        deleteSongMenuItemPopup.setName("lib-deleteSong");
        addSongMenuItemPopup.setName("lib-addSong");
        addToPlaylistPopupMenu.setName("addToPlaylist");

        popUpMenu.add(deleteSongMenuItemPopup);
        popUpMenu.add(addToPlaylistPopupMenu);
        popUpMenu.addSeparator();
        popUpMenu.add(addSongMenuItemPopup);

        //putting all panels into main frame
        this.add(songListView, BorderLayout.CENTER);
        this.add(controlView, BorderLayout.SOUTH);
        this.add(sideView, BorderLayout.WEST);

        this.pack();
    }

    /**
     * Apply ColorTheme to the all components and repaint main frame view.
     * @param colorTheme the color theme to apply
     */
    public void setColorTheme(ColorTheme colorTheme){
        this.setBackground(colorTheme.bgColor[0]);

        //pass the colorTheme
        songListView.setColorTheme(colorTheme);
        controlView.setColorTheme(colorTheme);
        sideView.setColorTheme(colorTheme);

        //Repaint main frame view
        this.repaint();
    }

    /**
     * Update the view of song info panel
     * with title, artist, duration of currently playing song.
     * @param song currently playing song
     */
    public void updateCurrentPlayingView(Song song){
        controlView.updateCurrentPlayingView(song);
    }

    /**
     * Creates menu bar, menu, menu items.
     */
    private void createMenu() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        openSongMenuItem = new JMenuItem("Open Song");
        addSongMenuItem = new JMenuItem("Add Song to Library");
        deleteSongMenuItem = new JMenuItem("Delete Song from Library");
        newPlaylistMenuItem = new JMenuItem("New Playlist");
        aboutMenuItem = new JMenuItem("About");
        exitMenuItem = new JMenuItem("Exit");
        //setting name(key) of menu item components
        openSongMenuItem.setName("openSong");
        addSongMenuItem.setName("lib-addSong");
        deleteSongMenuItem.setName("lib-deleteSong");
        aboutMenuItem.setName("about");
        newPlaylistMenuItem.setName("newPlaylist");
        exitMenuItem.setName("exit");
        //add menu items to fileMenu
        fileMenu.add(openSongMenuItem);
        fileMenu.add(addSongMenuItem);
        fileMenu.add(deleteSongMenuItem);
        fileMenu.add(newPlaylistMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(aboutMenuItem);
        fileMenu.add(exitMenuItem);

        //extra menus
        viewMenu = new JMenu("View");
        darkThemeMenuItem = new JCheckBoxMenuItem("Dark Theme",true);
        songInfoMenuItem = new JCheckBoxMenuItem("Song Info",false);
        darkThemeMenuItem.setName("darkTheme");
        songInfoMenuItem.setName("songInfo");
        viewMenu.add(darkThemeMenuItem);
        viewMenu.add(songInfoMenuItem);

        //add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        //add menu bar to main frame
        this.setJMenuBar(menuBar);
    }

    public void updateTableView(ArrayList<Song> songList){
        songListView.updateTableView(songList);
    }

    /**
     * Check if any row is selected in the song table
     * @return whether or not any row is selected.
     */
    public boolean isAnyRowSelected() {
        return (songListView.getSongTable().getSelectedRow() != -1);
    }

    /**
     * Changes the row selection of the table view.
     * @param rowIndex row to be selected.
     */
    public void changeTableRowSelection(int rowIndex){
        songListView.getSongTable().changeSelection(rowIndex,0,false,
                false);
    }

    /**
     * Gets the window name of this window. (different from frame title)
     * This can be "main" for the main window
     * or a playlist name for the playlist window.
     * @return the String of the window name
     */
    public String getWindowName(){ return this.windowName; }

    /**
     * Returns the songTable.
     * @return JTable containing songs in the library
     */
    public JTable getSongTable(){ return songListView.getSongTable(); }

    /**
     * Gets songListView of the main window
     * @return SongListView that the main frame currently showing
     */
    public SongListView getSongListView(){ return songListView; }

    /**
     * Gets controlView of the main window
     * @return ControlView that the main frame has
     */
    public ControlView getControlView() { return controlView; }

    /**
     * Gets sideView of the main window
     * @return SidePanelView that the main frame has
     */
    public SidePanelView getSideView() { return sideView; }

    /**
     * Returns a popup menu when right-clicking on the table area
     * with deleteSong menu item.
     * @return JPopupMenu containing menu items delete, add song, add song to playlist
     */
    public JPopupMenu getPopUpMenu() {
        deleteSongMenuItemPopup.setVisible(true);
        addToPlaylistPopupMenu.setVisible(true);
        return popUpMenu;
    }

    /**
     * Returns a popup menu when right-clicking outside of the table area
     * with deleteSong menu item removed.
     * @return JPopupMenu containing just the menu item add song.
     */
    public JPopupMenu getPopUpMenuInBlankspace(){
        deleteSongMenuItemPopup.setVisible(false);
        addToPlaylistPopupMenu.setVisible(false);
        return popUpMenu;
    }

    public void setSongListView(SongListView songListView) {
        this.songListView = songListView;
    }

    public void setControlView(ControlView controlView) {
        this.controlView = controlView;
    }

    /**
     * This method sets the text for the play button in order to switch between 'Play'<->'Pause' text change.
     * @param text the text to be assigned to the play button
     */
    public void setPlayBtnText(String text){ controlView.getPlayBtn().setText(text); }

    /**
     * This method displays an error message dialog.
     * @param errorMessage the error message to be displayed.
     */
    public void displayErrorMessage(String errorMessage){
        JOptionPane.showMessageDialog(this,
                errorMessage);
    }


// Add listeners ---------------------------------------------

    /**
     * Attach a window focus listener to this window
     * to check which whether the main window or a playlist window is focused
     * @param l WindowAdapter with
     */
    public void addFocusListener(WindowFocusListener l) {
        this.addWindowFocusListener(l);
    }

    /**
     * Attach a listener to all menu items in menu bar and popup menu
     * @param listener ActionListener with menu actions
     */
    public void addMenuItemListener(ActionListener listener) {
        //standard menu items in menu bar
        openSongMenuItem.addActionListener(listener);
        addSongMenuItem.addActionListener(listener);
        deleteSongMenuItem.addActionListener(listener);
        newPlaylistMenuItem.addActionListener(listener);
        aboutMenuItem.addActionListener(listener);
        exitMenuItem.addActionListener(listener);

        //popup menu items
        addSongMenuItemPopup.addActionListener(listener);
        deleteSongMenuItemPopup.addActionListener(listener);
        //add listener to menu items in the "Add To Playlist" menu
        for (int i=0; i<addToPlaylistPopupMenu.getItemCount(); i++) {
            addToPlaylistPopupMenu.getItem(i).addActionListener(listener);
        }
    }

    /**
     * Attach a listener to optional menu items in menu bar
     * @param listener ActionListener with optional menu actions
     */
    public void addViewMenuListener(ActionListener listener) {
        darkThemeMenuItem.addActionListener(listener);
        songInfoMenuItem.addActionListener(listener);
    }

    /**
     * Attach a listener to all player control buttons Play/Stop/Prev/Next
     * @param listener ActionListener with button actions
     */
    public void addPlayerControlButtonListener(ActionListener listener) {
        controlView.getPlayBtn().addActionListener(listener);
        controlView.getStopBtn().addActionListener(listener);
        controlView.getPrevBtn().addActionListener(listener);
        controlView.getNextBtn().addActionListener(listener);
    }

    /**
     *This method adds a ChangeListener to the volume slider.
     * @param listener the listener to be added to the volume slider
     */
    public void addVolumeSliderListener(ChangeListener listener){
        controlView.getVolumeSlider().addChangeListener(listener);
    }


// Add listeners for table view interactions --------------------------
    //TODO These 3 listener attachers should be in SongListView class for further uses.

    /**
     * This method adds a ListSelectionListener to the song table.
     * @param listener the listener to be added to the song table
     */
    public void addSelectionListenerForTable(ListSelectionListener listener){
        songListView.getSongTable().getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * This method adds a mouse adapter to the song table
     * @param adapter the adapter to be added to the song table
     */
    public void addMouseListenerForTable(MouseAdapter adapter){
        songListView.getSongTable().addMouseListener(adapter);
    }

    /**
     * Set drop target to table scroll pane for drag&drop action
     * @param dropTarget the drop target to be added to the scroll panel
     */
    public void addDragDropToScrollPane(DropTarget dropTarget){
        //TODO: this drop target conflicts with the new drag and drop (to playlists)
        songListView.setDropTarget(dropTarget);
    }

    /**
     * Sets menu items from an array of playlist names
     * add them to [Add To Playlist] popup menu.
     * All menu item components are named as "addToPlaylist" to distinguish menu item types.
     * @param playlistName the string array of all playlist names
     */
    public void setAddToPlaylistPopupMenuItem(ArrayList<String> playlistName){
        //initialize the sub menu
        addToPlaylistPopupMenu.removeAll();
        //add menu items of playlist names
        for (int i=0; i<playlistName.size(); i++){
            JMenuItem playlistItem = new JMenuItem(playlistName.get(i));
            playlistItem.setName("addToPlaylist");
            addToPlaylistPopupMenu.add(playlistItem);
        }
    }

}
