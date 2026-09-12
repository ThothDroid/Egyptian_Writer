package com.blueapps.egyptianwriter.export;

import java.io.Serializable;

public interface FileResultListener extends Serializable {

    void onShare();

    void onSave();

}
