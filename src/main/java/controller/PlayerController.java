package controller;

import database.DatabaseHandler;
import javazoom.jlgui.basicplayer.*;
import model.Song;
import view.MusicPlayerGUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * PlayerController manages actions related to playing songs
 * Play, Stop, Pause, Resume, Previous, Next.
 */
public class PlayerController {
    private BasicPlayer player;

    private ArrayList<Song> songList;  //can be either a library or a playlist
    private String songListName;
    private Song currentSong;          //the song currently loaded on the BasicPlayer
    private int currSongIndex;         //the current song index within songList

    private MusicPlayerGUI playerView; //to reflect player's action to the view

    //Recently Played Songs
    private ArrayList<Song> recentlyPlayedSongs;
    private int recentlyPlayedLimit = 10;

    private boolean isRepeating = false;
    private boolean isShuffling = false;

    /**
     * Constructor for this class
     * @param songList a list of all songs currently contained in the songList
     */
    public PlayerController(ArrayList<Song> songList, MusicPlayerGUI playerView){
        player = new BasicPlayer();
        this.songList = songList;
        if (songList.size() > 0) {
            //first song in the songList by default
            currSongIndex = 0;
            currentSong = songList.get(currSongIndex);
        }
        this.playerView = playerView;

        //add listener to the basic player
        player.addBasicPlayerListener(new MyBasicPlayerListener());

        //initialize the recently played songs
        //recentlyPlayedSongs = new ArrayList<Song>();
        recentlyPlayedSongs = DatabaseHandler.getInstance().getRecentSongs();
        initializeRecentlyPlayedMenu();
    }

    private void initializeRecentlyPlayedMenu() {
        for (Song song : recentlyPlayedSongs) {
            playerView.addMenuItemToPlayRecent(song.getFileName());
        }
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

    public void setCurrSongIndex(int index) {
        currSongIndex = index;
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
    public ArrayList<Song> getRecentlyPlayedSongs() { return DatabaseHandler.getInstance().getRecentSongs(); }

    /**
     * Sets isRepeating.
     * If true, repeat the currently playing song.
     * @param repeating the boolean
     */
    public void setRepeat(boolean repeating) {
        isRepeating = repeating;
        System.out.println("[Player] repeat: " + (isRepeating ? "on" : "off") );
        //TODO better to have updateRepeatMenuCheck here...
    }

    public void setShuffle(boolean shuffle) {
        isShuffling = shuffle;
        System.out.println("[Player] : " + (isShuffling ? "on" : "off"));
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

        //if nothing is selected, set the song to be the first song on the list
        if(song == null) {
            song = songList.get(0);
            System.out.println("[Player] selecting the first song on the list.");
        }

        this.setCurrentSong(song);

        //play the song
        try {
            player.open(new File(currentSong.getPath()));
            player.play();
        } catch(BasicPlayerException e) {
            e.printStackTrace();
        }

        System.out.println("[PlayerControl] Play Song '"+currentSong.getTitleAndArtist()+"' currSongIndex:"+currSongIndex);

        //reflect to the view
        //TODO make this to be handled in stateUpdated() of MyBasicPlayerListener (line 350)
        playerView.getControlView().updateCurrentPlayingView(currentSong);
        playerView.changeTableRowSelection(currSongIndex);

        addRecentlyPlayed();

        System.out.println();
    }

    /**
     * Plays the song and updates the currSongIndex
     * @param song to be played
     * @param index set the currSongIndex
     */
    public void playSong(Song song, int index){
        playSong(song);
        currSongIndex = index;
    }

    /**
     * Adds currently playing song to the list of recentlyPlayedSongs and [Play Recent] submenu.
     * Note that only the most recently played songs are kept within recentlyPlayedLimit.
     */
    public void addRecentlyPlayed(){
        //keep the number of songs stored under limit
        int size = recentlyPlayedSongs.size();
        if(size==recentlyPlayedLimit) {
            recentlyPlayedSongs.remove(size-1);
            playerView.removeLastMenuItemUnderPlayRecent();
        }

        //add the song to the top of the recently played list
        recentlyPlayedSongs.add(0, currentSong);
        //System.out.println("recentlyPlayedSongs.size(): "+recentlyPlayedSongs.size());
        //add the song title$artist to the [Play Recent] menu
        playerView.addMenuItemToPlayRecent(currentSong.getFileName());
        //System.out.println("[PlayerControl] '"+currentSong.getFileName()+"' is added to the recently played list.");
        System.out.println("'"+currentSong.getFileName()+"' is added to recentlyPlayedSongs and [Play Recent] submenu.");
        DatabaseHandler.getInstance().addRecentSong(currentSong.getPath());
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
        int lastIndex = songList.size() - 1;

        int prevIndex;
        //rotate
        if(currSongIndex == 0) {
            prevIndex = lastIndex;  //prevRow goes to the last
        } else {
            prevIndex = currSongIndex - 1;
        }
        System.out.println("[playPrevSong] song index from:" + currSongIndex+" to:"+prevIndex);

        // Get the previous song in the songList and play it
        Song prevSong = songList.get(prevIndex);
        this.playSong(prevSong, prevIndex);
    }

    /**
     * Play the song that comes after the currently playing song in the songList
     */
    public void playNextSong(){
        int lastIndex = songList.size() - 1;

        int nextIndex;
        //rotate
        if(currSongIndex == lastIndex) {
            nextIndex = 0;
        } else {
            nextIndex = currSongIndex + 1;
        }
        System.out.println("[playNextSong] song index from:" + currSongIndex+" to:"+nextIndex);

        // Get the next song in the songList and play it
        Song nextSong = songList.get(nextIndex);
        this.playSong(nextSong, nextIndex);
    }

    /**
     * Changes the volume of the basic player.
     * Note that the value is converted for the basic player setGain() method.
     * @param val value to be set as volume, range from 0 to 100
     */
    public void setVolume(int val) {
        if (val <= 0) {
            System.out.println("volume min 0");
        } if (val >= 100) {
            System.out.println("volume max 100");
        } else {
            double convertedVal = val / Math.abs(player.getMaximumGain() - player.getMinimumGain());
            try {
                player.setGain(convertedVal);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
            System.out.println("volume: " + val);
        }
    }

    /**
     * Increases the volume of the basic player by 5
     */
    public void increaseVolume(){
        System.out.println("Volume increased by 5%");
        int currentVolume = playerView.getControlView().getVolumeSlider().getValue();
        int increasedVolume = currentVolume + 5;
        if (increasedVolume > 100) { increasedVolume = 100; }

        setVolume(increasedVolume);
        //update the view
        playerView.setVolumeSlider(increasedVolume);
    }

    /**
     * Decreases the volume of the basic player by 5
     */
    public void decreaseVolume(){
        System.out.println("Volume decreased by 5%");
        int currentVolume = playerView.getControlView().getVolumeSlider().getValue();
        int decreasedVolume = currentVolume - 5;
        if (decreasedVolume < 0) { decreasedVolume = 0; }

        setVolume(decreasedVolume);
        //update the view
        playerView.setVolumeSlider(decreasedVolume);
    }

    //TODO below should be removed and the related parts should be refactored.
    public int getCurrSongIndex() {
        return this.currSongIndex;
    }

    /**
     * MyBasicPlayerListener class implements the actions triggered from basic player
     * 1. Update the progress bar as a song is playing
     * 2. Auto play the next song
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
         * Repeat / Shuffle / Autoplay features are implemented.
         * @param basicPlayerEvent
         */
        @Override
        public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
            //Autoplay the next music when the player finishes playing the current music
            if(basicPlayerEvent.getCode() == BasicPlayerEvent.EOM) {    //EOM: End of MP3
                if(isRepeating){
                    System.out.println("[Player] Repeat the song.");
                    playSong();
                }else if (isShuffling){
                    int nextSongIndex = (int)(Math.random() * songList.size());
                    setCurrentSong(songList.get(nextSongIndex));
                    System.out.println("[Player] next song index: " + nextSongIndex);

                } else {
                    //if not, Auto play the next song
                    System.out.println("[Player] Auto play the next song.");
                    playNextSong();
                }
            }

            //TODO might be better to handle the ui updates of play/stop/resume states...
            if(basicPlayerEvent.getCode() == BasicPlayerEvent.PLAYING) {
                System.out.println("[Player] state changed to BasicPlayerEvent.PLAYING");
                //TODO update row selection on the focused window?
                //TODO change the play button to "||"
            }

        }

        @Override
        public void opened(Object o, Map map) { }
        @Override
        public void setController(BasicController basicController) { }
    }
}
