package model;

import database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//same with SongDAO(Data Access Object). same concept same work.

/**
 * Model class in the MVC pattern.
 * SongLibrary class manages all songs.
 */
public class SongLibrary extends ArrayList<Song>{

    private DatabaseHandler dbHandler;

    public static final int     ADDSONG_FILEPATH_NULL = 0;
    public static final int     ADDSONG_SUCCESS = 1;
    public static final int     ADDSONG_ALREADY_EXIST = 2;

    public static final int     SORT_ASCENDING = 1;
    public static final int     SORT_DESCENDING = 0;

    /**
     * Construct an empty library
     */
    public SongLibrary(){
        this.dbHandler = DatabaseHandler.getInstance();
        this.addAll(dbHandler.getSongLibrary());
    }

    /**
     * Construct a library from an array of songs
     * This might be removed later...
     */
    public SongLibrary(ArrayList<Song> songArray){
        this.dbHandler = DatabaseHandler.getInstance();
        this.addAll(songArray);
    }

    /**
     * Return Song in the library by its file path.
     * If the song with such file path doesn't exist, returns null.
     * @param path the path of the song to be selected
     * @return the song at the corresponding path or null if the path is not contained in the library
     */
    public Song getSongByPath(String path) {
        for (Song song : this) {
            if (song.getPath().equals(path)) {
                return song;
            }
        }
        return null;
    }

    /**
     * Add a Song to the library, if not present.
     * Should use addSong(Song) instead of add(Song) which is Array's inherited method.
     * @param song to be added to the list
     * @return return code (ADDSONG_FILEPATH_NULL = 0, ADDSONG_SUCCESS = 1, ADDSONG_SONG_EXIST = 2)
     */
    public int addSong(Song song){
        if(song.getPath() == null) {
            System.out.println("[Library_ERROR] Not added. filePath: null\n");
            return ADDSONG_FILEPATH_NULL;
        } else {
            // Check if the song already exists in the library
            if ( exists(song) ) {
                System.out.println("[Library] Not added. Already in the library.\t"+song.getTitleAndArtist());
                return ADDSONG_ALREADY_EXIST;
            }else{
                //add song to the database
                dbHandler.addSong(song);
                this.add(song);
                System.out.println("[Library] Added a new song.\t"+song.getTitleAndArtist());
                return ADDSONG_SUCCESS;
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
     * @param song the song to be removed from the list
     */
    public void deleteSong(Song song){
        //Check if the song is in the library before deleteSong()
        if( exists(song) ) {
            dbHandler.deleteSong(song);
            this.remove(song);
            System.out.print("[Library] Deleted.\t");
        }else{
            System.out.print("[Library] Song does not exist in library.\t");
        }
        System.out.println("'"+song.getTitleAndArtist()+"'\n");
    }

    /**
     * Check if the Song already exists in the library
     * by comparing the filePath of the song as a key
     * @param song to be checked.
     * @return whether the song is in the library.
     */
    private boolean exists(Song song){
        //TODO would be better to call a dbHandler method to check database by sql? and pass the result through this method.
        for (Song currSong : this) {
            if (currSong.getPath().equals(song.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sort this library on Title
     * @param order SORT_ASCENDING for A-Z, SORT_DESCENDING for Z-A
     */
    public void sortByTitle(int order) {
        if(order != SORT_ASCENDING && order != SORT_DESCENDING ){
            System.out.println("sortByColumnName() improper parameter: order "+order);
        } else {
            //TODO this still doesn't match exactly to the table view sort.
            // 'Carry On' should come first than 'Car Wash'.
            Collections.sort(this, Comparator.comparing(song -> song.getTitle().toLowerCase()));
            System.out.print("library, " + this.size() + " songs sorted");
            if (order == SORT_DESCENDING) {
                Collections.reverseOrder();
                System.out.print(" in descending order.");
            } else {
                System.out.print(" in ascending order.");
            }
            System.out.println();
        }
    }

}
