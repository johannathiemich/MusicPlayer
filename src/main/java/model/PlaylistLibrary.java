package model;

import database.DatabaseHandler;

import java.util.ArrayList;

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
     * This might be removed later...
     */
    public PlaylistLibrary(ArrayList<Playlist> playlistList){
        this.dbHandler = DatabaseHandler.getInstance();
        this.addAll(playlistList);
    }

    public Playlist getPlaylistByName(String pName) {
        for (Playlist playlist: this) {
            if (playlist.getName().equals(pName)) {
                return playlist;
            }
        }
        return null;
    }

    public ArrayList<String> getAllPlaylistNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (Playlist playlist: this) {
            names.add(playlist.getName());
        }
        return names;
    }

    public String[] getAllPlaylistNamesArray() {
        String[] names = new String[this.size()];
        for(int i = 0; i < this.size(); i++) {
            names[i] = this.get(i).getName();
        }
        return names;
    }



    public boolean addPlaylist(Playlist playlist){
        boolean success = false;
        if(playlist == null) {
            System.out.println("[Library_ERROR] Not added. playlist: null\n");
            success = false;
        } else {
            // Check if the playlist already exists in the library
            if ( getPlaylistByName(playlist.getName()) != null ) {
                System.out.print("[Library] Not added. Already in the playlist library.\t");
                success = false;
            } else {
                //add playlist to the database
                dbHandler.addPlaylist(playlist);
                this.add(playlist);
                System.out.print("[Library] Added a new song.\t");
                success = true;
            }
            System.out.println("'"+playlist.getName()+"'\n");
        }
        return success;
    }

    public void deletePlaylist(Playlist playlist){
        //Check if the song is in the library before deleteSong()
        if( getPlaylistByName(playlist.getName()) != null) {
            dbHandler.deletePlaylist(playlist);
            this.remove(playlist);
            System.out.print("[Library] Deleted.\t");
        }else{
            System.out.print("[Library] Song does not exist in library.\t");
        }
        System.out.println("'"+playlist.getName()+"'\n");
    }


}
