package ru.natsuru.websdr.ui.components.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("ALL")
public abstract class Recycler<A, B, C> extends RecyclerView.Adapter<Recycler<A, B, C>.ViewHolder> {

    private final A objA;
    private final B objB;
    private final C objC;
    private final LayoutInflater inflater;
    private int[] idOfViews;

    public Recycler(Context context, A objA, B objB, C objC) {
        inflater = LayoutInflater.from(context);
        this.objA = objA;
        this.objB = objB;
        this.objC = objC;
    }

    public void setId() {
        this.idOfViews = setIdOfViews();
    }

    public A getObjA() {
        return objA;
    }

    public B getObjB() {
        return objB;
    }

    public C getObjC() {
        return objC;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        setId();
        return createList(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull Recycler.ViewHolder holder, int position) {
        actionWithInstanseOfList(holder, position);
    }

    @Override
    public int getItemCount() {
        return getCountOfInstances();
    }

    public abstract ViewHolder createList(ViewGroup parent, int viewType);
    public abstract void actionWithInstanseOfList(Recycler<A, B, C>.ViewHolder holder, int position);
    public abstract int getCountOfInstances();
    public abstract int[] setIdOfViews();

    @SuppressWarnings("InnerClassMayBeStatic")
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View viewA;
        private final View viewB;
        private final View viewC;
        private final View viewD;
        private final View viewE;
        private final View viewF;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewA = itemView.findViewById(idOfViews[0]);
            viewB = itemView.findViewById(idOfViews[1]);
            viewC = itemView.findViewById(idOfViews[2]);
            viewD = itemView.findViewById(idOfViews[3]);
            viewE = itemView.findViewById(idOfViews[4]);
            viewF = itemView.findViewById(idOfViews[5]);
        }

        public View getViewA() {
            return viewA;
        }

        public View getViewB() {
            return viewB;
        }

        public View getViewC() {
            return viewC;
        }

        public View getViewD() {
            return viewD;
        }

        public View getViewE() {
            return viewE;
        }

        public View getViewF() {
            return viewF;
        }
    }
}
