package view;

import model.PlaylistLibrary;
import model.Song;
import model.SongLibrary;
//import controller.TableRowTransferHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * SongListView class is to show either the library or a playlist.
 * This contains a table with column headers for songs.
 * Table related listeners are attached.
 */
public class SongListView extends JPanel {
    //components for table
    private JScrollPane tableScrollPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private String[] columnHeader;
    private int tableRowHeight = 24;
    private JPopupMenu tableHeaderPopup;
    private  ArrayList<JCheckBoxMenuItem> columnList;

    /**
     * Constructs a panel to show a list of songs
     * with an empty table view.
     */
    public SongListView(){
        // Table setup
        columnHeader = new String[]{"Title", "Artist", "Album", "Year", "Comment", "Genre"};
        table = new JTable(){
            @Override   //block table contents editing
            public boolean isCellEditable(int row, int column) { return false; }
        };

        //initialize table for dynamic row addition
        initializeTable();

        //table behavior setups
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setDragEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);

        //ui setups
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFont(MusicPlayerGUI.FONT);
        table.getTableHeader().setFont(MusicPlayerGUI.FONT);
        table.setRowHeight(tableRowHeight);
        table.setShowGrid(false);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        tableHeaderPopup = new JPopupMenu();
        columnList = new ArrayList<JCheckBoxMenuItem>();
        createTableHeaderPopup();

        //put table in place
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout());
        this.add(tableScrollPane, BorderLayout.CENTER);
    //    table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
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
        table.setModel(tableModel);
    }

    /**
     * Updates the table view.
     * @param songList list of Songs to be reflected in table view,
     *                 which can be from the library or a playlist.
     */
    public void updateTableView(ArrayList<Song> songList) {
        initializeTable();
        for (Song song : songList) {
            tableModel.addRow(song.toArrayNoPath());
        }
        tableModel.fireTableDataChanged();
        table.repaint();
        this.repaint();
    }

    public void createTableHeaderPopup() {
        JCheckBoxMenuItem title = new JCheckBoxMenuItem("Title", true);
        title.setEnabled(false);
        JCheckBoxMenuItem artist = new JCheckBoxMenuItem("Artist", true);
        JCheckBoxMenuItem album = new JCheckBoxMenuItem("Album", true);
        JCheckBoxMenuItem year = new JCheckBoxMenuItem("Year", true);
        JCheckBoxMenuItem comment = new JCheckBoxMenuItem("Comment", true);
        JCheckBoxMenuItem genre = new JCheckBoxMenuItem("Genre", true);

        tableHeaderPopup.add(title);
        tableHeaderPopup.add(artist);
        tableHeaderPopup.add(album);
        tableHeaderPopup.add(year);
        tableHeaderPopup.add(comment);
        tableHeaderPopup.add(genre);

        columnList.add(title);
        columnList.add(artist);
        columnList.add(album);
        columnList.add(year);
        columnList.add(comment);
        columnList.add(genre);
    }
    /**
     * Returns the table of this SongListView
     * @return JTable containing songs
     */
    public JTable getSongTable(){
        return table;
    }

    /**
     * Set color theme on the table view panel.
     */
    public void setColorTheme(ColorTheme colorTheme){
        Color[] bgColor = colorTheme.bgColor;
        Color[] fgColor = colorTheme.fgColor;
        Color[] pointColor = colorTheme.pointColor;

        //table
        table.setBackground(bgColor[0]);
        table.setForeground(fgColor[1]);
        table.getTableHeader().setBackground(bgColor[1]);
        table.getTableHeader().setForeground(fgColor[2]);

        //horizontal line of the table
        table.setGridColor(bgColor[2]);

        //table row selection
        table.setSelectionBackground(pointColor[0]);
        table.setSelectionForeground(pointColor[1]);
    }

    public void addItemListenerTableHeader(ItemListener listener) {
        for (int i = 0; i < tableHeaderPopup.getComponentCount(); i++) {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) tableHeaderPopup.getComponent(i);
            item.addItemListener(listener);
        }
    }

    public JPopupMenu getTableHeaderPopup() {
        tableHeaderPopup.setVisible(true);
        return tableHeaderPopup;
    }

    public void hideColumn(TableColumn column) {
        column.setWidth(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
    }

    public void showColumn(TableColumn column) {
        int numCol = 0;
        int size = 0;
        for (JCheckBoxMenuItem item : columnList) {
            if (item.isSelected()) {
                numCol++;
            }
        }
        size = table.getWidth() / numCol;

        column.setWidth(size);
        column.setMinWidth(size);
        column.setMaxWidth(size);
    }

    private int getColumnNumber(JCheckBoxMenuItem item) {
        switch (item.getText()) {
            case "Path":
                return 0;
            case "Title":
                return 1;
            case "Artist":
                return 2;
            case "Album":
                return 3;
            case "Year":
                return 4;
            case "Comment":
                return 5;
            case "Genre":
                return 6;
            default:
                return 0;
        }
    }

}
