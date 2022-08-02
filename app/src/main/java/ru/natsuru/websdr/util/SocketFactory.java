package ru.natsuru.websdr.util;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import ru.natsuru.websdr.model.Bucket;

public class SocketFactory {
    public static WebSocket getSocket(WebSocketListener listener, URI uri, List<Bucket<String>> headers) throws IOException {
        WebSocket socket = new WebSocketFactory().setConnectionTimeout(1000).createSocket(uri);
        socket.addListener(listener);
        for (int i = 0; i < headers.size(); i++) {
            Bucket<String> header = headers.get(i);
            socket.addHeader(header.getKey(), header.getValue());
        }
        return socket;
    }
}
