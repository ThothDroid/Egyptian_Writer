package com.blueapps.egyptianwriter.export;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.FragmentExportSettingsBinding;

import net.cachapa.expandablelayout.ExpandableLayout;

public class ExportSettingsFragment extends Fragment {

    private FragmentExportSettingsBinding binding;

    private ExportListener listener;
    private ExportProperty property;

    // Properties
    private int fileType = FILE_TYPE_EWDOC;

    // Views
    private TextView learnMoreTitle;
    private ExpandableLayout learnMoreLayout;
    private TextView learnMoreText;
    private Spinner fileFormat;
    private TextView noSettings;
    private ProgressBar progressBar;
    private Button exportButton;

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
        progressBar = binding.progressBar;
        exportButton = binding.Export;

        property = new ExportProperty();

        fileFormat.setEnabled(true);
        exportButton.setEnabled(true);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            learnMoreText.setText(Html.fromHtml(ContextCompat.getString(getContext(), R.string.export_learn_more_ewdoc), Html.FROM_HTML_MODE_LEGACY));
        } else {
            learnMoreText.setText(Html.fromHtml(ContextCompat.getString(getContext(), R.string.export_learn_more_ewdoc)));
        }

        fileFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileType = i;
                property.setFileType(i);
                String html = "";
                if (fileType == FILE_TYPE_EWDOC){
                    noSettings.setVisibility(View.VISIBLE);
                    html = ContextCompat.getString(getContext(), R.string.export_learn_more_ewdoc);
                } else if (fileType == FILE_TYPE_SVG){
                    noSettings.setVisibility(View.GONE);
                    html = ContextCompat.getString(getContext(), R.string.export_learn_more_svg);
                } else if (fileType == FILE_TYPE_PNG){
                    noSettings.setVisibility(View.GONE);
                    html = ContextCompat.getString(getContext(), R.string.export_learn_more_png);
                } else if (fileType == FILE_TYPE_JPEG){
                    noSettings.setVisibility(View.GONE);
                    html = ContextCompat.getString(getContext(), R.string.export_learn_more_jpeg);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    learnMoreText.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    learnMoreText.setText(Html.fromHtml(html));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        exportButton.setOnClickListener((view -> {
            fileFormat.setEnabled(false);
            exportButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            if (listener != null) listener.onExport(property);
        }));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setListener(ExportListener listener){
        this.listener = listener;
    }
}