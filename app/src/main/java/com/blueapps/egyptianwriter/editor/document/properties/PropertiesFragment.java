package com.blueapps.egyptianwriter.editor.document.properties;

import static com.blueapps.egyptianwriter.editor.document.properties.PropertiesManager.VERTICAL_ORIENTATION_MAP;
import static com.blueapps.egyptianwriter.editor.document.properties.PropertiesManager.WRITING_DIRECTION_MAP;
import static com.blueapps.egyptianwriter.editor.document.properties.PropertiesManager.WRITING_LAYOUT_MAP;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.blueapps.egyptianwriter.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PropertiesFragment extends Fragment implements Observer<Integer> {

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

        // Init Enums
        writingLayout = new EnumSettings(new ArrayList<>(Arrays.asList(binding.writingLayoutLines, binding.writingLayoutColumns)));
        verticalOrientation = new EnumSettings(new ArrayList<>(Arrays.asList(binding.verticalOrientationTop, binding.verticalOrientationMiddle, binding.verticalOrientationBottom)));
        writingDirection = new EnumSettings(new ArrayList<>(Arrays.asList(binding.writingDirectionLtr, binding.writingDirectionRtl)));

        propertiesManager.getTextSize().observe(getViewLifecycleOwner(), this);

        // Set initial values
        binding.verticalOrientationTop.setChecked(Objects.equals(propertiesManager.getVerticalOrientation().getValue(), VERTICAL_ORIENTATION_MAP.get("TOP")));
        binding.verticalOrientationMiddle.setChecked(Objects.equals(propertiesManager.getVerticalOrientation().getValue(), VERTICAL_ORIENTATION_MAP.get("MIDDLE")));
        binding.verticalOrientationBottom.setChecked(Objects.equals(propertiesManager.getVerticalOrientation().getValue(), VERTICAL_ORIENTATION_MAP.get("BOTTOM")));

        binding.writingLayoutLines.setChecked(Objects.equals(propertiesManager.getWritingLayout().getValue(), WRITING_LAYOUT_MAP.get("LINES")));
        binding.writingLayoutColumns.setChecked(Objects.equals(propertiesManager.getWritingLayout().getValue(), WRITING_LAYOUT_MAP.get("COLUMNS")));

        binding.writingDirectionLtr.setChecked(Objects.equals(propertiesManager.getWritingDirection().getValue(), WRITING_DIRECTION_MAP.get("LTR")));
        binding.writingDirectionRtl.setChecked(Objects.equals(propertiesManager.getWritingDirection().getValue(), WRITING_DIRECTION_MAP.get("RTL")));

        writingLayout.initListeners();
        verticalOrientation.initListeners();
        writingDirection.initListeners();

        textSizeIncrease.setOnClickListener((view) -> {
            new Thread(() -> {
                int value = propertiesManager.increaseTextSize();
                getActivity().runOnUiThread(() -> setEditTextSize(value));
            }).start();
        });

        textSizeDecrease.setOnClickListener((view) -> {
            new Thread(() -> {
                int value = propertiesManager.decreaseTextSize();
                getActivity().runOnUiThread(() -> setEditTextSize(value));
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

                if (!blockTextSize) {
                    /*new Thread(() -> {
                        String number = String.valueOf(charSequence);
                        try {
                            int numberInt = Integer.parseInt(number);
                            propertiesManager.setTextSize(numberInt);
                        } catch (NumberFormatException ignored) {
                        }
                    }).start();*/
                }
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

    private void setEditTextSize(int value){
        /*blockTextSize = true;
        editTextSize.setText(String.valueOf(value));
        propertiesManager.setTextSize(value);
        blockTextSize = false;*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onChanged(Integer integer) {
        setEditTextSize(integer);
    }
}
