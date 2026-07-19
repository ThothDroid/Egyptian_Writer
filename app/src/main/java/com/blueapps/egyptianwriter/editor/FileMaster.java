package com.blueapps.egyptianwriter.editor;

import android.content.Context;

import java.io.File;

public class FileMaster {

    protected final File file;
    protected final File path;
    protected final Context context;

    // For utility reasons I want to keep both constructors
    @SuppressWarnings("unused")
    public FileMaster(Context context, File file, String dir){
        this.context = context;
        this.path = new File(context.getFilesDir() + dir);
        this.file = file;
    }

    public FileMaster(Context context, String filename, String dir){
        this.context = context;
        this.path = new File(context.getFilesDir() + dir);
        this.file = new File(path, filename);
    }

}
