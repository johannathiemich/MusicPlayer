package model;

import database.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class Playlist {

    private ArrayList<Song> songList;
    private String name;
    private static DatabaseHandler dbHandler = DatabaseHandler.getInstance();
    //this needs to be instantiated just once at the very start of the application
    private static HashMap<String, Playlist> allPlaylists = getPlaylistMapFromDB();
    private static SongLibrary library;

    //private constructor since we only instantiate this class using the static factory method in order to keep track
    //of all the playlists already instantiated
    private Playlist(String name) {
            this.songList = new ArrayList<Song>();
            this.name = name;
            dbHandler.addPlaylist(this);
    }

    public static Playlist instantiatePlaylist(String name) {
        //check if playlist already exists
        if (allPlaylists.containsKey(name)) {
            return allPlaylists.get(name);
        } else {
            Playlist newPlaylist = new Playlist(name);
            allPlaylists.put(name, newPlaylist);
            return newPlaylist;
        }
    }

    public boolean addSongList(ArrayList<Song> songs) {
        boolean success = true;
        for(Song newSong: songs) {
            if (success) {
                success = success && this.addSong(newSong);
            }
        }
        return success;
    }

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
            System.out.print("[Playlist] Song not added. Already in the playlist.\t");
            return false;
        } else {
            //add song to the database
            success = success && dbHandler.addSongToPlaylist(this, song);
            this.songList.add(song);
            System.out.print("[Library] Added a new song.\t");
        }
        System.out.println("'"+song.getTitleAndArtist()+"'\n");
        return success;
    }

    public String getName() {
        return this.name;
    }


    private boolean songInLibrary(Song song){
        for (Song currSong : dbHandler.getSongLibrary()) {
            if (currSong.getPath().equals(song.getPath())) {
                return true;
            }
        }
        return false;
    }

    private boolean songInPlaylist(Song song){
        for (Song currSong : dbHandler.getSongsInPlaylist(this)) {
            if (currSong.getPath().equals(song.getPath())) {
                return true;
            }
        }
        return false;
    }

    private static HashMap<String, Playlist> getPlaylistMapFromDB() {
        if (allPlaylists == null)
            allPlaylists = new HashMap<String, Playlist>();
        //TODO: maybe have a DB method that returns a list of Playlist objects instead of Strings
        for (String playlistName : dbHandler.getAllPlaylists()) {
            Playlist newPlaylist = new Playlist(playlistName);
            newPlaylist.songList = dbHandler.getSongsInPlaylist(newPlaylist);
            allPlaylists.put(playlistName, newPlaylist);
        }
        return allPlaylists;
    }

    public static void setLibrary(SongLibrary plibrary) {
        library = plibrary;
    }
}
