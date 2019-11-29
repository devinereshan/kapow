package main.player;

import java.util.ArrayList;
import main.library.Track;


public class Queue {
    public final int SEEK_LEFT = -1;
    public final int SEEK_RIGHT = 1;

    private ArrayList<Track> tracks;
    private int currentIndex;


    public Queue() {
        currentIndex = 0;
        tracks = new ArrayList<>();
    }


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
        if (tracks.size() > 1) {
            currentIndex += 1;
        }

        return tracks.get(currentIndex);
    }


    public Track previous() {
        if (currentIndex > 0) {
            currentIndex -= 1;
        }

        return tracks.get(currentIndex);
    }


    public boolean hasTrack(int direction) {
        int index = currentIndex + direction;

        if (index < 0 || index > tracks.size() - 1) {
            return false;
        }

        if (tracks.get(index) != null) {
            return true;
        }

        return false;
    }
}