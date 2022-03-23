package ru.natsuru.websdr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FreqAdapter extends RecyclerView.Adapter<FreqAdapter.ViewHolder> {
    private final ArrayList<FreqStore> bands;
    private final LayoutInflater inflater;
    private final Tuner tuner;
    public FreqAdapter(Context context, ArrayList<FreqStore> bands, Tuner tuner){
        inflater = LayoutInflater.from(context);
        this.bands = bands;
        this.tuner = tuner;
    }
    @NonNull
    @Override
    public FreqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bands, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FreqAdapter.ViewHolder holder, int position) {
        double currentFreq = (holder.getAdapterPosition() - 28990) * (-1);
        tuner.setFreq(currentFreq);
        FreqStore band = bands.get(position);
        holder.freqInstanceView.setText(String.valueOf(band.getFreqInstance()));
    }
    @Override
    public int getItemCount() {
        return bands.size();
    }
    @SuppressWarnings("InnerClassMayBeStatic")
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView freqInstanceView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            freqInstanceView = itemView.findViewById(R.id.FreqInstance);
        }
    }
}
