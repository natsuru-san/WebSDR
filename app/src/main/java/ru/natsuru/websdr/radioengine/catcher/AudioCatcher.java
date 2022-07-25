package ru.natsuru.websdr.radioengine.catcher;

import android.media.AudioTrack;
import android.os.Build;
import ru.natsuru.websdr.radioengine.util.Decoder;

public class AudioCatcher extends AbstractCatcher<AudioTrack> {

    private AudioTrack track;

    @Override
    void recordingObject(AudioTrack obj) {
        track = obj;
    }

    @Override
    void write(byte[] arr) {
        short[] decoded = Decoder.decode(arr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            System.out.println("Received: " + decoded.length);
            track.write(decoded, 0, decoded.length, AudioTrack.WRITE_BLOCKING);
        } else {
            track.write(decoded, 0, decoded.length);
        }
        track.flush();
    }
}
