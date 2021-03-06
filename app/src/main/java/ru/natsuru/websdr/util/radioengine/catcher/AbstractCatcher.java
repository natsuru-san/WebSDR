package ru.natsuru.websdr.util.radioengine.catcher;

public abstract class AbstractCatcher<T> implements Catcher<T> {
    private boolean paused = false;
    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void unPause() {
        paused = false;
    }

    @Override
    public void catchByteArray(byte[] data) {
        if (!paused) {
            write(data);
        }
    }

    @Override
    public void setRecordingObject(T obj) {
        recordingObject(obj);
    }

    public abstract void recordingObject(T obj);
    public abstract void write(byte[] arr);
}
