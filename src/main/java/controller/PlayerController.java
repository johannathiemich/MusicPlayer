package controller;

import javazoom.jlgui.basicplayer.*;
import model.Song;
import view.MusicPlayerGUI;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * PlayerController manages actions related to playing songs
 * Play, Stop, Pause, Resume, Previous, Next.
 */
public class PlayerController {
    private BasicPlayer player;
    private Song currentSong;            //the song currently loaded on the BasicPlayer
    private ArrayList<Song> songList;    //can be either a library or a playlist
    private MusicPlayerGUI playerView; //to reflect player's action to the view

    //TODO better to handle the table selection from somewhere else not in player controller...
    private JTable table;
    private int currSongIndex;

    //Recently Played Songs
    private ArrayList<Song> recentlyPlayedSongs;

    /**
     * Constructor for this class
     * @param songList a list of all songs currently contained in the songList
     */
    public PlayerController(ArrayList<Song> songList, MusicPlayerGUI playerView){
        player = new BasicPlayer();
        this.songList = songList;
        if (songList.size() > 0) {
            currentSong = songList.get(0);   //first song in the songList by default
        }
        this.playerView = playerView;

        //add listener to the basic player
        player.addBasicPlayerListener(new MyBasicPlayerListener());

        //initialize the recently played songs
        recentlyPlayedSongs = new ArrayList<Song>();
    }

    /**
     * This methods returns the currently selected song in the table.
     * @return the song currently selected
     */
    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Gets the list of songs that the player is playing on.
     * @return ArrayList Song
     */
    public ArrayList<Song> getSongList(){
        return songList;
    }

    /**
     * Sets the list of songs that the player plays on.
     * @param songList either Library or Playlist
     */
    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    /**
     * Updates the list of songs that the player plays on.
     * @param songList either Library or Playlist
     */
    public void updateSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    /**
     * This method changes the currently selected song
     * @param song the song that is supposed to be marked as selected in the table
     */
    public void setCurrentSong(Song song) {
        currentSong = song;
    }

    /**
     * This method returns the current status of the basic player.
     * @return the current status of the basic player (STOP, RESUME, PLAY, SEEKING)
     */
    public int getPlayerStatus(){
        return player.getStatus();
    }

    /**
     * Gets the recentlyPlayedSongs
     * @return ArrayList<Song>
     */
    public ArrayList<Song> getRecentlyPlayedSongs() { return recentlyPlayedSongs; }

    //------------- Music player control --------------

    /**
     * Play a current song set in player.
     */
    public void playSong() {
        playSong(currentSong);
    }

    /**
     * This method plays the currently selected song
     * @param song to be played
     */
    public void playSong(Song song){

        //if nothing is selected, set the song to be the first song on the list
        if(song == null) {
            song = songList.get(0);
            System.out.println("[Player] selecting the first song on the list.");
        }

        //play the song
        try {
            this.setCurrentSong(song);
            player.open(new File(currentSong.getPath()));
            player.play();
        } catch(BasicPlayerException e) {
            e.printStackTrace();
        }

        System.out.println("[PlayerControl] Play Song '"+currentSong.getTitleAndArtist()+"'\n");

        //reflect to the view
        playerView.getControlView().updateCurrentPlayingView(currentSong);
        //TODO update all playlist window's view

        //add the song to the top of the recently played list
        recentlyPlayedSongs.add(0, currentSong);
        System.out.println("[Player] '"+currentSong.getTitleAndArtist()+"' is added to the recently played list.");
    }

    /**
     * Stop the song currently playing.
     */
    public void stopSong(){
        try {
            player.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
        System.out.println("[PlayerControl] Stop Song\n");
    }

    /**
     * Pause the song currently playing.
     */
    public void pauseSong(){
        if(player.getStatus() == BasicPlayer.PLAYING){
            try {
                player.pause();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("[PlayerControl] Pause Song\n");
        }
    }

    /**
     * Resume the song that was paused before
     * from the position it was paused.
     */
    public void resumeSong(){
        if(player.getStatus() == BasicPlayer.PAUSED){
            try {
                player.resume();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("[PlayerControl] Resume Song '"+currentSong.getTitleAndArtist()+"'\n");
        }
    }

    /**
     * Play the song that comes before the currently playing song in the songList
     */
    public void playPrevSong(){
        currSongIndex = table.getSelectedRow();
        //currSongIndex = playerView.getSongTable().getSelectedRow();
        //if (currSongIndex == -1 ) {
        //    currSongIndex = table.getSelectedRow();
        //}
        int prevRow;
        //int selectedRow = songList.indexOf(currentSong);
        int selectedRow = currSongIndex;
        int lastRow = songList.size() - 1;

        //selected row is negative if no row is selected --> play last song then
        if(selectedRow <= 0) {
            prevRow = lastRow;  //prevRow goes to the last
        } else {
            prevRow = selectedRow - 1;
        }

        // Get the previous song in the songList and play it
        Song prevSong = songList.get(prevRow);
        currSongIndex = prevRow;
        this.playSong(prevSong);
    }

    /**
     * Play the song that comes after the currently playing song in the songList
     */
    public void playNextSong(){
        //TODO make it work without checking the table row.. it should be based on the songList
        currSongIndex = table.getSelectedRow();
        //currSongIndex = playerView.getSongTable().getSelectedRow();
        //if (currSongIndex == -1 ) {
        //    currSongIndex = table.getSelectedRow();
        //
        System.out.println("before currently selected row: " + currSongIndex);
        int nextRow;
        //int selectedRow = songList.indexOf(currentSong);
        int selectedRow = currSongIndex;
        int lastRow = songList.size() - 1;

        if(selectedRow == lastRow) {
            nextRow = 0;    //nextRow goes to the top
        } else {
            nextRow = selectedRow + 1;
        }

        // Get the next song in the songList and play it
        Song nextSong = songList.get(nextRow);
        currSongIndex = nextRow;
        System.out.println("after currently selected row: " + currSongIndex);

        this.playSong(nextSong);
    }

    /**
     * Change volume of the basic player
     * @param volume value to be set as volume
     */
    public void setVolume(double volume) {
        try {
            this.player.setGain(this.convertVolume(volume));
            System.out.println(this.player.getGainValue());
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method converts the value of the JSlider into a volume that can be used by the basic player setGain()
     * method.
     * @param value the value to be converted.
     * @return the input value for the basic player setGain() method
     */
    private double convertVolume(double value) {
        return value / Math.abs(this.player.getMaximumGain() - this.player.getMinimumGain());
    }

    public int getCurrSongIndex() {
        return this.currSongIndex;
    }

    public void setSongTable(JTable table) {
        this.table = table;
    }


    /**
     * MyBasicPlayerListener class implements the actions triggered from basic player
     *
     */
    public class MyBasicPlayerListener implements BasicPlayerListener {
        /**
         * Progress callback while playing.
         * This method is called several time per seconds while playing.
         * properties map includes audio format features
         * such as instant bitrate, microseconds position, current frame number, ...
         * @param b         bytesread - from encoded stream.
         * @param microsec  microseconds - elapsed (reseted after a seek !).
         * @param bytes     pcmdata - PCM samples.
         * @param map       java.util.Map properties - audio stream parameters.
         */
        @Override
        public void progress(int b, long microsec, byte[] bytes, Map map) {
            // Update the progress bar
            playerView.getControlView().updateProgressView((int)microsec/1000, currentSong.getTime());
            //TODO update all windows
        }

        /**
         * Notification callback for basicplayer events such as opened, eom ...
         * @param basicPlayerEvent
         */
        @Override
        public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
            //Autoplay the next music when the player finishes playing the current music
            if(basicPlayerEvent.getCode() == BasicPlayerEvent.EOM) {    //EOM: End of MP3
                //TODO check if repeated is clicked
                //TODO shuffle..?
                //TODO if not, play the next song
                System.out.println("[Player] end of the music '"+currentSong.getTitleAndArtist()+"'");
                playNextSong();
            }

            //TODO might be better to handle the ui updates of play/stop/resume states...

        }

        @Override
        public void opened(Object o, Map map) { }
        @Override
        public void setController(BasicController basicController) { }
    }
}
