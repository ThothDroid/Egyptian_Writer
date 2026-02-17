package com.blueapps.egyptianwriter.layoutadapter;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridAdapter {

    public GridAdapter(Context context, RecyclerView grid, int maxLength, GridLayoutManager gridManager){

        float maxLengthDp = dpToPx(maxLength, context.getResources().getDisplayMetrics());

        grid.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int width = grid.getWidth();
            int columnCount = (int) ((width / maxLengthDp) + 1);
            gridManager.setSpanCount(columnCount);
        });
    }

}
