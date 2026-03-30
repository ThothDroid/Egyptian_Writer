package com.blueapps.egyptianwriter.dashboard.filegrid;

public interface FileListener {

    void OnDeleteFile(String name);
    void OnExportFile(String name);
    void OnOpenFile(String name);

}
