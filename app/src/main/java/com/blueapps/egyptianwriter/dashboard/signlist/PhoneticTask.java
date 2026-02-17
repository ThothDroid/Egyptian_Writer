package com.blueapps.egyptianwriter.dashboard.signlist;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.LruCache;

import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.signprovider.SignProvider;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("CallToPrintStackTrace")
public class PhoneticTask implements Runnable{

    private final int index;
    private final int id;
    private final SignProvider signProvider;
    private final SignData signData;
    private final RecyclerViewAdapter adapter;
    private final Activity activity;
    private final RecyclerView recyclerView;

    public PhoneticTask(Activity activity, RecyclerViewAdapter adapter, RecyclerView recyclerView, SignProvider signProvider, SignData signData, int index, int id){
        this.signProvider = signProvider;
        this.signData = signData;
        this.index = index;
        this.adapter = adapter;
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.id = id;
    }

    @Override
    public void run() {
        try {

            String phonetic = signData.getPhoneticString();
            if (phonetic == null) {
                signData.setPhonetics(signProvider.getPhoneticsFromGardiner(signData.getGardinerId()));
                phonetic = signData.getPhoneticString();
            }

            if (!signData.getPhonetics().isEmpty()) {

                // Save
                adapter.putPhonetic(index, signData);

                // update ui
                final String finalPhonetic = phonetic;
                activity.runOnUiThread(() -> {
                    // Check if the item is still at the same position
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(index);
                    if (viewHolder != null && (int) viewHolder.itemView.getTag(R.id.item_id) == id) {
                        ((RecyclerViewAdapter.RecyclerViewHolder) viewHolder).binding.signPhonetics.setText(finalPhonetic);
                    }
                });

            }

        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Error handling
        }
    }
}
