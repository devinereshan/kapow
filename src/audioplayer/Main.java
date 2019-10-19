package audioplayer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File songFile = new File("/home/bo/Music/Rick Astley/never_gonna_give_you_up.wav");
        
        if (songFile.exists()) {

            Track currentTrack = new Track(songFile);
            currentTrack.playClip();
            
            
            while (currentTrack.isPlaying()) {

            }
            
            System.out.println("Done");
            currentTrack.closeClip();
        }
    }
}