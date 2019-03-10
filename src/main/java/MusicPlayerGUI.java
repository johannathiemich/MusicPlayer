import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

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


    //TODO separate DatabaseHandler from View
    private DatabaseHandler db = new DatabaseHandler();

    public MusicPlayerGUI(String title) {
        this.setTitle(title);

        BasicPlayer player = new BasicPlayer();

        mainPanel = new JPanel();
        bottomButtonPanel = new JPanel();

        songTable = new JTable();
        initializeTable();
        updateTable();
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

        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomButtonPanel, BorderLayout.SOUTH);
        this.pack();
    }

    //TODO separate updateTable() from Data manipulation. Code below should be done in Controller.
    public void updateTable() {
        //DefaultTableModel tableModel = (DefaultTableModel) songTable.getModel();

        String[] columns = {"Path", "Title", "Artist", "Album", "Year", "Comment", "Genre"};
        ArrayList<Song> library = db.getSongLibrary();
        String[][] data = new String[library.size()][7];
        for (int i = 0; i < library.size(); i++) {
            Song current = library.get(i);
            data[i] = current.toArray();
           //tableModel.addRow(data[i]);
        }
        //songTable.setModel(tableModel);
        //tableModel.fireTableDataChanged();
        songTable = new JTable(data, columns);

    }

    public void initializeTable() {
        songTable = new JTable();
        songTable.setFillsViewportHeight(true);
    }


    //TODO add addActionListener for Table
    //TODO add addActionListener for Buttons
    //TODO add addActionListener for Slider
}
