package com.blueapps.egyptianwriter.learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blueapps.egyptianwriter.databinding.FragmentResultBinding;

import java.util.Arrays;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private static final String TAG = "ResultFragment";

    private int[] results = new int[0];
    private ResultCalculator resultCalculator;

    // Views
    private TextView resultText;

    private TextView allCards;
    private TextView correctCards;
    private TextView incorrectCards;
    private TextView skippedCards;

    private ProgressBar successRate;
    private TextView successRateText;
    private ProgressBar failRate;
    private TextView failRateText;
    private ProgressBar skipRate;
    private TextView skipRateText;

    // Constants

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance() {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Do something
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // init views
        resultText = binding.result;

        allCards = binding.allCardsText;
        correctCards = binding.correctCardsText;
        incorrectCards = binding.incorrectCardsText;
        skippedCards = binding.skippedCardsText;

        successRate = binding.successRateProgress;
        successRateText = binding.successRateText;
        failRate = binding.errorRateProgress;
        failRateText = binding.errorRateText;
        skipRate = binding.skipRateProgress;
        skipRateText = binding.skipRateText;

        setResults(results);

        return rootView;
    }

    public void setResults(int[] results){
        this.results = results;

        resultCalculator = new ResultCalculator(results, 0, 0);

        if (allCards != null) {

            allCards.setText(String.valueOf(resultCalculator.getAllCards()));
            correctCards.setText(String.valueOf(resultCalculator.getCorrectCards()));
            incorrectCards.setText(String.valueOf(resultCalculator.getIncorrectCards()));
            skippedCards.setText(String.valueOf(resultCalculator.getSkippedCards()));

            successRate.setProgress((int) (resultCalculator.getSuccessRate() * 100));
            successRateText.setText(resultCalculator.stringFromRate(resultCalculator.getSuccessRate()));
            failRate.setProgress((int) (resultCalculator.getErrorRate() * 100));
            failRateText.setText(resultCalculator.stringFromRate(resultCalculator.getErrorRate()));
            skipRate.setProgress((int) (resultCalculator.getSkipRate() * 100));
            skipRateText.setText(resultCalculator.stringFromRate(resultCalculator.getSkipRate()));

            StringBuilder s = new StringBuilder();

            s.append("Bearbeitete Karten: ");
            s.append(resultCalculator.getAllCards());
            s.append("\nRichtige Antworten: ");
            s.append(resultCalculator.getCorrectCards());
            s.append("\nFalsche Antworten: ");
            s.append(resultCalculator.getIncorrectCards());
            s.append("\nÜbersprungene Karten: ");
            s.append(resultCalculator.getSkippedCards());
            s.append("\n\nErfolgsquote: ");
            s.append(resultCalculator.getSuccessRate());
            s.append("\nFehlerquote: ");
            s.append(resultCalculator.getErrorRate());
            s.append("\nProzentsatz der übersprungenen Karten: ");
            s.append(resultCalculator.getSkipRate());
            s.append("\n\nGesamte Lernzeit: ");
            s.append(resultCalculator.getTime());
            s.append("\nDurchschnittliche Zeit pro Karte: ");
            s.append(resultCalculator.getAverageTimePerCard());
            s.append("\nSchnellste Antwort: ");
            s.append(resultCalculator.getQuickestAnswer());
            s.append("\n\n\nDEINE BEWERTUNG: ");
            if (resultCalculator.getScore() == ResultCalculator.SCORE_STUDENT) {
                s.append("Schüler!");
            } else if (resultCalculator.getScore() == ResultCalculator.SCORE_SCRIBE) {
                s.append("Schreiber!");
            } else if (resultCalculator.getScore() == ResultCalculator.SCORE_PRIEST) {
                s.append("Priester!");
            } else if (resultCalculator.getScore() == ResultCalculator.SCORE_WESIR) {
                s.append("Wesir!");
            }

            resultText.setText(s.toString());
        }
    }
}