import controller.MainController;
import database.DatabaseHandler;
import model.Playlist;

public class Main {

    public static void main(String[] args) {

        DatabaseHandler handler = DatabaseHandler.getInstance();
        //test code for playlist
        handler.clearPlaylists();
        handler.addPlaylist(new Playlist("Favorite"));
        handler.addPlaylist(new Playlist("Party"));
        handler.addPlaylist(new Playlist("Jazz"));

        MainController controller = new MainController();

/**        DatabaseHandler handler = DatabaseHandler.getInstance();
        handler.addSong(new Song(1));

        Playlist plist1 =  Playlist.instantiatePlaylist("myPlaylist1");
        Playlist plist2 = Playlist.instantiatePlaylist("myPlaylist2");

        handler.deleteSong(new Song(1));
        handler.deleteSong(new Song(10));
        handler.deleteSongFromPlaylist(plist1, new Song(1));

        System.out.println("This should be true: " + handler.playlistExists(plist1.getName()));
        System.out.println("This should be false: "  + handler.playlistExists("randomPlaylistXYZ"));

        ArrayList<Song> songs = handler.getSongsInPlaylist(plist1);
        for (Song song : songs) {
            System.out.println(song.getPath());
        }

        ArrayList<String> playlists = handler.getAllPlaylistsStrings();
        for (String playlist: playlists) {
            System.out.println(playlist);
        }

        handler.deletePlaylist(plist1);
        playlists = handler.getAllPlaylistsStrings();
        for (String playlist: playlists) {
            System.out.println(playlist);
        }
        System.out.println(" Before#############################################################################");
        for (Song song : songs) {
            System.out.println(song.getPath());
        }
        for (Song song: handler.getSongsInPlaylist(plist1)) {
            System.out.println(plist1.getName() + "  :  " +   song.getPath());
        }
        for (Song song: handler.getSongsInPlaylist(plist2)) {
            System.out.println(plist2.getName() + "  :  " +   song.getPath());
        }

        handler.deleteSong(new Song(1));
        System.out.println(" After#############################################################################");
        for (Song song : songs) {
            System.out.println(song.getPath());
        }
        for (Song song: handler.getSongsInPlaylist(plist1)) {
            System.out.println(plist1.getName() + "  :  " +   song.getPath());
        }
        for (Song song: handler.getSongsInPlaylist(plist2)) {
            System.out.println(plist2.getName() + "  :  " +   song.getPath());
        }**/

    }
}
