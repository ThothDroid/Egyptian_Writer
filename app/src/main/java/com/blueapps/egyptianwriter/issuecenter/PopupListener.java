package com.blueapps.egyptianwriter.issuecenter;

public interface PopupListener {

    void OnCancel();

    void OnSelected(String info, String name);

    void OnConfirmed(String info, String name);

}
