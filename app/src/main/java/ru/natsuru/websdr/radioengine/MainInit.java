//Класс-обёртка; служит для упрощения запуска
//движка. Управление общением с сервером также
//производится при его помощи. Достаточно
//создать новый экземпляр этого класса ^_^
//Зависимость пакета: com.neovisionaries:nv-websocket-client
//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;

import android.media.AudioTrack;

public class MainInit {
    private final Decoder decoder;
    private final FrameFetcher ff;
    public MainInit(AudioTrack audioTrack){
        audioTrack.play();
        decoder = new Decoder(audioTrack);
        ff = new FrameFetcher(this);
        ff.start();
    }
    protected void paused(){
        decoder.paused();
    }
    protected void setParams(double freq, int band, double minBorder, double maxBorder, int mode){
        ff.setParams(freq, band, minBorder, maxBorder, mode);
    }
    protected void setAudioParams(int gain, int noisereduse, double agchang, int squelch, int autonotch){
        ff.setSoundParams(gain, noisereduse, agchang, squelch, autonotch);
    }
    protected void prepareArr(byte[] binary){
        decoder.playOutput(binary);
    }
    @SuppressWarnings("SameParameterValue")
    protected void setDecoder(boolean decoderType){
        decoder.setDecoder(decoderType);
    }
    protected void closeSocket(){
        ff.closeSocket();
        try {
            ff.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected void setAudioTrack(AudioTrack audioTrack){
        decoder.setAudioTrack(audioTrack);
    }
}