//Служба фоновой работы радио
//Нужна для работы в фоне
//Copyright by Natsuru-san

package ru.natsuru.websdr.view;

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
import ru.natsuru.websdr.util.radioengine.catcher.AudioCatcher;
import ru.natsuru.websdr.util.radioengine.catcher.Catcher;
import ru.natsuru.websdr.util.radioengine.catcher.PictureCatcher;
import ru.natsuru.websdr.util.radioengine.linker.Linker;
import ru.natsuru.websdr.util.radioengine.linker.AudioLinker;
import ru.natsuru.websdr.util.radioengine.linker.PictureLinker;
import ru.natsuru.websdr.service.repository.RepoService;
import ru.natsuru.websdr.service.repository.RepoServiceImpl;

@SuppressWarnings("FieldCanBeLocal")
public class Radio extends Service {
    private NotificationManager notificationManager;
    private Notification notification;
    private AudioTrack audioTrack;
    private final IBinder binder = new ServiceGetter();
    private Linker<AudioTrack> audioTrackLinker;
    private Linker<Bitmap> bitmapLinker;
    private Timer timer;
    private RepoService repoService;
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    //Начальная инициация
    @Override
    public void onCreate() {
        repoService = new RepoServiceImpl();
        initAudio();
        initRadio();
        initNotify();
        repoService.setRunning(true);
        updateAudio();
        super.onCreate();
    }

    public void sendBitmapParams() {
        bitmapLinker.sendMessage();
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
        audioTrack.setVolume(repoService.getVolume() / 100f);
    }

    public void sendParams(){
        audioTrackLinker.sendMessage();
    }
    //Выбор частоты дискретизации
    public void setAudioMode(){
        if(repoService.isAudioDepth()){
            audioTrack.setPlaybackRate(repoService.getHighSampleRate());
        }else{
            audioTrack.setPlaybackRate(repoService.getLowSampleRate());
        }
    }
    //Закрытие службы
    public void closeRadio(){
        audioTrackLinker.close();
        bitmapLinker.close();
        notificationManager.cancelAll();
        repoService.setRunning(false);
        audioTrack.release();
        timer.cancel();
        timer.purge();
        stopSelf();
    }
    //Запуск нити с радио
    private void initRadio() {
        repoService = new RepoServiceImpl();
        Catcher<AudioTrack> audioCatcher = new AudioCatcher();
        audioCatcher.setRecordingObject(audioTrack);
        audioTrackLinker = new AudioLinker(audioCatcher, repoService.getCurrentRadioHeaders(), repoService.getCurrentRadioUri());
        audioTrackLinker.start();

        Catcher<Bitmap> bitmapCatcher = new PictureCatcher(repoService);
        bitmapLinker = new PictureLinker(bitmapCatcher, repoService.getCurrentWaterfallHeaders(), repoService.getCurrentWaterfallUri());
        bitmapLinker.start();
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
        af.setEncoding(repoService.getAudioFormate());
        af.setChannelMask(repoService.getAudioMask());
        audioTrack = new AudioTrack(aab.build(), af.build(), repoService.getAudioBufferSize(), repoService.getAudioMode(), repoService.getAudioSessionId());
        audioTrack.setPlaybackRate(repoService.getHighSampleRate());
        audioTrack.play();
    }
    //Запуск уведомления
    private void initNotify(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(repoService.getNotificationChannelId(), "Radio", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification);
        @SuppressLint("ResourceType") Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.ic_flag));
        view.setImageViewBitmap(R.id.Logo, bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, repoService.getNotificationChannelId())
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
        audioTrackLinker.close();
        bitmapLinker.close();
        initAudio();
        initRadio();
    }

    //Вложенный класс для доступа к экземпляру службы
    public class ServiceGetter extends Binder {
        public Radio getRadio(){
            return Radio.this;
        }
    }
}