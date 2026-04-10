package com.blueapps.egyptianwriter.editor.vocab.cards;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blueapps.egyptianwriter.databinding.FragmentSignCardViewBinding;
import com.blueapps.signprovider.SignProvider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SignCardViewFragment extends Fragment {

    private FragmentSignCardViewBinding binding;
    private static final String TAG = "SignCardViewFragment";

    private SignCard card;

    // Views
    private ImageView signView;

    // Constants
    public static final String ARG_CARD = "card";

    public SignCardViewFragment() {
        // Required empty public constructor
    }

    public static SignCardViewFragment newInstance(SignCard card) {
        SignCardViewFragment fragment = new SignCardViewFragment();
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
            if (parcelable instanceof SignCard){
                card = (SignCard) parcelable;
            }
        }
        if (card == null) Log.e(TAG, "Argument Card for SignCardViewFragment is null!\nProbably something got wrong with passing arguments to Fragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignCardViewBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // init Views
        signView = binding.sign;

        // init Image Drawable
        try {
            signView.setImageDrawable(card.getSign(getContext()));
        } catch (XmlPullParserException | IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}