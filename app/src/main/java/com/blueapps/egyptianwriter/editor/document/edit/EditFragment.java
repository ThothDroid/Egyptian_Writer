package com.blueapps.egyptianwriter.editor.document.edit;

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
import androidx.lifecycle.ViewModelProvider;

import com.blueapps.egyptianwriter.databinding.FragmentEditBinding;
import com.blueapps.egyptianwriter.editor.document.EditorViewModel;
import com.blueapps.egyptianwriter.issuecenter.IssueCenter;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;

    private EditorViewModel viewModel;


    // Views
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater, container, false);

        // Get ViewModel
        viewModel = new ViewModelProvider(getActivity()).get(EditorViewModel.class);

        // Set names for Views
        editText = binding.editText;

        editText.setText(viewModel.getFileMaster().getMdc());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (!IssueCenter.ISSUE_CENTER.isFatalIssue())
                    viewModel.getFileMaster().setMdc(editText.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        viewModel.getFileMaster().setMdc(editText.getText().toString());
    }
}
