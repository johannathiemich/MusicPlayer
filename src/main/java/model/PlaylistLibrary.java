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
     * @param playlistName the playlist name to be added
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
     * @param playlist the playlist to be deleted
     */
    public void deletePlaylist(Playlist playlist){
        //Check if the song is in the library before deleteSong()
        if( getPlaylistByName(playlist.getName()) != null) {
            dbHandler.deletePlaylist(getPlaylistByName(playlist.getName()));
            this.remove(this.getPlaylistByName(playlist.getName()));
            System.out.print("[PlaylistLibrary] Deleted.\t");
        }else{
            System.out.print("[PlaylistLibrary] Song does not exist in library.\t");
        }
        System.out.println("'"+playlist.getName()+"'\n");
    }


}
