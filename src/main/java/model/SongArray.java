package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongArray extends ArrayList<Song> {
    String type;    //library or playlist

    public static final int     SORT_ASCENDING = 1;
    public static final int     SORT_DESCENDING = 0;

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
     * Sorts the SongArray by the column name
     * @param order SORT_ASCENDING or SORT_DESCENDING
     * @param columnName "Title", "Artist", "Album", "Year", "Comment", or "Genre"
     */
    public void sortByColumn(int order, String columnName) {
        if(order != SORT_ASCENDING && order != SORT_DESCENDING ){
            System.out.println("sortByColumnName() improper parameter: order "+order);
        }
        switch (columnName) {
            case "Title":
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
        System.out.print(type+", "+this.size()+" songs sorted on '"+columnName+"'");
        if(order == SORT_DESCENDING) {
            Collections.reverseOrder();
            System.out.print(" in descending order.");
        }
        System.out.println();
    }
}
