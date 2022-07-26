//Служба фоновой работы радио
//Нужна для работы в фоне
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
import java.util.Timer;
import java.util.TimerTask;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.radioengine.catcher.AudioCatcher;
import ru.natsuru.websdr.radioengine.catcher.Catcher;
import ru.natsuru.websdr.radioengine.linker.Linker;
import ru.natsuru.websdr.radioengine.linker.AudioLinker;
import ru.natsuru.websdr.radioengine.util.repo.Repository;

@SuppressWarnings("FieldCanBeLocal")
public class RadioService extends Service {
    private NotificationManager notificationManager;
    private Notification notification;
    private AudioTrack audioTrack;
    private final IBinder binder = new ServiceGetter();
    private Linker<AudioTrack> linker;
    private Timer timer;
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    //Начальная инициация
    @Override
    public void onCreate() {
        initAudio();
        initRadio();
        initNotify();
        Repository.setRunning(true);
        updateAudio();
        super.onCreate();
    }

    //Задача по таймеру
    private void updateAudio(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                resetRadio();
            }
        }, 300000, 300000);
    }
    //Независимая регулировка громкости
    public void setVolume(){
        audioTrack.setVolume(Repository.getVolume() / 100f);
    }
    @SuppressWarnings("SameParameterValue")
    public void sendParams(){
        linker.sendMessage();
    }
    //Выбор частоты дискретизации
    public void setAudioMode(){
        if(Repository.isAudioDepth()){
            audioTrack.setPlaybackRate(Repository.SAMPLE_RATE);
        }else{
            audioTrack.setPlaybackRate(Repository.SAMPLE_RATE_LOW);
        }
    }
    //Закрытие службы
    public void closeRadio(){
        linker.close();
        notificationManager.cancelAll();
        Repository.setRunning(false);
        audioTrack.release();
        timer.cancel();
        timer.purge();
        stopSelf();
    }
    //Запуск нити с радио
    private void initRadio(){
        Catcher<AudioTrack> catcher = new AudioCatcher();
        catcher.setRecordingObject(audioTrack);
        linker = new AudioLinker(catcher);
        linker.start();
    }
    //Подготовка аудиотрека
    private void initAudio(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
        AudioAttributes.Builder aab = new AudioAttributes.Builder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            aab.setFlags(AudioAttributes.ALLOW_CAPTURE_BY_ALL);
        }
        aab.setFlags(AudioAttributes.CONTENT_TYPE_MUSIC);
        AudioFormat.Builder af = new AudioFormat.Builder();
        af.setEncoding(Repository.FORMAT);
        af.setChannelMask(Repository.MASK);
        audioTrack = new AudioTrack(aab.build(), af.build(), Repository.BUFFER_SIZE, Repository.MODE, Repository.ID);
        audioTrack.setPlaybackRate(Repository.SAMPLE_RATE);
        audioTrack.play();
    }
    //Запуск уведомления
    private void initNotify(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(Repository.CHANNEL_ID, "Radio", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification);
        @SuppressLint("ResourceType") Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.ic_flag));
        view.setImageViewBitmap(R.id.Logo, bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, Repository.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_radio_mono)
                    .setOngoing(true)
                    .setCustomContentView(view)
                    .setContentText(getString(R.string.Annotation))
                    .setContentTitle(getString(R.string.Annotation))
                    .build();
        } else {
            notification = new Notification();
        }
        startForeground(Service.BIND_IMPORTANT, notification);
    }

    public void resetRadio(){
        linker.close();
        initAudio();
        initRadio();
    }

    //Вложенный класс для доступа к экземпляру службы
    public class ServiceGetter extends Binder {
        public RadioService getRadio(){
            return RadioService.this;
        }
    }
}