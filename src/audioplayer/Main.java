package audioplayer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File songFile = new File("/home/bo/Music/Rick Astley/never_gonna_give_you_up.wav");
        
        if (songFile.exists()) {

            AudioListener al = new AudioListener();
            AudioPlayer ap = new AudioPlayer(al);

            ap.setCurrentSong(songFile);
            ap.playSong();
            
            // System.out.println("loading song");
            // while (!ap.isPlaying());
            // System.out.println("loaded song");
            // while (ap.isPlaying());
            
            while (ap.isPlaying()) {

            }
            
            System.out.println("Done");
            ap.closeCurrentClip();
        }
    }
}