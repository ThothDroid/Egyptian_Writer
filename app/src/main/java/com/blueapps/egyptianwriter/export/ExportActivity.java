package com.blueapps.egyptianwriter.export;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.blueapps.egyptianwriter.editor.document.FileMaster.ROOT_TAG_DOCUMENT;
import static com.blueapps.egyptianwriter.editor.document.FileMaster.ROOT_TAG_GLYPHX;
import static com.blueapps.egyptianwriter.editor.document.FileMaster.TAG_NAME_GLYPHX;
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
import com.blueapps.seshat.Seshat;
import com.blueapps.seshat.SeshatListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

    public static Element getGlyphXElement(File file) throws ParserConfigurationException {
        // DokumentBuilderFactory initialisieren
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // DokumentBuilder erstellen
        DocumentBuilder builder = factory.newDocumentBuilder();

        try (FileInputStream is = new FileInputStream(file)){

            Document document = builder.parse(is);
            if (document.hasChildNodes()){
                Element rootElement = document.getDocumentElement();
                if (Objects.equals(rootElement.getTagName(), ROOT_TAG_GLYPHX)){
                    return rootElement;
                } else if (Objects.equals(rootElement.getTagName(), ROOT_TAG_DOCUMENT)) {
                    NodeList glyphxNodes = document.getElementsByTagName(TAG_NAME_GLYPHX);
                    if (glyphxNodes.getLength() > 0) {
                        Node glyphxNode = glyphxNodes.item(0);
                        if (glyphxNode instanceof Element){
                            return (Element) glyphxNode;
                        }
                    }
                }
            }

        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static final String getGlyphX(File file){
        try {
            Element element = getGlyphXElement(file);

            if (element == null) return "<anchientText></anchientText>";

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            StringWriter writer = new StringWriter();

            transformer.transform(new DOMSource(element), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return "<anchientText></anchientText>";
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
                outputMimeTypes = new String[]{MIME_SVG};
                outputFileName = name + ".svg";
                outputFile = new File(exportFolder, outputFileName);
                Seshat seshat = new Seshat(this, getGlyphX(inputFile), new Handler(getMainLooper()));
                seshat.addSeshatListener(this);
                String SVG = seshat.convertToSVGString("", "", false, true);
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