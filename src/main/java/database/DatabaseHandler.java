package database;

import model.Song;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {

    // Create a named constant for the URL.
    // NOTE: This value is specific for Java DB.
    private final String createDatabaseURL = "jdbc:derby:SongsDB;create=true";
    private final String databaseURL = "jdbc:derby:SongsDB;create=false";
    private final String shutdownURL = "jdbc:derby:;shutdown=true";
    private final String tableName = "SONGS";

    public DatabaseHandler() {
        //dropAllTables();
        createSongTable();
    }

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

    public boolean addSong(Song song) {
        boolean success = true;
        Connection conn = null;
        Statement statement = null;
        String sql = "INSERT INTO SONGS " +
                "      VALUES ('"
                + song.getPath()    + "', '"
                + song.getTitle()   + "', '"
                + song.getArtist()  + "', '"
                + song.getAlbum()   + "', '"
                + song.getYear()    + "', '"
                + song.getComment() + "', '"
                + song.getGenre()   + "'"
                + ")";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
            System.out.println("Added song successfully.");
            success = true;
        } catch (SQLException e) {
            success = false;
            if (e.getSQLState().equals("23505")) {
                System.out.println("Song is already saved in the database.");
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
        return success;
    }

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
        } catch (SQLException e) {
            //TODO error code
            if (e.getSQLState().equals("")) {
                System.out.println("Song not found in the database.");
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }

    public ArrayList<Song> getSongLibrary() {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<Song> library = new ArrayList<Song>();
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
                library.add(song);
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
        return library;
    }

    public void dropAllTables(){
        try {
            // Get a Statement object.
            Connection conn = DriverManager.getConnection(createDatabaseURL);
            Statement stmt = conn.createStatement();

            try {
                // Drop the UnpaidOrder table.
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


