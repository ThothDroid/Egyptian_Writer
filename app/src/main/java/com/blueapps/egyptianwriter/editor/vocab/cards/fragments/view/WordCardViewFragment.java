package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.view;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentWordCardViewBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.WordCard;
import com.blueapps.thoth.ThothView;

public class WordCardViewFragment extends Fragment {

    private FragmentWordCardViewBinding binding;
    private static final String TAG = "WordCardViewFragment";

    private WordCard card;

    // Views
    private ThothView wordView;

    // Constants
    public static final String ARG_CARD = "card";

    public WordCardViewFragment() {
        // Required empty public constructor
    }

    public static WordCardViewFragment newInstance(WordCard card) {
        WordCardViewFragment fragment = new WordCardViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, card);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Parcelable parcelable = getArguments().getParcelable(ARG_CARD);
            if (parcelable instanceof WordCard){
                card = (WordCard) parcelable;
            }
        }
        if (card == null) Log.e(TAG, "Argument Card for WordCardViewFragment is null!\nProbably something got wrong with passing arguments to Fragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWordCardViewBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // init Views
        wordView = binding.hieroglyphicWord;

        // Set card data
        setCard(card);

        return rootView;
    }

    public void setCard(WordCard card){
        this.card = card;
        if (binding != null) {
            wordView.setMdCText(card.getWord());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}