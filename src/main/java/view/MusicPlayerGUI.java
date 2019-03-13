package view;

import model.SongLibrary;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;


public class MusicPlayerGUI extends JFrame {

    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JPanel bottomPanel;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem addSngItem;
    private JMenuItem openSongItem;

    private JTable songTable;
    private DefaultTableModel tableModel;
    private String[] columnHeader;

    //private JPanel dragDropPanel;

    private JButton playBtn;
    private JButton nextBtn;
    private JButton prevBtn;
    private JButton stopBtn;
    private JSlider volumeSlider;


    public MusicPlayerGUI(String frameTitle) {
        super(frameTitle);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        columnHeader = new String[]{"Path", "Title", "Artist", "Album", "Year", "Comment", "Genre"};

        mainPanel = new JPanel();
        bottomPanel = new JPanel();

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        addSngItem = new JMenuItem("Add File to Library");
        openSongItem = new JMenuItem("Open");
        initializeMenu();

        //create table and setup
        songTable = new JTable(){
            @Override   //block table contents editing
            public boolean isCellEditable(int row, int column) { return false; }
        };
        songTable.setFillsViewportHeight(true);
        songTable.setShowHorizontalLines(true);
        initializeTable();

        tableScrollPane = new JScrollPane(songTable);
       // dragDropPanel = new JPanel();
      //  dragDropPanel.add(tableScrollPane);

        stopBtn = new JButton("Stop");
        bottomPanel.add(stopBtn);

        prevBtn = new JButton("<<");
        bottomPanel.add(prevBtn);

        playBtn = new JButton("Play");
        bottomPanel.add(playBtn);

        nextBtn = new JButton(">>");
        bottomPanel.add(nextBtn);

        volumeSlider = new JSlider();
        bottomPanel.add(volumeSlider);

        //TODO layout bottomPanel

        //this.add(topPanel, BorderLayout.NORTH);
        this.setJMenuBar(menuBar);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.pack();
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(columnHeader,0);
        songTable.setModel(tableModel);

    }

    private void initializeMenu() {
        this.menu.setPreferredSize(new Dimension(50, this.menu.getPreferredSize().height));
        this.menu.add(addSngItem);
        this.menu.add(openSongItem);
        this.menuBar.add(menu);
    }

    //Useful when table view needs to change after 'Add Song To Library' action
    public void addRowToTableView(String[] row){ 
        tableModel.addRow(row);
    }

    public void updateTableView(SongLibrary library) {
        initializeTable();
        for(int i=0; i<library.size(); i++){
            tableModel.addRow(library.get(i).toArray());
        }
    }

    /**
     * Changes the row selection of the table view.
     * @param rowIndex row to be selected.
     */
    public void changeTableRowSelection(int rowIndex){
        songTable.changeSelection(rowIndex,0,false,false);
    }

    public JTable getSongTable(){
        return songTable;
    }

    public JScrollPane getScrollPane() { return this.tableScrollPane; }

    //For 'Play'<->'Pause' text change
    public String getPlayBtnText() { return playBtn.getText(); }
    public void setPlayBtnText(String text){
        playBtn.setText(text);
    }

    //Add listeners to components
    public void addDragDropListener(DropTarget target) {tableScrollPane.setDropTarget(target);}
    public void openSongItemListener(ActionListener listener) { openSongItem.addActionListener(listener);}
    public void addSongItemListener(ActionListener listener) { addSngItem.addActionListener(listener);}
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
    public void addVolumeSliderListener(ChangeListener listener){
        volumeSlider.addChangeListener(listener);
    }
    public void addTableListener(ListSelectionListener listener){
        songTable.getSelectionModel().addListSelectionListener(listener);
    }

    public void displayErrorMessage(String errorMessage){
        JOptionPane.showMessageDialog(this, errorMessage);
    }
}
