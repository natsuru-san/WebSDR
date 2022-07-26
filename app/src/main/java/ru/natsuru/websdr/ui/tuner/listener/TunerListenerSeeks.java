package ru.natsuru.websdr.ui.tuner.listener;

import android.annotation.SuppressLint;
import android.widget.SeekBar;

import ru.natsuru.websdr.R;
import ru.natsuru.websdr.Tuner;
import ru.natsuru.websdr.radioengine.util.repo.Repository;
import ru.natsuru.websdr.ui.components.abstractListeners.AbstractSeekbarListener;

public class TunerListenerSeeks extends AbstractSeekbarListener<Tuner> {
    public TunerListenerSeeks(Tuner layoutClass) {
        super(layoutClass);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void shifted(SeekBar seekBar, int progress) {
        switch (seekBar.getId()){
            case R.id.GainSeek:
                Repository.setGain(progress);
                getLayoutClass().sendAudioParams();
                break;
            case R.id.AgchangSeek:
                Repository.setAgchang(progress);
                getLayoutClass().sendAudioParams();
                break;
            case R.id.VolumeSeek:
                Repository.setVolume(progress);
                getLayoutClass().setVolume();
                break;
        }
    }
}
