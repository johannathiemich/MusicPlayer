package model;

import database.DatabaseHandler;

import java.util.ArrayList;

/**
 * Playlist class contains Songs by extending ArrayList Song.
 * This class represents a playlist
 */
public class Playlist extends ArrayList<Song> {

    //private ArrayList<Song> songList;
    private String name;
    private static DatabaseHandler dbHandler = DatabaseHandler.getInstance();
    //this needs to be instantiated just once at the very start of the application
    private static SongLibrary library;

    //private constructor since we only instantiate this class using the static factory method in order to keep track
    //of all the playlists already instantiated
    public Playlist(String name) {
        this.name = name;
        library = new SongLibrary(dbHandler.getSongLibrary());
    }

    public Playlist (String name, ArrayList<Song> songArray) {
        this.name = name;
        this.addAll(songArray);
        library = new SongLibrary(dbHandler.getSongLibrary());
    }

    /**
     * This method adds a list of song to this playlist
     * @param songs the songs to be added
     * @return true if all the songs could be added, false if at least one could not be added
     */
    public boolean addMultipleSongs(ArrayList<Song> songs) {
        boolean success = true;
        for(Song newSong: songs) {
            if (success) {
                success = success && this.addSong(newSong);
            }
        }
        return success;
    }

    /**
     * This method adds a song to this playlist
     * @param song the song to be added
     * @return true if the song was added successfully, false if not (if the song is null or is already in the playlist)
     */
    public boolean addSong(Song song) {
        boolean success = true;
        if(song.getPath() == null) {
            System.out.println("[Playlist_ERROR] Not added. filePath: null\n");
            return false;
        }
        if (!songInLibrary(song)) {
            System.out.println("Song " + song.getPath() + "is not in library yet. Adding it now.");
            success = success && library.addSong(song);
        }
        // Check if the song already exists in the playlist
        if (songInPlaylist(song)) {
            System.out.print("[Playlist: "+this.name+"] Song not added. Already in the playlist.\t");
            return false;
        } else {
            //add song to Playlist the database
            success = success && dbHandler.addSongToPlaylist(this, song);
            this.add(song);
            System.out.print("[Playlist: "+this.name+"] Added a song. \t");
        }
        System.out.println("'"+song.getTitleAndArtist()+"'\n");
        return success;
    }

    /**
     * This method returns the name of this playlist
     * @return the name of this playilst
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method checks if the song is in the song library
     * @param song the song to be checked
     * @return true if the song is in the library, false if not
     */
    private boolean songInLibrary(Song song){
        for (Song currSong : dbHandler.getSongLibrary()) {
            if (currSong.getPath().equals(song.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if the song is in this playlist
     * @param song the song to be checked
     * @return true if the song is in this playlist, false if not
     */
    private boolean songInPlaylist(Song song){
        for (Song currSong : dbHandler.getSongsInPlaylist(this)) {
            if (currSong.getPath().equals(song.getPath())) {
                return true;
            }
        }
        return false;
    }

  /**  //TODO: maybe have a DB method that returns a list of Playlist objects instead of Strings
    private static HashMap<String, Playlist> getPlaylistMapFromDB() {
        if (allPlaylists == null)
            allPlaylists = new HashMap<String, Playlist>();
        for (String playlistName : dbHandler.getAllPlaylistsStrings()) {
            Playlist newPlaylist = new Playlist(playlistName);
            newPlaylist.songList = dbHandler.getSongsInPlaylist(newPlaylist);
            allPlaylists.put(playlistName, newPlaylist);
        }
        return allPlaylists;
    }**/

    /**
     * This method updates the library that this playlist is referring to
     * @param plibrary the new library
     */
    public static void setLibrary(SongLibrary plibrary) {
        library = plibrary;
    }
}
