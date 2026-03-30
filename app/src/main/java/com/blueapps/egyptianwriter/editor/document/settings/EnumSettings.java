package com.blueapps.egyptianwriter.editor.document.settings;

import com.blueapps.egyptianwriter.CheckableImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EnumSettings {

    private final ArrayList<CheckableImageButton> imageButtons;
    private final ArrayList<EnumListener> listeners = new ArrayList<>();
    private boolean lockListener = false;

    public EnumSettings(ArrayList<CheckableImageButton> imageButtons){
        this.imageButtons = imageButtons;

        init();
    }

    public void init(){
        for (CheckableImageButton imageButton: imageButtons){
            imageButton.setOnCheckedChangeListener((button, isChecked) -> {

                if (!lockListener) {
                    lockListener = true;
                    for (CheckableImageButton imageButton1 : imageButtons) {
                        if (!Objects.equals(imageButton1, imageButton)) {
                            imageButton1.setChecked(false);
                            imageButton1.setClickable(true);
                        }
                    }
                    for (EnumListener listener: listeners){
                        listener.OnSelected(imageButtons.indexOf(imageButton));
                    }
                    imageButton.setClickable(false);
                    lockListener = false;
                }

            });
        }
    }

    public void addListener(EnumListener listener){
        listeners.add(listener);
    }

    public interface EnumListener{

        void OnSelected(int index);

    }

}
