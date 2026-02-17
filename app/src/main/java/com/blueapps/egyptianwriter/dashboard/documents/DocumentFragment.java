package com.blueapps.egyptianwriter.dashboard.documents;

import static androidx.core.util.TypedValueCompat.dpToPx;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.documents.createdocument.AddMenu;
import com.blueapps.egyptianwriter.dashboard.documents.createdocument.AddMenuListener;
import com.blueapps.egyptianwriter.dashboard.documents.documentgrid.DocumentGridAdapter;
import com.blueapps.egyptianwriter.dashboard.documents.documentgrid.DocumentGridData;
import com.blueapps.egyptianwriter.dashboard.documents.documentgrid.DocumentListener;
import com.blueapps.egyptianwriter.dashboard.documents.documentgrid.DocumentManager;
import com.blueapps.egyptianwriter.databinding.FragmentDocumentBinding;
import com.blueapps.egyptianwriter.editor.DocumentEditorActivity;
import com.blueapps.egyptianwriter.export.FileResultActivity;
import com.blueapps.egyptianwriter.fileimport.ImportListener;
import com.blueapps.egyptianwriter.fileimport.ImportManager;
import com.blueapps.egyptianwriter.issuecenter.PopupListener;
import com.blueapps.egyptianwriter.issuecenter.StandardPopup;
import com.blueapps.egyptianwriter.layoutadapter.ButtonAdapter;
import com.blueapps.egyptianwriter.layoutadapter.GridAdapter;

import java.util.ArrayList;

public class DocumentFragment extends Fragment implements AddMenuListener, DocumentListener, PopupListener, ImportListener {

    FragmentDocumentBinding binding;
    private static final String TAG = "DocumentFragment";
    private boolean hasStarted = false;

    private DocumentManager documentManager;
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
    public View onCreateView(LayoutInflater inflater,
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
        documentManager = new DocumentManager(getContext());

        // Set up import
        importManager = new ImportManager(getContext(), documentManager);
        importManager.setActivityResultLauncher(registerForActivityResult(new ActivityResultContracts.OpenDocument(), importManager));

        // Set up grid
        DocumentGridAdapter adapter = new DocumentGridAdapter(getContext(), getDocuments(documentManager));
        adapter.removeDocumentListeners();
        adapter.addDocumentListener(this);
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
        AddMenu addMenu = new AddMenu(getContext());
        int[] location = new int[2];
        addDocument.getLocationOnScreen(location);
        addMenu.setPosition(location[0], location[1] + 40);
        addMenu.addAddMenuListener(this);
        addMenu.show();
    }

    private ArrayList<DocumentGridData> getDocuments(DocumentManager documentManager){
        ArrayList<DocumentGridData> documentGridData = documentManager.getDocuments();

        if (documentGridData.isEmpty()){
            noDocumentContainer.setVisibility(View.VISIBLE);
            documentGrid.setVisibility(View.GONE);
        } else {
            noDocumentContainer.setVisibility(View.GONE);
            documentGrid.setVisibility(View.VISIBLE);
        }

        return documentGridData;
    }

    // Listeners

    // AddMenu Listener
    @Override
    public void OnAboard() {

    }

    @Override
    public void OnImportDocument() {
        importManager.addOnImportListener(this);
        importManager.showDialog();
    }

    @Override
    public void OnCreateDocument() {
        getActivity().runOnUiThread(() -> {

            StandardPopup standardPopup = new StandardPopup(getContext(), StandardPopup.MODE_ENTER_FILENAME,
                    getResources().getString(R.string.new_document_title), null,
                    getResources().getString(R.string.button_cancel),
                    getResources().getString(R.string.button_create));

            standardPopup.addFileNames(documentManager.getNames());
            standardPopup.setFilename(standardPopup.suggestFileName());
            standardPopup.show();
            standardPopup.addPopupListener(DocumentFragment.this);
        });
    }

    // Document Listener
    @Override
    public void OnDeleteDocument(String name) {

        StandardPopup standardPopup = new StandardPopup(getContext(), StandardPopup.MODE_WARNING,
                getResources().getString(R.string.delete_document),
                String.format(getResources().getString(R.string.delete_message_program), name),
                getResources().getString(R.string.button_cancel),
                getResources().getString(R.string.button_delete));
        standardPopup.addPopupListener(this);
        standardPopup.setFilename(name);
        standardPopup.show();

    }

    @Override
    public void OnExportDocument(String name) {
        Intent myIntent = new Intent(getActivity(), FileResultActivity.class);
        // Add extras
        myIntent.putExtra(KEY_FILE_NAME, name + ".ewdoc");
        DocumentFragment.this.startActivity(myIntent);
    }

    @Override
    public void OnOpenDocument(String name) {
        Intent myIntent = new Intent(getActivity(), DocumentEditorActivity.class);
        // Add extras
        myIntent.putExtra(KEY_NAME, name);
        myIntent.putExtra(KEY_FILE_NAME, name + ".ewdoc");
        DocumentFragment.this.startActivity(myIntent);
    }


    // Import Listener
    @Override
    public void onImport(String name) {
        DocumentGridAdapter adapter = new DocumentGridAdapter(getContext(), getDocuments(documentManager));
        adapter.removeDocumentListeners();
        adapter.addDocumentListener(this);
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
        documentManager.addDocument(name);
        DocumentGridAdapter adapter = new DocumentGridAdapter(getContext(), getDocuments(documentManager));
        adapter.removeDocumentListeners();
        adapter.addDocumentListener(this);
        documentGrid.setAdapter(adapter);
    }

    @Override
    public void OnConfirmed(String name) {
        documentManager.deleteDocument(name);
        DocumentGridAdapter adapter = new DocumentGridAdapter(getContext(), getDocuments(documentManager));
        adapter.removeDocumentListeners();
        adapter.addDocumentListener(this);
        documentGrid.setAdapter(adapter);
    }
}