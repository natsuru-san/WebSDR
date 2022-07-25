package ru.natsuru.websdr.radioengine.catcher;

public interface Catcher<T> {
    void pause();
    void unPause();
    void catchByteArray(byte[] data);
    void setRecordingObject(T obj);
}