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
    public static final String KEY_I_EXIST = "to be or not to be";
    public static final String DATA_TYPE_STRING = "string";
    public static final String DATA_TYPE_BOOLEAN = "bool";

    /**
     * Reads preferences.xml and copies all keys with their default value to DataStore, If the DataStore is empty
     * @param context The Context needed for accessing Resources
     */

    public static void build(Context context){
        if (!preferencesSaved(context)) {
            // Prepare xml data
            XmlResourceParser preferencesDataParser = context.getResources().getXml(R.xml.preferences);

            PreferencesSingleton singleton = PreferencesSingleton.getInstance();
            PreferencesHelper helper = new PreferencesHelper(singleton.getDataStore(context));

            try {

                preferencesDataParser.next();
                int eventType = preferencesDataParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    // begin document
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        Log.d(TAG, "Begin Document");
                    }
                    // begin text
                    else if (eventType == XmlPullParser.START_TAG) {
                        String tagName = preferencesDataParser.getName();
                        if (tagName.equals("element")) {

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

                // Save information of existence
                helper.putBoolean(KEY_I_EXIST, true);

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the DataStore is empty.
     * @param context Context needed to build a DataStore, if there is nothing
     * @return true if the DataStore isn't empty
     */
    public static boolean preferencesSaved(Context context){
        PreferencesSingleton singleton = PreferencesSingleton.getInstance();
        PreferencesHelper helper = new PreferencesHelper(singleton.getDataStore(context));

        return helper.getBoolean(KEY_I_EXIST, false);
    }


    /**
     * Get a String by a given key from the DataStore, builds a new DataStore with defaults, if it is empty
     * @param context Context needed to build new DataStore
     * @param key The key which is defined in the preferences.xml file
     * @return The value of the preference or "null", if the key doesn't exist, or if the key is for another datatype
     */
    public static String getString(Context context, String key){
        build(context);

        PreferencesSingleton singleton = PreferencesSingleton.getInstance();
        PreferencesHelper helper = new PreferencesHelper(singleton.getDataStore(context));

        return helper.getString(key, "null");
    }

    /**
     * Get a Boolean by a given key from the DataStore, builds a new DataStore with defaults, if it is empty
     * @param context Context needed to build new DataStore
     * @param key The key which is defined in the preferences.xml file
     * @return The value of the preference or false, if the key doesn't exist, or if the key is for another datatype
     */
    public static boolean getBoolean(Context context, String key){
        build(context);

        PreferencesSingleton singleton = PreferencesSingleton.getInstance();
        PreferencesHelper helper = new PreferencesHelper(singleton.getDataStore(context));

        return helper.getBoolean(key, false);
    }

}
