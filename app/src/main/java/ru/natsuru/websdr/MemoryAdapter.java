package ru.natsuru.websdr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<MemoryCell> memories;
    private final Main main;
    public MemoryAdapter(Context context, ArrayList<MemoryCell> memories, Main main){
        this.memories = memories;
        this.main = main;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MemoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.memories, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MemoryAdapter.ViewHolder holder, int position) {
        MemoryCell memory = memories.get(position);
        @SuppressLint("NonConstantResourceId") View.OnClickListener listener = v -> {
            switch (v.getId()){
                case R.id.TuneMemBtn:
                    main.tuneFromMemory(memory);
                    break;
                case R.id.DeleteMemBtn:
                    main.deleteFromMemory(memory);
                    break;
            }
        };
        String modeText = "";
        switch (memory.getMode()){
            case 0:
                modeText = main.getString(R.string.FM);
                break;
            case 1:
                modeText = main.getString(R.string.AM);
                break;
            case 2:
                modeText = main.getString(R.string.LSB);
                break;
            case 3:
                modeText = main.getString(R.string.USB);
                break;
            case 4:
                modeText = main.getString(R.string.CW);
                break;
        }
        holder.freqView.setText(String.valueOf(memory.getFreq()));
        holder.modulationView.setText(modeText);
        String borders = memory.getMinBorder() + " ... " + memory.getMaxBorder();
        holder.bordersView.setText(borders);
        holder.tuneBtn.setOnClickListener(listener);
        holder.deleteBtn.setOnClickListener(listener);
    }
    @Override
    public int getItemCount() {
        return memories.size();
    }
    @SuppressWarnings("InnerClassMayBeStatic")
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageButton tuneBtn;
        private final ImageButton deleteBtn;
        private final TextView freqView;
        private final TextView modulationView;
        private final TextView bordersView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tuneBtn = itemView.findViewById(R.id.TuneMemBtn);
            deleteBtn = itemView.findViewById(R.id.DeleteMemBtn);
            freqView = itemView.findViewById(R.id.FreqMem);
            modulationView = itemView.findViewById(R.id.ModulationMem);
            bordersView = itemView.findViewById(R.id.BordersMem);
        }
    }
}
