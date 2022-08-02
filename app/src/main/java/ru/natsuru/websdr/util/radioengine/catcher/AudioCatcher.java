package ru.natsuru.websdr.util.radioengine.catcher;

import android.media.AudioTrack;
import android.os.Build;
import ru.natsuru.websdr.util.Decoder;

public class AudioCatcher extends AbstractCatcher<AudioTrack> {

    private AudioTrack track;

    @Override
    public void recordingObject(AudioTrack obj) {
        track = obj;
    }

    @Override
    public void write(byte[] arr) {
        short[] decoded = Decoder.decode(arr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            track.write(decoded, 0, decoded.length, AudioTrack.WRITE_BLOCKING);
        } else {
            track.write(decoded, 0, decoded.length);
        }
        track.flush();
    }
}
