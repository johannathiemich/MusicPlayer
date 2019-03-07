public class Song {

    private String path;
    private String title;
    private String artist;
    private String album;
    private String year;
    private String comment;
    private String genre;

    public Song(String pPath, String pTitle, String pArtist, String pAlbum, String pYear, String pComment,
                String pGenre) {
        this.path = pPath;
        this.title = pTitle;
        this.artist = pArtist;
        this.album = pAlbum;
        this.year = pYear;
        this.comment = pComment;
        this.genre = pGenre;
    }


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

}
