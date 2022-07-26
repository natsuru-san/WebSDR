package ru.natsuru.websdr.ui.tuner.listener;

import android.annotation.SuppressLint;
import android.view.View;

import ru.natsuru.websdr.R;
import ru.natsuru.websdr.Tuner;
import ru.natsuru.websdr.ui.components.abstractListeners.AbstractButtonListener;

public class TunerListenerButtons extends AbstractButtonListener<Tuner> {

    public TunerListenerButtons(Tuner layoutClass) {
        super(layoutClass);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void clicked(View view) {
        switch (view.getId()){
            case R.id.UpFreqButton:
                getLayoutClass().setWidthChannel(true);
                getLayoutClass().sendParams();
                break;
            case R.id.DownFreqButton:
                getLayoutClass().setWidthChannel(false);
                getLayoutClass().sendParams();
                break;
            case R.id.HideBtn:
                getLayoutClass().settingsShow(false);
                break;
            case R.id.ShowBtn:
                getLayoutClass().settingsShow(true);
                break;
        }
    }
}
