package com.blueapps.egyptianwriter.editor.document;

import org.w3c.dom.Document;

public interface FileListener {

    void onGlyphXChanged(Document GlyphX);

    void onMdCChanged(String mdc);

}
