package model;

import database.DatabaseHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;

//same with SongDAO(Data Access Object). same concept same work.
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

    public String[] convertToString() {
        String[] resultString = new String[this.size()];

        for (int i = 0; i < this.size(); i++) {
            resultString[i] = this.get(i).getPath() + "     [" + this.get(i).getTitle()+ "]     [" +
                    this.get(i).getArtist() + "]";
        }
        return resultString;
    }

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
            boolean songContained = false;
            for (Song currSong : this) {
                if (currSong.getPath().equals(song.getPath())) {
                    songContained = true;
                }
            }
            if (!songContained) {
                //add song to the database
                boolean addSongSucceed = dbHandler.addSong(song);
                if (addSongSucceed) {
                    //add song to this library
                    this.add(song);
                    System.out.print("[addSong] success! ");
                } else {
                    System.out.print("[addSong] not added to db (already exist or fail) ");
                }
            }
            System.out.println(" FilePath: "+song.getPath());
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
        boolean songContained = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getPath().equals(song.getPath())) {
                songContained = true;
            }
        }
        if(songContained) {
            this.remove(song);
            this.dbHandler.deleteSong(song);
            System.out.println(" is deleted from library");
        }else{
            System.out.println(" does not exist in library");
        }
    }


}
