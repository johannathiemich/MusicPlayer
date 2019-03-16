package view;

import model.Song;
import model.SongLibrary;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
    // UI variables
    private Dimension frameSize = new Dimension(800,600);
    private Dimension frameMinSize = new Dimension(500,300);
    private Dimension buttonSize = new Dimension(60,40);
    private Color[] bgColor = {new Color(40,40,40), new Color(50,50,50)};
    private Color pointColor = new Color(0, 95, 96);
    private Color[] fgColor = {Color.white, Color.lightGray, Color.gray};
    private Font font = new Font("Helvetica", Font.PLAIN, 14);

    //panels to hold buttons, table, etc.
    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JPanel bottomPanel;
    private JPanel buttonPanel;
    private JPanel sliderPanel;
    private JPanel stopPanel;

    //components for table
    private JTable songTable;
    private DefaultTableModel tableModel;
    private String[] columnHeader;

    //components for songInfoPanel
    private JPanel songInfoPanel;
    private JLabel songTitleLbl;
    private JLabel songDetailLbl;
    private JLabel songTimeProgressLbl;
    private JLabel songTimeRemainedLbl;
    private JProgressBar songProgressBar;

    //components for player control
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
        this.setPreferredSize(frameSize);
        this.setMinimumSize(frameMinSize);

        //Panels and Layout
        mainPanel = new JPanel();
        bottomPanel = new JPanel();
        buttonPanel = new JPanel();
        sliderPanel = new JPanel();
        stopPanel = new JPanel();
        //Panel Layout
        bottomPanel.setLayout(new BorderLayout(0,0));
        stopPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        sliderPanel.setLayout(new BorderLayout());

        // Standard Menu setup
        createMenu();

        // Table setup
        columnHeader = new String[]{"Path", "Title", "Artist", "Album", "Year", "Comment", "Genre"};
        songTable = new JTable(){
            @Override   //block table contents editing
            public boolean isCellEditable(int row, int column) { return false; }
        };
        initializeTable();
        songTable.setFillsViewportHeight(true);
        songTable.setShowVerticalLines(false);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.getTableHeader().setReorderingAllowed(false);
        tableScrollPane = new JScrollPane(songTable);

        // PopUp Menu setup
        popUpMenu = new JPopupMenu();
        deleteSongMenuItemPopup = new JMenuItem("Delete This Song");
        addSongMenuItemPopup = new JMenuItem("Add A Song");
        popUpMenu.add(deleteSongMenuItemPopup);
        popUpMenu.add(addSongMenuItemPopup);

        //initializing buttons and slider
        stopBtn = new JButton("◼");
        prevBtn = new JButton("⦉⦉");
        playBtn = new JButton("▶");
        nextBtn = new JButton("⦊⦊");
        volumeSlider = new JSlider();

        //Song Info Panel @sellabae
        createSongInfoPanel();
        //Set a new look of the view @sellabae
        setTheme();
        //showLayoutBorders(true);

        //Add components in place-----------------
        stopPanel.add(stopBtn);

        buttonPanel.add(prevBtn);
        buttonPanel.add(playBtn);
        buttonPanel.add(nextBtn);

        //slider setup
        sliderPanel.add(volumeSlider);

        //putting all buttons into bottomPanel
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(sliderPanel, BorderLayout.EAST);
        bottomPanel.add(stopPanel, BorderLayout.WEST);

        //putting all panels into main frame
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.pack();
    }

    /**
     * Change the look of the view
     */
    private void setTheme(){
        //frame
        this.setBackground(bgColor[0]);

        //menu bar
//        menuBar.setBackground(bgColor[1]);
//        menu.setBackground(bgColor[1]);
//        menu.setForeground(fgColor[1]);

        //table view
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        songTable.setRowHeight(20);
        songTable.setShowGrid(false);
        songTable.setBackground(bgColor[0]);
        songTable.setForeground(fgColor[1]);
        songTable.getTableHeader().setBackground(bgColor[1]);
        songTable.getTableHeader().setForeground(fgColor[2]);
        //songTable.getTableHeader().setUI(new TableHeaderUI() { ... });    //UI object?

        //table row selection
        songTable.setSelectionBackground(fgColor[2]);
        songTable.setSelectionForeground(bgColor[0]);

        //song info panel
        songInfoPanel.setBackground(bgColor[1]);
        songTitleLbl.setForeground(fgColor[1]);
        songDetailLbl.setForeground(fgColor[2]);
        songTimeProgressLbl.setForeground(fgColor[2]);
        songTimeRemainedLbl.setForeground(fgColor[2]);

        //bottom panel
        bottomPanel.setBackground(bgColor[1]);
        stopPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        sliderPanel.setOpaque(false);

        //font
        songTitleLbl.setFont(new Font("Helvetica", Font.PLAIN, 14));

        //buttons size and color
        stopBtn.setPreferredSize(buttonSize);
        prevBtn.setPreferredSize(buttonSize);
        playBtn.setPreferredSize(buttonSize);
        nextBtn.setPreferredSize(buttonSize);

    }

    /**
     * Create all components for song info panel
     * which contains song title, artist, progressbar, duration.
     */
    private void createSongInfoPanel() {
        songInfoPanel = new JPanel();
        songInfoPanel.setLayout(new BorderLayout(0,0));

        //song info
        songTitleLbl = new JLabel("Title");
        songDetailLbl = new JLabel("Artist");
        songTimeProgressLbl = new JLabel("0:00");
        songTimeRemainedLbl = new JLabel("3:33");
        songTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        songDetailLbl.setHorizontalAlignment(SwingConstants.CENTER);
        songTimeProgressLbl.setHorizontalAlignment(SwingConstants.LEFT);
        songTimeRemainedLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        //progress bar
        songProgressBar = new JProgressBar(0,0,190);
        songProgressBar.setValue(100);

        //add all components to song info panel
        songInfoPanel.add(Box.createHorizontalStrut(10));   //invisible space
        songInfoPanel.add(songTitleLbl, BorderLayout.NORTH);
        songInfoPanel.add(songDetailLbl, BorderLayout.CENTER);
        songInfoPanel.add(songTimeProgressLbl, BorderLayout.WEST);
        songInfoPanel.add(songTimeRemainedLbl, BorderLayout.EAST);
        songInfoPanel.add(songProgressBar, BorderLayout.SOUTH);

        //add this song info panel to bottom panel
        bottomPanel.add(songInfoPanel, BorderLayout.NORTH);
    }

    /**
     * Only for development use.
     * Show borders of all components in the ui to check the layout.
     * @param show true to show, false not to show borders
     */
    private void showLayoutBorders(boolean show){
        LineBorder[] border = {new LineBorder(Color.red), new LineBorder(Color.green), new LineBorder(Color.blue)};
        tableScrollPane.setBorder(border[0]);
            songTable.setBorder(border[1]);
        bottomPanel.setBorder(border[0]);
            stopPanel.setBorder(border[1]);
                stopBtn.setBorder(border[2]);
            buttonPanel.setBorder(border[1]);
                prevBtn.setBorder(border[2]);
                playBtn.setBorder(border[2]);
                nextBtn.setBorder(border[2]);
            sliderPanel.setBorder(border[1]);
                volumeSlider.setBorder(border[2]);
        if(songInfoPanel != null){
            songInfoPanel.setBorder(border[0]);
            songTitleLbl.setBorder(border[1]);
            songDetailLbl.setBorder(border[1]);
            songTimeProgressLbl.setBorder(border[1]);
            songTimeRemainedLbl.setBorder(border[1]);
            songProgressBar.setBorder(border[1]);
        }
    }

    /**
     * Update the view of song info panel
     * with title, artist, duration of currently playing song.
     * @param song currently playing song
     */
    public void updateCurrentPlayingView(Song song){
        songTitleLbl.setText(song.getTitle());
        songDetailLbl.setText(song.getArtist());
        int duration = song.getLengthInSecond();
        int min = duration/60;
        int sec = duration%60;
        songTimeProgressLbl.setText("0:00");
        songTimeRemainedLbl.setText(min+":"+sec);
        songProgressBar.setMinimum(0);
        songProgressBar.setMaximum(duration);
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

        menu.setPreferredSize(new Dimension(50, menu.getPreferredSize().height));

        menu.add(openSongMenuItem);
        menu.add(addSongMenuItem);
        menu.add(deleteSongMenuItem);
        menu.add(aboutMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);

        this.setJMenuBar(menuBar);
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
     * Check if any row is selected in the song table
     * @return whether or not any row is selected.
     */
    public boolean isAnyRowSelected() {
        return (songTable.getSelectedRow() != -1);
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
