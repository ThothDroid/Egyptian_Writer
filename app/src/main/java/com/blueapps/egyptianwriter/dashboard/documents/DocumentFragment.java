package com.blueapps.egyptianwriter.dashboard.documents;

import static com.blueapps.egyptianwriter.dashboard.DashboardActivity.KEY_INTENT;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.blueapps.egyptianwriter.databinding.FragmentDocumentBinding;
import com.blueapps.egyptianwriter.editor.document.DocumentEditorActivity;
import com.blueapps.egyptianwriter.export.FileResultActivity;
import com.blueapps.egyptianwriter.fileimport.ImportListener;
import com.blueapps.egyptianwriter.fileimport.ImportManager;
import com.blueapps.egyptianwriter.issuecenter.PopupListener;
import com.blueapps.egyptianwriter.issuecenter.StandardPopup;
import com.blueapps.egyptianwriter.layoutadapter.ButtonAdapter;
import com.blueapps.egyptianwriter.layoutadapter.GridAdapter;

import java.util.ArrayList;

public class DocumentFragment extends Fragment implements AddMenuListener, FileListener, PopupListener, ImportListener {

    FragmentDocumentBinding binding;
    private static final String TAG = "DocumentFragment";

    private FileManager fileManager;
    private ImportManager importManager;

    // Views
    private ConstraintLayout main;
    private RecyclerView documentGrid;
    private Button addDocument;
    private ImageButton addDocumentSmall;
    private TextView documentTitle;
    private ConstraintLayout noDocumentContainer;

    // Constants
    public static final String KEY_NAME = "name";
    public static final String KEY_FILE_NAME = "filename";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // binding
        binding = FragmentDocumentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Set names for Views
        main = binding.main;
        documentGrid = binding.documentGrid;
        addDocument = binding.addDocument;
        addDocumentSmall = binding.addDocumentSmall;
        documentTitle = binding.documentTitle;
        noDocumentContainer = binding.noDocumentContainer;

        // Set up document grid
        fileManager = new FileManager(getContext(), "Documents", ".ewdoc"); // Egyptian Writer DOCument

        // Set up import
        importManager = new ImportManager(getContext(), fileManager);
        importManager.setActivityResultLauncher(registerForActivityResult(new ActivityResultContracts.OpenDocument(), importManager));

        // Set up grid
        FileGridAdapter adapter = new FileGridAdapter(getContext(), getDocuments(fileManager), R.layout.document_card);
        adapter.removeFileListeners();
        adapter.addFileListener(this);
        GridLayoutManager gridManager = new GridLayoutManager(getContext(), 2);

        // adapt layout
        new GridAdapter(getContext(), documentGrid, 200, gridManager);
        new ButtonAdapter(getContext(), addDocument, addDocumentSmall, documentTitle, main, 34);    // Padding left+right+middle = 16dp + 16dp + 2dp

        documentGrid.setLayoutManager(gridManager);
        documentGrid.setAdapter(adapter);

        addDocument.setOnClickListener(view -> {
            addDocument();
        });
        addDocumentSmall.setOnClickListener(view -> {
            addDocument();
        });

        // Handle data
        Bundle bundle = this.getArguments();
        Intent intent;
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent = bundle.getParcelable(KEY_INTENT, Intent.class);
            } else {
                intent = bundle.getParcelable(KEY_INTENT);
            }
            if (intent != null) {
                importManager.handleImportIntents(intent);
                importManager.addOnImportListener(this);
            }
        }

        return rootView;

    }

    private void addDocument(){
        AddMenu addMenu = new AddMenu(getContext(), getString(R.string.menu_item_import_document), getString(R.string.menu_item_create_document));
        int[] location = new int[2];
        addDocument.getLocationOnScreen(location);
        addMenu.setPosition(location[0], location[1] + 40);
        addMenu.addAddMenuListener(this);
        addMenu.show();
    }

    private ArrayList<FileGridData> getDocuments(FileManager fileManager){
        ArrayList<FileGridData> fileGridData = fileManager.getFiles();

        if (fileGridData.isEmpty()){
            noDocumentContainer.setVisibility(View.VISIBLE);
            documentGrid.setVisibility(View.GONE);
        } else {
            noDocumentContainer.setVisibility(View.GONE);
            documentGrid.setVisibility(View.VISIBLE);
        }

        return fileGridData;
    }

    // Listeners

    // AddMenu Listener
    @Override
    public void OnAboard() {

    }

    @Override
    public void OnImportFile() {
        importManager.addOnImportListener(this);
        importManager.showDialog();
    }

    @Override
    public void OnCreateFile() {
        getActivity().runOnUiThread(() -> {

            StandardPopup standardPopup = new StandardPopup(getContext(), StandardPopup.MODE_ENTER_FILENAME,
                    getResources().getString(R.string.new_document_title), null,
                    getResources().getString(R.string.button_cancel),
                    getResources().getString(R.string.button_create));

            standardPopup.addFileNames(fileManager.getNames());
            standardPopup.setFilename(standardPopup.suggestFileName());
            standardPopup.show();
            standardPopup.addPopupListener(DocumentFragment.this);
        });
    }

    // File Listener
    @Override
    public void OnDeleteFile(String name) {

        StandardPopup standardPopup = new StandardPopup(getContext(), StandardPopup.MODE_WARNING,
                getResources().getString(R.string.delete_document),
                String.format(getResources().getString(R.string.delete_message_document), name),
                getResources().getString(R.string.button_cancel),
                getResources().getString(R.string.button_delete));
        standardPopup.addPopupListener(this);
        standardPopup.setFilename(name);
        standardPopup.show();

    }

    @Override
    public void OnExportFile(String name) {
        Intent myIntent = new Intent(getActivity(), FileResultActivity.class);
        // Add extras
        myIntent.putExtra(KEY_FILE_NAME, name + ".ewdoc");
        DocumentFragment.this.startActivity(myIntent);
    }

    @Override
    public void OnOpenFile(String name) {
        Intent myIntent = new Intent(getActivity(), DocumentEditorActivity.class);
        // Add extras
        myIntent.putExtra(KEY_NAME, name);
        myIntent.putExtra(KEY_FILE_NAME, name + ".ewdoc");
        DocumentFragment.this.startActivity(myIntent);
    }


    // Import Listener
    @Override
    public void onImport(String name) {
        FileGridAdapter adapter = new FileGridAdapter(getContext(), getDocuments(fileManager), R.layout.document_card);
        adapter.removeFileListeners();
        adapter.addFileListener(this);
        documentGrid.setAdapter(adapter);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onCancel() {

    }


    // StandardPopup listener
    @Override
    public void OnCancel() {

    }

    @Override
    public void OnSelected(String name) {
        fileManager.addFile(name);
        FileGridAdapter adapter = new FileGridAdapter(getContext(), getDocuments(fileManager), R.layout.document_card);
        adapter.removeFileListeners();
        adapter.addFileListener(this);
        documentGrid.setAdapter(adapter);
    }

    @Override
    public void OnConfirmed(String name) {
        fileManager.deleteFile(name);
        FileGridAdapter adapter = new FileGridAdapter(getContext(), getDocuments(fileManager), R.layout.document_card);
        adapter.removeFileListeners();
        adapter.addFileListener(this);
        documentGrid.setAdapter(adapter);
    }
}