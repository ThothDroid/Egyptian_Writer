package com.blueapps.egyptianwriter.preferences.frontend;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.PreferencesFolderBinding;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FolderViewHolder> {

    private final Context context;
    private final ArrayList<Folder> items;

    public RecyclerViewAdapter(Context context, ArrayList<Folder> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        PreferencesFolderBinding binding = PreferencesFolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FolderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        // Set the data to textview and ImageView.

        // Set background
        Folder data = items.get(position);
        if (data.getMode() == Folder.MODE_TOP){
            holder.binding.folderCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.info_bg_top_item));
        } else if (data.getMode() == Folder.MODE_BOTTOM) {
            holder.binding.folderCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.info_bg_bottom_item));
        } else if (data.getMode() == Folder.MODE_MIDDLE) {
            holder.binding.folderCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.info_bg_middle_item));
        }

        // Change margins
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.folderCard.getLayoutParams();
        float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

        if (data.getMode() == Folder.MODE_TOP) {
            params.setMargins(params.leftMargin, (int) margin, params.rightMargin, 0);
        } else if (data.getMode() == Folder.MODE_BOTTOM){
            params.setMargins(params.leftMargin, 0, params.rightMargin, (int) margin);
        } else {
            params.setMargins(params.leftMargin, 0, params.rightMargin, 0);
        }
        holder.binding.folderCard.setLayoutParams(params);

        holder.binding.folderTitle.setText(data.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // View Holder Class to handle Recycler View.
    public class FolderViewHolder extends RecyclerView.ViewHolder {

        public PreferencesFolderBinding binding;

        public FolderViewHolder(PreferencesFolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
