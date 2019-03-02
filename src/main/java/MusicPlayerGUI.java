import javax.swing.*;
import javax.swing.border.Border;

import javazoom.jlgui.basicplayer.BasicPlayer;

import java.awt.*;


public class MusicPlayerGUI extends JFrame {

    JPanel mainPanel;
    JPanel tablePanel;
    JPanel bottomButtonPanel;

    JTable songTable;
    JButton startSong;

    public MusicPlayerGUI(String title) {
        this.setTitle(title);

        BasicPlayer player = new BasicPlayer();

        mainPanel = new JPanel();
        tablePanel = new JPanel();
        bottomButtonPanel = new JPanel();

        songTable = new JTable();
        tablePanel.add(songTable);

        startSong = new JButton("Play");
        bottomButtonPanel.add(startSong);

        this.add(tablePanel, BorderLayout.CENTER);
        this.add(bottomButtonPanel, BorderLayout.PAGE_END);
        this.pack();
    }
}
