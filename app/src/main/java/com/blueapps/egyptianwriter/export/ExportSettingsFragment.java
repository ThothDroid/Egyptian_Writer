package com.blueapps.egyptianwriter.export;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    // Parameters
    private String name = "";

    // Properties
    private int fileType = FILE_TYPE_EWDOC;

    // Views
    private TextView learnMoreTitle;
    private ExpandableLayout learnMoreLayout;
    private TextView learnMoreText;
    private Spinner fileFormat;
    private TextView noSettings;
    private CheckBox checkSvgTitle;
    private EditText inputSvgTitle;
    private ProgressBar progressBar;
    private TextView progressTitle;
    private TextView progressText;
    private Button exportButton;

    // Constants
    // Params
    public static final String ARG_NAME = "name";
    public static final int FILE_TYPE_EWDOC = 0;
    public static final int FILE_TYPE_SVG = 1;
    public static final int FILE_TYPE_PNG = 2;
    public static final int FILE_TYPE_JPEG = 3;

    public ExportSettingsFragment() {
        // Required empty public constructor
    }

    public static ExportSettingsFragment newInstance(String name) {
        ExportSettingsFragment fragment = new ExportSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
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
        checkSvgTitle = binding.checkSvgTitle;
        inputSvgTitle = binding.svgTitle;
        progressBar = binding.progressBar;
        progressTitle = binding.progressTitle;
        progressText = binding.progressText;
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
                    checkSvgTitle.setVisibility(View.GONE);
                    inputSvgTitle.setVisibility(View.GONE);
                    html = ContextCompat.getString(getContext(), R.string.export_learn_more_ewdoc);
                } else if (fileType == FILE_TYPE_SVG){
                    noSettings.setVisibility(View.GONE);
                    checkSvgTitle.setVisibility(View.VISIBLE);
                    inputSvgTitle.setVisibility(View.VISIBLE);
                    html = ContextCompat.getString(getContext(), R.string.export_learn_more_svg);
                } else if (fileType == FILE_TYPE_PNG){
                    noSettings.setVisibility(View.GONE);
                    checkSvgTitle.setVisibility(View.GONE);
                    inputSvgTitle.setVisibility(View.GONE);
                    html = ContextCompat.getString(getContext(), R.string.export_learn_more_png);
                } else if (fileType == FILE_TYPE_JPEG){
                    noSettings.setVisibility(View.GONE);
                    checkSvgTitle.setVisibility(View.GONE);
                    inputSvgTitle.setVisibility(View.GONE);
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

        // Title
        checkSvgTitle.setOnCheckedChangeListener((compoundButton, b) -> {
            inputSvgTitle.setEnabled(b);
            property.setBTitle(b);
            if (b){
                inputSvgTitle.setTextColor(getResources().getColor(R.color.l_textColor, getContext().getTheme()));
            } else {
                inputSvgTitle.setTextColor(getResources().getColor(R.color.l_textColorDisabled, getContext().getTheme()));
            }
        });
        inputSvgTitle.setText(name);
        inputSvgTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                property.setTitle(editable.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        exportButton.setOnClickListener((view -> {
            fileFormat.setEnabled(false);
            exportButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            progressTitle.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
            if (listener != null) listener.onExport(property);
        }));

        return binding.getRoot();
    }

    public void progress(int progress, int total){
        progressBar.setIndeterminate(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress((int) (((float) progress / (float) total) * 100), true);
        } else {
            progressBar.setProgress((int) (((float) progress / (float) total) * 100));
        }
        progressText.setText(String.format(getString(R.string.export_progress), progress, total, (int) (((float) progress / (float) total) * 100)));
    }

    public void postPrecessing(){
        progressBar.setIndeterminate(true);
        progressText.setText(R.string.export_processing);
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