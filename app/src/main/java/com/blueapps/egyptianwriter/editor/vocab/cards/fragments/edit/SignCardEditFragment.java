package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.edit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueapps.egyptianwriter.TranskriptionManager;
import com.blueapps.egyptianwriter.databinding.FragmentSignCardEditBinding;
import com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.signprovider.SignProvider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SignCardEditFragment extends Fragment {

    private FragmentSignCardEditBinding binding;
    private static final String TAG = "SignCardEditFragment";

    private SignCard card;
    private SignProvider signProvider;
    private VocabFileMaster vocabFileMaster;

    // Views
    private ImageView signView;
    private EditText gardiner;
    private EditText transcription;
    private EditText description;

    // Constants
    public static final String ARG_CARD = "card";
    public static final String EXTRA_FILE_NAME = "filename";

    public SignCardEditFragment() {
        // Required empty public constructor
    }

    public static SignCardEditFragment newInstance(SignCard card, String filename) {
        SignCardEditFragment fragment = new SignCardEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, card);
        args.putString(EXTRA_FILE_NAME, filename);
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
            String filename = getArguments().getString(EXTRA_FILE_NAME);
            if (filename != null) {
                vocabFileMaster = new VocabFileMaster(getContext(), filename);
            }
        }
        if (card == null) Log.e(TAG, "Argument Card for SignCardViewFragment is null!\nProbably something got wrong with passing arguments to Fragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignCardEditBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // init Views
        signView = binding.signImage;
        gardiner = binding.gardinerCode;
        transcription = binding.transcription;
        description = binding.descriptionInput;

        gardiner.setText(card.getSignId());
        transcription.setText(TranskriptionManager.convertTranscription(card.getTranscription()));
        description.setText(card.getDescription());

        // init Image Drawable
        try {
            signView.setImageDrawable(card.getSign(getContext()));
        } catch (XmlPullParserException | IOException | NullPointerException e) {
            e.printStackTrace();
        }

        signProvider = new SignProvider(getContext());
        gardiner.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                // Update Image
                try {
                    signView.setImageDrawable(signProvider.getSign(editable.toString()));
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                // Save changes to card
                card.setSignId(editable.toString());
                vocabFileMaster.setCard(card, card.getIndex());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        transcription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                // Save changes to card
                card.setTranscription(editable.toString());
                vocabFileMaster.setCard(card, card.getIndex());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                // Save changes to card
                card.setDescription(editable.toString());
                vocabFileMaster.setCard(card, card.getIndex());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}