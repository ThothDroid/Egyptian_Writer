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
import com.blueapps.maat.bounds.SimpleBound;
import com.blueapps.signprovider.SignProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

            drawSign(canvas, getHeight(), getWidth()/4);

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

    private void drawSign(Canvas canvas, int rootHeight, int left) throws XmlPullParserException, IOException, ParserConfigurationException {
        // get Sign
        Drawable drawable = signProvider.getSign(signId);

        // Create Element
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element element = document.createElement("sign");
        element.setAttribute("id", signId);

        // Calculate Bound
        SimpleBound bound = new SimpleBound(element);
        bound.getIds(false);
        BoundProperty property = new BoundProperty(0,0,rootHeight, 1,0,0,false,0,0,0,0,0,0,0,0);
        ArrayList<ValuePair<Float, Float>> dimension = new ArrayList<>();
        dimension.add(new ValuePair<>((float) drawable.getIntrinsicWidth(), (float) drawable.getIntrinsicHeight()));
        Rect rectBound = bound.getBound(property, dimension);

        drawable.setBounds(rectBound);
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
