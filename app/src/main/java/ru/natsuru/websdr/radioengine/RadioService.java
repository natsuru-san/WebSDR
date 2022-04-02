//Служба фоновой работы радио
//Нужна для работы в фоне; если работа в фоне не предполагается - используй MainInit
//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import ru.natsuru.websdr.R;

@SuppressWarnings("FieldCanBeLocal")
public class RadioService extends Service {
    private static boolean running = false;
    private static int modulationStatic;
    private static double minBorderStatic;
    private static double maxBorderStatic;
    private static double freqStatic;
    private static int noiseStateStatic;
    private static int squelchStateStatic;
    private static int autonotchStateStatic;
    private static int gainStatic;
    private static double agchangStatic;
    private static float volumeStatic;
    private static boolean audioModeStatic;
    private static int modeStatic;
    private static boolean codecStatic;
    private NotificationManager notificationManager;
    private Notification notification;
    private AudioTrack audioTrack;
    private MainInit mainInit;
    private final IBinder binder = new ServiceGetter();
    private final String CHANNEL_ID = "radio";
    private final int SAMPLE_RATE_LOW = 7119;
    private final int SAMPLE_RATE = SAMPLE_RATE_LOW * 2;
    private final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private final int MASK = AudioFormat.CHANNEL_OUT_MONO;
    private final int MODE = AudioTrack.MODE_STREAM;
    private final int ID = AudioManager.AUDIO_SESSION_ID_GENERATE;
    private final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, MASK, FORMAT) * 2;
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    //Начальная инициация
    @Override
    public void onCreate() {
        initRadio();
        initNotifications();
        running = true;
        super.onCreate();
    }
    //Независимая регулировка громкости
    public void setVolume(float volume){
        volumeStatic = volume;
        audioTrack.setVolume(volume);
    }
    @SuppressWarnings("SameParameterValue")
    public void sendParams(double freq, int band, double minBorder, double maxBorder, int mode){
        freqStatic = freq;
        minBorderStatic = minBorder;
        maxBorderStatic = maxBorder;
        modulationStatic = mode;
        mainInit.setParams(freq, band, minBorder, maxBorder, mode);
    }
    //Выбор частоты дискретизации
    public void setAudioMode(boolean audioMode){
        audioModeStatic = audioMode;
        if(audioMode){
            audioTrack.setPlaybackRate(SAMPLE_RATE_LOW);
        }else{
            audioTrack.setPlaybackRate(SAMPLE_RATE);
        }
    }
    //Передача аудиопараметров на сервер
    public void sendAudioParams(int gain, int noisereduse, double agchang, int squelch, int autonotch){
        gainStatic = gain;
        noiseStateStatic = noisereduse;
        agchangStatic = agchang;
        squelchStateStatic = squelch;
        autonotchStateStatic = autonotch;
        mainInit.setAudioParams(gain, noisereduse, agchang, squelch, autonotch);
    }
    //Закрытие службы
    public void closeRadio(){
        mainInit.closeSocket();
        notificationManager.cancelAll();
        running = false;
        stopSelf();
    }
    //Запуск нити с радио
    private void initRadio(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
        AudioAttributes.Builder aab = new AudioAttributes.Builder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            aab.setFlags(AudioAttributes.ALLOW_CAPTURE_BY_ALL);
        }
        aab.setFlags(AudioAttributes.CONTENT_TYPE_MUSIC);
        AudioFormat.Builder af = new AudioFormat.Builder();
        af.setEncoding(FORMAT);
        af.setChannelMask(MASK);
        audioTrack = new AudioTrack(aab.build(), af.build(), BUFFER_SIZE, MODE, ID);
        audioTrack.setPlaybackRate(SAMPLE_RATE);
        mainInit = new MainInit(audioTrack);
        mainInit.setDecoder(false);
    }
    //Запуск уведомления
    private void initNotifications(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, "Radio", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification);
        @SuppressLint("ResourceType") Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.ic_flag));
        view.setImageViewBitmap(R.id.Logo, bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_radio_mono)
                    .setOngoing(true)
                    .setCustomContentView(view)
                    .setContentText(getString(R.string.Annotation))
                    .setContentTitle(getString(R.string.Annotation))
                    .build();
        }else{
            notification = new Notification();
        }
        startForeground(Service.BIND_IMPORTANT, notification);
    }
    //Передача типа кодека
    public void setDecoder(boolean typeDecoder){
        mainInit.setDecoder(typeDecoder);
        codecStatic = typeDecoder;
    }
    //Хранение режима
    public void setMode(int mode){
        modeStatic = mode;
    }
    //Статические методы для выдачи параметров при восстановлении окна при запущенной службе
    public static int getModeStatic(){
        return modeStatic;
    }
    public static int getModulationStatic() {
        return modulationStatic;
    }
    public static double getMinBorderStatic() {
        return minBorderStatic;
    }
    public static double getMaxBorderStatic() {
        return maxBorderStatic;
    }
    public static double getFreqStatic() {
        return freqStatic;
    }
    public static int getNoiseStateStatic() {
        return noiseStateStatic;
    }
    public static int getSquelchStateStatic() {
        return squelchStateStatic;
    }
    public static int getAutonotchStateStatic() {
        return autonotchStateStatic;
    }
    public static int getGainStatic() {
        return gainStatic;
    }
    public static double getAgchangStatic() {
        return agchangStatic;
    }
    public static float getVolumeStatic() {
        return volumeStatic;
    }
    public static boolean getAudioModeStatic() {
        return audioModeStatic;
    }
    public static boolean isRunning(){
        return running;
    }
    public static boolean getCodecStatic(){
        return codecStatic;
    }
    //Вложенный класс для доступа к экземпляру службы
    public class ServiceGetter extends Binder{
        public RadioService getRadio(){
            return RadioService.this;
        }
    }
}