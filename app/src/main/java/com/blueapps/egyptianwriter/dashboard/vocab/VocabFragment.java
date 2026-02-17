package com.blueapps.egyptianwriter.dashboard.vocab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentVocabBinding;

public class VocabFragment extends Fragment {

    private FragmentVocabBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding
        binding = FragmentVocabBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        return rootView;

    }
}
