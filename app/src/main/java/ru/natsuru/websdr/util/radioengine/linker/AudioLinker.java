package ru.natsuru.websdr.util.radioengine.linker;

import android.media.AudioTrack;
import com.neovisionaries.ws.client.WebSocketException;
import java.net.URI;
import java.util.List;
import ru.natsuru.websdr.util.radioengine.catcher.Catcher;
import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.dao.repository.RepoAudio;

public class AudioLinker extends Linker<AudioTrack> {

    public AudioLinker(Catcher<AudioTrack> catcher, List<Bucket<String>> params, URI uri) {
        super(catcher, params, uri);
    }

    @Override
    public void messages() {
        socket.sendText("GET /~~param?f=" + RepoAudio.getFreq() + "&band=" + 0 + "&lo=" + RepoAudio.getMinBorder() + "&hi=" + RepoAudio.getMaxBorder() + "&mode=" + RepoAudio.getModulation() + "&name=");
        socket.sendText("GET /~~param?gain=" + RepoAudio.getGain());
        socket.sendText("GET /~~param?agchang=" + RepoAudio.getAgchang());
        socket.sendText("GET /~~param?squelch=" + RepoAudio.getSquelch());
        socket.sendText("GET /~~param?autonotch=" + RepoAudio.getAutonotch());
        socket.sendText("GET /~~param?noisered=" + RepoAudio.getNoise());
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
            sendMessage();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
}
