import controller.MainController;
import database.DatabaseHandler;
import model.Playlist;
import model.Song;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        MainController controller = new MainController();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        handler.addSong(new Song(1));

        Playlist plist1 =  Playlist.instantiatePlaylist("myPlaylist1");
        Playlist plist2 = Playlist.instantiatePlaylist("myPlaylist2");
        System.out.println(plist1.addSong(new Song(1)));
        System.out.println(plist2.addSong(new Song(1)));

        ArrayList<String> playlists = handler.getAllPlaylists();
        for (String playlist: playlists) {
            System.out.println(playlist);
        }

        handler.deleteSongFromPlaylist(plist1, new Song(1));
        handler.addSongToPlaylist(Playlist.instantiatePlaylist("newPlaylist"), new Song(1));

        handler.addSong(new Song(10));
        plist1.addSong(new Song(10));

        System.out.println("This should be true: " + handler.playlistExists(plist1.getName()));
        System.out.println("This should be false: "  + handler.playlistExists("randomPlaylistXYZ"));

        ArrayList<Song> songs = handler.getSongsInPlaylist(plist1);
        for (Song song : songs) {
            System.out.println(song.getPath());
        }
        //CreateCoffeeDB db = new CreateCoffeeDB();

        /*
        database.DatabaseHandler db = new database.DatabaseHandler();
        model.Song mySong = new model.Song("myPath", "myTitle", "myArtist", "myAlbum",
                2000, "myComment", "myGenre");
        model.Song newSong = new model.Song("ddd", "dd", "dd", "dd", 200,
                "dd", "ss");

        ArrayList<model.Song> library = db.getSongLibrary();
        System.out.println(library.toString());
        db.addSong(newSong);

        library = db.getSongLibrary();
        System.out.println(library.toString());

        view.MusicPlayerGUI player = new view.MusicPlayerGUI("MyTunes");
        player.setVisible(true);
        */
    }
}
