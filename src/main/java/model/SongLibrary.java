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
        System.out.print("[AddSongToLibrary] ");
        if(song.getPath()!=null) {
            boolean songContained = false;
            for (Song currSong : this) {
                if (currSong.getPath().equals(song.getPath())) {
                    songContained = true;
                }
            }
            if (!songContained) {
                //add song to the database and check if succeeded
                boolean addSongSucceed = dbHandler.addSong(song);
                if (addSongSucceed) {
                    //add song to this library
                    this.add(song);
                    System.out.print("SUCCESS! ");
                } else {
                    System.out.print("Not added. Already exists. ");
                }
            }
            System.out.println(" '"+song.getPath()+"'");
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
        System.out.print("[DeleteSongFromLibrary] ");
        boolean songContained = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getPath().equals(song.getPath())) {
                songContained = true;
            }
        }
        if(songContained) {
            this.remove(song);
            this.dbHandler.deleteSong(song);
            System.out.print("Deleted.");
        }else{
            System.out.print("Song does not exist in library.");
        }
        System.out.println(" '"+song.getTitleAndArtist()+"'");
    }


}
