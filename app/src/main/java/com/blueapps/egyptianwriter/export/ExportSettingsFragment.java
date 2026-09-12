package com.blueapps.egyptianwriter.export;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.FragmentExportSettingsBinding;

import net.cachapa.expandablelayout.ExpandableLayout;

public class ExportSettingsFragment extends Fragment {

    private FragmentExportSettingsBinding binding;

    private int fileType = FILE_TYPE_EWDOC;

    // Views
    private TextView learnMoreTitle;
    private ExpandableLayout learnMoreLayout;
    private TextView learnMoreText;
    private Spinner fileFormat;
    private TextView noSettings;

    // Constants
    public static final int FILE_TYPE_EWDOC = 0;
    public static final int FILE_TYPE_SVG = 1;
    public static final int FILE_TYPE_PNG = 2;
    public static final int FILE_TYPE_JPEG = 3;

    public ExportSettingsFragment() {
        // Required empty public constructor
    }

    public static ExportSettingsFragment newInstance(/*String param1, String param2*/) {
        ExportSettingsFragment fragment = new ExportSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExportSettingsBinding.inflate(inflater);

        // init Views
        fileFormat = binding.fileFormat;
        learnMoreTitle = binding.learnMoreTitle;
        learnMoreLayout = binding.learnMoreLayout;
        learnMoreText = binding.learnMoreText;
        noSettings = binding.noSettingsTitle;

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
        learnMoreText.setText(Html.fromHtml(ContextCompat.getString(getContext(), R.string.export_learn_more_ewdoc), Html.FROM_HTML_MODE_COMPACT));

        fileFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileType = i;
                if (fileType == FILE_TYPE_EWDOC){
                    noSettings.setVisibility(View.VISIBLE);
                    learnMoreText.setText(Html.fromHtml(ContextCompat.getString(getContext(), R.string.export_learn_more_ewdoc), Html.FROM_HTML_MODE_COMPACT));
                } else if (fileType == FILE_TYPE_SVG){
                    noSettings.setVisibility(View.GONE);
                    learnMoreText.setText(Html.fromHtml(ContextCompat.getString(getContext(), R.string.export_learn_more_svg), Html.FROM_HTML_MODE_COMPACT));
                } else if (fileType == FILE_TYPE_PNG){
                    noSettings.setVisibility(View.GONE);
                    learnMoreText.setText(Html.fromHtml(ContextCompat.getString(getContext(), R.string.export_learn_more_png), Html.FROM_HTML_MODE_COMPACT));
                } else if (fileType == FILE_TYPE_JPEG){
                    noSettings.setVisibility(View.GONE);
                    learnMoreText.setText(Html.fromHtml(ContextCompat.getString(getContext(), R.string.export_learn_more_jpeg), Html.FROM_HTML_MODE_COMPACT));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}