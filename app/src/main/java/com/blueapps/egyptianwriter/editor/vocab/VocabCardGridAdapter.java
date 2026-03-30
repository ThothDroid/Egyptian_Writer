package com.blueapps.egyptianwriter.editor.vocab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.dashboard.filegrid.FileGridAdapter;
import com.blueapps.egyptianwriter.dashboard.filegrid.FileGridData;
import com.blueapps.egyptianwriter.dashboard.signlist.RecyclerViewAdapter;
import com.blueapps.egyptianwriter.databinding.SignCardBinding;
import com.blueapps.egyptianwriter.databinding.VocabCardBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;

import java.util.ArrayList;

public class VocabCardGridAdapter extends RecyclerView.Adapter<VocabCardGridAdapter.RecyclerViewHolder>{

    private ArrayList<VocabListener> listeners = new ArrayList<>();
    private ArrayList<Card> dataList;
    private Context context;

    private int cardLayout;

    public VocabCardGridAdapter(Context context, ArrayList<Card> dataList, int cardLayout) {
        this.context = context;
        this.dataList = dataList;
        this.cardLayout = cardLayout;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        VocabCardBinding binding = VocabCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VocabCardGridAdapter.RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Card card = dataList.get(position);
        holder.binding.cardTitle.setText(String.valueOf(card.getIndex() + 1));
        holder.binding.cardview.setOnClickListener((view) -> {
            for (VocabListener listener: listeners){
                listener.onOpen(card.getIndex());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addVocabListener(VocabListener listener){
        this.listeners.add(listener);
    }

    public void removeVocabListeners(){
        this.listeners.clear();
    }


    public class RecyclerViewHolder extends FileGridAdapter.RecyclerViewHolder{

        VocabCardBinding binding;

        public RecyclerViewHolder(VocabCardBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
