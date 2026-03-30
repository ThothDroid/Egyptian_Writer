package com.blueapps.egyptianwriter.dashboard.filegrid;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.FileMoreMenuBinding;

import java.util.ArrayList;

public class FileMenu {

    FileMoreMenuBinding binding;
    private PopupWindow popupWindow;
    private Context context;

    private boolean dismissed = true;

    private int x = 0;
    private int y = 0;

    private ArrayList<FileMenuListener> listeners = new ArrayList<>();

    public FileMenu(Context context){

        this.context = context;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = FileMoreMenuBinding.inflate(layoutInflater);
        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(binding.getRoot());

        popupWindow.setWidth(ConstraintLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(binding.getRoot());
        popupWindow.setAnimationStyle(R.style.popup_window_animation);

        popupWindow.setBackgroundDrawable(new ColorDrawable());
        
        binding.export.setOnClickListener(view -> {
            for (FileMenuListener listener: listeners){
                listener.OnExport();
            }
            dismissed = false;
            popupWindow.dismiss();
        });
        binding.delete.setOnClickListener(view -> {
            for (FileMenuListener listener: listeners){
                listener.OnDelete();
            }
            dismissed = false;
            popupWindow.dismiss();
        });
        popupWindow.setOnDismissListener(() -> {
            if (dismissed) {
                for (FileMenuListener listener : listeners) {
                    listener.OnCancel();
                }
            }
        });

    }

    public void show(){
        popupWindow.showAtLocation(binding.getRoot(), Gravity.NO_GRAVITY, x + DPtoPX(24), y + DPtoPX(24)); // Displays popup above the anchor view.
    }

    public void addFileMenuListener(FileMenuListener listener){
        listeners.add(listener);
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int DPtoPX(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
