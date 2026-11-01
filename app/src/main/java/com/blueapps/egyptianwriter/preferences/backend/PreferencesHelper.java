package com.blueapps.egyptianwriter.preferences.backend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import java.util.Collections;
import java.util.Map;

import io.reactivex.Single;

public class PreferencesHelper {

    private final RxDataStore<Preferences> dataStore;
    private final Preferences error = new Preferences() {
        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }

        @Nullable
        @Override
        public <T> T get(@NonNull Key<T> key) {
            return null;
        }

        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return Collections.emptyMap();
        }
    };

    public PreferencesHelper(RxDataStore<Preferences> dataStore){
        this.dataStore = dataStore;
    }

    public boolean putString(Object ignored, String key, String value){ // ignored is used to increase parameter size. Otherwise, putString() and getString() are easily inverted
        Preferences.Key<String> KEY = PreferencesKeys.stringKey(key);
        Single<Preferences> updateResult = dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(error);

        return updateResult.blockingGet() != error;
    }

    public String getString(String key, @NonNull String defaultValue){
        Preferences.Key<String> KEY = PreferencesKeys.stringKey(key);
        Single<String> value = dataStore.data().firstOrError().map(preferences -> preferences.get(KEY)).onErrorReturnItem(defaultValue);
        return value.blockingGet();
    }

    public boolean putBoolean(Object ignored, String key, boolean value){
        Preferences.Key<Boolean> KEY = PreferencesKeys.booleanKey(key);
        Single<Preferences> updateResult = dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(error);

        return updateResult.blockingGet() != error;
    }

    public boolean getBoolean(String key, boolean defaultValue){
        Preferences.Key<Boolean> KEY = PreferencesKeys.booleanKey(key);
        Single<Boolean> value = dataStore.data().firstOrError().map(preferences -> preferences.get(KEY)).onErrorReturnItem(defaultValue);
        return value.blockingGet();
    }

}
