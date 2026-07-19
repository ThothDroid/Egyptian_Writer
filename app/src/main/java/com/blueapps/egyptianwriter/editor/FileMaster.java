package com.blueapps.egyptianwriter.editor;

import android.content.Context;
import android.util.Log;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.issuecenter.Issue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FileMaster {

    protected final File file;
    protected final File path;
    protected final Context context;

    // Content
    protected String content;

    // For utility reasons I want to keep both constructors
    @SuppressWarnings("unused")
    public FileMaster(Context context, File file, String dir){
        this.context = context;
        this.path = new File(context.getFilesDir() + dir);
        this.file = file;
    }

    public FileMaster(Context context, String filename, String dir){
        this.context = context;
        this.path = new File(context.getFilesDir() + dir);
        this.file = new File(path, filename);
    }

    public void extractData() throws IOException {
            //throw new FileNotFoundException();
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            reader.close();
            // Content
            content = stringBuilder.toString();
    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

}
