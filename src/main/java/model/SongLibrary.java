package model;

import database.DatabaseHandler;

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
        this.dbHandler = new DatabaseHandler();
        this.addAll(dbHandler.getSongLibrary());
    }

    /**
     * Construct a library from an array of songs
     * This might be removed later...
     */
    public SongLibrary(ArrayList<Song> songArray){
        this.dbHandler = new DatabaseHandler();
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
     * This method finds a song by its path
     * @param path the path of the song to be selected
     * @return the song at the corresponding path or null if the path is not contained in the library
     */
    public Song getSongByPath(String path) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getPath().equals(path)) {
                return this.get(i);
            }
        }
        return null;
    }

    /**
     * Add a Song to the library, if not present.
     * Should use addSong(Song) instead of add(Song) which is Array's inherited method.
     * @param song to be added to the list
     */
    public void addSong(Song song){
        if(song.getPath()!=null) {
            // Check if the song already exists in the library
            if ( exists(song) ) {
                System.out.print("[Library] Not added. Already in the library.\t");
            }else{
                //add song to the database
                dbHandler.addSong(song);
                this.add(song);
                System.out.print("[Library] Added a new song.\t");
            }
            System.out.println("'"+song.getTitleAndArtist()+"'\n");
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
     * Check the Song already exists in the library
     * by comparing the filePath of the song as a key
     * @param song to be checked.
     * @return whether the song is in the library.
     */
    public boolean exists(Song song){
        //TODO would be better to call a dbHandler method to check database by sql? and pass the result through this method.
        for (Song currSong : this) {
            if (currSong.getPath().equals(song.getPath())) {
                return true;
            }
        }
        return false;
    }

}
