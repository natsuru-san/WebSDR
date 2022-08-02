package ru.natsuru.websdr.ui.tuner.listener;

import android.annotation.SuppressLint;
import android.widget.CompoundButton;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.service.repository.RepoService;
import ru.natsuru.websdr.service.repository.RepoServiceImpl;
import ru.natsuru.websdr.view.Tuner;
import ru.natsuru.websdr.ui.components.listener.AbstractCompoundListener;

public class TunerListenerCompounds extends AbstractCompoundListener<Tuner> {

    private final RepoService repoService;

    public TunerListenerCompounds(Tuner layoutClass) {
        super(layoutClass);
        repoService = new RepoServiceImpl();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void checked(CompoundButton buttonView, boolean check) {
        switch (buttonView.getId()){
            case R.id.NoiseBox:
                if (check) {
                    repoService.setNoise(-100);
                } else {
                    repoService.setNoise(0);
                }
                break;
            case R.id.SquelchBox:
                if (check) {
                    repoService.setSquelch(1);
                } else {
                    repoService.setSquelch(0);
                }
                break;
            case R.id.AutogainBox:
                getLayoutClass().setAutogainViews(check);
                break;
            case R.id.AutonotchBox:
                if (check) {
                    repoService.setAutonotch(1);
                } else {
                    repoService.setAutonotch(0);
                }
                break;
        }
        getLayoutClass().sendAudioParams();
    }
}
