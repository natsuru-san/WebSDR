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
import java.util.ArrayList;

public class Tuner extends Fragment {
    private Main main;
    private View view;
    private ImageButton hideBtn;
    private Button showBtn;
    private RadioGroup modulationGroup;
    private RecyclerView tuneFreq;
    private ImageButton upFreqBtn;
    private ImageButton downFreqBtn;
    private CheckBox noiseReduction;
    private CheckBox squelch;
    private CheckBox autoNotch;
    private CheckBox autoGain;
    private SeekBar gain;
    private SeekBar agchang;
    private FrameLayout settings;
    private TextView upBorder;
    private TextView downBorder;
    private int mode = 1;
    private int modulation = 0;
    private double minBorder = 4.5;
    private double maxBorder = 4.5 * -1;
    private double freq = 0;
    private double previousFreq = 5;
    private int noiseState = 0;
    private int squelchState = 0;
    private int autonotchState = 0;
    private int gainValue = 10000;
    private double agchangValue = 0;
    private final double MAX_BORDER_LIMIT = 13;
    private final double MIN_BORDER_LIMIT = -13;
    private final double MAX_BORDER_LIMIT_OUT = 0;
    private final double MIN_BORDER_LIMIT_OUT = 0;
    public Tuner() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tuner, container, false);
        initView(); //Ищем вьюшки
        prepareView(); //Цепляем на вьюшки слушателей
        startSetting(); //Подготавливаем вьюшки
        generateFreqInstances(); //Генерируем диапазон частот
        return view;
    }
    //Слушатель радиокнопок
    @SuppressLint("NonConstantResourceId")
    private final RadioGroup.OnCheckedChangeListener changeListener = (group, checkedId) -> {
        switch (group.getCheckedRadioButtonId()){
            case R.id.FMButton:
                mode = 0;
                break;
            case R.id.AMButton:
                mode = 1;
                break;
            case R.id.LSBButton:
                mode = 2;
                break;
            case R.id.USBButton:
                mode = 3;
                break;
            case R.id.CWButton:
                mode = 4;
                break;
        }
        setMode();
        sendParams();
    };
    //Слушатель кнопок
    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener clickListener = v -> {
        switch (v.getId()){
            case R.id.UpFreqButton:
                setWidthChannel(true);
                break;
            case R.id.DownFreqButton:
                setWidthChannel(false);
                break;
            case R.id.HideBtn:
                if(settings.getVisibility() == View.VISIBLE) {
                    settings.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.ShowBtn:
                if(settings.getVisibility() == View.INVISIBLE) {
                    settings.setVisibility(View.VISIBLE);
                }
                break;
        }
        sendParams();
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
                if (isChecked) {
                    gain.setEnabled(false);
                    agchang.setEnabled(false);
                    gainValue = 10000;
                    agchangValue = 0;
                } else {
                    gain.setEnabled(true);
                    agchang.setEnabled(true);
                    gainValue = 0;
                    agchangValue = 0;
                    gain.setProgress(0);
                    agchang.setProgress(0);
                }
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
                    break;
                case R.id.AgchangSeek:
                    agchangValue = progress;
                    break;
            }
            sendAudioParams();
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
        tuneFreq = view.findViewById(R.id.TuneFreq);
        upFreqBtn = view.findViewById(R.id.UpFreqButton);
        downFreqBtn = view.findViewById(R.id.DownFreqButton);
        noiseReduction = view.findViewById(R.id.NoiseBox);
        squelch = view.findViewById(R.id.SquelchBox);
        autoNotch = view.findViewById(R.id.AutonotchBox);
        autoGain = view.findViewById(R.id.AutogainBox);
        gain = view.findViewById(R.id.GainSeek);
        agchang = view.findViewById(R.id.AgchangSeek);
        settings = view.findViewById(R.id.Settings);
        hideBtn = view.findViewById(R.id.HideBtn);
        showBtn = view.findViewById(R.id.ShowBtn);
        upBorder = view.findViewById(R.id.UpBorder);
        downBorder = view.findViewById(R.id.DownBorder);
    }
    //Установка слушателей
    private void prepareView(){
        modulationGroup.setOnCheckedChangeListener(changeListener);
        upFreqBtn.setOnClickListener(clickListener);
        downFreqBtn.setOnClickListener(clickListener);
        noiseReduction.setOnCheckedChangeListener(compoundListener);
        squelch.setOnCheckedChangeListener(compoundListener);
        autoNotch.setOnCheckedChangeListener(compoundListener);
        autoGain.setOnCheckedChangeListener(compoundListener);
        gain.setOnSeekBarChangeListener(seekListener);
        agchang.setOnSeekBarChangeListener(seekListener);
        hideBtn.setOnClickListener(clickListener);
        showBtn.setOnClickListener(clickListener);
        upBorder.setText(String.valueOf(maxBorder));
        downBorder.setText(String.valueOf(minBorder));
    }
    //Настройка вьюшек
    private void startSetting(){
        modulationGroup.clearCheck();
        modulationGroup.check(R.id.AMButton);
        squelch.setChecked(false);
        autoNotch.setChecked(false);
        noiseReduction.setChecked(false);
        autoGain.setChecked(true);
        gain.setEnabled(false);
        agchang.setEnabled(false);
        settings.setVisibility(View.INVISIBLE);
    }
    //Генерирование доступного диапазона частот для RecyclerView
    private void generateFreqInstances(){
        ArrayList<FreqStore> bands = new ArrayList<>();
        for(int i = 29000; i > -11; i--){
            bands.add(new FreqStore((double) i));
        }
        tuneFreq.setLayoutManager(new LinearLayoutManager(getContext()));
        tuneFreq.setAdapter(new FreqAdapter(getContext(), bands, this));
        tuneFreq.scrollToPosition(29001);
    }
    //Получение экземпляра MainActivity
    protected void setMain(Main main){
        this.main = main;
    }
    //Установка частоты
    protected void setFreq(double freq){
        this.freq = freq - 1;
        if(previousFreq > this.freq) {
            this.freq += 23;
        }
        previousFreq = this.freq;
        sendParams();
    }
    //Отправка аудио параметров
    private void sendAudioParams(){
        main.sendAudioParams(gainValue, noiseState, agchangValue, squelchState, autonotchState);
    }
    //Отправка параметров серверу
    private void sendParams(){
        main.sendParams(freq, 0, minBorder, maxBorder, modulation);
    }
    //Режим модуляции
    private void setMode(){
        switch (mode){
            case 0:
                modulation = 4;
                minBorder = -5;
                maxBorder = 5;
                main.setAudioMode(true);
                break;
            case 1:
                modulation = 1;
                minBorder = -4.5;
                maxBorder = 4.5;
                main.setAudioMode(false);
                break;
            case 2:
                modulation = 1;
                minBorder = -2.7;
                maxBorder = -0.3;
                main.setAudioMode(true);
                break;
            case 3:
                modulation = 1;
                minBorder = 0.3;
                maxBorder = 2.7;
                main.setAudioMode(true);
                break;
            case 4:
                modulation = 1;
                minBorder = -0.95;
                maxBorder = -0.55;
                main.setAudioMode(true);
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
}