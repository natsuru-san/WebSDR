//Copyright by Natsuru-san

package ru.natsuru.websdr;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import ru.natsuru.websdr.radioengine.RadioService;

@SuppressWarnings("FieldCanBeLocal")
public class Main extends AppCompatActivity {
    private RadioService radioService;
    private boolean settingsShow = false;
    private String freqText;
    private Tuner tuner;
    private FragmentManager fragmentManager;
    private TextView currentFreq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        startRadioService();
        notation(); //Должна быть включена при релизе
    }
    //Объект-слушатель соединений со службами; получает экземпляр службы при биндинге
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RadioService.ServiceGetter serviceGetter = (RadioService.ServiceGetter) service;
            radioService = serviceGetter.getRadio();
            readyTuner();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    //Подготавливаем вьюшки
    private void initView(){
        freqText = 0 + getString(R.string.khz);
        currentFreq = findViewById(R.id.CurrentFreq);
        currentFreq.setText(freqText);
        tuner = new Tuner();
    }
    //Подготавливаем тюнер
    private void readyTuner(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Container, tuner).commit();
        tuner.setMain(this);
        tuner.setService(radioService);
    }
    //Запускаем службу радио
    private void startRadioService(){
        Intent start = new Intent(this, RadioService.class);
        if(RadioService.isRunning()){
            freqText = RadioService.getFreqStatic() + getString(R.string.khz);
            currentFreq.setText(freqText);
        }else{
            startForegroundService(start);
        }
        bindService(start, serviceConnection, BIND_AUTO_CREATE);
    }
    @Override
    protected void onDestroy() {
        /*
        radioService.closeRadio();
        unbindService(serviceConnection);
        finish();
        */
        super.onDestroy();
    }
    protected void setFreqView(double freq){
        freqText = freq + getString(R.string.khz);
        currentFreq.setText(freqText);
    }
    protected void setSettingsShow(boolean settingsShow){
        this.settingsShow = settingsShow;
    }
    @Override
    public void onBackPressed() {
        if(settingsShow){
            tuner.settingsShow(false);
        }else{
            radioService.closeRadio();
            unbindService(serviceConnection);
            finish();
        }
    }
    private void notation(){
        Toast.makeText(this, getString(R.string.Notation), Toast.LENGTH_LONG).show();
    }
}