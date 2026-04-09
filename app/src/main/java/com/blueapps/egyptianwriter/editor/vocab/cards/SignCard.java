package com.blueapps.egyptianwriter.editor.vocab.cards;

import static com.blueapps.egyptianwriter.editor.vocab.FileMaster.XML_TAG_SIGN;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.blueapps.signprovider.SignProvider;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SignCard extends Card {

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
}
