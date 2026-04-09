package com.blueapps.egyptianwriter.editor.vocab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.dashboard.filegrid.FileGridAdapter;
import com.blueapps.egyptianwriter.databinding.VocabCardBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class VocabCardGridAdapter extends RecyclerView.Adapter<VocabCardGridAdapter.RecyclerViewHolder>{

    private final ArrayList<VocabListener> listeners = new ArrayList<>();
    private final ArrayList<Card> dataList;
    private final Context context;

    public VocabCardGridAdapter(Context context, ArrayList<Card> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        VocabCardBinding binding = VocabCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerViewHolder(binding);
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

        // TODO: Replace this quick&dirty solution with a better one like with signList
        if (card instanceof SignCard) {
            SignCard signCard = (SignCard) card;
            try {
                holder.binding.imageView2.setImageDrawable(signCard.getSign(context));
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
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


    public static class RecyclerViewHolder extends FileGridAdapter.RecyclerViewHolder{

        VocabCardBinding binding;

        public RecyclerViewHolder(VocabCardBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
