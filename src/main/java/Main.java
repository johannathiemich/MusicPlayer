import com.mpatric.mp3agic.*;
import javazoom.jlgui.basicplayer.BasicPlayer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        MusicPlayerGUI player = new MusicPlayerGUI("MyTunes");
        player.setVisible(true);

        //CreateCoffeeDB db = new CreateCoffeeDB();

        DatabaseHandler db = new DatabaseHandler();
        Song mySong = new Song("myPath", "myTitle", "myArtist", "myAlbum",
                "2000", "myComment", "myGenre");

        db.addSong(mySong);
    }
}
