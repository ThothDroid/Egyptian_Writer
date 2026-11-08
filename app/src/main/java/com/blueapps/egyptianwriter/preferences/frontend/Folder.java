package com.blueapps.egyptianwriter.preferences.frontend;

public class Folder {

    private int mode;
    private String title;

    // Constants
    public static final int MODE_TOP = 0;
    public static final int MODE_MIDDLE = 1;
    public static final int MODE_BOTTOM = 2;


    public Folder(int mode, String title) {
        this.mode = mode;
        this.title = title;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
