package com.blueapps.egyptianwriter.editor.vocab.cardeditor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public static String FRAGMENT_COUNT_MISMATCHING = "The size of the viewFragments ArrayList and of the editFragments ArrayList are mismatching! viewFragments.size = %d, editFragments.size = %d";

    private ArrayList<Fragment> viewFragments;
    private ArrayList<Fragment> editFragments;

    private boolean Mode = true;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> viewFragments, ArrayList<Fragment> editFragments) {
        super(fragmentActivity);
        this.viewFragments = viewFragments;
        this.editFragments = editFragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (Mode) {
            return viewFragments.get(position);
        } else {
            return editFragments.get(position);
        }
    }

    @Override
    public int getItemCount() {
        if (viewFragments.size() != editFragments.size()){
            throw new MismatchingNumberException(String.format(FRAGMENT_COUNT_MISMATCHING, viewFragments.size(), editFragments.size()));
        }
        return viewFragments.size();
    }

    public void setMode(boolean Mode){
        this.Mode = Mode;
        notifyDataSetChanged();
    }
}
