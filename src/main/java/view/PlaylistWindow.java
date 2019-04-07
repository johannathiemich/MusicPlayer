package view;

import javax.swing.*;
import java.awt.*;

public class PlaylistWindow extends JFrame {

    private String playlistName;
    private SongListView playlistView;
    private ControlView controlView;

    /**
     * Constructs a new window to display songs in a playlist.
     * This contains a table view of songs in the playlist
     * and a control view at the bottom.
     * @param playlistName name of the playlist
     * @param theme ColorTheme to apply to this playlist window
     */
    public PlaylistWindow(String playlistName, ColorTheme theme){
        super("Playlist: "+playlistName);
        //this.setDefaultCloseOperation();
        this.setPreferredSize(new Dimension(500,300));
        this.setMinimumSize(new Dimension(500,300));

        //initialize variables
        this.playlistName = playlistName;
        controlView = new ControlView();
        playlistView = new SongListView();

        //TODO call updateTableView(playlist)
        //playlistView.updateTableView(/*somehow playlist*/);

        //not show song info as a default for playlist window
        controlView.showSongInfoPanel(false);

        //apply theme to the views
        playlistView.setColorTheme(theme);
        controlView.setColorTheme(theme);

        //put playlist view and control view in place
        this.setLayout(new BorderLayout());
        this.add(playlistView, BorderLayout.CENTER);
        this.add(controlView, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Gets the table view of the playlist from this window
     * @return SongListView
     */
    public SongListView getTableView(){
        return playlistView;
    }

    /**
     * Gets the control view of this window
     * @return
     */
    public ControlView getControlView() {
        return controlView;
    }

    /**
     * Gets the playlist name of this window
     * @return the string of the playlist name
     */
    public String getPlaylistName(){
        return playlistName;
    }

    /**
     * Sets the playlist name
     * @param playlistName
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
