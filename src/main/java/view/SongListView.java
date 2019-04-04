package view;

import model.Song;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * SongListView class is to show either the library or a playlist.
 * This contains a table with column headers for songs.
 * Table related listeners are attached.
 */
public class SongListView extends JPanel {
    //components for table
    private JScrollPane tableScrollPane;
    private JTable songTable;
    private DefaultTableModel tableModel;
    private String[] columnHeader;

    /**
     * Constructs a panel to show a list of songs
     * with an empty table view.
     */
    public SongListView(){
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
        this.setLayout(new BorderLayout());
        this.add(tableScrollPane, BorderLayout.CENTER);
        //initializeTable();
    }

    /**
     * Constructs a panel to show a list of songs
     * with a table view from a list of songs.
     * @param songList list of Songs to be reflected in table view,
     *                 which can be from the library or a playlist.
     */
    public SongListView (ArrayList<Song> songList){
        this();
        updateTableView(songList);
    }

    /**
     * Initializes the table model to dynamically add rows later.
     */
    private void initializeTable() {
        //for dynamic row addition
        tableModel = new DefaultTableModel(columnHeader,0);
        songTable.setModel(tableModel);
    }

    /**
     * Updates the table view.
     * @param songList list of Songs to be reflected in table view,
     *                 which can be from the library or a playlist.
     */
    public void updateTableView(ArrayList<Song> songList) {

        for (Song song : songList) {
            tableModel.addRow(song.toArray());
        }
        tableModel.fireTableDataChanged();
    }

    /**
     * Returns the songTable of this SongListView
     * @return JTable containing songs
     */
    public JTable getSongTable(){
        return songTable;
    }

    /**
     * Set Dark theme to the table view panel.
     */
    public void setColorTheme(ColorTheme colorTheme){
        Color[] bgColor = colorTheme.bgColor;
        Color[] fgColor = colorTheme.fgColor;
        Color pointColor = colorTheme.pointColor;

        //table
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        songTable.setRowHeight(20);
        songTable.setShowGrid(false);
        songTable.setBackground(bgColor[0]);
        songTable.setForeground(fgColor[1]);
        songTable.getTableHeader().setBackground(bgColor[1]);
        songTable.getTableHeader().setForeground(fgColor[2]);

        //table row selection
        songTable.setSelectionBackground(pointColor);
        songTable.setSelectionForeground(fgColor[0]);
    }
}
