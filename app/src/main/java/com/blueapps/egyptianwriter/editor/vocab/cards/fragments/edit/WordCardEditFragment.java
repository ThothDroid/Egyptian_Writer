package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.edit;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentWordCardEditBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.WordCard;

public class WordCardEditFragment extends Fragment {

    private FragmentWordCardEditBinding binding;
    private static final String TAG = "WordCardEditFragment";

    private WordCard card;
    private CardListener cardListener;

    // Views

    // Constants
    public static final String ARG_CARD = "card";

    public WordCardEditFragment() {
        // Required empty public constructor
    }

    public static WordCardEditFragment newInstance(WordCard card) {
        WordCardEditFragment fragment = new WordCardEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, card);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Parcelable parcelable = getArguments().getParcelable(ARG_CARD);
            if (parcelable instanceof WordCard){
                card = (WordCard) parcelable;
            }
        }
        if (card == null) Log.e(TAG, "Argument Card for WordCardEditFragment is null!\nProbably something got wrong with passing arguments to Fragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWordCardEditBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // init Views


        // init listener
        if (getActivity() instanceof CardListener){
            cardListener = (CardListener) getActivity();
        } else {
            throw new RuntimeException("Activity must implement CardListener interface");
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}