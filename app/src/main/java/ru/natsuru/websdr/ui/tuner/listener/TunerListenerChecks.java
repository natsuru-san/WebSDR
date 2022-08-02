package ru.natsuru.websdr.ui.tuner.listener;

import android.annotation.SuppressLint;
import android.widget.RadioGroup;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.service.repository.RepoService;
import ru.natsuru.websdr.service.repository.RepoServiceImpl;
import ru.natsuru.websdr.view.Tuner;
import ru.natsuru.websdr.ui.components.listener.AbstractCheckedListener;

public class TunerListenerChecks extends AbstractCheckedListener<Tuner> {

    private final RepoService repoService;

    public TunerListenerChecks(Tuner layoutClass) {
        super(layoutClass);
        repoService = new RepoServiceImpl();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void checked(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.FMButton:
                repoService.setRadioMode(0);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.AMButton:
                repoService.setRadioMode(1);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.LSBButton:
                repoService.setRadioMode(2);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.USBButton:
                repoService.setRadioMode(3);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.CWButton:
                repoService.setRadioMode(4);
                getLayoutClass().setMode();
                getLayoutClass().sendParams();
                break;
            case R.id.AlawButton:
                repoService.setAlaw(true);
                break;
            case R.id.UlawButton:
                repoService.setAlaw(false);
                break;
        }
    }
}
