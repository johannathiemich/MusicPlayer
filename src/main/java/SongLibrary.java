import java.util.ArrayList;

public class SongLibrary {
    private ArrayList<Song> songList;
    //or just make SongLibrary extends ArrayList<Song>

    /**
     * Construct an empty library
     */
    public SongLibrary(){
        //initialize songArrayList
    }

    /**
     * Construct a library from an array of songs
     * @param songArray
     */
    public SongLibrary(ArrayList<Song> songArray){
        songList = songArray;
    }

    /**
     * Add a Song to the library, if not present.
     * @param song to be added to the list
     */
    public void addSong(Song song){
        if(songList.contains(song)){
            System.out.println("The song '"+song.getTitle()+"' already exist in the library");
        }else{
            songList.add(song);
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
        if(songList.contains(song)) {
            songList.remove(song);
        }else{
            System.out.println("The song '"+song.getTitle()+"' does not exist in the library");
        }
    }

}
