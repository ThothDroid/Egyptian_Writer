package com.blueapps.groupeditor;

import android.graphics.Rect;
import android.graphics.RectF;

public class Space {

    /**
     * Vertical coordinates range from 0 to 1000 and horizontal coordinates range from 0 to 2000
     */
    private int left;
    private int top;
    private int size;
    private final int type;

    // Constants
    public static final int TYPE_HIGH = 0;
    public static final int TYPE_SQUARE = 1;
    public static final int TYPE_WIDE = 2;

    public Space(int left, int top, int size, int type){
        this.left = left;
        this.top = top;
        this.size = size;
        this.type = type;
    }

    public Space(boolean firstBox, int type){
        if (firstBox) {
            this.left = 500;
            if (type == TYPE_HIGH) {
                this.top = 500;
            } else if (type == TYPE_SQUARE) {
                this.top = 666;
            } else {
                this.top = 750;
            }
        } else {
            if (type == TYPE_WIDE) {
                this.left = 1000;
            } else if (type == TYPE_SQUARE) {
                this.left = 1166;
            } else {
                this.left = 1250;
            }
            this.top = 0;
        }
        if (type == TYPE_SQUARE){
            this.size = 333;
        } else {
            this.size = 250;
        }
        this.type = type;
    }

    public RectF getViewRectF(float generalHeight){
        Rect rect = getRect();

        float l = ((float) rect.left / 1000) * generalHeight;
        float t = ((float) rect.top / 1000) * generalHeight;
        float r = ((float) rect.right / 1000) * generalHeight;
        float b = ((float) rect.bottom / 1000) * generalHeight;

        return new RectF(l, t, r, b);
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

        return new Rect(this.left, this.top, (int) (this.left + sx), (int) (this.top + sy));
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
