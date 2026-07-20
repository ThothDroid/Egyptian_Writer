package com.blueapps.egyptianwriter.editor.vocab.cards;

import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_TYPE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_VAL_TYPE_SIGN;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_DESCRIPTION;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_NAME_CARD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_SIGN;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_TRANSCRIPTION;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.blueapps.signprovider.SignProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SignCard extends Card implements Parcelable {

    private String signId;
    private String transcription;
    private String description;

    public SignCard(Element element, int index) {
        super(element, index);

        Node signNode = element.getElementsByTagName(XML_TAG_SIGN).item(0);
        signId = getChildString(signNode);

        Node tNode = element.getElementsByTagName(XML_TAG_TRANSCRIPTION).item(0);
        transcription = getChildString(tNode);

        Node dNode = element.getElementsByTagName(XML_TAG_DESCRIPTION).item(0);
        description = getChildString(dNode);
    }

    // I want to keep the getSignId function for later use
    @SuppressWarnings("unused")
    public String getSignId() {
        return signId;
    }

    public Drawable getSign(Context context) throws XmlPullParserException, IOException {
        SignProvider signProvider = new SignProvider(context);

        return signProvider.getSign(signId);
    }

    public String getTranscription() {
        return transcription;
    }

    public String getDescription() {
        return description;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Element getElement(Document document) {
        Element element = super.getElement(document);

        if (element == null) {
            element = document.createElement(XML_TAG_NAME_CARD);
            element.setAttribute(XML_ATTR_TYPE, XML_ATTR_VAL_TYPE_SIGN);

            Node signNode = document.createElement(XML_TAG_SIGN);
            signNode.setTextContent(signId);
            element.appendChild(signNode);

            Node transcriptionNode = document.createElement(XML_TAG_TRANSCRIPTION);
            transcriptionNode.setTextContent(transcription);
            element.appendChild(transcriptionNode);

            Node descriptionNode = document.createElement(XML_TAG_DESCRIPTION);
            descriptionNode.setTextContent(description);
            element.appendChild(descriptionNode);

        }
        return element;
    }

    // Parcel
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(index);
        parcel.writeString(signId);
        parcel.writeString(transcription);
        parcel.writeString(description);
    }

    protected SignCard(Parcel in) {
        index = in.readInt();
        signId = in.readString();
        transcription = in.readString();
        description = in.readString();
    }

    public static final Creator<SignCard> CREATOR = new Creator<>() {
        @Override
        public SignCard createFromParcel(Parcel in) {
            return new SignCard(in);
        }

        @Override
        public SignCard[] newArray(int size) {
            return new SignCard[size];
        }
    };
}
