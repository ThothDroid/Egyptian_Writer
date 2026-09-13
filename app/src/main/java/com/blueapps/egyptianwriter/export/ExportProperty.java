package com.blueapps.egyptianwriter.export;

import static com.blueapps.egyptianwriter.export.ExportSettingsFragment.FILE_TYPE_EWDOC;

public class ExportProperty {

    private int fileType = FILE_TYPE_EWDOC;

    private String title = "";
    private boolean b_title = true;

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBTitle() {
        return b_title;
    }

    public void setBTitle(boolean b_title) {
        this.b_title = b_title;
    }
}
