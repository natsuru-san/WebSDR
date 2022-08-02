package ru.natsuru.websdr.util.radioengine.linker;

import com.neovisionaries.ws.client.WebSocket;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import ru.natsuru.websdr.util.radioengine.catcher.Catcher;
import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.util.SocketFactory;
import ru.natsuru.websdr.model.SocketListener;

public abstract class Linker<T> extends Thread implements LinkerContract {

    protected WebSocket socket;

    public Linker(Catcher<T> catcher, List<Bucket<String>> params, URI uri) {
        try {
            socket = SocketFactory.getSocket(new SocketListener<>(catcher), uri, params);
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

    public abstract void messages();
    public abstract void onClose();
    public abstract void init();
}
