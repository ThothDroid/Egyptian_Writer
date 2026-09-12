package com.blueapps.egyptianwriter.export;

import static com.blueapps.egyptianwriter.export.ExportSettingsFragment.FILE_TYPE_EWDOC;

public class ExportProperty {

    private int fileType = FILE_TYPE_EWDOC;

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
