package com.blueapps.groupeditor;

import android.graphics.Rect;
import android.graphics.RectF;

public class Space {

    private float left;
    private float top;
    private float size;
    private int type = TYPE_SQUARE;

    // Constants
    public static final int TYPE_HIGH = 0;
    public static final int TYPE_SQUARE = 1;
    public static final int TYPE_WIDE = 2;

    public Space(float left, float top, float size, int type){
        this.left = left;
        this.top = top;
        this.size = size;
        this.type = type;
    }

    public Space(float generalHeight){
        this.left = 0;
        this.top = 0;
        this.size = generalHeight / 2;
    }

    public RectF getRectF(){
        float sx;
        float sy;

        if (type == TYPE_HIGH){
            sx = size;
            sy = size * 2;
        } else if (type == TYPE_WIDE) {
            sx = size * 2;
            sy = size;
        } else {
            sx = size;
            sy = size;
        }

        return new RectF(this.left, this.top, this.left + sx, this.top + sy);
    }

    public Rect getRect(){
        float sx;
        float sy;

        if (type == TYPE_HIGH){
            sx = size;
            sy = size * 2;
        } else if (type == TYPE_WIDE) {
            sx = size * 2;
            sy = size;
        } else {
            sx = size;
            sy = size;
        }

        return new Rect((int) this.left, (int) this.top, (int) (this.left + sx), (int) (this.top + sy));
    }

}
