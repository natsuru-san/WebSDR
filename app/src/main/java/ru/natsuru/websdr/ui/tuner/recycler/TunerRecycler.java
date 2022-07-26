package ru.natsuru.websdr.ui.tuner.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import ru.natsuru.websdr.R;
import ru.natsuru.websdr.Tuner;
import ru.natsuru.websdr.radioengine.util.NullNode;
import ru.natsuru.websdr.radioengine.util.repo.Repository;
import ru.natsuru.websdr.ui.components.abstractRecyclers.Recycler;

public class TunerRecycler extends Recycler<Tuner, List<Integer>, NullNode> {

    public TunerRecycler(Context context, Tuner objA, List<Integer> objB, NullNode objC) {
        super(context, objA, objB, objC);
    }

    @Override
    public Recycler<Tuner, List<Integer>, NullNode>.ViewHolder createList(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.bands, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void actionWithInstanseOfList(Recycler<Tuner, List<Integer>, NullNode>.ViewHolder holder, int position) {
        float currentFreq = (holder.getAdapterPosition() - 28990) * (-1);
        Repository.setFreq(currentFreq);
        getObjA().setFreq();
        Integer band = getObjB().get(position);
        TextView textView = (TextView) holder.getViewA();
        textView.setText(String.valueOf(band));
    }

    @Override
    public int getCountOfInstances() {
        return getObjB().size();
    }

    @Override
    public int[] setIdOfViews() {
        int[] ids = new int[6];
        ids[0] = R.id.FreqInstance;
        return ids;
    }
}
