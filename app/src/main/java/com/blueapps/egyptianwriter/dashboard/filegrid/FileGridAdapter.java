package com.blueapps.egyptianwriter.dashboard.filegrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;

import java.util.ArrayList;

public class FileGridAdapter extends RecyclerView.Adapter<FileGridAdapter.RecyclerViewHolder>{
    private ArrayList<FileGridData> dataList;
    private Context context;

    private int cardLayout;

    private ArrayList<FileListener> listeners = new ArrayList<>();

    public FileGridAdapter(Context context, ArrayList<FileGridData> dataList, int cardLayout) {
        this.context = context;
        this.dataList = dataList;
        this.cardLayout = cardLayout;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(cardLayout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        FileGridData data = dataList.get(position);
        holder.dataTitle.setText(data.getShortTitle());
        holder.buttonMore.setOnClickListener(view -> {
            FileMenu fileMenu = new FileMenu(context);

            int[] location = new int[2];
            holder.buttonMore.getLocationOnScreen(location);
            fileMenu.setPosition(location[0], location[1]);

            fileMenu.addFileMenuListener(new FileMenuListener() {
                @Override
                public void OnCancel() {

                }

                @Override
                public void OnExport() {
                    for (FileListener listener: listeners){
                        listener.OnExportFile(data.getTitle());
                    }
                }

                @Override
                public void OnDelete() {
                    for (FileListener listener: listeners){
                        listener.OnDeleteFile(data.getTitle());
                    }
                }
            });

            fileMenu.show();
        });

        holder.cardView.setOnClickListener(view -> {
            for (FileListener listener: listeners){
                listener.OnOpenFile(data.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return dataList.size();
    }

    public void addFileListener(FileListener listener){
        listeners.add(listener);
    }

    public void removeFileListeners(){
        listeners.clear();
    }

    // View Holder Class to handle Recycler View.
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView dataTitle;
        private final ImageButton buttonMore;
        private final CardView cardView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTitle = itemView.findViewById(R.id.data_title);
            buttonMore = itemView.findViewById(R.id.button_more);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
