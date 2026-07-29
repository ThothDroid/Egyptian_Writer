package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.learn;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.TranskriptionManager;
import com.blueapps.egyptianwriter.databinding.FragmentSignCardLearnBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.learning.LearningListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SignCardLearnFragment extends Fragment {

    private FragmentSignCardLearnBinding binding;
    private static final String TAG = "SignCardLearnFragment";

    private SignCard card;
    private LearningListener listener;

    private boolean textChanged = false;
    private int textStart = 0;
    private int textEnd = 0;
    private int textBefore = 0;
    private String text = "";

    private String userInput = "";

    // Views
    private CardView cardView;
    private ImageView signImage;
    private TextView description;
    private TextView descriptionLabel;
    private Button showDescription;
    private TextView transcriptionLabel;
    private EditText transcription;
    private TextView transcriptionText;
    private TextView transcriptionUserInput;
    private TextView transcriptionUserInputLabel;
    private TextView transcriptionAnswerLabel;
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
        cardView = binding.cardView;
        signImage = binding.learningSign;
        description = binding.learningDescription;
        descriptionLabel = binding.descriptionTitle;
        showDescription = binding.buttonShowAnswer;
        transcriptionLabel = binding.transcriptionTitle;
        transcription = binding.learningTranscription;
        transcriptionText = binding.transcriptionAnswer;
        transcriptionUserInput = binding.transcriptionUserAnswer;
        transcriptionUserInputLabel = binding.userAnswerLabel;
        transcriptionAnswerLabel = binding.answerLabel;
        checkButton = binding.buttonCheckAnswer;

        if (getActivity() instanceof LearningListener) {
            listener = (LearningListener) getActivity();
        } else {
            throw new RuntimeException("Activity must implement LearningListener");
        }

        // Set data to views
        if (card != null) {
            try {
                signImage.setImageDrawable(card.getSign(this.getContext()));
            } catch (XmlPullParserException | IOException e) {
                // TODO: Error handling
                throw new RuntimeException(e);
            }
            description.setText(card.getDescription());
            transcriptionText.setText(TranskriptionManager.convertTranscriptionItalic(card.getTranscription())); // Set the correct transcription text
        }

        description.setVisibility(View.GONE); // Hide description initially
        transcriptionLabel.setVisibility(View.GONE); // Hide transcription label initially
        transcription.setVisibility(View.GONE); // Hide transcription initially
        checkButton.setVisibility(View.GONE); // Hide check button initially
        transcriptionText.setVisibility(View.GONE); // Hide transcription text initially
        transcriptionUserInput.setVisibility(View.GONE); // Hide user input text initially
        transcriptionUserInputLabel.setVisibility(View.GONE); // Hide user input label initially
        transcriptionAnswerLabel.setVisibility(View.GONE); // Hide answer label initially

        showDescription.setOnClickListener(v -> {
            description.setVisibility(View.VISIBLE); // Show description when button is clicked
            showDescription.setVisibility(View.GONE); // Hide the button after showing the description
            descriptionLabel.setVisibility(View.GONE);
            transcriptionLabel.setVisibility(View.VISIBLE); // Show transcription label
            transcription.setVisibility(View.VISIBLE); // Show transcription
            checkButton.setVisibility(View.VISIBLE); // Show check button
        });

        checkButton.setEnabled(false);
        checkButton.setClickable(false);
        checkButton.setFocusable(false);

        transcription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!textChanged) {
                    textStart = start;
                    textEnd = start + count;
                    textBefore = start + before;
                    text = s.subSequence(textStart, textEnd).toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!textChanged){
                    textChanged = true;

                    s.replace(textStart, textEnd, TranskriptionManager.convertTranscription(text));

                    // Keep track on the unformatted code
                    userInput = userInput.substring(0, textStart) + text
                            + userInput.substring(textBefore);

                    textChanged = false;
                }

                boolean enabled = !s.toString().trim().isEmpty();
                checkButton.setEnabled(enabled);
                checkButton.setClickable(enabled);
                checkButton.setFocusable(enabled);
            }
        });

        checkButton.setOnClickListener(v -> {
            transcriptionLabel.setVisibility(View.GONE); // Hide transcription label
            transcription.setVisibility(View.GONE); // Hide transcription
            checkButton.setVisibility(View.GONE); // Hide check button
            transcriptionText.setVisibility(View.VISIBLE); // Show transcription text

            transcriptionUserInput.setText(TranskriptionManager.convertTranscriptionItalic(userInput));

            // Adjust margin
            changeLayoutMargin(cardView, getResources(), -1, -1, -1, 24); // 24dp bottom margin


            if (checkAnswer(userInput, card.getTranscription())) {
                // inform listener
                listener.onCorrectAnswer();

                // Adjust margin
                changeLayoutMargin(transcriptionAnswerLabel, getResources(), 24, -1, -1, -1); // 24dp bottom margin

                transcriptionAnswerLabel.setVisibility(View.VISIBLE); // Show answer label
                transcriptionAnswerLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.l_text_color_right));

                cardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.l_background_right));
                signImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.l_text_color_right));
                description.setTextColor(ContextCompat.getColor(getContext(), R.color.l_text_color_right));
            } else {
                // inform listener
                listener.onIncorrectAnswer();

                transcriptionUserInput.setVisibility(View.VISIBLE); // Show user input text
                transcriptionUserInputLabel.setVisibility(View.VISIBLE); // Show user input label
                transcriptionAnswerLabel.setVisibility(View.VISIBLE); // Show answer label

                cardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.l_background_false));
                signImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.l_text_color_false));
                description.setTextColor(ContextCompat.getColor(getContext(), R.color.l_text_color_false));
            }
        });

        return rootView;
    }

    public static boolean checkAnswer(String userInput, String correctAnswer) {

        // remove leading whitespaces
        userInput = userInput.trim();
        correctAnswer = correctAnswer.trim();

        return userInput.equals(correctAnswer);
    }


    /**
     * This function changes the layout margins of a view inside an ConstrainedLayout.
     * If you do not want to change a specific margin, for example the bottom margin, give it the value -1.
     * All values have the unit dp.
     *
     * @param view The view to change the margin
     * @param res The resources given by Application.getResources()
     * @param top The top margin
     * @param left The left margin
     * @param right The right margin
     * @param bottom The bottom margin
     */
    public static void changeLayoutMargin(@NonNull View view, Resources res, int top, int left, int right, int bottom){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();

        if (top != -1) layoutParams.topMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, top, res.getDisplayMetrics());

        if (left != -1) layoutParams.leftMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, left, res.getDisplayMetrics());

        if (right != -1) layoutParams.rightMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, right, res.getDisplayMetrics());

        if (bottom != -1) layoutParams.bottomMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, bottom, res.getDisplayMetrics());

        view.setLayoutParams(layoutParams);
    }
}