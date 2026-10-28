package com.blueapps.groupeditor;

public class Box {

    private final Space highSpace;
    private final Space squareSpace;
    private final Space wideSpace;

    public Box(Space highSpace, Space squareSpace, Space wideSpace){
        this.highSpace = highSpace;
        this.squareSpace = squareSpace;
        this.wideSpace = wideSpace;
    }

    public Box(boolean firstBox){
        this.highSpace = new Space(firstBox, Space.TYPE_HIGH);
        this.squareSpace = new Space(firstBox, Space.TYPE_SQUARE);
        this.wideSpace = new Space(firstBox, Space.TYPE_WIDE);
    }

    public Space getHighSpace() {
        return highSpace;
    }

    public Space getSquareSpace() {
        return squareSpace;
    }

    public Space getWideSpace() {
        return wideSpace;
    }
}
