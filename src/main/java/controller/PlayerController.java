package controller;

import model.Song;
import model.SongLibrary;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import view.MusicPlayerGUI;

import java.io.File;

import static java.lang.Math.abs;

/**
 * This Controller has reusable player control functions:
 * Play, Stop, Pause, Resume, Previous, Next.
 */
public class PlayerController {
    private BasicPlayer player;
    private Song currentSong;       //different from selectedSong
    private SongLibrary library;    //for skipping to prev/next song
    private MusicPlayerGUI playerView;

    /**
     * Constructor for this class
     * @param library a list of all songs currently contained in the library
     */
    public PlayerController(SongLibrary library, MusicPlayerGUI playerView){
        player = new BasicPlayer();
        this.library = library;
        if (library.size() > 0) {
            currentSong = library.get(0);   //first song in the library by default
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
     * This method updates the library
     * @param library
     */
    public void updateLibrary(SongLibrary library) {
        this.library = library;
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
            playerView.setPlayBtnText("||");
            playerView.changeTableRowSelection(library.getIndex(currentSong));
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
        playerView.setPlayBtnText("▶");
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
            playerView.setPlayBtnText("▶");
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
            playerView.setPlayBtnText("||");
            System.out.println("[PlayerControl] Resume Song '"+currentSong.getTitleAndArtist()+"'\n");
        }
    }

    /**
     * Play the song that comes before the currently playing song in the library
     */
    public void playPrevSong(){
        int prevRow;
        int selectedRow = library.getIndex(currentSong);
        int lastRow = playerView.getSongTable().getRowCount() - 1;

        //selected row is negative if no row is selected --> play last song then
        if(selectedRow <= 0) {
            prevRow = lastRow;  //prevRow goes to the last
        } else {
            prevRow = selectedRow - 1;
        }

        // Get the previous song in the library and play it
        Song prevSong = library.get(prevRow);
        this.playSong(prevSong);
    }

    /**
     * Play the song that comes after the currently playing song in the library
     */
    public void playNextSong(){

        int nextRow;
        int selectedRow = library.getIndex(currentSong);
        int lastRow = playerView.getSongTable().getRowCount() - 1;

        if(selectedRow == lastRow) {
            nextRow = 0;    //nextRow goes to the top
        } else {
            nextRow = selectedRow + 1;
        }

        // Get the next song in the library and play it
        Song nextSong = library.get(nextRow);
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
