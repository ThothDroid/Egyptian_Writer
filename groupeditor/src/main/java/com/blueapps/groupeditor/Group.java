package com.blueapps.groupeditor;

import android.graphics.RectF;

public class Group {

    private Box firstBox;
    private Box secondBox;
    private RectF signBound;
    private String signId;

    public Group(Box firstBox, Box secondBox, RectF signBound, String signId){
        this.firstBox = firstBox;
        this.secondBox = secondBox;
        this.signBound = signBound;
        this.signId = signId;
    }

    public Group(float generalHeight, RectF signBound, String signId){
        this.firstBox = new Box(generalHeight);
        this.secondBox = new Box(generalHeight);
        this.signBound = signBound;
        this.signId = signId;
    }

    public Box getFirstBox() {
        return firstBox;
    }

    public void setFirstBox(Box firstBox) {
        this.firstBox = firstBox;
    }

    public Box getSecondBox() {
        return secondBox;
    }

    public void setSecondBox(Box secondBox) {
        this.secondBox = secondBox;
    }

    public RectF getSignBound() {
        return signBound;
    }

    public void setSignBound(RectF signBound) {
        this.signBound = signBound;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }
}
