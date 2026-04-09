package com.blueapps.egyptianwriter.editor.vocab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.dashboard.documents.DocumentFragment;
import com.blueapps.egyptianwriter.databinding.ActivityVocabEditorBinding;
import com.blueapps.egyptianwriter.editor.vocab.cardeditor.CardEditorActivity;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.layoutadapter.ButtonAdapter;
import com.blueapps.egyptianwriter.layoutadapter.GridAdapter;

import java.util.ArrayList;

public class VocabEditorActivity extends AppCompatActivity implements VocabListener{

    private FileMaster fileMaster;
    private String name = "";

    // Views
    private View root;
    private ConstraintLayout noVocab;
    private RecyclerView cardGrid;

    // Constants
    public static final String EXTRA_INDEX = "index";
    public static final String EXTRA_LENGTH = "length";
    public static final String EXTRA_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create non fullscreen layout
        ActivityVocabEditorBinding binding = ActivityVocabEditorBinding.inflate(getLayoutInflater());
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
        name = intent.getStringExtra(DocumentFragment.KEY_NAME);
        String filename = intent.getStringExtra(DocumentFragment.KEY_FILE_NAME);

        // Set names for Views
        root = binding.getRoot();
        TextView vocabularyTitle = binding.vocabularyTitle;
        ImageButton buttonBack = binding.buttonBack;
        noVocab = binding.noVocabCardsContainer;
        cardGrid = binding.cardGrid;
        TextView vocabCardsTitle = binding.vocabCardsTitle;
        Button buttonAdd = binding.addVocabCard;
        ImageButton buttonAddSmall = binding.addVocabCardSmall;

        // Set up vocab cards grid
        fileMaster = new FileMaster(this, filename);
        fileMaster.extractData();

        // Set up grid
        VocabCardGridAdapter adapter = new VocabCardGridAdapter(this, getCards(fileMaster));
        adapter.removeVocabListeners();
        adapter.addVocabListener(this);
        GridLayoutManager gridManager = new GridLayoutManager(this, 2);

        vocabularyTitle.setText(name);
        buttonBack.setOnClickListener(view -> finish());

        // adapt layout
        new GridAdapter(this, cardGrid, 200, gridManager);
        new ButtonAdapter(this, buttonAdd, buttonAddSmall, vocabCardsTitle, binding.main, 34);    // Padding left+right+middle = 16dp + 16dp + 2dp

        cardGrid.setLayoutManager(gridManager);
        cardGrid.setAdapter(adapter);

        buttonAdd.setOnClickListener(view -> {
            //addCard();
        });
        buttonAddSmall.setOnClickListener(view -> {
            //addCard();
        });

    }

    private ArrayList<Card> getCards(FileMaster fileMaster){
        ArrayList<Card> fileGridData = fileMaster.getCards();

        if (fileGridData.isEmpty()){
            noVocab.setVisibility(View.VISIBLE);
            cardGrid.setVisibility(View.GONE);
        } else {
            noVocab.setVisibility(View.GONE);
            cardGrid.setVisibility(View.VISIBLE);
        }

        return fileGridData;
    }

    // Listeners

    // Vocab Listener
    @Override
    public void onOpen(int index) {
        ArrayList<Card> cards = fileMaster.getCards();

        Intent intent = new Intent(this, CardEditorActivity.class);
        // Add extras
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_INDEX, index);
        intent.putExtra(EXTRA_LENGTH, cards.size());
        this.startActivity(intent);
    }
}