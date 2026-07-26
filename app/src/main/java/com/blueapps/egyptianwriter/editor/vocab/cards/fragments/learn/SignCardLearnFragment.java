package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.learn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.FragmentSignCardEditBinding;
import com.blueapps.egyptianwriter.databinding.FragmentSignCardLearnBinding;
import com.blueapps.egyptianwriter.databinding.FragmentSignCardViewBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.editor.vocab.cards.fragments.view.SignCardViewFragment;

public class SignCardLearnFragment extends Fragment {

    private FragmentSignCardLearnBinding binding;
    private static final String TAG = "SignCardLearnFragment";

    private SignCard card;

    // Constants
    public static final String ARG_CARD = "card";

    public SignCardLearnFragment() {
        // Required empty public constructor
    }

    public static SignCardLearnFragment newInstance(Card card) {
        SignCardLearnFragment fragment = new SignCardLearnFragment();
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
            if (parcelable instanceof SignCard){
                card = (SignCard) parcelable;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignCardLearnBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        return rootView;
    }
}