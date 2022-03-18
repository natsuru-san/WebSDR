package ru.natsuru.websdr;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ru.natsuru.websdr.radioengine.MainInit;

@SuppressWarnings("FieldCanBeLocal")
public class Main extends AppCompatActivity {
    private AudioTrack audioTrack;
    private MainInit mainInit;
    private final int sampleRate = 7000 * 2;
    private final int typeEnc = AudioFormat.ENCODING_PCM_16BIT;
    private final int mask = AudioFormat.CHANNEL_OUT_MONO;
    private final int mode = AudioTrack.MODE_STREAM;
    private final int id = AudioManager.AUDIO_SESSION_ID_GENERATE;
    private final int bufferSize = AudioTrack.getMinBufferSize(sampleRate, mask, typeEnc) * 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        AudioAttributes.Builder aab = new AudioAttributes.Builder();
        aab.setFlags(AudioAttributes.ALLOW_CAPTURE_BY_ALL);
        aab.setFlags(AudioAttributes.CONTENT_TYPE_MUSIC);
        AudioFormat.Builder af = new AudioFormat.Builder();
        af.setSampleRate(sampleRate);
        af.setEncoding(typeEnc);
        af.setChannelMask(mask);
        audioTrack = new AudioTrack(aab.build(), af.build(), bufferSize, mode, id);
        audioTrack.setPlaybackRate(sampleRate);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
        audioTrack.play();
        mainInit = new MainInit(audioTrack);
    }
}