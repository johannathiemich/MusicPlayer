public class Song {

    private String path;
    private String title;
    private String artist;
    private String album;
    private int year;
    private String comment;
    private String genre;

    public Song(String pPath, String pTitle, String pArtist, String pAlbum, int pYear, String pComment,
                String pGenre) {
        this.path = pPath;
        this.title = pTitle;
        this.artist = pArtist;
        this.album = pAlbum;
        this.year = pYear;
        this.comment = pComment;
        this.genre = pGenre;
    }

    public String[] toArray() {
        String[] properties = new String[7];
        properties[0] = this.getPath();
        properties[1] = this.getTitle();
        properties[2] = this.getArtist();
        properties[3] = this.getAlbum();
        properties[4] = Integer.toString(this.getYear());
        properties[5] = this.getComment();
        properties[6] = this.getGenre();
        return properties;
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

    public int getYear() {
        return year;
    }

    public String getComment() {
        return comment;
    }

    public String getGenre() {
        return genre;
    }

}
