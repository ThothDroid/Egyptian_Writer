package com.blueapps.egyptianwriter.export;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blueapps.egyptianwriter.databinding.FragmentFileResultBinding;

// ...existing code...

public class FileResultFragment extends Fragment {

    private FragmentFileResultBinding binding;

    private String filename = "";
    private FileResultListener listener;

    // Views
    private TextView fileNameVert;
    private Button buttonSave;
    private Button buttonShare;


    // Constants
    // Arguments
    public static final String ARG_FILENAME = "filename";

    public FileResultFragment() {
        // Required empty public constructor
    }

    public static FileResultFragment newInstance(String filename) {
        FileResultFragment fragment = new FileResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILENAME, filename);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(FileResultListener listener){
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filename = getArguments().getString(ARG_FILENAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFileResultBinding.inflate(inflater);

        // Set names for Views
        fileNameVert = binding.fileNameVert;
        buttonSave = binding.buttonSave;
        buttonShare = binding.buttonShare;

        fileNameVert.setText(filename);

        buttonSave.setOnClickListener(view -> {
            listener.onSave();
        });

        buttonShare.setOnClickListener(view -> {
            listener.onShare();
        });

        return binding.getRoot();
    }
}