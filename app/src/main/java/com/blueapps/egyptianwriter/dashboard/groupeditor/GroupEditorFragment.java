package com.blueapps.egyptianwriter.dashboard.groupeditor;

import static com.blueapps.egyptianwriter.dashboard.groupeditor.SpaceTextWatcher.VALUE_TYPE_X;
import static com.blueapps.egyptianwriter.dashboard.groupeditor.SpaceTextWatcher.VALUE_TYPE_Y;
import static com.blueapps.groupeditor.Space.TYPE_HIGH;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentGroupEditorBinding;
import com.blueapps.groupeditor.Box;
import com.blueapps.groupeditor.Group;
import com.blueapps.groupeditor.GroupEditor;
import com.blueapps.groupeditor.GroupListener;
import com.google.android.material.tabs.TabLayout;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GroupEditorFragment extends Fragment implements GroupListener {

    private FragmentGroupEditorBinding binding;

    // Views
    private GroupEditor groupEditor;
    private EditText inputId;
    private TextView idText;
    private TabLayout tabLayout;
    private CheckBox showOtherBoxCheckbox;
    // Vertical space
    private EditText vertSpaceX;
    private EditText vertSpaceY;
    private EditText vertSpaceSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding
        binding = FragmentGroupEditorBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // initialize views
        groupEditor = binding.groupEditor;
        inputId = binding.inputId;
        idText = binding.textId;
        tabLayout = binding.boxes;
        showOtherBoxCheckbox = binding.showOtherBox;
        vertSpaceX = binding.vertSpaceXText;
        vertSpaceY = binding.vertSpaceYText;

        // Set dynamic size of groupEditor based on screen size
        int screenWidth = getResources().getDisplayMetrics().widthPixels * 3; // So it remains clear, even at maximum zoom
        groupEditor.setLayoutParams(new FrameLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

        groupEditor.setBoxStrokeWidth((int) (getResources().getDisplayMetrics().density * 6)); // 1dp stroke width

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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                groupEditor.setActiveBox(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        showOtherBoxCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            groupEditor.setShowOtherBox(isChecked);
        });

        groupEditor.post(() -> {
            Box box;
            if (tabLayout.getSelectedTabPosition() == 0) {
                box = groupEditor.getGroup().getFirstBox();
            } else {
                box = groupEditor.getGroup().getSecondBox();
            }
            vertSpaceX.setText(String.valueOf(box.getHighSpace().getRect().left));
            vertSpaceY.setText(String.valueOf(box.getHighSpace().getRect().top));
        });

        vertSpaceX.addTextChangedListener(new SpaceTextWatcher(groupEditor, tabLayout, TYPE_HIGH, VALUE_TYPE_X));
        vertSpaceY.addTextChangedListener(new SpaceTextWatcher(groupEditor, tabLayout, TYPE_HIGH, VALUE_TYPE_Y));

        // better layout for keyboard
        com.blueapps.thothexampleapp.KeyboardUtils.addKeyboardToggleListener(getActivity(), isVisible -> {
            if (inputId.isFocused()) {
                if (isVisible){
                    tabLayout.setVisibility(View.GONE);
                    binding.boxSettings.setVisibility(View.GONE);
                } else {
                    tabLayout.setVisibility(View.VISIBLE);
                    binding.boxSettings.setVisibility(View.VISIBLE);
                }
            } else {
                if (isVisible) {
                    inputId.setVisibility(View.GONE);
                    binding.titleId.setVisibility(View.GONE);
                    binding.textId.setVisibility(View.GONE);
                    showOtherBoxCheckbox.setVisibility(View.GONE);
                    binding.viewGuideline.setGuidelinePercent(0.3f);
                } else {
                    inputId.setVisibility(View.VISIBLE);
                    binding.titleId.setVisibility(View.VISIBLE);
                    binding.textId.setVisibility(View.VISIBLE);
                    showOtherBoxCheckbox.setVisibility(View.VISIBLE);
                    binding.viewGuideline.setGuidelinePercent(0.5f);
                }
            }
        });

        groupEditor.addGroupListener(this);

        return rootView;

    }

    @Override
    public void onGroupChanged(Group group) {
        idText.setText(group.getSignId());
    }
}
