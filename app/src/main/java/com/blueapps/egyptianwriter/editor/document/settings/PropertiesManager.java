package com.blueapps.egyptianwriter.editor.document.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PropertiesManager extends ViewModel {

    // Properties
    private final MutableLiveData<Integer> textSize = new MutableLiveData<>(40);
    private final MutableLiveData<Integer> writingLayout = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> verticalOrientation = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> writingDirection = new MutableLiveData<>(0);


    public LiveData<Integer> getTextSize(){
        return textSize;
    }

    public void setTextSize(int textSize){
        this.textSize.postValue(textSize);
    }

    public int increaseTextSize(){
        int textSize = getTextSize().getValue();
        if (textSize < 999) {
            textSize++;
        }
        return textSize;
    }

    public int decreaseTextSize(){
        int textSize = getTextSize().getValue();
        if (textSize > 1) {
            textSize--;
        }
        return textSize;
    }

    public LiveData<Integer> getWritingLayout() {
        return writingLayout;
    }

    public void setWritingLayout(int writingLayout){
        this.writingLayout.postValue(writingLayout);
    }

    public LiveData<Integer> getVerticalOrientation() {
        return verticalOrientation;
    }

    public void setVerticalOrientation(int verticalOrientation){
        this.verticalOrientation.postValue(verticalOrientation);
    }

    public LiveData<Integer> getWritingDirection() {
        return writingDirection;
    }

    public void setWritingDirection(int writingDirection){
        this.writingDirection.postValue(writingDirection);
    }

}
