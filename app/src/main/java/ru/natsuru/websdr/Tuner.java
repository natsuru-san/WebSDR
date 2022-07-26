//Класс-UI для интерактивной настройки радио
//Copyright by Natsuru-san

package ru.natsuru.websdr;

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
import java.util.List;

import ru.natsuru.websdr.radioengine.util.NullNode;
import ru.natsuru.websdr.radioengine.util.repo.Repository;
import ru.natsuru.websdr.service.RunningService;
import ru.natsuru.websdr.ui.components.abstractRecyclers.Recycler;
import ru.natsuru.websdr.ui.tuner.listener.TunerListenerButtons;
import ru.natsuru.websdr.ui.tuner.listener.TunerListenerChecks;
import ru.natsuru.websdr.ui.tuner.listener.TunerListenerCompounds;
import ru.natsuru.websdr.ui.tuner.listener.TunerListenerSeeks;
import ru.natsuru.websdr.ui.tuner.recycler.TunerRecycler;

@SuppressWarnings("FieldCanBeLocal")
public class Tuner extends Fragment {

    private Main main;
    private View view;
    private ImageButton hideBtn;
    private Button showBtn;
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
    private int firstRun = 0;
    private final View.OnClickListener clickListener = new TunerListenerButtons(this);
    private final RadioGroup.OnCheckedChangeListener changeListener = new TunerListenerChecks(this);
    private final SeekBar.OnSeekBarChangeListener seekListener = new TunerListenerSeeks(this);
    private final CompoundButton.OnCheckedChangeListener compoundListener = new TunerListenerCompounds(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tuner, container, false);
        initView(); //Ищем вьюшки
        prepareView(); //Цепляем на вьюшки слушателей
        startSetting(); //Подготавливаем вьюшки
        if (Repository.isRunning()) {
            resumeTuning();
        } else {
            generateFreqInstances(); //Генерируем начальный диапазон частот
            sendAudioParams();
            setVolume();
        }
        return view;
    }
    //Возобновление работы при запущенной службе
    private void resumeTuning(){
        setMode();
        upBorder.setText(String.valueOf(Repository.getMaxBorder()));
        downBorder.setText(String.valueOf(Repository.getMinBorder()));
        volume.setProgress((int) Repository.getVolume() * 100);
        noiseReduction.setChecked(Repository.getNoise() == -100);
        squelch.setChecked(Repository.getSquelch() == 1);
        autoNotch.setChecked(Repository.getAutonotch() == 1);
        if (Repository.getGain() == 10000) {
            setAutogainViews(true);
        } else {
            autoGain.setChecked(false);
            setAutogainViews(false);
            agchang.setProgress((int) Repository.getAgchang());
            gain.setProgress(Repository.getGain());
            agchangValueView.setText(String.valueOf(Repository.getAgchang()));
            gainValueView.setText(String.valueOf(Repository.getGain()));
        }
        sendAudioParams();
        RunningService.setAudioMode();
        generateFreqInstances();
        tuneFreq.scrollToPosition((int) (29001 - Repository.getFreq()) - 10);
        sendParams();
        sendAudioParams();
        setVolume();
    }

    //Поиск вьюшек
    private void initView() {
        modulationGroup = view.findViewById(R.id.ModulationGroup);
        codecGroup = view.findViewById(R.id.CodecGroup);
        tuneFreq = view.findViewById(R.id.TuneFreq);
        upFreqBtn = view.findViewById(R.id.UpFreqButton);
        downFreqBtn = view.findViewById(R.id.DownFreqButton);
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
    private void prepareView() {
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
    }
    //Настройка вьюшек
    private void startSetting() {
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
        upBorder.setText(String.valueOf(Repository.getMaxBorder()));
        downBorder.setText(String.valueOf(Repository.getMinBorder()));
    }
    //Генерирование доступного диапазона частот для RecyclerView
    private void generateFreqInstances() {
        List<Integer> listOfPrimitives = new ArrayList<>();
        for (int i = 29000; i > -11; i--) {
            listOfPrimitives.add(i);
        }
        Recycler<Tuner, List<Integer>, NullNode> listOfBands = new TunerRecycler(getContext(), this, listOfPrimitives, null);
        tuneFreq.setLayoutManager(new LinearLayoutManager(getContext()));
        tuneFreq.setAdapter(listOfBands);
        tuneFreq.scrollToPosition(29001);
    }
    //Получение экземпляра MainActivity
    protected void setMain(Main main){
        this.main = main;
    }

    //Установка частоты из ресайклера; имеет костыль из конструкции if-else для решения бага с первыми холостыми 20-ю вызовами из ресайклера
    public void setFreq() {
        if(firstRun < 20) {
            firstRun++;
            if(firstRun == 20) {
                sendParams();
            }
        } else {
            Repository.setFreq(Repository.getFreq() - 1);
            if(Repository.getPreviousFreq() > Repository.getFreq()) {
                Repository.setFreq(Repository.getFreq() + 23);
            }
            Repository.setPreviousFreq(Repository.getFreq());
            sendParams();
        }
    }
    //Отправка аудио параметров
    public void sendAudioParams() {
        RunningService.sendParams();
        if(gain.isEnabled() && agchang.isEnabled()){
            gainValueView.setText(String.valueOf(Repository.getGain()));
            agchangValueView.setText(String.valueOf((int) Repository.getAgchang()));
        }
    }
    //Независимая регулировка звука
    public void setVolume(){
        RunningService.setVolume();
        volumeValueView.setText(String.valueOf((int) Repository.getVolume()));
    }
    //Отправка параметров серверу
    public void sendParams(){
        RunningService.sendParams();
        main.setFreqView();
    }

    //Режим модуляции
    public void setMode(){
        upBorder.setText(String.valueOf(Repository.getMaxBorder()));
        downBorder.setText(String.valueOf(Repository.getMinBorder()));
        RunningService.setAudioMode();
    }
    //Регулируем ширину канала
    public void setWidthChannel(boolean vector) {
        Repository.setWidthStream(vector);
        upBorder.setText(String.valueOf(Repository.getMaxBorder()));
        downBorder.setText(String.valueOf(Repository.getMinBorder()));
        RunningService.setAudioMode();
    }

    //Показ/сокрытие панели настроек
    public void settingsShow(boolean show){
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
    public void setAutogainViews(boolean checked){
        if (checked) {
            gain.setEnabled(false);
            agchang.setEnabled(false);
            Repository.setGain(10000);
            Repository.setAgchang(0f);
            agchangValueView.setText(getString(R.string.AgchangValue));
            gainValueView.setText(getString(R.string.GainValue));
        } else {
            gain.setEnabled(true);
            agchang.setEnabled(true);
            Repository.setGain(0);
            Repository.setAgchang(0);
            gain.setProgress(Repository.getGain());
            agchang.setProgress((int) Repository.getAgchang());
            agchangValueView.setText(String.valueOf(Repository.getAgchang()));
            gainValueView.setText(String.valueOf(Repository.getGain()));
        }
    }
    //Внешний метод для установки сохранённых параметров канала из Main
    protected void setMemory(double freq, double minBorder, double maxBorder, int mode){
        setMode();
        upBorder.setText(String.valueOf(maxBorder));
        downBorder.setText(String.valueOf(minBorder));
        firstRun = 0;
        tuneFreq.scrollToPosition((int) (28991 - freq));
        modulationGroup.clearCheck();
        switch (mode) {
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
        if(main.getLastFreq() > Repository.getFreq()){
            Repository.setFreq(Repository.getFreq() + 19);
        }
        sendParams();
    }
}