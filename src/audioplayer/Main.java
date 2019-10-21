package audioplayer;

import java.io.File;
import java.util.Scanner;

public class Main {

    // temporary for program control
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        File songFile = new File("/home/bo/Music/Rick Astley/never_gonna_give_you_up.wav");
        
        if (songFile.exists()) {

            Track track = new Track(songFile);

            AudioPlayer audioPlayer = new AudioPlayer();

            audioPlayer.queueTrack(track);



            audioPlayer.play();

            // String previousElapsedTime = "00:00:00";
            // String elapsedTime = audioPlayer.getElapsedTime();
                
            String input = "";
            while (!input.equals("q")) {
                if (input.equals("p")) {
                    audioPlayer.pauseTrack();
                } else if (input.equals("s")) {
                    audioPlayer.play();
                }

                // elapsedTime = audioPlayer.getElapsedTime();

                // if (!elapsedTime.equals(previousElapsedTime)) {
                //     System.out.println(elapsedTime);
                //     previousElapsedTime = String.valueOf(elapsedTime);
                // }


                input = scanner.nextLine();
            }
            
            audioPlayer.quit();
            System.out.println("Done");
        }
    }
}