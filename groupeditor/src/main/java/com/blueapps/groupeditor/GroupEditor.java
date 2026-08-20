package com.blueapps.groupeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blueapps.signprovider.SignProvider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GroupEditor extends View {

    private static final String TAG = "GroupEditor";

    private SignProvider signProvider;
    private Paint fillPaint;
    private Paint borderPaint;

    // Values
    private Group group;

    public GroupEditor(Context context) {
        super(context);
        constructor();
    }

    public GroupEditor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        constructor();
    }

    public GroupEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        constructor();
    }

    public GroupEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        constructor();
    }

    private void constructor(){
        signProvider = new SignProvider(getContext());
        fillPaint = new Paint();
        fillPaint.setStrokeCap(Paint.Cap.ROUND);
        fillPaint.setStyle(Paint.Style.FILL);
        borderPaint = new Paint();
        borderPaint.setStrokeCap(Paint.Cap.ROUND);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    public void init(String signId) throws XmlPullParserException, IOException {
        // get Sign
        Drawable drawable = signProvider.getSign(signId);
        Rect bound = moveSign(drawable, getHeight()).getBounds();

        this.group = new Group(getHeight(), new RectF(bound), signId);
        this.invalidate();
    }

    public void init(Group group){
        this.group = group;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = widthSize;
        } else {
            //Be whatever you want
            width = 100;
        }

        int desiredHeight = width / 2;

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        try {

            if (group == null){
                init("#");
            }

            // get Sign
            Drawable sign = signProvider.getSign(group.getSignId());
            RectF bound = group.getSignBound();
            sign.setBounds((int) bound.left, (int) bound.top, (int) bound.right, (int) bound.bottom);

            // Draw background
            fillPaint.setColor(getResources().getColor(R.color.l_group_view_background, getContext().getTheme()));
            canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);

            fillPaint.setColor(getResources().getColor(R.color.l_group_view_background_more, getContext().getTheme()));
            canvas.drawRect(0, 0, bound.left, getHeight(), fillPaint);
            canvas.drawRect(bound.right, 0, getWidth(), getHeight(), fillPaint);

            sign.draw(canvas);

            // draw boxes
            drawBox(canvas, group.getFirstBox(), true);
            drawBox(canvas, group.getSecondBox(), false);

        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    private Drawable moveSign(Drawable drawable, int rootHeight) {
        // create drawable
        Rect bound = new Rect(0, 0, (int) getDrawableWidth(drawable), (int) getDrawableHeight(drawable));

        // scale sign
        float ratio = (float) bound.width() / (float) bound.height();
        int originalHeight = bound.bottom;
        int originalWidth = bound.right;
        if (originalHeight >= 1000){
            bound.bottom = rootHeight;
        } else {
            bound.bottom = (int) (rootHeight * ((float) originalHeight / 1000));
        }
        bound.right = (int) (ratio * bound.bottom);
        if (originalWidth >= 1000){
            bound.right = rootHeight;
            bound.bottom = (int) ((float) bound.right / ratio);
        }

        // center sign vertically
        int height = bound.height();
        int ty = (getHeight() / 2) - (height / 2);
        bound.top = bound.top + ty;
        bound.bottom = bound.bottom + ty;

        // center sign horizontally
        int width = bound.width();
        int tx = (getWidth() / 2) - (width / 2);
        bound.left = bound.left + tx;
        bound.right = bound.right + tx;

        drawable.setBounds(bound);
        return drawable;
    }

    private void drawBox(Canvas canvas, Box box, boolean firstBox){

        if (firstBox){
            fillPaint.setColor(getResources().getColor(R.color.l_group_view_first_box_border, getContext().getTheme()));
        } else {
            fillPaint.setColor(getResources().getColor(R.color.l_group_view_second_box_border, getContext().getTheme()));
        }

        // Draw highSpace
        Space highSpace = box.getHighSpace();
        //

    }

    private float getDrawableWidth(Drawable drawable){
        float width = drawable.getIntrinsicWidth();
        float density = getContext().getResources().getDisplayMetrics().density;

        width = width / density;

        Log.i(TAG, "Density: " + density);

        return width;
    }

    private float getDrawableHeight(Drawable drawable){
        float height = drawable.getIntrinsicHeight();
        float density = getContext().getResources().getDisplayMetrics().density;

        height = height / density;

        Log.i(TAG, "Density: " + density);

        return height;
    }
}
