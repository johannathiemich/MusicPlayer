package model;

import database.DatabaseHandler;

import javax.xml.crypto.Data;
import java.util.ArrayList;

//same with SongDAO(Data Access Object). same concept same work.

/**
 * Model class in the MVC pattern. For managing the list of songs that is shown in the application ("song library").
 */
public class SongLibrary extends ArrayList<Song>{

    private DatabaseHandler dbHandler;
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
     * This method converts the song library to a list of string with each string containing the song path, title and
     * artist. This method is needed for the ListDialgo class in order to choose a song to be deleted.
     * @return the song library as a list of strings
     */
    public String[] convertToString() {
        String[] resultString = new String[this.size()];

        for (int i = 0; i < this.size(); i++) {
            resultString[i] = this.get(i).getPath() + "     [" + this.get(i).getTitle()+ " - " +
                    this.get(i).getArtist() + "]";
        }
        return resultString;
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
     * Return the index number of the song in the library.
     * If the song doesn't exist, returns -1.
     * @param song to find index number in the library
     * @return index of the song
     */
    public int getIndex(Song song) {
        for (int i = 0; i < this.size(); i++) {
            String comparedPath = this.get(i).getPath();
            if (comparedPath.equals(song.getPath())) {
                return i;   //index of the song in the library
            }
        }
        return -1;  //song not found in the library
    }

    /**
     * Add a Song to the library, if not present.
     * Should use addSong(Song) instead of add(Song) which is Array's inherited method.
     * @param song to be added to the list
     * @return whether adding Song to library succeeds.
     */
    public boolean addSong(Song song){
        boolean success = false;
        if(song.getPath() == null) {
            System.out.println("[Library_ERROR] Not added. filePath: null\n");
            success = false;
        } else {
            // Check if the song already exists in the library
            if ( exists(song) ) {
                System.out.print("[Library] Not added. Already in the library.\t");
                success = false;
            }else{
                //add song to the database
                dbHandler.addSong(song);
                this.add(song);
                System.out.print("[Library] Added a new song.\t");
                success = true;
            }
            System.out.println("'"+song.getTitleAndArtist()+"'\n");
        }
        return success;
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
