package com.blueapps.egyptianwriter.editor.vocab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.layoutadapter.ButtonAdapter;
import com.blueapps.egyptianwriter.layoutadapter.GridAdapter;
import com.blueapps.egyptianwriter.learning.LearningActivity;

import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Collections;

public class VocabEditorActivity extends AppCompatActivity implements VocabListener{

    private VocabFileMaster vocabFileMaster;
    private String name = "";
    private String filename = "";

    // Views
    private View root;
    private ConstraintLayout noVocab;
    private RecyclerView cardGrid;

    // Constants
    public static final String EXTRA_INDEX = "index";
    public static final String EXTRA_LENGTH = "length";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_FILE_NAME = "filename";
    public static final String EXTRA_CARDS = "cards";

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
        filename = intent.getStringExtra(DocumentFragment.KEY_FILE_NAME);

        // Set names for Views
        root = binding.getRoot();
        TextView vocabularyTitle = binding.vocabularyTitle;
        ImageButton buttonBack = binding.buttonBack;
        noVocab = binding.noVocabCardsContainer;
        cardGrid = binding.cardGrid;
        TextView vocabCardsTitle = binding.vocabCardsTitle;
        Button buttonAdd = binding.addVocabCard;
        ImageButton buttonAddSmall = binding.addVocabCardSmall;
        Button buttonPractice = binding.practiseButton;

        // Set up vocab cards grid
        vocabFileMaster = new VocabFileMaster(this, filename);
        vocabFileMaster.extractData();

        // Set up grid
        VocabCardGridAdapter adapter = new VocabCardGridAdapter(this, getCards(vocabFileMaster));
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
            addCard();
        });
        buttonAddSmall.setOnClickListener(view -> {
            addCard();
        });

        buttonPractice.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, LearningActivity.class);

            // Prepare cards for learning activity
            ArrayList<Card> cards = vocabFileMaster.getCards();
            Collections.shuffle(cards);

            intent1.putExtra(EXTRA_CARDS, convertArrayList(cards));
            this.startActivity(intent1);
        });

    }

    private ArrayList<Card> getCards(VocabFileMaster vocabFileMaster){
        ArrayList<Card> fileGridData = vocabFileMaster.getCards();

        if (fileGridData.isEmpty()){
            noVocab.setVisibility(View.VISIBLE);
            cardGrid.setVisibility(View.GONE);
        } else {
            noVocab.setVisibility(View.GONE);
            cardGrid.setVisibility(View.VISIBLE);
        }

        return fileGridData;
    }

    private void addCard(){
        vocabFileMaster.addCard(new SignCard(vocabFileMaster.getCards().size()));
        ArrayList<Card> cards = getCards(vocabFileMaster);
        VocabCardGridAdapter adapter = new VocabCardGridAdapter(this, cards);
        adapter.removeVocabListeners();
        adapter.addVocabListener(this);
        cardGrid.setAdapter(adapter);
    }

    // Listeners

    // Vocab Listener
    @Override
    public void onOpen(int index) {
        ArrayList<Card> cards = vocabFileMaster.getCards();

        Intent intent = new Intent(this, CardEditorActivity.class);
        // Add extras
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_FILE_NAME, filename);
        intent.putExtra(EXTRA_INDEX, index);
        intent.putExtra(EXTRA_LENGTH, cards.size());
        intent.putExtra(EXTRA_CARDS, convertArrayList(cards));
        this.startActivity(intent);
    }

    public static Parcelable[] convertArrayList(@UnknownNullability ArrayList<Card> arrayList){
        Parcelable[] array = new Card[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++)
            array[i] = arrayList.get(i);
        return array;
    }
}