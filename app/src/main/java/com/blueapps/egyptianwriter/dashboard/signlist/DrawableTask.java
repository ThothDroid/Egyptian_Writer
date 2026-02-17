package com.blueapps.egyptianwriter.dashboard.signlist;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.LruCache;

import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.signprovider.SignProvider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class DrawableTask implements Runnable{

    private final int index;
    private final int id;
    private final String gardinerId;
    private final SignProvider signProvider;
    private final RecyclerView recyclerView;
    private final LruCache<String, Drawable> cache;
    private final Activity activity;
    private final RecyclerViewAdapter adapter;

    public DrawableTask(Activity activity, RecyclerViewAdapter adapter, RecyclerView recyclerView, SignProvider signProvider, LruCache<String, Drawable> cache, String gardinerId, int index, int id){
        this.recyclerView = recyclerView;
        this.signProvider = signProvider;
        this.gardinerId = gardinerId;
        this.index = index;
        this.cache = cache;
        this.id = id;
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public void run() {
        try {

            if (cache.get(gardinerId) == null) {
                Drawable drawable = signProvider.getSign(gardinerId);

                // Save to cache
                adapter.putDrawable(gardinerId, signProvider.getSign(gardinerId));

                // update ui
                activity.runOnUiThread(() -> {
                    // Check if the item is still at the same position
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(index);
                    if (viewHolder != null && (int) viewHolder.itemView.getTag(R.id.item_id) == id) {
                        ((RecyclerViewAdapter.RecyclerViewHolder) viewHolder).binding.signImage.setImageDrawable(drawable);
                    }
                });
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            // TODO: Error handling
        }
    }
}
