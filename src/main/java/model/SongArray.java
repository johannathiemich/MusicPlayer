package model;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongArray extends ArrayList<Song> {
    String type;    //library or playlist

    /**
     * Constructs Array of Songs
     * @param type "library" or "playlist"
     */
    public SongArray(String type){
        this.type = type;
    }

    /**
     * Gets the type of the SongArray
     * @return "library" or "playlist"
     */
    public String getType() { return type; }

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

//    /**
//     * Sort this library on Title
//     * @param order SORT_ASCENDING for A-Z, SORT_DESCENDING for Z-A
//     */
//    public void sortByTitle(int order) {
//        if(order != SORT_ASCENDING && order != SORT_DESCENDING ){
//            System.out.println("sortByColumnName() improper parameter: order "+order);
//        } else {
//            Collections.sort(this, Comparator.comparing(song -> song.getTitle().toLowerCase()));
//            System.out.print(this.type+", " + this.size() + " songs sorted");
//            if (order == SORT_DESCENDING) {
//                Collections.reverse(this);
//                System.out.print(" in descending order.");
//            } else {
//                System.out.print(" in ascending order.");
//            }
//            System.out.println();
//        }
//    }

    /**
     * Sorts the SongArray by the column name
     * @param order SORT_ASCENDING or SORT_DESCENDING
     * @param columnName "Title", "Artist", "Album", "Year", "Comment", or "Genre"
     */
    public void sortByColumn(SortOrder order, String columnName) {
        if(order != SortOrder.ASCENDING && order != SortOrder.DESCENDING ){
            System.out.println("sortByColumnName() improper parameter: order "+order);
        }
        switch (columnName) {
            case "Title":
                //TODO this still doesn't match exactly to the table view sort.
                // 'Carry On' should come first than 'Car Wash'.
                Collections.sort(this, Comparator.comparing(song -> song.getTitle().toLowerCase()));
                break;
            case "Artist":
                Collections.sort(this, Comparator.comparing(song -> song.getArtist().toLowerCase()));
                break;
            case "Album":
                Collections.sort(this, Comparator.comparing(song -> song.getAlbum().toLowerCase()));
                break;
            case "Year":
                Collections.sort(this, Comparator.comparing(song -> song.getYear().toLowerCase()));
                break;
            case "Comment":
                Collections.sort(this, Comparator.comparing(song -> song.getComment().toLowerCase()));
                break;
            case "Genre":
                Collections.sort(this, Comparator.comparing(song -> song.getGenre().toLowerCase()));
                break;
            default:
                System.out.println("sortByColumnName() improper parameter: columnName "+columnName);
        }
        System.out.print(this.type+", "+this.size()+" songs sorted on '"+columnName+"'");
        if(order == SortOrder.DESCENDING) {
            Collections.reverse(this);
            System.out.print(" in descending order.");
        } else {
            System.out.print(" in ascending order.");
        }
        System.out.println();
    }
}
