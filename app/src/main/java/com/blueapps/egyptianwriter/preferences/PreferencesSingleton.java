package com.blueapps.egyptianwriter.preferences;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

public class PreferencesSingleton {

    RxDataStore<Preferences> dataStore;
    private static final PreferencesSingleton ourInstance = new PreferencesSingleton();

    // Constants
    public static final String NAME_PREFERENCES_DATA_STORE = "preferencesDataStore";

    private PreferencesSingleton(){
        // Block constructor
    }

    public static PreferencesSingleton getInstance(){
        return ourInstance;
    }

    public RxDataStore<Preferences> getDataStore(Context context) {
        if (dataStore == null){
            dataStore = new RxPreferenceDataStoreBuilder(context, NAME_PREFERENCES_DATA_STORE).build();
        }
        return dataStore;
    }

    public void setDataStore(RxDataStore<Preferences> dataStore) {
        this.dataStore = dataStore;
    }
}
