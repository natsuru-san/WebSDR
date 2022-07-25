package ru.natsuru.websdr.radioengine.linker;

import com.neovisionaries.ws.client.WebSocket;
import java.io.IOException;
import ru.natsuru.websdr.radioengine.catcher.Catcher;
import ru.natsuru.websdr.radioengine.util.Repository;
import ru.natsuru.websdr.radioengine.websocket.SocketFactory;
import ru.natsuru.websdr.radioengine.websocket.SocketListener;

public abstract class Linker<T> extends Thread implements LinkerContract {

    protected WebSocket socket;

    public Linker(Catcher<T> catcher) {
        try {
            socket = SocketFactory.getSocket(new SocketListener<>(catcher), Repository.getUri(), Repository.getHeaders());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendMessage() {
        messages();
    }

    @Override
    public void close() {
        onClose();
        interrupt();
    }

    @Override
    public void run() {
        init();
    }

    abstract void messages();
    abstract void onClose();
    abstract void init();
}
