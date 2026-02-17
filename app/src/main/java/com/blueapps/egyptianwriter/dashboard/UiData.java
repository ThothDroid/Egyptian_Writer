package com.blueapps.egyptianwriter.dashboard;

@SuppressWarnings("rawtypes")
public class UiData {

    private Class selectedFragment;

    public UiData(Class selectedFragment){
        this.selectedFragment = selectedFragment;
    }

    public Class getSelectedFragment() {
        return selectedFragment;
    }

    public void setSelectedFragment(Class selectedFragment) {
        this.selectedFragment = selectedFragment;
    }
}
