package controller;

import model.Song;
import model.SongLibrary;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

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

    /**
     * Constructor for this class
     * @param library a list of all songs currently contained in the library
     */
    public PlayerController(SongLibrary library){
        player = new BasicPlayer();
        this.library = library;
        currentSong = new Song();
    }

    /**
     * This methods returns the currently selected song in the table.
     * @return the song currently selected
     */
    public Song getCurrentSong() {
        return currentSong;
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
     *
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
        //TODO set prev song as currentSong and play
        //The desired action should be implemented here
        //for additional user actions(e.g. key shortcut or standard menu)

        //Song prevSong = library.get(current-1);
        //change currentSong
        //playSong(currentSong);
    }

    /**
     * This method plays the song that comes after the currently playing song in the song table
     */
    public void playNextSong(){
        //TODO set next song as currentSong and play
        //change currentSong
        //playSong(currentSong);
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
