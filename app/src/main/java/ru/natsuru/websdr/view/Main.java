//Copyright by Natsuru-san

package ru.natsuru.websdr.view;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.dao.RunningService;
import ru.natsuru.websdr.model.Bucket;
import ru.natsuru.websdr.service.repository.RepoService;
import ru.natsuru.websdr.service.repository.RepoServiceImpl;
import ru.natsuru.websdr.service.storage.Store;
import ru.natsuru.websdr.service.storage.StoreImpl;
import ru.natsuru.websdr.model.MemoryCell;
import ru.natsuru.websdr.util.NullNode;
import ru.natsuru.websdr.ui.components.recycler.Recycler;
import ru.natsuru.websdr.ui.main.recycler.MemoryRecycler;

@SuppressWarnings("FieldCanBeLocal")
public class Main extends AppCompatActivity {

    private Store store;
    private RepoService repoService;
//    private Storage servers; //Для реализации на будущее :)
    private boolean settingsShow = false;
    private boolean asService = false;
    private String freqText;
    private Tuner tuner;
    private FragmentManager fragmentManager;
    private TextView currentFreq;
    private Button addToMemoryBtn;
    private Button restartBtn;
    private ImageButton openMemoryMenuBtn;
    private ImageButton closeMemoryMenuBtn;
    private ImageButton exitBtn;
    private ImageButton aboutBtn;
    private SwitchCompat asServiceBtn;
    private RecyclerView memoriesRV;
    private FrameLayout containerMemory;
    private List<MemoryCell> memories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = new StoreImpl(this);
//        servers = new ServersStorage(this);
        freqText = 0 + getString(R.string.khz);
        repoService = new RepoServiceImpl();
        setContentView(R.layout.main);
        initView();
        startRadioService();
        updateMemoriesList();
        getOptions();
        notation(); //Должна быть включена при релизе
    }
    //Объект-слушатель соединений со службами; получает экземпляр службы при биндинге
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Radio.ServiceGetter serviceGetter = (Radio.ServiceGetter) service;
            RunningService.setService(serviceGetter.getRadio());
            readyTuner();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener listener = v -> {
        switch (v.getId()){
            case R.id.OpenMemoryMenuBtn:
                containerMemory.setVisibility(View.VISIBLE);
                tuner.settingsShow(false);
                break;
            case R.id.CloseMemoryMenuBtn:
                containerMemory.setVisibility(View.INVISIBLE);
                break;
            case R.id.AddToMemBtn:
                addToMemory();
                break;
            case R.id.ExitBtn:
                RunningService.closeRadio();
                unbindService(serviceConnection);
                finish();
                break;
            case R.id.AboutBtn:
                Intent intent = new Intent(getApplicationContext(), About.class);
                startActivity(intent);
                break;
            case R.id.RestartBtn:
                RunningService.resetRadio();
                break;
        }
    };
    @SuppressLint("NonConstantResourceId")
    private final CompoundButton.OnCheckedChangeListener compoundListener = (buttonView, isChecked) -> {
        if (buttonView.getId() == R.id.AsServiceBtn) {
            asService = isChecked;
            setOptions();
        }
    };
    //Подготавливаем вьюшки
    private void initView(){
        //Ищем вьюшки
        currentFreq = findViewById(R.id.CurrentFreq);
        addToMemoryBtn = findViewById(R.id.AddToMemBtn);
        openMemoryMenuBtn = findViewById(R.id.OpenMemoryMenuBtn);
        closeMemoryMenuBtn = findViewById(R.id.CloseMemoryMenuBtn);
        aboutBtn = findViewById(R.id.AboutBtn);
        restartBtn = findViewById(R.id.RestartBtn);
        exitBtn = findViewById(R.id.ExitBtn);
        memoriesRV = findViewById(R.id.MemoriesRV);
        containerMemory = findViewById(R.id.ContainerMemory);
        asServiceBtn = findViewById(R.id.AsServiceBtn);
        //Настраиваем вьюшки
        currentFreq.setText(freqText);
        containerMemory.setVisibility(View.INVISIBLE);
        addToMemoryBtn.setOnClickListener(listener);
        openMemoryMenuBtn.setOnClickListener(listener);
        closeMemoryMenuBtn.setOnClickListener(listener);
        restartBtn.setOnClickListener(listener);
        aboutBtn.setOnClickListener(listener);
        exitBtn.setOnClickListener(listener);
        asServiceBtn.setOnCheckedChangeListener(compoundListener);
    }
    //Подготавливаем тюнер
    private void readyTuner(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ContainerTuner, tuner).commit();
        tuner.setMain(this);
    }
    //Запускаем службу радио
    private void startRadioService(){
        tuner = new Tuner();
        Intent start = new Intent(this, Radio.class);
        if(repoService.isRunning()){
            freqText = repoService.getCurrentStation().getFreq() + getString(R.string.khz);
            currentFreq.setText(freqText);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(start);
            }else{
                startService(start);
            }
        }
        bindService(start, serviceConnection, BIND_AUTO_CREATE);
    }
    @Override
    protected void onDestroy() {
        if(!asService){
            RunningService.closeRadio();
            finish();
        }
        super.onDestroy();
    }
    protected void setFreqView(){
        freqText = repoService.getCurrentStation().getFreq() + getString(R.string.khz);
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
            if(containerMemory.getVisibility() == View.VISIBLE){
                containerMemory.setVisibility(View.INVISIBLE);
            }else{
                if(!asService){
                    RunningService.closeRadio();
                    unbindService(serviceConnection);
                    finish();
                }
            }
        }
        super.onBackPressed();
    }
    private void notation(){
        Toast.makeText(this, getString(R.string.Notation), Toast.LENGTH_LONG).show();
    }
    private void updateMemoriesList() {
        memories = store.loadStations();
        updateAdapter();
    }
    protected void updateAdapter() {
        Recycler<Main, List<MemoryCell>, NullNode> recycler = new MemoryRecycler(this, this, memories, null);
        memoriesRV.setLayoutManager(new LinearLayoutManager(this));
        memoriesRV.setAdapter(recycler);
    }
    public void tuneFromMemory(@NonNull MemoryCell cell) {
        float freq = cell.getFreq();
        if(repoService.getPreviousFreq() > freq) {
            freq -= 19;
        }
        tuner.setMemory(freq, cell.getMinBorder(), cell.getMaxBorder(), cell.getMode());
        repoService.setPreviousFreq(freq);
    }

    public void deleteFromMemory(@NonNull MemoryCell cell) {
        List<MemoryCell> list = store.loadStations();
        list.remove(cell.getId());
        store.saveStations(list);
        updateMemoriesList();
    }
    private void addToMemory() {
        List<MemoryCell> list = store.loadStations();
        MemoryCell cell = repoService.getCurrentStation();
        cell.setId(list.size());
        list.add(cell);
        store.saveStations(list);
        updateMemoriesList();
    }
    private void setOptions() {
        List<Bucket<Boolean>> settingsList = new ArrayList<>();
        Bucket<Boolean> bucket = new Bucket<>("As Service" , asService);
        settingsList.add(bucket);
        store.saveSettings(settingsList);
    }
    private void getOptions() {
        if (store.loadSettings() != null) {
            asService = store.loadSettings().get(0).getValue();
        } else {
            asService = false;
        }
        asServiceBtn.setChecked(asService);
    }
}