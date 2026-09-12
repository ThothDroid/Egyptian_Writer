package com.blueapps.egyptianwriter.export;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.FragmentExportSettingsBinding;

import net.cachapa.expandablelayout.ExpandableLayout;

public class ExportSettingsFragment extends Fragment {

    private FragmentExportSettingsBinding binding;

    // Views
    private TextView learnMoreTitle;
    private ExpandableLayout learnMoreLayout;

    // Constants
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    // Parameters
    /*private String mParam1;
    private String mParam2;*/

    public ExportSettingsFragment() {
        // Required empty public constructor
    }

    public static ExportSettingsFragment newInstance(/*String param1, String param2*/) {
        ExportSettingsFragment fragment = new ExportSettingsFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExportSettingsBinding.inflate(inflater);

        // init Views
        learnMoreTitle = binding.learnMoreTitle;
        learnMoreLayout = binding.learnMoreLayout;

        learnMoreTitle.setOnClickListener((view) -> {
            if (learnMoreLayout.isExpanded()){
                learnMoreTitle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.clickable_textview_bg));
                learnMoreLayout.collapse(true);
            } else {
                learnMoreTitle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.context_textview_bg));
                learnMoreLayout.expand(true);
            }
        });
        learnMoreLayout.collapse(false);
        learnMoreTitle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.clickable_textview_bg));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}