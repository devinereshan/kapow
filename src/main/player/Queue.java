package main.player;

import java.io.File;
import java.util.ArrayList;

import main.library.Track;


public class Queue {
    public final int SEEK_LEFT = -1;
    public final int SEEK_RIGHT = 1;

    private ArrayList<Track> tracks;
    private ArrayList<File> trackFiles;
    private int currentIndex;
    // private File currentFile;


    public void addNext(Track track) {
        if (tracks.size() == 0) {
            tracks.add(track);
        } else {
            tracks.add(currentIndex + 1, track);
        }
    }


    public Track quickLoadTrack(Track track) {
        addNext(track);

        if (tracks.size() > 1) {
            currentIndex += 1;
        }

        return tracks.get(currentIndex);
    }


    public Track next() {
        // if (tracks.size() - 1 <= currentIndex) {
        //     return;
        // }

        if (tracks.size() > 1) {
            currentIndex += 1;
        }

        return tracks.get(currentIndex);
    }

    public Track previous() {

        currentIndex -= 1;
        return tracks.get(currentIndex);
    }

    public Queue() {
        currentIndex = 0;
        trackFiles = new ArrayList<>();
        tracks = new ArrayList<>();
    }


    // public void addTrackFile(File file) {
    //     trackFiles.add(file);
    //     if (trackFiles.size() == 1) {
    //         setCurrentFile(0);
    //     }
    // }

    // Insert track into queue to be played immediately
    // public void quickLoadTrackFile(File file) {
    //     if (currentFile == null) {
    //         addTrackFile(file);
    //     } else {
    //         trackFiles.add(currentIndex + 1, file);
    //         setCurrentFile(currentIndex + 1);
    //     }
    // }


    // private void setCurrentFile(int index) {
    //     if (indexHasTrack(index)) {
    //         currentIndex = index;
    //         currentFile  = trackFiles.get(currentIndex);
    //     }
    // }


    public boolean hasTrack(int direction) {
        return indexHasTrack(currentIndex + direction);
    }


    private boolean indexHasTrack(int index) {
        if (index < 0 || index > tracks.size() - 1) {
            return false;
        }

        if (tracks.get(index) != null) {
            return true;
        }

        return false;
    }


    // public File getCurrentTrackFile() {
    //     return currentFile;
    // }


    // public void seek(int direction) {
    //     if (indexHasTrack(currentIndex + direction)) {
    //         // setCurrentFile(currentIndex + direction);
    //         return direction
    //     }
    // }


    public int size() {
        return trackFiles.size();
    }


    // testing
    // public String toString() {
    //     String queue = String.format("Tracks queued: %d\n", trackFiles.size());
    //     for (File file: trackFiles) {
    //         queue += String.format("%d. %s\n", trackFiles.indexOf(file) + 1, file.getName());
    //     }
    //     return queue;
    // }
}