package ru.natsuru.websdr.ui.components.listener;

import android.widget.CompoundButton;

public abstract class AbstractCompoundListener<T> implements CompoundButton.OnCheckedChangeListener, ButtonListener<T> {

    private final T layoutClass;

    public AbstractCompoundListener(T layoutClass) {
        this.layoutClass = layoutClass;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        checked(compoundButton, b);
    }

    @Override
    public T getLayoutClass() {
        return layoutClass;
    }

    public abstract void checked(CompoundButton buttonView, boolean check);
}
