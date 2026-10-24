package com.blueapps.egyptianwriter.editor.vocab.cardeditor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class EditPagerAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> editFragments;

    public EditPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> editFragments) {
        super(fragmentActivity);
        this.editFragments = editFragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return editFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return editFragments.size();
    }
}
