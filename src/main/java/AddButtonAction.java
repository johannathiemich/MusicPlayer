import javazoom.jlgui.basicplayer.BasicPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddButtonAction extends AbstractAction {
    private String file;
    private DatabaseHandler databaseHandler;
    private MP3Handler mp3Handler;

    public AddButtonAction(String file, DatabaseHandler databaseHandler) {
        this.file = file;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Song song = MP3Handler.createSong(file);
        databaseHandler.addSong(song);
    }
}
