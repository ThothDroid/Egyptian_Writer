package com.blueapps.egyptianwriter.dashboard.vocab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.filegrid.addfile.AddMenu;
import com.blueapps.egyptianwriter.dashboard.filegrid.addfile.AddMenuListener;
import com.blueapps.egyptianwriter.dashboard.filegrid.FileGridAdapter;
import com.blueapps.egyptianwriter.dashboard.filegrid.FileGridData;
import com.blueapps.egyptianwriter.dashboard.filegrid.FileListener;
import com.blueapps.egyptianwriter.dashboard.filegrid.FileManager;
import com.blueapps.egyptianwriter.databinding.FragmentVocabBinding;
import com.blueapps.egyptianwriter.editor.vocab.VocabEditorActivity;
import com.blueapps.egyptianwriter.fileimport.ImportManager;
import com.blueapps.egyptianwriter.issuecenter.PopupListener;
import com.blueapps.egyptianwriter.issuecenter.StandardPopup;
import com.blueapps.egyptianwriter.layoutadapter.ButtonAdapter;
import com.blueapps.egyptianwriter.layoutadapter.GridAdapter;

import java.util.ArrayList;

public class VocabFragment extends Fragment implements FileListener, AddMenuListener, PopupListener {

    private FragmentVocabBinding binding;
    private static final String TAG = "VocabFragment";

    private FileManager fileManager;
    private ImportManager importManager;

    // Views
    private ConstraintLayout main;
    private RecyclerView vocabularyGrid;
    private Button addVocab;
    private ImageButton addVocabSmall;
    private TextView vocabularyTitle;
    private ConstraintLayout noVocabContainer;

    // Constants
    public static final String KEY_NAME = "name";
    public static final String KEY_FILE_NAME = "filename";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding
        binding = FragmentVocabBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Set names for Views
        main = binding.main;
        vocabularyGrid = binding.vocabularyGrid;
        addVocab = binding.addVocab;
        addVocabSmall = binding.addVocabSmall;
        vocabularyTitle = binding.vocabularyTitle;
        noVocabContainer = binding.noVocabContainer;

        // Set up document grid
        fileManager = new FileManager(getContext(), "Vocabulary", ".ewvs"); // Egyptian Writer Vocabulary Set

        // Set up import
        importManager = new ImportManager(getContext(), fileManager);
        importManager.setActivityResultLauncher(registerForActivityResult(new ActivityResultContracts.OpenDocument(), importManager));

        // Set up grid
        FileGridAdapter adapter = new FileGridAdapter(getContext(), getDocuments(fileManager), R.layout.vocab_box_item);
        adapter.removeFileListeners();
        adapter.addFileListener(this);
        GridLayoutManager gridManager = new GridLayoutManager(getContext(), 2);

        // adapt layout
        new GridAdapter(getContext(), vocabularyGrid, 200, gridManager);
        new ButtonAdapter(getContext(), addVocab, addVocabSmall, vocabularyTitle, main, 34);    // Padding left+right+middle = 16dp + 16dp + 2dp

        vocabularyGrid.setLayoutManager(gridManager);
        vocabularyGrid.setAdapter(adapter);

        addVocab.setOnClickListener(view -> {
            addVocabulary();
        });
        addVocabSmall.setOnClickListener(view -> {
            addVocabulary();
        });

        return rootView;

    }

    private void addVocabulary(){
        AddMenu addMenu = new AddMenu(getContext(), getString(R.string.menu_item_import_vocab), getString(R.string.menu_item_create_vocab));
        int[] location = new int[2];
        addVocab.getLocationOnScreen(location);
        addMenu.setPosition(location[0], location[1] + 40);
        addMenu.addAddMenuListener(this);
        addMenu.show();
    }


    private ArrayList<FileGridData> getDocuments(FileManager fileManager){
        ArrayList<FileGridData> fileGridData = fileManager.getFiles();

        if (fileGridData.isEmpty()){
            noVocabContainer.setVisibility(View.VISIBLE);
            vocabularyGrid.setVisibility(View.GONE);
        } else {
            noVocabContainer.setVisibility(View.GONE);
            vocabularyGrid.setVisibility(View.VISIBLE);
        }

        return fileGridData;
    }

    // Listeners

    // FileListener
    @Override
    public void OnDeleteFile(String name) {
        StandardPopup standardPopup = new StandardPopup(getContext(), StandardPopup.MODE_WARNING,
                getResources().getString(R.string.delete_vocab),
                String.format(getResources().getString(R.string.delete_message_vocab), name),
                getResources().getString(R.string.button_cancel),
                getResources().getString(R.string.button_delete));
        standardPopup.addPopupListener(this);
        standardPopup.setFilename(name);
        standardPopup.show();
    }

    @Override
    public void OnExportFile(String name) {

    }

    @Override
    public void OnOpenFile(String name) {
        Intent myIntent = new Intent(getActivity(), VocabEditorActivity.class);
        // Add extras
        myIntent.putExtra(KEY_NAME, name);
        myIntent.putExtra(KEY_FILE_NAME, name + ".ewvs");
        VocabFragment.this.startActivity(myIntent);
    }

    // Add Menu Listener
    @Override
    public void OnAboard() {

    }

    @Override
    public void OnImportFile() {

    }

    @Override
    public void OnCreateFile() {
        getActivity().runOnUiThread(() -> {

            StandardPopup standardPopup = new StandardPopup(getContext(), StandardPopup.MODE_ENTER_FILENAME,
                    getResources().getString(R.string.new_vocab_set_title),
                    getResources().getString(R.string.new_vocab_set_description),
                    getResources().getString(R.string.button_cancel),
                    getResources().getString(R.string.button_create));

            standardPopup.addFileNames(fileManager.getNames());
            standardPopup.setFilename(standardPopup.suggestFileName());
            standardPopup.show();
            standardPopup.addPopupListener(VocabFragment.this);
        });
    }

    // Popup Listener
    @Override
    public void OnCancel() {

    }

    @Override
    public void OnSelected(String name) {
        fileManager.addFile(name);
        FileGridAdapter adapter = new FileGridAdapter(getContext(), getDocuments(fileManager), R.layout.vocab_box_item);
        adapter.removeFileListeners();
        adapter.addFileListener(this);
        vocabularyGrid.setAdapter(adapter);
    }

    @Override
    public void OnConfirmed(String name) {
        fileManager.deleteFile(name);
        FileGridAdapter adapter = new FileGridAdapter(getContext(), getDocuments(fileManager), R.layout.vocab_box_item);
        adapter.removeFileListeners();
        adapter.addFileListener(this);
        vocabularyGrid.setAdapter(adapter);
    }
}
