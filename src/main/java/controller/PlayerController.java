package controller;

import model.Song;
import model.SongLibrary;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.File;

/**
 * This Controller has reusable player control functions:
 * Play, Stop, Pause, Resume, Previous, Next.
 */
public class PlayerController {
    private BasicPlayer player;
    private Song currentSong;       //different from selectedSong
    private SongLibrary library;    //for skipping to prev/next song

    public PlayerController(SongLibrary library){
        player = new BasicPlayer();
        this.library = library;
        currentSong = new Song();
    }

    public Song getCurrentSong() {
        return currentSong;
    }
    public void setCurrentSong(Song song) {
        currentSong = song;
    }

    public int getPlayerStatus(){
        return player.getStatus();
    }


    //------------- Music player control --------------

    //Might not needed
    public void playSong() {
        playSong(currentSong);
    }

    public void playSong(Song song){
        if(song!=null){
            try {
                player.open(new File(song.getPath()));
                player.play();
                currentSong = song;
            } catch(BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("Play Song: "+currentSong.getTitleAndArtist());
        }
    }

    public void stopSong(){
        try {
            player.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
        System.out.println("Stop Song");
    }

    public void pauseSong(){
        if(player.getStatus() == BasicPlayer.PLAYING){
            try {
                player.pause();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("Pause Song");
        }
    }

    public void resumeSong(){
        if(player.getStatus() == BasicPlayer.PAUSED){
            try {
                player.resume();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("Resume Song: "+currentSong.getTitleAndArtist());
        }
    }

    public void playPrevSong(){
        //TODO set prev song as currentSong and play
        //The desired action should be implemented here
        //for additional user actions(e.g. key shortcut or standard menu)

        //Song prevSong = library.get(current-1);
        //change currentSong
        //playSong(currentSong);
    }

    public void playNextSong(){
        //TODO set next song as currentSong and play
        //change currentSong
        //playSong(currentSong);
    }
}
