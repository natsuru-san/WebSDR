package ru.natsuru.websdr.util.radioengine.linker;

public interface LinkerContract extends Runnable {
    void sendMessage();
    void close();
    @Override
    void run();
}
