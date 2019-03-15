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
    private int lengthInSecond;

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
            //Get MP3File
            System.out.print("[NewSong] '"+filePath+"' ");
            Mp3File mp3file = new Mp3File(filePath);

            //Fetching mp3file info
            lengthInSecond = (int)mp3file.getLengthInSeconds();
            System.out.print("\tlength: "+lengthInSecond+"sec ");
            if (mp3file.hasId3v1Tag()) {
                System.out.println("[MP3tag]Id3v1");
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                setProperties(filePath,
                        id3v1Tag.getTitle(), id3v1Tag.getArtist(), id3v1Tag.getAlbum(),
                        id3v1Tag.getYear(),id3v1Tag.getComment(), id3v1Tag.getGenreDescription() );
            } else if (mp3file.hasId3v2Tag()) {
                System.out.println("[MP3tag]Id3v2");
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                setProperties(filePath,
                        id3v2Tag.getTitle(), id3v2Tag.getArtist(), id3v2Tag.getAlbum(),
                        id3v2Tag.getYear(),id3v2Tag.getComment(), id3v2Tag.getGenreDescription() );
            }
        } catch (IOException e) {
            System.err.println("[ERROR] File Not Found. filePath='"+filePath+"'");
            //e.printStackTrace();
        } catch (UnsupportedTagException e) {
            System.out.println("[ERROR] Unsupported Tag");
            //e.printStackTrace();
        } catch (InvalidDataException e) {
            System.out.println("[ERROR] Invalid Data");
            //JOptionPane.showMessageDialog(null, "The selected file is not a valid mp3 file.");
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
    public int getLengthInSecond() { return lengthInSecond; }
    public String getTitleAndArtist() { return title + " - " + artist; }


    /**
     * Set properties of the song.
     * Title, Artist, Album fields with null or blank String are replaced to "unknown".
     */
    public void setProperties(String pPath, String pTitle, String pArtist, String pAlbum, String pYear, String pComment,
                              String pGenre){
        this.path = pPath;
        this.title = (pTitle==null || pTitle=="") ? "unknown" : pTitle;
        this.artist = (pArtist==null || pTitle=="") ? "unknown" : pArtist;
        this.album = (pAlbum==null || pTitle=="") ? "unknown" : pAlbum;
        this.year = (pYear==null) ? "" : pYear;
        this.comment = (pComment==null) ? "" : pComment;
        this.genre = (pGenre==null) ? "" : pGenre;
    }

}
