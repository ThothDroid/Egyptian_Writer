package com.blueapps.groupeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blueapps.glpyhconverter.GlyphConverter;
import com.blueapps.maat.BoundCalculation;
import com.blueapps.maat.BoundProperty;
import com.blueapps.maat.ValuePair;
import com.blueapps.signprovider.SignProvider;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class GroupEditor extends View {

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
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        try {

            int height = getWidth() / 2;

            // Draw background
            paint.setColor(getResources().getColor(R.color.l_group_view_background, getContext().getTheme()));
            canvas.drawRect(new Rect(0, 0, getWidth(), height), paint);
            paint.setColor(getResources().getColor(R.color.l_group_view_background_more, getContext().getTheme()));
            canvas.drawRect(new Rect(0, 0, getWidth()/4, height), paint);
            canvas.drawRect(new Rect((getWidth()/4)*3, 0, getWidth(), height), paint);

            drawSign(canvas, getWidth()/2, getWidth()/4);

        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawSign(Canvas canvas, int rootHeight, int left) throws XmlPullParserException, IOException {
        // get Sign
        Drawable drawable = signProvider.getSign(signId);

        // Get Sign as GlyphX
        if (signId.isEmpty()){
            signId = "none";
        }
        Document glyphX = GlyphConverter.convertToGlyphXDocument(signId);

        // Get Bound
        BoundCalculation boundCalculation = new BoundCalculation(glyphX);
        ArrayList<String> ids = boundCalculation.getIds(false, false);
        BoundProperty property = new BoundProperty(left, 0, rootHeight,
                BoundProperty.VERTICAL_ORIENTATION_MIDDLE, BoundProperty.WRITING_DIRECTION_LTR, BoundProperty.WRITING_LAYOUT_LINES,
                false, 0, 0,
                0, 0,
                0, 0,
                0, 0);
        ArrayList<ValuePair<Float, Float>> dimensions = new ArrayList<>();
        dimensions.add(new ValuePair<>((float) drawable.getIntrinsicWidth(), (float) drawable.getIntrinsicHeight()));
        ArrayList<Rect> bounds = boundCalculation.getBounds(dimensions, property);
        Rect bound = bounds.get(0);

        drawable.setBounds(bound);
        drawable.draw(canvas);
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
