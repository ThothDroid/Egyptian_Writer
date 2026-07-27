package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.learn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueapps.egyptianwriter.databinding.FragmentSignCardLearnBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SignCardLearnFragment extends Fragment {

    private FragmentSignCardLearnBinding binding;
    private static final String TAG = "SignCardLearnFragment";

    private SignCard card;

    // Views
    private ImageView signImage;
    private TextView description;
    private TextView descriptionLabel;
    private Button showDescription;
    private TextView transcriptionLabel;
    private EditText transcription;
    private Button checkButton;

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

        // Initialize views
        signImage = binding.learningSign;
        description = binding.learningDescription;
        descriptionLabel = binding.descriptionTitle;
        showDescription = binding.buttonShowAnswer;
        transcriptionLabel = binding.transcriptionTitle;
        transcription = binding.learningTranscription;
        checkButton = binding.buttonCheckAnswer;

        // Set data to views
        if (card != null) {
            try {
                signImage.setImageDrawable(card.getSign(this.getContext()));
            } catch (XmlPullParserException | IOException e) {
                // TODO: Error handling
                throw new RuntimeException(e);
            }
            description.setText(card.getDescription());
        }

        description.setVisibility(View.GONE); // Hide description initially
        transcriptionLabel.setVisibility(View.GONE); // Hide transcription label initially
        transcription.setVisibility(View.GONE); // Hide transcription initially
        checkButton.setVisibility(View.GONE); // Hide check button initially

        showDescription.setOnClickListener(v -> {
            description.setVisibility(View.VISIBLE); // Show description when button is clicked
            showDescription.setVisibility(View.GONE); // Hide the button after showing the description
            descriptionLabel.setVisibility(View.GONE);
            transcriptionLabel.setVisibility(View.VISIBLE); // Show transcription label
            transcription.setVisibility(View.VISIBLE); // Show transcription
            checkButton.setVisibility(View.VISIBLE); // Show check button
        });

        checkButton.setOnClickListener(v -> {
            transcriptionLabel.setVisibility(View.GONE); // Hide transcription label
            transcription.setVisibility(View.GONE); // Hide transcription
            checkButton.setVisibility(View.GONE); // Hide check button
            if (checkAnswer(transcription.getText().toString(), card.getTranscription())) {

            } else {

            }
        });

        return rootView;
    }

    public static boolean checkAnswer(String userInput, String correctAnswer) {

        // remove leading whitespaces
        userInput = userInput.trim();
        correctAnswer = correctAnswer.trim();

        return userInput.equalsIgnoreCase(correctAnswer);
    }
}