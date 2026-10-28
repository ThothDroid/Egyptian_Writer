package com.blueapps.groupeditor;

import android.graphics.Rect;
import android.graphics.RectF;

public class Space {

    private final int left;
    private final int top;
    private final int size;
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
            this.left = 1000 / 2;
            if (type == TYPE_HIGH) {
                this.top = 1000 / 2;
            } else if (type == TYPE_SQUARE) {
                this.top = (1000 / 3) * 2;
            } else {
                this.top = (1000 / 4) * 3;
            }
        } else {
            if (type == TYPE_WIDE) {
                this.left = 1000;
            } else if (type == TYPE_SQUARE) {
                this.left = (1000 / 6) * 7;
            } else {
                this.left = (1000 / 4) * 5;
            }
            this.top = 0;
        }
        if (type == TYPE_SQUARE){
            this.size = 1000 / 3;
        } else {
            this.size = 1000 / 4;
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

}
