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
     * This method returns a playlist object by its name
     * @param pName the name of the playlist to be returned
     * @return the playlist with the name pName
     */
    public Playlist getPlaylistByName(String pName) {
        boolean playlistExists = false;
        for (Playlist playlist: this) {
            if (playlist.getName().equals(pName)) {
                playlistExists = true;
            }
        }
        if (playlistExists) {
            ArrayList<Song> playList = dbHandler.getSongsInPlaylist(pName);
            return new Playlist(pName, playList);
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
     * @param playlist the playlist to be added
     * @return true if the playlist could be added, false if not (e.g. the playlist already exists)
     */
    public boolean addPlaylist(Playlist playlist){
        boolean success = false;
        if(playlist == null) {
            System.out.println("[PlaylistLibrary_ERROR] Not added. playlist: null\n");
            success = false;
        } else {
            // Check if the playlist already exists in the library
            if ( getPlaylistByName(playlist.getName()) != null ) {
                System.out.print("[PlaylistLibrary] Not added. Already in the playlist library.\t");
                success = false;
            } else {
                //add playlist to the database
                dbHandler.addPlaylist(playlist);
                this.add(playlist);
                System.out.print("[PlaylistLibrary] Added a new song.\t");
                success = true;
            }
            System.out.println("'"+playlist.getName()+"'\n");
        }
        return success;
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
