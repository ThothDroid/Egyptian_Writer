package com.blueapps.egyptianwriter.learning;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.editor.vocab.cards.fragments.learn.SignCardLearnFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class LearningPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Card> cards;

    private HashMap<Integer, Integer> results = new HashMap<>();

    public LearningPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Card> cards) {
        super(fragmentActivity);
        this.cards = cards;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == cards.size()) {
            ResultFragment resultFragment =  ResultFragment.newInstance();
            resultFragment.setResults(results);
            return resultFragment;
        }
        Card card = cards.get(position);
        if (card instanceof SignCard) {
            return SignCardLearnFragment.newInstance((SignCard) card);
        }
        throw new IllegalArgumentException("Unsupported card type: " + card.getClass().getSimpleName());
    }

    @Override
    public int getItemCount() {
        return cards.size() + 1; // +1 for the result fragment
    }

    public void addCard(Card card, int position) {
        cards.add(position, card);
        notifyItemInserted(position);
    }

    public void setResults(HashMap<Integer, Integer> results) {
        this.results = results;
        notifyItemChanged(cards.size()); // Notify that the result fragment has changed
    }

}
