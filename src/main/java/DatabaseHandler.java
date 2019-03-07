import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class DatabaseHandler {

    private final String databaseURL = "jdbc:derby:SongsDB;create=true";
    private final String tableName = "SONGS";

    public DatabaseHandler() {

        // Create a named constant for the URL.
        // NOTE: This value is specific for Java DB.
        Connection conn = null;
        Statement statement = null;
        String sql = "CREATE TABLE SONGS (SONG_PATH VARCHAR(512) PRIMARY KEY, TITLE VARCHAR(256), ARTIST VARCHAR(256), " +
                "ALBUM VARCHAR(256), YEAR_PUBLISHED VARCHAR(23), COMMENT VARCHAR(256), GENRE VARCHAR(256))";
        try {
            conn = DriverManager.getConnection(databaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            String SQLState = e.getSQLState();
            if (SQLState.equals("X0Y32")) {
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
            conn = DriverManager.getConnection(databaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            return true;
        } catch (SQLException e) {
            String SQLState = e.getSQLState();
            if (SQLState.equals("")) {
                System.out.println("Song is already saved in the database.");
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }
}


