//Репозиторий состояний. Хранит текущие параметры движка

package ru.natsuru.websdr.dao.repository;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import ru.natsuru.websdr.model.Bucket;

public class RepoAudio {
    //Параметры соединения
    private static final String accept = "*/*";
    private static final String acceptEncoding = "gzip, deflate";
    private static final String cacheControl = "no-cache";
    private static final String connection = "keep-alive, Upgrade";
    private static final String cookie = "ID=61ad12a58ea4; view=2; usejava=nn";
    private static final String dnt = "1";
    private static final String host = "websdr.ewi.utwente.nl:8901";
    private static final String origin = "http://" + host;
    private static final String pragma = "no-cache";
    private static final String secWebSocketExtensions = "permessage-deflate";
    private static final String secWebSocketKey = "c23YG8a5cE5Dyoj2hdiLBQ==";
    private static final String secWebSocketVersion = "13";
    private static final String upgrade = "websocket";
    private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0 Safari/605.1.15";
    private static final String additional = "/~~stream";
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
    static final float MAX_BORDER_LIMIT = 6;
    static final float MIN_BORDER_LIMIT = -6;
    static final float MAX_BORDER_LIMIT_OUT = 0;
    static final float MIN_BORDER_LIMIT_OUT = 0;

    public static void setAudioDepth(boolean audioDepth) {
        RepoAudio.audioDepth = audioDepth;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        RepoAudio.running = running;
    }

    public static int getModulation() {
        return modulation;
    }

    public static void setModulation(int modulation) {
        RepoAudio.modulation = modulation;
    }

    public static float getMinBorder() {
        return minBorder;
    }

    public static void setMinBorder(float minBorder) {
        RepoAudio.minBorder = minBorder;
        Logic.verifyLimit();
        Logic.checkDepth();
    }

    public static float getMaxBorder() {
        return maxBorder;
    }

    public static void setMaxBorder(float maxBorder) {
        RepoAudio.maxBorder = maxBorder;
        Logic.verifyLimit();
        Logic.checkDepth();
    }

    public static float getFreq() {
        return freq;
    }

    public static void setFreq(float freq) {
        RepoAudio.freq = freq;
    }

    public static float getPreviousFreq() {
        return previousFreq;
    }

    public static void setPreviousFreq(float previousFreq) {
        RepoAudio.previousFreq = previousFreq;
    }

    public static int getNoise() {
        return noise;
    }

    public static void setNoise(int noise) {
        RepoAudio.noise = noise;
    }

    public static int getSquelch() {
        return squelch;
    }

    public static void setSquelch(int squelch) {
        RepoAudio.squelch = squelch;
    }

    public static int getAutonotch() {
        return autonotch;
    }

    public static void setAutonotch(int autonotch) {
        RepoAudio.autonotch = autonotch;
    }

    public static int getGain() {
        return gain;
    }

    public static void setGain(int gain) {
        RepoAudio.gain = gain;
    }

    public static float getAgchang() {
        return agchang;
    }

    public static void setAgchang(float agchang) {
        RepoAudio.agchang = agchang;
    }

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float volume) {
        RepoAudio.volume = volume;
    }

    public static boolean isaLaw() {
        return aLaw;
    }

    public static void setaLaw(boolean aLaw) {
        RepoAudio.aLaw = aLaw;
    }

    public static int getMode() {
        return mode;
    }

    public static boolean isAudioDepth() {
        return audioDepth;
    }

    public static List<Bucket<String>> getHeaders() {
        List<Bucket<String>> params = new ArrayList<>();
        params.add(new Bucket<>("Accept", accept));
        params.add(new Bucket<>("Accept-Encoding", acceptEncoding));
        params.add(new Bucket<>("Cache-Control", cacheControl));
        params.add(new Bucket<>("Connection", connection));
        params.add(new Bucket<>("Cookie", cookie));
        params.add(new Bucket<>("DNT", dnt));
        params.add(new Bucket<>("Host", host));
        params.add(new Bucket<>("Origin", origin));
        params.add(new Bucket<>("Pragma", pragma));
        params.add(new Bucket<>("Sec-WebSocket-Extensions", secWebSocketExtensions));
        params.add(new Bucket<>("Sec-WebSocket-Key", secWebSocketKey));
        params.add(new Bucket<>("Sec-WebSocket-Version", secWebSocketVersion));
        params.add(new Bucket<>("Upgrade", upgrade));
        params.add(new Bucket<>("User-Agent", userAgent));
        return params;
    }

    public static URI getUri() {
        return uri;
    }

    //Режим модуляции
    public static void setMode(int mode){
        RepoAudio.mode = mode;
        Logic.prepareMode();
        Logic.checkDepth();
    }

    public static void setWidthStream(boolean vector){
        Logic.widthStream(vector);
        Logic.verifyLimit();
        Logic.checkDepth();
    }
}
