//Copyright by Natsuru-san

package ru.natsuru.websdr;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import ru.natsuru.websdr.radioengine.MainInit;

@SuppressWarnings("FieldCanBeLocal")
public class Main extends AppCompatActivity {
    private boolean settingsShow = false;
    private AudioTrack audioTrack;
    private String freqText;
    private MainInit mainInit;
    private Tuner tuner;
    private FragmentManager fragmentManager;
    private TextView currentFreq;
    private final int SAMPLE_RATE_LOW = 7119;
    private final int SAMPLE_RATE = SAMPLE_RATE_LOW * 2;
    private final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private final int MASK = AudioFormat.CHANNEL_OUT_MONO;
    private final int MODE = AudioTrack.MODE_STREAM;
    private final int ID = AudioManager.AUDIO_SESSION_ID_GENERATE;
    private final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, MASK, FORMAT) * 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        freqText = 1008 + getString(R.string.khz);
        currentFreq = findViewById(R.id.CurrentFreq);
        currentFreq.setText(freqText);
        initView();
        initRadio();
    }
    private void initView(){
        fragmentManager = getSupportFragmentManager();
        tuner = new Tuner();
        fragmentManager.beginTransaction().replace(R.id.Container, tuner).commit();
        tuner.setMain(this);
    }
    private void initRadio(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
        AudioAttributes.Builder aab = new AudioAttributes.Builder();
        aab.setFlags(AudioAttributes.ALLOW_CAPTURE_BY_ALL);
        aab.setFlags(AudioAttributes.CONTENT_TYPE_MUSIC);
        AudioFormat.Builder af = new AudioFormat.Builder();
        af.setEncoding(FORMAT);
        af.setChannelMask(MASK);
        audioTrack = new AudioTrack(aab.build(), af.build(), BUFFER_SIZE, MODE, ID);
        audioTrack.setPlaybackRate(SAMPLE_RATE);
        mainInit = new MainInit(audioTrack);
        mainInit.setDecoder(false);
    }
    protected void sendAudioParams(int gain, int noisereduse, double agchang, int squelch, int autonotch){
        mainInit.setAudioParams(gain, noisereduse, agchang, squelch, autonotch);
    }
    protected void setVolume(float volume){
        audioTrack.setVolume(volume);
    }
    @SuppressWarnings("SameParameterValue")
    protected void sendParams(double freq, int band, double minBorder, double maxBorder, int mode){
        freqText = freq + getString(R.string.khz);
        mainInit.setParams(freq, band, minBorder, maxBorder, mode);
        currentFreq.setText(freqText);
    }
    protected void setAudioMode(boolean audioMode){
        if(audioMode){
            audioTrack.setPlaybackRate(SAMPLE_RATE_LOW);
        }else{
            audioTrack.setPlaybackRate(SAMPLE_RATE);
        }
    }
    protected void setSettingsShow(boolean settingsShow){
        this.settingsShow = settingsShow;
    }
    @Override
    public void onBackPressed() {
        if(settingsShow){
            tuner.settingsShow(false);
        }else{
            mainInit.closeSocket();
            finish();
        }
    }
}