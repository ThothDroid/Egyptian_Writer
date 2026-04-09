package com.blueapps.egyptianwriter.dashboard;

import android.view.MenuItem;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.documents.DocumentFragment;
import com.blueapps.egyptianwriter.dashboard.signlist.SignListFragment;
import com.blueapps.egyptianwriter.dashboard.vocab.VocabFragment;
import com.blueapps.egyptianwriter.info.InfoActivity;
import com.google.android.material.navigation.NavigationView;

import org.apache.commons.lang3.ArrayUtils;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<UiData> uiData = new MutableLiveData<>(new UiData(DocumentFragment.class));
    private int menuItem = R.id.menu_item_documents;

    private static final int[] navigatorSelectableItems = {R.id.menu_item_documents, R.id.menu_item_sign_list,
            R.id.menu_item_vocab};

    public LiveData<UiData> getUiState(){
        return uiData;
    }

    @SuppressWarnings("rawtypes")
    public Class onNavigationSelectionChange(MenuItem menuItem, NavigationView navigationView, DrawerLayout drawerLayout){
        int id = menuItem.getItemId();
        this.menuItem = id;

        if (ArrayUtils.contains(navigatorSelectableItems, id)) {
            navigationView.setCheckedItem(id);

            UiData data = uiData.getValue();

            if (id == R.id.menu_item_documents) {
                data.setSelectedFragment(DocumentFragment.class);
            } else if (id == R.id.menu_item_sign_list) {
                data.setSelectedFragment(SignListFragment.class);
            } else if (id == R.id.menu_item_vocab) {
                data.setSelectedFragment(VocabFragment.class);
            }

            uiData.setValue(data);
            drawerLayout.close();

        } else {

            if (id == R.id.menu_item_about){
                return InfoActivity.class;
            }

        }

        return null;
    }

    public void onBackPressed(DrawerLayout drawerLayout, NavigationView navigationView){
        if (menuItem == R.id.menu_item_documents){
            drawerLayout.open();
            return;
        }

        if(ArrayUtils.contains(navigatorSelectableItems, menuItem)){
            UiData data = uiData.getValue();

            data.setSelectedFragment(DocumentFragment.class);
            menuItem = R.id.menu_item_documents;
            navigationView.setCheckedItem(menuItem);

            uiData.setValue(data);
        }
    }

}
