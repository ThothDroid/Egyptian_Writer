package com.blueapps.egyptianwriter.learning;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentResultBinding;
import com.blueapps.egyptianwriter.databinding.FragmentSignCardLearnBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private static final String TAG = "ResultFragment";

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

        return rootView;
    }
}