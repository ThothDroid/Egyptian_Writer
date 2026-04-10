package com.blueapps.egyptianwriter.editor.vocab.cards;

import static com.blueapps.egyptianwriter.editor.vocab.FileMaster.XML_TAG_SIGN;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.blueapps.signprovider.SignProvider;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SignCard extends Card implements Parcelable {

    private final String signId;

    public SignCard(Element element, int index) {
        super(element, index);

        Node signNode = element.getElementsByTagName(XML_TAG_SIGN).item(0);
        signId = getChildString(signNode);
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

    // Parcel
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(index);
        parcel.writeString(signId);
    }

    protected SignCard(Parcel in) {
        index = in.readInt();
        signId = in.readString();
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
