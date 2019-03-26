package model;

import database.DatabaseHandler;

import java.util.ArrayList;

public class Playlist {

    private ArrayList<Song> songList;
    private String name;
    private DatabaseHandler dbHandler;
    private static ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();

    public Playlist(String name) {
        if (!exists(name)) {
            this.dbHandler = DatabaseHandler.getInstance();
            this.songList = new ArrayList<Song>();
            this.name = name;
            allPlaylists.add(this);
            this.dbHandler.addPlaylist(this);
        }
    }

    public Playlist(String name, ArrayList<Song> songs) {
        if (!exists(name)) {
            this.dbHandler = DatabaseHandler.getInstance();
            this.name = name;
            this.songList = songs;
            allPlaylists.add(this);
            this.dbHandler.addPlaylist(this);
        }
    }

    public boolean addSong(Song song) {
        boolean success = false;
        if(song.getPath() == null) {
            System.out.println("[Playlist_ERROR] Not added. filePath: null\n");
            success = false;
        } else {
            // Check if the song already exists in the playlist
            if ( exists(song) ) {
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
        }
        return success;
    }

    public String getName() {
        return this.name;
    }

    private boolean exists(Song song){
        //TODO would be better to call a dbHandler method to check database by sql? and pass the result through this method.
        for (Song currSong : this.songList) {
            if (currSong.getPath().equals(song.getPath())) {
                return true;
            }
        }
        return false;
    }

    private boolean exists(String name){
        //TODO would be better to call a dbHandler method to check database by sql? and pass the result through this method.
        for (Playlist currPlaylist : allPlaylists) {
            if (currPlaylist.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
