package com.blueapps.groupeditor;

import android.graphics.Rect;
import android.graphics.RectF;

public class Space {

    private final float left;
    private final float top;
    private final float size;
    private final int type;

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

    public Space(float generalHeight, int type){
        this.left = 0;
        this.top = 0;
        this.size = generalHeight / 2;
        this.type = type;
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
