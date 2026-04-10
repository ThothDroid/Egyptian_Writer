package com.blueapps.egyptianwriter.info;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.InfoItemBinding;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.InfoViewHolder> {

    private Context context;
    private ArrayList<InfoData> items;

    public RecyclerViewAdapter(Context context, ArrayList<InfoData> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        InfoItemBinding binding = InfoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InfoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        // Set the data to textview and imageview.
        InfoData data = items.get(position);
        if (data.getMode() == InfoData.MODE_SINGLE_ITEM){
            holder.binding.itemCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.info_bg_single_item));
            holder.binding.groupCard.setVisibility(View.GONE);
            holder.binding.itemCard.setVisibility(View.VISIBLE);
            holder.binding.separatorTop.setVisibility(View.INVISIBLE);
            holder.binding.separatorBottom.setVisibility(View.INVISIBLE);
        } else if (data.getMode() == InfoData.MODE_TOP_ITEM){
            holder.binding.itemCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.info_bg_top_item));
            holder.binding.groupCard.setVisibility(View.GONE);
            holder.binding.itemCard.setVisibility(View.VISIBLE);
            holder.binding.separatorTop.setVisibility(View.INVISIBLE);
            holder.binding.separatorBottom.setVisibility(View.VISIBLE);
        } else if (data.getMode() == InfoData.MODE_BOTTOM_ITEM) {
            holder.binding.itemCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.info_bg_bottom_item));
            holder.binding.groupCard.setVisibility(View.GONE);
            holder.binding.itemCard.setVisibility(View.VISIBLE);
            holder.binding.separatorTop.setVisibility(View.VISIBLE);
            holder.binding.separatorBottom.setVisibility(View.INVISIBLE);
        } else if (data.getMode() == InfoData.MODE_MIDDLE_ITEM) {
            holder.binding.itemCard.setBackground(AppCompatResources.getDrawable(context, R.drawable.info_bg_middle_item));
            holder.binding.groupCard.setVisibility(View.GONE);
            holder.binding.itemCard.setVisibility(View.VISIBLE);
            holder.binding.separatorTop.setVisibility(View.VISIBLE);
            holder.binding.separatorBottom.setVisibility(View.VISIBLE);
        } else if (data.getMode() == InfoData.MODE_GROUP_TITLE_ITEM) {
            holder.binding.groupCard.setVisibility(View.VISIBLE);
            holder.binding.itemCard.setVisibility(View.GONE);
            holder.binding.separatorTop.setVisibility(View.INVISIBLE);
            holder.binding.separatorBottom.setVisibility(View.INVISIBLE);
        }

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.itemCard.getLayoutParams();
        float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());

        if (data.isGroup() && data.getMode() != InfoData.MODE_BOTTOM_ITEM) {
            if (data.isTop()) {
                params.setMargins(params.leftMargin, (int) margin, params.rightMargin, 0);
            } else {
                params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 0);
            }
        } else {
            if (data.isGroup()) {
                params.setMargins(params.leftMargin, 0, params.rightMargin, (int) margin);
            }
        }
        holder.binding.itemCard.setLayoutParams(params);

        holder.binding.itemCard.setOnClickListener((view -> {
            data.triggerAction(context);
        }));

        if (data.getSubtitle() != null){
            holder.binding.itemSubtitle.setText(data.getSubtitle());
            holder.binding.groupItemSubtitle.setText(data.getSubtitle());
            holder.binding.groupItemSubtitle.setVisibility(View.VISIBLE);
            holder.binding.itemSubtitle.setVisibility(View.VISIBLE);
            holder.binding.itemTitleMiddle.setVisibility(View.INVISIBLE);
            holder.binding.itemTitle.setVisibility(View.VISIBLE);
        } else {
            holder.binding.groupItemSubtitle.setVisibility(View.GONE);
            holder.binding.itemSubtitle.setVisibility(View.INVISIBLE);
            holder.binding.itemTitleMiddle.setVisibility(View.VISIBLE);
            holder.binding.itemTitle.setVisibility(View.INVISIBLE);
        }
        holder.binding.itemTitleMiddle.setText(data.getTitle());
        holder.binding.itemTitle.setText(data.getTitle());
        holder.binding.groupItem.setText(data.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // View Holder Class to handle Recycler View.
    public class InfoViewHolder extends RecyclerView.ViewHolder {

        public InfoItemBinding binding;

        public InfoViewHolder(InfoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
