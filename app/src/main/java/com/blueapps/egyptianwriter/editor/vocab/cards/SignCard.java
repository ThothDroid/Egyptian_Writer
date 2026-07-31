package com.blueapps.egyptianwriter.editor.vocab.cards;

import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_LEARN_DESCRIPTION;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_LEARN_EXPIRE_DATE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_SCORE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_TYPE;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_VAL_TYPE_SIGN;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_ATTR_VAL_TYPE_STANDARD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_DESCRIPTION;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_NAME_CARD;
import static com.blueapps.egyptianwriter.editor.vocab.VocabFileMaster.XML_TAG_SETTINGS;
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
    private boolean learnDescription = false;

    public SignCard(int index) {
        super(index);
    }

    public SignCard(Element element, int index) {
        super(element, index);

        Node signNode = element.getElementsByTagName(XML_TAG_SIGN).item(0);
        signId = getChildString(signNode);

        Node tNode = element.getElementsByTagName(XML_TAG_TRANSCRIPTION).item(0);
        transcription = getChildString(tNode);

        Node dNode = element.getElementsByTagName(XML_TAG_DESCRIPTION).item(0);
        description = getChildString(dNode);

        Node settingsNode = element.getElementsByTagName(XML_TAG_SETTINGS).item(0);
        if (settingsNode != null) {
            String learnDescriptionAttr = ((Element) settingsNode).getAttribute(XML_ATTR_LEARN_DESCRIPTION);
            learnDescription = Boolean.parseBoolean(learnDescriptionAttr);
        }

        if (signId == null) {
            signId = "";
        }
        if (transcription == null) {
            transcription = "";
        }
        if (description == null) {
            description = "";
        }
    }

    // I want to keep the getSignId function for later use
    @SuppressWarnings("unused")
    public String getSignId() {
        return signId;
    }

    public boolean getLearnDescription() {
        return learnDescription;
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

    public void setLearnDescription(boolean learnDescription) {
        this.learnDescription = learnDescription;
    }

    @Override
    public Element getElement(Document document) {

        element = document.createElement(XML_TAG_NAME_CARD);
        element.setAttribute(XML_ATTR_TYPE, XML_ATTR_VAL_TYPE_SIGN);
        element.setAttribute(XML_ATTR_SCORE, String.valueOf(score));
        element.setAttribute(XML_ATTR_LEARN_EXPIRE_DATE, String.valueOf(learnExpireDate));

        Node signNode = document.createElement(XML_TAG_SIGN);
        signNode.setTextContent(signId);
        element.appendChild(signNode);

        Node transcriptionNode = document.createElement(XML_TAG_TRANSCRIPTION);
        transcriptionNode.setTextContent(transcription);
        element.appendChild(transcriptionNode);

        Node descriptionNode = document.createElement(XML_TAG_DESCRIPTION);
        descriptionNode.setTextContent(description);
        element.appendChild(descriptionNode);

        Element settingsElement = document.createElement(XML_TAG_SETTINGS);
        settingsElement.setAttribute(XML_ATTR_LEARN_DESCRIPTION, String.valueOf(learnDescription));
        element.appendChild(settingsElement);

        return element;
    }

    // Parcel
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(signId);
        parcel.writeString(transcription);
        parcel.writeString(description);
        parcel.writeByte((byte) (learnDescription ? 1 : 0)); // write boolean as byte
    }

    protected SignCard(Parcel in) {
        super(in);
        signId = in.readString();
        transcription = in.readString();
        description = in.readString();
        learnDescription = in.readByte() != 0; // read boolean as byte
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
