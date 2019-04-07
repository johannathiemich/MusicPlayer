package database;

import model.Playlist;
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
    private final String songsTableName = "SONGS";
    private final String playlistSongsTableName = "PLAYLIST_SONGS";
    private final String playlistTableName = "PLAYLISTS";
    private static DatabaseHandler handler_instance = null;

    /**
     * Constructor for this class
     */
    private DatabaseHandler() {
        //dropAllTables();  //this is for testing
        createSongTable();
        createPlaylistTable();
        createPlaylistSongsTable();
    }

    public static DatabaseHandler getInstance()
    {
        if (handler_instance == null)
            handler_instance = new DatabaseHandler();

        return handler_instance;
    }

    /**
     * This method creates the table that holds the songs in the database. If the table already exists, it won't
     * create a new one. The ID for each row is just the absolute path to the mp3 file.
     * The table contains the following columns: SONG_PATH, TITLE; ARTIST, ALBUM, YEAR_PUBLISHED, COMMENT, GENRE
     */
    public void createSongTable(){
        Connection conn = null;
        Statement statement = null;
        String sql = "CREATE TABLE "+ songsTableName + "( " +
                "FILEPATH VARCHAR(512) PRIMARY KEY," +
                "TITLE VARCHAR(256), " +
                "ARTIST VARCHAR(256), " +
                "ALBUM VARCHAR(256), " +
                "YEAR_PUBLISHED VARCHAR(256), " +
                "COMMENT VARCHAR(256), " +
                "GENRE VARCHAR(256), " +
                "DURATION INTEGER" +
                " )";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            DriverManager.getConnection(shutdownURL);
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println(songsTableName +" table already exists, won't create a new one.");
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void createPlaylistSongsTable() {
        Connection conn = null;
        Statement statement = null;
        String sql = "CREATE TABLE " + playlistSongsTableName + "( " +
                "NAME VARCHAR(512), " +
                "FILEPATH VARCHAR(512), " +
                "PRIMARY KEY (NAME, FILEPATH) )";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            DriverManager.getConnection(shutdownURL);
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println(playlistSongsTableName +" table already exists, won't create a new one.");
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void createPlaylistTable() {
        Connection conn = null;
        Statement statement = null;
        String sql = "CREATE TABLE " + playlistTableName + "( " +
                "NAME VARCHAR(512) PRIMARY KEY )";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            DriverManager.getConnection(shutdownURL);
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println(playlistTableName +" table already exists, won't create a new one.");
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
        String sql = "INSERT INTO "+ songsTableName +
                "      VALUES ('"
                + song.getPath()    + "', '"
                + song.getTitle()   + "', '"
                + song.getArtist()  + "', '"
                + song.getAlbum()   + "', '"
                + song.getYear()    + "', '"
                + song.getComment() + "', '"
                + song.getGenre()   + "', "
                + song.getDuration()    //this field is integer
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

    public boolean addPlaylist(Playlist playlist) {
        boolean success = false;
        Connection conn = null;
        Statement statement = null;
        String sql = "INSERT INTO "+ playlistTableName +
                "      VALUES ('" + playlist.getName() + "')";
        System.out.println("[Database] sql executed: " + sql);
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
            System.out.println("[Database] Added playlist successfully.");
            success = true;
        } catch (SQLException e) {
            success = false;
            if (e.getSQLState().equals("23505")) {
                System.out.println("[Database] playlist is already saved in the database.");
                success = false;
            } else if (e.getSQLState().equals("XJ015")) {
                System.out.println("[Database] Derby shutdown normally.");
            } else {
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }

    public boolean addSongToPlaylist(Playlist playlist, Song song) {
        boolean success = false;
        Connection conn = null;
        Statement statement = null;
        if (!playlistExists(playlist.getName())) {
            System.out.println("This playlist does not exist yet, needs to be created first.");
            return false;
        }
        String sql = "INSERT INTO "+ playlistSongsTableName +
                "      VALUES ('"
                + playlist.getName() + "', '"
                + song.getPath()     + "'"
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
                System.out.println("[Database] This song is already saved in the playlist.");
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
        String sql = "DELETE FROM " + songsTableName +
                "      WHERE FILEPATH = '" + song.getPath() + "'";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            conn.close();
            System.out.println("[Database] Deleted song.");
        } catch (SQLException e) {
            success = false;
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("[Database] Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
        //TODO need to look into its behavior...
        if (success) {
            for (Playlist playlist : this.getAllPlaylistsObjects()) {
                this.deleteSongFromPlaylist(playlist, song);
            }
        }
        return success;
    }

    public boolean deletePlaylist(Playlist playlist) {
        boolean success = true;
        Connection conn = null;
        Statement statement = null;
        String sql1 = "DELETE FROM " + playlistTableName +
                "      WHERE NAME = '" + playlist.getName() + "'";
        String sql2 = "DELETE FROM " + playlistSongsTableName +
                "      WHERE NAME = '" + playlist.getName() + "'";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql1);
            statement.execute(sql2);
            conn.close();
            System.out.println("[Database] Deleted playlist from database.");
        } catch (SQLException e) {
            success = false;
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("[Database] Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean deleteSongFromPlaylist(Playlist playlist, Song song) {
        boolean success = true;
        Connection conn = null;
        Statement statement = null;
        String sql = "DELETE FROM " + playlistSongsTableName +
                "      WHERE FILEPATH = '" + song.getPath() + "' AND NAME = '" + playlist.getName() + "'";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql);
            conn.close();
            System.out.println("[Database] Deleted song from playlist.");
        } catch (SQLException e) {
            success = false;
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("[Database] Derby shutdown normally.");
            } else {
                e.printStackTrace();
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
        ArrayList<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM " + songsTableName;
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while(results.next())
            {
                String file_path = results.getString(results.findColumn("FILEPATH"));
                String title = results.getString(results.findColumn("TITLE"));
                String artist = results.getString(results.findColumn("ARTIST"));
                String album = results.getString(results.findColumn("ALBUM"));
                String year = results.getString(results.findColumn("YEAR_PUBLISHED"));
                String comment = results.getString(results.findColumn("COMMENT"));
                String genre = results.getString(results.findColumn("GENRE"));

                //TODO [3] Replace this try-catch to 'int duration = results.getInt(8);'.
                int duration;
                try {
                    duration = results.getInt(8);
                }catch(SQLException ex){
                    duration = 0;
                }
                Song song = new Song(file_path, title, artist, album, year, comment, genre, duration);
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
            return null;
        }
        return list;
    }

    public ArrayList<Song> getSongsInPlaylist(Playlist playlist) {
        Connection conn = null;
        Statement statement = null;
        ArrayList<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM " + playlistSongsTableName + " INNER JOIN " + songsTableName + " ON " +
                playlistSongsTableName + ".FILEPATH = " + songsTableName + ".FILEPATH " +
                "WHERE " + playlistSongsTableName + ".NAME = '" + playlist.getName() + "'";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while(results.next())
            {
                String file_path = results.getString(results.findColumn("FILEPATH"));
                String title = results.getString(results.findColumn("TITLE"));
                String artist = results.getString(results.findColumn("ARTIST"));
                String album = results.getString(results.findColumn("ALBUM"));
                String year = results.getString(results.findColumn("YEAR_PUBLISHED"));
                String comment = results.getString(results.findColumn("COMMENT"));
                String genre = results.getString(results.findColumn("GENRE"));

                //TODO [3] Replace this try-catch to 'int duration = results.getInt(8);'.
                int duration;
                try {
                    duration = results.getInt(8);
                }catch(SQLException ex){
                    duration = 0;
                }
                Song song = new Song(file_path, title, artist, album, year, comment, genre, duration);
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
     * This overloads the other method with the same name (see parameter difference)
     * @param playlistName
     * @return
     */
    public ArrayList<Song> getSongsInPlaylist(String playlistName) {
        Connection conn = null;
        Statement statement = null;
        ArrayList<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM " + playlistSongsTableName + " INNER JOIN " + songsTableName + " ON " +
                playlistSongsTableName + ".FILEPATH = " + songsTableName + ".FILEPATH " +
                "WHERE " + playlistSongsTableName + ".NAME = '" + playlistName + "'";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while(results.next())
            {
                String file_path = results.getString(results.findColumn("FILEPATH"));
                String title = results.getString(results.findColumn("TITLE"));
                String artist = results.getString(results.findColumn("ARTIST"));
                String album = results.getString(results.findColumn("ALBUM"));
                String year = results.getString(results.findColumn("YEAR_PUBLISHED"));
                String comment = results.getString(results.findColumn("COMMENT"));
                String genre = results.getString(results.findColumn("GENRE"));

                //TODO [3] Replace this try-catch to 'int duration = results.getInt(8);'.
                int duration;
                try {
                    duration = results.getInt(8);
                }catch(SQLException ex){
                    duration = 0;
                }
                Song song = new Song(file_path, title, artist, album, year, comment, genre, duration);
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

    public ArrayList<Playlist> getAllPlaylistsObjects() {
        Connection conn = null;
        Statement statement = null;
        ArrayList<Playlist> list = new ArrayList<Playlist>();
        String sql = "SELECT NAME FROM " + playlistTableName;
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while(results.next())
            {
                String name = results.getString(results.findColumn("NAME"));
                Playlist playlist = new Playlist(name);
                list.add(playlist);
            }
            results.close();
            conn.close();

        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
            return null;
        }

        //TODO need to check this behavior...
        for (Playlist playlist : list) {
            ArrayList<Song> songList = getSongsInPlaylist(playlist);
            playlist.addMultipleSongs(songList);
        }

        return list;
    }

    public ArrayList<String> getAllPlaylistsStrings() {
        Connection conn = null;
        Statement statement = null;
        ArrayList<String> list = new ArrayList<String>();
        String sql = "SELECT NAME FROM " + playlistTableName;
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while(results.next())
            {
                String name = results.getString(results.findColumn("NAME"));
                list.add(name);
            }
            results.close();
            conn.close();

        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
            return null;
        }
        return list;
    }

    public boolean playlistExists(String playlistName) {
        boolean exists = false;
        Connection conn = null;
        Statement statement = null;
        ArrayList<Song> list = new ArrayList<Song>();
        String sql = "SELECT NAME FROM " + playlistTableName + " WHERE NAME = '" + playlistName + "'";
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while(results.next())
            {
                exists = true;
                System.out.println(results.getString(results.findColumn("NAME")));
            }
            results.close();
            conn.close();

        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
                exists = false;
            }
        }
        return exists;
    }

    /**
     * Clears Playlist_Songs and Playlist table
     */
    public boolean clearPlaylists(){
        boolean success = false;
        Connection conn = null;
        Statement statement = null;
        String sql1 = "DELETE FROM " + playlistTableName;
        String sql2 = "DELETE FROM " + playlistSongsTableName;
        try {
            conn = DriverManager.getConnection(createDatabaseURL);
            statement = conn.createStatement();
            statement.execute(sql1);
            statement.execute(sql2);
            conn.close();
            success = true;
            System.out.println("[Database] Cleared "+playlistTableName+" and "+playlistSongsTableName+".");

        } catch (SQLException e) {
            success = false;
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("[Database] Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * This method drops all tables currently contained in the database. This method is usefule for resetting
     * the database.
     */
    private void dropAllTables(){
        try {
            //Get connection and statement
            Connection conn = DriverManager.getConnection(createDatabaseURL);
            Statement stmt = conn.createStatement();

            try {
                // Drop the 'SONGS' table from DB
                stmt.execute("DROP TABLE "+ songsTableName);
                System.out.println(songsTableName +" table dropped.");

                stmt.execute("DROP TABLE "+ playlistSongsTableName);
                System.out.println(playlistSongsTableName +" table dropped.");

                stmt.execute("DROP TABLE "+ playlistTableName);
                System.out.println(playlistTableName +" table dropped.");
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


