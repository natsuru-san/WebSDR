//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;

import android.util.Log;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketListener;
import java.io.IOException;
import java.net.URISyntaxException;

@SuppressWarnings({"FieldCanBeLocal"})
public class FrameFetcher extends Thread implements Runnable{
    private final String streamKey = "c23YG8a5cE5Dyoj2hdiLBQ==";
    private final String baseUrl = "websdr.ewi.utwente.nl:8901";
    private final String streamAdditional = "/~~stream";
    private double freq = 1008.000;
    private int band = 0;
    private int mode = 1;
    private double maxBorder = (-4.5);
    private double minBorder = (4.5);
    private int gain = 10000;
    private int noisereduse = 0;
    private double agchang = 0.0;
    private int squelch = 0;
    private int autonotch = 0;
    private WebSocket streamWs;
    private WebSocketListener streamWsl;
    private final MainInit ai;
    public FrameFetcher(MainInit ai){
        this.ai = ai;
    }
    //Ставим частоты, тип модуляции и прочее
    protected void setParams(double freq, int band, double minBorder, double maxBorder, int mode){
        this.freq = freq;
        this.band = band;
        this.minBorder = minBorder;
        this.maxBorder = maxBorder;
        this.mode = mode;
        sendMessage();
    }
    //Регулируем параметры звука на стороне сервера
    protected void setSoundParams(int gain, int noisereduse, double agchang, int squelch, int autonotch){
        this.gain = gain;
        this.noisereduse = noisereduse;
        this.agchang = agchang;
        this.squelch = squelch;
        this.autonotch = autonotch;
        sendMessage();
    }
    private void init() throws IOException, URISyntaxException, WebSocketException {
        //Создаём подписчика на события веб-сокета
        streamWsl = new ListenerWS(ai).getListener();
        //Получаем сокеты
        streamWs = new InitWS(streamKey, baseUrl, streamAdditional, streamWsl).getWs();
        streamWs.connect();
        sendMessage();
    }
    //Отсылаем сообщение с параметрами серверу
    private void sendMessage(){
        streamWs.sendText("GET /~~param?f=" + freq + "&band=" + band + "&lo=" + minBorder + "&hi=" + maxBorder + "&mode=" + mode + "&name=");
        streamWs.sendText("GET /~~param?gain=" + gain);
        streamWs.sendText("GET /~~param?agchang=" + agchang);
        streamWs.sendText("GET /~~param?squelch=" + squelch);
        streamWs.sendText("GET /~~param?autonotch=" + autonotch);
        streamWs.sendText("GET /~~param?noisered=" + noisereduse);
    }
    //Запускаем нить...
    @Override
    public void run() {
        try {
            init();
        } catch (IOException | URISyntaxException | WebSocketException e) {
            e.printStackTrace();
            Log.d("Warn: ", e.toString());
        }
    }
}
