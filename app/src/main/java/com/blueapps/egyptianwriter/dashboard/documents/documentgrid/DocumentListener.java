package com.blueapps.egyptianwriter.dashboard.documents.documentgrid;

public interface DocumentListener {

    void OnDeleteDocument(String name);
    void OnExportDocument(String name);
    void OnOpenDocument(String name);

}
