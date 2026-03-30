package com.blueapps.egyptianwriter.editor.vocab.cardeditor;

import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_INDEX;
import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_LENGTH;
import static com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity.EXTRA_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.documents.DocumentFragment;
import com.blueapps.egyptianwriter.databinding.ActivityCardEditorBinding;

public class CardEditorActivity extends AppCompatActivity {

    private ActivityCardEditorBinding binding;

    // Views
    private View root;
    private TextView cardTitle;
    private TextView cardNumber;
    private ImageButton buttonBack;

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

        // Set names for Views
        root = binding.getRoot();
        cardTitle = binding.cardTitle;
        cardNumber = binding.cardNumber;
        buttonBack = binding.buttonBack;

        cardTitle.setText(name);
        cardNumber.setText(String.format(getString(R.string.vocab_card_number_template), index + 1, length));

        buttonBack.setOnClickListener(view -> {
            finish();
        });

    }
}
