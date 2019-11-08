/* Kapow! - Kool Audio Player, or whatever... */
package kapow;

import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    // temporary for program control
    static Scanner scanner = new Scanner(System.in);

    static void databaseTest() {
        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();

            databaseConnection.restoreFromTemplate();
            databaseConnection.insertSampleData();
            databaseConnection.createBackup();
            databaseConnection.readTrackTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    static void commandLineTest() {
        String path = System.getProperty("user.home") + File.separator + "Music/Rick Astley/never_gonna_give_you_up.wav";

        File songFile = new File(path);

        if (songFile.exists()) {

            // Track track = new Track(songFile);

            AudioPlayer audioPlayer = new AudioPlayer();

            audioPlayer.queueTrack(songFile);



            audioPlayer.play();

            String previousElapsedTime = "00:00:00";
            String elapsedTime = audioPlayer.getElapsedTime();
            String lengthOfTrack = audioPlayer.getLengthOfTrackInSeconds();

            String input = "";
            while (!input.equals("q")) {
                // if (input.equals("p")) {
                //     audioPlayer.pauseTrack();
                // } else if (input.equals("s")) {
                //     audioPlayer.play();
                // }

                elapsedTime = audioPlayer.getElapsedTime();

                if (!elapsedTime.equals(previousElapsedTime)) {
                    System.out.format("%s / %s\n", elapsedTime, lengthOfTrack);
                    previousElapsedTime = String.valueOf(elapsedTime);
                }


                // input = scanner.nextLine();
            }

            audioPlayer.quit();
            System.out.println("Done");
        }
    }


    public static void main(String[] args) {
        databaseTest();
    }
}