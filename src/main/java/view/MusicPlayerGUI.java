package view;

import model.Song;
import model.SongLibrary;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * This class represents the main frame for our application. It holds all the GUI components.
 * In the Model - View - Controller  pattern this class represents the main view that is responsible
 * for displaying the data from the model and delegating user interactions to the controller.
 */
public class MusicPlayerGUI extends JFrame {

    //panels to hold buttons, table, etc.
    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JPanel bottomPanel;
    private JPanel buttonPanel;
    private JPanel sliderPanel;
    private JPanel stopPanel;

    //all components related to table
    private JTable songTable;
    private DefaultTableModel tableModel;
    private String[] columnHeader;

    //all the buttons from the bottom panel
    private JButton playBtn;
    private JButton nextBtn;
    private JButton prevBtn;
    private JButton stopBtn;
    private JSlider volumeSlider;

    //top bar containing standard menu and components for its entries
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem openSongMenuItem;
    private JMenuItem addSongMenuItem;
    private JMenuItem deleteSongMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuItem exitMenuItem;

    //all components for popup menu (left click)
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
        this.setSize(800,600);
        this.setMinimumSize(new Dimension(600,300));

        //Panels and Layout
        mainPanel = new JPanel();
        bottomPanel = new JPanel();
        buttonPanel = new JPanel();
        sliderPanel = new JPanel();
        stopPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        sliderPanel.setLayout(new BorderLayout());
        stopPanel.setLayout(new BorderLayout());

        // Standard Menu setup
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        openSongMenuItem = new JMenuItem("Open Song");
        addSongMenuItem = new JMenuItem("Add Song to Library");
        deleteSongMenuItem = new JMenuItem("Delete Song from Library");
        aboutMenuItem = new JMenuItem("About");
        exitMenuItem = new JMenuItem("Exit");
        createMenu();

        // Table setup
        columnHeader = new String[]{"Path", "Title", "Artist", "Album", "Year", "Comment", "Genre"};
        songTable = new JTable(){
            @Override   //block table contents editing
            public boolean isCellEditable(int row, int column) { return false; }
        };
        songTable.setFillsViewportHeight(true);
        songTable.setShowHorizontalLines(true);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.getTableHeader().setReorderingAllowed(false);
        initializeTable();
        tableScrollPane = new JScrollPane(songTable);

        // PopUp Menu
        popUpMenu = new JPopupMenu();
        deleteSongMenuItemPopup = new JMenuItem("Delete This Song");
        addSongMenuItemPopup = new JMenuItem("Add A Song");
        popUpMenu.add(deleteSongMenuItemPopup);
        popUpMenu.add(addSongMenuItemPopup);

        //initializing all buttons and placing them on panel
        stopBtn = new JButton("[]");
        stopPanel.add(stopBtn);

        prevBtn = new JButton("|<");
        buttonPanel.add(prevBtn);

        playBtn = new JButton(">");
        buttonPanel.add(playBtn);

        nextBtn = new JButton(">|");
        buttonPanel.add(nextBtn);

        //initializing slider
        volumeSlider = new JSlider();
        sliderPanel.add(volumeSlider);

        //putting all buttons into bottomPanel
        bottomPanel.add(buttonPanel);
        bottomPanel.add(sliderPanel, BorderLayout.EAST);
        bottomPanel.add(stopPanel, BorderLayout.WEST);

        //putting all panels into main frame
        this.setJMenuBar(menuBar);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.pack();

    }

    /**
     * This method creates the standard menu and adds the menu item entries
     */
    private void createMenu() {
        menu.setPreferredSize(new Dimension(50, menu.getPreferredSize().height));
        menu.add(openSongMenuItem);
        menu.add(addSongMenuItem);
        menu.add(deleteSongMenuItem);
        menu.add(aboutMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);
    }

    /**
     * This method initializes the table model.
     */
    private void initializeTable() {
        //for dynamic row addition
        tableModel = new DefaultTableModel(columnHeader,0);
        songTable.setModel(tableModel);
    }

    /**
     * Updates the table view.
     * @param library SongLibrary reflected to table view.
     */
    public void updateTableView(SongLibrary library) {
        initializeTable();
        for (Song song : library) {
            tableModel.addRow(song.toArray());
        }
        tableModel.fireTableDataChanged();
    }

    /**
     * Changes the row selection of the table view.
     * @param rowIndex row to be selected.
     */
    public void changeTableRowSelection(int rowIndex){
        songTable.changeSelection(rowIndex,0,false,
                false);
    }

    /**
     * Returns the songTable.
     * @return JTable containing songs in the library
     */
    public JTable getSongTable(){ return songTable; }

    /**
     * Returns the popup menu (right click) when clicking on the table area.
     * (Since a song has been clicked, it can be deleted).
     * @return JPopupMenu containing menu items delete, add song
     */
    public JPopupMenu getPopUpMenu() {
        deleteSongMenuItemPopup.setVisible(true);
        return popUpMenu;
    }

    /**
     * Returns the popup menu (right click) when clicking outside of the table area.
     * (Since no song has been clicked, it cannot be deleted).
     * @return JPopupMenu containing just the menu item add song.
     */
    public JPopupMenu getPopUpMenuInBlankspace(){
        deleteSongMenuItemPopup.setVisible(false);
        return popUpMenu;
    }

    /**
     * Returns the scroll panel of the table
     * @return JScrollPane for the table.
     */
    public JScrollPane getScrollPane() { return this.tableScrollPane; }

    //For 'Play'<->'Pause' text change

    /**
     * This method returns the play button text.
     * @return String containing play button text.
     */
    public String getPlayBtnText() { return playBtn.getText(); }

    /**
     * This method sets the text for the play button in order to switch between 'Play'<->'Pause' text change.
     * @param text the text to be assigned to the play button
     */
    public void setPlayBtnText(String text){ playBtn.setText(text); }

    /**
     * This method displays an error message dialog.
     * @param errorMessage the error message to be displayed.
     */
    public void displayErrorMessage(String errorMessage){
        JOptionPane.showMessageDialog(this,
                errorMessage);
    }


// Adding listeners ---------------------------------------------

    /**
     *This method adds an ActionListener to the 'OpenSong' standard menu item.
     * @param listener the listener to be added to the menu item
     */
    public void addOpenSongMenuItemListener(ActionListener listener) {
        openSongMenuItem.addActionListener(listener);
    }

    /**
     *This method adds an ActionListener to the 'Add Song' standard menu item.
     * @param listener the listener to be added to the menu item
     */
    public void addAddSongMenuItemListener(ActionListener listener) {
        addSongMenuItem.addActionListener(listener);
    }

    /**
     *This method adds an ActionListener to the 'Delete Song' standard menu item.
     * @param listener the listener to be added to the menu item
     */
    public void addDeleteSongMenuListener (ActionListener listener) {
        deleteSongMenuItem.addActionListener(listener);
    }

    /**
     * Add an ActionListener to 'About' standard menu item.
     * @param listener the listener to be added to the menu item
     */
    public void addAboutMenuItemListener (ActionListener listener) {
        aboutMenuItem.addActionListener(listener);
    }

    /**
     *This method adds an ActionListener to the 'Exit' standard menu item.
     * @param listener the listener to be added to the menu item
     */
    public void addExitMenuItemListener (ActionListener listener) {
        exitMenuItem.addActionListener(listener);
    }

    //Add listeners to popup menu
    /**
     *This method adds an ActionListener to the 'Delete Song' popup menu item.
     * @param listener the listener to be added to the popup menu item
     */
    public void addDeleteSongPopupListener(ActionListener listener) {
        deleteSongMenuItemPopup.addActionListener(listener);
    }

    /**
     *This method adds an ActionListener to the 'Add Song' popup menu item.
     *@param listener the listener to be added to the popup menu item
     */
    public void addAddSongPopupListener (ActionListener listener) {
        addSongMenuItemPopup.addActionListener(listener);
    }

    //Add listeners to buttons
    /**
     *This method adds an ActionListener to the play button.
     * @param listener the listener to be added to the play button
     */
    public void addPlayBtnListener(ActionListener listener){
        playBtn.addActionListener(listener);
    }

    /**
     *This method adds an ActionListener to the stop button.
     *@param listener the listener to be added to the stop button
     */
    public void addStopBtnListener(ActionListener listener){
        stopBtn.addActionListener(listener);
    }

    /**
     *This method adds an ActionListener to the 'previous song' button.
     *@param listener the listener to be added to the 'previous song' button
     */
    public void addPrevBtnListener(ActionListener listener){
        prevBtn.addActionListener(listener);
    }

    /**
     *This method adds an ActionListener to the 'next song' button.
     *@param listener the listener to be added to the 'next song' button
     */
    public void addNextBtnListener(ActionListener listener){
        nextBtn.addActionListener(listener);
    }

    //Add listener to volume slider
    /**
     *This method adds a ChangeListener to the volume slider.
     * @param listener the listener to be added to the volume slider
     */
    public void addVolumeSliderListener(ChangeListener listener){
        volumeSlider.addChangeListener(listener);
    }

    //Add listener to table
    /**
     * This method adds a ListSelectionListener to the song table.
     * @param listener the listener to be added to the song table
     */
    public void addSelectionListenerForTable(ListSelectionListener listener){
        songTable.getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * This method adds a mouse adapter to the song table
     * @param adapter the adapter to be added to the song table
     */
    public void addMouseListenerForTable(MouseAdapter adapter){
        songTable.addMouseListener(adapter);
    }

}
