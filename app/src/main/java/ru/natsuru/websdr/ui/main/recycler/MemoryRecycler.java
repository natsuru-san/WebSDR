package ru.natsuru.websdr.ui.main.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import ru.natsuru.websdr.view.Main;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.model.MemoryCell;
import ru.natsuru.websdr.util.NullNode;
import ru.natsuru.websdr.ui.components.recycler.Recycler;
import ru.natsuru.websdr.ui.main.listener.MainListenerButtons;

public class MemoryRecycler extends Recycler<Main, List<MemoryCell>, NullNode> {

    public MemoryRecycler(Context context, Main objA, List<MemoryCell> objB, NullNode objC) {
        super(context, objA, objB, objC);
    }

    @Override
    public Recycler<Main, List<MemoryCell>, NullNode>.ViewHolder createList(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.memories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void actionWithInstanseOfList(Recycler<Main, List<MemoryCell>, NullNode>.ViewHolder holder, int position) {
        MemoryCell memory = getObjB().get(position);
        View.OnClickListener listener = new MainListenerButtons(this, memory);
        String modeText = "";
        switch (memory.getMode()){
            case 0:
                modeText = getObjA().getString(R.string.FM);
                break;
            case 1:
                modeText = getObjA().getString(R.string.AM);
                break;
            case 2:
                modeText = getObjA().getString(R.string.LSB);
                break;
            case 3:
                modeText = getObjA().getString(R.string.USB);
                break;
            case 4:
                modeText = getObjA().getString(R.string.CW);
                break;
        }
        TextView freqView = (TextView) holder.getViewC();
        freqView.setText(String.valueOf(memory.getFreq()));

        TextView modulationView = (TextView) holder.getViewD();
        modulationView.setText(modeText);

        TextView bordersView = (TextView) holder.getViewE();
        String borders = memory.getMinBorder() + " ... " + memory.getMaxBorder();
        bordersView.setText(borders);

        ImageButton tuneBtn = (ImageButton) holder.getViewA();
        tuneBtn.setOnClickListener(listener);

        ImageButton deleteBtn = (ImageButton) holder.getViewB();
        deleteBtn.setOnClickListener(listener);
    }

    @Override
    public int getCountOfInstances() {
        return getObjB().size();
    }

    @Override
    public int[] setIdOfViews() {
        int[] ids = new int[6];
        ids[0] = R.id.TuneMemBtn;
        ids[1] = R.id.DeleteMemBtn;
        ids[2] = R.id.FreqMem;
        ids[3] = R.id.ModulationMem;
        ids[4] = R.id.BordersMem;
        return ids;
    }
}
