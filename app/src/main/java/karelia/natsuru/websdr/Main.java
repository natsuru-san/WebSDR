//Copyright by Natsuru-san

package karelia.natsuru.websdr;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import karelia.natsuru.websdr.data.entity.Station;
import karelia.natsuru.websdr.data.service.DataService;
import karelia.natsuru.websdr.data.service.DataServiceImpl;
import karelia.natsuru.websdr.radioengine.RadioService;

@SuppressWarnings("FieldCanBeLocal")
public class Main extends AppCompatActivity {

    private boolean settingsShow = false;
    private boolean asService = false;
    private BigDecimal lastFreq = BigDecimal.ZERO;
    private DataService dataService;
    private Storage storage;
    private RadioService radioService;
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
    @SuppressWarnings("FieldMayBeFinal")
    private List<Station> memories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        freqText = 0 + getString(R.string.khz);
        setContentView(R.layout.main);
        initView();
        startRadioService();
        try {
            updateMemoriesList();
            getOptions();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
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
                try {
                    addToMemory();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ExitBtn:
                radioService.closeRadio();
                unbindService(serviceConnection);
                finish();
                break;
            case R.id.AboutBtn:
                Intent intent = new Intent(getApplicationContext(), About.class);
                startActivity(intent);
                break;
            case R.id.RestartBtn:
                radioService.closeRadio();
                unbindService(serviceConnection);
                Intent start = new Intent(this, RadioService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(start);
                }else{
                    startService(start);
                }
                bindService(start, serviceConnection, BIND_AUTO_CREATE);
                break;
        }
    };
    @SuppressLint("NonConstantResourceId")
    private final CompoundButton.OnCheckedChangeListener compoundListener = (buttonView, isChecked) -> {
        if (buttonView.getId() == R.id.AsServiceBtn) {
            asService = isChecked;
            try {
                setOptions();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
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
        tuner.setService(radioService);
    }
    //Запускаем службу радио
    private void startRadioService(){
        tuner = new Tuner();
        Intent start = new Intent(this, RadioService.class);
        if(RadioService.isRunning()){
            freqText = RadioService.getFreqStatic() + getString(R.string.khz);
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
            radioService.closeRadio();
            finish();
        }
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
            if(containerMemory.getVisibility() == View.VISIBLE){
                containerMemory.setVisibility(View.INVISIBLE);
            }else{
                if(!asService){
                    radioService.closeRadio();
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
    private void updateMemoriesList() throws IOException, JSONException {
        dataService = new DataServiceImpl(this);
        memories = dataService.getAllStations();
        updateAdapter();
    }
    protected void updateAdapter() {
        memoriesRV.setLayoutManager(new LinearLayoutManager(this));
        memoriesRV.setAdapter(new MemoryAdapter(this, memories, this));
    }
    protected void tuneFromMemory(@NonNull Station cell) {
        var freq = cell.getFreq();
        if(lastFreq.compareTo(freq) > 0) {
            freq = freq.subtract(new BigDecimal("19"));
        }
        tuner.setMemory(freq, cell.getMinBorder(), cell.getMaxBorder(), cell.getMode());
        lastFreq = freq;
    }
    protected BigDecimal getLastFreq(){
        return lastFreq;
    }
    protected void deleteFromMemory(@NonNull MemoryCell cell){
        try {
            JSONArray stations = storage.load(false);
            stations.remove(cell.getId());
            storage.save(stations, false);
            updateMemoriesList();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void addToMemory() throws IOException, JSONException {
        JSONArray stations = storage.load(false);
        stations.put(tuner.getMemory());
        storage.save(stations, false);
        updateMemoriesList();
    }
    private void setOptions() throws JSONException, IOException {
        JSONObject jObject = new JSONObject();
        jObject.put("AsService", asService);
        JSONArray options = storage.load(true);
        options.remove(0);
        options.put(0, jObject);
        storage.save(options, true);
    }
    private void getOptions() throws JSONException, IOException {
        JSONArray options = storage.load(true);
        JSONObject jObject = options.getJSONObject(0);
        asService = jObject.getBoolean("AsService");
        asServiceBtn.setChecked(asService);
    }
}