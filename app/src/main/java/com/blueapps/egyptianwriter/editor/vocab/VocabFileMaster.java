package com.blueapps.egyptianwriter.editor.vocab;

import android.content.Context;
import android.util.Log;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.editor.FileChanger;
import com.blueapps.egyptianwriter.editor.FileMaster;
import com.blueapps.egyptianwriter.editor.vocab.cards.Card;
import com.blueapps.egyptianwriter.editor.vocab.cards.SignCard;
import com.blueapps.egyptianwriter.issuecenter.Issue;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class VocabFileMaster extends FileMaster {

    private static final String TAG = "VocabFileMaster";

    // Content
    private final ArrayList<Card> cards = new ArrayList<>();

    // Constants
    // XML
    public static final String XML_ROOT_TAG_DOCUMENT = "ancientFlashCards";
    public static final String XML_TAG_NAME_CARD = "card";
    public static final String XML_ATTR_TYPE = "type";
    public static final String XML_ATTR_VAL_TYPE_SIGN = "sign";
    public static final String XML_ATTR_VAL_TYPE_STANDARD = "standard";
    public static final String XML_TAG_SIGN = "sign";
    public static final String XML_TAG_TRANSCRIPTION = "transcription";
    public static final String XML_TAG_DESCRIPTION = "description";
    public static final String XML_TAG_SETTINGS = "settings";
    public static final String XML_ATTR_LEARN_DESCRIPTION = "learnDescription";

    // Because for utility I want to keep both constructors
    @SuppressWarnings("unused")
    public VocabFileMaster(Context context, File file){
        super(context, file, "/Vocabulary");
    }

    public VocabFileMaster(Context context, String filename){
        super(context, filename, "/Vocabulary");
    }

    public VocabFileMaster(Context context, String filename, ArrayList<Card> cards){
        this(context, filename);
        this.cards.addAll(cards);
    }

    @Override
    public void extractData(){

        try {

            super.extractData();

            if(content.isEmpty()){
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                //root elements
                rootDocument = docBuilder.newDocument();

                Element rootElement = rootDocument.createElement(XML_ROOT_TAG_DOCUMENT);
                rootDocument.appendChild(rootElement);
            } else {
                rootDocument = loadXMLFromString(content);

                if (rootDocument.hasChildNodes()){
                    Element rootElement = rootDocument.getDocumentElement();
                    if (Objects.equals(rootElement.getTagName(), XML_ROOT_TAG_DOCUMENT)) {
                        NodeList cardNodes = rootDocument.getElementsByTagName(XML_TAG_NAME_CARD);
                        for (int i = 0; i < cardNodes.getLength(); i++){
                            Node cardNode = cardNodes.item(i);

                            if (cardNode instanceof Element){

                                Element cardElement = (Element) cardNode;
                                String type = cardElement.getAttribute(XML_ATTR_TYPE);
                                if (Objects.equals(type, XML_ATTR_VAL_TYPE_SIGN)){
                                    cards.add(new SignCard(cardElement, i));
                                } else {
                                    cards.add(new Card(cardElement, i));
                                }
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

    public ArrayList<Card> getCards(){
        return cards;
    }

    public void addCard(Card card){
        cards.add(card);

        // Apply changes to file
        applyContentToDocument();
        new Thread(new FileChanger(file, rootDocument)).start();
    }

    public void setCard(Card card, int index){
        if (index >= 0 && index < cards.size()) {
            cards.set(index, card);

            // Apply changes to file
            applyContentToDocument();
            new Thread(new FileChanger(file, rootDocument)).start();
        }
    }

    private void applyContentToDocument(){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root elements
            rootDocument = docBuilder.newDocument();

            Element rootElement = rootDocument.createElement(XML_ROOT_TAG_DOCUMENT);

            // children
            for (Card card : cards){
                Element cardElement = card.getElement(rootDocument);
                rootElement.appendChild(cardElement);
            }

            rootDocument.appendChild(rootElement);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}
