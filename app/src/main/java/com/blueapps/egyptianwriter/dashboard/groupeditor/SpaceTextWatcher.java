package com.blueapps.egyptianwriter.dashboard.groupeditor;

import static com.blueapps.groupeditor.Space.TYPE_HIGH;
import static com.blueapps.groupeditor.Space.TYPE_SQUARE;

import android.text.Editable;
import android.text.TextWatcher;

import com.blueapps.groupeditor.Box;
import com.blueapps.groupeditor.GroupEditor;
import com.blueapps.groupeditor.Space;
import com.google.android.material.tabs.TabLayout;

public class SpaceTextWatcher implements TextWatcher {

    private final GroupEditor groupEditor;
    private final TabLayout tabLayout;
    private final int spaceType;
    private final int valueType;

    // Constants
    public static final int VALUE_TYPE_X = 0;
    public static final int VALUE_TYPE_Y = 1;

    public SpaceTextWatcher(GroupEditor groupEditor, TabLayout tabLayout, int spaceType, int valueType){
        this.groupEditor = groupEditor;
        this.tabLayout = tabLayout;
        this.spaceType = spaceType;
        this.valueType = valueType;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            int value = Integer.parseInt(editable.toString());
            Space space = getSpace();
            if (valueType == VALUE_TYPE_X) {
                space.setLeft(value);
            } else if (valueType == VALUE_TYPE_Y) {
                space.setTop(value);
            }
            groupEditor.invalidate();
        } catch (NumberFormatException e) {
            // Ignore text changing, if text is not an integer
            e.printStackTrace();
        }
    }

    private Space getSpace() {
        Box box;
        if (tabLayout.getSelectedTabPosition() == 0) {
            box = groupEditor.getGroup().getFirstBox();
        } else {
            box = groupEditor.getGroup().getSecondBox();
        }
        Space space;
        if (spaceType == TYPE_HIGH) {
            space = box.getHighSpace();
        } else if (spaceType == TYPE_SQUARE) {
            space = box.getSquareSpace();
        } else {
            space = box.getWideSpace();
        }
        return space;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
}
