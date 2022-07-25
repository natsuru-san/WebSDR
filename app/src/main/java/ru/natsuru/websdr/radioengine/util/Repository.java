//Репозиторий состояний. Хранит текущие параметры движка

package ru.natsuru.websdr.radioengine.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    //Параметры соединения
    private static String accept = "*/*";
    private static String acceptEncoding = "gzip, deflate";
    private static String cacheControl = "no-cache";
    private static String connection = "keep-alive, Upgrade";
    private static String cookie = "ID=61ad12a58ea4; view=2; usejava=nn";
    private static String dnt = "1";
    private static String host = "websdr.ewi.utwente.nl:8901";
    private static String origin = "http://" + host;
    private static String pragma = "no-cache";
    private static String secWebSocketExtensions = "permessage-deflate";
    private static String secWebSocketKey = "c23YG8a5cE5Dyoj2hdiLBQ==";
    private static String secWebSocketVersion = "13";
    private static String upgrade = "websocket";
    private static String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0 Safari/605.1.15";
    private static String additional = "/~~stream";
    private static URI uri;
    static {
        try {
            uri = new URI("ws://" + host + additional);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //Настройки сессии
    private static boolean running = false;
    private static boolean aLaw = true;
    private static boolean audioDepth = true;
    private static int mode = 0;
    private static int modulation = 0;
    private static float minBorder = 4.5f;
    private static float maxBorder = -4.5f;
    private static float freq = 0f;
    private static float previousFreq = -1f;
    private static int noise = 0;
    private static int squelch = 0;
    private static int autonotch = 0;
    private static int gain = 10000;
    private static float agchang = 0;
    private static float volume = 100f;

    public static final String CHANNEL_ID = "Ran radio";
    public static final int FACTOR = 81; //Поправочная константа
    public static final int SAMPLE_RATE_LOW = 7119 + FACTOR;
    public static final int SAMPLE_RATE = SAMPLE_RATE_LOW * 2;
    public static final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static final int MASK = AudioFormat.CHANNEL_OUT_MONO;
    public static final int MODE = AudioTrack.MODE_STREAM;
    public static final int ID = AudioManager.AUDIO_SESSION_ID_GENERATE;
    public static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, MASK, FORMAT) * 2;

    //Лимиты ширины принимаемого потока
    private static final float MAX_BORDER_LIMIT = 6;
    private static final float MIN_BORDER_LIMIT = -6;
    private static final float MAX_BORDER_LIMIT_OUT = 0;
    private static final float MIN_BORDER_LIMIT_OUT = 0;

    public static void setAccept(String accept) {
        Repository.accept = accept;
    }

    public static void setAcceptEncoding(String acceptEncoding) {
        Repository.acceptEncoding = acceptEncoding;
    }

    public static void setCacheControl(String cacheControl) {
        Repository.cacheControl = cacheControl;
    }

    public static void setConnection(String connection) {
        Repository.connection = connection;
    }

    public static void setCookie(String cookie) {
        Repository.cookie = cookie;
    }

    public static void setDnt(String dnt) {
        Repository.dnt = dnt;
    }

    public static void setHost(String host) {
        Repository.host = host;
    }

    public static void setOrigin(String origin) {
        Repository.origin = origin;
    }

    public static void setPragma(String pragma) {
        Repository.pragma = pragma;
    }

    public static void setSecWebSocketExtensions(String secWebSocketExtensions) {
        Repository.secWebSocketExtensions = secWebSocketExtensions;
    }

    public static void setSecWebSocketKey(String secWebSocketKey) {
        Repository.secWebSocketKey = secWebSocketKey;
    }

    public static void setSecWebSocketVersion(String secWebSocketVersion) {
        Repository.secWebSocketVersion = secWebSocketVersion;
    }

    public static void setUpgrade(String upgrade) {
        Repository.upgrade = upgrade;
    }

    public static void setUserAgent(String userAgent) {
        Repository.userAgent = userAgent;
    }

    public static void setAdditional(String additional) {
        Repository.additional = additional;
    }

    public static void setUri(URI uri) {
        Repository.uri = uri;
    }

    public static void setAudioDepth(boolean audioDepth) {
        Repository.audioDepth = audioDepth;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Repository.running = running;
    }

    public static int getModulation() {
        return modulation;
    }

    public static void setModulation(int modulation) {
        Repository.modulation = modulation;
    }

    public static float getMinBorder() {
        return minBorder;
    }

    public static void setMinBorder(float minBorder) {
        Repository.minBorder = minBorder;
        verifyLimit();
        checkDepth();
    }

    public static float getMaxBorder() {
        return maxBorder;
    }

    public static void setMaxBorder(float maxBorder) {
        Repository.maxBorder = maxBorder;
        verifyLimit();
        checkDepth();
    }

    public static float getFreq() {
        return freq;
    }

    public static void setFreq(float freq) {
        Repository.freq = freq;
    }

    public static float getPreviousFreq() {
        return previousFreq;
    }

    public static void setPreviousFreq(float previousFreq) {
        Repository.previousFreq = previousFreq;
    }

    public static int getNoise() {
        return noise;
    }

    public static void setNoise(int noise) {
        Repository.noise = noise;
    }

    public static int getSquelch() {
        return squelch;
    }

    public static void setSquelch(int squelch) {
        Repository.squelch = squelch;
    }

    public static int getAutonotch() {
        return autonotch;
    }

    public static void setAutonotch(int autonotch) {
        Repository.autonotch = autonotch;
    }

    public static int getGain() {
        return gain;
    }

    public static void setGain(int gain) {
        Repository.gain = gain;
    }

    public static double getAgchang() {
        return agchang;
    }

    public static void setAgchang(float agchang) {
        Repository.agchang = agchang;
    }

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float volume) {
        Repository.volume = volume;
    }

    public static boolean isaLaw() {
        return aLaw;
    }

    public static void setaLaw(boolean aLaw) {
        Repository.aLaw = aLaw;
    }

    public static int getMode() {
        return mode;
    }

    public static boolean isAudioDepth() {
        return audioDepth;
    }

    public static List<Bucket> getHeaders() {
        List<Bucket> params = new ArrayList<>();
        params.add(new Bucket("Accept", accept));
        params.add(new Bucket("Accept-Encoding", acceptEncoding));
        params.add(new Bucket("Cache-Control", cacheControl));
        params.add(new Bucket("Connection", connection));
        params.add(new Bucket("Cookie", cookie));
        params.add(new Bucket("DNT", dnt));
        params.add(new Bucket("Host", host));
        params.add(new Bucket("Origin", origin));
        params.add(new Bucket("Pragma", pragma));
        params.add(new Bucket("Sec-WebSocket-Extensions", secWebSocketExtensions));
        params.add(new Bucket("Sec-WebSocket-Key", secWebSocketKey));
        params.add(new Bucket("Sec-WebSocket-Version", secWebSocketVersion));
        params.add(new Bucket("Upgrade", upgrade));
        params.add(new Bucket("User-Agent", userAgent));
        return params;
    }

    public static URI getUri() {
        return uri;
    }

    //Режим модуляции
    public static void setMode(int mode){
        Repository.mode = mode;
        switch (mode){
            case 0:
                modulation = 4;
                minBorder = -5f;
                maxBorder = 5f;
                audioDepth = false;
                break;
            case 1:
                modulation = 1;
                minBorder = -4.5f;
                maxBorder = 4.5f;
                audioDepth = true;
                break;
            case 2:
                modulation = 1;
                minBorder = -2.7f;
                maxBorder = -0.3f;
                audioDepth = true;
                break;
            case 3:
                modulation = 1;
                minBorder = 0.3f;
                maxBorder = 2.7f;
                audioDepth = true;
                break;
            case 4:
                modulation = 1;
                minBorder = -0.95f;
                maxBorder = -0.55f;
                audioDepth = false;
                break;
        }
        checkDepth();
    }

    private static void checkDepth() {
        if (mode == 0) {
            audioDepth = false;
        } else {
            audioDepth = !(minBorder > -4.0f) && !(maxBorder < 4.0f);
        }
    }

    //Проверка предела увеличения канала
    private static void verifyLimit(){
        if(maxBorder > MAX_BORDER_LIMIT){
            maxBorder = MAX_BORDER_LIMIT;
        }
        if(maxBorder < MAX_BORDER_LIMIT_OUT){
            maxBorder = MAX_BORDER_LIMIT_OUT;
        }
        if(minBorder < MIN_BORDER_LIMIT){
            minBorder = MIN_BORDER_LIMIT;
        }
        if(minBorder > MIN_BORDER_LIMIT_OUT){
            minBorder = MIN_BORDER_LIMIT_OUT;
        }
    }

    public static void setWidthAStream(boolean vector){
        switch (mode){
            case 0:
            case 1:
                if(vector){
                    minBorder = minBorder - 0.5f;
                    maxBorder = maxBorder + 0.5f;
                }else{
                    minBorder = minBorder + 0.5f;
                    maxBorder = maxBorder - 0.5f;
                }
                break;
            case 2:
                if(vector){
                    minBorder = minBorder - 0.1f;
                }else{
                    minBorder = minBorder + 0.1f;
                }
                break;
            case 3:
                if(vector){
                    maxBorder = maxBorder + 0.1f;
                }else{
                    maxBorder = maxBorder - 0.1f;
                }
                break;
            case 4:
                if(vector){
                    minBorder = minBorder - 0.05f;
                }else{
                    minBorder = minBorder + 0.05f;
                }
                break;
        }
        verifyLimit();
        checkDepth();
    }
}
