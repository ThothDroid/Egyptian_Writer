package com.blueapps.egyptianwriter.editor.vocab.cards;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

    private SignCard card;

    // Views
    private ImageView signView;

    public SignCardViewFragment(SignCard card) {
        this.card = card;
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