package ru.natsuru.websdr.ui.main.listener;

import android.annotation.SuppressLint;
import android.view.View;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.model.MemoryCell;
import ru.natsuru.websdr.ui.components.listener.AbstractButtonListener;
import ru.natsuru.websdr.ui.main.recycler.MemoryRecycler;

public class MainListenerButtons extends AbstractButtonListener<MemoryRecycler> {

    private final MemoryCell cell;

    public MainListenerButtons(MemoryRecycler layoutClass, MemoryCell cell) {
        super(layoutClass);
        this.cell = cell;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void clicked(View view) {
        switch (view.getId()){
            case R.id.TuneMemBtn:
                getLayoutClass().getObjA().tuneFromMemory(cell);
                break;
            case R.id.DeleteMemBtn:
                getLayoutClass().getObjA().deleteFromMemory(cell);
                break;
        }
    }
}
