package view;

import model.Song;
import model.SongLibrary;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import javax.swing.JFrame;


public class MusicPlayerGUI extends JFrame {

    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JPanel bottomPanel;
    private JPanel buttonPanel;
    private JPanel sliderPanel;
    private JPanel stopPanel;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem addSongMenuItem;
    private JMenuItem openSongMenuItem;
    private JMenuItem exitApplicationMenuItem;
    private JMenuItem deleteSongMenuItem;

    private JTable songTable;
    private DefaultTableModel tableModel;
    private String[] columnHeader;

    private JButton playBtn;
    private JButton nextBtn;
    private JButton prevBtn;
    private JButton stopBtn;
    private JSlider volumeSlider;

    private JPopupMenu popUpMenu;
    private JMenuItem deleteSongMenuItemPopup;
    private JMenuItem addSongMenuItemPopup;




    public MusicPlayerGUI(String frameTitle) {
        super(frameTitle);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        columnHeader = new String[]{"Path", "Title", "Artist", "Album", "Year", "Comment", "Genre"};

        mainPanel = new JPanel();
        bottomPanel = new JPanel();
        buttonPanel = new JPanel();
        sliderPanel = new JPanel();
        stopPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        //buttonPanel.setLayout(new BorderLayout());
        sliderPanel.setLayout(new BorderLayout());
        stopPanel.setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        openSongMenuItem = new JMenuItem("Play External Song");
        addSongMenuItem = new JMenuItem("Add File to Library");
        deleteSongMenuItem = new JMenuItem("Delete Song from Library");
        exitApplicationMenuItem = new JMenuItem("Exit Application");
        createMenu();

        //create table and setup
        songTable = new JTable(){
            @Override   //block table contents editing
            public boolean isCellEditable(int row, int column) { return false; }
        };
        songTable.setFillsViewportHeight(true);
        songTable.setShowHorizontalLines(true);
        initializeTable();

        popUpMenu = new JPopupMenu();
        deleteSongMenuItemPopup = new JMenuItem("Delete This Song");
        addSongMenuItemPopup = new JMenuItem("Add A Song");
        popUpMenu.add(deleteSongMenuItemPopup);
        popUpMenu.add(addSongMenuItemPopup);

        tableScrollPane = new JScrollPane(songTable);

        stopBtn = new JButton("[]");
        stopPanel.add(stopBtn);

        prevBtn = new JButton("|<");
        buttonPanel.add(prevBtn);

        playBtn = new JButton(">");
        buttonPanel.add(playBtn);

        nextBtn = new JButton(">|");
        buttonPanel.add(nextBtn);

        volumeSlider = new JSlider();
        sliderPanel.add(volumeSlider);

        bottomPanel.add(buttonPanel);
        bottomPanel.add(sliderPanel, BorderLayout.EAST);
        bottomPanel.add(stopPanel, BorderLayout.WEST);

        //TODO layout bottomPanel

        this.setJMenuBar(menuBar);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.pack();


    }

    private void createMenu() {
        this.menu.setPreferredSize(new Dimension(50, this.menu.getPreferredSize().height));
        this.menu.add(openSongMenuItem);
        this.menu.add(addSongMenuItem);
        this.menu.add(deleteSongMenuItem);
        this.menu.add(exitApplicationMenuItem);
        this.menuBar.add(menu);
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(columnHeader,0);
        songTable.setModel(tableModel);
    }

    //When table view needs to change after 'Add Song To Library' action
    public void addRowToTableView(String[] row){ 
        tableModel.addRow(row);
    }

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

    //getters and setters
    public JTable getSongTable(){ return songTable; }
    public JPopupMenu getPopUpMenu() {
        deleteSongMenuItemPopup.setVisible(true);
        return popUpMenu;
    }
    public JPopupMenu getPopUpMenuInBlankspace(){
        deleteSongMenuItemPopup.setVisible(false);
        return popUpMenu;
    }
    public JScrollPane getScrollPane() { return this.tableScrollPane; }

    //For 'Play'<->'Pause' text change
    public String getPlayBtnText() { return playBtn.getText(); }
    public void setPlayBtnText(String text){ playBtn.setText(text); }


    //Error message Dialog
    public void displayErrorMessage(String errorMessage){
        JOptionPane.showMessageDialog(this,
                errorMessage);
    }


// Adding listeners ---------------------------------------------

    //Add listeners to standard menu
    public void addOpenSongMenuItemListener(ActionListener listener) {
        openSongMenuItem.addActionListener(listener);
    }
    public void addAddSongMenuItemListener(ActionListener listener) {
        addSongMenuItem.addActionListener(listener);
    }
    public void addExitApplicationMenuItemListener (ActionListener listener) {
        exitApplicationMenuItem.addActionListener(listener);
    }
    public void addDeleteSongMenuListener (ActionListener listener) { deleteSongMenuItem.addActionListener(listener);}

    //Add listener to popup menu
    public void addDeleteSongPopupListener(ActionListener listener) {
        deleteSongMenuItemPopup.addActionListener(listener);
    }
    public void addAddSongPopupListener (ActionListener listener) {addSongMenuItemPopup.addActionListener(listener);}
    //Add listeners to buttons
    public void addPlayBtnListener(ActionListener listener){
        playBtn.addActionListener(listener);
    }
    public void addStopBtnListener(ActionListener listener){
        stopBtn.addActionListener(listener);
    }
    public void addPrevBtnListener(ActionListener listener){
        prevBtn.addActionListener(listener);
    }
    public void addNextBtnListener(ActionListener listener){
        nextBtn.addActionListener(listener);
    }
    //Add listener to slider
    public void addVolumeSliderListener(ChangeListener listener){
        volumeSlider.addChangeListener(listener);
    }
    //Add listener to table
    public void addSelectionListenerForTable(ListSelectionListener listener){
        songTable.getSelectionModel().addListSelectionListener(listener);
    }
    public void addPopupTriggerListenerForTable(MouseAdapter adapter){
        songTable.addMouseListener(adapter);
    }

}
