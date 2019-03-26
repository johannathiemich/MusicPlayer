import controller.MainController;
import database.DatabaseHandler;
import model.Playlist;
import model.Song;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

//        MainController controller = new MainController();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        Playlist plist1 = new Playlist("myPlaylist1");
        Playlist plist2 = new Playlist("myPlaylist2");
        plist1.addSong(new Song());
        plist2.addSong(new Song());

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
