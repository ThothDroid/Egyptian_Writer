package com.blueapps.egyptianwriter.editor.vocab.cards;

import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_SCORE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_TYPE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_VAL_TYPE_STANDARD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_NAME_CARD;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// Because some functions are only for utility
@SuppressWarnings("unused")
public class Card implements Parcelable {

    private static final String TAG = "Card";

    protected Element element;
    protected int index;
    protected int score = 0;

    public Card(int index){
        element = null;
        this.index = index;
    }

    public Card(Element element, int index){
        this.element = element;
        this.index = index;

        // get score
        String stringScore = element.getAttribute(XML_ATTR_SCORE);
        try {
            int intScore = Integer.parseInt(stringScore);
            if (intScore >= 0 && intScore <= 100){
                this.score = intScore;
            }
        } catch (NumberFormatException e){
            Log.e(TAG, "Parsing Error: Score is not a number! Score: \"" + stringScore + "\"");
        }
    }

    protected Card(Parcel in) {
        index = in.readInt();
        score = in.readInt();
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
        element.setAttribute(XML_ATTR_SCORE, String.valueOf(score));

        return element;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(index);
        parcel.writeInt(score);
    }
}
