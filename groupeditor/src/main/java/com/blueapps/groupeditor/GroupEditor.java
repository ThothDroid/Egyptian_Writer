package com.blueapps.groupeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blueapps.signprovider.SignProvider;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GroupEditor extends View {

    SignProvider signProvider;

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
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // get Sign
        try {
            Drawable drawable = signProvider.getSign(signId);
            drawable.draw(canvas);
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
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
