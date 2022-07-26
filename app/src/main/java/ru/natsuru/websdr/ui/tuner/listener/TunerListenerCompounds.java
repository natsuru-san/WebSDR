package ru.natsuru.websdr.ui.tuner.listener;

import android.annotation.SuppressLint;
import android.widget.CompoundButton;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.Tuner;
import ru.natsuru.websdr.radioengine.util.repo.Repository;
import ru.natsuru.websdr.ui.components.abstractListeners.AbstractCompoundListener;

public class TunerListenerCompounds extends AbstractCompoundListener<Tuner> {

    public TunerListenerCompounds(Tuner layoutClass) {
        super(layoutClass);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void checked(CompoundButton buttonView, boolean check) {
        switch (buttonView.getId()){
            case R.id.NoiseBox:
                if (check) {
                    Repository.setNoise(-100);
                } else {
                    Repository.setNoise(0);
                }
                break;
            case R.id.SquelchBox:
                if (check) {
                    Repository.setSquelch(1);
                } else {
                    Repository.setSquelch(0);
                }
                break;
            case R.id.AutogainBox:
                getLayoutClass().setAutogainViews(check);
                break;
            case R.id.AutonotchBox:
                if (check) {
                    Repository.setAutonotch(1);
                } else {
                    Repository.setAutonotch(0);
                }
                break;
        }
        getLayoutClass().sendAudioParams();
    }
}
