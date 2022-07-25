package ru.natsuru.websdr.radioengine.linker;

import android.media.AudioTrack;
import com.neovisionaries.ws.client.WebSocketException;
import ru.natsuru.websdr.radioengine.catcher.Catcher;
import ru.natsuru.websdr.radioengine.util.Repository;

public class AudioLinker extends Linker<AudioTrack> {

    public AudioLinker(Catcher<AudioTrack> catcher) {
        super(catcher);
    }

    @Override
    void messages() {
        socket.sendText("GET /~~param?f=" + Repository.getFreq() + "&band=" + 0 + "&lo=" + Repository.getMinBorder() + "&hi=" + Repository.getMaxBorder() + "&mode=" + Repository.getModulation() + "&name=");
        socket.sendText("GET /~~param?gain=" + Repository.getGain());
        socket.sendText("GET /~~param?agchang=" + Repository.getAgchang());
        socket.sendText("GET /~~param?squelch=" + Repository.getSquelch());
        socket.sendText("GET /~~param?autonotch=" + Repository.getAutonotch());
        socket.sendText("GET /~~param?noisered=" + Repository.getNoise());
    }

    @Override
    void onClose() {
        socket.sendClose();
        socket.disconnect();
    }

    @Override
    void init() {
        try {
            socket.connect();
            sendMessage();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
}
