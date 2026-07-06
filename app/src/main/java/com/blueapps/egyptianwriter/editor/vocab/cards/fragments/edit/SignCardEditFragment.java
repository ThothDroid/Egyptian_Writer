package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.edit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blueapps.egyptianwriter.databinding.FragmentSignCardEditBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;

public class SignCardEditFragment extends Fragment {

    private FragmentSignCardEditBinding binding;
    private static final String TAG = "SignCardEditFragment";

    private SignCard card;

    // Constants
    public static final String ARG_CARD = "card";

    public SignCardEditFragment() {
        // Required empty public constructor
    }

    public static SignCardEditFragment newInstance(SignCard card) {
        SignCardEditFragment fragment = new SignCardEditFragment();
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
        if (card == null) Log.e(TAG, "Argument Card for SignCardViewFragment is null!\nProbably something got wrong with passing arguments to Fragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignCardEditBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}