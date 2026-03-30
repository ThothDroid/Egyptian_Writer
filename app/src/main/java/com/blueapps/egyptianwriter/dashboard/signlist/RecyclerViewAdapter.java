package com.blueapps.egyptianwriter.dashboard.signlist;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.SignCardBinding;
import com.blueapps.signprovider.SignProvider;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private final ArrayList<SignData> DataArrayList;
    private final LruCache<String, Drawable> drawableCache;
    private RecyclerView recyclerView;
    private final SignProvider signProvider;
    private final Activity context;
    private final GridLayoutManager layoutManager;

    private ExecutorService executorService;

    public RecyclerViewAdapter(ArrayList<SignData> recyclerData, Activity context, SignProvider signProvider, GridLayoutManager layoutManager, int threadSize){
        this.DataArrayList = recyclerData;
        this.signProvider = signProvider;
        this.context = context;
        this.layoutManager = layoutManager;
        start(threadSize);

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass(); // RAM in MB, which is reserved for the app
        final int cacheSize = 1024 * 1024 * memoryClass / 4; // 1/4 of RAM in Bytes
        drawableCache = new LruCache<>(cacheSize){
            @Override
            protected int sizeOf(String key, Drawable value) {
                return estimateVectorDrawableSize();
            }
        };
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        SignCardBinding binding = SignCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        // Set the data to textView and ImageView.
        SignData recyclerData = DataArrayList.get(position);

        String gardinerId = recyclerData.getGardinerId();
        if (gardinerId != holder.binding.signTitle.getText())holder.binding.signTitle.setText(gardinerId);

        holder.binding.signImage.setImageDrawable(drawableCache.get(gardinerId));

        holder.binding.signPhonetics.setText(recyclerData.getPhoneticString());

        holder.bind(recyclerData, executorService,
                new DrawableTask(context, this, recyclerView, signProvider, drawableCache, gardinerId, position, recyclerData.getId()),
                new PhoneticTask(context, this, recyclerView, signProvider, recyclerData, position, recyclerData.getId()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;

        // Add scroll listener to update visible items
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                // Update all currently visible items
                for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                    if (i >= 0 && i < DataArrayList.size()) {
                        SignData item = DataArrayList.get(i);
                        String gardinerId = item.getGardinerId();
                        Drawable drawable = drawableCache.get(gardinerId);

                        // If drawable is loaded, update the view
                        if (drawable != null) {
                            RecyclerView.ViewHolder vh = layoutManager.findViewByPosition(i) != null
                                    ? recyclerView.findViewHolderForAdapterPosition(i)
                                    : null;

                            if (vh != null) {
                                ((RecyclerViewHolder) vh).binding.signImage.setImageDrawable(drawable);
                            }
                        }
                        if (item.getPhonetics() != null && !item.getPhonetics().isEmpty()){
                            RecyclerView.ViewHolder vh = layoutManager.findViewByPosition(i) != null
                                    ? recyclerView.findViewHolderForAdapterPosition(i)
                                    : null;

                            if (vh != null) {
                                ((RecyclerViewHolder) vh).binding.signPhonetics.setText(item.getPhoneticString());
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return DataArrayList.size();
    }

    public void shutdown(){
        drawableCache.evictAll();
        executorService.shutdown();
    }

    public void start(int threadSize){
        this.executorService = Executors.newFixedThreadPool(threadSize);
    }

    public void putDrawable(String key, Drawable value){
        drawableCache.put(key, value);
    }

    public void putPhonetic(int index, SignData value){
        DataArrayList.set(index, value);
    }

    private int estimateVectorDrawableSize() {
        // Conservative fallback: ~50KB average for a vector
        return 50 * 1024;
    }

    // View Holder Class to handle Recycler View.
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public SignCardBinding binding;

        public RecyclerViewHolder(SignCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SignData item, ExecutorService executorService, DrawableTask drawableTask, PhoneticTask phoneticTask) {
            // Store the item's ID in the view for later verification
            this.itemView.setTag(R.id.item_id, item.getId());

            // Load data asynchronously
            if (!executorService.isShutdown()) {
                executorService.submit(drawableTask);
                executorService.submit(phoneticTask);
            }
        }
    }

}
