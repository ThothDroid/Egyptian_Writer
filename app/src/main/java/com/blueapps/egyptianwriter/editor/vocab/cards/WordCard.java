package com.blueapps.egyptianwriter.editor.vocab.cards;

import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Element;

public class WordCard extends Card implements Parcelable {

    public WordCard(int index) {
        super(index);
    }

    public WordCard(Element element, int index) {
        super(element, index);
    }

    protected WordCard(Parcel in) {
        super(in);
    }

}
