package com.blueapps.egyptianwriter.dashboard.filegrid;

public class FileGridData {

    private String title;
    private String shortTitle;
    private String filename;

    public FileGridData(String title, String filename) {
        this.title = title;
        /*if (title.length() > 20) {
            this.shortTitle = title.substring(0, 20) + "…";
        } else {*/
            this.shortTitle = title;
        //}
        this.filename = filename;
    }

    public String getShortTitle(){
        return shortTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
