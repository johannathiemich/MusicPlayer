package model;

import database.DatabaseHandler;

import java.util.ArrayList;

//same with SongDAO(Data Access Object). same concept same work.

/**
 * Model class in the MVC pattern.
 * SongLibrary class manages all songs.
 */
public class SongLibrary extends SongArray{

    private DatabaseHandler dbHandler;

    public static final int     ADDSONG_FILEPATH_NULL = 0;
    public static final int     ADDSONG_SUCCESS = 1;
    public static final int     ADDSONG_ALREADY_EXIST = 2;

    /**
     * Construct an empty library
     */
    public SongLibrary(){
        super("library");
        this.dbHandler = DatabaseHandler.getInstance();
        this.addAll(dbHandler.getSongLibrary());
    }

    /**
     * Construct a library from an array of songs
     * This might be removed later...
     */
    public SongLibrary(ArrayList<Song> songArray){
        super("library");
        this.dbHandler = DatabaseHandler.getInstance();
        this.addAll(songArray);
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

}
