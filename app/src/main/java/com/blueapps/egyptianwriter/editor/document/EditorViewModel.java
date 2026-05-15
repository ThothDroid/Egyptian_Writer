package com.blueapps.egyptianwriter.editor.document;

import androidx.lifecycle.ViewModel;

public class EditorViewModel extends ViewModel {

    // Constants
    public static final boolean MODE_READ = true;
    public static final boolean MODE_WRITE = false;

    private boolean mode = MODE_READ;
    private FileMaster fileMaster = null;
    private boolean noIssue = true;


    // Getter and Setter
    public boolean getMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public boolean isNoIssue(){
        return noIssue;
    }

    public void setNoIssue(boolean b){
        this.noIssue = b;
    }

    public FileMaster getFileMaster() {
        return fileMaster;
    }

    public void setFileMaster(FileMaster fileMaster) {
        if (this.fileMaster == null) {
            this.fileMaster = fileMaster;
            this.fileMaster.extractData();
        }
    }
}
