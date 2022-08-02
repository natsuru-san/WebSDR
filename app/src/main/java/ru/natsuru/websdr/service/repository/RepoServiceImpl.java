package ru.natsuru.websdr.service.repository;

import java.net.URI;
import java.util.List;
import ru.natsuru.websdr.dao.repository.RepoAudio;
import ru.natsuru.websdr.dao.repository.RepoPic;
import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.model.MemoryCell;

public class RepoServiceImpl implements RepoService {
    @Override
    public MemoryCell getCurrentStation() {
        int mode = RepoAudio.getMode();
        float minBorder = RepoAudio.getMinBorder();
        float maxBorder = RepoAudio.getMaxBorder();
        float freq = RepoAudio.getFreq();
        return new MemoryCell(mode, minBorder, maxBorder, freq, -1);
    }

    @Override
    public List<Bucket<String>> getCurrentRadioHeaders() {
        return RepoAudio.getHeaders();
    }

    @Override
    public List<Bucket<String>> getCurrentWaterfallHeaders() {
        return RepoPic.getHeaders();
    }

    @Override
    public URI getCurrentWaterfallUri() {
        return RepoPic.getUri();
    }

    @Override
    public URI getCurrentRadioUri() {
        return RepoAudio.getUri();
    }

    @Override
    public boolean isAlaw() {
        return RepoAudio.isaLaw();
    }

    @Override
    public void setAlaw(boolean alaw) {
        RepoAudio.setaLaw(alaw);
    }

    @Override
    public void setRadioMode(int mode) {
        RepoAudio.setMode(mode);
    }

    @Override
    public float getPreviousFreq() {
        return RepoAudio.getPreviousFreq();
    }

    @Override
    public void setPreviousFreq(float previousFreq) {
        RepoAudio.setPreviousFreq(previousFreq);
    }

    @Override
    public int getNoise() {
        return RepoAudio.getNoise();
    }

    @Override
    public void setNoise(int noise) {
        RepoAudio.setNoise(noise);
    }

    @Override
    public int getSquelch() {
        return RepoAudio.getSquelch();
    }

    @Override
    public void setSquelch(int squelch) {
        RepoAudio.setSquelch(squelch);
    }

    @Override
    public int getAutonotch() {
        return RepoAudio.getAutonotch();
    }

    @Override
    public void setAutonotch(int autonotch) {
        RepoAudio.setAutonotch(autonotch);
    }

    @Override
    public int getGain() {
        return RepoAudio.getGain();
    }

    @Override
    public void setGain(int gain) {
        RepoAudio.setGain(gain);
    }

    @Override
    public float getAgchang() {
        return RepoAudio.getAgchang();
    }

    @Override
    public void setAgchang(float agchang) {
        RepoAudio.setAgchang(agchang);
    }

    @Override
    public void setFreq(float freq) {
        RepoAudio.setFreq(freq);
    }

    @Override
    public void setWidthStream(boolean vector) {
        RepoAudio.setWidthStream(vector);
    }

    @Override
    public float getVolume() {
        return RepoAudio.getVolume();
    }

    @Override
    public void setVolume(float volume) {
        RepoAudio.setVolume(volume);
    }

    @Override
    public boolean isAudioDepth() {
        return RepoAudio.isAudioDepth();
    }

    @Override
    public int getHighSampleRate() {
        return RepoAudio.SAMPLE_RATE;
    }

    @Override
    public int getLowSampleRate() {
        return RepoAudio.SAMPLE_RATE_LOW;
    }

    @Override
    public int getAudioFormate() {
        return RepoAudio.FORMAT;
    }

    @Override
    public int getAudioMask() {
        return RepoAudio.MASK;
    }

    @Override
    public int getAudioMode() {
        return RepoAudio.MODE;
    }

    @Override
    public int getAudioSessionId() {
        return RepoAudio.ID;
    }

    @Override
    public int getAudioBufferSize() {
        return RepoAudio.BUFFER_SIZE;
    }

    @Override
    public String getNotificationChannelId() {
        return RepoAudio.CHANNEL_ID;
    }

    @Override
    public void setBitmapArray(byte[] bitmapArray) {
        //Надо имплементировать!!!
    }

    @Override
    public void setRunning(boolean running) {
        RepoAudio.setRunning(running);
    }

    @Override
    public boolean isRunning() {
        return RepoAudio.isRunning();
    }
}