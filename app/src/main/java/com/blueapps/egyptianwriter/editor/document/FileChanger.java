package com.blueapps.egyptianwriter.editor.document;

import static com.blueapps.egyptianwriter.editor.document.FileMaster.DocumentToString;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileChanger implements Runnable{

    private final File file;
    private final Document newDoc;
    private String docString;

    public FileChanger(File file, Document newDoc){
        this.file = file;
        this.newDoc = newDoc;
        docString = DocumentToString(newDoc);
    }


    @Override
    public void run() {
        try {
            FileOutputStream overWrite = new FileOutputStream(file, false);
            overWrite.write(docString.getBytes());
            overWrite.flush();
            overWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
