package com.blueapps.egyptianwriter.editor.vocab.cardeditor;

import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_CARDS;
import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_INDEX;
import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_LENGTH;
import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.ActivityCardEditorBinding;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.editor.vocab.cards.fragments.view.SignCardViewFragment;

import java.util.ArrayList;
import java.util.Objects;

public class CardEditorActivity extends AppCompatActivity {

    private ActivityCardEditorBinding binding;

    // Views
    private View root;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create non fullscreen layout
        binding = ActivityCardEditorBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        // Optimize for software keyboard on android 15+
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());

            int topInset = Math.max(navInsets.top, systemBars.top);
            int bottomInset = Math.max(imeInsets.bottom, navInsets.bottom);

            root.setPadding(navInsets.left, topInset, navInsets.right, bottomInset);

            return insets;
        });

        // get Extras
        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);
        int index = intent.getIntExtra(EXTRA_INDEX, 0);
        int length = intent.getIntExtra(EXTRA_LENGTH, 1);
        Parcelable[] parcelables = intent.getParcelableArrayExtra(EXTRA_CARDS);
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(parcelables).length; i++){
            if (parcelables[i] instanceof Card) {
                cards.add((Card) parcelables[i]);
            }
        }

        // Set names for Views
        root = binding.getRoot();
        TextView cardTitle = binding.cardTitle;
        TextView cardNumber = binding.cardNumber;
        ImageButton buttonBack = binding.buttonBack;
        viewPager2 = binding.viewPager;

        cardTitle.setText(name);
        cardNumber.setText(String.format(getString(R.string.vocab_card_number_template), index + 1, length));

        buttonBack.setOnClickListener(view -> {
            finish();
        });

        // init viewPager
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++){
            Card card = cards.get(i);
            if (card instanceof SignCard) {
                fragments.add(SignCardViewFragment.newInstance((SignCard) card));
            }
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragments);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(index, false);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                cardNumber.setText(String.format(getString(R.string.vocab_card_number_template), position + 1, length));
            }
        });

    }
}
