package com.blueapps.egyptianwriter.editor.document;

import static com.blueapps.egyptianwriter.editor.document.EditorViewModel.MODE_READ;
import static com.blueapps.egyptianwriter.editor.document.EditorViewModel.MODE_WRITE;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.blueapps.egyptianwriter.CheckableImageButton;
import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.documents.DocumentFragment;
import com.blueapps.egyptianwriter.databinding.ActivityDocumentEditorBinding;
import com.blueapps.egyptianwriter.editor.document.edit.EditFragment;
import com.blueapps.egyptianwriter.editor.document.settings.PropertiesFragment;
import com.blueapps.egyptianwriter.editor.document.settings.PropertiesManager;
//import com.blueapps.thoth.ThothListener;
import com.blueapps.thoth.ThothListener;
import com.blueapps.thoth.ThothView;
import com.otaliastudios.zoom.ZoomLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Document;

public class DocumentEditorActivity extends AppCompatActivity implements ImageButtonListener, ThothListener {

    private ActivityDocumentEditorBinding binding;
    private static final String TAG = "DocumentEditorActivity";

    private EditorViewModel viewModel;
    private PropertiesManager propertiesManager;

    private DisplayMetrics displayMetrics;

    private FragmentManager fragmentManager;

    private String filename = "";

    // Views
    private View root;
    private TextView documentTitle;
    private ImageButton buttonBack;
    private ImageButton buttonMode;
    private ThothView thothView;
    private ExpandableLayout expandableLayout;
    private ConstraintLayout background;
    private FragmentContainerView containerView;
    private CheckableImageButton buttonWrite;
    private CheckableImageButton buttonSettings;
    private ZoomLayout zoomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create non fullscreen layout
        binding = ActivityDocumentEditorBinding.inflate(getLayoutInflater());
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
        String name = intent.getStringExtra(DocumentFragment.KEY_NAME);
        filename = intent.getStringExtra(DocumentFragment.KEY_FILE_NAME);

        // get ViewModel
        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        propertiesManager = new ViewModelProvider(this).get(PropertiesManager.class);

        // Set names for Views
        root = binding.getRoot();
        documentTitle = binding.documentTitle;
        buttonBack = binding.buttonBack;
        buttonMode = binding.buttonMode;
        thothView = binding.glyphXView;
        expandableLayout = binding.editorExpandLayout;
        background = binding.editorContainer;
        containerView = binding.editFragmentContainer;
        buttonWrite = binding.buttonWrite;
        buttonSettings = binding.buttonSettings;
        zoomLayout = binding.zoom;

        displayMetrics = getResources().getDisplayMetrics();

        documentTitle.setText(name);
        buttonBack.setOnClickListener(view -> {
            finish();
        });
        buttonMode.setOnClickListener(view -> {
            if (viewModel.getMode()){
                viewModel.setMode(MODE_WRITE);
                buttonMode.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.edit_note));
                expandableLayout.expand(true);
            } else {
                viewModel.setMode(MODE_READ);
                buttonMode.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.opened_book));
                expandableLayout.collapse(true);
            }
        });

        if (viewModel.getMode()) {
            expandableLayout.collapse(false);
        } else {
            expandableLayout.expand(false);
        }

        viewModel.setFileMaster(new FileMaster(this, filename));
        viewModel.getFileMaster().addFileListener(new FileListener() {
            @Override
            public void onGlyphXChanged(Document GlyphX) {
                try {
                    thothView.setGlyphXText(GlyphX);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onMdCChanged(String mdc) {

            }
        });

        try {
            thothView.setGlyphXText(viewModel.getFileMaster().getGlyphX());
        } catch (Exception e){
            e.printStackTrace();
            // TODO: Error handling
        }
        thothView.setAltText(viewModel.getFileMaster().getMdc());

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerView.getId(), new EditFragment());
        transaction.commit();

        background.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            // Set up height of EditText
            int height = background.getHeight();

            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) expandableLayout.getLayoutParams();
            lp.height = (int) (height * 0.5);
            expandableLayout.setLayoutParams(lp);
        });

        ImageButtonGroup imageButtonGroup = new ImageButtonGroup();
        imageButtonGroup.addImageButton(buttonWrite);
        imageButtonGroup.addImageButton(buttonSettings);
        imageButtonGroup.addImageButtonListener(this);

        // Update ThothView
        propertiesManager.getTextSize().observe(this, integer -> thothView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, integer, displayMetrics)));
        propertiesManager.getWritingLayout().observe(this, integer -> thothView.setWritingLayout(integer));
        propertiesManager.getVerticalOrientation().observe(this, integer -> thothView.setVerticalOrientation(integer));
        propertiesManager.getWritingDirection().observe(this, integer -> thothView.setWritingDirection(integer));

        // init ThothView
        thothView.setSignPadding(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, displayMetrics));
        thothView.setLayoutSignPadding(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, displayMetrics));
        thothView.setInterLinePadding(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, displayMetrics));
        thothView.setThothListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //thothView.cancelRender();
        binding = null;
    }

    @Override
    public void OnPositionChanges(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (position){
            case 0:
                transaction.replace(containerView.getId(), new EditFragment());
                break;
            case 1:
                transaction.replace(containerView.getId(), new PropertiesFragment());
        }

        transaction.commit();
    }

    // ThothListener
    @Override
    public void OnRenderStart() {
        Log.d(TAG, "Render started!");
    }

    @Override
    public void OnRender(float v, int i, int i1) {
        Log.d(TAG, "Rendering: " + v + "% [" + i + "/" + i1 + "]");
    }

    @Override
    public void OnRenderCancel() {
        Log.d(TAG, "Render canceled!");
    }

    @Override
    public void OnRenderFinished() {
        Log.d(TAG, "Render finished!");
    }
}