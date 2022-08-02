package ru.natsuru.websdr.util.radioengine.catcher;

public interface Catcher<T> {
    void pause();
    void unPause();
    void catchByteArray(byte[] data);
    void setRecordingObject(T obj);
}