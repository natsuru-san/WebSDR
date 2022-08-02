package ru.natsuru.websdr.ui.components.listener;

import android.widget.RadioGroup;

public abstract class AbstractCheckedListener<T> implements RadioGroup.OnCheckedChangeListener, ButtonListener<T> {

    private final T layoutClass;

    public AbstractCheckedListener(T layoutClass) {
        this.layoutClass = layoutClass;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        checked(radioGroup, i);
    }

    @Override
    public T getLayoutClass() {
        return layoutClass;
    }

    public abstract void checked(RadioGroup radioGroup, int i);
}
