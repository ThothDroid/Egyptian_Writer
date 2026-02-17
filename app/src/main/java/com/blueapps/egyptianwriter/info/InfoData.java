package com.blueapps.egyptianwriter.info;

public class InfoData {

    private int mode;
    private boolean top;

    private String title;
    private String subtitle;

    // Constants
    public static final int MODE_SINGLE_ITEM = 0;
    public static final int MODE_TOP_ITEM = 1;
    public static final int MODE_BOTTOM_ITEM = 2;
    public static final int MODE_MIDDLE_ITEM = 3;
    public static final int MODE_GROUP_TITLE_ITEM = 4;

    public InfoData(int mode, boolean top, String title, String subtitle){
        this.mode = mode;
        this.top = top;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isGroup(){
        return mode == MODE_TOP_ITEM || mode == MODE_MIDDLE_ITEM || mode == MODE_BOTTOM_ITEM;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
