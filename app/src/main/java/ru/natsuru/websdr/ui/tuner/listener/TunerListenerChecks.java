package ru.natsuru.websdr.ui.tuner.listener;

import android.annotation.SuppressLint;
import android.widget.RadioGroup;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.Tuner;
import ru.natsuru.websdr.radioengine.util.repo.Repository;
import ru.natsuru.websdr.ui.components.abstractListeners.AbstractCheckedListener;

public class TunerListenerChecks extends AbstractCheckedListener<Tuner> {

    public TunerListenerChecks(Tuner layoutClass) {
        super(layoutClass);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void checked(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.FMButton:
                Repository.setMode(0);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.AMButton:
                Repository.setMode(1);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.LSBButton:
                Repository.setMode(2);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.USBButton:
                Repository.setMode(3);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.CWButton:
                Repository.setMode(4);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.AlawButton:
                Repository.setaLaw(true);
                break;
            case R.id.UlawButton:
                Repository.setaLaw(false);
                break;
        }
    }
}
