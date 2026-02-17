package com.blueapps.egyptianwriter.dashboard.dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentDictionaryBinding;

public class DictionaryFragment extends Fragment {

    private FragmentDictionaryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding
        binding = FragmentDictionaryBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        return rootView;

    }
}
