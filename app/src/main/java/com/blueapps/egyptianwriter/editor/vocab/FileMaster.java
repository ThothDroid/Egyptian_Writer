package com.blueapps.egyptianwriter.editor.vocab;

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
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FileMaster {

    private static final String TAG = "FileMaster";

    private final File file;
    private final File path;
    private final Context context;

    private final ArrayList<Card> cards = new ArrayList<>();

    // Constants
    // XML
    public static final String XML_ROOT_TAG_DOCUMENT = "ancientFlashCards";
    public static final String XML_TAG_NAME_CARD = "card";
    public static final String XML_ATTR_TYPE = "type";
    public static final String XML_ATTR_VAL_TYPE_SIGN = "sign";
    public static final String XML_TAG_SIGN = "sign";

    public FileMaster(Context context, File file){
        this.context = context;
        this.path = new File(context.getFilesDir() + "/Vocabulary");
        this.file = file;
    }

    public FileMaster(Context context, String filename){
        this.context = context;
        this.path = new File(context.getFilesDir() + "/Vocabulary");
        this.file = new File(path, filename);
    }

    public void extractData(){

        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            reader.close();
            content = stringBuilder.toString();

            if(content.isEmpty()){
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                //root elements
                rootDocument = docBuilder.newDocument();

                Element rootElement = rootDocument.createElement(ROOT_TAG_DOCUMENT);
                rootDocument.appendChild(rootElement);
            } else {
                rootDocument = loadXMLFromString(content);

                if (rootDocument.hasChildNodes()){
                    Element rootElement = rootDocument.getDocumentElement();
                    if (Objects.equals(rootElement.getTagName(), ROOT_TAG_DOCUMENT)) {
                        NodeList cardNodes = rootDocument.getElementsByTagName(TAG_NAME_CARD);
                        for (int i = 0; i < cardNodes.getLength(); i++){
                            Node cardNode = cardNodes.item(i);
                            if (cardNode instanceof Element){
                                cards.add(new Card((Element) cardNode, i));
                            } else {
                                Log.i(TAG, "Node is not an Element.");
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e){
            // TODO: display popup window despite activity is not running
            new Issue(context, context.getString(R.string.error_unexpected_title),
                    context.getString(R.string.error_unexpected_text),
                    "Trying to extract data: FileNotFoundException on java.io.FileInputStream: " + e.getLocalizedMessage()).show();
        } catch (Exception e) {
            // TODO: Error Handling
            throw new RuntimeException(e);
        }
    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

}
