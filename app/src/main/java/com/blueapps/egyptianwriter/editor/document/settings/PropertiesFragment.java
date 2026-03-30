package com.blueapps.egyptianwriter.editor.document.settings;

import android.os.Bundle;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.blueapps.egyptianwriter.CheckableImageButton;
import com.blueapps.egyptianwriter.databinding.FragmentSettingsBinding;
import com.blueapps.egyptianwriter.editor.document.EditorViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class PropertiesFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private PropertiesManager propertiesManager;

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

        // Init Enums
        writingLayout = new EnumSettings(new ArrayList<>(Arrays.asList(binding.writingLayoutLines, binding.writingLayoutColumns)));
        verticalOrientation = new EnumSettings(new ArrayList<>(Arrays.asList(binding.verticalOrientationTop, binding.verticalOrientationMiddle, binding.verticalOrientationBottom)));
        writingDirection = new EnumSettings(new ArrayList<>(Arrays.asList(binding.writingDirectionLtr, binding.writingDirectionRtl)));

        textSizeIncrease.setOnClickListener((view) -> {
            new Thread(() -> {
                String value = String.valueOf(propertiesManager.increaseTextSize());
                getActivity().runOnUiThread(() -> editTextSize.setText(value));
            }).start();
        });

        textSizeDecrease.setOnClickListener((view) -> {
            new Thread(() -> {
                String value = String.valueOf(propertiesManager.decreaseTextSize());
                getActivity().runOnUiThread(() -> editTextSize.setText(value));
            }).start();
        });

        editTextSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                new Thread(() -> {
                    String number = String.valueOf(charSequence);
                    try {
                        int numberInt = Integer.parseInt(number);
                        propertiesManager.setTextSize(numberInt);
                    } catch (NumberFormatException ignored) {}
                }).start();
            }
        });

        writingLayout.addListener((index -> {
            propertiesManager.setWritingLayout(index);
        }));
        verticalOrientation.addListener((index -> {
            propertiesManager.setVerticalOrientation(index);
        }));
        writingDirection.addListener((index -> {
            propertiesManager.setWritingDirection(index);
        }));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
