package com.blueapps.egyptianwriter.editor.document.properties;

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
import androidx.lifecycle.ViewModelProvider;

import com.blueapps.egyptianwriter.databinding.FragmentSettingsBinding;

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

    // Constants
    public static final String ERROR_WRONG_FORMAT = "Input must be a number!";
    public static final String ERROR_WRONG_RANGE = "Input must be between %d and %d!";



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
                    if (textSize < 998){
                        textSize++;
                        final int finalTextSize = textSize;
                        handler.post(() -> editTextSize.setText(String.valueOf(finalTextSize)));
                    } else {
                        if (textSize != 999) wrongFormat(String.format(ERROR_WRONG_RANGE, 0, 999));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    wrongFormat(ERROR_WRONG_FORMAT);
                }
            }).start();
        });

        textSizeDecrease.setOnClickListener(view -> {
            String sTextSize = String.valueOf(editTextSize.getText());
            new Thread(() -> {
                try {
                    int textSize = Integer.parseInt(sTextSize);
                    if (textSize > 1){
                        textSize--;
                        final int finalTextSize = textSize;
                        handler.post(() -> editTextSize.setText(String.valueOf(finalTextSize)));
                    } else {
                        if (textSize != 1) wrongFormat(String.format(ERROR_WRONG_RANGE, 0, 999));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    wrongFormat(ERROR_WRONG_FORMAT);
                }
            }).start();
        });

        editTextSize.addTextChangedListener(this);

        return binding.getRoot();
    }

    private void wrongFormat(String message){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            textSizeIncrease.setVisibility(View.INVISIBLE);
            textSizeDecrease.setVisibility(View.INVISIBLE);
            editTextSize.setError(message);
        });
    }

    private void rightFormat(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            textSizeIncrease.setVisibility(View.VISIBLE);
            textSizeDecrease.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        new Thread(() -> {
            try {
                int textSize = Integer.parseInt(editable.toString());
                if (textSize > 0 && textSize < 999) {
                    propertiesManager.setTextSize(textSize);
                    rightFormat();
                } else {
                    wrongFormat(String.format(ERROR_WRONG_RANGE, 0, 999));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                wrongFormat(ERROR_WRONG_FORMAT);
            }
        }).start();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
}
