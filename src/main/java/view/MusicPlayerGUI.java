package view;

import model.Song;
import model.SongLibrary;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
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
    private JLabel songTimePlayingLbl;
    private JLabel songTimeRemainingLbl;
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
        deleteSongMenuItemPopup.setName("delete");
        addSongMenuItemPopup.setName("add");
        popUpMenu.add(deleteSongMenuItemPopup);
        popUpMenu.add(addSongMenuItemPopup);

        //Buttons and Slider setup
        stopBtn = new JButton("◼");
        prevBtn = new JButton("⦉⦉");
        playBtn = new JButton("▶");
        nextBtn = new JButton("⦊⦊");
        volumeSlider = new JSlider();
        //setting name(key) of button components
        stopBtn.setName("stop");
        playBtn.setName("play");
        prevBtn.setName("prev");
        nextBtn.setName("next");


        //Song Info Panel @sellabae
        createSongInfoPanel();
        //Set a new look of the view @sellabae
        setDarkTheme();
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
     * Apply dark theme to the all components and repaint main frame view.
     */
    public void setDarkTheme(){
//  public void setTheme(Color[] bgColor, Color[] fgColor, Color pointColor){  //for later

        //frame
        this.setBackground(bgColor[0]);

        //menu bar
//        menuBar.setBackground(bgColor[1]);
//        menu.setBackground(bgColor[1]);
//        menu.setForeground(fgColor[1]);

        //table
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        songTable.setRowHeight(20);
        songTable.setShowGrid(false);
        songTable.setBackground(bgColor[0]);
        songTable.setForeground(fgColor[1]);
        songTable.getTableHeader().setBackground(bgColor[1]);
        songTable.getTableHeader().setForeground(fgColor[2]);

        //table row selection
        songTable.setSelectionBackground(fgColor[2]);
        songTable.setSelectionForeground(bgColor[0]);

        //bottom panel
        bottomPanel.setBackground(bgColor[1]);
        stopPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        sliderPanel.setOpaque(false);
        stopBtn.setPreferredSize(buttonSize);
        prevBtn.setPreferredSize(buttonSize);
        playBtn.setPreferredSize(buttonSize);
        nextBtn.setPreferredSize(buttonSize);

        //song info panel
        if(songInfoPanel!=null) {
            songInfoPanel.setBackground(bgColor[1]);
            songTitleLbl.setForeground(fgColor[1]);
            songDetailLbl.setForeground(fgColor[2]);
            songTimePlayingLbl.setForeground(fgColor[2]);
            songTimeRemainingLbl.setForeground(fgColor[2]);
            songTitleLbl.setFont(new Font("Helvetica", Font.PLAIN, 14));
        }

        //Repaint main frame view
        this.repaint();
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
        songTimePlayingLbl = new JLabel("0:00");
        songTimeRemainingLbl = new JLabel("3:33");
        songTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        songDetailLbl.setHorizontalAlignment(SwingConstants.CENTER);
        songTimePlayingLbl.setHorizontalAlignment(SwingConstants.LEFT);
        songTimeRemainingLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        //progress bar
        songProgressBar = new JProgressBar(0,0,190);
        songProgressBar.setValue(60);

        //add all components to song info panel
        songInfoPanel.add(Box.createHorizontalStrut(10));   //invisible space
        songInfoPanel.add(songTitleLbl, BorderLayout.NORTH);
        songInfoPanel.add(songDetailLbl, BorderLayout.CENTER);
        songInfoPanel.add(songTimePlayingLbl, BorderLayout.WEST);
        songInfoPanel.add(songTimeRemainingLbl, BorderLayout.EAST);
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
            songTimePlayingLbl.setBorder(border[1]);
            songTimeRemainingLbl.setBorder(border[1]);
            songProgressBar.setBorder(border[1]);
        }
    }

    /**
     * Update the view of song info panel
     * with title, artist, duration of currently playing song.
     * @param song currently playing song
     */
    public void updateCurrentPlayingView(Song song){
        if(songInfoPanel != null) {
            songTitleLbl.setText(song.getTitle());
            songDetailLbl.setText(song.getArtist());
            songTimePlayingLbl.setText("0:00");
            songTimeRemainingLbl.setText(song.getDurationMinSec());
            songProgressBar.setMinimum(0);
            songProgressBar.setMaximum(song.getDuration());
        }
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
     * Returns the scroll panel of the table
     * @return JScrollPane for the table.
     */
    public JScrollPane getScrollPane() { return this.tableScrollPane; }

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
        playBtn.addActionListener(listener);
        stopBtn.addActionListener(listener);
        prevBtn.addActionListener(listener);
        nextBtn.addActionListener(listener);
    }

    /**
     *This method adds a ChangeListener to the volume slider.
     * @param listener the listener to be added to the volume slider
     */
    public void addVolumeSliderListener(ChangeListener listener){
        volumeSlider.addChangeListener(listener);
    }

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

    /**
     * Set drop target to table scroll pane for drag&drop action
     * @param dropTarget the drop target to be added to the scroll panel
     */
    public void addDragDropToScrollPane(DropTarget dropTarget){
        tableScrollPane.setDropTarget(dropTarget);
    }

}
