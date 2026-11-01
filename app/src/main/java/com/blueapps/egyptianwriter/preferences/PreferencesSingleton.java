package com.blueapps.egyptianwriter.preferences;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;

public class PreferencesSingleton {

    RxDataStore<Preferences> dataStore;
    private static final PreferencesSingleton ourInstance = new PreferencesSingleton();

    private PreferencesSingleton(){
        // Block constructor
    }

    public static PreferencesSingleton getInstance(){
        return ourInstance;
    }

    public RxDataStore<Preferences> getDataStore() {
        return dataStore;
    }

    public void setDataStore(RxDataStore<Preferences> dataStore) {
        this.dataStore = dataStore;
    }
}
