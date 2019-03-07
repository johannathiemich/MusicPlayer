import com.mpatric.mp3agic.*;

import java.io.IOException;

public class MP3Handler {

    public MP3Handler() {
    }

    public static Song createSong(String filePath) {
        Song selectedSong = null;
        try {
            Mp3File mp3file = new Mp3File(filePath);
            if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                selectedSong = new Song(filePath, id3v1Tag.getTitle(), id3v1Tag.getArtist(), id3v1Tag.getAlbum(),
                        Integer.parseInt(id3v1Tag.getYear()),id3v1Tag.getComment(), id3v1Tag.getGenreDescription() );
            } else if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                selectedSong = new Song(filePath, id3v2Tag.getTitle(), id3v2Tag.getArtist(), id3v2Tag.getAlbum(),
                        Integer.parseInt(id3v2Tag.getYear()),id3v2Tag.getComment(), id3v2Tag.getGenreDescription() );
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
        return selectedSong;
    }
}
