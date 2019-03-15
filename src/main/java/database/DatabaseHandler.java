package database;

import model.Song;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class is responsible for accessing and updating the database when songs are added / deleted, etc.
 * It represents the model in the Model - View - Controller  Pattern. It is updated by the controller and provides
 * the data displayed by the view.
 */
public class DatabaseHandler {

    // Create a named constant for the URL.
    // NOTE: This value is specific for Java DB.
    private final String createDatabaseURL = "jdbc:derby:SongsDB;create=true";
    private final String shutdownURL = "jdbc:derby:;shutdown=true";
    private final String tableName = "SONGS";

    /**
     * Constructor for this class
     */
    public DatabaseHandler() {
        //we should not drop all tables every time we start the application
        //dropAllTables();
        createSongTable();
    }

    /**
     * This method creates the table that holds the songs in the database. If the table already exists, it won't
     * create a new one. The ID for each row is just the absolute path to the mp3 file.
     * The table contains the following columns: SONG_PATH, TITLE; ARTIST, ALBUM, YEAR_PUBLISHED, COMMENT, GENRE
     */
    public void createSongTable(){
        Connection conn = null;
        Statement statement = null;
        String sql = "CREATE TABLE SONGS (SONG_PATH VARCHAR(512) PRIMARY KEY, TITLE VARCHAR(256), ARTIST VARCHAR(256), " +
                "ALBUM VARCHAR(256), YEAR_PUBLISHED VARCHAR(256), COMMENT VARCHAR(256), GENRE VARCHAR(256))";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            DriverManager.getConnection(shutdownURL);
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Table already exists, won't create a new one.");
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method adds a row containing a song to the songs database table.
     * @param song the song to be added, identified by its path
     * @return true if the insert was successful; false if it was not successful
     */
    public boolean addSong(Song song) {
        boolean success = false;
        Connection conn = null;
        Statement statement = null;
        String sql = "INSERT INTO SONGS " +
                "      VALUES ('"
                + song.getPath()    + "', '"
                + song.getTitle()   + "', '"
                + song.getArtist()  + "', '"
                + song.getAlbum()   + "', '"
                + song.getYear()    + "', '"
                //+ "comment "        + "', '"
                + song.getComment() + "', '"
                + song.getGenre()   + "'"
                + ")";
        System.out.println("[Database] sql executed: " + sql);
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
            System.out.println("[Database] Added song successfully.");
            success = true;
        } catch (SQLException e) {
            success = false;
            if (e.getSQLState().equals("23505")) {
                System.out.println("[Database] Song is already saved in the database.");
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("[Database] Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * This method deletes a song from the database table.
     * @param song the song to be deleted from the database; is identified by its absolute path
     * @return true if the song was deleted successfully; false if not
     */
    public boolean deleteSong(Song song) {
        boolean success = true;
        Connection conn = null;
        Statement statement = null;
        String sql = "DELETE FROM " + tableName +
                "      WHERE SONG_PATH = '" + song.getPath() + "'";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            conn.close();
            System.out.println("[Database] Deleted song.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("[Database] Derby shutdown normally.");
            } else {
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }

    /**
     * This method returns all the songs saved in the database
     * @return an ArrayList of songs
     */
    public ArrayList<Song> getSongLibrary() {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM " + tableName;
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while(results.next())
            {
                String file_path = results.getString(1);
                String title = results.getString(2);
                String artist = results.getString(3);
                String album = results.getString(4);
                String year = results.getString(5);
                String comment = results.getString(6);
                String genre = results.getString(7);
                Song song = new Song(file_path, title, artist, album, year, comment, genre);
                list.add(song);
            }
            results.close();
            conn.close();

        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * This method drops all tables currently contained in the database. This method is usefule for resetting
     * the database.
     */
    public void dropAllTables(){
        try {
            //Get connection and statement
            Connection conn = DriverManager.getConnection(createDatabaseURL);
            Statement stmt = conn.createStatement();

            try {
                // Drop the 'SONGS' table from DB
                stmt.execute("DROP TABLE "+tableName);
                System.out.println(tableName+" table dropped.");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}


