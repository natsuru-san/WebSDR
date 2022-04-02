//Класс-UI для интерактивной настройки радио
//Copyright by Natsuru-san

package ru.natsuru.websdr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import ru.natsuru.websdr.radioengine.RadioService;

@SuppressWarnings("FieldCanBeLocal")
public class Tuner extends Fragment {
    private RadioService service;
    private Main main;
    private View view;
    private ImageButton hideBtn;
    private Button showBtn;
    private Button depth8kBtn;
    private Button depth16kBtn;
    private RadioGroup modulationGroup;
    private RadioGroup codecGroup;
    private RecyclerView tuneFreq;
    private ImageButton upFreqBtn;
    private ImageButton downFreqBtn;
    private CheckBox noiseReduction;
    private CheckBox squelch;
    private CheckBox autoNotch;
    private CheckBox autoGain;
    private SeekBar gain;
    private SeekBar agchang;
    private SeekBar volume;
    private FrameLayout settings;
    private TextView upBorder;
    private TextView downBorder;
    private TextView gainValueView;
    private TextView agchangValueView;
    private TextView volumeValueView;
    private TextView depthValueView;
    private int firstRun = 0;
    private int mode = 1;
    private int modulation = 0;
    private double minBorder = 4.5;
    private double maxBorder = 4.5 * -1;
    private double freq = 0;
    private double previousFreq = -1;
    private int noiseState = 0;
    private int squelchState = 0;
    private int autonotchState = 0;
    private int gainValue = 10000;
    private double agchangValue = 0;
    private float volumeValue = 100f;
    private final double MAX_BORDER_LIMIT = 6;
    private final double MIN_BORDER_LIMIT = -6;
    private final double MAX_BORDER_LIMIT_OUT = 0;
    private final double MIN_BORDER_LIMIT_OUT = 0;
    private boolean currentDepth = false;
    private boolean codec = false;
    //Переменные для восстановления параметров
    private boolean running = false;
    private int modulationStatic;
    private double minBorderStatic;
    private double maxBorderStatic;
    private double freqStatic;
    private int noiseStateStatic;
    private int squelchStateStatic;
    private int autonotchStateStatic;
    private int gainStatic;
    private double agchangStatic;
    private float volumeStatic;
    private boolean audioModeStatic;
    private int modeStatic;
    private boolean codecStatic;
    public Tuner() {
        if(RadioService.isRunning()){
            running = true;
            modulationStatic = RadioService.getModulationStatic();
            minBorderStatic = RadioService.getMinBorderStatic();
            maxBorderStatic = RadioService.getMaxBorderStatic();
            freqStatic = RadioService.getFreqStatic();
            noiseStateStatic = RadioService.getNoiseStateStatic();
            squelchStateStatic = RadioService.getSquelchStateStatic();
            autonotchStateStatic = RadioService.getAutonotchStateStatic();
            gainStatic = RadioService.getGainStatic();
            agchangStatic = RadioService.getAgchangStatic();
            volumeStatic = RadioService.getVolumeStatic();
            audioModeStatic = RadioService.getAudioModeStatic();
            modeStatic = RadioService.getModeStatic();
            codecStatic = RadioService.getCodecStatic();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tuner, container, false);
        initView(); //Ищем вьюшки
        prepareView(); //Цепляем на вьюшки слушателей
        startSetting(); //Подготавливаем вьюшки
        if(running){
            resumeTuning();
        }else{
            generateFreqInstances(); //Генерируем начальный диапазон частот
            sendAudioParams();
            setVolume();
        }
        return view;
    }
    //Возобновление работы при запущенной службе
    private void resumeTuning(){
        mode = modeStatic;
        modulation = modulationStatic;
        setMode();
        minBorder = minBorderStatic;
        maxBorder = maxBorderStatic;
        upBorder.setText(String.valueOf(maxBorder));
        downBorder.setText(String.valueOf(minBorder));
        volumeValue = volumeStatic * 100;
        volume.setProgress((int) volumeValue);
        noiseState = noiseStateStatic;
        squelchState = squelchStateStatic;
        autonotchState = autonotchStateStatic;
        noiseReduction.setChecked(noiseState == -100);
        squelch.setChecked(squelchState == 1);
        autoNotch.setChecked(autonotchState == 1);
        if(gainStatic == 10000){
            setAutogainViews(true);
        }else{
            autoGain.setChecked(false);
            setAutogainViews(false);
            agchangValue = agchangStatic;
            gainValue = gainStatic;
            agchang.setProgress((int) agchangValue);
            gain.setProgress(gainValue);
            agchangValueView.setText(String.valueOf(agchangValue));
            gainValueView.setText(String.valueOf(gainValue));
        }
        sendAudioParams();
        currentDepth = audioModeStatic;
        setDepth();
        codec = codecStatic;
        setDecoder();
        generateFreqInstances();
        tuneFreq.scrollToPosition((int) (29001 - freqStatic) - 10);
        freq = freqStatic;
        sendParams();
        sendAudioParams();
        setVolume();
    }
    //Слушатель радиокнопок
    @SuppressLint("NonConstantResourceId")
    private final RadioGroup.OnCheckedChangeListener changeListener = (group, checkedId) -> {
        switch (group.getCheckedRadioButtonId()){
            case R.id.FMButton:
                mode = 0;
                setMode();
                sendParams();
                break;
            case R.id.AMButton:
                mode = 1;
                setMode();
                sendParams();
                break;
            case R.id.LSBButton:
                mode = 2;
                setMode();
                sendParams();
                break;
            case R.id.USBButton:
                mode = 3;
                setMode();
                sendParams();
                break;
            case R.id.CWButton:
                mode = 4;
                setMode();
                sendParams();
                break;
            case R.id.AlawButton:
                codec = false;
                setDecoder();
                break;
            case R.id.UlawButton:
                codec = true;
                setDecoder();
                break;
        }
    };
    //Слушатель кнопок
    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener clickListener = v -> {
        switch (v.getId()){
            case R.id.UpFreqButton:
                setWidthChannel(true);
                sendParams();
                break;
            case R.id.DownFreqButton:
                setWidthChannel(false);
                sendParams();
                break;
            case R.id.HideBtn:
                settingsShow(false);
                break;
            case R.id.ShowBtn:
                settingsShow(true);
                break;
            case R.id.DepthBtn8K:
                currentDepth = true;
                setDepth();
                break;
            case R.id.DepthBtn16K:
                currentDepth = false;
                setDepth();
                break;
        }
    };
    //Слушатель галочек
    @SuppressLint("NonConstantResourceId")
    private final CompoundButton.OnCheckedChangeListener compoundListener = (buttonView, isChecked) -> {
        switch (buttonView.getId()){
            case R.id.NoiseBox:
                if (isChecked) {
                    noiseState = -100;
                } else {
                    noiseState = 0;
                }
                break;
            case R.id.SquelchBox:
                if (isChecked) {
                    squelchState = 1;
                } else {
                    squelchState = 0;
                }
                break;
            case R.id.AutogainBox:
                setAutogainViews(isChecked);
                break;
            case R.id.AutonotchBox:
                if (isChecked) {
                    autonotchState = 1;
                } else {
                    autonotchState = 0;
                }
                break;
        }
        sendAudioParams();
    };
    //Слушатель SeekBar
    private final SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @SuppressLint("NonConstantResourceId")
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()){
                case R.id.GainSeek:
                    gainValue = progress;
                    sendAudioParams();
                    break;
                case R.id.AgchangSeek:
                    agchangValue = progress;
                    sendAudioParams();
                    break;
                case R.id.VolumeSeek:
                    volumeValue = progress;
                    setVolume();
                    break;
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
    //Поиск вьюшек
    private void initView(){
        modulationGroup = view.findViewById(R.id.ModulationGroup);
        codecGroup = view.findViewById(R.id.CodecGroup);
        tuneFreq = view.findViewById(R.id.TuneFreq);
        upFreqBtn = view.findViewById(R.id.UpFreqButton);
        downFreqBtn = view.findViewById(R.id.DownFreqButton);
        depth8kBtn = view.findViewById(R.id.DepthBtn8K);
        depth16kBtn = view.findViewById(R.id.DepthBtn16K);
        depthValueView = view.findViewById(R.id.DepthValue);
        noiseReduction = view.findViewById(R.id.NoiseBox);
        squelch = view.findViewById(R.id.SquelchBox);
        autoNotch = view.findViewById(R.id.AutonotchBox);
        autoGain = view.findViewById(R.id.AutogainBox);
        gain = view.findViewById(R.id.GainSeek);
        agchang = view.findViewById(R.id.AgchangSeek);
        volume = view.findViewById(R.id.VolumeSeek);
        settings = view.findViewById(R.id.Settings);
        hideBtn = view.findViewById(R.id.HideBtn);
        showBtn = view.findViewById(R.id.ShowBtn);
        upBorder = view.findViewById(R.id.UpBorder);
        downBorder = view.findViewById(R.id.DownBorder);
        gainValueView = view.findViewById(R.id.GainValue);
        agchangValueView = view.findViewById(R.id.AgchangValue);
        volumeValueView = view.findViewById(R.id.VolumeValue);
    }
    //Установка слушателей
    private void prepareView(){
        modulationGroup.setOnCheckedChangeListener(changeListener);
        codecGroup.setOnCheckedChangeListener(changeListener);
        upFreqBtn.setOnClickListener(clickListener);
        downFreqBtn.setOnClickListener(clickListener);
        noiseReduction.setOnCheckedChangeListener(compoundListener);
        squelch.setOnCheckedChangeListener(compoundListener);
        autoNotch.setOnCheckedChangeListener(compoundListener);
        autoGain.setOnCheckedChangeListener(compoundListener);
        gain.setOnSeekBarChangeListener(seekListener);
        agchang.setOnSeekBarChangeListener(seekListener);
        volume.setOnSeekBarChangeListener(seekListener);
        hideBtn.setOnClickListener(clickListener);
        showBtn.setOnClickListener(clickListener);
        depth8kBtn.setOnClickListener(clickListener);
        depth16kBtn.setOnClickListener(clickListener);
    }
    //Настройка вьюшек
    private void startSetting(){
        modulationGroup.clearCheck();
        modulationGroup.check(R.id.AMButton);
        codecGroup.clearCheck();
        codecGroup.check(R.id.AlawButton);
        squelch.setChecked(false);
        autoNotch.setChecked(false);
        noiseReduction.setChecked(false);
        autoGain.setChecked(true);
        gain.setEnabled(false);
        agchang.setEnabled(false);
        volume.setEnabled(true);
        gainValueView.setText(getString(R.string.GainValue));
        agchangValueView.setText(getString(R.string.AgchangValue));
        volumeValueView.setText(getString(R.string.VolumeValue));
        settings.setVisibility(View.INVISIBLE);
        upBorder.setText(String.valueOf(maxBorder));
        downBorder.setText(String.valueOf(minBorder));
        depthValueView.setText(getString(R.string.Depth16k));
    }
    //Генерирование доступного диапазона частот для RecyclerView
    private void generateFreqInstances(){
        ArrayList<FreqStore> bands = new ArrayList<>();
        for(int i = 29000; i > -11; i--){
            bands.add(new FreqStore(i));
        }
        tuneFreq.setLayoutManager(new LinearLayoutManager(getContext()));
        tuneFreq.setAdapter(new FreqAdapter(getContext(), bands, this));
        tuneFreq.scrollToPosition(29001);
    }
    //Получение экземпляра MainActivity
    protected void setMain(Main main){
        this.main = main;
    }
    //Получение экземпляра RadioService
    protected void setService(RadioService service){
        this.service = service;
    }
    //Установка частоты из ресайклера; имеет костыль из конструкции if-else для решения бага с первыми холостыми 20-ю вызовами из ресайклера
    protected void setFreq(double freq){
        if(firstRun < 20){
            firstRun++;
            if(firstRun == 20){
                sendParams();
            }
        }else{
            this.freq = freq - 1;
            if(previousFreq > this.freq) {
                this.freq += 23;
            }
            previousFreq = this.freq;
            sendParams();
        }
    }
    //Отправка аудио параметров
    private void sendAudioParams(){
        service.sendAudioParams(gainValue, noiseState, agchangValue, squelchState, autonotchState);
        if(gain.isEnabled() && agchang.isEnabled()){
            gainValueView.setText(String.valueOf(gainValue));
            agchangValueView.setText(String.valueOf((int)agchangValue));
        }
    }
    //Независимая регулировка звука
    private void setVolume(){
        service.setVolume(volumeValue / 100f);
        volumeValueView.setText(String.valueOf((int)volumeValue));
    }
    //Отправка параметров серверу
    private void sendParams(){
        service.sendParams(freq, 0, minBorder, maxBorder, modulation);
        service.setMode(mode);
        main.setFreqView(freq);
    }
    //Установка частоты дискретизации; нужно, поскольку сервер отдаёт поток с разной частотой
    private void setDepth(){
        service.setAudioMode(currentDepth);
        if(currentDepth){
            depthValueView.setText(getString(R.string.Depth8k));
        }else{
            depthValueView.setText(getString(R.string.Depth16k));
        }
    }
    //Режим модуляции
    private void setMode(){
        switch (mode){
            case 0:
                modulation = 4;
                minBorder = -5;
                maxBorder = 5;
                break;
            case 1:
                modulation = 1;
                minBorder = -4.5;
                maxBorder = 4.5;
                break;
            case 2:
                modulation = 1;
                minBorder = -2.7;
                maxBorder = -0.3;
                break;
            case 3:
                modulation = 1;
                minBorder = 0.3;
                maxBorder = 2.7;
                break;
            case 4:
                modulation = 1;
                minBorder = -0.95;
                maxBorder = -0.55;
                break;
        }
        upBorder.setText(String.valueOf(maxBorder));
        downBorder.setText(String.valueOf(minBorder));
    }
    //Регулируем ширину канала
    private void setWidthChannel(boolean vector){
        switch (mode){
            case 0:
            case 1:
                if(vector){
                    minBorder = minBorder - 0.5;
                    maxBorder = maxBorder + 0.5;
                }else{
                    minBorder = minBorder + 0.5;
                    maxBorder = maxBorder - 0.5;
                }
                break;
            case 2:
                if(vector){
                    minBorder = minBorder - 0.1;
                }else{
                    minBorder = minBorder + 0.1;
                }
                break;
            case 3:
                if(vector){
                    maxBorder = maxBorder + 0.1;
                }else{
                    maxBorder = maxBorder - 0.1;
                }
                break;
            case 4:
                if(vector){
                    minBorder = minBorder - 0.05;
                }else{
                    minBorder = minBorder + 0.05;
                }
                break;
        }
        verifyLimit();
        upBorder.setText(String.valueOf(maxBorder));
        downBorder.setText(String.valueOf(minBorder));
    }
    //Проверка предела увеличения канала
    private void verifyLimit(){
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
    //Показ/сокрытие панели настроек
    protected void settingsShow(boolean show){
        if(show){
            if(settings.getVisibility() == View.INVISIBLE) {
                settings.setVisibility(View.VISIBLE);
            }
        }else{
            if(settings.getVisibility() == View.VISIBLE) {
                settings.setVisibility(View.INVISIBLE);
            }
        }
        main.setSettingsShow(show);
    }
    //Autogain флажок; настройка зависимых от него вьюшек
    private void setAutogainViews(boolean checked){
        if(checked){
            gain.setEnabled(false);
            agchang.setEnabled(false);
            gainValue = 10000;
            agchangValue = 0;
            agchangValueView.setText(getString(R.string.AgchangValue));
            gainValueView.setText(getString(R.string.GainValue));
        }else{
            gain.setEnabled(true);
            agchang.setEnabled(true);
            gainValue = 0;
            agchangValue = 0;
            gain.setProgress(gainValue);
            agchang.setProgress((int) agchangValue);
            agchangValueView.setText(String.valueOf(agchangValue));
            gainValueView.setText(String.valueOf(gainValue));
        }
    }
    //Декодер
    private void setDecoder(){
        service.setDecoder(codec);
    }
    //Внешний метод для установки сохранённых параметров канала из Main
    protected void setMemory(double freq, double minBorder, double maxBorder, int mode){
        this.mode = mode;
        setMode();
        this.minBorder = minBorder;
        this.maxBorder = maxBorder;
        upBorder.setText(String.valueOf(maxBorder));
        downBorder.setText(String.valueOf(minBorder));
        firstRun = 0;
        this.freq = freq;
        tuneFreq.scrollToPosition((int) (28991 - freq));
        modulationGroup.clearCheck();
        switch (mode){
            case 0:
                modulationGroup.check(R.id.FMButton);
                break;
            case 1:
                modulationGroup.check(R.id.AMButton);
                break;
            case 2:
                modulationGroup.check(R.id.LSBButton);
                break;
            case 3:
                modulationGroup.check(R.id.USBButton);
                break;
            case 4:
                modulationGroup.check(R.id.CWButton);
                break;
        }
        if(main.getLastFreq() > this.freq){
            this.freq += 19;
        }
        sendParams();
    }
    //Внешний метод для получения текущих параметров станции для Main
    protected JSONObject getMemory(){
        HashMap<String, String> station = new HashMap<>();
        station.put("Mode", String.valueOf(mode));
        station.put("MinBorder", String.valueOf(minBorder));
        station.put("MaxBorder", String.valueOf(maxBorder));
        station.put("Freq", String.valueOf(freq));
        return new JSONObject(station);
    }
}