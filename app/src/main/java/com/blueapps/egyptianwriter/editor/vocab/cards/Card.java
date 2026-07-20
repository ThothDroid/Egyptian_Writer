package com.blueapps.egyptianwriter.editor.vocab.cards;

import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_LEARN_DESCRIPTION;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_TYPE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_VAL_TYPE_SIGN;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_VAL_TYPE_STANDARD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_DESCRIPTION;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_NAME_CARD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_SETTINGS;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_SIGN;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_TRANSCRIPTION;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// Because some functions are only for utility
@SuppressWarnings("unused")
public class Card implements Parcelable {

    protected Element element;
    protected int index;

    public Card(){
        element = null;
    }

    public Card(Element element, int index){
        this.element = element;
        this.index = index;
    }

    protected Card(Parcel in) {
        index = in.readInt();
        element = null;
    }

    public static final Creator<Card> CREATOR = new Creator<>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    protected String getChildString(Node rootNode){
        NodeList signNodes = rootNode.getChildNodes();
        for (int i = 0; i < signNodes.getLength(); i++){
            Node node = signNodes.item(i);
            if (node instanceof Text){
                Text text = (Text) node;
                return text.getWholeText();
            }
        }
        return null;
    }

    public Element getElement(Document document) {

        element = document.createElement(XML_TAG_NAME_CARD);
        element.setAttribute(XML_ATTR_TYPE, XML_ATTR_VAL_TYPE_STANDARD);

        return element;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(index);
    }
}
