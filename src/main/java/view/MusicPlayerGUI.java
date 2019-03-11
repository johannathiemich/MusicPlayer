package view;

import model.Song;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class MusicPlayerGUI extends JFrame {

    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JPanel bottomPanel;
    
    private JTable songTable;
    private DefaultTableModel tableModel;
    private String[] columnHeader;

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

        initializeTable();

        tableScrollPane = new JScrollPane(songTable);

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

        //TODO standard menu

        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.pack();
    }

    public void initializeTable() {
        tableModel = new DefaultTableModel(columnHeader,0);
        songTable = new JTable(tableModel){
            //block table contents editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        songTable.setFillsViewportHeight(true);
        songTable.setShowHorizontalLines(true);
    }

    //Useful when table view needs to change after 'Add Song To Library' action
    public void addRowToTableView(String[] row){ 
        tableModel.addRow(row);
    }

    public void updateTableView(ArrayList<Song> library) {

        for(int i=0; i<library.size(); i++){
            tableModel.addRow(library.get(i).toArray());
        }
    }

    public JTable getSongTable(){
        return songTable;
    }

    //For 'Play'<->'Pause' text change
    public void setPlayBtnText(String text){
        playBtn.setText(text);
    }

    //Add listeners to components
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
