package kapow;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;


public class AudioListener implements LineListener {

    private boolean done = false;

    @Override
    public synchronized void update(LineEvent event) {
        // TODO Auto-generated method stub
        Type eventType = event.getType();
        if (eventType == Type.STOP || eventType == Type.CLOSE) {
            done = true;
            notifyAll();
        }
        if (eventType == Type.START || eventType == Type.OPEN) {
            done = false;
            notifyAll();
        }

    }

    public synchronized boolean isDone() {
        return done;
    }

    public void reset() {
        done = false;
    }
}