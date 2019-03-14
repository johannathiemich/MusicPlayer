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
        currentSong = new Song();
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
     *
     */
    public void playSong() {
        playSong(currentSong);
    }

    /**
     * This method plays the currently selected song
     * @param song
     */
    public void playSong(Song song){
        if(song!=null){
            try {
                player.open(new File(song.getPath()));
                player.play();
                currentSong = song;
            } catch(BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("[PlayerControl] Play Song: "+currentSong.getTitleAndArtist());
        }
    }

    /**
     * This method stops the song currently playing (setting the current playing position to zero)
     */
    public void stopSong(){
        try {
            player.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
        System.out.println("[PlayerControl] Stop Song");
    }

    /**
     * This method pauses the song and remembers its current real time position (so that it can be resumed from that
     * point on later after)
     */
    public void pauseSong(){
        if(player.getStatus() == BasicPlayer.PLAYING){
            try {
                player.pause();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("[PlayerControl] Pause Song");
        }
    }

    /**
     * This method plays the song that was paused before from the position it was paused.
     */
    public void resumeSong(){
        if(player.getStatus() == BasicPlayer.PAUSED){
            try {
                player.resume();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("[PlayerControl] Resume Song: "+currentSong.getTitleAndArtist());
        }
    }

    /**
     * This method plays the song that comes before the currently playing song in the song table
     */
    public void playPrevSong(){
        int prevRow;
        int selectedRow = playerView.getSongTable().getSelectedRow();
        int lastRow = playerView.getSongTable().getRowCount() - 1;

        //selected row is negative if no row is selected --> play last song then
        if(selectedRow <= 0) {
            prevRow = lastRow;
        } else {
            prevRow = selectedRow - 1;
        }

        // Update row selection on the view
        playerView.changeTableRowSelection(prevRow);
        // Get the previous song from the library
        Song prevSong = library.get(prevRow);
        currentSong = prevSong;

        // Set prevSong as a current one and play it
        this.setCurrentSong(prevSong);
        this.playSong();
        // Change the button text
        playerView.setPlayBtnText("||");
    }

    /**
     * This method plays the song that comes after the currently playing song in the song table
     */
    public void playNextSong(){

        int nextRow;
        int selectedRow = playerView.getSongTable().getSelectedRow();
        int lastRow = playerView.getSongTable().getRowCount() - 1;
        //selected row is -1 if no row is selected --> play the first song then
        if(selectedRow == lastRow || selectedRow < 0) {
            nextRow = 0;    //nextRow goes to the top
        } else {
            nextRow = selectedRow + 1;
        }

        // Update row selection on the view
        playerView.changeTableRowSelection(nextRow);
        // Get the previous song from the library
        Song nextSong = library.get(nextRow);
        currentSong = nextSong;

        // Set prevSong as a current one and play it
        this.setCurrentSong(nextSong);
        this.playSong();
        // Change the button text
        playerView.setPlayBtnText("||");
    }

    /**
     * This method changes the volume of the basic player
     * @param volume the new volume value to be used by the basic player
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
