package com.blueapps.egyptianwriter.editor.vocab.cardeditor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> viewFragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> viewFragments) {
        super(fragmentActivity);
        this.viewFragments = viewFragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return viewFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return viewFragments.size();
    }
}
