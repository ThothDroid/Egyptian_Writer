package com.blueapps.egyptianwriter.editor.vocab.cards;

import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_LEARN_EXPIRE_DATE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_SCORE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_TYPE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_VAL_TYPE_WORD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_NAME_CARD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_WORD;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WordCard extends Card implements Parcelable {

    private String word = "";

    public WordCard(int index) {
        super(index);
    }

    public WordCard(Element element, int index) {
        super(element, index);

        Node wordNode = element.getElementsByTagName(XML_TAG_WORD).item(0);
        word = getChildString(wordNode);
        if (word == null) {
            word = "";
        }

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public Element getElement(Document document) {
        element = document.createElement(XML_TAG_NAME_CARD);
        element.setAttribute(XML_ATTR_TYPE, XML_ATTR_VAL_TYPE_WORD);
        element.setAttribute(XML_ATTR_SCORE, String.valueOf(score));
        element.setAttribute(XML_ATTR_LEARN_EXPIRE_DATE, String.valueOf(learnExpireDate));

        Node wordNode = document.createElement(XML_TAG_WORD);
        wordNode.setTextContent(word);
        element.appendChild(wordNode);

        return element;
    }

    // Parcel

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(word);
    }

    protected WordCard(Parcel in) {
        super(in);
        word = in.readString();
    }

    public static final Creator<WordCard> CREATOR = new Creator<>() {
        @Override
        public WordCard createFromParcel(Parcel in) {
            return new WordCard(in);
        }

        @Override
        public WordCard[] newArray(int size) {
            return new WordCard[size];
        }
    };

}
