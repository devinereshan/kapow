package audioplayer;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        AudioPlayer ap = new AudioPlayer();
        File songFile = new File("/home/bo/Music/Rick Astley/never_gonna_give_you_up.wav");
        ap.setCurrentSong(songFile);
        ap.playSong();

        while (true);
    }
}