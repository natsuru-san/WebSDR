package ru.natsuru.websdr.radioengine.linker;

public interface LinkerContract extends Runnable {
    void sendMessage();
    void close();
    @Override
    void run();
}
