package ru.natsuru.websdr.radioengine.websocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import ru.natsuru.websdr.radioengine.util.Bucket;

public class SocketFactory {
    public static WebSocket getSocket(WebSocketListener listener, URI uri, List<Bucket> headers) throws IOException {
        WebSocket socket = new WebSocketFactory().setConnectionTimeout(1000).createSocket(uri);
        socket.addListener(listener);
        for (int i = 0; i < headers.size(); i++) {
            Bucket header = headers.get(i);
            socket.addHeader(header.getKey(), header.getValue());
        }
        return socket;
    }
}
