package com.blueapps.egyptianwriter.editor.document.properties;

import static com.blueapps.egyptianwriter.editor.document.properties.PropertiesManager.VERTICAL_ORIENTATION_MAP;
import static com.blueapps.egyptianwriter.editor.document.properties.PropertiesManager.WRITING_DIRECTION_MAP;
import static com.blueapps.egyptianwriter.editor.document.properties.PropertiesManager.WRITING_LAYOUT_MAP;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.blueapps.egyptianwriter.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PropertiesFragment extends Fragment implements TextWatcher {

    private FragmentSettingsBinding binding;
    private PropertiesManager propertiesManager;
    private boolean blockTextSize = false;

    // Views
    private EditText editTextSize;
    private ImageButton textSizeIncrease;
    private ImageButton textSizeDecrease;
    private EnumSettings writingLayout;
    private EnumSettings verticalOrientation;
    private EnumSettings writingDirection;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        // Get ViewModel
        propertiesManager = new ViewModelProvider(getActivity()).get(PropertiesManager.class);

        // Set names for Views
        editTextSize = binding.inputTextSize;
        textSizeIncrease = binding.textSizeIncrease;
        textSizeDecrease = binding.textSizeDecrease;

        // Set initial values
        editTextSize.setText(propertiesManager.getTextSize().getValue().toString());

        // textSize
        Handler handler = new Handler(Looper.getMainLooper());

        textSizeIncrease.setOnClickListener(view -> {
            String sTextSize = String.valueOf(editTextSize.getText());
            new Thread(() -> {
                try {
                    int textSize = Integer.parseInt(sTextSize);
                    if (textSize < 999){
                        textSize++;
                        final int finalTextSize = textSize;
                        handler.post(() -> editTextSize.setText(String.valueOf(finalTextSize)));
                    } else {
                        disableChangeButtons();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    disableChangeButtons();
                }
            }).start();
        });

        textSizeDecrease.setOnClickListener(view -> {
            String sTextSize = String.valueOf(editTextSize.getText());
            new Thread(() -> {
                try {
                    int textSize = Integer.parseInt(sTextSize);
                    if (textSize > 0){
                        textSize--;
                        final int finalTextSize = textSize;
                        handler.post(() -> editTextSize.setText(String.valueOf(finalTextSize)));
                    } else {
                        disableChangeButtons();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    disableChangeButtons();
                }
            }).start();
        });

        return binding.getRoot();
    }

    private void disableChangeButtons(){
        textSizeIncrease.setVisibility(View.INVISIBLE);
        textSizeDecrease.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            int textSize = Integer.parseInt(charSequence.toString());
            propertiesManager.setTextSize(textSize);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
