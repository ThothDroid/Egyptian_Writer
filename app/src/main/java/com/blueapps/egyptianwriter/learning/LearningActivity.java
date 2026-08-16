package com.blueapps.egyptianwriter.learning;

import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_CARDS;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.ActivityLearningBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.editor.vocab.cards.fragments.learn.SignCardLearnFragment;

import java.util.ArrayList;
import java.util.Objects;

public class LearningActivity extends AppCompatActivity implements LearningListener {

    private static final String TAG = "LearningActivity";
    private ActivityLearningBinding binding;

    private int[] results;
    private int index = 0;
    private ResultFragment resultFragment;

    // Views
    private ProgressBar progressBar;
    private Button nextButton;
    private Button finishButton;
    private Button skipButton;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get Extras
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(EXTRA_CARDS);
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(parcelables).length; i++){
            if (parcelables[i] instanceof Card) {
                cards.add((Card) parcelables[i]);
            }
        }

        // Initialize views
        progressBar = binding.progressBar;
        nextButton = binding.buttonNext;
        finishButton = binding.buttonFinish;
        skipButton = binding.buttonSkip;
        viewPager = binding.viewPager2;

        results = new int[cards.size()];

        // Set up ViewPager2 with the cards
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof SignCard) {
                fragments.add(SignCardLearnFragment.newInstance(card));
            }
        }
        resultFragment = ResultFragment.newInstance();
        fragments.add(resultFragment);
        LearningPagerAdapter adapter = new LearningPagerAdapter(this, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false); // Disable swipe navigation

        finishButton.setVisibility(View.GONE); // Hide finish button initially
        nextButton.setVisibility(View.GONE); // Hide next button initially
        skipButton.setVisibility(View.VISIBLE);

        nextButton.setOnClickListener(v -> {
            next(cards);
        });

        skipButton.setOnClickListener(v -> {
            results[index] = 0;
            index++;
            next(cards);
        });

        finishButton.setOnClickListener(v -> {
            this.finish();
        });

    }

    private void next(ArrayList<Card> cards) {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem < cards.size()) {
            viewPager.setCurrentItem(currentItem + 1);
        }

        int progress;
        if (currentItem == cards.size() - 1) {
            resultFragment.setResults(results);
            progress = 100;
            // Show finish button and hide next and skip buttons
            finishButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        } else {
            progress = (100 / cards.size()) * (currentItem + 1);
            // Show skip button and hide next button
            skipButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(progress, true);
        } else {
            progressBar.setProgress(progress);
        }
    }

    @Override
    public void onCorrectAnswer() {
        answer();
        results[index] = 2;
        index++;
    }

    @Override
    public void onIncorrectAnswer() {
        answer();
        results[index] = 1;
        index++;
    }

    private void answer(){
        skipButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
    }
}