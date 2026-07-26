package com.blueapps.egyptianwriter.learning;

import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_CARDS;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.documents.DocumentFragment;
import com.blueapps.egyptianwriter.databinding.ActivityLearningBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;

public class LearningActivity extends AppCompatActivity {

    private static final String TAG = "LearningActivity";
    private ActivityLearningBinding binding;

    // Views
    private ProgressBar progressBar;
    private MaterialButton nextButton;
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
        viewPager = binding.viewPager2;

        nextButton.setOnClickListener(v -> {
            /*int currentItem = viewPager.getCurrentItem();
            if (currentItem < cards.size() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }*/
            int progress = progressBar.getProgress();
            progressBar.setProgress(progress + 10, true);
        });

    }
}