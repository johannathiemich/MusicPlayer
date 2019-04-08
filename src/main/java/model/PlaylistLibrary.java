package model;

import database.DatabaseHandler;

import java.util.ArrayList;

/**
 * PlaylistLibrary class manages all Playlists.
 */
public class PlaylistLibrary extends ArrayList<Playlist> {

    private DatabaseHandler dbHandler;
    /**
     * Construct an empty library
     */
    public PlaylistLibrary(){
        this.dbHandler = DatabaseHandler.getInstance();
        this.addAll(dbHandler.getAllPlaylistsObjects());
    }

    /**
     * Construct a library from an array of songs
     */
    public PlaylistLibrary(ArrayList<Playlist> playlistList){
        this.dbHandler = DatabaseHandler.getInstance();
        this.addAll(playlistList);
    }

    /**
     * Checks if the name of a playlist exists in playlist library
     * @param playlistName the playlist name to check (not case sensitive)
     * @return true if exists, false if not.
     */
    public boolean exists(String playlistName){
        for (Playlist playlist : this) {
            if (playlist.getName().equalsIgnoreCase(playlistName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns a playlist object by its name
     * @param pName the name of the playlist to be returned
     * @return the playlist with the name pName
     */
    public Playlist getPlaylistByName(String pName) {
        if ( this.exists(pName) ) {
            ArrayList<Song> songs = dbHandler.getSongsInPlaylist(pName);
            return new Playlist(pName, songs);
        } else {
            return null;
        }
    }

    /**
     * This method returns a list of names of all the playlists existing
     * @return all the names of all the playlists existing
     */
    public ArrayList<String> getAllPlaylistNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (Playlist playlist: this) {
            names.add(playlist.getName());
        }
        return names;
    }

    /**
     * This method adds a new playlist to the list of all playlists
     * @param playlistName the name of a playlist to be added
     * @return true if the playlist is added, false if not (e.g. the playlist already exists)
     */
    public boolean addPlaylist(String playlistName){
        //Check if the parameter is null
        if(playlistName == null) {
            System.out.println("[PlaylistLibrary_ERROR] Not added. playlist: null\n");
            return false;
        }
        // Check if the playlist already exists in the library
        if ( this.exists(playlistName) ) {
            System.out.print("[PlaylistLibrary] Not added. \""+playlistName+"\" already in the playlist library.\n");
            return false;
        }

        // Add playlist to the database
        if( !dbHandler.addPlaylist(playlistName) ) { return false; }
        // Add playlist to this playlist library
        this.add(new Playlist(playlistName));
        System.out.print("[PlaylistLibrary] Added a new playlist \"" + playlistName + "\".\n");

        return true;
    }

    /**
     * This method removes a playlist from the list of all existing playlists
     * @param playlistName the name of the playlist to be deleted
     * @return true if the playlist is deleted, false if not (e.g. a non existing playlist name)
     */
    public boolean deletePlaylist(String playlistName){
        //Check if the parameter is null
        if(playlistName == null) {
            System.out.println("[PlaylistLibrary_ERROR] Not deleted. playlist: null\n");
            return false;
        }
        //Check if the playlist is in the library
        if( !this.exists(playlistName) ) {
            System.out.print("[PlaylistLibrary] There's no playlist named \""+playlistName+"\".\n");
            return false;
        }

        // Delete playlist from the database
        if ( !dbHandler.deletePlaylist(playlistName) ) { return false; }
        // Delete playlist from this playlist library
        this.remove(this.getPlaylistByName(playlistName));
        System.out.print("[PlaylistLibrary] Deleted the playlist \""+playlistName+"\".\n");

        return true;
    }


}
