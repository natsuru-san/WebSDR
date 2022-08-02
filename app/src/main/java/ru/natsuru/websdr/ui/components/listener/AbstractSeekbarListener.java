package ru.natsuru.websdr.ui.components.listener;

import android.widget.SeekBar;

public abstract class AbstractSeekbarListener<T> implements SeekBar.OnSeekBarChangeListener, ButtonListener<T> {

    private final T layoutClass;

    public AbstractSeekbarListener(T layoutClass) {
        this.layoutClass = layoutClass;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        shifted(seekBar, i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public T getLayoutClass() {
        return layoutClass;
    }

    public abstract void shifted(SeekBar seekBar, int progress);
}
