package com.blueapps.egyptianwriter.editor.document;

import com.blueapps.egyptianwriter.CheckableImageButton;

import java.util.ArrayList;
import java.util.Objects;

public class ImageButtonGroup implements CheckableImageButton.OnCheckedChangeListener {

    private final ArrayList<CheckableImageButton> imageButtons = new ArrayList<>();
    private final ArrayList<ImageButtonListener> listeners = new ArrayList<>();
    private int position = -1;

    public ImageButtonGroup(){

    }

    public void addImageButton(CheckableImageButton imageButton){
        this.imageButtons.add(imageButton);
        imageButton.setOnCheckedChangeListener(this);
    }

    public int getPosition(){
        return position;
    }

    public void addImageButtonListener(ImageButtonListener listener){
        this.listeners.add(listener);
    }

    @Override
    public void onCheckedChanged(CheckableImageButton button, boolean isChecked) {
        if (isChecked){
            button.setClickable(false);
            position = -1;
            int counter = 0;
            for (CheckableImageButton imageButton: imageButtons){
                if (!Objects.equals(imageButton, button)){
                    imageButton.setClickable(true);
                    imageButton.setChecked(false);
                } else {
                    position = counter;
                }
                counter++;
            }
            for (ImageButtonListener listener: listeners){
                listener.OnPositionChanges(position);
            }
        }
    }
}
