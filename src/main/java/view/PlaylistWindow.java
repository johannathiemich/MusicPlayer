package view;

import model.Playlist;

import javax.swing.*;
import java.awt.*;

public class PlaylistWindow extends JDialog {

    private String playlistName;
    private SongListView playlistView;
    private ControlView controlView;

    /**
     * Constructs a new window to display songs in a playlist.
     * This contains a table view of songs in the playlist
     * and a control view at the bottom.
     * @param owner the owner of this playlist window
     * @param playlist the playlist to be displayed in the new window
     * @param theme ColorTheme to apply to this playlist window
     */
    public PlaylistWindow(MusicPlayerGUI owner, Playlist playlist, ColorTheme theme){
        super(owner, "Playlist: "+playlist.getName(), false);
        //this.setDefaultCloseOperation();
        this.setPreferredSize(new Dimension(400,300));
        this.setMinimumSize(new Dimension(400,300));

        //initialize variables
        this.playlistName = playlist.getName();
        controlView = owner.getControlView();
        playlistView = owner.getSongListView();

        //set the songListView (the table view)
        playlistView.updateTableView(playlist.getSongList());

        //not show song info panel as a default for playlist window
        controlView.showSongInfoPanel(false);

        //menu bar setting
        this.setJMenuBar(owner.getJMenuBar());
        this.getJMenuBar().getMenu(1).setVisible(false);

        //apply theme to the views
        playlistView.setColorTheme(theme);
        controlView.setColorTheme(theme);

        //put playlist view and control view in place
        this.setLayout(new BorderLayout());
        this.add(playlistView, BorderLayout.CENTER);
        this.add(controlView, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);

        //connects listeners to the window
        //...
    }

    /**
     * Gets the table view of the playlist from this window
     * @return SongListView
     */
    public SongListView getSongListView(){
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
