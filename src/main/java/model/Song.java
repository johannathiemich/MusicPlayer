package model;

import com.mpatric.mp3agic.*;

import java.io.IOException;

public class Song {

    private String path;
    private String title;
    private String artist;
    private String album;
    private String year;
    private String comment;
    private String genre;

    /**
     * Construct an empty Song object
     */
    public Song(){
        setProperties("-","-","-","-","-","-","-");
    }

    /**
     * Construct a Song object from properties
     */
    public Song(String pPath, String pTitle, String pArtist, String pAlbum,
                String pYear, String pComment, String pGenre) {
        setProperties(pPath, pTitle, pArtist, pAlbum, pYear, pComment, pGenre);
    }

    /**
     * Construct a Song object from a valid MP3 File path
     * @param filePath
     */
    public Song(String filePath){
        try {
            Mp3File mp3file = new Mp3File(filePath);
            if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                setProperties(filePath,
                        id3v1Tag.getTitle(), id3v1Tag.getArtist(), id3v1Tag.getAlbum(),
                        id3v1Tag.getYear(),id3v1Tag.getComment(), id3v1Tag.getGenreDescription() );
            } else if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                setProperties(filePath,
                        id3v2Tag.getTitle(), id3v2Tag.getArtist(), id3v2Tag.getAlbum(),
                        id3v2Tag.getYear(),id3v2Tag.getComment(), id3v2Tag.getGenreDescription() );
            }
        } catch (IOException e) {
            System.err.println("[ERROR] File Not Found. filePath='"+filePath+"'");
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transform the Song object to an array of strings
     * @return string array of song properties
     */
    //getPropertiesInArray
    public String[] toArray() {
        String[] properties = new String[7];
        properties[0] = this.getPath();
        properties[1] = this.getTitle();
        properties[2] = this.getArtist();
        properties[3] = this.getAlbum();
        properties[4] = this.getYear();
        properties[5] = this.getComment();
        properties[6] = this.getGenre();
        return properties;
    }


    //getters and setters
    public String getPath() {
        return path;
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getAlbum() {
        return album;
    }
    public String getYear() {
        return year;
    }
    public String getComment() {
        return comment;
    }
    public String getGenre() {
        return genre;
    }
    public String getTitleAndArtist() { return title + " - " + artist; }


    public void setProperties(String pPath, String pTitle, String pArtist, String pAlbum, String pYear, String pComment,
                              String pGenre){
        this.path = pPath;
        this.title = (pTitle==null) ? "unknown" : pTitle;
        this.artist = (pArtist==null) ? "unknown" : pArtist;
        this.album = (pAlbum==null) ? "unknown" : pAlbum;
        this.year = (pYear==null) ? "" : pYear;
        this.comment = (pComment==null) ? "" : pComment;
        this.genre = (pGenre==null) ? "" : pGenre;
    }

}
