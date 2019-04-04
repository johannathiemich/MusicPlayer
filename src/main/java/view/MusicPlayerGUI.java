package view;

import model.Song;
import model.SongLibrary;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * This class represents the main frame for our application. It holds all the GUI components.
 * In the Model - View - Controller  pattern this class represents the main view that is responsible
 * for displaying the data from the model and delegating user interactions to the controller.
 */
public class MusicPlayerGUI extends JFrame {
    //panel size dimensions
    private Dimension frameSize = new Dimension(800,600);
    private Dimension frameMinSize = new Dimension(500,300);
    private Dimension sidePanelSize = new Dimension(100, 500);

    //complete SongListView panel
    private SongListView libraryView;
    //complete ControlView panel
    private ControlView controlView;

    //panels to hold buttons, table, etc.
    private JPanel sidePanel;
    private JPanel playListPanel;

    //buttons for sidePanel
    private JButton playListBtn;
    private JButton libBtn;

    //top bar containing standard menu and menu items for its entries
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem openSongMenuItem;
    private JMenuItem addSongMenuItem;
    private JMenuItem deleteSongMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuItem exitMenuItem;

    //popup menu(right-click) and menu items
    private JPopupMenu popUpMenu;
    private JMenuItem deleteSongMenuItemPopup;
    private JMenuItem addSongMenuItemPopup;

    /**
     * This constructor initializes all necessary components.
     * @param frameTitle title of our application
     */
    public MusicPlayerGUI(String frameTitle) {
        super(frameTitle);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(frameSize);
        this.setMinimumSize(frameMinSize);

        // library or playlist view of the main window
        libraryView = new SongListView();
        // controlView with player buttons and volume slider
        controlView = new ControlView();

        sidePanel = new JPanel();
        playListPanel = new JPanel();
        //Panel Layout
        sidePanel.setLayout(new BorderLayout());
        playListPanel.setLayout(new BorderLayout());

        // Standard Menu setup
        createMenu();

        // PopUp Menu setup
        popUpMenu = new JPopupMenu();
        deleteSongMenuItemPopup = new JMenuItem("Delete This Song");
        addSongMenuItemPopup = new JMenuItem("Add A Song");
        deleteSongMenuItemPopup.setName("delete");
        addSongMenuItemPopup.setName("add");
        popUpMenu.add(deleteSongMenuItemPopup);
        popUpMenu.add(addSongMenuItemPopup);

        //Side Panel Buttons
        libBtn= new JButton("Library");
        playListBtn = new JButton("Playlist");
        playListBtn.setName("playlist");
        libBtn.setName("library");


        //Set a new look of the view @sellabae
        setColorTheme(ColorTheme.dark);


        //playlist setup
        playListPanel.add(libBtn, BorderLayout.NORTH);
        playListPanel.add(playListBtn,BorderLayout.SOUTH);
        sidePanel.add(playListPanel, BorderLayout.NORTH);

        //putting all panels into main frame
        this.add(libraryView, BorderLayout.CENTER);
        this.add(controlView, BorderLayout.SOUTH);
        this.add(sidePanel, BorderLayout.WEST);

        this.pack();
    }

    /**
     * Apply ColorTheme to the all components and repaint main frame view.
     * @param colorTheme the color theme to apply
     */
    public void setColorTheme(ColorTheme colorTheme){
        this.setBackground(colorTheme.bgColor[0]);
        libraryView.setTheme(colorTheme);
        controlView.setColorTheme(colorTheme);

        //menu bar
//        menuBar.setBackground(bgColor[1]);
//        menu.setBackground(bgColor[1]);
//        menu.setForeground(fgColor[1]);

        //side panel
        sidePanel.setBackground(colorTheme.bgColor[1]);
        sidePanel.setOpaque(false);
        sidePanel.setPreferredSize(sidePanelSize);

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
        menu = new JMenu("Menu");
        openSongMenuItem = new JMenuItem("Open Song");
        addSongMenuItem = new JMenuItem("Add Song to Library");
        deleteSongMenuItem = new JMenuItem("Delete Song from Library");
        aboutMenuItem = new JMenuItem("About");
        exitMenuItem = new JMenuItem("Exit");

        //setting name(key) of menu item components
        openSongMenuItem.setName("open");
        addSongMenuItem.setName("add");
        deleteSongMenuItem.setName("delete");
        aboutMenuItem.setName("about");
        exitMenuItem.setName("exit");

        menu.setPreferredSize(new Dimension(50, menu.getPreferredSize().height));

        menu.add(openSongMenuItem);
        menu.add(addSongMenuItem);
        menu.add(deleteSongMenuItem);
        menu.add(aboutMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);

        this.setJMenuBar(menuBar);
    }

    public void updateTableView(SongLibrary library){
        libraryView.updateTableView(library);
    }

    /**
     * Check if any row is selected in the song table
     * @return whether or not any row is selected.
     */
    public boolean isAnyRowSelected() {
        return (libraryView.getSongTable().getSelectedRow() != -1);
    }

    /**
     * Changes the row selection of the table view.
     * @param rowIndex row to be selected.
     */
    public void changeTableRowSelection(int rowIndex){
        libraryView.getSongTable().changeSelection(rowIndex,0,false,
                false);
    }

    /**
     * Returns the songTable.
     * @return JTable containing songs in the library
     */
    public JTable getSongTable(){ return libraryView.getSongTable(); }

    /**
     * Returns a popup menu when right-clicking on the table area
     * with deleteSong menu item.
     * @return JPopupMenu containing menu items delete, add song
     */
    public JPopupMenu getPopUpMenu() {
        deleteSongMenuItemPopup.setVisible(true);
        return popUpMenu;
    }

    /**
     * Returns a popup menu when right-clicking outside of the table area
     * with deleteSong menu item removed.
     * @return JPopupMenu containing just the menu item add song.
     */
    public JPopupMenu getPopUpMenuInBlankspace(){
        deleteSongMenuItemPopup.setVisible(false);
        return popUpMenu;
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
     * Attach a listener to all menu items in menu bar and popup menu
     * @param listener ActionListener with menu actions
     */
    public void addMenuItemListener(ActionListener listener) {
        //standard menu items in menu bar
        openSongMenuItem.addActionListener(listener);
        addSongMenuItem.addActionListener(listener);
        deleteSongMenuItem.addActionListener(listener);
        aboutMenuItem.addActionListener(listener);
        exitMenuItem.addActionListener(listener);
        //popup menu items
        addSongMenuItemPopup.addActionListener(listener);
        deleteSongMenuItemPopup.addActionListener(listener);
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
        libraryView.getSongTable().getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * This method adds a mouse adapter to the song table
     * @param adapter the adapter to be added to the song table
     */
    public void addMouseListenerForTable(MouseAdapter adapter){
        libraryView.getSongTable().addMouseListener(adapter);
    }

    /**
     * Set drop target to table scroll pane for drag&drop action
     * @param dropTarget the drop target to be added to the scroll panel
     */
    public void addDragDropToScrollPane(DropTarget dropTarget){
        libraryView.setDropTarget(dropTarget);
    }

}
