package com.blueapps.egyptianwriter.editor.document;

import androidx.lifecycle.ViewModel;

public class EditorViewModel extends ViewModel {

    // Constants
    public static final boolean MODE_READ = true;
    public static final boolean MODE_WRITE = false;

    private boolean mode = MODE_READ;
    private DocFileMaster docFileMaster = null;


    // Getter and Setter
    public boolean getMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public DocFileMaster getFileMaster() {
        return docFileMaster;
    }

    public void setFileMaster(DocFileMaster docFileMaster) {
        if (this.docFileMaster == null) {
            this.docFileMaster = docFileMaster;
            this.docFileMaster.extractData();
        }
    }
}
