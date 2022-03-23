//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings({"SpellCheckingInspection", "FieldCanBeLocal"})
public class InitWS{
    private final WebSocket ws;
    private final URI uri;
    public InitWS(String key, String baseUrl, String additional, WebSocketListener wsl) throws IOException, URISyntaxException{
        uri = new URI("ws://" + baseUrl + additional);
        ws = new WebSocketFactory().setConnectionTimeout(1000).createSocket(uri);
        ws.addListener(wsl);
        //Ставим заголовки запроса
        ws.addHeader("Accept", "*/*");
        ws.addHeader("Accept-Encoding", "gzip, deflate");
        ws.addHeader("Cache-Control", "no-cache");
        ws.addHeader("Connection", "keep-alive, Upgrade");
        ws.addHeader("Cookie", "ID=61ad12a58ea4; view=2; usejava=nn");
        ws.addHeader("DNT", "1");
        ws.addHeader("Host", baseUrl);
        ws.addHeader("Origin", "http://" + baseUrl);
        ws.addHeader("Pragma", "no-cache");
        ws.addHeader("Sec-WebSocket-Extensions", "permessage-deflate");
        ws.addHeader("Sec-WebSocket-Key", key);
        ws.addHeader("Sec-WebSocket-Version", "13");
        ws.addHeader("Upgrade", "websocket");
        ws.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0 Safari/605.1.15");
    }
    protected WebSocket getWs(){
        return ws;
    }
}
