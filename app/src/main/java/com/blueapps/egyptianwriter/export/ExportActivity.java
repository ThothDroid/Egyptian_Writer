package com.blueapps.egyptianwriter.export;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.blueapps.egyptianwriter.export.ExportSettingsFragment.FILE_TYPE_EWDOC;
import static com.blueapps.egyptianwriter.export.ExportSettingsFragment.FILE_TYPE_SVG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.documents.DocumentFragment;
import com.blueapps.egyptianwriter.databinding.ActivityFileResultBinding;
import com.blueapps.egyptianwriter.editor.document.FileMaster;
import com.blueapps.egyptianwriter.editor.document.properties.PropertiesManager;
import com.blueapps.seshat.Seshat;
import com.blueapps.seshat.SeshatListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExportActivity extends AppCompatActivity implements ActivityResultCallback<Uri>, ExportListener, SeshatListener {

    private ActivityFileResultBinding binding;

    private ActivityResultLauncher<String> saveResultLauncher;

    private String name = "output";
    private File inputFile;
    private File exportFolder;
    private File outputFile;
    private String outputFileName = "output";
    private String[] outputMimeTypes = new String[]{MIME_DEFAULT};
    private FragmentManager fragmentManager;
    private ExportSettingsFragment exportSettingsFragment;
    private FileResultFragment fileResultFragment;

    // Views
    private ImageButton buttonBack;
    private FragmentContainerView fragmentContainerView;

    // Constants,
    public static final String MIME_DEFAULT = "application/octet-stream";
    public static final String MIME_EWDOC = "application/vnd.com.blueapps.egyptianwriter.ewdoc";
    public static final String MIME_SVG = "image/svg+xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileResultBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get Extras
        Intent intent = getIntent();
        String filename = intent.getStringExtra(DocumentFragment.KEY_FILE_NAME);
        name = intent.getStringExtra(DocumentFragment.KEY_NAME);

        // Set names for Views
        buttonBack = binding.buttonBack;
        fragmentContainerView = binding.fragmentContainerView;

        inputFile = new File(getFilesDir() + "/Documents/" + filename);

        buttonBack.setOnClickListener(view -> {
            finish();
        });

        exportSettingsFragment = ExportSettingsFragment.newInstance();
        exportSettingsFragment.setListener(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragmentContainerView.getId(), exportSettingsFragment);
        transaction.commit();

        saveResultLauncher = registerForActivityResult(
                new ActivityResultContracts.CreateDocument(MIME_EWDOC), this);

    }

    private void createFile(String filename) {
        if (outputFile.exists()) {
            saveResultLauncher.launch(filename);
        } else {
            Toast.makeText(this, getString(R.string.error_file_ran_out), Toast.LENGTH_SHORT).show();
        }
    }

    private void copyFile(File from, Uri to){
        try (InputStream is = new FileInputStream(from); OutputStream os = getContentResolver().openOutputStream(to)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: ERROR HANDLING
        }
    }

    private void shareFile(File file, String[] mimetypes){
        if (file.exists()) {
            Uri uri = getUriForFile(this, "com.blueapps.fileprovider", file);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            if (mimetypes.length > 0) shareIntent.setType(mimetypes[0]);
            shareIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
        } else {
            Toast.makeText(this, getString(R.string.error_file_ran_out), Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            Log.e("FileDeletion", "Failed to delete file: " + file.getAbsolutePath());
                        }
                    } else if (file.isDirectory()) {
                        clearFolder(file); // Recursively delete subfolders
                        file.delete(); // Delete the empty subfolder
                    }
                }
            }
            Log.d("FileDeletion", "All files in exports folder deleted.");
        } else {
            Log.e("FileDeletion", "Folder does not exist.");
        }
    }

    public void writeFile(File file, String content){
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(content);
            myWriter.close();  // must close manually
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setSeshatSettings(Seshat seshat, PropertiesManager properties){
        seshat.setTextSize(properties.getTextSize().getValue());
        seshat.setVerticalOrientation(properties.getVerticalOrientation().getValue());
        seshat.setWritingDirection(properties.getWritingDirection().getValue());
        seshat.setWritingLayout(properties.getWritingLayout().getValue());

        seshat.setPagePaddingLeft(15);
        seshat.setPagePaddingTop(15);
        seshat.setPagePaddingRight(15);
        seshat.setPagePaddingBottom(15);

        seshat.setSignPadding(5);
        seshat.setLayoutSignPadding(3);
        seshat.setInterLinePadding(15);
    }


    @Override
    public void onActivityResult(Uri destination) {
        if (outputFile.exists()) {
            copyFile(outputFile, destination);
        } else {
            Toast.makeText(this, getString(R.string.error_file_ran_out), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExport(ExportProperty property) {
        new Thread(() -> {
            exportFolder = new File(getCacheDir(), "exports");
            if (!exportFolder.exists()){
                exportFolder.mkdirs();
            }
            if (property.getFileType() == FILE_TYPE_EWDOC){
                outputMimeTypes = new String[]{MIME_EWDOC};
                outputFileName = name + ".ewdoc";
                outputFile = new File(exportFolder, outputFileName);
                copyFile(inputFile, Uri.fromFile(outputFile));
            } else if (property.getFileType() == FILE_TYPE_SVG){
                // Configure input file
                outputMimeTypes = new String[]{MIME_SVG};
                outputFileName = name + ".svg";
                outputFile = new File(exportFolder, outputFileName);

                // Read Document
                FileMaster fileMaster = new FileMaster(this, binding.getRoot(), inputFile);
                fileMaster.extractData();
                PropertiesManager propertiesManager = fileMaster.getPropertiesManager();

                // Configure Seshat
                Seshat seshat = new Seshat(this, Seshat.convertToXmlString(fileMaster.getGlyphX()), new Handler(getMainLooper()));
                setSeshatSettings(seshat, propertiesManager);
                seshat.addSeshatListener(this);
                String SVG = seshat.convertToSVGString("", "", false, true);

                // Write file
                writeFile(outputFile, SVG);
            }

            fileResultFragment = FileResultFragment.newInstance(outputFileName);
            fileResultFragment.setListener(new FileResultListener() {
                @Override
                public void onShare() {
                    shareFile(outputFile, outputMimeTypes);
                }

                @Override
                public void onSave() {
                    createFile(outputFileName);
                }
            });

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
            transaction.replace(fragmentContainerView.getId(), fileResultFragment);
            transaction.commit();
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearFolder(exportFolder);
    }

    @Override
    public void onExportStarted() {

    }

    @Override
    public void onExportProgress(int i, int i1) {
        exportSettingsFragment.progress(i, i1);
    }

    @Override
    public void onPostProcessingStarted() {
        exportSettingsFragment.postPrecessing();
    }

    @Override
    public void onExportCompleted() {

    }
}