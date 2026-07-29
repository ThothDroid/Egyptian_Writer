package com.blueapps.egyptianwriter.learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentResultBinding;

import java.util.Arrays;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private static final String TAG = "ResultFragment";

    private int[] results = new int[0];

    // Constants

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance() {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Do something
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.result.setText(Arrays.toString(results));

        return rootView;
    }

    public void setResults(int[] results){
        this.results = results;
        binding.result.setText(Arrays.toString(results));
    }
}