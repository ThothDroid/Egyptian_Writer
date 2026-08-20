package com.blueapps.groupeditor;

public class Box {

    private Space highSpace;
    private Space squareSpace;
    private Space wideSpace;

    public Box(Space highSpace, Space squareSpace, Space wideSpace){
        this.highSpace = highSpace;
        this.squareSpace = squareSpace;
        this.wideSpace = wideSpace;
    }

    public Box(float generalHeight){
        this.highSpace = new Space(generalHeight);
        this.squareSpace = new Space(generalHeight);
        this.wideSpace = new Space(generalHeight);
    }

}
