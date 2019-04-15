package controller;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import model.Song;
import view.MusicPlayerGUI;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * PlayerController manages actions related to playing songs
 * Play, Stop, Pause, Resume, Previous, Next.
 */
public class PlayerController {
    private BasicPlayer player;
    private Song currentSong;            //the song currently loaded on the BasicPlayer
    private ArrayList<Song> songList;    //can be either a library or a playlist
    private MusicPlayerGUI playerView; //to reflect player's action to the view

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
        if(song!=null){
            try {
                player.open(new File(song.getPath()));
                player.play();
                this.setCurrentSong(song);
            } catch(BasicPlayerException e) {
                e.printStackTrace();
            }
            //reflect to the view
            playerView.updateCurrentPlayingView(currentSong);
            System.out.println("[PlayerControl] Play Song '"+currentSong.getTitleAndArtist()+"'\n");
        }
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
        int prevRow;
        int selectedRow = songList.indexOf(currentSong);
        int lastRow = songList.size() - 1;

        //selected row is negative if no row is selected --> play last song then
        if(selectedRow <= 0) {
            prevRow = lastRow;  //prevRow goes to the last
        } else {
            prevRow = selectedRow - 1;
        }

        // Get the previous song in the songList and play it
        Song prevSong = songList.get(prevRow);
        this.playSong(prevSong);
    }

    /**
     * Play the song that comes after the currently playing song in the songList
     */
    public void playNextSong(){

        int nextRow;
        int selectedRow = songList.indexOf(currentSong);
        int lastRow = songList.size() - 1;

        if(selectedRow == lastRow) {
            nextRow = 0;    //nextRow goes to the top
        } else {
            nextRow = selectedRow + 1;
        }

        // Get the next song in the songList and play it
        Song nextSong = songList.get(nextRow);
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
        return value / abs(this.player.getMaximumGain() - this.player.getMinimumGain());
    }
}
