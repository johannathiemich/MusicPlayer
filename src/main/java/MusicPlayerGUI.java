import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import javazoom.jlgui.basicplayer.BasicPlayer;

import java.awt.*;
import java.util.ArrayList;


public class MusicPlayerGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel bottomButtonPanel;

    //TODO private fields
    JTable songTable;
    JScrollPane tableScrollPane;
    JButton startSong;
    JButton nextSong;
    JButton prevSong;
    JButton stopSong;
    JSlider scrollVolume;

    private String[] columnHeader;
    private DefaultTableModel tableModel;


    public MusicPlayerGUI(String frameTitle) {
        this.setTitle(frameTitle);

        columnHeader = new String[]{"Path", "Title", "Artist", "Album", "Year", "Comment", "Genre"};

        mainPanel = new JPanel();
        bottomButtonPanel = new JPanel();

        initializeTable();

        tableScrollPane = new JScrollPane(songTable);

        stopSong = new JButton("Stop");
        bottomButtonPanel.add(stopSong);

        prevSong = new JButton("<<");
        bottomButtonPanel.add(prevSong);

        startSong = new JButton("Play");
        bottomButtonPanel.add(startSong);

        nextSong = new JButton(">>");
        bottomButtonPanel.add(nextSong);

        scrollVolume = new JSlider();
        bottomButtonPanel.add(scrollVolume);

        //TODO layout bottomButtonPanel

        //TODO standard menu

        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomButtonPanel, BorderLayout.SOUTH);
        this.pack();
    }

    public void initializeTable() {
        tableModel = new DefaultTableModel(columnHeader,0);
        songTable = new JTable(tableModel){
            //Disable user editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        songTable.setFillsViewportHeight(true);
        songTable.setShowHorizontalLines(true);
    }

    //This will be useful when the view needs to change after 'Add Song To Library' action
    public void addRowToTableView(String[] row){
        tableModel.addRow(row);
    }

    public void updateTableView(ArrayList<Song> library) {

        for(int i=0; i<library.size(); i++){
            tableModel.addRow(library.get(i).toArray());
        }
    }

}
