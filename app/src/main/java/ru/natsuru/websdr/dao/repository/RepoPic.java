package ru.natsuru.websdr.dao.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import ru.natsuru.websdr.model.Bucket;

public class RepoPic {

    //Параметры соединения
    private static final String accept = "*/*";
    private static final String acceptEncoding = "gzip, deflate";
    private static final String acceptLanguage = "en-US";
    private static final String cacheControl = "no-cache";
    private static final String connection = "keep-alive, Upgrade";
    private static final String cookie = "ID=62e0348dfdc3; view=2";
    private static final String dnt = "1";
    private static final String host = "websdr.ewi.utwente.nl:8901";
    private static final String origin = "http://" + host;
    private static final String pragma = "no-cache";
    private static final String secWebSocketExtensions = "permessage-deflate";
    private static final String secWebSocketKey = "NxkIlBqtdofiUqJmxp2r3A==";
    private static final String secWebSocketVersion = "13";
    private static final String upgrade = "websocket";
    private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0 Safari/605.1.15";
    private static final String additional = "/~~waterstream0?format=10&width=1024&zoom=0&start=0";
    private static URI uri;
    static {
        try {
            uri = new URI("ws://" + host + additional);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //Настройки сессии
    private static byte[] signal = null;
    private static long start = 0L;
    private static int zoom = 0;

    public static long getStart() {
        return start;
    }

    public static void setStart(long start) {
        RepoPic.start = start;
    }

    public static int getZoom() {
        return zoom;
    }

    public static void setZoom(int zoom) {
        RepoPic.zoom = zoom;
    }

    public static void setSignal(byte[] signal) {
        RepoPic.signal = signal;
    }

    public static URI getUri() {
        return uri;
    }

    public static byte[] getSignal() {
        return signal;
    }

    public static List<Bucket<String>> getHeaders() {
        List<Bucket<String>> params = new ArrayList<>();
        params.add(new Bucket<>("Accept", accept));
        params.add(new Bucket<>("Accept-Encoding", acceptEncoding));
        params.add(new Bucket<>("Accept-Language", acceptLanguage));
        params.add(new Bucket<>("Cache-Control", cacheControl));
        params.add(new Bucket<>("Connection", connection));
        params.add(new Bucket<>("Cookie", cookie));
        params.add(new Bucket<>("DNT", dnt));
        params.add(new Bucket<>("Host", host));
        params.add(new Bucket<>("Origin", origin));
        params.add(new Bucket<>("Pragma", pragma));
        params.add(new Bucket<>("Sec-WebSocket-Extensions", secWebSocketExtensions));
        params.add(new Bucket<>("Sec-WebSocket-Key", secWebSocketKey));
        params.add(new Bucket<>("Sec-WebSocket-Version", secWebSocketVersion));
        params.add(new Bucket<>("Upgrade", upgrade));
        params.add(new Bucket<>("User-Agent", userAgent));
        return params;
    }
}
