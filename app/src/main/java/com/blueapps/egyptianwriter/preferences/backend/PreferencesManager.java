package com.blueapps.egyptianwriter.preferences.backend;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.blueapps.egyptianwriter.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class PreferencesManager {

    // Constants
    private static final String TAG = "PreferencesManager";
    public static final String DATA_TYPE_STRING = "string";
    public static final String DATA_TYPE_BOOLEAN = "bool";

    public static void build(Context context){
        // Prepare xml data
        XmlResourceParser preferencesDataParser = context.getResources().getXml(R.xml.preferences);

        PreferencesSingleton singleton = PreferencesSingleton.getInstance();
        PreferencesHelper helper = new PreferencesHelper(singleton.getDataStore(context));

        try {

            preferencesDataParser.next();
            int eventType = preferencesDataParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                // begin document
                if (eventType == XmlPullParser.START_DOCUMENT){
                    Log.d(TAG, "Begin Document");
                }
                // begin text
                else if (eventType == XmlPullParser.START_TAG) {
                    String tagName = preferencesDataParser.getName();
                    if (tagName.equals("element")){

                        String strKey = preferencesDataParser.getAttributeValue(null, "key");
                        String strDataType = preferencesDataParser.getAttributeValue(null, "datatype");
                        String strDefault = preferencesDataParser.getAttributeValue(null, "default");

                        // Save structure
                        if (strDataType.equals(DATA_TYPE_STRING)) {
                            helper.putString(strKey, strDefault);
                        } else if (strDataType.equals(DATA_TYPE_BOOLEAN)) {
                            boolean boolDefault = Boolean.parseBoolean(strDefault);
                            helper.getBoolean(strKey, boolDefault);
                        }

                    }
                }
                eventType = preferencesDataParser.next();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    public static String getString(Context context, String key){
        build(context);
        return "";
    }

}
