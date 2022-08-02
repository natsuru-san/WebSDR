package ru.natsuru.websdr.ui.components.listener;

import android.view.View;

public abstract class AbstractButtonListener<T> implements View.OnClickListener, ButtonListener<T> {

    private final T layoutClass;

    public AbstractButtonListener(T layoutClass) {
        this.layoutClass = layoutClass;
    }
    @Override
    public void onClick(View view) {
        clicked(view);
    }

    public T getLayoutClass() {
        return layoutClass;
    }

    public abstract void clicked(View view);
}
