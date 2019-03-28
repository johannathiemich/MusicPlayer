package model;

import database.DatabaseHandler;

import java.util.ArrayList;

public class Playlist {

    //TODO list of strings (key is path)
    private ArrayList<Song> songList;
    private String name;
    private DatabaseHandler dbHandler;
    private static ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();

    public Playlist(String name) {
        if(!playlistExists(name)) {
            this.dbHandler = DatabaseHandler.getInstance();
            this.songList = new ArrayList<Song>();
            this.name = name;
            allPlaylists.add(this);
            this.dbHandler.addPlaylist(this);
        }
    }

    public Playlist(String name, ArrayList<Song> songs) {
        if(!playlistExists(name)) {
            this.dbHandler = DatabaseHandler.getInstance();
            this.name = name;
            this.songList = songs;
            allPlaylists.add(this);
            this.dbHandler.addPlaylist(this);
        }
    }

    public boolean addSong(Song song) {
        //TODO: check song is in library
        boolean success = false;
        if(song.getPath() == null) {
            System.out.println("[Playlist_ERROR] Not added. filePath: null\n");
            success = false;
        } else if (songInLibrary(song)){
            // Check if the song already exists in the playlist
            if ( songInPlaylist(song) ) {
                System.out.print("[Playlist] Not added. Already in the playlist.\t");
                success = false;
            }else{
                //add song to the database
                dbHandler.addSongToPlaylist(this, song);
                this.songList.add(song);
                System.out.print("[Library] Added a new song.\t");
                success = true;
            }
            System.out.println("'"+song.getTitleAndArtist()+"'\n");
        } else {
            System.out.println("Song is not in library yet. Add it to the library first");
            success = false;
        }
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



    private boolean playlistExists(String name){
        for (String currPlaylist : dbHandler.getAllPlaylists()) {
            if (currPlaylist.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
