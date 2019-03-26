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
    private int duration;

    /**
     * Construct an empty Song object
     */
    public Song(){
        this.path = "randomPath";
    }

    /**
     * Construct a Song object from properties
     * @param pDuration integer number in second
     */
    public Song(String pPath, String pTitle, String pArtist, String pAlbum,
                String pYear, String pComment, String pGenre, int pDuration) {
        setProperties(pPath, pTitle, pArtist, pAlbum, pYear, pComment, pGenre, pDuration);
    }

    /**
     * Construct a Song object from a valid MP3 File path
     * @param filePath of an MP3 file
     */
    public Song(String filePath){
        System.out.print("[Song] new filePath: '"+filePath+"' ");

        try {
            //Get MP3File
            Mp3File mp3file = new Mp3File(filePath);

            //Fetching mp3file info
            duration = (int)mp3file.getLengthInSeconds();

            //more info with ID3V1/ID3V2 tags
            if (mp3file.hasId3v1Tag()) {
                System.out.println("\tMP3tag:Id3v1 ");
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                setProperties( filePath,
                        id3v1Tag.getTitle(), id3v1Tag.getArtist(), id3v1Tag.getAlbum(),
                        id3v1Tag.getYear(),id3v1Tag.getComment(), id3v1Tag.getGenreDescription(),
                        duration );
            } else if (mp3file.hasId3v2Tag()) {
                System.out.println("\tMP3tag:Id3v2 ");
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                setProperties( filePath,
                        id3v2Tag.getTitle(), id3v2Tag.getArtist(), id3v2Tag.getAlbum(),
                        id3v2Tag.getYear(),id3v2Tag.getComment(), id3v2Tag.getGenreDescription(),
                        duration );
            }
        } catch (IOException e) {
            System.err.println("[Song_ERROR] File Not Found. filePath='"+filePath+"'");
        } catch (UnsupportedTagException e) {
            System.out.println("[Song_ERROR] Unsupported Tag");
        } catch (InvalidDataException e) {
            System.out.println("[Song_ERROR] Invalid Data. Not MP3 file.");
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


    // Getters
    public String getPath() { return path; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getYear() { return year; }
    public String getComment() { return comment; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }

    /**
     * Get song duration in '0:00' form
     * @return
     */
    public String getDurationMinSec() {
        int min = duration / 60;
        int sec = duration % 60;
        String minSec;
        if (sec < 10) {
            minSec = min + ":0" + sec;
        } else {
            minSec = min + ":" + sec;
        }
        return minSec;
    }

    /**
     * Get title and artist of the song in 'title - artist' form
     * @return
     */
    public String getTitleAndArtist() {
        return title + " - " + artist;
    }


    /**
     * Set properties of the song.
     * Title, Artist, Album fields with null or blank String are replaced to "unknown".
     */
    public void setProperties(String pPath, String pTitle, String pArtist, String pAlbum,
                              String pYear, String pComment, String pGenre, int pDuration) {
        this.path = pPath;
        this.title = (pTitle==null || pTitle.equals("")) ? "unknown" : pTitle;
        this.artist = (pArtist==null || pArtist.equals("")) ? "unknown" : pArtist;
        this.album = (pAlbum==null || pAlbum.equals("")) ? "unknown" : pAlbum;
        this.year = (pYear==null) ? "" : pYear;
        this.comment = (pComment==null) ? "" : pComment;
        this.genre = (pGenre==null) ? "" : pGenre;
        this.duration = pDuration;
    }

}
