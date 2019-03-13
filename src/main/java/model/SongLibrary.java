package model;

import java.util.ArrayList;

public class SongLibrary extends ArrayList<Song>{

    private DatabaseHandler dbHandler;
    /**
     * Construct an empty library
     */
    public SongLibrary(){
        this.dbHandler = new DatabaseHandler();
        this.addAll(dbHandler.getSongLibrary());
    }

    /**
     * Construct a library from an array of songs
     * @param songArray
     */
    public SongLibrary(ArrayList<Song> songArray){
        this.addAll(songArray);
        this.dbHandler = new DatabaseHandler();
    }

    /**
     * Add a Song to the library, if not present.
     * @param song to be added to the list
     */
    public void addSong(Song song){
        if(song.getPath()!=null) {
            System.out.print("Song '" + song.getTitle() + "'");
            //TODO modify comparison (compare path instead) @sellabae
            if (this.contains(song)) {
                System.out.println(" already exists in library");
            } else {
                this.add(song);
                this.dbHandler.addSong(song);
                System.out.println(" is added to library");
            }
        }
    }

    /**
     * Add multiple songs to the library, if not present.
     * @param songArray songs to be added to the list
     */
    public void addMutipleSongs(ArrayList<Song> songArray){
        for (Song song : songArray) {
            addSong(song);
        }
    }

    /**
     * Delete a song from the library, if present.
     * @param song to be removed from the list
     */
    public void deleteSong(Song song){
        System.out.print("The song '"+song.getTitle()+"'");
        if(this.contains(song)) {
            this.remove(song);
            this.dbHandler.deleteSong(song);
            System.out.println(" is deleted from library");
        }else{
            System.out.println(" does not exist in library");
        }
    }


}
