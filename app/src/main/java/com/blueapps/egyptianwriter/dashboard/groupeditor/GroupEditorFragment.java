package com.blueapps.egyptianwriter.dashboard.groupeditor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentGroupEditorBinding;
import com.blueapps.groupeditor.GroupEditor;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GroupEditorFragment extends Fragment {

    private FragmentGroupEditorBinding binding;

    // Views
    private GroupEditor groupEditor;
    private EditText inputId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding
        binding = FragmentGroupEditorBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // initialize views
        groupEditor = binding.groupEditor;
        inputId = binding.inputId;

        inputId.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    groupEditor.init(editable.toString());
                } catch (XmlPullParserException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        return rootView;

    }
}
