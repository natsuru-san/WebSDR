package ru.natsuru.websdr.service.repository;

import java.net.URI;
import java.util.List;
import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.model.MemoryCell;

public interface RepoService {
    MemoryCell getCurrentStation();
    List<Bucket<String>> getCurrentRadioHeaders();
    URI getCurrentRadioUri();
    boolean isAlaw();
    void setAlaw(boolean alaw);
    void setRadioMode(int mode);
    float getPreviousFreq();
    void setPreviousFreq(float previousFreq);
    int getNoise();
    void setNoise(int noise);
    int getSquelch();
    void setSquelch(int squelch);
    int getAutonotch();
    void setAutonotch(int autonotch);
    int getGain();
    void setGain(int gain);
    float getAgchang();
    void setAgchang(float agchang);
    void setFreq(float freq);
    void setWidthStream(boolean vector);

    List<Bucket<String>> getCurrentWaterfallHeaders();
    URI getCurrentWaterfallUri();

    void setRunning(boolean running);
    boolean isRunning();
    float getVolume();
    void setVolume(float volume);
    boolean isAudioDepth();
    int getHighSampleRate();
    int getLowSampleRate();
    int getAudioFormate();
    int getAudioMask();
    int getAudioMode();
    int getAudioSessionId();
    int getAudioBufferSize();
    String getNotificationChannelId();
    void setBitmapArray(byte[] bitmapArray);
}
