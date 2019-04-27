package model;

import com.mpatric.mp3agic.*;

import java.io.File;
import java.io.IOException;

public class Song {

    private String path;
    private String title;
    private String artist;
    private String album;
    private String year;
    private String comment;
    private String genre;
    private int time;

    /**
     * Construct an empty Song object
     */
    public Song(int number){
        this.path = "randomPath" + number;
    }

    /**
     * Construct a Song object from properties
     * @param pTime integer number in second
     */
    public Song(String pPath, String pTitle, String pArtist, String pAlbum,
                String pYear, String pComment, String pGenre, int pTime) {
        setProperties(pPath, pTitle, pArtist, pAlbum, pYear, pComment, pGenre, pTime);
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
            time = (int)mp3file.getLengthInSeconds();

            //more info with ID3V1/ID3V2 tags
            if (mp3file.hasId3v1Tag()) {
                System.out.println("\tMP3tag:Id3v1 ");
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                setProperties( filePath,
                        id3v1Tag.getTitle(), id3v1Tag.getArtist(), id3v1Tag.getAlbum(),
                        id3v1Tag.getYear(),id3v1Tag.getComment(), id3v1Tag.getGenreDescription(),
                        time );
            } else if (mp3file.hasId3v2Tag()) {
                System.out.println("\tMP3tag:Id3v2 ");
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                setProperties( filePath,
                        id3v2Tag.getTitle(), id3v2Tag.getArtist(), id3v2Tag.getAlbum(),
                        id3v2Tag.getYear(),id3v2Tag.getComment(), id3v2Tag.getGenreDescription(),
                        time );
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

    public String[] toArrayNoPath() {
        String[] properties = new String[6];
        properties[0] = this.getTitle();
        properties[1] = this.getArtist();
        properties[2] = this.getAlbum();
        properties[3] = this.getYear();
        properties[4] = this.getComment();
        properties[5] = this.getGenre();
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
    public int getTime() { return time; }

    /**
     * Get title and artist of the song in 'title - artist' form
     * @return
     */
    public String getTitleAndArtist() {
        return title + " - " + artist;
    }

    /**
     * Get a string of filename with extension from file path
     * @return String of the filename in from of "~.mp3"
     */
    public String getFileName() {
        return path.substring(path.lastIndexOf(File.separatorChar)+1);
    }

    /**
     * Set properties of the song.
     * Title, Artist, Album fields with null or blank String are replaced to "unknown".
     */
    public void setProperties(String pPath, String pTitle, String pArtist, String pAlbum,
                              String pYear, String pComment, String pGenre, int pTime) {
        this.path = pPath;
        this.title = (pTitle==null || pTitle.equals("")) ? "unknown" : pTitle;
        this.artist = (pArtist==null || pArtist.equals("")) ? "unknown" : pArtist;
        this.album = (pAlbum==null || pAlbum.equals("")) ? "unknown" : pAlbum;
        this.year = (pYear==null) ? "" : pYear;
        this.comment = (pComment==null) ? "" : pComment;
        this.genre = (pGenre==null) ? "" : pGenre;
        this.time = pTime;
    }

    @Override
    public boolean equals(java.lang.Object song1) {
        Song song = (Song) song1;
        return this.getPath().equals(song.getPath());
    }

}
