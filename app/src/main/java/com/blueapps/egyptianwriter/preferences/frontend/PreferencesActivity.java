package com.blueapps.egyptianwriter.preferences.frontend;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.databinding.ActivityPreferencesBinding;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity {

    private ActivityPreferencesBinding binding;


    // Views
    private ImageButton buttonBack;
    private RecyclerView preferencesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create non fullscreen layout
        binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set names for views
        buttonBack = binding.buttonBack;
        preferencesList = binding.preferencesList;

        // Prepare folder data
        ArrayList<Folder> data = new ArrayList<>();
        data.add(new Folder(Folder.MODE_TOP, "Top Item"));
        data.add(new Folder(Folder.MODE_MIDDLE, "Middle Item"));
        data.add(new Folder(Folder.MODE_MIDDLE, "Another Item"));
        data.add(new Folder(Folder.MODE_MIDDLE, "Another Item"));
        data.add(new Folder(Folder.MODE_MIDDLE, "Another Item"));
        data.add(new Folder(Folder.MODE_BOTTOM, "Bottom Item"));

        // Init RecyclerView
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
        preferencesList.setAdapter(adapter);
        preferencesList.setLayoutManager(new LinearLayoutManager(this));


        buttonBack.setOnClickListener(view -> finish());

    }
}
