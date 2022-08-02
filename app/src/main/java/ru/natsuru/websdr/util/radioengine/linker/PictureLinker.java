package ru.natsuru.websdr.util.radioengine.linker;

import android.graphics.Bitmap;
import com.neovisionaries.ws.client.WebSocketException;
import java.net.URI;
import java.util.List;
import ru.natsuru.websdr.util.radioengine.catcher.Catcher;
import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.dao.repository.RepoPic;

public class PictureLinker extends Linker<Bitmap> {

    public PictureLinker(Catcher<Bitmap> catcher, List<Bucket<String>> params, URI uri) {
        super(catcher, params, uri);
    }

    @Override
    public void messages() {
        String msg = "GET /~~waterparam?start=" + RepoPic.getStart() + "&zoom=" + RepoPic.getZoom();
        socket.sendText(msg);
    }

    @Override
    public void onClose() {
        socket.sendClose();
        socket.disconnect();
    }

    @Override
    public void init() {
        try {
            socket.connect();
            socket.sendText("GET /~~waterparam?slow=4");
            socket.sendText("GET /~~waterparam?dmin=0&dmax=255");
            socket.sendText("GET /~~waterparam?band=0&zoom=0&start=0");
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
}
