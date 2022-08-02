package ru.natsuru.websdr.ui.tuner.listener;

import android.annotation.SuppressLint;
import android.widget.SeekBar;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.service.repository.RepoService;
import ru.natsuru.websdr.service.repository.RepoServiceImpl;
import ru.natsuru.websdr.view.Tuner;
import ru.natsuru.websdr.ui.components.listener.AbstractSeekbarListener;

public class TunerListenerSeeks extends AbstractSeekbarListener<Tuner> {

    private final RepoService repoService;

    public TunerListenerSeeks(Tuner layoutClass) {
        super(layoutClass);
        repoService = new RepoServiceImpl();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void shifted(SeekBar seekBar, int progress) {
        switch (seekBar.getId()){
            case R.id.GainSeek:
                repoService.setGain(progress);
                getLayoutClass().sendAudioParams();
                break;
            case R.id.AgchangSeek:
                repoService.setAgchang(progress);
                getLayoutClass().sendAudioParams();
                break;
            case R.id.VolumeSeek:
                repoService.setVolume(progress);
                getLayoutClass().setVolume();
                break;
        }
    }
}
