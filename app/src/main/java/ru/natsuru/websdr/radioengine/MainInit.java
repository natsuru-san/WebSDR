//Класс-обёртка; служит для упрощения запуска
//движка. Управление общением с сервером также
//производится при его помощи. Достаточно
//создать новый экземпляр этого класса ^_^
//Зависимость: com.neovisionaries:nv-websocket-client
//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;

import android.media.AudioTrack;

public class MainInit {
    private final AudioHolder audioHolder;
    private final FrameFetcher ff;
    public MainInit(AudioTrack audioTrack){
        audioHolder = new AudioHolder(audioTrack);
        ff = new FrameFetcher(this);
        ff.start();
    }
    public void setParams(double freq, int band, double minBorder, double maxBorder, int mode){
        ff.setParams(freq, band, minBorder, maxBorder, mode);
    }
    public void setAudioParams(int gain, int noisereduse, double agchang, int squelch, int autonotch){
        ff.setSoundParams(gain, noisereduse, agchang, squelch, autonotch);
    }
    public void prepareArr(byte[] binary){
        audioHolder.playOutput(binary);
    }
}