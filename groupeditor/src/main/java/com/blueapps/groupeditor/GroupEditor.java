package com.blueapps.groupeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blueapps.signprovider.SignProvider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class GroupEditor extends View {

    private static final String TAG = "GroupEditor";

    private SignProvider signProvider;
    private Paint paint;

    // Values
    private String signId = "";

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
        paint = new Paint();
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

            drawSign(canvas, getHeight());

            // Draw background
            /*paint.setColor(getResources().getColor(R.color.l_group_view_background, getContext().getTheme()));
            canvas.drawRect(new Rect(0, 0, getWidth(), height), paint);
            paint.setColor(getResources().getColor(R.color.l_group_view_background_more, getContext().getTheme()));
            canvas.drawRect(new Rect(0, 0, getWidth()/4, height), paint);
            canvas.drawRect(new Rect((getWidth()/4)*3, 0, getWidth(), height), paint);*/

        } catch (IOException | XmlPullParserException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawSign(Canvas canvas, int rootHeight) throws XmlPullParserException, IOException, ParserConfigurationException {
        // get Sign
        Drawable drawable = signProvider.getSign(signId);

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
        drawable.draw(canvas);
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

    // Getter and Setter
    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
        this.invalidate();
    }
}
