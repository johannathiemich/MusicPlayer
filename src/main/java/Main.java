import com.mpatric.mp3agic.*;
import javazoom.jlgui.basicplayer.BasicPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //MusicPlayerGUI playerView = new MusicPlayerGUI("MyTunes 0.5");
        //playerView.setVisible(true);

        MainController controller = new MainController();
        //CreateCoffeeDB db = new CreateCoffeeDB();

        /*
        DatabaseHandler db = new DatabaseHandler();
        Song mySong = new Song("myPath", "myTitle", "myArtist", "myAlbum",
                2000, "myComment", "myGenre");
        Song newSong = new Song("ddd", "dd", "dd", "dd", 200,
                "dd", "ss");

        ArrayList<Song> library = db.getSongLibrary();
        System.out.println(library.toString());
        db.addSong(newSong);

        library = db.getSongLibrary();
        System.out.println(library.toString());

        MusicPlayerGUI player = new MusicPlayerGUI("MyTunes");
        player.setVisible(true);
        */
    }
}
